
package com.netflix.conductor.core.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {

    private ObjectMapper objectMapper;
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key1", "${var1}");
        input.put("key2", "${var2}");

        Map<String, Object> json = new HashMap<>();
        json.put("var1", "value1");
        json.put("var2", "value2");

        Map<String, Object> result = parametersUtils.replace(input, json);

        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
    }

    @Test
    public void testReplaceString() {
        String result = (String) parametersUtils.replace("${env}");
        assertEquals("", result);

        result = (String) parametersUtils.replace("No replacements here");
        assertEquals("No replacements here", result);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("name", "${workflow.input.name}");

        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        when(workflow.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(2);

        TaskDef taskDef = new TaskDef();
        taskDef.setName("Task1");

        String taskId = "1";

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDef, taskId);

        assertNotNull(result);
    }

    // Add more tests for other methods here
}
