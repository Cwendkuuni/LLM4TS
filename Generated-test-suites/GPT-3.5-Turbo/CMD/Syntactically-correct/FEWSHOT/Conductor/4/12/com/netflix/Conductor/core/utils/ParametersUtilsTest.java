package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    @Test
    public void testGetTaskInput() {
        ParametersUtils parametersUtils = new ParametersUtils(null);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(Collections.singletonMap("input1", "value1"));
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("input2", "value2"));
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("input3", "value3");
        
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDef, "taskId");
        
        assertNotNull(taskInput);
        assertEquals("value1", taskInput.get("workflow.input"));
        assertEquals("value2", taskInput.get("input2"));
        assertEquals("value3", taskInput.get("input3"));
    }

    @Test
    public void testGetWorkflowInput() {
        ParametersUtils parametersUtils = new ParametersUtils(null);
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("input1", "value1"));
        
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("input2", "value2");
        
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        
        assertNotNull(workflowInput);
        assertEquals("value1", workflowInput.get("input1"));
        assertEquals("value2", workflowInput.get("input2"));
    }

    @Test
    public void testReplace() {
        ParametersUtils parametersUtils = new ParametersUtils(null);
        Map<String, Object> input = Collections.singletonMap("key", "${value}");
        Object json = "{\"value\": \"replaced\"}";

        Map<String, Object> replaced = parametersUtils.replace(input, json);

        assertNotNull(replaced);
        assertEquals("replaced", replaced.get("key"));
    }

    @Test
    public void testReplaceVariables() {
        ParametersUtils parametersUtils = new ParametersUtils(null);
        String paramString = "${workflow.input.key}";
        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("key", "replaced"));
        String taskId = "taskId";

        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertNotNull(replaced);
        assertEquals("replaced", replaced);
    }

}