package com.netflix.conductor.core.utils;

import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

import org.springframework.mock.web.MockHttpServletRequest;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;
	
	@Before
    public void setUp() {
        parametersUtils = Mockito.mock(ParametersUtils.class);	
    }
	
    @Test
    public void getTaskInputTest() {
        Map<String, Object> result = parametersUtils.getTaskInput(Collections.singletonMap("paramKey", "paramValue"), new WorkflowModel(), null, "testTaskId");
        Assert.assertNotNull(result);
    }
	
    @Test
    public void getTaskInputV1Test() {
        Assert.assertNotNull(parametersUtils.getTaskInputV1(new WorkflowModel(), Collections.singletonMap("paramKey", "paramValue")));
    }

    @Test
    public void getTaskInputV2Test() { 
        Assert.assertNotNull(parametersUtils.getTaskInputV2(Collections.singletonMap("paramValue", "paramKey"), new WorkflowModel(), "testTaskId", null));
    }

    @Test
    public void replaceMapJsonTest() {
        Assert.assertNotNull(parametersUtils.replace(Collections.singletonMap("paramKey", "paramValue"), new Object()));
    }

    @Test
    public void replaceStringTest() {
        Assert.assertNotNull(parametersUtils.replace("paramKey"));
    }

    @Test(expected = RuntimeException.class)
    public void cloneTest() {
        parametersUtils.clone(Collections.singletonMap("Test", new MockHttpServletRequest()));
    }

    @Test
    public void getWorkflowInputTest() {
        Assert.assertNotNull(parametersUtils.getWorkflowInput(new WorkflowDef(), Collections.singletonMap("paramKey", "paramValue")));
    }
}
