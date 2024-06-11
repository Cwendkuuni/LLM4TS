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
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

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

@Component
public class ParametersUtilsTest {

    private ParametersUtils utils;
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.objectMapper = mock(ObjectMapper.class);
        this.utils = new ParametersUtils(this.objectMapper);

        this.workflowDef = new WorkflowDef();
        this.workflowModel = new WorkflowModel();
        this.taskDef = new TaskDef();

        when(this.objectMapper.readValue(anyByteArray(), eq(new TypeReference<Map<String, Object>>() {})))
                .then(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = this.workflowModel;
        TaskDef taskDefinition = this.taskDef;
        String taskId = "task1";

        // Test with null inputParams
        Map<String, Object> result = this.utils.getTaskInput(null, workflow, taskDefinition, taskId);
        assertNull(result);

        // Test with empty inputParams
        inputParams.clear();
        result = this.utils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNull(result);

        // Test with non-existent taskDefinition
        taskDefinition = null;
        result = this.utils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNull(result);

        // Test with null workflow
        workflow = null;
        result = this.utils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNull(result);

        // Test with valid input
        inputParams.put("key1", "value1");
        workflow.setInput(Collections.singletonMap("key2", "value2"));
        taskDefinition.setInputTemplate(Collections.singletonMap("key3", "value3"));

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("key1", "value1");
        expectedResult.put("key2", "value2");
        expectedResult.put("key3", "value3");

        result = this.utils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = this.workflowModel;
        String taskId = "task1";

        // Test with null input
        Map<String, Object> result = this.utils.getTaskInputV2(null, workflow, taskId, this.taskDef);
        assertNull(result);

        // Test with empty input
        result = this.utils.getTaskInputV2(input, workflow, taskId, this.taskDef);
        assertNull(result);

        // Test with null workflow
        workflow = null;
        result = this.utils.getTaskInputV2(input, workflow, taskId, this.taskDef);
        assertNull(result);

        // Test with valid input
        input.put("key1", "value1");
        workflow.setInput(Collections.singletonMap("key2", "value2"));
        workflow.getTasks().add(new TaskModel());

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("key1", "value1");
        expectedResult.put("input", workflow.getInput());
        expectedResult.put("tasks", workflow.getTasks());

        result = this.utils.getTaskInputV2(input, workflow, taskId, this.taskDef);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace() {
        String paramString = "${workflow.input.key1} ${workflow.input.key2} ${task.output.key3} ${envVar} ${missing}";
        DocumentContext documentContext = mock(DocumentContext.class);

        // Test with null documentContext
        Object result = this.utils.replace(paramString, documentContext, null);
        assertEquals("", result);

        // Test with empty documentContext
        result = this.utils.replace(paramString, documentContext, Collections.emptyMap());
        assertEquals("", result);

        // Test with valid replacement
        when(documentContext.read("$.workflow.input.key1"))
                .thenReturn("value1");
        when(documentContext.read("$.workflow.input.key2"))
                .thenReturn("value2");
        when(documentContext.read("$.task.output.key3"))
                .thenReturn("value3");
        when(EnvUtils.getSystemParametersValue("envVar", null))
                .thenReturn("envValue");

        result = this.utils.replace(paramString, documentContext, null);
        assertEquals("value1 value2 value3 envValue", result);

        // Test with missing variable
        result = this.utils.replace(paramString, documentContext, null);
        assertEquals("${missing}", result);

        // Test with missing environment variable
        when(EnvUtils.isEnvironmentVariable("missing"))
                .thenReturn(false);
        result = this.utils.replace(paramString, documentContext, null);
        assertEquals("", result);
    }

    @Test
    public void testReplaceList() {
        List<Object> values = new LinkedList<>();
        String paramString = "${${0}} ${${1}} ${${2}}";

        // Test with null values
        Object result = this.utils.replaceList(values, null, documentContext);
        assertEquals(values, result);

        // Test with empty values
        values.clear();
        result = this.utils.replaceList(values, null, documentContext);
        assertEquals(values, result);

        // Test with valid replacements
        when(documentContext.read("$.workflow.input.key1"))
                .thenReturn("value1");
        when(documentContext.read("$.workflow.input.key2"))
                .thenReturn("value2");
        when(documentContext.read("$.workflow.input.key3"))
                .thenReturn("value3");

        values.add("value1");
        values.add("value2");
        values.add("value3");

        result = this.utils.replaceList(values, null, documentContext);
        assertEquals(values, result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.key1}.${workflow.input.key2}.${workflow.input.key3}";
        DocumentContext documentContext = mock(DocumentContext.class);

        // Test with null documentContext
        Object result = this.utils.replaceVariables(paramString, documentContext, null);
        assertEquals("", result);

        // Test with empty documentContext
        result = this.utils.replaceVariables(paramString, documentContext, Collections.emptyMap());
        assertEquals("", result);

        // Test with valid replacement
        when(documentContext.read("$.workflow.input.key1"))
                .thenReturn(new HashMap<String, Object>() {{
                    put("key3", "value3");
                }});
        when(documentContext.read("$.workflow.input.key2"))
                .thenReturn("key2");

        result = this.utils.replaceVariables(paramString, documentContext, null);
        assertEquals("value3.key2.${workflow.input.key3}", result);

        // Test with missing variable
        result = this.utils.replaceVariables(paramString, documentContext, null);
        assertEquals(paramString, result);
    }
}