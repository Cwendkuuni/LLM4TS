package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        assertNull(parametersUtils.getWorkflowInput(workflowDef, inputParams));

        inputParams.put("key","value");
        assertTrue(parametersUtils.getWorkflowInput(workflowDef, inputParams).size() != 0);
    }

    @Test
    public void testGetTaskInputV2_Exception() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        String taskId = "1";
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
	 
        Mockito.when(objectMapper.readValue(any(byte[].class), any(TypeReference.class))).thenThrow(IOException.class);
        assertThrows(RuntimeException.class, () -> parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition));
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key","value");
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
        Mockito.when(workflow.getWorkflowDefinition()).thenReturn(workflowDef);
        Mockito.when(workflowDef.getSchemaVersion()).thenReturn(2);

        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        String taskId = "1";
        assertTrue(parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId).size() != 0);

        Mockito.when(workflowDef.getSchemaVersion()).thenReturn(1);
        assertTrue(parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId).size() != 0);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = Mockito.mock(Map.class);
        Object json = Mockito.mock(Object.class);
        assertEquals(input, parametersUtils.replace(input, json));
    }

    @Test
    public void testReplace_String() {
        String paramString = "$$";
        assertEquals(paramString, parametersUtils.replace(paramString));
    }

}
