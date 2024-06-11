package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class ParametersUtilsTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String WORKFLOW_NAME = "workflowName";
    private static final String VERSION = "1.0.0";
    private static final String CORRELATION_ID = "correlationId";
    private static final String REASON_FOR_INCOMPLETION = "reasonForIncompletion";
    private static final String WORKFLOW_TASK_ID = "workflowTaskId";
    private static final String TASK_REFERENCE_NAME = "taskReferenceName";
    private static final String TASK_NAME = "taskName";
    private static final String TASK_TYPE = "taskType";
    private static final String TASK_INPUT_DATA = "taskInputData";
    private static final String TASK_OUTPUT_DATA = "taskOutputData";
    private static final String WORKFLOW_INPUT = "workflowInput";
    private static final String WORKFLOW_OUTPUT = "workflowOutput";
    private static final String TASK_INPUT_NAME = "taskInputName";
    private static final String TASK_OUTPUT_NAME = "taskOutputName";
    private static final String INPUT_TEMPLATE = "inputTemplate";
    private static final String INPUT_EXPRESSION = "${workflow.%s.%s}";

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private DocumentContext documentContext;
    private TaskDef taskDef;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;
    private EnvUtils envUtils;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        documentContext = mock(DocumentContext.class);
        taskDef = mock(TaskDef.class);
        workflowDef = mock(WorkflowDef.class);
        workflowModel = mock(WorkflowModel.class);
        envUtils = mock(EnvUtils.class);
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput_workflowSchemaVersion1_inputParamsIsNull_returnsEmptyMap() {
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);

        Map<String, Object> inputParams = null;
        Map<String, Object> expectedResult = Collections.emptyMap();

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams, workflowModel, null, null);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInput_workflowSchemaVersion1_inputParamsIsNotEmpty_returnsTaskInput() {
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(TASK_INPUT_NAME, "taskInputValue");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put(TASK_INPUT_NAME, "taskInputValue");

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams, workflowModel, null, null);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInput_workflowSchemaVersion1_inputParamsIsNull_and_taskDefNotNull_returnsTaskInput() {
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);

        Map<String, Object> inputParams = null;
        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put(TASK_INPUT_NAME, taskDef.getInputTemplate().get(TASK_INPUT_NAME));

        when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap(TASK_INPUT_NAME, "taskInputValue"));

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams, workflowModel, taskDef, null);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInput_workflowSchemaVersion2_inputParamsIsNull_returnsEmptyMap() {
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);

        Map<String, Object> inputParams = null;
        Map<String, Object> expectedResult = Collections.emptyMap();

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams, workflowModel, null, null);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInput_workflowSchemaVersion2_inputParamsIsNotEmpty_returnsReplacedTaskInput() {
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(TASK_INPUT_NAME, "${workflow.workflowName.taskName}");

        Map<String, Object> expectedWorkflow = new HashMap<>();
        expectedWorkflow.put(WORKFLOW_ID, WORKFLOW_ID);
        expectedWorkflow.put(WORKFLOW_NAME, WORKFLOW_NAME);
        expectedWorkflow.put(VERSION, VERSION);
        expectedWorkflow.put("input", Collections.singletonMap(TASK_NAME, "taskNameInputValue"));
        expectedWorkflow.put("output", Collections.singletonMap(TASK_NAME, "taskNameOutputValue"));

        when(workflowModel.getInput()).thenReturn(expectedWorkflow);

        Map<String, Object> expectedTask = new HashMap<>();
        expectedTask.put(TASK_REFERENCE_NAME, TASK_REFERENCE_NAME);
        expectedTask.put(TASK_TYPE, TASK_TYPE);
        expectedTask.put(TASK_INPUT_DATA, Collections.singletonMap(TASK_OUTPUT_NAME, "taskOutputNameValue"));
        expectedTask.put(TASK_OUTPUT_DATA, Collections.singletonMap(TASK_INPUT_NAME, "taskInputNameValue"));

        when(workflowModel.getTasks())
                .thenReturn(Collections.singletonList(new TaskModel(expectedTask)));

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put(TASK_INPUT_NAME, "taskNameOutputValue");

        when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(map.getType()))).then(
                invocation -> invocation.getArgument(0));

        when(documentContext.read(Any.class)).thenReturn(expectedWorkflow);

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams, workflowModel, null, null);

        assertEquals(expectedResult, result);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTaskInput_workflowSchemaVersion2_inputParamsIsNotEmpty_throwsRuntimeException_whenCloningInputTemplateFails() {
        when(workflowModel.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(TASK_INPUT_NAME, "${workflow.workflowName.taskName}");

        when(workflowModel.getInput()).thenReturn(new HashMap<>());

        when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap(TASK_INPUT_NAME, "taskInputValue"));

        when(objectMapper.writeValueAsBytes(ArgumentMatchers.any(Map.class))).thenThrow(new IOException());

        parametersUtils.getTaskInput(
                inputParams, workflowModel, taskDef, null);
    }

    @Test
    public void testReplace_jsonStringIsNull_returnsNull() {
        Map<String, Object> input = new HashMap<>();
        input.put(TASK_INPUT_NAME, null);

        Object result = parametersUtils.replace(input, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplace_jsonStringIsEmpty_returnsEmptyMap() {
        Map<String, Object> input = new HashMap<>();
        input.put(TASK_INPUT_NAME, "{}");

        Map<String, Object> expectedResult = new HashMap<>();

        when(documentContext.read(ArgumentMatchers.eq(INPUT_TEMPLATE))).thenReturn(Collections.emptyMap());

        Map<String, Object> result = parametersUtils.replace(input, documentContext, null);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_jsonStringIsNotEmpty_returnsReplacedJsonString() {
        Map<String, Object> input = new HashMap<>();
        input.put(TASK_INPUT_NAME, "{\"input\":{\"input1\":\"value1\"}}");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put(TASK_INPUT_NAME, "{\"input\":{\"input1\":\"replacedValue1\", \"input2\":\"value2\"}}");
        expectedResult.put(INPUT_TEMPLATE, "{\"input\":{\"input1\":\"$\\{inputParams.input1}\", \"input2\":\"value2\"}}");

        when(documentContext.read(INPUT_TEMPLATE)).thenReturn(expectedResult.get(INPUT_TEMPLATE));
        when(input.get(TASK_INPUT_NAME)).thenReturn(input.get(TASK_INPUT_NAME));
        when(input.getOrDefault(INPUT_TEMPLATE, null)).thenReturn(null);

        Map<String, Object> result = parametersUtils.replace(input, documentContext, null);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_jsonStringIsNotEmpty_whenTaskDefNotNull_returnsMergedJsonString() {
        Map<String, Object> input = new HashMap<>();
        input.put(TASK_INPUT_NAME, "{\"input\":{\"input1\":\"value1\"}}");

        Map<String, Object> expectedTaskInputTemplate = new HashMap<>();
        expectedTaskInputTemplate.put(TASK_INPUT_NAME, "{\"input\":{\"input1\":\"$\\{inputParams.input1}\", \"input2\":\"value2\"}}");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put(TASK_INPUT_NAME, "{\"input\":{\"input1\":\"replacedValue1\", \"input2\":\"value2\", \"input3\":\"taskDefInput3Value\"}}");

        when(documentContext.read(INPUT_TEMPLATE)).thenReturn(expectedTaskInputTemplate.get(INPUT_TEMPLATE));
        when(input.get(TASK_INPUT_NAME)).thenReturn(input.get(TASK_INPUT_NAME));
        when(input.getOrDefault(INPUT_TEMPLATE, null)).thenReturn(null);
        when(taskDef.getInputTemplate()).thenReturn(expectedTaskInputTemplate);

        Map<String, Object> result = parametersUtils.replace(input, documentContext, taskDef);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_stringIsNotEmpty_returnsReplacedString() {
        String inputString = "${workflow.%s.%s}";

        String expectedResult = "${" + WORKFLOW_NAME + "." + TASK_NAME + "}";

        Map<String, Object> workflow = new HashMap<>();
        workflow.put(WORKFLOW_NAME, WORKFLOW_NAME);
        workflow.put(TASK_NAME, TASK_NAME);

        when(documentContext.read(INPUT_TEMPLATE)).thenReturn(Collections.emptyMap());

        String result = parametersUtils.replaceVariables(inputString, documentContext, null);

        assertEquals(expectedResult, result);
    }

    // Add additional test cases for other methods (getWorkflowInput, replaceList, replaceVariables)
}