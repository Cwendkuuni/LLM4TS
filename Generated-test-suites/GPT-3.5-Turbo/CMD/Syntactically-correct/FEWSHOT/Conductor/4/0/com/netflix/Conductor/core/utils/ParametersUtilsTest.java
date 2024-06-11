package com.netflix.conductor.core.utils;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(null); // Null parameter for objectMapper
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "taskId";
        assertNotNull(parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");
        String json = "{\"key1\":\"value1\"}";
        assertNotNull(parametersUtils.replace(input, json));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "variable:${key1}";
        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("key1", "value1"));
        assertNull(parametersUtils.replaceVariables(paramString, documentContext, "taskId"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();
        assertNotNull(parametersUtils.getWorkflowInput(workflowDef, inputParams));
    }

    // Add more test cases to cover all methods in ParametersUtils class

}