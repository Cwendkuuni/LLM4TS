package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.model.TaskModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.core.utils.TaskUtils;

public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ParametersUtils parametersUtils;
    private TaskDef taskDefinition;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;
    private TaskModel taskModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        taskDefinition = mock(TaskDef.class);
        workflowDef = mock(WorkflowDef.class);
        workflowModel = mock(WorkflowModel.class);
        taskModel = mock(TaskModel.class);

        when(taskDefinition.getInputTemplate()).thenReturn(new HashMap<>());
        when(workflowDef.getInputTemplate()).thenReturn(new HashMap<>());
        when(workflowDef.getSchemaVersion()).thenReturn(1L);
        when(workflowModel.getTasks()).thenReturn(List.of(taskModel));
        when(taskModel.getReferenceTaskName()).thenReturn("task1");
        when(taskModel.getTaskDefName()).thenReturn("task1Def");
        when(taskModel.getInputData()).thenReturn(new HashMap<>());
        when(taskModel.getOutputData()).thenReturn(new HashMap<>());
        when(taskModel.getScheduledTime()).thenReturn(new LocalDateTime("2022-01-01T01:01:01.000Z"));
        when(taskModel.getStartTime()).thenReturn(new LocalDateTime("2022-01-01T02:02:02.000Z"));
        when(taskModel.getEndTime()).thenReturn(new LocalDateTime("2022-01-01T03:03:03.000Z"));
        when(taskModel.getStatus()).thenReturn("RUNNING");
        when(taskModel.getCorrelationId()).thenReturn("correlationId");
        when(taskModel.getReasonForIncompletion()).thenReturn("reasonForIncompletion");
        when(taskModel.getCallbackAfterSeconds()).thenReturn(5L);
        when(taskModel.getWorkerId()).thenReturn("workerId");
        when(taskModel.getIteration()).thenReturn(1);
        when(taskModel.getTaskType()).thenReturn("taskType");

        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV1() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.task1.input.param1", "value1");
        inputParams.put("workflow.input.task1.output.param2", "value2");
        inputParams.put("workflow.input.param3", "value3");
        inputParams.put("task1.output.param4", "value4");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, null);

        assertEquals(5, result.size());
        assertEquals("value1", result.get("task1.input.param1"));
        assertEquals("value2", result.get("task1.output.param2"));
        assertNull(result.get("workflow.input.param3"));
        assertEquals("value4", result.get("task1.output.param4"));
        assertNull(result.get("task1.input.param2"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        Map<String, Object> workflowInput = new LinkedHashMap<>();
        workflowInput.put("input", new HashMap<>());
        workflowInput.put("output", new HashMap<>());
        workflowInput.put("status", "RUNNING");
        workflowInput.put("workflowId", "workflowId");
        workflowInput.put("parentWorkflowId", "parentWorkflowId");
        workflowInput.put("parentWorkflowTaskId", "parentWorkflowTaskId");
        workflowInput.put("workflowType", "workflowType");
        workflowInput.put("version", 1L);
        workflowInput.put("variables", new HashMap<>());
        Map<String, Object> taskInput = new HashMap<>();
        taskInput.put("input", new HashMap<>());
        taskInput.put("output", new HashMap<>());
        taskInput.put("taskType", "taskType");
        Map<String, Object> taskOutput = new HashMap<>();
        taskOutput.put("param1", "value1");
        taskOutput.put("param2", "value2");
        Map<String, Object> taskData = new HashMap<>();
        taskData.put("input", taskInput);
        taskData.put("output", taskOutput);
        Map<String, Object> task = new HashMap<>();
        task.put("inputData", taskData);
        Map<String, Object> workflow = new HashMap<>();
        workflow.put("input", workflowInput);
        workflow.put("tasks", List.of(task));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.task1.input.param1", "value1");
        inputParams.put("workflow.input.task1.output.param2", "value2");
        inputParams.put("workflow.input.param3", "value3");
        inputParams.put("task1.input.param4", "value4");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, null);

        assertEquals(6, result.size());
        assertEquals("value1", result.get("task1.input.param1"));
        assertEquals("value2", result.get("task1.output.param2"));
        assertEquals("value3", result.get("workflow.input.param3"));
        assertEquals(null, result.get("workflow.input.task1.input.param2"));
        assertEquals("value4", result.get("task1.input.param4"));
        assertEquals("workflowId", result.get("workflow.workflowId"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.workflowId}-${workflow.input.parentWorkflowId}-${workflow.input.workflowType}-${workflow.input.version}-${workflow.input.correlationId}-${workflow.input.reasonForIncompletion}";
        DocumentContext documentContext = mock(DocumentContext.class);

        Map<String, Object> result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result.toString());

        when(documentContext.read("workflow.input.workflowId")).thenReturn("workflowId");
        when(documentContext.read("workflow.input.parentWorkflowId")).thenReturn("parentWorkflowId");
        when(documentContext.read("workflow.input.version")).thenReturn(1L);
        when(documentContext.read("workflow.input.correlationId")).thenReturn("correlationId");
        when(documentContext.read("workflow.input.reasonForIncompletion")).thenReturn("reasonForIncompletion");

        result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("workflowId-parentWorkflowId-workflowType-1-correlationId-reasonForIncompletion", result.toString());
    }

    @Test
    public void testReplaceVariablesWithNullValue() {
        String paramString = "${workflow.input.workflowId}-${workflow.input.parentWorkflowId}-${workflow.input.version}-${workflow.input.correlationId}-${workflow.input.reasonForIncompletion}";
        DocumentContext documentContext = mock(DocumentContext.class);

        Map<String, Object> result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result.toString());

        when(documentContext.read("workflow.input.workflowId")).thenReturn("workflowId");
        when(documentContext.read("workflow.input.parentWorkflowId")).thenReturn(null);
        when(documentContext.read("workflow.input.version")).thenReturn(1L);
        when(documentContext.read("workflow.input.correlationId")).thenReturn("correlationId");
        when(documentContext.read("workflow.input.reasonForIncompletion")).thenReturn("reasonForIncompletion");

        result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("workflowId-null-1-correlationId-reasonForIncompletion", result.toString());
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        String paramString = "${workflow.input.workflowId}-${workflow.input.parentWorkflowId}-${workflow.input.version}-${workflow.input.correlationId}-${workflow.input.reasonForIncompletion}";
        DocumentContext documentContext = mock(DocumentContext.class);

        Map<String, Object> result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result.toString());

        when(documentContext.read("workflow.input.workflowId")).thenReturn("");
        when(documentContext.read("workflow.input.parentWorkflowId")).thenReturn("parentWorkflowId");
        when(documentContext.read("workflow.input.version")).thenReturn(1L);
        when(documentContext.read("workflow.input.correlationId")).thenReturn("correlationId");
        when(documentContext.read("workflow.input.reasonForIncompletion")).thenReturn("reasonForIncompletion");

        result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("parentWorkflowId-workflowId-1-correlationId-reasonForIncompletion", result.toString());
    }

    @Test
    public void testReplaceVariablesWithMultipleNestedLevels() {
        String paramString = "${workflow.input.task1.input.task2.input.param1}-${workflow.input.task1.output.task2.output.param2}";
        DocumentContext documentContext = mock(DocumentContext.class);

        Map<String, Object> result = parametersUtils.replaceVariables(paramString, documentContext, null);

        when(documentContext.read("workflow.input.task1.input.task2.input.param1")).thenReturn("param1Value");
        when(documentContext.read("workflow.input.task1.output.task2.output.param2")).thenReturn("param2Value");

        result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("param1Value-param2Value", result.toString());
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() {
        String paramString = "${env.VAR1}-${workflow.input.workflowId}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String sysValue = "VAR1Value";

        Map<String, Object> result = parametersUtils.replaceVariables(paramString, documentContext, null);

        when(documentContext.read("workflow.input.workflowId")).thenReturn("workflowId");

        EnvironmentVar envVar = new EnvironmentVar("VAR1", sysValue);
        EnvUtils.setEnvironmentVariable(envVar);

        result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(sysValue + "-workflowId", result.toString());

        EnvUtils.resetEnvironmentVariables();
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariableNullValue() {
        String paramString = "${env.VAR1}-${workflow.input.workflowId}";
        DocumentContext documentContext = mock(DocumentContext.class);

        Map<String, Object> result = parametersUtils.replaceVariables(paramString, documentContext, null);

        when(documentContext.read("workflow.input.workflowId")).thenReturn("workflowId");

        EnvironmentVar envVar = new EnvironmentVar("VAR1", null);
        EnvUtils.setEnvironmentVariable(envVar);

        result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("workflowId-null", result.toString());

        EnvUtils.resetEnvironmentVariables();
    }

    @Test
    public void testReplaceVariablesWithDateTimeFormat() {
        String paramString = "${workflow.input.task1.output.createdAt:yyyy-MM-dd HH:mm:ss.SSS}-${workflow.input.workflowId}";
        DocumentContext documentContext = mock(DocumentContext.class);

        LocalDateTime createdAt = LocalDateTime.now();

        Map<String, Object> result = parametersUtils.replaceVariables(paramString, documentContext, null);

        when(documentContext.read("workflow.input.task1.output.createdAt")).thenReturn(createdAt);

        result = parametersUtils.replaceVariables(paramString, documentContext, null);

        String formattedDate = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").print(createdAt);

        assertEquals(formattedDate + "-workflowId", result.toString());
    }

    @Test
    public void testReplaceVariablesWithListOfObjects() {
        String paramString = "${workflow.input.task1.output.list}";
        DocumentContext documentContext = mock(DocumentContext.class);
        List<Map<String, Object>> list = List.of(
                Map.of("param1", "value1"),
                Map.of("param1", "value2")
        );

        Map<String, Object> result = parametersUtils.replaceVariables(paramString, documentContext, null);

        when(documentContext.read("workflow.input.task1.output.list")).thenReturn(list);

        result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(list.stream().map(map -> map.toString()).collect(Collectors.joining(",")), result.toString());
    }

    @Test
    public void testReplaceVariablesWithMapOfObjects() {
        String paramString = "${workflow.input.task1.output.mapping}";
        DocumentContext documentContext = mock(DocumentContext.class);
        Map<String, Object> mapping = Map.of("param1", "value1", "param2", "value2");

        Map<String, Object> result = parametersUtils.replaceVariables(paramString, documentContext, null);

        when(documentContext.read("workflow.input.task1.output.mapping")).thenReturn(mapping);

        result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(mapping.toString(), result.toString());
    }
}