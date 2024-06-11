package com.netflix.conductor.core.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(null);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "value2");

        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "taskId";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("key1"));
        assertEquals("value2", taskInput.get("key2"));
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(null);

        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "value2");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals("value1", workflowInput.get("key1"));
        assertEquals("value2", workflowInput.get("key2"));
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(null);

        Map<String, Object> input = new HashMap<>();
        input.put("key1", "This is ${key2}");
        input.put("key2", "replacement");

        Object replaced = parametersUtils.replace(input);

        assertNotNull(replaced);
        assertTrue(replaced instanceof Map);

        Map<String, Object> replacedMap = (Map<String, Object>) replaced;
        assertEquals("This is replacement", replacedMap.get("key1"));
    }

}