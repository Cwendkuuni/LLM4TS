package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testClone() {
        HashMap<String, Object> input = new HashMap<>();
        input.put("key", "value");

        HashMap<String, Object> result = (HashMap<String, Object>) parametersUtils.clone(input);

        assertEquals(input, result);
    }

    @Test
    public void testCloneWithIOException() {
        // To trigger IOException, we will pass a non-serializable object
        HashMap<String, Object> input = new HashMap<>();
        input.put("key", new Object());

        try {
            parametersUtils.clone(input);
            fail("Expected exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("Unable to clone input params", e.getMessage());
        }
    }

    @Test
    public void testGetTaskInput() {
        // We use mocks for complex objects
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
        
        TaskDef taskDefinition = mock(TaskDef.class);
        when(taskDefinition.getInputTemplate()).thenReturn(new HashMap<>());

        HashMap<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "val1");
        
        try {
            parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Unable to clone input params"));
        }
    }

    @Test
    public void testReplace() {
        HashMap<String, Object> input = new HashMap<>();
        input.put("key1", "${task1.output.key1}");
        input.put("key2", "${workflow.input.key2}");

        HashMap<String, Object> replacements = new HashMap<>();
        replacements.put("task1.output.key1", "value1");
        replacements.put("workflow.input.key2", "value2");

        HashMap<String, Object> replaced = (HashMap<String, Object>) parametersUtils.replace(input, replacements);
        
        assertNotNull(replaced.get("key1"));
        assertNotNull(replaced.get("key2"));
    }

    @Test
    public void testReplaceString() {
        String replaced = (String) parametersUtils.replace("${task1.output.key1}");
        assertTrue(replaced.isEmpty());
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        when(workflowDef.getInputTemplate()).thenReturn(Collections.emptyMap());
        
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, Collections.emptyMap());
        assertNotNull(workflowInput);
    }
}
