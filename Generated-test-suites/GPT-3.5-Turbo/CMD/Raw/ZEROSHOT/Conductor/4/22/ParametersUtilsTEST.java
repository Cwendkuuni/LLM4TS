package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        // Create mock objects
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        String taskId = "123";

        // Define behavior for mocks
        when(workflow.getWorkflowDefinition()).thenReturn(new WorkflowDef());
        when(workflow.getTasks()).thenReturn(new ArrayList<>());

        // Test the method
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Verify the result
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testReplace() {
        // Create mock objects
        Map<String, Object> input = new HashMap<>();
        Object json = "{\"key\": \"value\"}";

        // Test the method
        Map<String, Object> result = parametersUtils.replace(input, json);

        // Verify the result
        assertEquals(input, result);
    }

    @Test
    public void testReplaceString() {
        // Test the method
        Object result = parametersUtils.replace("test");

        // Verify the result
        assertEquals("test", result);
    }
}