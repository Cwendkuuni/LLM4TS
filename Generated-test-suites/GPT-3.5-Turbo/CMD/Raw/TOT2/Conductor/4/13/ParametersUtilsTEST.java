
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
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "123";

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        assertNotNull(taskInput);
        // Add more assertions as needed
    }

    // Add more test cases for other methods in ParametersUtils class

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input.key1}");
        input.put("key2", "${task.input.key2}");
        input.put("key3", "someValue");

        Object replaced = parametersUtils.replace(input);

        assertNotNull(replaced);
        // Add assertions for the expected result after replacement
    }

    @Test
    public void testReplaceVariables() {
        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("key1", "value1"));
        String paramString = "${key1}";

        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, "123");

        assertNotNull(replaced);
        // Add assertions for the expected result after variable replacement
    }

    // Write more test cases for other scenarios and edge cases

}

