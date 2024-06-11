package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import java.util.*;

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
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        WorkflowModel workflowModel = Mockito.mock(WorkflowModel.class);
        TaskDef taskDef = Mockito.mock(TaskDef.class);
        Mockito.when(workflowModel.getWorkflowDefinition()).thenReturn(Mockito.mock(WorkflowDef.class));

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
        Assert.assertNotNull(result);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "value");

        String json = "{\"key\":\"value\"}";

        Assert.assertEquals(input, parametersUtils.replace(input, json));
    }

    @Test
    public void testReplaceString() {
        String paramString = "value";

        Assert.assertEquals(paramString, parametersUtils.replace(paramString));
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String,Object> inputParams = new HashMap<>();
        inputParams.put("WorkflowData", "data");
        WorkflowDef workflowDef = new WorkflowDef();
        Assert.assertSame(inputParams, parametersUtils.getWorkflowInput(workflowDef, inputParams));
    }

    // Add more test cases for other public methods of ParametersUtils class
}