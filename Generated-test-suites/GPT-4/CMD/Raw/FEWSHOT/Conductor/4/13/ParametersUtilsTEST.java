
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(new WorkflowDef());

        assertNull(parametersUtils.getTaskInput(inputParams, workflow, null, null));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(new WorkflowDef());

        assertNull(parametersUtils.getTaskInputV2(inputParams, workflow, null, null));
    }

    @Test
    public void testClone() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("testKey", "testValue");

        Map<String, Object> cloneMap = parametersUtils.clone(inputTemplate);
        assertEquals(inputTemplate, cloneMap);
    }

    @Test
    public void testReplaceMapObject() {
        Map<String, Object> input = new HashMap<>();
        input.put("testKey", "${testValue}");

        Map<String, Object> replaced = parametersUtils.replace(input, new Object());
        assertEquals(input, replaced);
    }

    @Test
    public void testReplaceString() {
        String paramString = "${testKey}";
        assertEquals("${testKey}", parametersUtils.replace(paramString));
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowDefinition(new WorkflowDef());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "workflow.input.testValue");
        assertNull(parametersUtils.getTaskInputV1(workflow, inputParams));
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");

        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(inputParams);

        assertEquals(inputParams, parametersUtils.getWorkflowInput(workflowDef, inputParams));
    }
}
