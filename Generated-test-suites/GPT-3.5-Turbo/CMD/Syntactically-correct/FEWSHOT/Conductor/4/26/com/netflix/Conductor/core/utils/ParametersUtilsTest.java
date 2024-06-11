
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(null); // Pass null as ObjectMapper for testing
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId("123");
        workflow.setWorkflowName("TestWorkflow");
        workflow.setWorkflowVersion(1);
        workflow.setStatus("RUNNING");
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        workflow.setCorrelationId("corr-123");
        workflow.setReasonForIncompletion("Incomplete");

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setName("TestTask");
        taskDefinition.setInputTemplate(new HashMap<>());
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${workflow.input.param1}");
        inputParams.put("param2", "${workflow.output.param2}");
        inputParams.put("param3", "${workflow.status}");
        
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "task-123");
        
        assertNotNull(taskInput);
        assertEquals(3, taskInput.size());
        assertEquals("123", taskInput.get("param1"));
        assertEquals(null, taskInput.get("param2")); // Output is not set in the workflow
        assertEquals("RUNNING", taskInput.get("param3"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        workflowDef.getInputTemplate().put("param1", "value1");
        workflowDef.getInputTemplate().put("param2", "value2");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param2", "value_override");
        inputParams.put("param3", "value3");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals(3, workflowInput.size());
        assertEquals("value1", workflowInput.get("param1"));
        assertEquals("value_override", workflowInput.get("param2")); // Overridden value
        assertEquals("value3", workflowInput.get("param3"));
    }

}
