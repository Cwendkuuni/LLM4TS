package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    private static final String WORKFLOW_NAME = "testWorkflow";
    private static final String WORKFLOW_VERSION = "1.0.0";
    private static final String WORKFLOW_ID = "testWorkflowId";
    private static final String CORRELATION_ID = "testCorrelationId";
    private static final String TASK_DEF_NAME = "testTaskDefName";
    private static final String TASK_REF_NAME = "testTaskRefName";
    private static final String TASK_TYPE = "testTaskType";
    private static final String TASK_INPUT_KEY = "testTaskInputKey";
    private static final String TASK_OUTPUT_KEY = "testTaskOutputKey";
    private static final String WORKFLOW_INPUT_KEY = "testWorkflowInputKey";
    private static final String WORKFLOW_OUTPUT_KEY = "testWorkflowOutputKey";
    private static final String WORKFLOW_INPUT_VAL = "testWorkflowInputVal";
    private static final String TASK_INPUT_VAL = "testTaskInputVal";
    private static final String TASK_OUTPUT_VAL = "testTaskOutputVal";

    private ParametersUtils utils;
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;
    private TaskModel taskModel;

    @Before
    public void setup() throws IOException {
        this.objectMapper = Mockito.spy(new ObjectMapper());
        this.workflowDef = Mockito.mock(WorkflowDef.class);
        this.workflowModel = new WorkflowModel();
        this.workflowModel.setName(WORKFLOW_NAME);
        this.workflowModel.setWorkflowDefinition(this.workflowDef);
        this.workflowModel.setWorkflowId(WORKFLOW_ID);
        this.workflowModel.setCorrelationId(CORRELATION_ID);
        this.workflowDef.setSchemaVersion(1);
        this.workflowDef.setInput(new HashMap<>());
        this.workflowDef.getInput().put(WORKFLOW_INPUT_KEY, WORKFLOW_INPUT_VAL);

        this.taskDef = Mockito.mock(TaskDef.class);
        this.taskDef.setInputTemplate(new HashMap<>());
        this.taskDef.getInputTemplate().put(TASK_INPUT_KEY, TASK_INPUT_VAL);
        this.taskDef.setTaskType(TASK_TYPE);

        this.taskModel = new TaskModel();
        this.taskModel.setTaskDefName(TASK_DEF_NAME);
        this.taskModel.setReferenceTaskName(TASK_REF_NAME);
        this.taskModel.setInputData(new HashMap<>());
        this.taskModel.getInputData().put(TASK_INPUT_KEY, TASK_INPUT_VAL);
        this.taskModel.setOutputData(new HashMap<>());
        this.taskModel.getOutputData().put(TASK_OUTPUT_KEY, TASK_OUTPUT_VAL);
        this.workflowModel.setTasks(List.of(this.taskModel));

        this.utils = new ParametersUtils(this.objectMapper);
        ReflectionTestUtils.setFieldValue(this.workflowModel, "status", null);
        ReflectionTestUtils.setFieldValue(this.workflowModel, "parentWorkflowId", null);
        ReflectionTestUtils.setFieldValue(this.workflowModel, "parentWorkflowTaskId", null);
        ReflectionTestUtils.setFieldValue(
                this.workflowModel, "reasonForIncompletion", null);
        ReflectionTestUtils.setFieldValue(
                this.workflowModel, "workflowInstanceId", null);
    }

    @Test
    public void testGetTaskInputV1() throws Exception {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(TASK_INPUT_KEY, TASK_INPUT_VAL);

        Map<String, Object> result = this.utils.getTaskInput(inputParams, this.workflowModel, null, null);
        assertEquals(TASK_INPUT_VAL, result.get(TASK_INPUT_KEY));
    }

    @Test
    public void testGetTaskInputV2() throws Exception {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(TASK_INPUT_KEY, TASK_INPUT_VAL);

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put(WORKFLOW_INPUT_KEY, WORKFLOW_INPUT_VAL);

        Map<String, Object> expectedResult = new LinkedHashMap<>();
        expectedResult.put(TASK_INPUT_KEY, TASK_INPUT_VAL);
        expectedResult.put(WORKFLOW_INPUT_KEY, WORKFLOW_INPUT_VAL);

        this.workflowDef.setInput(workflowInput);
        this.taskDef.setInputTemplate(new HashMap<>());

        Map<String, Object> result = this.utils.getTaskInput(inputParams, this.workflowModel, this.taskDef, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputWithNullWorkflow() throws Exception {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(TASK_INPUT_KEY, TASK_INPUT_VAL);

        Map<String, Object> result = this.utils.getTaskInput(inputParams, null, null, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testReplaceVariables() throws Exception {
        String jsonString = "{\"input\":{\"testKey\":\"${WORKFLOW_INPUT_VAL}\"},\"output\":{\"testKey\":\"${TASK_OUTPUT_VAL}\"}}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        String expectedJsonString =
                String.format(
                        "{\"input\":{\"testKey\":\"%s\"},\"output\":{\"testKey\":\"%s\"}}",
                        WORKFLOW_INPUT_VAL, TASK_OUTPUT_VAL);

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(expectedJsonString, Objects.toString(replacement));
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() throws Exception {
        String jsonString = "{\"input\":{\"testKey\":\"${WORKFLOW_INPUT_VAL}\"},\"output\":{\"testKey\":\"${TASK_OUTPUT_VAL}\"}}";

        Object replacement = this.utils.replaceVariables(jsonString, null, null);
        assertEquals(jsonString, Objects.toString(replacement));
    }

    @Test
    public void testReplaceVariablesWithEmptyDocumentContext() throws Exception {
        String jsonString = "{\"input\":{\"testKey\":\"${WORKFLOW_INPUT_VAL}\"},\"output\":{\"testKey\":\"${TASK_OUTPUT_VAL}\"}}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(jsonString, Objects.toString(replacement));
    }

    @Test
    public void testReplaceVariablesWithNoReplacements() throws Exception {
        String jsonString = "{\"input\":{\"testKey\":\"${WORKFLOW_INPUT_VAL}\"},\"output\":{\"testKey\":\"${TASK_OUTPUT_VAL}\"}}";
        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", new HashMap<>()));

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(jsonString, Objects.toString(replacement));
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() throws Exception {
        String jsonString = "{\"input\":{\"testKey\":\"${ENV_VAR_KEY}\"},\"output\":{\"testKey\":\"${TASK_OUTPUT_VAL}\"}}";
        String envVarValue = "envVarValue";
        System.setProperty("ENV_VAR_KEY", envVarValue);

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(
                String.format("{\"input\":{\"testKey\":\"%s\"},\"output\":{\"testKey\":\"%s\"}}", envVarValue, TASK_OUTPUT_VAL),
                Objects.toString(replacement));
    }

    @Test
    public void testReplaceVariablesWithWorkflowInput() throws Exception {
        String jsonString = "{\"input\":{\"testKey\":\"${workflow.input.testWorkflowInputKey}\"},\"output\":{\"testKey\":\"${task.output.testTaskOutputKey}\"}}";
        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", this.workflowModel));

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(
                String.format(
                        "{\"input\":{\"testKey\":\"%s\"},\"output\":{\"testKey\":\"%s\"}}",
                        WORKFLOW_INPUT_VAL, TASK_OUTPUT_VAL),
                Objects.toString(replacement));
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariablesWithNullReplacement() throws Exception {
        String jsonString = "{\"input\":{\"testKey\":\"${WORKFLOW_INPUT_VAL}\"},\"output\":{\"testKey\":\"${TASK_OUTPUT_VAL}\"}}";
        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", this.workflowModel));
        MethodUtils.invokeMethod(this.utils, "replaceVariables", jsonString, documentContext, null);
    }

    @Test
    public void testReplaceVariablesWithEmptyJsonString() throws Exception {
        String jsonString = "";
        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", this.workflowModel));

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(jsonString, Objects.toString(replacement));
    }

    @Test
    public void testReplaceVariablesWithJsonNode() throws Exception {
        String jsonString = "{\"input\":{\"testKey\":\"${WORKFLOW_INPUT_VAL}\"},\"output\":{\"testKey\":\"${TASK_OUTPUT_VAL}\"}}";
        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", new ObjectMapper().readTree(jsonString)));

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(jsonString, Objects.toString(replacement));
    }

    @Test
    public void testReplaceVariablesWithList() throws Exception {
        String jsonString = "[{\"input\":{\"testKey\":\"${WORKFLOW_INPUT_VAL}\"},\"output\":{\"testKey\":\"${TASK_OUTPUT_VAL}\"}}]";
        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", this.workflowModel));

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(jsonString, Objects.toString(replacement));
    }

    @Test
    public void testReplaceVariablesWithNestedList() throws Exception {
        String jsonString = "[{\"input\":{\"testKey\":\"${WORKFLOW_INPUT_VAL}\"},\"output\":[{\"testKey\":\"${TASK_OUTPUT_VAL1}\"},{\"testKey\":\"${TASK_OUTPUT_VAL2}\"}]}]";
        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", this.workflowModel));

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(jsonString, Objects.toString(replacement));
    }

    @Test
    public void testReplaceVariablesWithNestedMap() throws Exception {
        String jsonString =
                "{\"input\":{\"map\":{\"testMapKey1\":{\"testSubMapKey\":\"${WORKFLOW_INPUT_VAL}\"}}},\"output\":{\"testKey\":\"${TASK_OUTPUT_VAL}\"}}";
        DocumentContext documentContext = JsonPath.parse(Map.of("workflow", this.workflowModel));

        Object replacement = this.utils.replaceVariables(jsonString, documentContext, null);
        assertEquals(jsonString, Objects.toString(replacement));
    }
}