package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.workflow.WorkflowInstanceEvent;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Test class for ParametersUtils. */
public class ParametersUtilsTest {

    private static final String WORKFLOW_DEF_JSON =
            "{"
                    + "\"name\":\"testWorkflow\","
                    + "\"version\":\"1.0.0\","
                    + "\"input\":{\"input1\":\"${{input.input1}}\"}}";

    private static final String TASK_DEF_JSON =
            "{"
                    + "\"name\":\"testTaskDef\","
                    + "\"inputTemplate\":{"
                    + "\"input1\":\"${{workflow.input.input1}}\","
                    + "\"input2\":\"${{workflow.input.input2}}\""
                    + "}},"
                    + "\"output\":{\"output1\":\"${{input.input1}}\"}}";

    private static final String WORKFLOW_INPUT_JSON = "{\"input1\":\"testValue1\"}";

    private static final String TASK_INPUT_JSON = "{\"input1\":\"${{workflow.input.input1}}\",\"input2\":\"testValue2\"}";

    private static final String WORKFLOW_OUTPUT_JSON = "{\"output1\":\"testValue3\"}";

    private static final String TASK_OUTPUT_JSON = "{\"output1\":\"${{task.input.input1}}\"}";

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private DocumentContext documentContext;
    private TaskDef taskDef;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        taskDef = mock(TaskDef.class);
        workflowDef = mock(WorkflowDef.class);
        workflowModel = mock(WorkflowModel.class);
        when(objectMapper.readValue(anyString(), Map.class)).thenAnswer(
                new Answer<Map<String, Object>>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable {
                        String json = invocation.getArgument(0);
                        return new LinkedHashMap<>((Map<String, Object>) JsonPath.parse(json));
                    }
                });
        documentContext = JsonPath.using(Configuration.defaultConfiguration());
        when(workflowModel.getInput()).thenReturn(mapOf("input1", "testValue1"));
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV1() throws IOException {
        when(workflowModel.getTasks()).thenReturn(List.of(
                mock(TaskModel.class).givenInputData(mapOf("input1", "testValue1")),
                taskDef
        ));
        when(taskDef.getInputTemplate()).thenReturn(mapOf("input1", "", "input2", ""));

        Map<String, Object> input = parametersUtils.getTaskInput(
                mapOf("input1", "testValue1"), workflowModel, taskDef, "testTaskId");

        assertEquals(mapOf("input1", "testValue1", "input2", "testValue2"), input);
        verify(taskDef, times(1)).getInputTemplate();
    }

    @Test
    public void testGetTaskInputV1WithNullInputParams() {
        when(workflowModel.getTasks()).thenReturn(List.of(taskDef));

        Map<String, Object> input = parametersUtils.getTaskInput(null, workflowModel, taskDef, "testTaskId");

        assertNull(input);
    }

    @Test
    public void testGetTaskInputV1WithNullWorkflowModel() {
        when(taskDef.getInputTemplate()).thenReturn(mapOf("input1", "", "input2", ""));

        Map<String, Object> input = parametersUtils.getTaskInput(
                mapOf("input1", "testValue1"), null, taskDef, "testTaskId");

        assertNull(input);
    }

    @Test
    public void testGetTaskInputV1WithNullTaskDef() {
        when(workflowModel.getTasks()).thenReturn(List.of(mapOf("name", "taskName")));

        Map<String, Object> input = parametersUtils.getTaskInput(
                mapOf("input1", "testValue1"), workflowModel, null, "testTaskId");

        assertNull(input);
    }

    @Test
    public void testGetTaskInputV2() throws IOException {
        when(workflowModel.getTasks()).thenReturn(List.of(
                mock(TaskModel.class).givenInputData(mapOf("input1", "testValue1")),
                taskDef
        ));
        when(workflowModel.getTasks().get(1).getInputData()).thenReturn(mapOf("input2", "testValue2"));
        when(taskDef.getInputTemplate()).thenReturn(mapOf("input1", ""));

        Map<String, Object> input = parametersUtils.getTaskInput(
                mapOf("input1", "testValue1"), workflowModel, taskDef, "testTaskId");

        assertEquals(mapOf("input1", "testValue1", "input2", "testValue2"), input);
        verify(taskDef, times(1)).getInputTemplate();
    }

    @Test
    public void testGetTaskInputV2WithNullInputParams() {
        when(workflowModel.getTasks()).thenReturn(List.of(taskDef));

        Map<String, Object> input = parametersUtils.getTaskInput(null, workflowModel, taskDef, "testTaskId");

        assertNull(input);
    }

    @Test
    public void testGetTaskInputV2WithNullWorkflowModel() {
        when(taskDef.getInputTemplate()).thenReturn(mapOf("input1", ""));

        Map<String, Object> input = parametersUtils.getTaskInput(
                mapOf("input1", "testValue1"), null, taskDef, "testTaskId");

        assertNull(input);
    }

    @Test
    public void testGetTaskInputV2WithNullTaskDef() {
        when(workflowModel.getTasks()).thenReturn(List.of(mapOf("name", "taskName")));

        Map<String, Object> input = parametersUtils.getTaskInput(
                mapOf("input1", "testValue1"), workflowModel, null, "testTaskId");

        assertNull(input);
    }

    @Test
    public void testReplaceVariables() throws IOException {
        String input = "{{${workflow.input.input1}}} {{${workflow.input.input2}}}";
        when(workflowModel.getInput()).thenReturn(mapOf("input1", "testValue1", "input2", "testValue2"));

        String expectedOutput = "testValue1 testValue2";
        assertEquals(expectedOutput, parametersUtils.replaceVariables(input, documentContext, null));
    }

    @Test
    public void testReplaceVariablesWithNullDocumentContext() {
        String input = "{{${workflow.input.input1}}} {{${workflow.input.input2}}}";
        assertEquals(input, parametersUtils.replaceVariables(input, null, null));
    }

    @Test
    public void testReplaceVariablesWithNullInput() {
        String input = "{{${workflow.input.input1}}} {{${workflow.input.input2}}}";

        Map<String, Object> workflowInput = mapOf("input1", "testValue1", "input2", null);
        when(workflowModel.getInput()).thenReturn(workflowInput);

        String expectedOutput = "{{${workflow.input.input1}}} {{null}}";
        assertEquals(expectedOutput, parametersUtils.replaceVariables(input, documentContext, null));
    }

    @Test
    public void testReplaceVariablesWithNullWorkflowModel() {
        String input = "{{${workflow.input.input1}}} {{${workflow.input.input2}}}";

        when(workflowModel.getInput()).thenReturn(mapOf("input1", null, "input2", "testValue2"));

        String expectedOutput = "null {{testValue2}}";
        assertEquals(expectedOutput, parametersUtils.replaceVariables(input, documentContext, null));
    }

    @Test
    public void testReplaceVariablesWithEmptyWorkflowModel() {
        String input = "{{${workflow.input.input1}}} {{${workflow.input.input2}}}";

        when(workflowModel.getInput()).thenReturn(Collections.emptyMap());

        String expectedOutput = input;
        assertEquals(expectedOutput, parametersUtils.replaceVariables(input, documentContext, null));
    }

    @Test
    public void testReplaceVariablesWithNullTaskId() {
        String input = "{{${workflow.input.input1}}} {{${workflow.input.input2}}}";

        Map<String, Object> workflowInput = mapOf("input1", "testValue1", "input2", "testValue2");
        when(workflowModel.getInput()).thenReturn(workflowInput);

        String expectedOutput = "testValue1 testValue2";
        assertEquals(expectedOutput, parametersUtils.replaceVariables(input, documentContext, null));
    }

    @Test
    public void testReplaceVariablesWithEmptyDocumentContext() {
        String input = "{{${workflow.input.input1}}} {{${workflow.input.input2}}}";

        Map<String, Object> workflowInput = mapOf("input1", "testValue1", "input2", "testValue2");
        when(workflowModel.getInput()).thenReturn(workflowInput);

        String expectedOutput = input;
        assertEquals(expectedOutput, parametersUtils.replaceVariables(input, null, null));
    }

    @Test
    public void testGetWorkflowInput() throws IOException {
        Map<String, Object> expectedInput = mapOf("input1", "testValue1");
        when(workflowDef.getInputTemplate()).thenReturn(mapOf("input1", ""));

        Map<String, Object> input = parametersUtils.getWorkflowInput(workflowDef, null);

        assertEquals(expectedInput, input);
        verify(workflowDef, times(1)).getInputTemplate();
    }

    @Test
    public void testGetWorkflowInputWithNullWorkflowDef() {
        Map<String, Object> expectedInput = new HashMap<>();

        Map<String, Object> input = parametersUtils.getWorkflowInput(null, null);

        assertEquals(expectedInput, input);
    }

    @Test
    public void testGetWorkflowInputWithNullInputParams() {
        Map<String, Object> expectedInput = new HashMap<>();

        Map<String, Object> input = parametersUtils.getWorkflowInput(workflowDef, null);

        assertEquals(expectedInput, input);
    }

    private Map<String, Object> mapOf(String... keyValues) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put(keyValues[i], keyValues[i + 1]);
        }
        return map;
    }
}