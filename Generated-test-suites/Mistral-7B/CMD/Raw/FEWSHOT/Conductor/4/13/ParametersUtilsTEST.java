package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** Used to parse and resolve the JSONPath bindings in the workflow and task definitions. */
public class ParametersUtilsTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private WorkflowModel workflow;
    @Mock
    private TaskDef taskDefinition;
    @Mock
    private TaskModel taskModel;
    @Mock
    private WorkflowDef workflowDef;
    @InjectMocks
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(workflow.getTasks()).thenReturn(Collections.singletonList(taskModel));
        when(taskModel.getReferenceTaskName()).thenReturn("task1");
        when(taskModel.getTaskDefName()).thenReturn("taskDefinition1");
        when(workflow.getInput()).thenReturn(Collections.singletonMap("input1", "inputValue1"));
        when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("input2", "inputValue2"));
        when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("input3", "inputValue3"));
    }

    @After
    public void tearDown() {
        objectMapper = null;
        workflow = null;
        taskDefinition = null;
        taskModel = null;
        workflowDef = null;
        parametersUtils = null;
    }

    @Test
    public void testGetTaskInput_WithValidInputParamsAndWorkflowAndTaskDefinition_ReturnsReplacedMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.input1}", "workflowInputValue1");
        inputParams.put("${taskDefinition.inputTemplate.input3}", "taskDefinitionInputValue3");
        inputParams.put("${input2}", "inputValue2");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("workflowInputValue1", "inputValue1");
        expectedResult.put("inputValue2", "inputValue2");
        expectedResult.put("taskDefinitionInputValue3", "inputValue3");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInput_WithNullInputParamsAndValidWorkflowAndTaskDefinition_ReturnsEmptyMap() {
        Map<String, Object> result = parametersUtils.getTaskInput(null, workflow, taskDefinition, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInput_WithValidInputParamsAndNullWorkflowAndTaskDefinition_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.input1}", "workflowInputValue1");
        inputParams.put("${taskDefinition.inputTemplate.input3}", "taskDefinitionInputValue3");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, null, null, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInput_WithValidInputParamsAndWorkflowWithNullInput_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.input1}", "workflowInputValue1");
        inputParams.put("${taskDefinition.inputTemplate.input3}", "taskDefinitionInputValue3");

        when(workflow.getInput()).thenReturn(null);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInput_WithValidInputParamsAndTaskDefinitionWithNullInputTemplate_ReturnsEmptyMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.input1}", "workflowInputValue1");
        inputParams.put("${taskDefinition.inputTemplate.input3}", "taskDefinitionInputValue3");

        when(taskDefinition.getInputTemplate()).thenReturn(null);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testReplace_WithStringInput_ReturnsReplacedString() {
        String input = "${workflow.input.input1} ${taskDefinition.inputTemplate.input3} ${input2}";
        DocumentContext documentContext = mock(DocumentContext.class);

        String expectedResult = "workflowInputValue1 taskDefinitionInputValue3 inputValue2";

        when(documentContext.read("input2")).thenReturn("inputValue2");
        when(workflow.getInput()).thenReturn(Collections.singletonMap("input1", "workflowInputValue1"));
        when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("input3", "taskDefinitionInputValue3"));

        String result = (String) parametersUtils.replace(input, documentContext, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_WithMapInput_ReturnsReplacedMap() {
        Map<String, Object> input = new HashMap<>();
        input.put("${workflow.input.input1}", "workflowInputValue1");
        input.put("${taskDefinition.inputTemplate.input3}", "taskDefinitionInputValue3");
        input.put("${input2}", "inputValue2");

        DocumentContext documentContext = mock(DocumentContext.class);

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("workflowInputValue1", "inputValue1");
        expectedResult.put("inputValue2", "inputValue2");
        expectedResult.put("taskDefinitionInputValue3", "inputValue3");

        when(documentContext.read("input2")).thenReturn("inputValue2");
        when(workflow.getInput()).thenReturn(Collections.singletonMap("input1", "workflowInputValue1"));
        when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("input3", "taskDefinitionInputValue3"));

        Map<String, Object> result = parametersUtils.replace(input, documentContext, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_WithJsonStringInput_ReturnsReplacedJsonString() {
        String input = "{\"input1\": \"${workflow.input.input1}\", \"input2\": \"${input2}\", \"input3\": \"${taskDefinition.inputTemplate.input3}\"}";
        DocumentContext documentContext = mock(DocumentContext.class);

        String expectedResult = "{\"input1\": \"workflowInputValue1\", \"input2\": \"inputValue2\", \"input3\": \"taskDefinitionInputValue3\"}";

        when(documentContext.read("input2")).thenReturn("inputValue2");
        when(workflow.getInput()).thenReturn(Collections.singletonMap("input1", "workflowInputValue1"));
        when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("input3", "taskDefinitionInputValue3"));

        String result = (String) parametersUtils.replace(input, documentContext, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReplace_WithNullInput_ReturnsOriginalInput() {
        String input = "${workflow.input.input1} ${taskDefinition.inputTemplate.input3} ${input2}";
        DocumentContext documentContext = mock(DocumentContext.class);

        String result = (String) parametersUtils.replace(input, documentContext, null);
        assertEquals(input, result);
    }

    @Test
    public void testReplace_WithEmptyDocumentContext_ReturnsOriginalInput() {
        String input = "${workflow.input.input1} ${taskDefinition.inputTemplate.input3} ${input2}";
        DocumentContext documentContext = mock(DocumentContext.class);

        when(documentContext.read(ArgumentMatchers.anyString())).thenReturn(null);

        String result = (String) parametersUtils.replace(input, documentContext, null);
        assertEquals(input, result);
    }

    @Test
    public void testGetTaskInputV1_WithValidInputParamsAndWorkflow_ReturnsReplacedMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.input1}", "workflowInputValue1");
        inputParams.put("task1.input", "taskInputValue1");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("workflowInputValue1", "inputValue1");
        expectedResult.put("taskInputValue1", "taskInputValue1");

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflow, inputParams);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTaskInputV1_WithNullInputParamsAndWorkflow_ReturnsEmptyMap() {
        Map<String, Object> result = parametersUtils.getTaskInputV1(workflow, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetWorkflowInput_WithValidInputParamsAndWorkflowDef_ReturnsReplacedMap() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflowDef.inputTemplate.input1}", "workflowDefInputValue1");

        Map<String, Object> expectedResult = new HashMap<>();
        expectedResult.put("workflowDefInputValue1", "inputValue1");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetWorkflowInput_WithNullInputParamsAndWorkflowDef_ReturnsEmptyMap() {
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}