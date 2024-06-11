package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @InjectMocks
    ParametersUtils parametersUtils;

    @Mock
    ObjectMapper objectMapper;

    @Test
    public void testGetTaskInput() {
        // Add setup and expected inputs and outputs...

        // Perform operation
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        // Check results
    }

    @Test
    public void testGetTaskInputV2() {
        // Add setup and expected inputs and outputs...

        // Perform operation
        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);

        // Check results
    }

    @Test
    public void testClone() {
        // Add setup and expected inputs and outputs...

        // Perform operation
        Map<String, Object> result = parametersUtils.clone(inputTemplate);

        // Check results
    }

    @Test
    public void testReplace_WithMapInput() {
        // Add setup and expected inputs and outputs...

        // Perform operation
        Map<String, Object> result = parametersUtils.replace(input, json);

        // Check results
    }

    @Test
    public void testReplace_WithStringInput() {
        // Add setup and expected inputs and outputs...

        // Perform operation
        Object result = parametersUtils.replace(paramString);

        // Check results
    }

    @Test
    public void testGetTaskInputV1() {
        // Add setup and expected inputs and outputs...

        // Perform operation
        Map<String, Object> result = parametersUtils.getTaskInputV1(workflow, inputParams);

        // Check results
    }

    @Test
    public void testGetWorkflowInput() {
        // Add setup and expected inputs and outputs...

        // Perform operation
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        // Check results
    }

    // Add more test methods here...
}