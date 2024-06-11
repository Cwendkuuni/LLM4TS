package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
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
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.workflow.WorkflowTaskInput;

public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);

    private ObjectMapper objectMapper;
    private ObjectMapper jsonPathObjectMapper;
    private ParametersUtils parametersUtils;
    private TaskDef taskDefinition;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;
    private EnvUtils envUtils;
    private TaskUtils taskUtils;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonPathObjectMapper = new ObjectMapper();
        Configuration configuration = JsonPath.configuration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        JsonPath.using(configuration).parse("{}");
        jsonPathObjectMapper.setConfiguration(configuration);
        parametersUtils = new ParametersUtils(objectMapper);
        taskDefinition = mock(TaskDef.class);
        workflowDef = mock(WorkflowDef.class);
        workflowModel = mock(WorkflowModel.class);
        envUtils = mock(EnvUtils.class);
        taskUtils = mock(TaskUtils.class);
    }

    @Test
    public void testGetTaskInputV1() {
        when(workflowModel.getInput()).thenReturn(new HashMap<>());
        when(workflowModel.getTasks()).thenReturn(Collections.emptyList());

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, null);
        assertNull(result);

        Map<String, Object> inputParams = new LinkedHashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("workflow.output.param2", "value2");
        when(workflowModel.getInput()).thenReturn(inputParams);
        when(workflowModel.getTasks()).thenReturn(List.of(mockTask()));

        Map<String, Object> expectedOutput = new LinkedHashMap<>();
        expectedOutput.put("workflow.input.param1", "value1");
        expectedOutput.put("task1.input.param3", "value3"); // task1 exists in workflow
        expectedOutput.put("task2.output.param4", "value4"); // task2 exists in workflow

        Map<String, Object> resultOutput = parametersUtils.getTaskInputV1(workflowModel, inputParams);
        assertEquals(expectedOutput, resultOutput);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("workflow.output.param2", "value2");

        Map<String, Object> workflowParams = new LinkedHashMap<>();
        workflowParams.put("input", new LinkedHashMap<>());
        workflowParams.put("output", new LinkedHashMap<>());
        workflowParams.put("status", "Running");
        workflowParams.put("workflowId", "workflow1");
        workflowParams.put("parentWorkflowId", "parentWorkflow1");
        workflowParams.put("parentWorkflowTaskId", "parentTask1");
        workflowParams.put("workflowType", "workflowType1");
        workflowParams.put("version", 1);
        workflowParams.put("variables", new LinkedHashMap<>());

        Map<String, Map<String, Object>> inputMap = new LinkedHashMap<>();
        inputMap.put("workflow", workflowParams);

        Map<String, TaskModel> tasks = new LinkedHashMap<>();
        TaskModel task1 = new TaskModel();
        task1.setReferenceTaskName("task1");
        task1.setInputData(new LinkedHashMap<>());
        task1.setOutputData(new LinkedHashMap<>());
        tasks.put("task1", task1);
        tasks.put("task2", new TaskModel());

        when(workflowModel.getInput()).thenReturn(inputParams);
        when(workflowModel.getTasks()).thenReturn(tasks.values());

        Map<String, Object> expectedOutput = new LinkedHashMap<>();
        expectedOutput.put("workflow.input.param1", "value1");
        expectedOutput.put("task1.input.param3", "value3"); // task1 exists in workflow
        expectedOutput.put("task1.output.param4", "value4"); // task1 exists in workflow
        expectedOutput.put("task2.output.param5", "value5"); // task2 does not exist in workflow

        Map<String, Object> resultOutput = parametersUtils.getTaskInputV2(null, workflowModel, null, null);
        assertEquals(expectedOutput, resultOutput);
    }

    @Test
    public void testGetTaskInputNullWorkflow() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("workflow.output.param2", "value2");

        when(workflowModel.getInput()).thenReturn(inputParams);

        assertThrows(NullPointerException.class, () -> parametersUtils.getTaskInputV2(null, workflowModel, null, null));
    }

    @Test
    public void testGetTaskInputNullTaskDefinition() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("workflow.output.param2", "value2");

        Map<String, Object> workflowParams = new LinkedHashMap<>();
        workflowParams.put("input", new LinkedHashMap<>());
        workflowParams.put("output", new LinkedHashMap<>());
        workflowParams.put("status", "Running");
        workflowParams.put("workflowId", "workflow1");
        workflowParams.put("parentWorkflowId", "parentWorkflow1");
        workflowParams.put("parentWorkflowTaskId", "parentTask1");
        workflowParams.put("workflowType", "workflowType1");
        workflowParams.put("version", 1);
        workflowParams.put("variables", new LinkedHashMap<>());

        Map<String, Map<String, Object>> inputMap = new LinkedHashMap<>();
        inputMap.put("workflow", workflowParams);

        when(workflowModel.getInput()).thenReturn(inputParams);
        when(workflowModel.getTasks()).thenReturn(Collections.emptyList());

        assertThrows(
                NullPointerException.class,
                () -> parametersUtils.getTaskInputV2(inputMap, workflowModel, null, taskDefinition));
    }

    @Test
    public void testGetTaskInputNullTaskId() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("workflow.output.param2", "value2");

        Map<String, Object> workflowParams = new LinkedHashMap<>();
        workflowParams.put("input", new LinkedHashMap<>());
        workflowParams.put("output", new LinkedHashMap<>());
        workflowParams.put("status", "Running");
        workflowParams.put("workflowId", "workflow1");
        workflowParams.put("parentWorkflowId", "parentWorkflow1");
        workflowParams.put("parentWorkflowTaskId", "parentTask1");
        workflowParams.put("workflowType", "workflowType1");
        workflowParams.put("version", 1);
        workflowParams.put("variables", new LinkedHashMap<>());

        Map<String, Map<String, Object>> inputMap = new LinkedHashMap<>();
        inputMap.put("workflow", workflowParams);

        Map<String, TaskModel> tasks = new LinkedHashMap<>();
        TaskModel task1 = new TaskModel();
        task1.setReferenceTaskName("task1");
        task1.setInputData(new LinkedHashMap<>());
        task1.setOutputData(new LinkedHashMap<>());
        tasks.put("task1", task1);

        when(workflowModel.getInput()).thenReturn(inputParams);
        when(workflowModel.getTasks()).thenReturn(tasks.values());

        Map<String, Object> resultOutput = parametersUtils.getTaskInputV2(inputMap, workflowModel, null, null);
        assertNull(resultOutput);
    }

    @Test
    public void testReplace() {
        String input = "${workflow.input.param1}.${workflow.output.param2}.${{workflow.input.param2}}";
        Map<String, Object> workflowParams = new LinkedHashMap<>();
        workflowParams.put("input", new LinkedHashMap<>());
        workflowParams.put("output", new LinkedHashMap<>());
        workflowParams.put("input.param1", "value1");
        workflowParams.put("input.param2", "value2");
        workflowParams.put("output.param2", "value3");

        DocumentContext documentContext = JsonPath.parse(workflowParams);

        String result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals("value1.value3.value2", result);
    }

    @Test
    public void testReplaceEnvironmentVariables() {
        String input = "${ENV_VAR}";
        Map<String, Object> workflowParams = new LinkedHashMap<>();
        workflowParams.put("ENV_VAR", "envValue");

        DocumentContext documentContext = JsonPath.parse(workflowParams);

        String result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals("envValue", result);
    }

    @Test
    public void testReplaceNullDocumentContext() {
        String input = "${workflow.input.param1}.${workflow.output.param2}.${{workflow.input.param2}}";
        workflowParams.put("input", new LinkedHashMap<>());
        workflowParams.put("output", new LinkedHashMap<>());

        String result = parametersUtils.replaceVariables(input, null, null);
        assertEquals(input, result);
    }

    @Test
    public void testReplaceEmptyDocumentContext() {
        String input = "${workflow.input.param1}.${workflow.output.param2}.${{workflow.input.param2}}";
        Map<String, Object> workflowParams = new LinkedHashMap<>();

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        String result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals(input, result);
    }

    @Test
    public void testReplaceEmptyString() {
        String input = "${}";

        Map<String, Object> workflowParams = new LinkedHashMap<>();

        DocumentContext documentContext = JsonPath.parse(workflowParams);

        String result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariablesWithDepth() {
        String input = "${workflow.input.param1.deep.param2}.${{workflow.input.param1.deep.param1}}";
        Map<String, Object> workflowParams = new LinkedHashMap<>();
        workflowParams.put("input", new LinkedHashMap<>());
        workflowParams.put(
                "input.param1.deep",
                new LinkedHashMap<>(Map.of("param1", "paramValue1", "deep.param1", "paramValue2")));

        DocumentContext documentContext = JsonPath.parse(workflowParams);

        String result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals("paramValue1.paramValue2", result);
    }

    @Test
    public void testReplaceVariablesWithDepthAndEnvironmentVariables() {
        String input = "${workflow.input.param1.deep.param2}.${ENV_VAR}.${{workflow.input.param1.deep.param1}}";
        Map<String, Object> workflowParams = new LinkedHashMap<>();
        workflowParams.put("input", new LinkedHashMap<>());
        workflowParams.put(
                "input.param1.deep",
                new LinkedHashMap<>(Map.of("param1", "paramValue1", "deep.param1", "paramValue2")));
        workflowParams.put("ENV_VAR", "envValue");

        DocumentContext documentContext = JsonPath.parse(workflowParams);

        String result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals("paramValue1.envValue.paramValue2", result);
    }

    private TaskModel mockTask() {
        TaskModel task = new TaskModel();
        task.setReferenceTaskName("task1");
        task.setInputData(new LinkedHashMap<>());
        task.setOutputData(new LinkedHashMap<>());
        return task;
    }
}