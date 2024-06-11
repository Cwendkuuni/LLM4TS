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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(2);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertNotNull(result);
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testReplaceVariables() {
        String inputString = "Hello, ${name}!";
        DocumentContext jsonContext = JsonPath.parse("{\"name\": \"John Doe\"}");

        String outputString = (String) parametersUtils.replaceVariables(inputString, jsonContext, null);

        assertNotNull(outputString);
        assertEquals("Hello, John Doe!", outputString);
    }

    @Test
    public void testClone() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("key", "value");

        Map<String, Object> clonedMap = parametersUtils.clone(inputMap);

        assertNotNull(clonedMap);
        assertEquals("value", clonedMap.get("key"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("key", "${replacement}");
        DocumentContext jsonContext = JsonPath.parse("{\"replacement\": \"value\"}");

        Map<String, Object> replacedMap = parametersUtils.replace(inputMap, jsonContext);

        assertNotNull(replacedMap);
        assertEquals("value", replacedMap.get("key"));
    }

    // ...more test methods here...
}
