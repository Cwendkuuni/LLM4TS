package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    ObjectMapper objectMapper;
    ParametersUtils parametersUtils;
    WorkflowModel workflowModel;
    TaskDef taskDef;
    WorkflowDef workflowDef;
   
   
    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
        workflowModel = mock(WorkflowModel.class);
        taskDef = mock(TaskDef.class);
        workflowDef = mock(WorkflowDef.class);
    }

    @Test
    public void testGetTaskInput() {
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(2);

        Map<String, Object> result = parametersUtils.getTaskInput(new HashMap<>(), workflowModel, taskDef, "1");
        assertTrue(result.isEmpty());

        //Additional test cases are needed for complete test coverage of getTaskInput method
    }

    @Test
    public void testReplace() {
        String inputPath = "${workflow.input.param1}";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param1", "value1");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", inputPath);
        
        Map<String, Object> result = parametersUtils.replace(inputParams, inputMap);
        assertEquals("value1", result.get("param1"));

        //Additional test cases are needed for complete test coverage of replace method
    }

    @Test
    public void testReplaceString() {
        Object result = parametersUtils.replace("${workflow.param1}");
        assertTrue(result instanceof String);

        //Additional test cases are needed for complete test coverage of replace method with string input
    }
    
    @Test
    public void testGetWorkflowInput() {
        when(workflowDef.getInputTemplate()).thenReturn(null);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(result);
        assertEquals("value1", result.get("param1"));

        //Additional test cases are needed for complete test coverage of getWorkflowInput method
    }
}