package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;
    @SuppressWarnings("unchecked")
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInput() throws Exception {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = createWorkflowModel();
        TaskDef taskDefinition = createTaskDefinition();
        String taskId = "taskId";

        // When
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Then
        assertEquals(expectedMap(), result);
    }

    @Test
    public void testGetTaskInputV2() throws Exception {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = createWorkflowModel();
        TaskDef taskDefinition = createTaskDefinition();
        String taskId = "taskId";

        // When
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        // Then
        assertEquals(expectedMap(), result);
    }

    @Test
    public void testReplace() throws Exception {
        // Given
        String paramString = "${workflow.input.name}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replace(paramString, documentContext, taskId);

        // Then
        assertEquals("name", result);
    }

    @Test
    public void testReplaceVariables() throws Exception {
        // Given
        String paramString = "${workflow.input.name}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("name", result);
    }

    @Test
    public void testReplaceVariablesWithEmptyJsonPath() throws Exception {
        // Given
        String paramString = "{$}$";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariablesWithNullJsonPath() throws Exception {
        // Given
        String paramString = "${null}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals(null, result);
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() throws Exception {
        // Given
        String paramString = "${ENV_VAR}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";
        String envVar = "test";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals(envVar, result);
    }

    @Test
    public void testReplaceVariablesWithJsonPath() throws Exception {
        // Given
        String paramString = "${$.test.name}";
        DocumentContext documentContext = createDocumentContext();
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals("testName", result);
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariablesWithInvalidJsonPath() throws Exception {
        // Given
        String paramString = "${$.test.$}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";

        // When & Then
        parametersUtils.replaceVariables(paramString, documentContext, taskId);
    }

    @Test
    public void testReplaceVariablesWithEmptyInput() throws Exception {
        // Given
        String paramString = "${workflow.input.name}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";
        Map<String, Object> inputParams = new HashMap<>();

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals(null, result);
    }

    @Test
    public void testReplaceVariablesWithNullInput() throws Exception {
        // Given
        String paramString = "${workflow.input.name}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String taskId = "taskId";
        Map<String, Object> inputParams = null;

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals(null, result);
    }

    @Test
    public void testReplaceVariablesWithEmptyDocumentContext() throws Exception {
        // Given
        String paramString = "${workflow.input.name}";
        DocumentContext documentContext = new DocumentContext(Collections.emptyMap());
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals(null, result);
    }

    @Test
    public void testReplaceVariablesWithNonExistingJsonPath() throws Exception {
        // Given
        String paramString = "${$.test.nonExistingPath}";
        DocumentContext documentContext = createDocumentContext();
        String taskId = "taskId";

        // When
        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        // Then
        assertEquals(null, result);
    }

    private Map<String, Object> createWorkflowModel() {
        Map<String, Object> workflow = new HashMap<>();
        workflow.put("input", new HashMap<>());
        workflow.put("output", new HashMap<>());
        workflow.put("status", "testStatus");
        workflow.put("workflowId", "workflowId");
        workflow.put("parentWorkflowId", "parentWorkflowId");
        workflow.put("parentWorkflowTaskId", "parentWorkflowTaskId");
        workflow.put("workflowName", "workflowName");
        workflow.put("workflowVersion", "workflowVersion");
        workflow.put("correlationId", "correlationId");
        workflow.put("reasonForIncompletion", "reasonForIncompletion");
        workflow.put("schemaVersion", 1);
        workflow.put("variables", new HashMap<>());
        workflow.put("tasks", new LinkedList<>());
        return workflow;
    }

    private TaskDef createTaskDefinition() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("name", "testName");
        return new TaskDef("testTaskDefName", null, null, null, null, null, inputTemplate);
    }

    private DocumentContext createDocumentContext() {
        Map<String, Object> json = new HashMap<>();
        json.put("test", new HashMap<>());
        json.put("testName", "testName");
        return new DocumentContext(json);
    }

    private Map<String, Object> expectedMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("testName", "testName");
        return result;
    }
}