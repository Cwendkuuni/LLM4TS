
package com.netflix.conductor.core.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.WorkflowModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private final ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
    private final Map<String, Object> dummyInput = Collections.singletonMap("key", "value");

    @Test
    public void testReplace() {
        Map<String, Object> result = parametersUtils.replace(dummyInput, dummyInput);
        Assert.assertEquals(dummyInput, result);
    }

    @Test
    public void testReplaceString() {
        Object result = parametersUtils.replace("test");
        Assert.assertEquals("test", result);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel dummyWorkflowModel = new WorkflowModel();
        TaskDef dummyTaskDefinition = new TaskDef();
        Map<String, Object> result = parametersUtils.getTaskInput(dummyInput, dummyWorkflowModel, dummyTaskDefinition, "dummyTaskId");
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testGetTaskInputV2() {
        WorkflowModel dummyWorkflowModel = new WorkflowModel();
        dummyWorkflowModel.setWorkflowDefinition(new WorkflowDef());
        dummyWorkflowModel.getWorkflowDefinition().setSchemaVersion(2);
        TaskDef dummyTaskDef = new TaskDef();
        dummyTaskDef.setInputTemplate(new HashMap<>());
        Map<String, Object> result = parametersUtils.getTaskInputV2(dummyInput, dummyWorkflowModel, "dummyTaskId", dummyTaskDef);
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testGetWorkflowInput() {
        WorkflowDef dummyWorkflowDef = new WorkflowDef();
        dummyWorkflowDef.setInputTemplate(new HashMap<>());
        Map<String, Object> result = parametersUtils.getWorkflowInput(dummyWorkflowDef, dummyInput);
        Assert.assertEquals(dummyInput, result);
    }

    // For edge cases and potential exception handling scenarios

    @Test(expected = RuntimeException.class)
    public void testCloneWithInvalidInput() {
        // Providing a non-Serializable input should result in a RuntimeException
        parametersUtils.clone(Collections.singletonMap("key", new Object()));
    }

    @Test
    public void testReplaceWithNullInput() {
        // If we have a null value in a key-value pair in the input map, it should be replaced with the returned value from taskDefinition.getInputTemplate()
        Map<String, Object> input = new HashMap<>();
        input.put("key", null);
        Map<String, Object> result = parametersUtils.replace(input, dummyInput);
        Assert.assertEquals(dummyInput, result.get("key"));
    }
}
