
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());

        TaskDef taskDefinition = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${workflow.input.param1}");
        inputParams.put("param2", "${workflow.output.param2}");
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");

        assertNotNull(taskInput);
        assertEquals(2, taskInput.size());
        assertEquals("value1", taskInput.get("param1"));
        assertEquals("value2", taskInput.get("param2"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "value2");
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals(2, workflowInput.size());
        assertEquals("value1", workflowInput.get("param1"));
        assertEquals("value2", workflowInput.get("param2"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("param1", "${workflow.input.param1}");
        input.put("param2", "${workflow.output.param2}");
        Map<String, Object> replacedParams = parametersUtils.replace(input, "{}");

        assertNotNull(replacedParams);
        assertEquals(2, replacedParams.size());
        assertEquals("", replacedParams.get("param1"));
        assertEquals("", replacedParams.get("param2"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.param1}";
        Object replacedValue = parametersUtils.replaceVariables(paramString, "{}", "taskId");

        assertNotNull(replacedValue);
        assertEquals("", replacedValue.toString());
    }

    @Test
    public void testReplaceVariablesSystemParams() {
        String paramString = "${system:envVar}";
        Object replacedValue = parametersUtils.replaceVariables(paramString, "{}", "taskId");

        assertNotNull(replacedValue);
        assertEquals("envVarValue", replacedValue.toString());
    }
}
