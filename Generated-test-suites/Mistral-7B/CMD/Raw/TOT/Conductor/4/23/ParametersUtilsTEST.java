package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    @Mock private Logger logger;
    @Mock private ObjectMapper objectMapper;

    @InjectMocks private ParametersUtils parametersUtils;

    private WorkflowModel workflowModel;
    private TaskDef taskDefinition;
    private Map<String, Object> inputParams;

    @Before
    public void setup() {
        workflowModel = mock(WorkflowModel.class);
        taskDefinition = mock(TaskDef.class);
        inputParams = new HashMap<>();
    }

    @Test
    public void testGetTaskInput() {
        // Test with valid input
        when(workflowModel.getInput()).thenReturn(new HashMap<>());
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskDefinition));
        when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("testKey", "testValue"));

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");

        assertEquals("testValue", result.get("testKey"));
        assertNull(result.get("nonExistentKey"));
        verifyGetTaskInputCall(workflowModel, taskDefinition, inputParams);

        // Test with empty input
        workflowModel.getInput().clear();
        result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");
        assertNull(result.get("testKey"));

        // Test with no task definition
        workflowModel.getTasks().clear();
        result = parametersUtils.getTaskInput(inputParams, workflowModel, null, "taskId");
        assertNull(result.get("testKey"));

        // Test with null inputParams
        inputParams = null;
        result = parametersUtils.getTaskInput(null, workflowModel, taskDefinition, "taskId");
        assertNull(result.get("testKey"));
    }

    @Test
    public void testReplace() {
        String jsonString = "{\"testKey\": \"${workflow.input.test}\"}";
        Object json = JsonPath.parse(jsonString);

        // Test with valid workflow input
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("test", "testValue");
        when(workflowModel.getInput()).thenReturn(workflowInput);

        Object result = parametersUtils.replace(json, workflowModel);

        assertEquals("testValue", result);

        // Test with null workflow input
        workflowInput = null;
        result = parametersUtils.replace(json, workflowModel);
        assertNull(result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.test}.${workflow.output.test}";

        // Test with valid document context
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("workflow.input.test", "testValue1");
        documentContext.put("workflow.output.test", "testValue2");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("testValue1.testValue2", result);

        // Test with null document context
        documentContext = null;
        result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertNull(result);
    }

    @Test
    public void testReplaceList() {
        String jsonString = "[{\"testKey\": \"${workflow.input.test}\"}]";
        Object json = JsonPath.parse(jsonString);

        // Test with valid workflow input
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("test", "testValue1");
        when(workflowModel.getInput()).thenReturn(workflowInput);

        Object result = parametersUtils.replaceList((List<?>) json, "taskId", workflowModel);

        assertEquals("[{\"testKey\": \"testValue1\"}]", result.toString());

        // Test with null workflow input
        workflowInput = null;
        result = parametersUtils.replaceList((List<?>) json, "taskId", workflowModel);
        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithInvalidPath() {
        String paramString = "${workflow.${invalidPath}.test}";

        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("workflow.input.test", "testValue1");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() {
        String paramString = "${env.TEST_VARIABLE}";

        Map<String, Object> documentContext = new HashMap<>();
        String envVariableValue = "testValueFromEnv";
        when(EnvUtils.getSystemParametersValue(paramString, "taskId"))
                .thenReturn(envVariableValue);

        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");

        assertEquals(envVariableValue, result);
    }

    @Test
    public void testReplaceVariablesWithNullValueInDocumentContext() {
        String paramString = "${workflow.input.nonExistentKey}";

        Map<String, Object> documentContext = new HashMap<>();

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithNoMatchingValueInDocumentContextAndEnvUtils() {
        String paramString = "${workflow.input.nonExistentKey}";

        Map<String, Object> documentContext = new HashMap<>();
        String envVariableValue = null;

        when(EnvUtils.getSystemParametersValue(paramString, "taskId"))
                .thenReturn(envVariableValue);

        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithEmptyDocumentContext() {
        String paramString = "${workflow.input.test}";

        Map<String, Object> documentContext = new HashMap<>();

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        String paramString = "${}";

        Map<String, Object> documentContext = new HashMap<>();

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("", result);
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariablesWithExceptionInJsonPath() {
        String paramString = "${workflow.input.test}.nonExistentKey";

        Map<String, Object> documentContext = new HashMap<>();

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithEmptyTaskDefinition() {
        String paramString = "${workflow.input.test}";

        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> documentContext = new HashMap<>();

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithEmptyWorkflowModel() {
        String paramString = "${workflow.input.test}";

        WorkflowModel workflowModel = new WorkflowModel();

        Map<String, Object> documentContext = new HashMap<>();

        Object result = parametersUtils.replaceVariables(paramString, documentContext, workflowModel);

        assertNull(result);
    }

    @Test
    public void testGetTaskInputV1() {
        // Test with valid input
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("test", "testValue");
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(workflowInput);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskDefinition));
        when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("testKey", "testValue"));

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertEquals("testValue", result.get("testKey"));
        assertNull(result.get("nonExistentKey"));

        // Test with no input template
        taskDefinition.setInputTemplate(null);

        result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertNull(result.get("testKey"));

        // Test with null input template
        taskDefinition.setInputTemplate(null);

        result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertNull(result.get("testKey"));
    }

    @Test
    public void testGetWorkflowInput() {
        // Test with valid input template
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("testKey", "testValue");
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(inputTemplate);

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("testValue", result.get("testKey"));
        assertNull(result.get("nonExistentKey"));

        // Test with no input template
        workflowDef.setInputTemplate(null);

        result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNull(result.get("testKey"));
    }

    private void verifyGetTaskInputCall(WorkflowModel workflowModel, TaskDef taskDefinition, Map<String, Object> inputParams) {
        parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");

        Map<String, Object> expectedInput = new HashMap<>();
        expectedInput.putAll(workflowModel.getInput());
        expectedInput.putAll(taskDefinition.getInputTemplate());

        assertEquals(expectedInput, inputParams);
        verify(logger).debug(
                "getTaskInput called with workflowModel={}, taskDefinition={}, inputParams={}",
                workflowModel,
                taskDefinition,
                inputParams);
    }
}