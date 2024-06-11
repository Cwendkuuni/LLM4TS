package com.netflix.conductor.core.utils;

import static com.netflix.conductor.common.utils.ParametersUtils.clone;
import static com.netflix.conductor.common.utils.ParametersUtils.getTaskInput;
import static com.netflix.conductor.common.utils.ParametersUtils.getTaskInputV1;
import static com.netflix.conductor.common.utils.ParametersUtils.getWorkflowInput;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/** Tests for the ParametersUtils class. */
public class ParametersUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private ObjectMapper objectMapper;
    private WorkflowDef workflowDef;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        this.objectMapper = mock(ObjectMapper.class);
        this.workflowDef = mock(WorkflowDef.class);
        this.workflowModel = mock(WorkflowModel.class);
        this.taskDef = mock(TaskDef.class);
        this.parametersUtils = new ParametersUtils(this.objectMapper);

        when(this.workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        when(this.workflowModel.getWorkflowDefinition()).thenReturn(this.workflowDef);
        when(this.workflowModel.getInput()).thenReturn(Collections.emptyMap());
        when(this.workflowModel.getTasks()).thenReturn(Collections.emptyList());
        when(this.workflowModel.getWorkflowName()).thenReturn("testWorkflow");
        when(this.workflowModel.getWorkflowVersion()).thenReturn(1);
        when(this.workflowModel.getCorrelationId()).thenReturn("testCorrelationId");
        when(this.taskDef.getInputTemplate()).thenReturn(Collections.singletonMap("testKey", "testValue"));
        when(this.taskDef.getInputData()).thenReturn(Collections.singletonMap("testKey2", "testValue2"));
        when(this.taskDef.getOutputData()).thenReturn(Collections.singletonMap("testKey3", "testValue3"));
        when(this.taskDef.getTaskType()).thenReturn("testTaskType");
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testInput", "testValue");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("testInput", "testValue");
        expectedResult.put("testKey", "testValue");

        Map<String, Object> result = this.parametersUtils.getTaskInput(inputParams, this.workflowModel, this.taskDef, "testTaskId");

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputWithNullInput() {
        Map<String, Object> result = this.parametersUtils.getTaskInput(null, this.workflowModel, this.taskDef, "testTaskId");

        assertNull(result);
    }

    @Test
    public void testGetTaskInputV1() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testInput.testWorkflow.testKey", "testValue");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("testInput.testWorkflow.testKey", "testValue");

        Map<String, Object> result = this.parametersUtils.getTaskInputV1(this.workflowModel, inputParams);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputV1WithNullInput() {
        Map<String, Object> result = this.parametersUtils.getTaskInputV1(this.workflowModel, null);

        assertNull(result);
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("testKey", "testValue");

        Map<String, Object> result = this.parametersUtils.getWorkflowInput(this.workflowDef, inputParams);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace() {
        String paramString = "${testWorkflow.testKey}.${testTaskType}.testKey3";

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.testWorkflow.testKey")).thenReturn("testValue");

        Object replacement = this.parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("testValue.testTaskType.testKey3", replacement);
    }

    @Test
    public void testReplaceWithNullDocumentContext() {
        String paramString = "${testWorkflow.testKey}.${testTaskType}.testKey3";

        Object replacement = this.parametersUtils.replaceVariables(paramString, null, null);

        assertNull(replacement);
    }

    @Test
    public void testReplaceWithEnvironmentVariable() {
        String paramString = "${TEST_ENV_VAR}";
        String environmentVariable = "testValue";

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read(paramString)).thenReturn(null);
        when(EnvUtils.getSystemParametersValue(paramString, null)).thenReturn(environmentVariable);

        Object replacement = this.parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(environmentVariable, replacement);
    }

    @Test
    public void testReplaceWithEmptyString() {
        String paramString = "${}";

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read(paramString)).thenReturn(null);

        Object replacement = this.parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("", replacement);
    }

    @Test
    public void testReplaceList() {
        List<Object> values = List.of("${testWorkflow.testKey}", "${testTaskType}.testKey3");

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read("$.testWorkflow.testKey")).thenReturn("testValue");

        List<Object> replacedList = this.parametersUtils.replaceList(values, null, documentContext);

        assertEquals(List.of("testValue", "testTaskType.testKey3"), replacedList);
    }

    @Test
    public void testReplaceListWithNullDocumentContext() {
        List<Object> values = List.of("${testWorkflow.testKey}", "${testTaskType}.testKey3");

        List<Object> replacedList = this.parametersUtils.replaceList(values, null, null);

        assertEquals(Collections.emptyList(), replacedList);
    }

    @Test
    public void testReplaceListWithEnvironmentVariable() {
        List<Object> values = List.of("${TEST_ENV_VAR}", "${testTaskType}.testKey3");
        String environmentVariable = "testValue";

        DocumentContext documentContext = mock(DocumentContext.class);
        when(documentContext.read(paramString -> paramString.startsWith("$.testWorkflow")))
                .thenReturn(null);
        when(EnvUtils.getSystemParametersValue(paramString, null)).thenReturn(environmentVariable);

        List<Object> replacedList = this.parametersUtils.replaceList(values, null, documentContext);

        assertEquals(List.of(environmentVariable, "${testTaskType}.testKey3"), replacedList);
    }
}