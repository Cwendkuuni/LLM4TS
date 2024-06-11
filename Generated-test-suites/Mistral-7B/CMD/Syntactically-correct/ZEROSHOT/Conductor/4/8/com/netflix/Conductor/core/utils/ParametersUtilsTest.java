package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

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

/**
 * Test class for {@link ParametersUtils}.
 */
@RunWith(SpringRunner.class)
public class ParametersUtilsTest {

    private static final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);

    /** Test instance of {@link ParametersUtils}. */
    private final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    /** Test workflow definition. */
    private static final WorkflowDef TEST_WORKFLOW_DEF = new WorkflowDef();

    /** Test workflow model with tasks. */
    private static final WorkflowModel TEST_WORKFLOW_MODEL = new WorkflowModel();

    static {
        TEST_WORKFLOW_MODEL.setWorkflowDefinition(TEST_WORKFLOW_DEF);
        TEST_WORKFLOW_MODEL.setId("testWorkflowId");
        TEST_WORKFLOW_MODEL.setName("testWorkflowName");
        TEST_WORKFLOW_MODEL.setVersion("testWorkflowVersion");
        TEST_WORKFLOW_MODEL.setInput(new HashMap<>());
        TEST_WORKFLOW_MODEL.setTasks(new LinkedList<>());

        TaskDef taskDef1 = new TaskDef();
        taskDef1.setName("taskDefName1");
        taskDef1.setInputTemplate(new HashMap<>());
        taskDef1.setTaskType("taskType1");

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskDefName(taskDef1.getName());
        taskModel1.setReferenceTaskName("taskRefName1");
        taskModel1.setInputData(new HashMap<>());
        taskModel1.setOutputData(new HashMap<>());

        TEST_WORKFLOW_MODEL.getTasks().add(taskModel1);

        TaskDef taskDef2 = new TaskDef();
        taskDef2.setName("taskDefName2");
        taskDef2.setInputTemplate(new HashMap<>());
        taskDef2.setTaskType("taskType2");

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setTaskDefName(taskDef2.getName());
        taskModel2.setReferenceTaskName("taskRefName2");
        taskModel2.setInputData(new HashMap<>());
        taskModel2.setOutputData(new HashMap<>());

        TEST_WORKFLOW_MODEL.getTasks().add(taskModel2);

        TEST_WORKFLOW_DEF.setId("testWorkflowDefId");
        TEST_WORKFLOW_DEF.setName("testWorkflowDefName");
        TEST_WORKFLOW_DEF.setVersion("testWorkflowDefVersion");
        TEST_WORKFLOW_DEF.setInputTemplate(new HashMap<>());

        Configuration config = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(TEST_WORKFLOW_MODEL, config);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow", expectedWorkflowParams());
        expectedOutput.put("task1", expectedTask1Params());
        expectedOutput.put("task2", expectedTask2Params());

        Map<String, Object> actualOutput = parametersUtils.getTaskInput(inputParams, TEST_WORKFLOW_MODEL, null, null);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetTaskInputWithNullInputParams() {
        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow", expectedWorkflowParams());
        expectedOutput.put("task1", new HashMap<>());
        expectedOutput.put("task2", new HashMap<>());

        Map<String, Object> actualOutput = parametersUtils.getTaskInput(null, TEST_WORKFLOW_MODEL, null, null);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetTaskInputWithNullTaskDefinition() {
        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow", expectedWorkflowParams());
        expectedOutput.put("task1", expectedTask1Params());
        expectedOutput.put("task2", expectedTask2Params());

        Map<String, Object> actualOutput =
                parametersUtils.getTaskInput(new HashMap<>(), TEST_WORKFLOW_MODEL, null, null);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow", expectedWorkflowParams());
        expectedOutput.put("task1", expectedTask1Params());
        expectedOutput.put("task2", expectedTask2Params());

        Map<String, Object> actualOutput =
                parametersUtils.getTaskInputV2(inputParams, TEST_WORKFLOW_MODEL, "taskId1", null);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetTaskInputV2WithNullTaskId() {
        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put("workflow", expectedWorkflowParams());
        expectedOutput.put("task1", expectedTask1Params());
        expectedOutput.put("task2", expectedTask2Params());

        Map<String, Object> actualOutput =
                parametersUtils.getTaskInputV2(inputParams, TEST_WORKFLOW_MODEL, null, null);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testReplace() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}";
        DocumentContext documentContext = JsonPath.parse(TEST_WORKFLOW_MODEL, Configuration.defaultConfiguration());

        Object replacement = parametersUtils.replace(inputString, documentContext, null);
        assertEquals("testWorkflowInputStringTestTask1OutputIntegerParam1testTask2OutputStringParam2", replacement);
    }

    @Test
    public void testReplaceWithNullDocumentContext() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}";

        Object replacement = parametersUtils.replace(inputString, null, null);
        assertEquals(inputString, replacement);
    }

    @Test
    public void testReplaceWithEmptyString() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}";

        Object replacement = parametersUtils.replace("", documentContext, null);
        assertEquals("", replacement);
    }

    @Test
    public void testReplaceWithTaskId() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}${task1.output.stringParam3:taskId1}";
        DocumentContext documentContext = JsonPath.parse(TEST_WORKFLOW_MODEL, Configuration.defaultConfiguration());

        Object replacement = parametersUtils.replace(inputString, documentContext, "taskId1");
        assertEquals(
                "testWorkflowInputStringtestTask1OutputIntegerParam1testTask2OutputStringParam2task1OutputStringParam3:taskId1",
                replacement);
    }

    @Test
    public void testReplaceVariables() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}${workflow.input:stringParam4}";
        DocumentContext documentContext = JsonPath.parse(TEST_WORKFLOW_MODEL, Configuration.defaultConfiguration());

        Object replacement = parametersUtils.replaceVariables(inputString, documentContext, null);
        assertEquals(
                "testWorkflowInputStringtestTask1OutputIntegerParam1testTask2OutputStringParam2testWorkflowInputstringParam4",
                replacement);
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}${workflow.input:stringParam4}";

        Object replacement = parametersUtils.replaceVariables(inputString, null, null);
        assertEquals(inputString, replacement);
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}${workflow.input:stringParam4}";

        Object replacement = parametersUtils.replaceVariables("", documentContext, null);
        assertEquals("", replacement);
    }

    @Test
    public void testReplaceVariablesWithTaskId() {
        String inputString =
                "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}${workflow.input:stringParam4:taskId1}";
        DocumentContext documentContext = JsonPath.parse(TEST_WORKFLOW_MODEL, Configuration.defaultConfiguration());

        Object replacement = parametersUtils.replaceVariables(inputString, documentContext, "taskId1");
        assertEquals(
                "testWorkflowInputStringtestTask1OutputIntegerParam1testTask2OutputStringParam2testWorkflowInputstringParam4:taskId1",
                replacement);
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}${ENV_VAR_TEST}";
        DocumentContext documentContext = JsonPath.parse(TEST_WORKFLOW_MODEL, Configuration.defaultConfiguration());
        String envVarTest = "testEnvVarValue";

        Mockito.when(objectMapper.readValue(String.class, Mockito.anyString())).thenReturn(envVarTest);

        Object replacement = parametersUtils.replaceVariables(inputString, documentContext, null);
        assertEquals(
                "testWorkflowInputStringtestTask1OutputIntegerParam1testTask2OutputStringParam2testEnvVarValue", replacement);
    }

    @Test
    public void testReplaceVariablesWithNullEnvironmentVariable() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}${ENV_VAR_TEST}";
        DocumentContext documentContext = JsonPath.parse(TEST_WORKFLOW_MODEL, Configuration.defaultConfiguration());

        Object replacement = parametersUtils.replaceVariables(inputString, documentContext, null);
        assertEquals(inputString, replacement);
    }

    @Test
    public void testReplaceVariablesWithEmptyEnvironmentVariable() {
        String inputString = "${workflow.input.stringParam1}${task1.output.integerParam1}${task2.output.stringParam2}${ENV_VAR_TEST}";
        DocumentContext documentContext = JsonPath.parse(TEST_WORKFLOW_MODEL, Configuration.defaultConfiguration());

        Object replacement = parametersUtils.replaceVariables(inputString, documentContext, null);
        assertEquals(inputString, replacement);
    }

    private Map<String, Object> expectedTask1Params() {
        Map<String, Object> expectedTask1Params = new HashMap<>();
        expectedTask1Params.put("input", TEST_WORKFLOW_MODEL.getTasks().get(0).getInputData());
        expectedTask1Params.put("output", TEST_WORKFLOW_MODEL.getTasks().get(0).getOutputData());
        expectedTask1Params.put("taskType", "taskType1");
        expectedTask1Params.put("referenceTaskName", "taskRefName1");
        expectedTask1Params.put("retryCount", 0);
        expectedTask1Params.put("correlationId", null);
        expectedTask1Params.put("pollCount", null);
        expectedTask1Params.put("taskDefName", "taskDefName1");
        expectedTask1Params.put("scheduledTime", null);
        expectedTask1Params.put("startTime", null);
        expectedTask1Params.put("endTime", null);
        expectedTask1Params.put("workflowInstanceId", null);
        expectedTask1Params.put("taskId", "taskId1");
        expectedTask1Params.put("reasonForIncompletion", null);
        expectedTask1Params.put("callbackAfterSeconds", null);
        expectedTask1Params.put("workerId", null);
        expectedTask1Params.put("iteration", null);
        return expectedTask1Params;
    }

    private Map<String, Object> expectedTask2Params() {
        Map<String, Object> expectedTask2Params = new HashMap<>();
        expectedTask2Params.put("input", TEST_WORKFLOW_MODEL.getTasks().get(1).getInputData());
        expectedTask2Params.put("output", TEST_WORKFLOW_MODEL.getTasks().get(1).getOutputData());
        expectedTask2Params.put("taskType", "taskType2");
        expectedTask2Params.put("referenceTaskName", "taskRefName2");
        expectedTask2Params.put("retryCount", 0);
        expectedTask2Params.put("correlationId", null);
        expectedTask2Params.put("pollCount", null);
        expectedTask2Params.put("taskDefName", "taskDefName2");
        expectedTask2Params.put("scheduledTime", null);
        expectedTask2Params.put("startTime", null);
        expectedTask2Params.put("endTime", null);
        expectedTask2Params.put("workflowInstanceId", null);
        expectedTask2Params.put("taskId", "taskId2");
        expectedTask2Params.put("reasonForIncompletion", null);
        expectedTask2Params.put("callbackAfterSeconds", null);
        expectedTask2Params.put("workerId", null);
        expectedTask2Params.put("iteration", null);
        return expectedTask2Params;
    }

    private Map<String, Object> expectedWorkflowParams() {
        Map<String, Object> expectedWorkflowParams = new HashMap<>();
        expectedWorkflowParams.put("input", TEST_WORKFLOW_MODEL.getInput());
        expectedWorkflowParams.put("output", TEST_WORKFLOW_MODEL.getOutput());
        expectedWorkflowParams.put("status", null);
        expectedWorkflowParams.put("workflowId", "testWorkflowId");
        expectedWorkflowParams.put("parentWorkflowId", null);
        expectedWorkflowParams.put("parentWorkflowTaskId", null);
        expectedWorkflowParams.put("workflowType", "testWorkflowName");
        expectedWorkflowParams.put("version", "testWorkflowVersion");
        expectedWorkflowParams.put("correlationId", null);
        expectedWorkflowParams.put("reasonForIncompletion", null);
        expectedWorkflowParams.put("schemaVersion", 1);
        expectedWorkflowParams.put("variables", new HashMap<>());
        return expectedWorkflowParams;
    }
}