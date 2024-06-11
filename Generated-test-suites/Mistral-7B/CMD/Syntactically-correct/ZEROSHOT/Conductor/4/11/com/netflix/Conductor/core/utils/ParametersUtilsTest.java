package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

/**
 * Unit tests for the {@link ParametersUtils} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);

    @InjectMocks
    private ParametersUtils parametersUtils;

    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = Mockito.mock(ObjectMapper.class);
        workflowDef = Mockito.mock(WorkflowDef.class);
        taskDef = Mockito.mock(TaskDef.class);
        workflowModel = new WorkflowModel();
    }

    @Test
    public void testGetTaskInputV1() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("task1.input.name1", "value1");
        inputParams.put("task1.output.name2", "value2");
        workflowModel.setInput(Map.of("name3", "value3"));
        TaskModel taskModel = new TaskModel();
        taskModel.setInputData(Map.of("name4", "value4"));
        taskModel.setOutputData(Map.of("name5", "value5"));
        workflowModel.setTasks(List.of(taskModel));

        // When
        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        // Then
        assertEquals(Map.of("task1.input.name1", "value1", "task1.output.name2", "value2", "name3", "value3"), result);
    }

    @Test
    public void testGetTaskInputV2() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("task1.input.name1", "value1");
        inputParams.put("task1.output.name2", "value2");
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Map.of("name3", "value3"));
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Map.of("task1.input.name1", "inputParam1"));
        workflowModel.setTasks(List.of(taskDef));

        // When
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "task1", taskDef);

        // Then
        assertEquals(Map.of("inputParam1", "value1", "name3", "value3"), result);
    }

    @Test
    public void testReplace() {
        // Given
        String input = "${workflow.input.name1}.${task1.output.name2}";
        Map<String, Object> workflowInput = Map.of("name1", "workflowValue1");
        Map<String, Object> taskOutput = Map.of("name2", "taskOutputValue2");

        // When
        Object result = parametersUtils.replace(input, workflowInput, taskOutput);

        // Then
        assertEquals("workflowValue1.taskOutputValue2", result);
    }

    @Test
    public void testReplaceList() {
        // Given
        List<Object> inputList = List.of("${workflow.input.name1}", "${task1.output.name2}");
        Map<String, Object> workflowInput = Map.of("name1", "workflowValue1");
        Map<String, Object> taskOutput = Map.of("name2", "taskOutputValue2");

        // When
        List<Object> result = parametersUtils.replaceList(inputList, null, null);

        // Then
        assertEquals(
                Arrays.asList("workflowValue1", "taskOutputValue2"), result);
    }

    @Test
    public void testReplaceVariables() {
        // Given
        String input = "${workflow.input.name1}.${task1.output.name2}";
        Map<String, Object> workflowInput = Map.of("name1", "workflowValue1");
        Map<String, Object> taskOutput = Map.of("name2", "taskOutputValue2");

        // When
        Object result =
                parametersUtils.replaceVariables(input, null, null, 0);

        // Then
        assertEquals("workflowValue1.taskOutputValue2", result);
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        // Given
        String input = " ${workflow.input.name1} ";
        Map<String, Object> workflowInput = Map.of("name1", "");

        // When
        Object result =
                parametersUtils.replaceVariables(input, null, null, 0);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariablesWithEnvironmentVariable() {
        // Given
        String input = "${ENV_VAR}";
        String envVariableValue = "envVariableValue";

        // When
        Object result =
                parametersUtils.replaceVariables(input, null, null, 0);

        // Then
        assertEquals(envVariableValue, result);
    }

    @Test
    public void testReplaceVariablesWithDocumentContext() {
        // Given
        String input = "${workflow.input.name1}";
        Map<String, Object> workflowInput = Map.of("name1", "workflowValue1");
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        Object result =
                parametersUtils.replaceVariables(input, documentContext, null);

        // Then
        Mockito.verify(documentContext).read("workflow.input.name1");
        assertEquals("workflowValue1", result);
    }

    @Test(expected = RuntimeException.class)
    public void testReplaceVariablesWithIOException() {
        // Given
        String input = "${workflow.input.name1}";
        Map<String, Object> workflowInput = Map.of("name1", "workflowValue1");
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        Mockito.doThrow(new IOException("IOException"))
                .when(documentContext)
                .read("workflow.input.name1");

        // When & Then
        parametersUtils.replaceVariables(input, documentContext, null);
    }

    @Test
    public void testGetWorkflowInput() {
        // Given
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("name1", "value1");
        workflowDef.setInputTemplate(inputParams);

        // When
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, null);

        // Then
        assertEquals(Map.of("name1", "value1"), result);
    }
}