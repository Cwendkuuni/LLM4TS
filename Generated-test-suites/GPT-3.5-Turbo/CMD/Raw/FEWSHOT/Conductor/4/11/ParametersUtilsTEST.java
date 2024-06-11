package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(new WorkflowDef());
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";
        
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        workflow.setStatus("RUNNING");
        workflow.setWorkflowId("workflow1");
        workflow.setParentWorkflowId("parentWorkflow1");
        workflow.setParentWorkflowTaskId("parentTask1");
        workflow.setWorkflowName("TestWorkflow");
        workflow.setWorkflowVersion(1);
        workflow.setCorrelationId("correlation123");
        workflow.setReasonForIncompletion("Incomplete");
        workflow.setVariables(new HashMap<>());
        
        TaskModel task = new TaskModel();
        task.setInputData(new HashMap<>());
        task.setOutputData(new HashMap<>());
        task.setTaskType("SimpleTask");
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setReferenceTaskName("task1");
        task.setRetryCount(0);
        task.setCorrelationId("taskCorrelation");
        task.setPollCount(1);
        task.setTaskDefName("TestTaskDef");
        task.setScheduledTime(System.currentTimeMillis());
        task.setStartTime(System.currentTimeMillis());
        task.setEndTime(System.currentTimeMillis());
        task.setWorkflowInstanceId("workflow123");
        task.setTaskId("task123");
        task.setReasonForIncompletion("Task incomplete");
        task.setCallbackAfterSeconds(60);
        task.setWorkerId("worker1");
        task.setIteration(1);
        
        workflow.addTask(task);
        
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("workflow.input", workflow.getInput());
        inputMap.put("workflow.output", workflow.getOutput());
        inputMap.put("workflow.status", workflow.getStatus());
        // Add more fields for testing
        
        input.put("key1", "workflow.input.key1");
        input.put("task1.input.key2", "workflow.output.key2");
        
        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, "task1", task);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("workflow.input.key1", result.get("key1"));
    }
    
    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        String input = "{\"key\": \"value\"}";
        Map<String, Object> inputMap = Collections.singletonMap("key", "value");
        
        Map<String, Object> result = parametersUtils.replace(input, inputMap);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("value", result.get("key"));
    }
    
    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        String paramString = "${workflow.input.key1}";
        Map<String, Object> documentContext = Collections.singletonMap("key1", "value1");
        
        Object result = parametersUtils.replaceVariables(paramString, documentContext, "task1");
        
        assertNotNull(result);
        assertEquals("value1", result);
    }
    
    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("input1", "value1");
        
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("value1", result.get("input1"));
    }
}