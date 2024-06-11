package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParametersUtilsTest {
    
    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "value2");

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");
        
        // Add assertions for the task input map
        assertEquals("value1", taskInput.get("param1"));
        assertEquals("value2", taskInput.get("param2"));
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "value2");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        
        // Add assertions for the workflow input map
        assertEquals("value1", workflowInput.get("param1"));
        assertEquals("value2", workflowInput.get("param2"));
    }

    // Add more test cases to cover other methods in the ParametersUtils class

}