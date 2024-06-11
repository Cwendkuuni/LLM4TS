package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ParametersUtils}.
 */
public class ParametersUtilsTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String TASK_ID = "taskId";
    private static final String TASK_REFERENCE_NAME = "taskReferenceName";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final String WORKFLOW_VERSION = "workflowVersion";
    private static final String VARIABLE_KEY = "variableKey";
    private static final String ENV_VARIABLE = "envVariable";

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;
    private TaskModel taskModel;

    @Before
    public void setup() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
        MockitoAnnotations.initMocks(this);

        workflowDef = mock(WorkflowDef.class);
        workflowModel = mock(WorkflowModel.class);
        taskDef = mock(TaskDef.class);
        taskModel = mock(TaskModel.class);
    }

    @Test
    public void testGetTaskInput_BasicScenario() {
        // Given
        Map<String, Object> inputParams = new LinkedHashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = new WorkflowDef();
        TaskDef taskDef = new TaskDef();
        TaskModel taskModel = new TaskModel();

        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getInput()).thenReturn(Collections.singletonMap("inputKey", "inputValue"));
        when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("inputKeyTemplate", "{{inputKey}}"));
        when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("taskInputKey", "{{inputKey}}"));
        when(taskModel.getInputData()).thenReturn(Collections.singletonMap("taskInputKey", "taskInputValue"));
        when(taskModel.getTaskType()).thenReturn("taskType");

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, TASK_ID);

        // Then
        assertNotNull(result);
        assertEquals("inputValue", result.get("inputKey"));
        assertEquals("taskInputValue", result.get("taskInputKey"));
        assertEquals("taskType", result.get("taskType"));
    }

    @Test
    public void testGetTaskInput_InputTemplateIsNull() {
        // Given
        Map<String, Object> inputParams = new LinkedHashMap<>();
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskDef taskDef = new TaskDef();
        TaskModel taskModel = new TaskModel();

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, TASK_ID);

        // Then
        assertNotNull(result);
        assertNull(result.getOrDefault("taskInputKey", null));
    }

    @Test
    public void testGetTaskInput_WorkflowDefinitionIsNull() {
        // Given
        Map<String, Object> inputParams = new LinkedHashMap<>();
        WorkflowModel workflowModel = null;
        TaskDef taskDef = new TaskDef();
        TaskModel taskModel = new TaskModel();

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, TASK_ID);

        // Then
        assertNotNull(result);
        assertNull(result.getOrDefault("workflowInputKey", null));
    }

    @Test
    public void testGetTaskInput_InputTemplateAndWorkflowDefinitionBothNull() {
        // Given
        Map<String, Object> inputParams = new LinkedHashMap<>();
        WorkflowModel workflowModel = null;
        TaskDef taskDef = new TaskDef();
        TaskModel taskModel = new TaskModel();

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, TASK_ID);

        // Then
        assertNotNull(result);
        assertNull(result.getOrDefault("workflowInputKey", null));
        assertNull(result.getOrDefault("taskInputKey", null));
    }

    @Test
    public void testReplace_EmptyString() {
        // Given
        String paramString = "";
        DocumentContext documentContext = mock(DocumentContext.class);

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testReplace_StringWithEmptyJsonPath() {
        // Given
        String paramString = "${}";
        DocumentContext documentContext = mock(DocumentContext.class);

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertNull(result);
    }

    @Test
    public void testReplace_StringWithValidJsonPath() {
        // Given
        String paramString = "${workflow.input.inputKey}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.input.inputKey")).thenReturn("inputValue");

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("inputValue", result);
    }

    @Test
    public void testReplace_StringWithValidJsonPathAndVariable() {
        // Given
        String paramString = "${workflow.input.inputKey.variableKey}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.input.inputKey")).thenReturn(Collections.singletonMap("variableKey", "variableValue"));

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("variableValue", result);
    }

    @Test
    public void testReplace_StringWithValidJsonPathAndEnvironmentVariable() {
        // Given
        String paramString = "${workflow.input.inputKey.${ENV_VARIABLE}}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String envVariableValue = "envVariableValue";
        when(EnvUtils.getSystemParametersValue(ENV_VARIABLE, TASK_ID)).thenReturn(envVariableValue);

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("envVariableValue", result);
    }

    @Test(expected = RuntimeException.class)
    public void testReplace_StringWithInvalidJsonPath() {
        // Given
        String paramString = "${workflow.input.invalidPath}";
        DocumentContext documentContext = mock(DocumentContext.class);

        // When
        parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        // expected exception
    }

    @Test
    public void testReplace_StringArray() {
        // Given
        String paramString = "${taskDef.inputTemplate[0]} ${taskDef.inputTemplate[1]}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$[0]")).thenReturn("inputValue1");
        when(documentContext.read("$[1]")).thenReturn("inputValue2");

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals("inputValue1 inputValue2", result);
    }

    @Test
    public void testReplace_Map() {
        // Given
        String paramString = "${workflow.input}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.input")).thenReturn(new HashMap<String, Object>() {{
            put("inputKey1", "inputValue1");
            put("inputKey2", "inputValue2");
        }});

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals(new HashMap<String, Object>() {{
            put("inputKey1", "inputValue1");
            put("inputKey2", "inputValue2");
        }}, result);
    }

    @Test
    public void testReplace_List() {
        // Given
        String paramString = "${workflow.tasks[0].inputData}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.tasks[0].inputData")).thenReturn(new HashMap<String, Object>() {{
            put("inputKey1", "inputValue1");
            put("inputKey2", "inputValue2");
        }});

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        assertEquals(new HashMap<String, Object>() {{
            put("inputKey1", "inputValue1");
            put("inputKey2", "inputValue2");
        }}, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReplace_ListIndexOutOfBoundsException() {
        // Given
        String paramString = "${workflow.tasks[3].inputData}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.size()).thenReturn(3);

        // When
        parametersUtils.replaceVariables(paramString, documentContext, null);

        // Then
        // expected exception
    }

    @Test
    public void testReplaceList_EmptyList() {
        // Given
        String paramString = "${tasks[0]}";
        DocumentContext documentContext = mock(DocumentContext.class);

        // When
        List<Object> result = parametersUtils.replaceList(Collections.emptyList(), TASK_ID, documentContext);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReplaceList_SingleElementList() {
        // Given
        String paramString = "${tasks[0]}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$[0]")).thenReturn("taskElement");

        // When
        List<Object> result = parametersUtils.replaceList(Collections.singletonList("tasks"), TASK_ID, documentContext);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("taskElement", result.get(0));
    }

    @Test
    public void testReplaceList_MultiElementList() {
        // Given
        String paramString = "${tasks[0].inputData[0]} ${tasks[1].inputData[1]}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.size()).thenReturn(2);
        when(documentContext.read("$[0].inputData[0]")).thenReturn("inputElement1");
        when(documentContext.read("$[1].inputData[1]")).thenReturn("inputElement2");

        // When
        List<Object> result = parametersUtils.replaceList(Arrays.asList("tasks", "tasks"), TASK_ID, documentContext);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("inputElement1", result.get(0));
        assertEquals("inputElement2", result.get(1));
    }
}