
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class ParametersUtilsTest {
    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        // Setup data.
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "test";

        // Execute the method.
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Assert the result.
        assertEquals(inputParams, result);
    }
    
    // More test cases can be added here to test other methods in ParametersUtils.

    @Test
    public void testReplaceVariables() {
        // Setup
        String paramString = "${workflow.param}";

        // Execute
        Object replaced = parametersUtils.replace(paramString);

        // Assert
        assertEquals(paramString, replaced.toString());
    }
}
