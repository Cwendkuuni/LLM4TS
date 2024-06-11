package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.Task;

/**
 * Unit tests for {@link ParametersUtils}.
 */
public class ParametersUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final Pattern PATTERN = Pattern.compile("\\${([\\w._%]+)}");

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WorkflowDef workflowDef;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskDef taskDef;

    @Mock
    private Task task;

    private ParametersUtils utils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(task));
        when(task.getReferenceTaskName()).thenReturn("childTask");
        when(task.getTaskDefName()).thenReturn("childTaskDef");
        when(task.getScheduledTime()).thenAnswer(new Answer<DateTime>() {
            @Override
            public DateTime answer(InvocationOnMock invocation) {
                return new DateTime();
            }
        });
        when(task.getStartTime()).thenAnswer(new Answer<DateTime>() {
            @Override
            public DateTime answer(InvocationOnMock invocation) {
                return new DateTime();
            }
        });
        when(task.getEndTime()).thenAnswer(new Answer<DateTime>() {
            @Override
            public DateTime answer(InvocationOnMock invocation) {
                return new DateTime();
            }
        });
        when(task.getWorkerId()).thenReturn("workerId");
        when(task.getIteration()).thenReturn(1);
        when(task.getReasonForIncompletion()).thenReturn("ReasonForIncompletion");
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(1);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(workflowModel.getInput()).thenReturn(Collections.emptyMap());
        when(workflowModel.getVariables()).thenReturn(Collections.emptyMap());

        utils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV1() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.variable}", "value");
        inputParams.put("${workflow.input.task.childTask.output.status}", "status");

        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("variable", "valueFromWorkflow");
        workflowModel.setInput(workflowInput);
        workflowModel.setVariables(Collections.singletonMap("variable", "valueFromWorkflow"));

        Map<String, Object> expected = new HashMap<>();
        expected.put("value", "valueFromWorkflow");
        expected.put("status", "status");

        Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, taskDef, null);

        assertEquals(expected, result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.variable}", "value");
        inputParams.put("${workflow.input.task.childTask.output.status}", "status");

        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("variable", "valueFromWorkflow");
        workflowModel.setInput(workflowInput);
        workflowModel.setVariables(Collections.singletonMap("variable", "valueFromWorkflow"));

        Map<String, Object> expected = new HashMap<>();
        expected.put("value", "valueFromWorkflow");
        expected.put("status", "status");

        when(workflowModel.getTasks())
                .thenReturn(Collections.singletonList(task));
        when(task.getInputData())
                .thenReturn(Collections.singletonMap("status", "statusFromTask"));
        when(task.getOutputData())
                .thenReturn(Collections.singletonMap("status", "statusFromTask"));

        Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, taskDef, null);

        assertEquals(expected, result);

        verify(taskDef, times(0)).getInputTemplate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTaskInputV2WithNullWorkflowModel() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.variable}", "value");
        inputParams.put("${workflow.input.task.childTask.output.status}", "status");

        Map<String, Object> result = utils.getTaskInput(inputParams, null, taskDef, null);

        fail("Should have thrown IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTaskInputV2WithNullTaskDef() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.variable}", "value");
        inputParams.put("${workflow.input.task.childTask.output.status}", "status");

        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("variable", "valueFromWorkflow");
        workflowModel.setInput(workflowInput);
        workflowModel.setVariables(Collections.singletonMap("variable", "valueFromWorkflow"));

        Map<String, Object> result = utils.getTaskInput(inputParams, workflowModel, null, null);

        fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testReplaceVariables() {
        String input = "${workflow.input.variable}${workflow.input.task.childTask.output.status}";

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("variable")).thenReturn("valueFromWorkflow");
        when(documentContext.read("status")).thenReturn("statusFromWorkflow");

        String expected = "valueFromWorkflowstatusFromWorkflow";

        String result = utils.replaceVariables(input, documentContext, null);

        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        String input = "${workflow.input.variable}${workflow.input.task.childTask.output.status}";

        String result = utils.replaceVariables(input, null, null);

        assertEquals(input, result);
    }

    @Test
    public void testReplaceVariablesWithNullTaskId() {
        String input = "${workflow.input.variable}${workflow.input.task.childTask.output.status}";

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("variable")).thenReturn("valueFromWorkflow");
        when(documentContext.read("status")).thenReturn("statusFromWorkflow");

        String result = utils.replaceVariables(input, documentContext, null);

        assertEquals(input, result);
    }

    @Test
    public void testReplaceVariablesWithEmptyStringInput() {
        String input = "";

        DocumentContext documentContext = mock(DocumentContext.class);

        String result = utils.replaceVariables(input, documentContext, null);

        assertEquals(input, result);
    }

    @Test(expected = PathNotFoundException.class)
    public void testReplaceVariablesWithMissingVariable() {
        String input = "${workflow.input.missingVariable}";

        DocumentContext documentContext = mock(DocumentContext.class);

        utils.replaceVariables(input, documentContext, null);

        fail("Should have thrown PathNotFoundException");
    }

    private static class Utils {

        public static void checkArgument(boolean condition, String message) {
            if (!condition) {
                throw new IllegalArgumentException(message);
            }
        }
    }
}