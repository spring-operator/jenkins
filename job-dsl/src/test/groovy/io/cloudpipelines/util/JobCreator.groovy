package io.cloudpipelines.util

import groovy.transform.CompileStatic
import groovy.xml.XmlUtil
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.JobParent
import javaposse.jobdsl.dsl.MemoryJobManagement

/**
 * @author Marcin Grzejszczak
 */
@CompileStatic
trait JobCreator {

	JobParent createJobParent() {
		JobParent jp = new StubbedJobParent()
		JobManagement jm = new MemoryJobManagement()
		jp.setJm(jm)
		defaultStubbing(jm)
		return jp
	}

	String folderName() {
		return "default_pipeline"
	}

	void storeJobsAndViews(MemoryJobManagement jm) {
		File jobs = new File("build/${folderName()}/jobs/")
		File views = new File("build/${folderName()}/views/")
		jobs.mkdirs()
		views.mkdirs()
		jm.savedConfigs.each {
			new File(jobs, "${it.key}.xml").text = XmlUtil.serialize(it.value)
		}
		jm.savedViews.each {
			new File(views, "${it.key}.xml").text = XmlUtil.serialize(it.value)
		}
	}

	private void defaultStubbing(MemoryJobManagement jm) {
		jm.availableFiles['foo/pipeline.sh'] = JobCreator.getResource('/cloudpipelines-scripts/pipeline.sh').text
		jm.availableFiles['foo/build_and_upload.sh'] = JobCreator.getResource('/cloudpipelines-scripts/build_and_upload.sh').text
		jm.availableFiles['foo/build_api_compatibility_check.sh'] = JobCreator.getResource('/cloudpipelines-scripts/build_api_compatibility_check.sh').text
		jm.availableFiles['foo/test_deploy.sh'] = JobCreator.getResource('/cloudpipelines-scripts/test_deploy.sh').text
		jm.availableFiles['foo/test_smoke.sh'] = JobCreator.getResource('/cloudpipelines-scripts/test_smoke.sh').text
		jm.availableFiles['foo/test_rollback_deploy.sh'] = JobCreator.getResource('/cloudpipelines-scripts/test_rollback_deploy.sh').text
		jm.availableFiles['foo/test_rollback_smoke.sh'] = JobCreator.getResource('/cloudpipelines-scripts/test_rollback_smoke.sh').text
		jm.availableFiles['foo/stage_deploy.sh'] = JobCreator.getResource('/cloudpipelines-scripts/stage_deploy.sh').text
		jm.availableFiles['foo/stage_e2e.sh'] = JobCreator.getResource('/cloudpipelines-scripts/stage_e2e.sh').text
		jm.availableFiles['foo/prod_deploy.sh'] = JobCreator.getResource('/cloudpipelines-scripts/prod_deploy.sh').text
		jm.availableFiles['foo/prod_complete.sh'] = JobCreator.getResource('/cloudpipelines-scripts/prod_complete.sh').text
	}

	static class StubbedJobParent extends JobParent {
		@Override
		Object run() {
			return null
		}
	}
}
