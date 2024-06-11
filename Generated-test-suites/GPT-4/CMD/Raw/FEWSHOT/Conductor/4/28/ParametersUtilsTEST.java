
package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ParametersUtilsTest {
    @Mock
    private ObjectMapper objectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void getTaskInputTest() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        when(workflow.getWorkflowDefinition()).thenReturn(workflowDef);
        when(workflowDef.getSchemaVersion()).thenReturn(2);

        TaskDef taskDefinition = mock(TaskDef.class);
        Map<String, Object> taskParams = parametersUtils.getTaskInput(new HashMap<String, Object>(), workflow, taskDefinition, "1");
        assertNotNull(taskParams);
    }

    @Test
    public void replaceTest() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "${workflow.input.json}");

        Map<String, Object> replaced = parametersUtils.replace(map, "{}");

        assertTrue(replaced.containsKey("key"));
        assertEquals("${workflow.input.json}", replaced.get("key"));
    }

    @Test
    public void replaceVariablesTest() {
        Object replaced = parametersUtils.replace("Hello ${name}");
        assertEquals("Hello ${name}", replaced);
    }

    @Test
    public void cloneTest() {
        Map<String, Object> input = Collections.singletonMap("key", "value");

        try {
            when(objectMapper.writeValueAsBytes(input)).thenReturn("value".getBytes());
            when(objectMapper.readValue("value".getBytes(), parametersUtils.map)).thenReturn(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> cloned = parametersUtils.clone(input);
        assertEquals(input, cloned);
    }

    @Test
    public void getWorkflowInputTest() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        Map<String, Object> workflowParams = parametersUtils.getWorkflowInput(workflowDef, new HashMap<String, Object>());
        assertNotNull(workflowParams);
    }
}
