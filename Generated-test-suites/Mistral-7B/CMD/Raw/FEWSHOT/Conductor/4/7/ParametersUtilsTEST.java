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
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.model.TaskModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowDefinition;

/** Test class for ParametersUtils. */
public class ParametersUtilsTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);
    private static final ObjectMapper OBJECT_MAPPER = Mockito.mock(ObjectMapper.class);
    private static final Configuration JSON_PATH_CONFIGURATION =
            Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);

    private ParametersUtils parametersUtils;
    private TaskDef taskDef;
    private TaskModel taskModel;
    private WorkflowModel workflowModel;
    private WorkflowModel workflowModelWithTasks;

    @Before
    public void setup() {
        parametersUtils = new ParametersUtils(OBJECT_MAPPER);
        taskDef = Mockito.mock(TaskDef.class);
        taskModel = new TaskModel();
        workflowModel = new WorkflowModel();
        workflowModelWithTasks = new WorkflowModel();
        when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("foo", "bar"));
        workflowModel.setDefinition(new WorkflowDefinition());
        workflowModel.getDefinition().setSchemaVersion(1);
        workflowModelWithTasks.setDefinition(new WorkflowDefinition());
        workflowModelWithTasks.getDefinition().setSchemaVersion(2);
        when(taskModel.getReferenceTaskName()).thenReturn("task1");
        when(taskModel.getTaskDefName()).thenReturn("taskDef1");
        when(taskModel.getInputData()).thenReturn(new HashMap<>());
        when(taskModel.getOutputData()).thenReturn(new HashMap<>());
        when(taskModel.getScheduledTime()).thenReturn(new LocalDateTime());
        when(taskModel.getStartTime()).thenReturn(new LocalDateTime());
        when(taskModel.getEndTime()).thenReturn(new LocalDateTime());
        when(taskModel.getIteration()).thenReturn(1);
        when(taskModel.getCorrelationId()).thenReturn("correlationId");
        when(taskModel.getReasonForIncompletion()).thenReturn("reasonForIncompletion");
        when(taskModel.getCallbackAfterSeconds()).thenReturn(10);
        when(taskModel.getWorkerId()).thenReturn("workerId");
        when(taskDef.getName()).thenReturn("taskDefName");
        when(taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("foo", "bar"));

        WorkflowModel workflowModel1 = new WorkflowModel();
        WorkflowModel workflowModel2 = new WorkflowModel();
        List<TaskModel> tasks = new LinkedList<>();
        tasks.add(taskModel);
        tasks.add(taskModel);
        workflowModel1.setTasks(tasks);
        workflowModel1.setDefinition(new WorkflowDefinition());
        workflowModel1.getDefinition().setSchemaVersion(1);
        workflowModel2.setTasks(tasks);
        workflowModel2.setDefinition(new WorkflowDefinition());
        workflowModel2.getDefinition().setSchemaVersion(2);
        workflowModelWithTasks.setTasks(tasks);
    }

    /** Test getTaskInput method with Workflow schema version 1. */
    @Test
    public void testGetTaskInputV1() {
        // Given
        when(workflowModel.getInput()).thenReturn(new HashMap<>());
        Map<String, Object> expectedInput = new HashMap<>();
        expectedInput.put("workflowInputKey", "workflowInputValue");
        when(workflowModel.getInput()).thenReturn(expectedInput);

        // When
        Map<String, Object> actualInput = parametersUtils.getTaskInput(null, workflowModel, taskDef, null);

        // Then
        assertEquals(expectedInput, actualInput);
    }

    /** Test getTaskInput method with Workflow schema version 2. */
    @Test
    public void testGetTaskInputV2() {
        // Given
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParamsKey1", "inputParamsValue1");
        Map<String, Object> expectedInput = new HashMap<>();
        expectedInput.put("workflowInputKey", "workflowInputValue");
        expectedInput.put("taskInputKey1", "taskInputValue1");
        expectedInput.put("taskInputKey2", "taskInputValue2");

        // When
        when(workflowModel.getInput()).thenReturn(new HashMap<>());
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel));
        when(taskModel.getInputData()).thenReturn(new HashMap<>());

        Map<String, Object> actualInput =
                parametersUtils.getTaskInput(inputParams, workflowModelWithTasks, taskDef, "task1");

        // Then
        assertEquals(expectedInput, actualInput);
    }

    /** Test replace method with String value. */
    @Test
    public void testReplaceString() {
        // Given
        String input = "${foo}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        String actualResult = parametersUtils.replaceVariables(input, documentContext, null);

        // Then
        assertEquals("", actualResult);
    }

    /** Test replace method with null value. */
    @Test
    public void testReplaceNull() {
        // Given
        String input = "${foo}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // When
        String actualResult = parametersUtils.replaceVariables(input, documentContext, null);

        // Then
        assertEquals("", actualResult);
    }

    /** Test replace method with Map value. */
    @Test
    public void testReplaceMap() {
        // Given
        String input = "${workflow.input.foo}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("foo", "workflowInputValue");

        // When
        when(documentContext.read(ArgumentMatchers.eq("workflow.input")))
                .thenReturn(workflowInput);
        String actualResult = parametersUtils.replaceVariables(input, documentContext, null);

        // Then
        assertEquals("workflowInputValue", actualResult);
    }

    /** Test replace method with List value. */
    @Test
    public void testReplaceList() {
        // Given
        String input = "${tasks[0].output.bar}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        List<Object> tasksOutput = new LinkedList<>();
        tasksOutput.add(new HashMap<>());
        ((Map<String, Object>) tasksOutput.get(0)).put("bar", "taskOutputValue");

        // When
        when(documentContext.read(ArgumentMatchers.eq("tasks[0].output")))
                .thenReturn(tasksOutput);
        String actualResult = parametersUtils.replaceVariables(input, documentContext, null);

        // Then
        assertEquals("taskOutputValue", actualResult);
    }

    /** Test replace method with environment variable value. */
    @Test
    public void testReplaceEnvVariable() {
        // Given
        String input = "${ENV_VAR}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);
        String envVariableValue = "environmentVariableValue";

        // When
        when(EnvUtils.getSystemParametersValue(ArgumentMatchers.eq("ENV_VAR")))
                .thenReturn(envVariableValue);
        String actualResult = parametersUtils.replaceVariables(input, documentContext, null);

        // Then
        assertEquals(envVariableValue, actualResult);
    }
}