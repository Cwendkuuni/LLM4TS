package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
        workflowDef = Mockito.mock(WorkflowDef.class);
        taskDef = Mockito.mock(TaskDef.class);
        workflowModel = new WorkflowModel();
    }

    @Test
    public void testGetTaskInputWithNullInputParams() {
        Map<String, Object> expectedResult = new HashMap<>();
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1L);
        when(workflowModel.getTasks()).thenReturn(Collections.emptyList());

        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, taskDef, "taskId");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputWithEmptyInputParams() {
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> expectedResult = new HashMap<>();
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1L);
        when(workflowModel.getTasks()).thenReturn(Collections.emptyList());

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputV1() {
        // Given
        String workflowName = "workflowName";
        String taskRefName = "taskRefName";
        String paramName = "paramName";
        String paramValue = "paramValue";

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put(paramName, paramValue);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setName(workflowName);
        workflowModel.setInput(workflowInput);
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));

        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName(taskRefName);

        WorkflowModel expectedWorkflowModel = new WorkflowModel();
        expectedWorkflowModel.setName(workflowName);
        expectedWorkflowModel.setInput(workflowInput);
        expectedWorkflowModel.getTasks().add(taskModel);

        when(objectMapper.readValue(Mockito.anyByteArray(), Mockito.eq(Map.class))).then(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            return parametersUtils.clone((Map<String, Object>) JsonPath.parse(bytes, Configuration.defaultConfiguration()));
        });

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Then
        verify(objectMapper, times(1)).readValue(Mockito.anyByteArray(), Mockito.eq(Map.class));
        verifyNoMoreInteractions(objectMapper);
        assertEquals(expectedWorkflowModel, result);

        ArgumentCaptor<byte[]> byteCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(objectMapper, times(1)).writeValueAsBytes(Map.of("input", workflowInput));
    }

    @Test
    public void testGetTaskInputV2() {
        // Given
        String workflowName = "workflowName";
        String taskRefName = "taskRefName";
        String paramName = "paramName";
        String paramValue = "paramValue";

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setName(workflowName);
        workflowModel.setInput(new HashMap<>());
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));

        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName(taskRefName);
        taskModel.setInputData(new HashMap<>());
        taskModel.setInputData().put(paramName, paramValue);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("input", Map.of("inputParam", "inputValue"));

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Then
        assertEquals(Map.of("inputParam", "inputValue"), result);

        verify(taskModel, times(1)).getInputData();
        verifyNoMoreInteractions(taskModel);
        verify(workflowModel, times(1)).getTasks();
        verifyNoMoreInteractions(workflowModel);
    }

    @Test
    public void testReplace() {
        // Given
        String paramString = "${workflow.input.inputParam} ${workflow.output.outputParam} ${envVariable} ${null}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";
        String envVariable = "envVariable";

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("inputParam", "workflowInputValue");
        workflowInput.put("outputParam", "workflowOutputValue");

        when(documentContext.read("$..input")).thenReturn(workflowInput);
        when(documentContext.read("$.envVariable")).thenReturn(envVariable);
        when(documentContext.read("$.null")).thenReturn(null);

        // When
        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("workflowInputValue workflowOutputValue envVariable null", replacement);

        verify(documentContext, times(1)).read("$..input");
        verify(documentContext, times(1)).read("$.envVariable");
        verify(documentContext, times(1)).read("$.null");
    }

    @Test
    public void testReplaceList() {
        // Given
        List<Object> values = List.of("${workflow.input.inputParam}", "${workflow.output.outputParam}");
        String taskId = "taskId";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("inputParam", "workflowInputValue");
        workflowInput.put("outputParam", "workflowOutputValue");

        when(documentContext.read("$..input")).thenReturn(workflowInput);

        // When
        List<Object> replacedList = parametersUtils.replaceList(values, taskId, documentContext);

        // Then
        assertEquals(List.of("workflowInputValue", "workflowOutputValue"), replacedList);

        verify(documentContext, times(1)).read("$..input");
    }
}