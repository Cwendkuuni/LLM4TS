package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.jayway.jsonpath.DocumentContext;
import org.jayway.jsonpath.JsonPath;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowInstanceId;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;

public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);

    private ObjectMapper objectMapper;
    private Configuration configuration;
    private ParametersUtils utils;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        configuration = new Configuration.Builder().imports(ClassImposteriser.getInstance()).build();
        utils = new ParametersUtils(objectMapper);
        workflowDef = createMockWorkflowDef();
        taskDef = createMockTaskDef();
        workflowModel = createMockWorkflowModel();
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(workflowDef);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.myParam", "value1");
        inputParams.put("task.input.myParam", "value2");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("myParam", "value2");

        Map<String, Object> result = utils.getTaskInputV1(workflow, inputParams);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputV1_EmptyInputParams() {
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> inputParams = Collections.emptyMap();

        Map<String, Object> expectedResult = new HashMap<>();

        Map<String, Object> result = utils.getTaskInputV1(workflow, inputParams);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputV1_NullWorkflow() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.myParam", "value1");
        inputParams.put("task.input.myParam", "value2");

        Map<String, Object> result = utils.getTaskInputV1(null, inputParams);
        assertNull(result);
    }

    @Test
    public void testGetTaskInputV1_NullInputParams() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(workflowDef);

        Map<String, Object> result = utils.getTaskInputV1(workflow, null);
        assertNull(result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("myParam", "value1");

        DocumentContext documentContext = createMockDocumentContext();
        when(documentContext.read("$.workflow.input.myParam")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "value2";
            }
        });

        when(documentContext.read("$.task.input.myParam")).thenReturn(null);

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("myParam", "value2");

        Map<String, Object> result = utils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputV2_NullDocumentContext() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("myParam", "value1");

        Map<String, Object> result = utils.getTaskInputV2(inputParams, null, "taskId", taskDef);
        assertNull(result);
    }

    @Test
    public void testGetTaskInputV2_NullTaskDef() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("myParam", "value1");

        DocumentContext documentContext = createMockDocumentContext();
        when(documentContext.read("$.workflow.input.myParam")).thenReturn("value2");

        Map<String, Object> result = utils.getTaskInputV2(inputParams, workflowModel, "taskId", null);
        assertNull(result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.myParam}.${task.input.myParam}";
        DocumentContext documentContext = createMockDocumentContext();
        when(documentContext.read("$.workflow.input.myParam")).thenReturn("value1");
        when(documentContext.read("$.task.input.myParam")).thenReturn("value2");

        Object result = utils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals("value1.value2", result);
    }

    @Test
    public void testReplaceVariables_NullDocumentContext() {
        String paramString = "${workflow.input.myParam}.${task.input.myParam}";

        Object result = utils.replaceVariables(paramString, null, "taskId");
        assertNull(result);
    }

    @Test
    public void testReplaceVariables_EmptyDocumentContext() {
        String paramString = "${workflow.input.myParam}.${task.input.myParam}";
        DocumentContext documentContext = new DocumentContext(Collections.emptyMap());

        Object result = utils.replaceVariables(paramString, documentContext, "taskId");
        assertNull(result);
    }

    @Test
    public void testReplaceVariables_NoMatch() {
        String paramString = "${invalid.param}";
        DocumentContext documentContext = createMockDocumentContext();

        Object result = utils.replaceVariables(paramString, documentContext, "taskId");
        assertNull(result);
    }

    @Test
    public void testReplaceVariables_SystemVariable() {
        String paramString = "${SYSTEM_VARIABLE}";
        DocumentContext documentContext = createMockDocumentContext();
        String systemVariableValue = "value1";

        when(EnvUtils.getSystemParametersValue(paramString, "taskId")).thenReturn(systemVariableValue);

        Object result = utils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals(systemVariableValue, result);
    }

    @Test
    public void testReplaceVariables_JsonPathValue() {
        String paramString = "${$.workflow.input.myParam}";
        DocumentContext documentContext = createMockDocumentContext();
        when(documentContext.read("$.workflow.input.myParam")).thenReturn("value1");

        Object result = utils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals("value1", result);
    }

    @Test
    public void testReplaceVariables_JsonPathValueWithTaskId() {
        String paramString = "${$.task.output.myParam[0].mySubParam}";
        DocumentContext documentContext = createMockDocumentContext();
        List<Map<String, Object>> taskOutput = new LinkedList<>();
        Map<String, Object> output = new HashMap<>();
        output.put("mySubParam", "value1");
        taskOutput.add(output);
        when(documentContext.read("$.task.output")).thenReturn(taskOutput);

        Object result = utils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals("value1", result);
    }

    @Test
    public void testReplaceVariables_JsonPathValueWithEmptyList() {
        String paramString = "${$.task.output.myParam[0].mySubParam}";
        DocumentContext documentContext = createMockDocumentContext();
        List<Map<String, Object>> taskOutput = new LinkedList<>();

        when(documentContext.read("$.task.output")).thenReturn(taskOutput);

        Object result = utils.replaceVariables(paramString, documentContext, "taskId");
        assertNull(result);
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.myParam", "value1");

        WorkflowDef workflowDef = createMockWorkflowDef();
        when(workflowDef.getInputTemplate()).thenReturn(inputParams);

        Map<String, Object> result = utils.getWorkflowInput(workflowDef, Collections.emptyMap());
        assertEquals("value1", result.get("myParam"));
    }

    @Test
    public void testGetWorkflowInput_NullWorkflowDef() {
        Map<String, Object> result = utils.getWorkflowInput(null, Collections.emptyMap());
        assertNull(result);
    }

    private WorkflowDef createMockWorkflowDef() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        return workflowDef;
    }

    private TaskDef createMockTaskDef() {
        TaskDef taskDef = mock(TaskDef.class);
        when(taskDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        return taskDef;
    }

    private WorkflowModel createMockWorkflowModel() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowModel.getVariables()).thenReturn(Collections.emptyMap());
        return workflowModel;
    }

    private DocumentContext createMockDocumentContext() {
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read(anyString())).thenCall(invocation -> null);
        return documentContext;
    }
}