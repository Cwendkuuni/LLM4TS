package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WorkflowModel workflowModel;

    @Mock
    private TaskDef taskDef;

    @Test
    public void getTaskInputV2Test() {
        String taskId = UUID.randomUUID().toString();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("inputKey", "inputValue");

        when(taskDef.getInputTemplate()).thenReturn(inputTemplate);

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);

        assertEquals("inputValue", result.get("inputKey"));
        assertEquals("testValue", result.get("testKey"));
    }

    @Test
    public void getWorkflowInputTest() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "inputValue");
        workflowDef.setInputTemplate(inputParams);
        Map<String, Object> taskInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("inputValue", taskInput.get("inputKey"));
    }

    // more test cases for every method in ParametersUtils
}