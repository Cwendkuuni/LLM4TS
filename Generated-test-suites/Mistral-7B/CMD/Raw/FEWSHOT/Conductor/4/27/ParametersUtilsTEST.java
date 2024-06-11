package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Pattern;
import java.util.stream.Collectors;

/**
 * Unit tests for the {@link ParametersUtils} class.
 */
public class ParametersUtilsTest {

    private static final String WORKFLOW_NAME = "myWorkflow";
    private static final String WORKFLOW_VERSION = "1.0";
    private static final String TASK_DEF_NAME = "myTaskDef";
    private static final String TASK_REF_NAME = "myTaskRef";
    private static final String TASK_INPUT_KEY = "myTaskInput";
    private static final String WORKFLOW_INPUT_KEY = "myWorkflowInput";
    private static final String WORKFLOW_OUTPUT_KEY = "myWorkflowOutput";
    private static final String TASK_OUTPUT_KEY = "myTaskOutput";
    private static final String ENV_VAR_KEY = "MY_ENV_VAR";

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);

        workflowModel = new WorkflowModel();
        workflowModel.setName(WORKFLOW_NAME);
        workflowModel.setVersion(WORKFLOW_VERSION);
        workflowModel.setInput(new HashMap<>());

        taskDef = new TaskDef();
        taskDef.setName(TASK_DEF_NAME);
        taskDef.setInputTemplate(new HashMap<>());
        taskDef.setTaskType("myTaskType");

        workflowModel.setTasks(Collections.singletonList(new TaskModel()));
        when(workflowModel.getTaskByRefName(TASK_REF_NAME)).thenReturn(taskDef);
    }

    @After
    public void tearDown() {
        reset(objectMapper);
    }

    @Test
    public void testGetTaskInput_NullInputParams_EmptyMapReturned() {
        assertEquals(Collections.emptyMap(), parametersUtils.getTaskInput(null, workflowModel, null, null));
    }

    @Test
    public void testGetTaskInput_NullWorkflow_EmptyMapReturned() {
        Map<String, Object> inputParams = new HashMap<>();
        assertEquals(Collections.emptyMap(), parametersUtils.getTaskInput(inputParams, null, null, null));
    }

    @Test
    public void testGetTaskInput_NullTaskDefinition_EmptyMapReturned() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        assertEquals(Collections.emptyMap(), parametersUtils.getTaskInput(inputParams, workflowModel, null, null));
    }

    @Test
    public void testGetTaskInput_NoTaskDefinitionInputTemplate_InputParamsUsed() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(TASK_INPUT_KEY, "taskInputValue");
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));
        when(workflowModel.getTaskByRefName(TASK_REF_NAME)).thenReturn(taskDef);
        when(taskDef.getInputTemplate()).thenReturn(Collections.emptyMap());

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        assertEquals(inputParams, result);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void testGetTaskInput_TaskDefinitionInputTemplate_InputParamsMerged() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put(TASK_INPUT_KEY, "taskInputTemplateValue");
        taskDef.setInputTemplate(inputTemplate);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(TASK_INPUT_KEY, "taskInputValue");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.putAll(inputTemplate);
        expectedResult.putAll(inputParams);

        assertEquals(expectedResult, result);
        verify(objectMapper, times(1)).readValue(anyByteArray(), map);
    }

    @Test
    public void testGetTaskInput_WorkflowInputTemplate_WorkflowInputUsed() {
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put(WORKFLOW_INPUT_KEY, "workflowInputValue");
        workflowModel.setInput(workflowInput);

        Map<String, Object> result = parametersUtils.getTaskInput(Collections.emptyMap(), workflowModel, null, null);

        assertEquals(workflowInput, result);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void testGetTaskInput_WorkflowInputTemplateAndTaskDefinitionInputTemplate_WorkflowInputUsed() {
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put(WORKFLOW_INPUT_KEY, "workflowInputValue");
        workflowModel.setInput(workflowInput);

        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put(TASK_INPUT_KEY, "taskInputTemplateValue");
        taskDef.setInputTemplate(inputTemplate);

        Map<String, Object> result = parametersUtils.getTaskInput(Collections.emptyMap(), workflowModel, null, null);

        assertEquals(workflowInput, result);
        verify(objectMapper, times(0)).readValue(anyByteArray(), map);
    }

    @Test
    public void testReplace_String_EnvironmentVariableValueReplaced() {
        String paramString = "${" + ENV_VAR_KEY + "}";
        String envVarValue = "envVarValue";
        EnvironmentVariables environmentVariables = mock(EnvironmentVariables.class);
        when(environmentVariables.getSystemParametersValue(ENV_VAR_KEY, null)).thenReturn(envVarValue);

        Object replacement = parametersUtils.replaceVariables(paramString, null, null);

        assertEquals(envVarValue, replacement);
    }

    @Test
    public void testReplace_String_JsonPathExpressionValueReplaced() {
        String paramString = "${$.myArray[0]}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.myArray[0]")).thenReturn("documentContextValue");

        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("documentContextValue", replacement);
    }

    @Test
    public void testReplace_String_EmptyJsonPathExpression_ValueUnchanged() {
        String paramString = "${$.myArray[0]}";
        DocumentContext documentContext = mock(DocumentContext.class);

        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(replacement);
    }

    @Test
    public void testReplace_String_InvalidJsonPathExpression_ValueUnchanged() {
        String paramString = "${$.myArray[0]}FOO";
        DocumentContext documentContext = mock(DocumentContext.class);

        Object replacement = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(replacement);
    }

    @Test
    public void testReplace_StringWithList_JsonPathExpressionValueReplacedForEachElement() {
        String paramString = "${[0]['$.myArray[0]'], [1]['$.myArray[1]']}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.myArray[0]")).thenReturn("documentContextValue0");
        when(documentContext.read("$.myArray[1]")).thenReturn("documentContextValue1");

        List<Object> replacement = (List<Object>) parametersUtils.replaceVariables(paramString, documentContext, null);

        List<Object> expectedReplacement = new LinkedList<>();
        expectedReplacement.add("documentContextValue0");
        expectedReplacement.add("documentContextValue1");

        assertEquals(expectedReplacement, replacement);
    }

    @Test
    public void testReplace_StringWithMap_JsonPathExpressionValueReplacedForEachKeyValuePair() {
        String paramString = "${$.myMap['key1'], $.myMap['key2']}";
        DocumentContext documentContext = mock(DocumentContext.class);
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("key1", "documentContextValue1");
        myMap.put("key2", "documentContextValue2");
        when(documentContext.read("$.myMap")).thenReturn(myMap);

        List<Map.Entry<String, Object>> replacement =
                (List<Map.Entry<String, Object>>) parametersUtils.replaceVariables(paramString, documentContext, null);

        List<Map.Entry<String, Object>> expectedReplacement = new LinkedList<>();
        Map.Entry<String, Object> entry1 = Map.entry("key1", "documentContextValue1");
        Map.Entry<String, Object> entry2 = Map.entry("key2", "documentContextValue2");
        expectedReplacement.add(entry1);
        expectedReplacement.add(entry2);

        assertEquals(expectedReplacement, replacement);
    }

    @Test
    public void testReplace_StringWithMap_JsonPathExpressionValueReplacedForEachKeyValuePair_EmptyMap() {
        String paramString = "${$.myMap['key1'], $.myMap['key2']}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.myMap")).thenReturn(Collections.emptyMap());

        List<Map.Entry<String, Object>> replacement =
                (List<Map.Entry<String, Object>>) parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(Collections.emptyList(), replacement);
    }

    @Test
    public void testGetTaskInputV1_WorkflowInput_InputUsed() {
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put(WORKFLOW_INPUT_KEY, "workflowInputValue");
        workflowModel.setInput(workflowInput);

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, null);

        assertEquals(workflowInput, result);
    }

    @Test
    public void testGetTaskInputV1_TaskInput_InputUsed() {
        Map<String, Object> taskInput = new HashMap<>();
        taskInput.put(TASK_INPUT_KEY, "taskInputValue");
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put(TASK_INPUT_KEY, "workflowInputValue");
        workflowModel.setInput(workflowInput);
        taskDef.setInputData(taskInput);

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, null);

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.putAll(taskInput);

        assertEquals(expectedResult, result);
    }
}