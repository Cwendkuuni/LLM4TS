package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.netflix.conductor.core.utils.ParametersUtils;

public class ParametersUtilsTest {

    private ParametersUtils utils;
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        utils = new ParametersUtils(objectMapper);
        workflowDef = Mockito.mock(WorkflowDef.class);
        taskDef = Mockito.mock(TaskDef.class);
        workflowModel = Mockito.mock(WorkflowModel.class);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();

        // Test with valid input
        when(workflowModel.getInput()).thenReturn(Collections.singletonMap("testKey", "testValue"));
        when(workflowModel.getTasks()).thenReturn(Collections.singletonList(taskModel()));
        when(taskModel().getInputData()).thenReturn(Collections.singletonMap("taskTestKey", "taskTestValue"));

        Map<String, Object> result = utils.getTaskInputV2(inputParams, workflowModel, taskDef, "testTaskId");

        assertEquals("testValue", result.get("workflow.input.testKey"));
        assertEquals("taskTestValue", result.get("tasks.testTaskId.input.taskTestKey"));

        // Test with null workflowModel
        result = utils.getTaskInputV2(inputParams, null, taskDef, "testTaskId");
        assertNull(result);

        // Test with null taskDef
        result = utils.getTaskInputV2(inputParams, workflowModel, null, "testTaskId");
        assertNull(result);
    }

    @Test
    public void testReplace() {
        String paramString = "${workflow.input.testKey}.${tasks.testTaskId.input.taskTestKey}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        // Test with valid documentContext
        when(documentContext.read("$.workflow.input.testKey")).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "testValue";
            }
        });
        when(documentContext.read("$.tasks[0].input.taskTestKey")).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "taskTestValue";
            }
        });

        String result = utils.replaceVariables(paramString, documentContext, "testTaskId");

        assertEquals("testValue.taskTestValue", result);

        // Test with empty documentContext
        result = utils.replaceVariables(paramString, Mockito.mock(DocumentContext.class), "testTaskId");
        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceList() {
        List<Object> values = new LinkedList<>();
        values.add("${workflow.input.testKey}");
        values.add(Collections.singletonMap("testKey", "testValue"));

        String result = utils.replaceList(values, "testTaskId", Mockito.mock(DocumentContext.class));

        assertEquals("[testValue, {testKey=testValue}]", result.toString());
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${${${}}";
        DocumentContext documentContext = Mockito.mock(DocumentContext.class);

        String result = utils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result);

        when(documentContext.read("${${}}")).thenReturn("testValue");

        result = utils.replaceVariables(paramString, documentContext, null);

        assertEquals("${testValue}", result);
    }

    private TaskModel taskModel() {
        return Mockito.mock(TaskModel.class);
    }
}