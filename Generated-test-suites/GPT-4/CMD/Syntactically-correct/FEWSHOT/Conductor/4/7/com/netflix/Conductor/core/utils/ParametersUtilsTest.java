package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

public class ParametersUtilsTest {
    private ParametersUtils utils;
    private ObjectMapper objectMapper = new ObjectMapper();
    private WorkflowModel workflow;
    private TaskDef taskDef;

    @Before
    public void setUp() {
        utils = new ParametersUtils(objectMapper);
        workflow = mock(WorkflowModel.class);
        taskDef = mock(TaskDef.class);
    }

    @Test
    public void testGetTaskInput_Version1() {
        when(workflow.getWorkflowDefinition()).thenReturn(new WorkflowDef());
        Map<String, Object> result = utils.getTaskInput(new HashMap<>(), workflow, taskDef, "testId");
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testGetTaskInput_Version2() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(2);
        when(workflow.getWorkflowDefinition()).thenReturn(workflowDef);
        Map<String, Object> result = utils.getTaskInput(new HashMap<>(), workflow, taskDef, "testId");
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testClone() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        assertEquals(map, utils.clone(map));
    }

    @Test
    public void testReplace_TaskIdNull() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        assertEquals(map, utils.replace(map, "jsonString"));
    }

    @Test
    public void testReplace_TaskIdNotNull() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "${value}");
        assertEquals(map, utils.replace(map, map));
    }

    @Test
    public void testReplace_EmptyParamString() {
        assertEquals("", utils.replace(""));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");
        workflowDef.setInputTemplate(inputTemplate);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "newValue");
        assertEquals(inputParams, utils.getWorkflowInput(workflowDef, inputParams));
    }
}