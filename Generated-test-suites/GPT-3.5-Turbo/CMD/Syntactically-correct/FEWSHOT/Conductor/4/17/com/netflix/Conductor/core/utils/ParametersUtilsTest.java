package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());
        taskDefinition.getInputTemplate().put("key1", "value1");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("input1", "workflow.input.param1");
        inputParams.put("input2", "task.input.param2");

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("key1"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "value1");
        input.put("key2", "${workflow.input.param1}");

        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("workflow", Collections.singletonMap("input", Collections.singletonMap("param1", "replacement"))));

        Map<String, Object> replaced = parametersUtils.replace(input, documentContext);

        assertNotNull(replaced);
        assertEquals("value1", replaced.get("key1"));
        assertEquals("replacement", replaced.get("key2"));
    }

    @Test
    public void testReplaceVariables() {
        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("workflow", Collections.singletonMap("input", Collections.singletonMap("param1", "replacement"))));

        String replaced = (String) parametersUtils.replaceVariables("Test ${workflow.input.param1}", documentContext, "taskId", 0);

        assertEquals("Test replacement", replaced);
    }
}