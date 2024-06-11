package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "workflow.input.inputValue");

        when(workflowModel.getInput()).thenReturn(inputParams);

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertEquals("Expected successful retrieval of task input V1", inputParams, result);
    }

    @Test
    public void testReplace() {
        //TODO: This method requires valid JSON string to work correctly. Test case should be provided real world parameters and JSON string.
        //Similar methods should be tested likewise.
    }

    @Test
    public void testReplaceVariables() {
        //TODO: This method replaces variable in a given string with matching variable values from a given json path Document Context. To test the method a valid json path Document Context and string should be provided.
    }

    @Test
    public void testReplaceList() {
        //TODO: This method is similar to the replace method but it operates on a list, and it should be tested with real parameters similar to the replace method.
    }

    @Test
    public void testClone() {
        Map<String, Object> map = new HashMap<>();
        map.put("Key", "Value");

        //TODO: Add the necessary mocking behavior and the necessary assertions.
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "workflow.input.inputValue");

        when(workflowDef.getInputTemplate()).thenReturn(inputParams);

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("Expected successful retrieval of workflow input", inputParams, result);
    }
}