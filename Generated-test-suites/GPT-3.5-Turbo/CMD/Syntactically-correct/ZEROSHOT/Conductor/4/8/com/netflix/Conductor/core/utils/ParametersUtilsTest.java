package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper mockObjectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        parametersUtils = new ParametersUtils(mockObjectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "12345";

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Add your assertions here
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "12345";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Add your assertions here
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = new Object();

        Map<String, Object> result = parametersUtils.replace(input, json);

        // Add your assertions here
    }

    @Test
    public void testReplaceString() {
        String paramString = "testString";

        Object result = parametersUtils.replace(paramString);

        // Add your assertions here
    }

    // Add more test methods for other public methods in ParametersUtils

}