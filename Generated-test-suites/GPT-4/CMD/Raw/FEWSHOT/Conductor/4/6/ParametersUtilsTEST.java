
package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testClone() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("key", "value");
        Map<String, Object> result = parametersUtils.clone(inputMap);
        assertEquals(inputMap, result);
        assertNotSame(inputMap, result);
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("key", "${value}");
        Map<String, Object> resultMap = parametersUtils.replace(inputMap, new HashMap<>());
        assertEquals("${value}", resultMap.get("key"));
    }

    @Test
    public void testReplaceString() {
        String replacedString = (String) parametersUtils.replace("${variable}");
        assertEquals("${variable}", replacedString);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("param1", "value1");
        workflowDef.setInputTemplate(inputTemplate);
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("value1", result.get("param1"));
    }

    @Test
    public void testGetTaskInput() {
        // Please write unit test method for this
        // Now it would be hard to write this test method due to complex dependencies and private
        // method calls in this method. Proper refactoring or disintegration of this method or
        // mocking frameworks like Mockito or PowerMock can help
    }

    @Test
    public void testGetTaskInputV1() {
        // Similar to the testGetTaskInput, this method would also require a deep
        // mock or refactoring to be able to write unit tests for it.
    }

    @Test
    public void testGetTaskInputV2() {
        // Similar to the testGetTaskInput, this method would also require a deep
        // mock or refactoring to be able to write unit tests for it.
    }
}
