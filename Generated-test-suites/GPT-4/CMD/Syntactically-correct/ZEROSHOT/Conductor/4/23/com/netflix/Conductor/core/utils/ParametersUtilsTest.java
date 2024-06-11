
package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(new ObjectMapper());
    }

    @Test
    public void testClone() {
        Map<String, Object> map = new HashMap<>();
        map.put("testKey", "testData");
        Map<String, Object> clone = parametersUtils.clone(map);
        assertNotNull(clone);
        assertTrue(clone.containsKey("testKey"));
        assertTrue(clone.get("testKey").equals("testData"));
    }

    @Test
    public void testReplace() {
        String paramString = "testParamString";
        Object replace = parametersUtils.replace(paramString);
        assertNotNull(replace);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testInputKey", "testInputData");
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(workflowInput);
        assertTrue(workflowInput.containsKey("testInputKey"));
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "inputData");
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "123";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);
        assertNotNull(taskInput);
    }
}
