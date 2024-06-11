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
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.test.TestUtils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/** Tests for the {@link ParametersUtils} class. */
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtils.class);
    private static final Pattern PATTERN =
            Pattern.compile(
                    "(?=(?<!\\$)\\$\\{)(?:(?=.*?\\{(?!.*?\\1)(.*\\}(?!.*\\2).*))(?=.*?\\}(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^{]*(?=\\2$)",
                    Pattern.DOTALL);

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private ObjectMapper jsonMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        jsonMapper = Mockito.mock(ObjectMapper.class);
        workflowDef = Mockito.mock(WorkflowDef.class);
        taskDef = Mockito.mock(TaskDef.class);
        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        parametersUtils = new ParametersUtils(objectMapper);
        when(objectMapper.writeValueAsBytes(ArgumentMatchers.any(Map.class))).then(
                invocation -> TestUtils.mapToJson(invocation.getArgument(0)).getBytes());
        when(objectMapper.readValue(ArgumentMatchers.eq(new StringSource("{\"name\": \"value\"}")),
                TypeReference.of(Map.class)))
                .thenReturn(Collections.singletonMap("name", "value"));
        when(jsonMapper.readValue(ArgumentMatchers.anyString(), TypeReference.of(Map.class)))
                .then(invocation -> (Map<String, Object>) JsonPath.parse(invocation.getArgument(0)));
        when(workflowDef.getInputTemplate()).thenReturn(new HashMap<>());
        when(workflowDef.getName()).thenReturn("workflowName");
        when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());
        when(taskDef.getName()).thenReturn("taskName");
        when(taskDef.getInputData()).thenReturn(new LinkedHashMap<>());
    }

    @Test
    public void testGetTaskInput() {
        // Given
       Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflowParams", new HashMap<>());
        inputParams.put("taskParams", new HashMap<>());

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);

        // Then
        assertEquals(2, result.size());
        assertEquals("workflowName", result.get("workflow"));
        assertTrue(result.containsKey("task"));
        assertEquals("taskName", result.get("task").get("taskType"));
    }

    @Test
    public void testGetTaskInputWithNullInputParams() {
        // Given
        Map<String, Object> inputParams = null;

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetTaskInputWithNullTaskDefinition() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflowParams", new HashMap<>());
        inputParams.put("taskParams", new HashMap<>());

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetTaskInputWithNullWorkflowModel() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflowParams", new HashMap<>());
        inputParams.put("taskParams", new HashMap<>());

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, null, taskDef, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testGetTaskInputWithEmptyTasksList() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflowParams", new HashMap<>());

        workflowModel.setTasks(Collections.emptyList());

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testReplaceVariables() {
        // Given
        String paramString = "${workflow.workflowName}.${workflow.workflowVersion}.${input.inputParam}";
        DocumentContext documentContext = null;
        String taskId = null;

        // When
        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertNull(replacement);

        // Given
        DocumentContext mockDocumentContext = Mockito.mock(DocumentContext.class);
        when(jsonMapper.readValue(ArgumentMatchers.anyString(), TypeReference.of(Map.class)))
                .then(invocation -> mockDocumentContext);

        // When
        replacement = parametersUtils.replaceVariables(paramString, mockDocumentContext, taskId);

        // Then
        verify(mockDocumentContext, times(1)).read(ArgumentMatchers.equalTo("workflow.workflowName"));
        verify(mockDocumentContext, times(1)).read(ArgumentMatchers.equalTo("workflow.workflowVersion"));
        verify(mockDocumentContext, times(0)).read(ArgumentMatchers.equalTo("input.inputParam"));
        assertEquals("workflowName.workflowVersion.inputParam", replacement.toString());

        // Given
        String paramStringWithEnvVariable = "${workflow.workflowName}.${env.TEST_ENV_VAR}.${input.inputParam}";
        when(EnvUtils.getSystemParametersValue(ArgumentMatchers.eq("env.TEST_ENV_VAR"), taskId))
                .thenReturn("testEnvVariableValue");

        // When
        replacement = parametersUtils.replaceVariables(paramStringWithEnvVariable, mockDocumentContext, taskId);

        // Then
        verify(mockDocumentContext, times(1)).read(ArgumentMatchers.equalTo("workflow.workflowName"));
        verify(mockDocumentContext, times(1)).read(ArgumentMatchers.equalTo("env.TEST_ENV_VAR"));
        verify(mockDocumentContext, times(1)).read(ArgumentMatchers.equalTo("input.inputParam"));
        assertEquals("workflowName.testEnvVariableValue.inputParam", replacement.toString());
    }

    @Test
    public void testReplaceVariablesWithEmptyDocumentContext() {
        // Given
        String paramString = "${workflow.workflowName}.${workflow.workflowVersion}.${input.inputParam}";
        DocumentContext documentContext = null;
        String taskId = null;

        // When
        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertNull(replacement);
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        // Given
        String paramString = "${workflow.workflowName}.${workflow.workflowVersion}.${input.inputParam}";
        String taskId = null;

        // When
        Object replacement = parametersUtils.replaceVariables(paramString, null, taskId);

        // Then
        assertNull(replacement);
    }

    @Test
    public void testReplaceVariablesWithEmptyParamString() {
        // Given
        String paramString = "";
        DocumentContext documentContext = null;
        String taskId = null;

        // When
        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("", replacement);
    }

    @Test
    public void testReplaceVariablesWithNestedParamString() {
        // Given
        String paramString = "${workflow.workflowName}.${workflow.workflowVersion}.${input.inputParam1}.${input.inputParam2}";
        DocumentContext documentContext = null;
        String taskId = null;

        // When
        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertNull(replacement);

        // Given
        DocumentContext mockDocumentContext = Mockito.mock(DocumentContext.class);
        when(jsonMapper.readValue(ArgumentMatchers.anyString(), TypeReference.of(Map.class)))
                .then(invocation -> mockDocumentContext);

        // When
        replacement = parametersUtils.replaceVariables(paramString, mockDocumentContext, taskId);

        // Then
        verify(mockDocumentContext, times(1)).read(ArgumentMatchers.equalTo("workflow.workflowName"));
        verify(mockDocumentContext, times(1)).read(ArgumentMatchers.equalTo("workflow.workflowVersion"));
        verify(mockDocumentContext, times(1)).read(ArgumentMatchers.equalTo("input.inputParam1"));
        verify(mockDocumentContext, times(1)).read(ArgumentMatchers.equalTo("input.inputParam2"));
        assertEquals("workflowName.workflowVersion.inputParam1.inputParam2", replacement.toString());
    }

    @Test
    public void testReplaceVariablesWithLoopTask() {
        // Given
        String paramString = "${loopTask.input.inputParam}";
        DocumentContext documentContext = null;
        String taskId = "taskId";

        TaskModel taskModel = new TaskModel();
        taskModel.setInputData(new LinkedHashMap<String, Object>() {{
            put("inputParam", "inputValue");
        }});

        when(workflowModel.getTaskByRefName(ArgumentMatchers.eq("loopTask")))
                .thenReturn(taskModel);

        // When
        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("inputValue", replacement);
    }

    @Test
    public void testReplaceVariablesWithNestedLoopTask() {
        // Given
        String paramString = "${loopTask1.input.inputParam1}.${loopTask2.input.inputParam2}";
        DocumentContext documentContext = null;
        String taskId = "taskId";

        TaskModel loopTask1 = new TaskModel();
        loopTask1.setInputData(new LinkedHashMap<String, Object>() {{
            put("inputParam1", "inputValue1");
        }});

        TaskModel loopTask2 = new TaskModel();
        loopTask2.setInputData(new LinkedHashMap<String, Object>() {{
            put("inputParam2", "inputValue2");
        }});

        when(workflowModel.getTaskByRefName(ArgumentMatchers.eq("loopTask1")))
                .thenReturn(loopTask1);
        when(workflowModel.getTaskByRefName(ArgumentMatchers.eq("loopTask2")))
                .thenReturn(loopTask2);

        // When
        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("inputValue1.inputValue2", replacement);
    }

    @Test
    public void testGetWorkflowInput() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();

        // When
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        // Then
        assertEquals("workflowName", result.get("workflow"));
        assertNull(result.get("task"));
    }
}