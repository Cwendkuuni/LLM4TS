package com.netflix.conductor.core.utils.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.MapUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private TaskDef taskDefinition;
    private WorkflowDef workflowDefinition;
    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
        taskDefinition = Mockito.mock(TaskDef.class);
        workflowDefinition = Mockito.mock(WorkflowDef.class);
        workflowModel = Mockito.mock(WorkflowModel.class);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> workflowInput = MapUtils.newMap("inputKey", "workflowValue");
        Map<String, Object> inputParams = MapUtils.newMap();
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDefinition);
        when(workflowDefinition.getSchemaVersion()).thenReturn(1L);
        when(workflowModel.getInput()).thenReturn(workflowInput);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");
        assertEquals(2, result.size());
        assertEquals("workflowValue", result.get("inputKey"));

        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDefinition);
        when(workflowDefinition.getSchemaVersion()).thenReturn(2L);

        Map<String, Object> inputTemplate = MapUtils.newMap("inputKey", "templateValue");
        when(taskDefinition.getInputTemplate()).thenReturn(inputTemplate);
        when(objectMapper.readValue(Mockito.anyString(), Map.class)).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> input = MapUtils.newMap("taskInputKey", "taskValue");
        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(Mockito.argThat(task -> task.getInputData().equals(input)));

        result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskRefName");
        assertEquals(4, result.size());
        assertEquals("templateValue", result.get("inputKey"));
        assertEquals("taskValue", result.get("taskInputKey"));

        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(null);

        try {
            result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskRefName");
            assertEquals(0, result.size());
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReplace() {
        String inputString = "${workflow.input.inputKey} ${task.input.taskInputKey}";
        Map<String, Object> workflowInput = MapUtils.newMap("inputKey", "workflowValue");
        Map<String, Object> taskInput = MapUtils.newMap("taskInputKey", "taskValue");
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        Map<String, Object> result = parametersUtils.replace(inputString, documentContext, null);
        assertEquals("workflowValue taskValue", result.toString());

        Map<String, Object> workflow = MapUtils.newMap("input", workflowInput);
        result = parametersUtils.replace(inputString, documentContext, "taskRefName");
        assertEquals("workflowValue taskValue", result.toString());

        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(Mockito.argThat(task -> task.getInputData().equals(taskInput)));
        result = parametersUtils.replace(inputString, documentContext, "taskRefName");
        assertEquals("workflowValue taskValue", result.toString());

        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(null);
        try {
            result = parametersUtils.replace(inputString, documentContext, "taskRefName");
            assertEquals("", result.toString());
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReplaceVariables() throws IOException {
        String inputString = "${workflow.input.${workflow.input.inputKey}}";
        Map<String, Object> workflowInput = MapUtils.newMap("inputKey", "inputKeyValue");

        JsonNode jsonNode = objectMapper.readTree("{\"input\":{\"inputKey\":\"workflowValue\"}}");
        DocumentContext documentContext = JsonPath.parse(jsonNode);

        String result = parametersUtils.replaceVariables(inputString, documentContext, null).toString();
        assertEquals("workflowValue", result);

        workflowInput.put("inputKeyValue", "variableValue");
        jsonNode = objectMapper.readTree("{\"input\":{\"inputKey\":\"workflowValue\", \"inputKeyValue\":\"variableValue\"}}");
        documentContext = JsonPath.parse(jsonNode);

        result = parametersUtils.replaceVariables(inputString, documentContext, null).toString();
        assertEquals("variableValue", result);

        result = parametersUtils.replaceVariables(inputString, documentContext, "taskRefName").toString();
        assertEquals("workflowValue", result);

        Map<String, Object> taskInput = MapUtils.newMap("taskInputKey", "taskValue");
        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(Mockito.argThat(task -> task.getInputData().equals(taskInput)));

        result = parametersUtils.replaceVariables(inputString, documentContext, "taskRefName").toString();
        assertEquals("workflowValue", result);

        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(null);
        try {
            result = parametersUtils.replaceVariables(inputString, documentContext, "taskRefName").toString();
            assertEquals("", result);
        } catch (NullPointerException e) {
            // expected
        }

        inputString = "${workflow.input.${workflow.input.inputKey}.${workflow.input.inputKeyValue}}";
        workflowInput.put("inputKeyValue", MapUtils.newMap("subKey", "subValue"));
        jsonNode = objectMapper.readTree("{\"input\":{\"inputKey\":{\"inputKeyValue\":{\"subKey\":\"workflowValue\"}}}}");
        documentContext = JsonPath.parse(jsonNode);

        result = parametersUtils.replaceVariables(inputString, documentContext, null).toString();
        assertEquals("workflowValue", result);

        result = parametersUtils.replaceVariables(inputString, documentContext, "taskRefName").toString();
        assertEquals("workflowValue", result);

        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(Mockito.argThat(task -> task.getInputData().equals(taskInput)));

        result = parametersUtils.replaceVariables(inputString, documentContext, "taskRefName").toString();
        assertEquals("workflowValue", result);

        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(null);
        try {
            result = parametersUtils.replaceVariables(inputString, documentContext, "taskRefName").toString();
            assertEquals("", result);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputTemplate = MapUtils.newMap("inputKey", "templateValue");
        when(workflowModel.getInputTemplate()).thenReturn(inputTemplate);

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDefinition, null);
        assertEquals(2, result.size());
        assertEquals("templateValue", result.get("inputKey"));

        when(workflowModel.getInputTemplate()).thenReturn(null);
        result = parametersUtils.getWorkflowInput(workflowDefinition, null);
        assertEquals(0, result.size());
    }
}