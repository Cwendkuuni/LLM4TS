package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(new HashMap<>());
        workflow.setOutput(new HashMap<>());
        workflow.setStatus("COMPLETED");
        workflow.setWorkflowId("123");
        workflow.setWorkflowName("TestWorkflow");
        workflow.setWorkflowVersion(1);
        workflow.setCorrelationId("corrId");
        workflow.setReasonForIncompletion("reason");
        workflow.getVariables().put("var1", "value1");

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setName("TestTask");
        taskDefinition.getInputTemplate().put("inputParam1", "data1");

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${workflow.input.test}");
        inputParams.put("param2", "${workflow.output.test}");
        inputParams.put("param3", "${workflow.status}");
        inputParams.put("param4", "${workflow.workflowId}");
        inputParams.put("param5", "${workflow.workflowName}");
        inputParams.put("param6", "${workflow.workflowVersion}");
        inputParams.put("param7", "${workflow.correlationId}");
        inputParams.put("param8", "${workflow.reasonForIncompletion}");
        inputParams.put("param9", "${workflow.variables.var1}");

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, "12345", taskDefinition);

        assertEquals("data1", result.get("param1"));
        assertEquals(null, result.get("param2"));
        assertEquals("COMPLETED", result.get("param3"));
        assertEquals("123", result.get("param4"));
        assertEquals("TestWorkflow", result.get("param5"));
        assertEquals(1, result.get("param6"));
        assertEquals("corrId", result.get("param7"));
        assertEquals("reason", result.get("param8"));
        assertEquals("value1", result.get("param9"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${workflow.input.test}");

        Map<String, Object> json = new HashMap<>();
        json.put("workflow", Map.of("input", Map.of("test", "replaced")));

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertEquals("replaced", result.get("key"));
    }

    @Test
    public void testReplaceVariables() {
        String inputString = "${workflow.input.test}";
        Map<String, Object> json = new HashMap<>();
        json.put("workflow", Map.of("input", Map.of("test", "replaced")));

        Object result = parametersUtils.replaceVariables(inputString, json);

        assertEquals("replaced", result);
    }

}