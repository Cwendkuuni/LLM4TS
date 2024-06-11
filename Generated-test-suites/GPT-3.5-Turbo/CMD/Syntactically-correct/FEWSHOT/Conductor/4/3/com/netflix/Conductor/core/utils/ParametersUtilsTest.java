package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "123";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> input = new HashMap<>();
        Map<String, Object> taskParams = new HashMap<>();
        input.put("taskParams", taskParams);
        String taskId = "456";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        assertNotNull(taskInput);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input.variable1}");
        input.put("key2", "${task.input.variable2}");
        input.put("key3", "${task.output.variable3}");
        Object json = "{ \"workflow\": { \"input\": { \"variable1\": \"value1\" }, \"output\": {}, \"status\": \"RUNNING\" } }";

        Map<String, Object> replacedInput = parametersUtils.replace(input, json);

        assertEquals("value1", replacedInput.get("key1"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${task.input.variable1}";
        Object json = "{ \"task\": { \"input\": { \"variable1\": \"value1\" }, \"output\": {} } }";

        Object replacedValue = parametersUtils.replaceVariables(paramString, json);

        assertEquals("value1", replacedValue);
    }
}