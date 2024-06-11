package com.netflix.conductor.core.utils;
import static org.junit.Assert.*;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ParametersUtilsTest {

    @Mock
    ObjectMapper objectMapper;

    @Test
    public void testParametersUtilsConstructor() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        assertNotNull(parametersUtils);
    }

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = null;
        TaskDef taskDefinition = null;
        String taskId = "1";
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(result);
    }

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = null;
        String taskId = "1";
        TaskDef taskDefinition = null;
        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, taskId, taskDefinition);
        assertNotNull(result);
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        Map<String, Object> input = new HashMap<>();
        Object json = new HashMap<>();
        Map<String, Object> result = parametersUtils.replace(input, json);
        assertNotNull(result);
    }

    @Test
    public void testReplaceString() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        String paramString = "test string";
        Object result = parametersUtils.replace(paramString);
        assertNotNull(result);
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);
        WorkflowDef workflowDef = null;
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(result);
    }
}