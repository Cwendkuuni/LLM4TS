package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.jayway.jsonpath.Configuration;
import org.jayway.jsonpath.DocumentContext;
import org.jayway.jsonpath.JsonPath;
import org.jayway.jsonpath.Option;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

/** Tests for the ParametersUtils class. */
public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    private ObjectMapper objectMapper;
    private WorkflowModel workflow;
    private TaskModel task;
    private WorkflowDef workflowDef;
    private Map<String, Object> inputParams = new HashMap<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = Mockito.mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);

        workflow = new WorkflowModel();
        task = new TaskModel();
        workflowDef = Mockito.mock(WorkflowDef.class);

        Mockito.when(workflow.getTasks()).thenReturn(Collections.singletonList(task));
        Mockito.when(workflow.getParentWorkflowId()).thenReturn("parentWorkflowId");
        Mockito.when(workflow.getParentWorkflowTaskId()).thenReturn("parentWorkflowTaskId");
        Mockito.when(workflow.getWorkflowId()).thenReturn("workflowId");
        Mockito.when(workflow.getWorkflowName()).thenReturn("workflowName");
        Mockito.when(workflow.getWorkflowVersion()).thenReturn(1);

        inputParams = new HashMap<>();
    }

    @Test
    public void testGetTaskInputV1() {
        workflow.setInput(new HashMap<>());
        workflow.getInput().put("inputKey", "inputValue");
        String taskInputPath = "workflow.input.inputKey";

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflow, inputParams);
        assertEquals("inputValue", result.get("inputKey"));
    }

    @Test
    public void testGetTaskInputV1_NullInput() {
        workflow.setInput(null);

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflow, inputParams);
        assertNull(result);
    }

    @Test
    public void testGetTaskInputV1_NullInputKey() {
        workflow.setInput(new HashMap<>());
        String taskInputPath = "workflow.input.nullKey";

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflow, inputParams);
        assertNull(result);
    }

    @Test
    public void testGetTaskInputV1_NullWorkflow() {
        inputParams.put("inputKey", "inputValue");

        Map<String, Object> result = parametersUtils.getTaskInputV1(null, inputParams);
        assertNull(result);
    }

    @Test
    public void testGetTaskInputV1_NullInputParams() {
        Map<String, Object> result = parametersUtils.getTaskInputV1(workflow, null);
        assertNull(result);
    }

    @Test
    public void testGetTaskInputV2() {
        workflow.setInput(new HashMap<>());
        workflow.getInput().put("inputKey", "inputValue");
        task.setInputData(new HashMap<>());
        task.getInputData().put("taskInputKey", "taskInputValue");
        String taskInputPath = "$.workflow.input.inputKey";
        String taskInputPath2 = "$.workflow.tasks[0].inputData.taskInputKey";

        Configuration config = Configuration.defaultConfiguration();
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), config);

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(inputParams, workflow, task.getReferenceTaskName(), taskDefinition());

        assertEquals("inputValue", result.get("inputKey"));
        assertEquals("taskInputValue", result.get("taskInputKey"));
    }

    @Test
    public void testGetTaskInputV2_NullTask() {
        workflow.setInput(new HashMap<>());
        workflow.getInput().put("inputKey", "inputValue");
        String taskInputPath = "$.workflow.input.inputKey";

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(inputParams, workflow, null, taskDefinition());

        assertEquals("inputValue", result.get("inputKey"));
    }

    @Test
    public void testGetTaskInputV2_NullWorkflow() {
        String taskInputPath = "$.workflow.input.inputKey";

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(inputParams, null, task.getReferenceTaskName(), taskDefinition());

        assertNull(result);
    }

    @Test
    public void testGetTaskInputV2_NullInputParams() {
        workflow.setInput(new HashMap<>());
        workflow.getInput().put("inputKey", "inputValue");
        task.setInputData(new HashMap<>());
        task.getInputData().put("taskInputKey", "taskInputValue");
        String taskInputPath = "$.workflow.input.inputKey";

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(null, workflow, task.getReferenceTaskName(), taskDefinition());

        assertNull(result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.inputKey}.${workflow.input.outputKey}";
        DocumentContext documentContext = JsonPath.parse(new HashMap<>());
        String replacement = "replacementValue";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("", result);

        workflow.setInput(new HashMap<>());
        workflow.getInput().put("inputKey", "inputValue");
        workflow.getInput().put("outputKey", "outputValue");

        documentContext = JsonPath.parse(workflow);
        result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("inputValue.outputValue", result);

        documentContext = JsonPath.parse(new HashMap<>());
        documentContext.setVariables(new HashMap<>());
        documentContext.setVariable("workflow", workflow);
        result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("inputValue.outputValue", result);

        String paramString2 = "${workflow.input[\"inputKey\"].outputKey}";
        result = parametersUtils.replaceVariables(paramString2, documentContext, null);
        assertEquals("outputValue", result);

        String paramString3 = "${workflow.input[\"inputKey\"].outputKey[0]}";
        result = parametersUtils.replaceVariables(paramString3, documentContext, null);
        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithTask() {
        workflow.setInput(new HashMap<>());
        workflow.getInput().put("inputKey", "inputValue");
        task.setInputData(new HashMap<>());
        task.getInputData().put("taskInputKey", "taskInputValue");

        Configuration config = Configuration.defaultConfiguration();
        DocumentContext documentContext = JsonPath.parse(new HashMap<>(), config);

        String paramString = "${workflow.input.inputKey}.${tasks[0].inputData.taskInputKey}";
        String replacement = "replacementValue";

        Map<String, Object> result =
                parametersUtils.replace(inputParams, documentContext, task.getReferenceTaskName());
        assertEquals("inputValue.taskInputValue", result.get("inputKey"));

        documentContext = JsonPath.parse(workflow);
        result = parametersUtils.replace(inputParams, documentContext, task.getReferenceTaskName());
        assertEquals("inputValue.taskInputValue", result.get("inputKey"));

        documentContext = JsonPath.parse(new HashMap<>());
        documentContext.setVariables(new HashMap<>());
        documentContext.setVariable("workflow", workflow);
        documentContext.setVariable("tasks", Collections.singletonList(task));
        result = parametersUtils.replace(inputParams, documentContext, task.getReferenceTaskName());
        assertEquals("inputValue.taskInputValue", result.get("inputKey"));
    }

    @Test
    public void testReplaceVariablesWithEmptyMap() {
        Configuration config = Configuration.defaultConfiguration();
        DocumentContext documentContext = JsonPath.parse(new HashMap<>(), config);

        String paramString = "${workflow.input.inputKey}";
        String replacement = "replacementValue";

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);
        assertNull(result);

        Map<String, Object> result2 =
                parametersUtils.replace(inputParams, documentContext, "taskId");
        assertNull(result2);

        Map<String, Object> inputParams2 = new HashMap<>();
        inputParams2.put("inputKey", "inputValue");
        result = parametersUtils.replace(inputParams2, documentContext, null);
        assertEquals("inputValue", result.get("inputKey"));

        Map<String, Object> result3 = parametersUtils.replace(inputParams2, documentContext, "taskId");
        assertEquals(inputParams2, result3);
    }

    @Test
    public void testReplaceVariablesWithEmptyJsonString() {
        Configuration config = Configuration.defaultConfiguration();
        DocumentContext documentContext = JsonPath.parse("{}", config);

        String paramString = "${workflow.input.inputKey}";
        String replacement = "replacementValue";

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);
        assertNull(result);

        Map<String, Object> result2 =
                parametersUtils.replace(inputParams, documentContext, "taskId");
        assertNull(result2);

        Map<String, Object> inputParams2 = new HashMap<>();
        inputParams2.put("inputKey", "inputValue");
        result = parametersUtils.replace(inputParams2, documentContext, null);
        assertEquals("inputValue", result.get("inputKey"));

        Map<String, Object> result3 = parametersUtils.replace(inputParams2, documentContext, "taskId");
        assertEquals(inputParams2, result3);
    }

    @Test
    public void testReplaceVariablesWithNullJsonString() {
        Configuration config = Configuration.defaultConfiguration();
        DocumentContext documentContext = JsonPath.parse(null, config);

        String paramString = "${workflow.input.inputKey}";
        String replacement = "replacementValue";

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);
        assertNull(result);

        Map<String, Object> result2 =
                parametersUtils.replace(inputParams, documentContext, "taskId");
        assertNull(result2);

        Map<String, Object> inputParams2 = new HashMap<>();
        inputParams2.put("inputKey", "inputValue");
        result = parametersUtils.replace(inputParams2, documentContext, null);
        assertEquals("inputValue", result.get("inputKey"));

        Map<String, Object> result3 = parametersUtils.replace(inputParams2, documentContext, "taskId");
        assertEquals(inputParams2, result3);
    }

    @Test
    public void testGetWorkflowInput() {
        workflowDef.setInputTemplate(new HashMap<>());
        workflowDef.getInputTemplate().put("inputKey", "inputValue");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("inputValue", result.get("inputKey"));
    }

    private TaskDef taskDefinition() {
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        Mockito.when(taskDefinition.getInputTemplate()).thenReturn(new HashMap<>());
        Mockito.when(taskDefinition.getInputTemplate().get("taskInputKey"))
                .thenReturn("taskInputValue");
        return taskDefinition;
    }
}