package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(null); // Mock ObjectMapper

        // Create test input
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";
        Map<String, Object> inputParams = new HashMap<>();

        // Test when workflow schema version is greater than 1
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(taskInputV2);

        // TODO: Add more assertions for the taskInputV2 map

        // Test when workflow schema version is 1 (deprecated)
        Map<String, Object> taskInputV1 = parametersUtils.getTaskInputV1(workflow, inputParams);
        assertNotNull(taskInputV1);

        // TODO: Add more assertions for the taskInputV1 map

    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(null); // Mock ObjectMapper

        // Create test input
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(workflowInput);

        // TODO: Add assertions for the workflowInput map

    }

    // Add more test methods to cover other functionalities in the ParametersUtils class

}