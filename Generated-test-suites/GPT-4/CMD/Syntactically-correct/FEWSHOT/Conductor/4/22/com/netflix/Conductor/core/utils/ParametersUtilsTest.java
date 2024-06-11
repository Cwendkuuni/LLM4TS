package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);

        // Mock the environment
        EnvUtils = mock(EnvUtils.class);
        when(EnvUtils.isEnvironmentVariable("TEST_ENV_VAR")).thenReturn(true);
        when(EnvUtils.getSystemParametersValue("TEST_ENV_VAR", null)).thenReturn("Mock Env Value");
    }
  
    @Test
    public void testGetWorkflowInput() {        
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key1", "value1");
        
        workflowDef.setInputTemplate(inputTemplate);
        Map<String, Object> inputParams = new HashMap<>();
        
        inputParams = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("value1", inputParams.get("key1"));
    }

    @Test
    public void testReplaceWithParameterString() {
        String replacedValue = (String) parametersUtils.replace("${TEST_ENV_VAR}");
        assertEquals("Mock Env Value", replacedValue);
    }

    @Test
    public void testReplaceWithParameterMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "${TEST_ENV_VAR}");
        
        inputParams = parametersUtils.replace(inputParams, "${TEST_ENV_VAR}");

        assertEquals("Mock Env Value", inputParams.get("key1"));
    }
      
    // Add more tests to verify other functions
}