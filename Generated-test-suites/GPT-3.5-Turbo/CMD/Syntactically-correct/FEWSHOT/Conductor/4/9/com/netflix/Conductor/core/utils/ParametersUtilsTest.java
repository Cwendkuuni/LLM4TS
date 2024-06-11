
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    
    private ParametersUtils parametersUtils;
    private Map<String, Object> inputParams;
    private WorkflowModel workflow;
    private TaskDef taskDefinition;
    private String taskId;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
        inputParams = new HashMap<>();
        
        // Setup mock WorkflowModel, TaskDef, and taskId
        // workflow = setupWorkflowModel();
        // taskDefinition = setupTaskDef();
        // taskId = "task123";
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(taskInput);
        // Add assertions based on expected values
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        assertNotNull(taskInput);
        // Add assertions based on expected values
    }

    @Test
    public void testReplace() {
        Object replaced = parametersUtils.replace(inputParams, "{}");
        assertNotNull(replaced);
        // Add assertions based on expected values
    }

    @Test
    public void testReplaceVariables() {
        Object replaced = parametersUtils.replaceVariables("${test.param}", JsonPath.parse(Collections.singletonMap("test", "value")));
        assertEquals("value", replaced);
    }
    
    // Additional test cases for other methods in ParametersUtils class can be added here

}
