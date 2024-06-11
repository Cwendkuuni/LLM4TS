package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.jayway.jsonpath.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    private ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDefinition);

        assertNotNull(taskInput);
        assertEquals(0, taskInput.size());
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input.param1}");
        input.put("key2", "${workflow.input.param2}");

        String json = "{\"workflow\": {\"input\": {\"param1\": \"value1\", \"param2\": \"value2\"}}}";

        Object replaced = parametersUtils.replace(input, json);

        assertNotNull(replaced);
        assertTrue(replaced instanceof Map);

        Map<String, Object> replacedMap = (Map<String, Object>) replaced;
        assertEquals("value1", replacedMap.get("key1"));
        assertEquals("value2", replacedMap.get("key2"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "Hello ${workflow.input.name}, welcome to ${workflow.input.location}";

        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(Map.of("name", "John", "location", "New York"));

        Configuration option =
                Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);

        Object replaced = parametersUtils.replaceVariables(paramString, JsonPath.parse(workflow.getInput(), option), "taskId");

        assertNotNull(replaced);
        assertEquals("Hello John, welcome to New York", replaced.toString());
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Map.of("param1", "default1", "param2", "default2"));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param2", "value2");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals(2, workflowInput.size());
        assertEquals("default1", workflowInput.get("param1"));
        assertEquals("value2", workflowInput.get("param2"));
    }

    @Test(expected = RuntimeException.class)
    public void testCloneException() {
        when(objectMapper.writeValueAsBytes(anyMap())).thenThrow(new RuntimeException());
        parametersUtils.clone(new HashMap<>());
    }

    @Test
    public void testReplaceList() {
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> value = new HashMap<>();
        value.put("key1", "${workflow.input.param1}");
        value.put("key2", "${workflow.input.param2}");
        values.add(value);

        String json = "{\"workflow\": {\"input\": {\"param1\": \"value1\", \"param2\": \"value2\"}}}";

        Object replaced = parametersUtils.replaceList(values, "taskId", JsonPath.parse(json));

        assertNotNull(replaced);
        assertTrue(replaced instanceof List);

        List<Map<String, Object>> replacedList = (List<Map<String, Object>>) replaced;
        assertEquals(1, replacedList.size());

        Map<String, Object> replacedValue = replacedList.get(0);
        assertEquals("value1", replacedValue.get("key1"));
        assertEquals("value2", replacedValue.get("key2"));
    }

}