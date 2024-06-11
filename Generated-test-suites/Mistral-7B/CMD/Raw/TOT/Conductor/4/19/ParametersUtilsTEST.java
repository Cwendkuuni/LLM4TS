package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private WorkflowModel workflowModel;
    private TaskDef taskDef;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
        objectMapper = mock(ObjectMapper.class);
        workflowModel = new WorkflowModel();
        taskDef = mock(TaskDef.class);
    }

    @Test
    public void testGetTaskInput() throws Exception {
        // Test case 1: Input params are null and workflow is null
        Map<String, Object> expected = Collections.emptyMap();
        when(objectMapper.readValue(anyString(), Map.class)).thenReturn(Collections.emptyMap());
        Map<String, Object> actual = parametersUtils.getTaskInput(null, null, null, null);
        assertEquals(expected, actual);

        // Test case 2: Input params and workflow are provided
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey1", "testValue1");
        workflowModel.setInput(new HashMap<>());
        workflowModel.getInput().put("testKey2", "testValue2");
        when(objectMapper.readValue(anyString(), Map.class))
                .then((invocation) -> invocation.getArgument(0));

        Map<String, Object> actual1 = parametersUtils.getTaskInput(inputParams, workflowModel, null, null);
        Map<String, Object> expected1 = new HashMap<>();
        expected1.put("testKey1", "testValue1");
        expected1.put("testKey2", "testValue2");
        assertEquals(expected1, actual1);

        // Test case 3: Input params are null, workflow is not null, and inputTemplate is provided
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("testKey3", "testValue3");
        workflowModel.setInput(null);
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate);

        Map<String, Object> actual2 = parametersUtils.getTaskInput(null, workflowModel, taskDef, null);
        Map<String, Object> expected2 = new HashMap<>();
        expected2.put("testKey3", "testValue3");
        assertEquals(expected2, actual2);

        // Test case 4: Input params are provided, workflow is null, and inputTemplate is not provided
        Map<String, Object> inputParams2 = new HashMap<>();
        inputParams2.put("testKey4", "testValue4");
        when(taskDef.getInputTemplate()).thenReturn(null);

        Map<String, Object> actual3 = parametersUtils.getTaskInput(inputParams2, null, taskDef, null);
        Map<String, Object> expected3 = new HashMap<>();
        expected3.put("testKey4", "testValue4");
        assertEquals(expected3, actual3);

        // Test case 5: Input params are provided, workflow is not null, and inputTemplate is provided
        Map<String, Object> inputParams3 = new HashMap<>();
        inputParams3.put("testKey5", "testValue5");
        Map<String, Object> inputTemplate2 = new LinkedHashMap<>();
        inputTemplate2.put("testKey5", "testValue5_template");
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate2);

        Map<String, Object> actual4 = parametersUtils.getTaskInput(inputParams3, workflowModel, taskDef, null);
        Map<String, Object> expected4 = new HashMap<>();
        expected4.put("testKey5", "testValue5_template");
        assertEquals(expected4, actual4);

        // Test case 6: Input params and workflow are provided, inputTemplate is not present in taskDef, and the key exists in input
        Map<String, Object> inputParams4 = new HashMap<>();
        inputParams4.put("testKey6", "testValue6");
        Map<String, Object> inputTemplate3 = new HashMap<>();
        when(taskDef.getInputTemplate()).thenReturn(null);

        Map<String, Object> actual5 = parametersUtils.getTaskInput(inputParams4, workflowModel, taskDef, null);
        Map<String, Object> expected5 = new HashMap<>();
        expected5.put("testKey6", "testValue6");
        assertEquals(expected5, actual5);

        // Test case 7: Input params and workflow are provided, inputTemplate is present in taskDef, and the key exists in input
        Map<String, Object> inputTemplate4 = new HashMap<>();
        inputTemplate4.put("testKey7", "testValue7_template");
        Map<String, Object> inputParams5 = new HashMap<>();
        inputParams5.put("testKey7", "testValue7");
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate4);

        Map<String, Object> actual6 = parametersUtils.getTaskInput(inputParams5, workflowModel, taskDef, null);
        Map<String, Object> expected6 = new HashMap<>();
        expected6.put("testKey7", "testValue7_template");
        assertEquals(expected6, actual6);

        // Test case 8: Input params and workflow are provided, inputTemplate is present in taskDef, and the key is not present in input
        Map<String, Object> inputTemplate5 = new HashMap<>();
        inputTemplate5.put("testKey8", "testValue8_template");
        Map<String, Object> inputParams6 = new HashMap<>();
        inputParams6.put("testKey6", "testValue6");
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate5);

        Map<String, Object> actual7 = parametersUtils.getTaskInput(inputParams6, workflowModel, taskDef, null);
        Map<String, Object> expected7 = new HashMap<>();
        assertEquals(expected7, actual7);

        // Test case 9: Input params and workflow are provided, inputTemplate is present in taskDef, and the key is not present in input, but is present in workflow
        Map<String, Object> inputParams7 = new HashMap<>();
        inputParams7.put("testKey9", "testValue9");
        Map<String, Object> inputTemplate6 = new HashMap<>();
        inputTemplate6.put("testKey9", "testValue9_template");
        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("testKey9", "testValue9_workflow");
        workflowModel.setInput(workflowInput);
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate6);

        Map<String, Object> actual8 = parametersUtils.getTaskInput(inputParams7, workflowModel, taskDef, null);
        Map<String, Object> expected8 = new HashMap<>();
        expected8.put("testKey9", "testValue9_template");
        assertEquals(expected8, actual8);

        // Test case 10: Input params and workflow are provided, inputTemplate is present in taskDef, and the key is not present in input or workflow
        Map<String, Object> inputParams8 = new HashMap<>();
        Map<String, Object> inputTemplate7 = new HashMap<>();
        inputTemplate7.put("testKey10", "testValue10_template");
        Map<String, Object> workflowInput2 = new HashMap<>();
        workflowInput2.put("testKey11", "testValue11_workflow");
        workflowModel.setInput(workflowInput2);
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate7);

        Map<String, Object> actual9 = parametersUtils.getTaskInput(inputParams8, workflowModel, taskDef, null);
        Map<String, Object> expected9 = new HashMap<>();
        assertEquals(expected9, actual9);

        // Test case 11: Input params and workflow are provided, inputTemplate is present in taskDef, and the key is present in input, workflow, and inputTemplate
        Map<String, Object> inputParams9 = new HashMap<>();
        inputParams9.put("testKey11", "testValue11");
        Map<String, Object> inputTemplate8 = new HashMap<>();
        inputTemplate8.put("testKey11", "testValue11_template");
        Map<String, Object> workflowInput3 = new HashMap<>();
        workflowInput3.put("testKey11", "testValue11_workflow");
        workflowModel.setInput(workflowInput3);
        when(taskDef.getInputTemplate()).thenReturn(inputTemplate8);

        Map<String, Object> actual10 = parametersUtils.getTaskInput(inputParams9, workflowModel, taskDef, null);
        Map<String, Object> expected10 = new HashMap<>();
        expected10.put("testKey11", "testValue11_template");
        assertEquals(expected10, actual10);
    }

    @Test
    public void testGetWorkflowInput() {
        // Test case 1: WorkflowDef is null
        WorkflowDef workflowDef = null;
        Map<String, Object> expected = new HashMap<>();
        Map<String, Object> actual = parametersUtils.getWorkflowInput(workflowDef, null);
        assertEquals(expected, actual);

        // Test case 2: WorkflowDef is provided
        WorkflowDef workflowDef2 = mock(WorkflowDef.class);
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("testKey12", "testValue12");
        when(workflowDef2.getInputTemplate()).thenReturn(inputTemplate);

        Map<String, Object> actual1 = parametersUtils.getWorkflowInput(workflowDef2, null);
        Map<String, Object> expected1 = new HashMap<>();
        expected1.put("testKey12", "testValue12");
        assertEquals(expected1, actual1);
    }
}