package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

/** Used to parse and resolve the JSONPath bindings in the workflow and task definitions. */
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtils.class);

    @Test
    public void testGetTaskInput() {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

        // Test case 1: null workflow, null taskDefinition, null inputParams
        Map<String, Object> result = parametersUtils.getTaskInput(null, null, null, null);
        assertNull(result);

        // Test case 2: null workflow, null taskDefinition, non-null inputParams
        Map<String, Object> inputParams = new HashMap<>();
        result = parametersUtils.getTaskInput(inputParams, null, null, null);
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test case 3: null workflow, non-null taskDefinition, null inputParams
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        result = parametersUtils.getTaskInput(null, null, taskDefinition, null);
        assertNull(result);

        // Test case 4: null workflow, non-null taskDefinition, non-null inputParams
        inputParams = new HashMap<>();
        result = parametersUtils.getTaskInput(inputParams, null, taskDefinition, null);
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test case 5: non-null workflow, null taskDefinition, null inputParams
        WorkflowModel workflow = createWorkflowModel();
        result = parametersUtils.getTaskInput(null, workflow, null, null);
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test case 6: non-null workflow, null taskDefinition, non-null inputParams
        inputParams = new HashMap<>();
        result = parametersUtils.getTaskInput(inputParams, workflow, null, null);
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test case 7: non-null workflow, non-null taskDefinition, null inputParams
        taskDefinition = createTaskDef();
        result = parametersUtils.getTaskInput(null, workflow, taskDefinition, null);
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test case 8: non-null workflow, non-null taskDefinition, non-null inputParams
        inputParams = new HashMap<>();
        result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");
        assertEquals(1, result.size());

        // Test case 9: non-null workflow, non-null taskDefinition, non-null inputParams
        // with task input template
        WorkflowModel workflowWithInputTemplate = createWorkflowModelWithInputTemplate();
        Map<String, Object> expectedInput = new HashMap<>();
        expectedInput.put("foo", "bar");
        when(objectMapper.readValue(Mockito.anyBytes(), Map.class)).thenReturn(expectedInput);
        when(taskDefinition.getInputTemplate()).thenReturn(expectedInput);

        result = parametersUtils.getTaskInput(inputParams, workflowWithInputTemplate, taskDefinition, "taskId");
        assertEquals(2, result.size());
        assertEquals("bar", result.get("foo"));

        // Test case 10: non-null workflow, non-null taskDefinition, non-null inputParams
        // with workflow input template
        WorkflowDef workflowDef = createWorkflowDef();
        Map<String, Object> expectedWorkflowInput = new HashMap<>();
        expectedWorkflowInput.put("foo", "bar");
        when(objectMapper.readValue(Mockito.anyBytes(), Map.class)).thenReturn(expectedWorkflowInput);
        when(workflowWithInputTemplate.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getInputTemplate()).thenReturn(expectedWorkflowInput);

        result = parametersUtils.getTaskInput(inputParams, workflowWithInputTemplate, taskDefinition, "taskId");
        assertEquals(3, result.size());
        assertEquals("bar", result.get("foo"));
    }

    // Add other test cases for other methods in the ParametersUtils class

    private WorkflowModel createWorkflowModel() {
        // Initialize the workflow model with default values
        WorkflowModel workflow = new WorkflowModel();
        return workflow;
    }

    private TaskDef createTaskDef() {
        // Initialize the task definition with default values
        TaskDef taskDef = new TaskDef();
        return taskDef;
    }

    private WorkflowModel createWorkflowModelWithInputTemplate() {
        // Initialize the workflow model with an input template
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInputTemplate(new HashMap<>());
        return workflow;
    }

    private WorkflowDef createWorkflowDef() {
        // Initialize the workflow definition with an input template
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());
        return workflowDef;
    }
}