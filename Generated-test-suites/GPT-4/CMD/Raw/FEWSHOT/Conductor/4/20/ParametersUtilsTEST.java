
package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.Before;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    
    @Before
    public void setUp() {
      objectMapper = new ObjectMapper();
      parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel mockWorkflow = new WorkflowModel(new WorkflowDef());
        TaskDef mockTaskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, 
                                                mockWorkflow, "123", mockTaskDef);
        assertEquals("value", result.get("key"));
    }

    // Test with null task definition
    @Test
    public void testGetTaskInputV2_NullTaskDef() {
        WorkflowModel mockWorkflow = new WorkflowModel(new WorkflowDef());
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, 
                                                mockWorkflow, "123", null);
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testClone() {
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");

        Map<String, Object> cloned = parametersUtils.clone(data);
        assertEquals("value", cloned.get("key"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> data = new HashMap<>();
        data.put("key", "${value}");

        Map<String, Object> result = parametersUtils.replace(data, Collections.singletonMap("value", "replacement"));
        assertEquals("replacement", result.get("key"));
    }

    @Test
    public void testReplaceString() {
        String value = "${value}";
        Object result = parametersUtils.replace(value);
        assertEquals("${value}", result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> template = new HashMap<>();
        template.put("key", "value");
        workflowDef.setInputTemplate(template);

        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals("value", result.get("key"));
    }
}
