package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task123";

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "workflow.input.param2");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertEquals("value1", result.get("param1"));
        assertEquals("workflow.input.param2", result.get("param2"));
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input.param1}");
        input.put("key2", "${task.output.param2}");

        HashMap<String, Object> json = new HashMap<>();
        json.put("workflow", Map.of("input", Map.of("param1", "value1"), "output", Map.of("param2", "value2")));

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

        DocumentContext documentContext = JsonPath.parse("{}");
        String paramString = "{\"key\": ${workflow.input.param}}";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, "task123");

        assertEquals("{\"key\": null}", result.toString());
    }
}


// Add more test cases here

