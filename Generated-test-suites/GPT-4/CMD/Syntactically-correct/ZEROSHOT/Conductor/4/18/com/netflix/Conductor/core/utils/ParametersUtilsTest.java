package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflowParam", "${workflow.input.workflowParam}");

        when(workflow.getInput()).thenReturn(Collections.singletonMap("workflowParam", "workflowValue"));

        Map<String, Object> taskInput = parametersUtils.getTaskInputV1(workflow, inputParams);

        assertEquals("workflowValue", taskInput.get("workflowParam"));
    }

    @Test
    public void testReplace() {
        DocumentContext documentContext = mock(DocumentContext.class);
        Map<String, Object> input = new HashMap<>();
        input.put("paramKey", "${paramKey}");

        when(documentContext.read("paramKey")).thenReturn("paramValue");

        Map<String, Object> replaced = parametersUtils.replace(input, documentContext);

        assertEquals("paramValue", replaced.get("paramKey"));
    }

    @Test
    public void testReplaceString() {
        String paramString = "${var1}";

        Object replaced = parametersUtils.replace(paramString);

        assertEquals("${var1}", replaced.toString());  // No input is available to replace 'var1'
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        when(workflowDef.getInputTemplate()).thenReturn(null);

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("testValue", workflowInput.get("testKey"));
    }
}
