package com.netflix.conductor.core.utils;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInputV2() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId("workflow_id");

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setName("task_definition");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "${workflow.input.param2}");

        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflow, "task_id", taskDefinition);

        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("param1"));
        assertEquals("${workflow.input.param2}", taskInput.get("param2"));
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        String inputString = "This is a ${workflow.input.param1} input string";
        DocumentContext documentContext = JsonPath.parse("{\"workflow\": {\"input\": {\"param1\": \"replacement_value\"}}}");

        Object replacedValue = parametersUtils.replaceVariables(inputString, documentContext, null);

        assertEquals("This is a replacement_value input string", replacedValue);
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());

        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setName("workflow_definition");
        workflowDef.setInputTemplate(Map.of("param1", "value1", "param2", "value2"));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");

        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(workflowInput);
        assertEquals("value1", workflowInput.get("param1"));
        assertEquals("value2", workflowInput.get("param2"));
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(new ObjectMapper());
        Map<String, Object> input = Map.of("param1", "${workflow.input.param1}", "param2", "${workflow.notExist.param2}");
        Map<String, Object> json = Map.of("workflow", Map.of("input", Map.of("param1", "value1")));

        Map<String, Object> replacedMap = parametersUtils.replace(input, json);

        assertNotNull(replacedMap);
        assertEquals("value1", replacedMap.get("param1"));
        assertNull(replacedMap.get("param2"));
    }
}