package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ParametersUtilsTest {
    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() throws IOException {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testParam", "ParamValue");
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        workflowModel.getWorkflowDefinition().setSchemaVersion(1);
        Task.TaskDefinition taskDef = new Task.TaskDefinition();
        String taskId = "1";

        given(objectMapper.writeValueAsBytes(inputParams)).willReturn(new byte[0]);
        given(objectMapper.readValue(new byte[0], new TypeReference<Map<String, Object>>(){}))
                .willReturn(inputParams);

        Map<String, Object> resultMap = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);
        
        assertThat(resultMap, is(inputParams));
    }

    // Add more tests as per your need for other methods.
}