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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Test class for ParametersUtils.
 */
public class ParametersUtilsTest {

    private static final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private static final WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
    private static final WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
    private static final TaskDef taskDef = Mockito.mock(TaskDef.class);
    private static final TaskModel taskModel = Mockito.mock(TaskModel.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "taskId";

        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(1);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel));
        when(taskModel.getReferenceTaskName()).thenReturn("taskReference");
        when(taskModel.getTaskDefName()).thenReturn("taskDefName");
        when(taskModel.getTaskType()).thenReturn("taskType");
        when(taskModel.getInputData()).thenReturn(Collections.singletonMap("inputKey", "inputValue"));
        when(taskModel.getOutputData()).thenReturn(Collections.singletonMap("outputKey", "outputValue"));
        when(taskModel.getStatus()).thenReturn(TaskUtils.Status.SUCCEEDED);

        Map<String, Object> result =
                ParametersUtils.getInstance(objectMapper).getTaskInput(inputParams, workflowModel, taskDefinition, taskId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("inputValue", result.get("inputKey"));
        assertEquals("outputValue", result.get("outputKey"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "taskId";

        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(2);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel));
        when(taskModel.getReferenceTaskName()).thenReturn("taskReference");
        when(taskModel.getTaskDefName()).thenReturn("taskDefName");
        when(taskModel.getTaskType()).thenReturn("taskType");
        when(taskModel.getInputTemplate()).thenReturn(Map.of("inputKey", "inputValue"));
        when(taskModel.getStatus()).thenReturn(TaskUtils.Status.SUCCEEDED);

        Map<String, Object> result =
                ParametersUtils.getInstance(objectMapper).getTaskInput(inputParams, workflowModel, taskDefinition, taskId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("inputValue", result.get("inputKey"));
        assertEquals(null, result.get("outputKey"));
    }

    @Test
    public void testGetTaskInputV2WithInputTemplateOverriden() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "taskId";

        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(2);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel));
        when(taskModel.getReferenceTaskName()).thenReturn("taskReference");
        when(taskModel.getTaskDefName()).thenReturn("taskDefName");
        when(taskModel.getTaskType()).thenReturn("taskType");
        when(taskModel.getInputTemplate()).thenReturn(Map.of("inputKey", "overrideValue"));
        when(taskModel.getInputData()).thenReturn(Collections.singletonMap("inputKey", "inputValue"));
        when(taskModel.getStatus()).thenReturn(TaskUtils.Status.SUCCEEDED);

        Map<String, Object> result =
                ParametersUtils.getInstance(objectMapper).getTaskInput(inputParams, workflowModel, taskDefinition, taskId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("overrideValue", result.get("inputKey"));
        assertEquals("inputValue", result.get("outputKey"));
    }

    @Test
    public void testReplace() {
        String paramString = "${workflow.input.inputKey}${taskDefName}.outputKey";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(Map.class))).thenReturn(
                Map.of("workflow", Map.of("input", Map.of("inputKey", "inputValue"))));
        when(documentContext.read(ArgumentMatchers.anyString())).thenReturn("workflowValue");

        Object result = ParametersUtils.getInstance(objectMapper).replace(paramString, documentContext, null);

        assertEquals("inputValueworkflowValue.outputKey", result);
    }

    @Test
    public void testReplaceWithEnvironmentVariable() {
        String paramString = "${envVar}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        when(EnvUtils.getSystemParametersValue(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn("envValue");

        Object result = ParametersUtils.getInstance(objectMapper).replace(paramString, documentContext, null);

        assertEquals("envValue", result);
    }

    @Test
    public void testReplaceWithNullDocumentContext() {
        String paramString = "${workflow.input.inputKey}${taskDefName}.outputKey";

        Object result = ParametersUtils.getInstance(objectMapper).replace(paramString, null, null);

        assertNull(result);
    }

    @Test
    public void testReplaceWhenExpressionIsBlank() {
        String paramString = "${}";

        Object result = ParametersUtils.getInstance(objectMapper).replace(paramString, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.inputKey}${taskDefName}.outputKey";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(Map.class))).thenReturn(
                Map.of("workflow", Map.of("input", Map.of("inputKey", "inputValue"))));
        when(documentContext.read(ArgumentMatchers.anyString())).thenReturn("workflowValue");

        Object result =
                ParametersUtils.getInstance(objectMapper)
                        .replaceVariables(paramString, documentContext, null);

        assertEquals("inputValueworkflowValue.outputKey", result);
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        String paramString = "${workflow.input.inputKey}${taskDefName}.outputKey";

        Object result =
                ParametersUtils.getInstance(objectMapper)
                        .replaceVariables(paramString, null, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        String paramString = "${}";

        Object result =
                ParametersUtils.getInstance(objectMapper)
                        .replaceVariables(paramString, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() {
        String paramString = "${envVar}";

        Object result =
                ParametersUtils.getInstance(objectMapper)
                        .replaceVariables(paramString, documentContext, null);

        assertEquals("envValue", result);
    }

    @Test
    public void testReplaceVariablesWithEmptyStringAndEnvironmentVariable() {
        String paramString = "${envVar}${}";

        Object result =
                ParametersUtils.getInstance(objectMapper)
                        .replaceVariables(paramString, documentContext, null);

        assertEquals("envValue", result);
    }

    @Test(expected = RuntimeException.class)
    public void testCloneWithIOException() {
        when(objectMapper.writeValueAsBytes(ArgumentMatchers.any(Map.class))).thenThrow(IOException.class);

        Map<String, Object> result =
                ParametersUtils.getInstance(objectMapper).clone(Map.of("key", "value"));

        fail("Expected RuntimeException");
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowDef workflowDef = new WorkflowDef();

        when(workflowDef.getInputTemplate()).thenReturn(Map.of("inputKey", "inputValue"));

        Map<String, Object> result =
                ParametersUtils.getInstance(objectMapper).getWorkflowInput(workflowDef, inputParams);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("inputValue", result.get("inputKey"));
        assertEquals(null, result.get("outputKey"));
    }

    @Test
    public void testGetWorkflowInputWithNullInputTemplate() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowDef workflowDef = new WorkflowDef();

        Map<String, Object> result =
                ParametersUtils.getInstance(objectMapper).getWorkflowInput(workflowDef, inputParams);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    private static class Utils {
        public static void checkArgument(boolean condition, String message) {
            if (!condition) {
                throw new IllegalArgumentException(message);
            }
        }
    }

    private static ParametersUtils getInstance(ObjectMapper objectMapper) {
        return new ParametersUtils(objectMapper);
    }
}