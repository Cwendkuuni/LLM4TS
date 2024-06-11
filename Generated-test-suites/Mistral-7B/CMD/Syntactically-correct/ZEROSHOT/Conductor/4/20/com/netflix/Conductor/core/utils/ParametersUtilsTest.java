package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.model.TaskModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.workflow.WorkflowVersion;

/**
 * Unit tests for {@link ParametersUtils}.
 */
public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Logger logger;

    @Mock
    private TaskDef taskDefinition;

    @Mock
    private WorkflowDef workflowDef;

    @Mock
    private WorkflowModel workflowModel;

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("workflowInputKey", "workflowInputValue");
        workflowModel.setInput(workflowInput);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParamsKey", "inputParamsValue");

        when(objectMapper.readValue(mock(byte[].class), new TypeReference<Map<String, Object>>() {})).thenReturn(workflowInput);
        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(new TaskModel());

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertEquals("workflowInputValue", result.get("workflowInputKey"));
        assertEquals("inputParamsValue", result.get("inputParamsKey"));
    }

    @Test
    public void testGetTaskInputV1_NullValues() {
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowModel.setInput(workflowInput);

        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> expectedResult = new HashMap<>();

        when(objectMapper.readValue(mock(byte[].class), new TypeReference<Map<String, Object>>() {})).thenReturn(Collections.emptyMap());

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertNull(result.get("workflowInputKey"));
        assertNull(result.get("inputParamsKey"));
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetTaskInputV1_NullWorkflowModel() {
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> expectedResult = new HashMap<>();

        Map<String, Object> result = parametersUtils.getTaskInputV1(null, inputParams);

        assertNull(result);
    }

    @Test
    public void testGetTaskInputV1_NullInputParams() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);

        Map<String, Object> expectedResult = new HashMap<>();

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, null);

        assertNull(result);
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(List.of(mock(TaskModel.class)));
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("workflowInputKey", "workflowInputValue");
        workflowModel.setInput(workflowInput);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParamsKey", "inputParamsValue");

        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getInputData()).thenReturn(new HashMap<>());
        when(taskModel.getOutputData()).thenReturn(new HashMap<>());
        when(taskModel.getReferenceTaskName()).thenReturn("taskRefName");
        when(taskModel.getTaskDefName()).thenReturn("taskDefName");

        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(taskModel);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(WorkflowVersion.V2);

        when(objectMapper.readValue(mock(byte[].class), new TypeReference<Map<String, Object>>() {})).thenReturn(workflowInput);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");

        assertEquals("workflowInputValue", result.get("workflow.input.workflowInputKey"));
        assertEquals("inputParamsValue", result.get("inputParamsKey"));
        assertEquals("workflowDef.inputTemplate.workflowInputKey", result.get("workflow.input.workflowInputKey2"));
        assertEquals(null, result.get("workflow.output"));
        assertEquals(null, result.get("workflow.status"));
        assertEquals(null, result.get("workflow.workflowId"));
        assertEquals(null, result.get("workflow.parentWorkflowId"));
        assertEquals(null, result.get("workflow.parentWorkflowTaskId"));
        assertEquals(null, result.get("workflow.workflowType"));
        assertEquals(null, result.get("workflow.version"));
        assertEquals(null, result.get("workflow.correlationId"));
        assertEquals(null, result.get("workflow.reasonForIncompletion"));
        assertEquals(null, result.get("taskDefName"));
        assertEquals("taskId", result.get("taskId"));
        assertEquals("taskDefName", result.get("taskDefinition.taskDefName"));
        assertEquals(null, result.get("taskDefinition.inputTemplate"));
    }

    @Test
    public void testGetTaskInputV2_NullTaskDefinition() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTasks(List.of(mock(TaskModel.class)));
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("workflowInputKey", "workflowInputValue");
        workflowModel.setInput(workflowInput);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParamsKey", "inputParamsValue");

        TaskModel taskModel = mock(TaskModel.class);
        when(taskModel.getInputData()).thenReturn(new HashMap<>());
        when(taskModel.getOutputData()).thenReturn(new HashMap<>());
        when(taskModel.getReferenceTaskName()).thenReturn("taskRefName");

        when(workflowModel.getTaskByRefName("taskRefName")).thenReturn(taskModel);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(WorkflowVersion.V2);

        when(objectMapper.readValue(mock(byte[].class), new TypeReference<Map<String, Object>>() {})).thenReturn(workflowInput);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, "taskId");

        assertEquals("workflowInputValue", result.get("workflow.input.workflowInputKey"));
        assertEquals("inputParamsValue", result.get("inputParamsKey"));
        assertEquals(null, result.get("taskDefinition.taskDefName"));
    }

    @Test
    public void testReplaceVariables() {
        DocumentContext documentContext = mock(DocumentContext.class);
        Configuration configuration = Configuration.defaultConfiguration();
        configuration.addOptions(new Option[0]);
        when(JsonPath.parse(anyString(), any())).thenReturn(documentContext);

        String input = "${workflow.input.workflowInputKey}.${workflow.input.workflowInputKey2}";
        String workflowInput = "{\"workflowInputKey\": \"workflowInputValue\", \"workflowInputKey2\": \"workflowInputValue2\"}";

        String expectedResult = "workflowInputValue.workflowInputValue2";

        Object result = parametersUtils.replaceVariables(input, documentContext, null);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceVariables_EnvironmentVariable() {
        DocumentContext documentContext = mock(DocumentContext.class);
        Configuration configuration = Configuration.defaultConfiguration();
        configuration.addOptions(new Option[0]);
        when(JsonPath.parse(anyString(), any())).thenReturn(documentContext);

        String input = "${ENV_VARIABLE}";
        String envVariableValue = "envVariableValue";
        when(EnvUtils.getSystemParametersValue(input, null)).thenReturn(envVariableValue);

        Object result = parametersUtils.replaceVariables(input, documentContext, null);

        assertEquals(envVariableValue, result);
    }

    @Test
    public void testReplaceVariables_NullDocumentContext() {
        String input = "${workflow.input.workflowInputKey}";

        Object result = parametersUtils.replaceVariables(input, null, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariables_NullInput() {
        DocumentContext documentContext = mock(DocumentContext.class);

        Object result = parametersUtils.replaceVariables(null, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, null);

        assertEquals(workflowDef.getInputTemplate(), result);
    }

    @Test
    public void testGetWorkflowInput_NullWorkflowDef() {
        Map<String, Object> result = parametersUtils.getWorkflowInput(null, null);

        assertNull(result);
    }
}