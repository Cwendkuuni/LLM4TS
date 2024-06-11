package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        workflow.setStatus("RUNNING");
        workflow.setWorkflowId("12345");
        workflow.setWorkflowType("TestWorkflow");
        workflow.setVariables(new HashMap<>());

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "task1", taskDefinition);

        assertNotNull(taskInput);
        assertEquals("12345", taskInput.get("workflow").get("workflowId"));
        assertEquals("TestWorkflow", taskInput.get("workflow").get("workflowType"));
        assertEquals("RUNNING", taskInput.get("workflow").get("status"));
    }

    @Test
    public void testReplaceVariables() {
        String input = "Hello ${{workflow.workflowId}}";
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("workflow", Map.of("workflowId", "12345"));

        Object replaced = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals("Hello 12345", replaced.toString());
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals(0, workflowInput.size());
    }
}