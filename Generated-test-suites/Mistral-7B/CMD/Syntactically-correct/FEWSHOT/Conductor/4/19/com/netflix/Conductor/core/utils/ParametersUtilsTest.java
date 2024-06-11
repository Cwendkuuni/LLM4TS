package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.cli.util.EnvironmentUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class ParametersUtilsTest {

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;

    @Before
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        taskDef = new TaskDef();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInput() throws IOException {
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam1", "value1");
        WorkflowDef workflowDef = new WorkflowDef();
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(inputParams);
        workflowModel.setWorkflowDefinition(workflowDef);
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));
        when(objectMapper.readValue(anyByteArray(), Arguments.type(Map.class))).then(invocation -> invocation.getArguments()[0]);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        verify(objectMapper).readValue(ArgumentMatchers.anyByteArray(), Arguments.type(Map.class));
        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInputV2() throws IOException {
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam1", "value1");
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(1);
        workflowModel.setWorkflowDefinition(workflowDef);
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(inputParams);
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);

        assertNull(result);
    }

    @Test
    public void testGetTaskInputV2WithInput() throws IOException {
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam1", "value1");
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(2);
        Map<String, Map<String, Object>> inputMap = new HashMap<>();
        Map<String, Object> workflowParams = new HashMap<>();
        workflowParams.put("input", workflowDef.getInput());
        inputMap.put("workflow", workflowParams);
        workflowModel.setWorkflowDefinition(workflowDef);
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));
        TaskDef taskDef = new TaskDef();
        when(objectMapper.readValue(anyByteArray(), Arguments.type(Map.class))).then(invocation -> invocation.getArguments()[0]);

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);

        verify(objectMapper).readValue(ArgumentMatchers.anyByteArray(), Arguments.type(Map.class));
        assertEquals(inputParams, result);
    }

    @Test
    public void testReplace() {
        Object doc = new HashMap<>();
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read(anyString())).thenReturn("replacedValue");

        Object result = parametersUtils.replace(null, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceString() {
        String paramString = "${workflow.input.inputParam1}";
        DocumentContext documentContext = mock(DocumentContext.class);
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("inputParam1", "replacedValue");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("replacedValue", result);
    }

    @Test
    public void testReplaceEnvironmentVariable() {
        String paramString = "${ENV_VAR}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String envValue = "envValue";

        when(EnvironmentUtils.getSystemParametersValue(paramString, null)).thenReturn(envValue);

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(envValue, result);
    }

    @Test
    public void testReplaceList() {
        List<Object> values = Arrays.asList("${workflow.input.inputParam1}", "value2");
        DocumentContext documentContext = mock(DocumentContext.class);
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("inputParam1", "replacedValue");

        List<Object> result = parametersUtils.replaceList(values, null, documentContext);

        List<Object> expectedResult = Arrays.asList("replacedValue", "value2");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.inputParam1}.${workflow.output.outputParam1}";
        DocumentContext documentContext = mock(DocumentContext.class);
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("inputParam1", "replacedValue1");
        Map<String, Object> workflowOutput = new HashMap<>();
        workflowOutput.put("outputParam1", "replacedValue2");

        when(documentContext.read(ArgumentMatchers.anyString())).thenAnswer(invocation -> {
            String paramPath = invocation.getArgument(0);
            if (paramPath.equals("workflow.input.inputParam1")) {
                return replacedValue1;
            } else if (paramPath.equals("workflow.output.outputParam1")) {
                return replacedValue2;
            }
            throw new RuntimeException("Invalid parameter path");
        });

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("replacedValue1replacedValue2", result);
    }

    @Test
    public void testReplaceVariablesWithTaskId() {
        String paramString = "${taskId}.${workflow.input.inputParam1}";
        DocumentContext documentContext = mock(DocumentContext.class);
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("inputParam1", "replacedValue1");
        String taskId = "taskId";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals("taskIdreplacedValue1", result);
    }

    @Test
    public void testReplaceVariablesWithDepth() {
        String paramString = "${${workflow.input.inputParam1}}";
        DocumentContext documentContext = mock(DocumentContext.class);
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("inputParam1", "replacedValue1");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null, 1);

        assertEquals("replacedValue1", result);
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariablesWithInvalidExpression() {
        String paramString = "${workflow.input.inputParam1}.";
        DocumentContext documentContext = mock(DocumentContext.class);

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null, 0);
    }
}