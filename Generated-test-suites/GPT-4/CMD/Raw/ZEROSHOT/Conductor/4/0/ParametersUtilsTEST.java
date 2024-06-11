
package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {
    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput_ConfigurationNull_Success() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        HashMap<String, Object> inputTemplate = new HashMap<>();
        taskDefinition.setInputTemplate(inputTemplate);
        String taskId = "testId";
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertEquals(inputParams, result);
    }

    @Test
    public void testReplace_NoMatches_Success() {
        Map<String, Object> input = new HashMap<>();
        String json = "{}";
        Map<String, Object> result = parametersUtils.replace(input, json);
        assertEquals(input, result);
    }

    @Test
    public void testReplace_String_Success() {
        String paramString = "no variable here";
        Object result = parametersUtils.replace(paramString);
        assertEquals(paramString, result);
    }

    @Test
    public void testGetTaskInputV1_Success() {
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("variable1", "workflow.input.var1");

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("var1", "value1");
        when(workflow.getInput()).thenReturn(workflowInput);

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflow, inputParams);
        assertEquals("value1", result.get("variable1"));
    }

    @Test
    public void testGetWorkflowInput_NullInputParams_Success() {
        Map<String, Object> workflowDefInputTemplate = new HashMap<>();
        workflowDefInputTemplate.put("key1", "value1");

        Map<String, Object> workflowDefInputParms = new HashMap<>();
        workflowDefInputParms.putAll(workflowDefInputTemplate);

        WorkflowModel.Definition workflowDef = new WorkflowModel.Definition();
        workflowDef.setInputTemplate(workflowDefInputParms);

        Map<String, Object> inputParams = null;
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertNull(result);
    }
}
