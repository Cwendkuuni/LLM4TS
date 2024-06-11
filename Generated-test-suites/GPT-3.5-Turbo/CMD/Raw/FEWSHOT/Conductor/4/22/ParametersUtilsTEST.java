package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertEquals(0, taskInput.size());
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "task1";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        assertEquals(0, taskInputV2.size());
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${value1}");
        Object json = "{\"value1\": \"replacedValue\"}";

        Map<String, Object> replacedValues = parametersUtils.replace(input, json);

        assertEquals("replacedValue", replacedValues.get("key1"));
    }

    @Test
    public void testReplaceString() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String paramString = "${value1}";
        Object json = "{\"value1\": \"replacedValue\"}";

        Object replacedValue = parametersUtils.replace(paramString, json);

        assertEquals("replacedValue", replacedValue);
    }

    @Test
    public void testReplaceList() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        List<String> values = Arrays.asList("${value1}", "${value2}");
        String taskId = "task1";
        DocumentContext io = JsonPath.parse(Collections.emptyMap());

        List<Object> replacedList = parametersUtils.replaceList(values, taskId, io);

        assertEquals(2, replacedList.size());
        assertEquals("replacedValue1", replacedList.get(0));
        assertEquals("replacedValue1", replacedList.get(1));
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String paramString = "${workflow.input.var}";
        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("var", "replacedValue"));
        String taskId = "task1";

        Object replacedValue = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals("replacedValue", replacedValue);
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("value1", workflowInput.get("key1"));
    }
}