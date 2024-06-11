package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.cli.Util;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class ParametersUtilsTest {

    private final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInput_NullInputParams_ReturnsEmptyMap() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);

        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, taskDefinition, "taskId");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInput_NullWorkflow_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, null, taskDefinition, "taskId");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInput_NullTaskDefinition_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, "taskId");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInput_ValidInput_ReturnsCorrectValues() throws IOException {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflowInputParam", "workflowValue");
        inputParams.put("taskInputParam", "taskValue");

        WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
        when(workflowDef.getInputTemplate()).thenReturn(mockInputTemplate());

        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);

        Map<String, Object> expectedWorkflowInput = new HashMap<>();
        expectedWorkflowInput.put("workflowOutputParam", "workflowValue");

        when(taskDefinition.getInputTemplate()).thenReturn(mockInputTemplate());

        Map<String, Object> expectedTaskInput = new HashMap<>();
        expectedTaskInput.put("taskOutputParam", "taskValue");

        when(taskDefinition.getName()).thenReturn("taskName");
        when(taskDefinition.getInputData()).thenReturn(expectedTaskInput);
        when(taskDefinition.getOutputData()).thenReturn(expectedWorkflowInput);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");

        Map<String, Object> actualWorkflowInput = (Map<String, Object>) ReflectionTestUtils.getFieldValue(
                result, "workflow");

        Map<String, Object> actualTaskInput = (Map<String, Object>) ReflectionTestUtils.getFieldValue(
                result, "input");

        assertEquals(expectedTaskInput, actualTaskInput);
        assertEquals(expectedWorkflowInput, actualWorkflowInput);
    }

    @Test
    public void testGetTaskInputV2_NullInput_ReturnsEmptyMap() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);

        Map<String, Object> result = parametersUtils.getTaskInputV2(null, workflowModel, "taskId", taskDefinition);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInputV2_NullWorkflow_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, null, "taskId", taskDefinition);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInputV2_NullTaskDefinition_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInputV2_ValidInput_ReturnsCorrectValues() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflowInputParam", "workflowValue");
        inputParams.put("taskInputParam", "taskValue");

        WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
        when(workflowDef.getInputTemplate()).thenReturn(mockInputTemplate());

        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);

        TaskModel taskModel = Mockito.mock(TaskModel.class);
        when(taskModel.getReferenceTaskName()).thenReturn("taskName");
        when(taskModel.getInputData()).thenReturn(mockInputTemplate());
        when(taskModel.getOutputData()).thenReturn(mockInputTemplate());

        List<TaskModel> tasks = new LinkedList<>();
        tasks.add(taskModel);

        when(workflowModel.getTasks()).thenReturn(tasks);

        Map<String, Object> expectedWorkflowInput = new HashMap<>();
        expectedWorkflowInput.put("workflowOutputParam", "workflowValue");

        Map<String, Object> expectedTaskInput = new HashMap<>();
        expectedTaskInput.put("taskOutputParam", "taskValue");

        when(taskDefinition.getInputTemplate()).thenReturn(mockInputTemplate());

        Map<String, Map<String, Object>> inputMap = new HashMap<>();
        inputMap.put("workflow", expectedWorkflowInput);

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDefinition);

        Map<String, Object> actualWorkflowInput = (Map<String, Object>) ReflectionTestUtils.getFieldValue(
                result, "workflow");

        Map<String, Object> actualTaskInput = (Map<String, Object>) ReflectionTestUtils.getFieldValue(
                result, "input");

        assertEquals(expectedTaskInput, actualTaskInput);
        assertEquals(expectedWorkflowInput, actualWorkflowInput);
    }

    @Test
    public void testReplace_NullInput_ReturnsEmptyString() {
        String paramString = null;
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("", result);
    }

    @Test
    public void testReplace_NoVariables_ReturnsOriginalString() {
        String paramString = "ThisIsAString";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result);
    }

    @Test
    public void testReplace_SimpleVariable_ReturnsReplacedString() {
        String paramString = "${workflow.input.workflowOutputParam}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read("$.workflow.input.workflowOutputParam")).thenReturn("workflowValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("workflowValue", result);
    }

    @Test
    public void testReplace_NestedVariable_ReturnsReplacedString() {
        String paramString = "${workflow.input.workflowOutputParam.taskOutputParam}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read("$.workflow.input.workflowOutputParam.taskOutputParam"))
                .thenReturn("taskValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("taskValue", result);
    }

    @Test
    public void testReplace_MultipleVariables_ReturnsReplacedString() {
        String paramString = "${workflow.input.workflowOutputParam.taskOutputParam1}${workflow.input.workflowOutputParam.taskOutputParam2}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read("$.workflow.input.workflowOutputParam.taskOutputParam1"))
                .thenReturn("taskValue1");
        when(documentContext.read("$.workflow.input.workflowOutputParam.taskOutputParam2"))
                .thenReturn("taskValue2");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("taskValue1taskValue2", result);
    }

    @Test
    public void testReplace_EnvironmentVariable_ReturnsReplacedString() {
        String paramString = "${ENV_VAR}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String envVarValue = "envVarValue";
        when(System.getenv("ENV_VAR")).thenReturn(envVarValue);

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(envVarValue, result);
    }

    @Test
    public void testReplace_InvalidVariable_ReturnsOriginalString() {
        String paramString = "${invalidVariable}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result);
    }

    private Map<String, Object> mockInputTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("workflowOutputParam", "workflowValue");
        map.put("taskOutputParam", "taskValue");
        return map;
    }
}