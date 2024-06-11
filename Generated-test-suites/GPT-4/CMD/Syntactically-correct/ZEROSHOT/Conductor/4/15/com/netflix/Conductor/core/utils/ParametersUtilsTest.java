package com.netflix.conductor.core.utils;

import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("sample_input_key","sample_input_value");
        WorkflowModel workflowModel = WorkflowModelFactory.create();
        TaskDef taskDef = TaskDefFactory.create();
        String taskId = "taskId";
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);
        assertTrue(taskInput.size() > 0);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        input.put("key","value");
        WorkflowModel workflowModel = WorkflowModelFactory.create();
        TaskDef taskDef = TaskDefFactory.create();
        String taskId = "taskId";
        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflowModel, taskId, taskDef);
        assertEquals(taskInputV2.get("key"), "value");
    }

    @Test
    public void testReplace() {

        Map<String, Object> input = new HashMap<>();
        input.put("key", "${my.var}");
        Map<String, Object> json = new HashMap<>();
        json.put("my.var", "my.value");

        Map<String, Object> result = parametersUtils.replace(input, json);
        assertEquals(result.get("key"), "my.value");
    }

    @Test
    public void testReplaceString(){
        String paramString = "${my.var}";
        Object replaced = parametersUtils.replace(paramString);
        assertEquals(replaced, paramString);
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflowModel = WorkflowModelFactory.create();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("sample_input_key","sample_input_value");
        Map<String, Object> taskInputV1 = parametersUtils.getTaskInputV1(workflowModel, inputParams);
        assertTrue(taskInputV1.size() > 0);
    }

    @Test
    public void testGetWorkflowInput(){
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("sample_input_key","sample_input_value");
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(workflowInput.get("sample_input_key"),"sample_input_value");
    }
}
