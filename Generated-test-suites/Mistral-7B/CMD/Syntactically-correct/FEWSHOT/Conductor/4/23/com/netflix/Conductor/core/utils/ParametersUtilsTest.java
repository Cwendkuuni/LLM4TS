package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
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
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtils.class);
    private static final Pattern PATTERN =
            Pattern.compile(
                    "(?=(?<!\\$)\\$\\{)(?:(?=.*?\\{(?!.*?\\1)(.*\\}(?!.*\\2).*))(?=.*?\\}(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^{]*(?=\\2$)",
                    Pattern.DOTALL);

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;
    private TaskDef taskDefinition;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.writeValueAsBytes(ArgumentMatchers.any())).thenReturn(new byte[0]);
        when(objectMapper.readValue(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(new TypeReference<Map<String, Object>>() {})))
                .thenReturn(new HashMap<>());

        this.objectMapper = objectMapper;
        this.parametersUtils = new ParametersUtils(objectMapper);

        this.taskDefinition = mock(TaskDef.class);
        this.workflowDef = mock(WorkflowDef.class);
        this.workflowModel = mock(WorkflowModel.class);
    }

    @Test
    public void testGetTaskInputEmptyInputParams() {
        Map<String, Object> result = parametersUtils.getTaskInput(
                null,
                workflowModel,
                taskDefinition,
                "taskId");
        assertNull(result);
    }

    @Test
    public void testGetTaskInputNoTaskDefinition() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams,
                workflowModel,
                null,
                "taskId");
        assertNull(result);
    }

    @Test
    public void testGetTaskInputWithTaskDefinition() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");

        when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("key2", "value2"));

        Map<String, Object> result = parametersUtils.getTaskInput(
                inputParams,
                workflowModel,
                taskDefinition,
                "taskId");

        Map<String, Object> expected = new HashMap<>();
        expected.put("key1", "value1");
        expected.put("key2", "value2");

        assertEquals(expected, result);
    }

    @Test
    public void testReplaceString() {
        String input = "${workflow.input.a}.${workflow.input.b}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("workflow.input.a")).thenReturn("valueA");
        when(documentContext.read("workflow.input.b")).thenReturn("valueB");

        Object replacement = parametersUtils.replaceVariables(input, documentContext, null);

        assertEquals("valueA.valueB", replacement);
    }

    @Test
    public void testReplaceStringWithEmptyValue() {
        String input = "${workflow.input.a}.${workflow.input.b}";
        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("workflow.input.a")).thenReturn(null);
        when(documentContext.read("workflow.input.b")).thenReturn("valueB");

        Object replacement = parametersUtils.replaceVariables(input, documentContext, null);

        assertNull(replacement);
    }

    @Test
    public void testReplaceStringWithEnvironmentVariable() {
        String input = "${ENV_VAR}";
        DocumentContext documentContext = mock(DocumentContext.class);
        String envValue = "value";
        when(EnvUtils.getSystemParametersValue(input, null)).thenReturn(envValue);

        Object replacement = parametersUtils.replaceVariables(input, documentContext, null);

        assertEquals(envValue, replacement);
    }

    @Test
    public void testReplaceStringWithNullDocumentContext() {
        String input = "${workflow.input.a}.${workflow.input.b}";
        DocumentContext documentContext = null;

        Object replacement = parametersUtils.replaceVariables(input, documentContext, null);

        assertNull(replacement);
    }

    @Test
    public void testReplaceStringWithEmptyDocumentContext() {
        String input = "${workflow.input.a}.${workflow.input.b}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), Configuration.defaultConfiguration());

        Object replacement = parametersUtils.replaceVariables(input, documentContext, null);

        assertNull(replacement);
    }

    @Test
    public void testReplaceMap() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input.a}.${workflow.input.b}");

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("workflow.input.a")).thenReturn("valueA");
        when(documentContext.read("workflow.input.b")).thenReturn("valueB");

        Map<String, Object> result = parametersUtils.replace(input, documentContext, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("key1", "valueA.valueB");

        assertEquals(expected, result);
    }

    @Test
    public void testReplaceList() {
        List<Object> input = new LinkedList<>();
        input.add("${workflow.input.a}.${workflow.input.b}");

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("workflow.input.a")).thenReturn("valueA");
        when(documentContext.read("workflow.input.b")).thenReturn("valueB");

        List<Object> result = parametersUtils.replaceList(input, null, documentContext);

        List<Object> expected = new LinkedList<>();
        expected.add("valueA.valueB");

        assertEquals(expected, result);
    }

    @Test
    public void testReplaceListWithNullDocumentContext() {
        List<Object> input = new LinkedList<>();
        input.add("${workflow.input.a}.${workflow.input.b}");

        DocumentContext documentContext = null;

        List<Object> result = parametersUtils.replaceList(input, null, documentContext);

        List<Object> expected = new LinkedList<>();

        assertSame(expected, result);
    }
}