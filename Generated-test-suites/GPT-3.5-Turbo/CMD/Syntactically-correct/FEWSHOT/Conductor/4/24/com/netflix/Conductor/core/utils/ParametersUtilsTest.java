package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        workflow.setStatus("COMPLETED");
        workflow.setWorkflowId("workflowId");
        workflow.setParentWorkflowId("parentWorkflowId");
        workflow.setParentWorkflowTaskId("parentWorkflowTaskId");
        workflow.setWorkflowName("workflowName");
        workflow.setWorkflowVersion(1);
        workflow.setCorrelationId("correlationId");
        workflow.setReasonForIncompletion("reasonForIncompletion");
        
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName("task1");
        taskModel.setInputData(new HashMap<>());
        taskModel.setOutputData(new HashMap<>());
        taskModel.setTaskType("taskType");
        taskModel.setStatus(TaskModel.TaskStatus.COMPLETED);
        taskModel.setReferenceTaskName("referenceTaskName");
        taskModel.setRetryCount(1);
        taskModel.setCorrelationId("correlationId");
        taskModel.setPollCount(1);
        taskModel.setTaskDefName("taskDefName");
        taskModel.setScheduledTime(System.currentTimeMillis());
        taskModel.setStartTime(System.currentTimeMillis());
        taskModel.setEndTime(System.currentTimeMillis());
        taskModel.setWorkflowInstanceId("workflowInstanceId");
        taskModel.setTaskId("taskId");
        taskModel.setReasonForIncompletion("reasonForIncompletion");
        taskModel.setCallbackAfterSeconds(10);
        taskModel.setWorkerId("workerId");
        taskModel.setIteration(1);
        
        workflow.getTasks().add(taskModel);
        
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());
        
        Map<String, Object> inputParams = new HashMap<>();
        
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDefinition);
        
        assertEquals("COMPLETED", result.get("status"));
        // Add more assertions as needed
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${workflow.input.test}");
        input.put("key2", "${task.input.param1}");
        
        Object json = "{ \"workflow\": { \"input\": { \"test\": \"value1\" } }, \"task\": { \"input\": { \"param1\": \"value2\" } } }";
        
        Map<String, Object> result = parametersUtils.replace(input, json);
        
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        // Add more assertions as needed
    }

    @Test
    public void testReplaceVariables() {
        DocumentContext documentContext = JsonPath.parse("{ \"workflow\": { \"input\": { \"test\": \"value1\" } } }");
        String paramString = "${workflow.input.test}";
        
        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");
        
        assertEquals("value1", result);
        // Add more assertions as needed
    }

}