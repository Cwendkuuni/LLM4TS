package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.stereotype.Component;

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
import com.netflix.conductor.model.WorkflowModel.WorkflowInstanceIdentifier;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        Configuration configuration = Configuration.defaultConfiguration();
        DocumentContext documentContext = mock(DocumentContext.class);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());

        TaskDef taskDef = mock(TaskDef.class);
        Map<String, Object> taskInput = new HashMap<>();
        when(taskDef.getInputTemplate()).thenReturn(taskInput);
        when(taskDef.getInputData()).thenReturn(Collections.emptyMap());

        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        assertEquals(Collections.emptyMap(), result);

        // Test when inputParams is null
        result = parametersUtils.getTaskInput(null, workflowModel, taskDef, "taskId");
        assertEquals(Collections.emptyMap(), result);

        // Test when taskDef is null
        result = parametersUtils.getTaskInput(inputParams, workflowModel, null, "taskId");
        assertEquals(Collections.emptyMap(), result);

        // Test when workflowModel is null
        result = parametersUtils.getTaskInput(inputParams, null, taskDef, "taskId");
        assertEquals(Collections.emptyMap(), result);

        // Test with valid input
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("inputKey", "value");
        when(workflowDef.getInputTemplate()).thenReturn(inputTemplate);

        taskInput.put("taskKey", "value");

        inputParams.put("workflowKey", "workflowValue");

        result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
        assertEquals("workflowValue", result.get("workflow"));
        assertEquals("value", result.get("input.taskKey"));
    }

    @Test
    public void testGetTaskInputV2() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        Configuration configuration = Configuration.defaultConfiguration();
        DocumentContext documentContext = mock(DocumentContext.class);

        WorkflowModel workflowModel = new WorkflowModel();
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        when(workflowDef.getSchemaVersion()).thenReturn(2);

        TaskDef taskDef = mock(TaskDef.class);
        Map<String, Object> taskInput = new HashMap<>();
        when(taskDef.getInputTemplate()).thenReturn(taskInput);
        when(taskDef.getInputData()).thenReturn(Collections.emptyMap());
        when(taskDef.getTaskType()).thenReturn("taskType");

        Map<String, Object> workflow = new HashMap<>();
        workflow.put("input", "workflowInput");
        workflow.put("output", "workflowOutput");
        workflow.put("status", "workflowStatus");
        workflow.put("workflowId", "workflowId");
        workflow.put("parentWorkflowId", "parentWorkflowId");
        workflow.put("parentWorkflowTaskId", "parentWorkflowTaskId");
        workflow.put("workflowType", "workflowType");
        workflow.put("version", "version");
        workflow.put("correlationId", "correlationId");
        workflow.put("reasonForIncompletion", "reasonForIncompletion");
        workflow.put("variables", "variables");

        List<TaskModel> tasks = new LinkedList<>();
        TaskModel task = new TaskModel();
        task.setInputData(Collections.singletonMap("taskKey", "taskValue"));
        task.setOutputData(Collections.singletonMap("taskKey", "taskOutputValue"));
        task.setReferenceTaskName("taskRefName");
        task.setTaskDefName("taskDefName");
        task.setScheduledTime(System.currentTimeMillis());
        task.setStartTime(System.currentTimeMillis());
        task.setEndTime(System.currentTimeMillis());
        task.setWorkerId("workerId");
        task.setIteration(1);
        tasks.add(task);
        workflow.put("tasks", tasks);

        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflowKey", "workflowInput");

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, null, null);

        assertEquals(workflow, result);

        // Test when inputParams is null
        result = parametersUtils.getTaskInputV2(null, workflowModel, null, null);
        assertNull(result);

        // Test when workflowModel is null
        result = parametersUtils.getTaskInputV2(inputParams, null, null, null);
        assertNull(result);

        // Test with valid input
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("inputKey", "value");
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate);

        Map<String, Object> taskParams = new HashMap<>();
        taskParams.put("taskKey", "taskValue");

        workflow.put("tasks", tasks);
        tasks.get(0).setInputData(taskParams);

        inputParams.put("workflowKey", workflow);

        result = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskDef, null);
        assertEquals("taskValue", result.get("tasks.0.input.taskKey"));
        assertEquals("taskOutputValue", result.get("tasks.0.output.taskKey"));
        assertEquals("taskDefName", result.get("tasks.0.taskDefName"));
        assertEquals("taskType", result.get("tasks.0.taskType"));
    }

    @Test
    public void testReplace() {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        Configuration configuration = Configuration.defaultConfiguration();
        DocumentContext documentContext = mock(DocumentContext.class);

        String paramString = "${workflow.input.inputKey}.${tasks.0.output.taskKey}.${workflow.output}";
        String workflowInput = "workflowInput";
        String taskOutput = "taskOutputValue";
        String workflowOutput = "workflowOutput";

        when(documentContext.read("$.workflow.input.inputKey")).thenReturn("inputValue");
        when(documentContext.read("$.tasks.0.output.taskKey")).thenReturn("taskInputValue");

        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("inputValue.taskOutputValue.workflowOutput", result);

        // Test with null documentContext
        result = parametersUtils.replaceVariables(paramString, null, null);
        assertEquals(paramString, result);

        // Test with empty map
        DocumentContext emptyDocumentContext = new DocumentContext(Collections.emptyMap());
        result = parametersUtils.replaceVariables(paramString, emptyDocumentContext, null);
        assertEquals(paramString, result);

        // Test with system variables
        String systemVariable = "systemVariable";
        when(EnvUtils.getSystemParametersValue(paramString, null)).thenReturn(systemVariable);

        result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals(systemVariable, result);

        // Test with empty string
        paramString = "${}";
        result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals(paramString, result);
    }
}