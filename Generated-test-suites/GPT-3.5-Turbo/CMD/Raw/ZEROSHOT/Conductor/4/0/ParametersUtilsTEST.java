package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.mockito.Mock;
import java.util.HashMap;
import java.util.Map;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "123";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Add your assertions here
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "123";
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> taskInputV2 = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Add your assertions here
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = new Object();

        Map<String, Object> replacedInput = parametersUtils.replace(input, json);

        // Add your assertions here
    }

    // Add more test methods for other public methods in ParametersUtils class

}