package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.slf4j.LoggerFactory;

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

/** Used to parse and resolve the JSONPath bindings in the workflow and task definitions. */
public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TaskUtils taskUtils;

    @Mock
    private EnvUtils envUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflowModel = createMockWorkflowModel();
        TaskDef taskDefinition = createMockTaskDefinition();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");
        assertEquals(5, taskInput.size());
        assertEquals("workflowName", taskInput.get("workflow.workflowName"));
        assertEquals("workflowVersion", taskInput.get("workflow.workflowVersion"));
        assertEquals("workflowId", taskInput.get("workflow.workflowId"));
        assertEquals("taskId", taskInput.get("task.taskId"));
        assertEquals("taskType", taskInput.get("task.taskType"));
        verify(taskUtils, times(1)).getTaskDefName(taskDefinition);
        verify(envUtils, never()).getSystemParametersValue(anyString(), anyString());
    }

    @Test
    public void testGetTaskInputWithEnvVariable() {
        WorkflowModel workflowModel = createMockWorkflowModel();
        TaskDef taskDefinition = createMockTaskDefinition();
        Map<String, Object> inputParams = new HashMap<>();
        when(envUtils.getSystemParametersValue(eq("ENV_VAR"), anyString())).thenReturn("envValue");

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");
        assertEquals(6, taskInput.size());
        assertEquals("workflowName", taskInput.get("workflow.workflowName"));
        assertEquals("workflowVersion", taskInput.get("workflow.workflowVersion"));
        assertEquals("workflowId", taskInput.get("workflow.workflowId"));
        assertEquals("taskId", taskInput.get("task.taskId"));
        assertEquals("taskType", taskInput.get("task.taskType"));
        assertEquals("envValue", taskInput.get("task.ENV_VAR"));
        verify(taskUtils, times(1)).getTaskDefName(taskDefinition);
        verify(envUtils, times(1)).getSystemParametersValue(eq("ENV_VAR"), anyString());
    }

    @Test
    public void testGetTaskInputWithInputTemplate() {
        WorkflowModel workflowModel = createMockWorkflowModel();
        TaskDef taskDefinition = createMockTaskDefinition();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("input.param", "value");

        when(taskDefinition.getInputTemplate()).thenReturn(inputTemplate);

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");
        assertEquals(7, taskInput.size());
        assertEquals("workflowName", taskInput.get("workflow.workflowName"));
        assertEquals("workflowVersion", taskInput.get("workflow.workflowVersion"));
        assertEquals("workflowId", taskInput.get("workflow.workflowId"));
        assertEquals("taskId", taskInput.get("task.taskId"));
        assertEquals("taskType", taskInput.get("task.taskType"));
        assertEquals("value", taskInput.get("task.input.param"));
        verify(taskUtils, never()).getTaskDefName(taskDefinition);
        verify(envUtils, never()).getSystemParametersValue(anyString(), anyString());
    }

    @Test
    public void testGetTaskInputWithWorkflowInputTemplate() {
        WorkflowDef workflowDef = createMockWorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("param", "value");

        when(workflowDef.getInputTemplate()).thenReturn(workflowInput);

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowDef, null, null);
        assertEquals(3, taskInput.size());
        assertEquals("param", taskInput.get("param"));
        verify(objectMapper, never()).readValue(anyByteArray(), eq(map));
        verify(objectMapper, never()).writeValueAsBytes(anyMap(Map.class.getClassLoader()));
        verify(envUtils, never()).getSystemParametersValue(anyString(), anyString());
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.workflowName}.${task.taskType}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";

        when(documentContext.read("workflow.workflowName")).thenReturn("workflowName");
        when(documentContext.read("task.taskType")).thenReturn("taskType");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        assertEquals("workflowName.taskType", result);

        verify(documentContext, times(2)).read(anyString());
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        String paramString = "${}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("", result);

        verify(documentContext, never()).read(anyString());
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        String paramString = "${workflow.workflowName}";
        String taskId = "taskId";

        Object result = parametersUtils.replaceVariables(paramString, null, taskId);
        assertEquals("", result);

        verify(documentContext, never()).read(anyString());
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() {
        String paramString = "${ENV_VAR}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";

        when(envUtils.getSystemParametersValue(eq("ENV_VAR"), anyString())).thenReturn("envValue");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        assertEquals("envValue", result);

        verify(documentContext, never()).read(anyString());
        verify(envUtils, times(1)).getSystemParametersValue(eq("ENV_VAR"), anyString());
    }

    @Test
    public void testReplaceVariablesWithEmptyEnvironmentVariable() {
        String paramString = "${ENV_VAR}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";

        when(envUtils.getSystemParametersValue(eq("ENV_VAR"), anyString())).thenReturn(null);

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        assertEquals("", result);

        verify(documentContext, never()).read(anyString());
        verify(envUtils, times(1)).getSystemParametersValue(eq("ENV_VAR"), anyString());
    }

    @Test
    public void testReplaceVariablesWithJsonPath() {
        String paramString = "${$.params.param}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";
        Map<String, Object> json = new HashMap<>();
        json.put("params", new HashMap<>() {{
            put("param", "value");
        }});

        when(documentContext.read("$.params")).thenReturn(json.get("params"));

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        assertEquals("value", result);

        verify(documentContext, times(1)).read(eq("$.params"));
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariablesWithException() {
        String paramString = "${workflow.workflowName}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";

        doThrow(RuntimeException.class).when(documentContext).read(eq("workflow.workflowName"));

        parametersUtils.replaceVariables(paramString, documentContext, taskId);

        verify(documentContext, times(1)).read(eq("workflow.workflowName"));
    }

    @Test
    public void testReplaceVariablesWithNullParamString() {
        String paramString = null;
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);
        assertEquals("", result);

        verify(documentContext, never()).read(anyString());
    }

    @Test
    public void testReplaceList() {
        List<Object> values = new LinkedList<>() {{
            add("${workflow.workflowName}");
            add("${task.taskType}");
        }};
        String taskId = "taskId";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        when(documentContext.read("workflow.workflowName")).thenReturn("workflowName");
        when(documentContext.read("task.taskType")).thenReturn("taskType");

        List<Object> replacedList = parametersUtils.replaceList(values, taskId, documentContext);
        assertEquals(2, replacedList.size());
        assertEquals("workflowName", replacedList.get(0));
        assertEquals("taskType", replacedList.get(1));

        verify(documentContext, times(2)).read(anyString());
    }

    @Test
    public void testReplaceListWithEmptyList() {
        List<Object> values = Collections.emptyList();
        String taskId = "taskId";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        List<Object> replacedList = parametersUtils.replaceList(values, taskId, documentContext);
        assertEquals(0, replacedList.size());

        verify(documentContext, never()).read(anyString());
    }

    @Test
    public void testReplaceListWithEnvironmentVariable() {
        List<Object> values = new LinkedList<>() {{
            add("${ENV_VAR}");
        }};
        String taskId = "taskId";

        when(envUtils.getSystemParametersValue(eq("ENV_VAR"), anyString())).thenReturn("envValue");

        List<Object> replacedList = parametersUtils.replaceList(values, taskId, documentContext);
        assertEquals(1, replacedList.size());
        assertEquals("envValue", replacedList.get(0));

        verify(documentContext, never()).read(anyString());
        verify(envUtils, times(1)).getSystemParametersValue(eq("ENV_VAR"), anyString());
    }

    @Test
    public void testReplaceListWithNullDocumentContext() {
        List<Object> values = new LinkedList<>() {{
            add("${workflow.workflowName}");
        }};
        String taskId = "taskId";

        List<Object> replacedList = parametersUtils.replaceList(values, taskId, null);
        assertEquals(0, replacedList.size());

        verify(documentContext, never()).read(anyString());
    }

    @Test
    public void testReplaceMap() {
        Map<String, Object> input = new HashMap<>();
        Map<String, Object> json = new HashMap<>();
        json.put("param1", "${workflow.workflowName}");
        json.put("param2", "${task.taskType}");
        input.put("input", json);
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";

        when(documentContext.read("workflow.workflowName")).thenReturn("workflowName");
        when(documentContext.read("task.taskType")).thenReturn("taskType");

        Map<String, Object> replacedMap = parametersUtils.replace(input, documentContext, taskId);
        assertEquals(6, replacedMap.size());
        assertEquals("workflowName", replacedMap.get("input.param1"));
        assertEquals("taskType", replacedMap.get("input.param2"));

        verify(documentContext, times(2)).read(anyString());
    }

    @Test
    public void testReplaceMapWithEmptyMap() {
        Map<String, Object> input = new HashMap<>();
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";

        Map<String, Object> replacedMap = parametersUtils.replace(input, documentContext, taskId);
        assertEquals(0, replacedMap.size());

        verify(documentContext, never()).read(anyString());
    }

    @Test
    public void testReplaceMapWithEnvironmentVariable() {
        Map<String, Object> input = new HashMap<>();
        Map<String, Object> json = new HashMap<>();
        json.put("param", "${ENV_VAR}");
        input.put("input", json);
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String taskId = "taskId";

        when(envUtils.getSystemParametersValue(eq("ENV_VAR"), anyString())).thenReturn("envValue");

        Map<String, Object> replacedMap = parametersUtils.replace(input, documentContext, taskId);
        assertEquals(3, replacedMap.size());
        assertEquals("envValue", replacedMap.get("input.param"));

        verify(documentContext, never()).read(anyString());
        verify(envUtils, times(1)).getSystemParametersValue(eq("ENV_VAR"), anyString());
    }

    private WorkflowModel createMockWorkflowModel() {
        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        when(workflowModel.getWorkflowDefinition()).thenReturn(createMockWorkflowDef());
        return workflowModel;
    }

    private WorkflowDef createMockWorkflowDef() {
        WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
        return workflowDef;
    }

    private TaskDef createMockTaskDefinition() {
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        return taskDefinition;
    }
}