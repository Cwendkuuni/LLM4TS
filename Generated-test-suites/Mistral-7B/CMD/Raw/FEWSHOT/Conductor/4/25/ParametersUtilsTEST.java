package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** Used to parse and resolve the JSONPath bindings in the workflow and task definitions. */
public class ParametersUtilsTest {

    private static final String WORKFLOW_ID = "workflowId";
    private static final String TASK_ID = "taskId";
    private static final String TASK_DEF_NAME = "taskDefName";
    private static final String TASK_REF_NAME = "taskRefName";
    private static final String PARAM_KEY = "paramKey";
    private static final String PARAM_VALUE = "paramValue";

    private ParametersUtils utils;
    private ObjectMapper objectMapper;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        utils = new ParametersUtils(objectMapper);
        workflowModel = new WorkflowModel();
        taskDef = new TaskDef();
    }

    @Test
    public void testGetTaskInput() {
        // Given a workflow model with input and tasks
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(PARAM_KEY, PARAM_VALUE);
        workflowModel.setInput(inputParams);
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName(TASK_REF_NAME);
        taskModel.setInputData(new HashMap<>());
        taskModel.setInputData().put(PARAM_KEY, PARAM_VALUE);
        workflowModel.setTasks(new LinkedList<>(Collections.singletonList(taskModel)));

        // When getting the task input
        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put(PARAM_KEY, PARAM_VALUE);
        Map<String, Object> actualOutput = utils.getTaskInput(inputParams, workflowModel, taskDef, TASK_ID);

        // Then the output should match the expected
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetTaskInputWithNullInput() {
        // Given a workflow model with null input and tasks
        workflowModel.setInput(null);
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName(TASK_REF_NAME);
        taskModel.setInputData(new HashMap<>());
        workflowModel.setTasks(new LinkedList<>(Collections.singletonList(taskModel)));

        // When getting the task input
        Map<String, Object> actualOutput = utils.getTaskInput(null, workflowModel, taskDef, TASK_ID);

        // Then the output should be an empty map
        assertNull(actualOutput);
    }

    @Test
    public void testGetTaskInputWithEmptyTasksList() {
        // Given a workflow model with null input and empty tasks list
        workflowModel.setInput(null);
        workflowModel.setTasks(new LinkedList<>());

        // When getting the task input
        Map<String, Object> actualOutput = utils.getTaskInput(null, workflowModel, taskDef, TASK_ID);

        // Then the output should be an empty map
        assertNull(actualOutput);
    }

    @Test
    public void testGetTaskInputWithoutTask() {
        // Given a workflow model with input and no task with the given task id
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(PARAM_KEY, PARAM_VALUE);
        workflowModel.setInput(inputParams);
        workflowModel.setTasks(new LinkedList<>());

        // When getting the task input
        Map<String, Object> actualOutput = utils.getTaskInput(inputParams, workflowModel, taskDef, TASK_ID);

        // Then the output should be an empty map
        assertNull(actualOutput);
    }

    @Test
    public void testGetTaskInputV2() {
        // Given a workflow model with input and tasks
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(PARAM_KEY, PARAM_VALUE);
        workflowModel.setInput(inputParams);
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName(TASK_REF_NAME);
        taskModel.setInputData(new HashMap<>());
        taskModel.setInputData().put(PARAM_KEY, PARAM_VALUE);
        workflowModel.setTasks(new LinkedList<>(Collections.singletonList(taskModel)));
        when(objectMapper.readValue(ArgumentMatchers.anyBytes(), ArgumentMatchers.eq(map.getType())))
                .thenReturn(new HashMap<>());

        // When getting the task input using getTaskInputV2
        Map<String, Object> expectedOutput = new HashMap<>();
        expectedOutput.put(PARAM_KEY, PARAM_VALUE);
        Map<String, Object> actualOutput = utils.getTaskInputV2(inputParams, workflowModel, TASK_ID, taskDef);

        // Then the output should match the expected
        assertEquals(expectedOutput, actualOutput);

        // Verify the objectMapper.readValue call
        byte[] bytes = "{}".getBytes();
        verify(objectMapper).readValue(bytes, map.getType());
    }

    @Test
    public void testGetTaskInputV2WithNullInput() {
        // Given a workflow model with null input and tasks
        workflowModel.setInput(null);
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName(TASK_REF_NAME);
        taskModel.setInputData(new HashMap<>());
        workflowModel.setTasks(new LinkedList<>(Collections.singletonList(taskModel)));

        // When getting the task input using getTaskInputV2
        Map<String, Object> actualOutput = utils.getTaskInputV2(null, workflowModel, TASK_ID, taskDef);

        // Then the output should be an empty map
        assertNull(actualOutput);
    }

    @Test
    public void testGetTaskInputV2WithEmptyTasksList() {
        // Given a workflow model with null input and empty tasks list
        workflowModel.setInput(null);
        workflowModel.setTasks(new LinkedList<>());

        // When getting the task input using getTaskInputV2
        Map<String, Object> actualOutput = utils.getTaskInputV2(null, workflowModel, TASK_ID, taskDef);

        // Then the output should be an empty map
        assertNull(actualOutput);
    }

    @Test
    public void testGetTaskInputV2WithoutTask() {
        // Given a workflow model with input and no task with the given task id
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put(PARAM_KEY, PARAM_VALUE);
        workflowModel.setInput(inputParams);
        workflowModel.setTasks(new LinkedList<>());

        // When getting the task input using getTaskInputV2
        Map<String, Object> actualOutput = utils.getTaskInputV2(inputParams, workflowModel, TASK_ID, taskDef);

        // Then the output should be an empty map
        assertNull(actualOutput);
    }

    @Test
    public void testReplace() {
        // Given a string with a variable to be replaced
        String inputString = "${PARAM_KEY}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        // When replacing the variable
        Object actualOutput = utils.replace(inputString, documentContext, TASK_ID);

        // Then the output should be the original string if no variable is found or the variable value if it is found
        assertEquals(inputString, actualOutput);

        // Given a string with a variable to be replaced and the variable value
        when(documentContext.read(PARAM_KEY)).thenReturn(PARAM_VALUE);

        // When replacing the variable
        actualOutput = utils.replace(inputString, documentContext, TASK_ID);

        // Then the output should be the variable value
        assertEquals(PARAM_VALUE, actualOutput);
    }

    @Test
    public void testReplaceWithNullDocumentContext() {
        // Given a string with a variable to be replaced
        String inputString = "${PARAM_KEY}";

        // When replacing the variable with a null document context
        Object actualOutput = utils.replace(inputString, null, TASK_ID);

        // Then the output should be the original string
        assertEquals(inputString, actualOutput);
    }

    @Test
    public void testReplaceList() {
        // Given a list with some strings to be replaced
        List<String> inputList = new LinkedList<>(Collections.singletonList("${PARAM_KEY}"));
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        // When replacing the variables in the list
        List<Object> actualOutput = utils.replaceList(inputList, TASK_ID, documentContext);

        // Then the output should be the original list if no variable is found or the replaced list if variables are found
        assertEquals(inputList, actualOutput);

        // Given a list with some strings to be replaced and the variable values
        when(documentContext.read(PARAM_KEY)).thenReturn(PARAM_VALUE);

        // When replacing the variables in the list
        actualOutput = utils.replaceList(inputList, TASK_ID, documentContext);

        // Then the output should be a list with the variable values instead of the original strings
        List<String> expectedOutput = new LinkedList<>(Collections.singletonList(PARAM_VALUE));
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testReplaceListWithNullDocumentContext() {
        // Given a list with some strings to be replaced
        List<String> inputList = new LinkedList<>(Collections.singletonList("${PARAM_KEY}"));

        // When replacing the variables in the list with a null document context
        List<Object> actualOutput = utils.replaceList(inputList, TASK_ID, null);

        // Then the output should be the original list
        assertEquals(inputList, actualOutput);
    }
}