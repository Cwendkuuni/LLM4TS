package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.netflix.conductor.model.WorkflowModel.WorkflowTask;

public class ParametersUtilsTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);
    private static final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputNullWorkflow() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = null;
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "");
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetTaskInputNullTaskDefinition() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = null;

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "");
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetTaskInputEmptyInputParams() {
        Map<String, Object> inputParams = Collections.emptyMap();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "");
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetTaskInputValidData() throws JsonProcessingException {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = Mockito.spy(new WorkflowModel());
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());
        taskDefinition.getInputTemplate().put("inputParamKey", "testValue");

        when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(1L);

        when(objectMapper.writeValueAsBytes(inputParams)).thenReturn(new byte[0]);
        when(objectMapper.readValue(anyByteArray(), mapType)).thenReturn(inputParams);

        when(workflow.getInput()).thenReturn(new HashMap<>());
        when(workflow.getInput().get("inputParamKey")).thenReturn("workflowValue");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "");

        assertEquals(Collections.singletonMap("inputParamKey", "workflowValue"), result);
    }

    @Test
    public void testGetTaskInputV2WorkflowWithTasks() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        WorkflowModel.WorkflowTask workflowTask = new WorkflowModel.WorkflowTask();
        workflowTask.setInputData(new HashMap<>());
        workflowTask.getInputData().put("taskInputParamKey", "taskValue");
        workflow.setTasks(Collections.singletonList(workflowTask));

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());

        when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(2L);

        when(objectMapper.writeValueAsBytes(inputParams)).thenReturn(new byte[0]);
        when(objectMapper.readValue(anyByteArray(), mapType)).thenReturn(inputParams);

        when(workflow.getInput()).thenReturn(new HashMap<>());
        when(workflow.getInput().get("inputParamKey")).thenReturn("workflowValue");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "");

        assertEquals(
                new LinkedHashMap<String, Object>() {
                    {
                        put("inputParamKey", "workflowValue");
                        put(
                                TaskUtils.removeIterationFromTaskRefName(
                                        workflowTask.getReferenceTaskName()),
                                workflowTask);
                    }
                },
                result);
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() {
        String paramString = "${VARIABLE}";
        String expected = "testValue";

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read(eq("VARIABLE"))).thenReturn("testValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        String paramString = "${VARIABLE}";

        String result = parametersUtils.replaceVariables(paramString, null, null);
        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceVariablesWithNullEnvironmentVariable() {
        String paramString = "${VARIABLE}";
        String expected = "";

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read(eq("VARIABLE"))).thenReturn(null);

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithJsonPath() {
        String paramString = "$.name";
        Map<String, Object> json = new HashMap<>();
        json.put("name", "testValue");

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read(eq("$.name"))).thenReturn("testValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("testValue", result);
    }

    @Test
    public void testReplaceVariablesWithJsonPathAndTaskId() {
        String paramString = "$.tasks[0].inputData.taskInputParamKey";
        Map<String, Object> json = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        WorkflowModel.WorkflowTask workflowTask = new WorkflowModel.WorkflowTask();
        workflowTask.setInputData(new HashMap<>());
        workflowTask.getInputData().put("taskInputParamKey", "taskValue");
        workflow.setTasks(Collections.singletonList(workflowTask));

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read(eq("$.tasks[0].inputData.taskInputParamKey"))).thenReturn("taskValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, "workflowId");
        assertEquals("taskValue", result);
    }

    @Test
    public void testReplaceVariablesWithJsonPathAndNullDocumentContext() {
        String paramString = "$.tasks[0].inputData.taskInputParamKey";

        String result = parametersUtils.replaceVariables(paramString, null, null);
        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceVariablesWithJsonPathAndEmptyJson() {
        String paramString = "$.tasks[0].inputData.taskInputParamKey";
        Map<String, Object> json = new HashMap<>();

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals(paramString, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReplaceVariablesWithInvalidJsonPath() {
        String paramString = "$.tasks[0].inputData.taskInputParamKey.";

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        throwable(
                () -> parametersUtils.replaceVariables(paramString, documentContext, null));
    }

    @Test
    public void testReplaceVariablesWithInvalidJsonPathAndTaskId() {
        String paramString = "$.tasks[0].inputData.taskInputParamKey.";
        Map<String, Object> json = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        WorkflowModel.WorkflowTask workflowTask = new WorkflowModel.WorkflowTask();

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        throwable(
                () -> parametersUtils.replaceVariables(paramString, documentContext, "workflowId"));
    }

    @Test
    public void testCloneEmptyMap() throws JsonProcessingException {
        Map<String, Object> mapToClone = Collections.emptyMap();
        when(objectMapper.writeValueAsBytes(mapToClone)).thenReturn(new byte[0]);
        when(objectMapper.readValue(anyByteArray(), mapType)).thenReturn(mapToClone);

        Map<String, Object> result = parametersUtils.clone(mapToClone);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testCloneMap() throws JsonProcessingException {
        Map<String, Object> mapToClone = new HashMap<>();
        mapToClone.put("key1", "value1");
        mapToClone.put("key2", "value2");

        when(objectMapper.writeValueAsBytes(mapToClone)).thenReturn(new byte[0]);
        when(objectMapper.readValue(anyByteArray(), mapType)).thenReturn(mapToClone);

        Map<String, Object> result = parametersUtils.clone(mapToClone);
        assertEquals(mapToClone, result);
    }

    @Test(expected = RuntimeException.class)
    public void testCloneMapWithIoException() throws JsonProcessingException {
        Map<String, Object> mapToClone = new HashMap<>();
        mapToClone.put("key1", "value1");
        mapToClone.put("key2", "value2");

        when(objectMapper.writeValueAsBytes(mapToClone))
                .thenThrow(new IOException("IOException occurred while writing bytes"));

        throwable(() -> parametersUtils.clone(mapToClone));
    }

    @Test
    public void testReplaceWithEmptyJson() {
        String paramString = "{}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceWithJson() {
        String paramString = "{\"key\":\"value\"}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read(".key")).thenReturn("testValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("{\"key\":\"testValue\"}", result);
    }

    @Test
    public void testReplaceWithJsonAndTaskId() {
        String paramString = "{\"key\":\"$.tasks[0].inputData.taskInputParamKey\"}";
        Map<String, Object> json = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        WorkflowModel.WorkflowTask workflowTask = new WorkflowModel.WorkflowTask();
        workflowTask.setInputData(new HashMap<>());
        workflowTask.getInputData().put("taskInputParamKey", "taskValue");
        workflow.setTasks(Collections.singletonList(workflowTask));

        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        when(documentContext.read(".tasks[0].inputData.taskInputParamKey")).thenReturn("taskValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, "workflowId");
        assertEquals("{\"key\":\"taskValue\"}", result);
    }

    @Test
    public void testReplaceWithJsonAndNullDocumentContext() {
        String paramString = "{\"key\":\"$.tasks[0].inputData.taskInputParamKey\"}";

        String result = parametersUtils.replaceVariables(paramString, null, null);
        assertEquals(paramString, result);
    }

    private static void checkArgument(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void throwable(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            throw e;
        }
    }

    private static TypeReference<Map<String, Object>> mapType = new TypeReference<>() {};
}