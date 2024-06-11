package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.workflow.WorkflowDefinition;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParametersUtilsTest {

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private WorkflowModel workflowModel;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        WorkflowDefinition workflowDefinition = new WorkflowDefinition();
        workflowDefinition.setInput(Map.of("input1", "value1"));
        workflowModel = new WorkflowModel("workflowName", workflowDefinition, 1L);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = Map.of("param1", "value1");
        Map<String, Object> expectedOutput = Map.of(
                "param1", "value1",
                "input", Map.of("input1", "value1"),
                "workflow", Map.of("input", Map.of("input1", "value1")));

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams, workflowModel, null, null);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetTaskInputWithTaskDefinition() {
        Map<String, Object> inputParams = Map.of("param1", "value1");
        Map<String, Object> taskDefinitionInputTemplate = Map.of("param2", "value2");
        Map<String, Object> expectedOutput = Map.of(
                "param1", "value1",
                "param2", "value2");

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams, workflowModel, null, new TaskDef() {{
                    this.inputTemplate = taskDefinitionInputTemplate;
                }});

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetTaskInputWithEmptyInputParams() {
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInputWithNullTaskDefinition() {
        Map<String, Object> result = parametersUtils.getTaskInput(
                Map.of("param1", "value1"), workflowModel, null, null);

        assertTrue(result.containsKey("param1"));
        assertTrue(result.containsKey("workflow"));
    }

    @Test
    public void testGetTaskInputV2() throws IOException {
        Map<String, Object> inputParams = Map.of("param1", "value1");
        Map<String, Object> expectedOutput = Map.of(
                "param1", "value1",
                "input", Map.of("input1", "value1"),
                "workflow", Map.of(
                        "input", Map.of("input1", "value1"),
                        "tasks", List.of(
                                Map.of(
                                        "referenceTaskName", "task1",
                                        "inputData", Map.of("param2", "value2")),
                                Map.of(
                                        "referenceTaskName", "task2"))));

        DocumentContext documentContext = JsonPath.parse(expectedOutput);
        Map<String, Object> result = parametersUtils.getTaskInputV2(
                inputParams, workflowModel, "taskId", null);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testReplace() {
        String paramString = "${workflow.input.input1}${tasks[0].inputData.param2}${tasks[1].inputData.param3}";
        Map<String, Object> document = Map.of(
                "workflow", Map.of("input", Map.of("input1", "value1")),
                "tasks", List.of(
                        Map.of("inputData", Map.of("param2", "value2")),
                        Map.of("inputData", Map.of("param3", "value3"))));

        Object expectedOutput = "value1value2value3";

        Object result = parametersUtils.replace(paramString, document, null);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testReplaceWithEmptyDocument() {
        String paramString = "${workflow.input.input1}${tasks[0].inputData.param2}${tasks[1].inputData.param3}";

        Object result = parametersUtils.replace(paramString, null, null);

        assertNull(result);
    }

    @Test
    public void testReplaceWithNullTaskId() {
        String paramString = "${workflow.input.input1}${tasks[0].inputData.param2}${tasks[1].inputData.param3}";

        Object result = parametersUtils.replace(paramString, null, "nullTaskId");

        assertNull(result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.input1}${tasks[0].inputData.param2}${tasks[1].inputData.param3}";

        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", Map.of("input", Map.of("input1", "value1"))));

        Object expectedOutput = "value1";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testReplaceVariablesWithEmptyDocument() {
        String paramString = "${workflow.input.input1}${tasks[0].inputData.param2}${tasks[1].inputData.param3}";

        Object result = parametersUtils.replaceVariables(paramString, null, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithNullTaskId() {
        String paramString = "${workflow.input.input1}${tasks[0].inputData.param2}${tasks[1].inputData.param3}";

        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", Map.of("input", Map.of("input1", "value1"))));

        Object result = parametersUtils.replaceVariables(paramString, documentContext, "nullTaskId");

        assertNull(result);
    }
}