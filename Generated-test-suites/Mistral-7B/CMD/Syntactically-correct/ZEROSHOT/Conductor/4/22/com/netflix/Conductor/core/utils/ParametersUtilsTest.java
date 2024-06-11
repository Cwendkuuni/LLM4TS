package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;

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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

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
 * Unit tests for the ParametersUtils class.
 */
@Component
public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = Mockito.mock(ObjectMapper.class);
        workflowDef = Mockito.mock(WorkflowDef.class);
        taskDef = Mockito.mock(TaskDef.class);
        workflowModel = new WorkflowModel();
        parametersUtils = new ParametersUtils(objectMapper);
        Mockito.when(workflowDef.getInputTemplate()).thenReturn(new HashMap<>());
        Mockito.when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());
        Mockito.when(taskDef.getInputData()).thenReturn(new HashMap<>());
        Mockito.when(taskDef.getOutputData()).thenReturn(new HashMap<>());
        Mockito.when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel()));
        Mockito.when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        Mockito.when(workflowModel.getWorkflowId()).thenReturn("workflowId");
        Mockito.when(taskModel().getReferenceTaskName()).thenReturn("taskRefName");
        Mockito.when(taskModel().getTaskDefName()).thenReturn("taskDefName");
        Mockito.when(taskModel().getTaskType()).thenReturn("taskType");
        Mockito.when(taskModel().getInputData()).thenReturn(inputData());
        Mockito.when(taskModel().getOutputData()).thenReturn(outputData());
    }

    private Map<String, Object> inputData() {
        Map<String, Object> inputData = new HashMap<>();
        inputData.put("inputParam1", "inputValue1");
        inputData.put("inputParam2", "inputValue2");
        return inputData;
    }

    private Map<String, Object> outputData() {
        Map<String, Object> outputData = new HashMap<>();
        outputData.put("outputParam1", "outputValue1");
        outputData.put("outputParam2", "outputValue2");
        return outputData;
    }

    private TaskModel taskModel() {
        TaskModel taskModel = new TaskModel();
        taskModel.setInputData(inputData());
        taskModel.setOutputData(outputData());
        return taskModel;
    }

    @Test
    public void testGetTaskInput_ValidInput_ReturnsExpectedMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam3", "inputValue3");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("inputParam1", "inputValue1");
        expectedResult.put("inputParam2", "inputValue2");
        expectedResult.put("inputParam3", "inputValue3");
        expectedResult.put("workflowId", "workflowId");
        expectedResult.put("taskDefName", "taskDefName");
        expectedResult.put("taskType", "taskType");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInput_NullInputParams_ReturnsEmptyMap() {
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflowModel, taskDef, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInput_NullTaskDefinition_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam3", "inputValue3");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInput_NullWorkflowModel_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam3", "inputValue3");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, null, taskDef, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTaskInputV2_ValidInput_ReturnsExpectedMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam3", "inputValue3");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("inputParam1", "inputValue1");
        expectedResult.put("inputParam2", "inputValue2");
        expectedResult.put("inputParam3", "inputValue3");
        expectedResult.put("workflowId", "workflowId");
        expectedResult.put("taskDefName", "taskDefName");
        expectedResult.put("taskType", "taskType");

        Configuration config =
                Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), config);

        Map<String, Object> resolvedInput =
                parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskRefName", taskDef);

        assertEquals(expectedResult, resolvedInput);
    }

    @Test
    public void testReplaceWithEmptyJsonPath_ReturnsInputStringUnchanged() {
        String inputString = "${emptyJsonPath}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());

        String result = parametersUtils.replaceVariables(inputString, documentContext, null);

        assertEquals(inputString, result);
    }

    @Test
    public void testReplaceWithValidJsonPath_ReturnsReplacedString() {
        String inputString = "${workflow.input.inputParam1}";
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("inputParam1", "replacedValue");

        Configuration config =
                Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(workflowInput, config);

        String result = parametersUtils.replaceVariables(inputString, documentContext, null);

        assertEquals("replacedValue", result);
    }

    @Test
    public void testReplaceWithNullJsonPath_ReturnsInputStringUnchanged() {
        String inputString = "${nullJsonPath}";
        Configuration config =
                Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(null, config);

        String result = parametersUtils.replaceVariables(inputString, documentContext, null);

        assertEquals(inputString, result);
    }

    @Test
    public void testReplaceWithEnvironmentVariable_ReturnsReplacedString() {
        String inputString = "${ENV_VARIABLE}";
        String envVariableValue = "envValue";
        System.setProperty("ENV_VARIABLE", envVariableValue);

        String result = parametersUtils.replaceVariables(inputString, null, null);

        assertEquals(envVariableValue, result);
    }

    @Test
    public void testReplaceWithNestedJsonPath_ReturnsReplacedString() {
        String inputString = "${workflow.input.taskDefs[0].inputData.inputParam1}";
        Map<String, Object> workflowInput = new HashMap<>();
        Map<String, Object> taskDefInput = new HashMap<>();
        taskDefInput.put("inputParam1", "replacedValue");
        workflowInput.put("inputData", taskDefInput);
        workflowInput.put("taskDefs", Collections.singletonList(new HashMap<>()));

        Configuration config =
                Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(workflowInput, config);

        String result = parametersUtils.replaceVariables(inputString, documentContext, null);

        assertEquals("replacedValue", result);
    }
}