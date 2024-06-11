
package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WorkflowModel workflow;

    @Mock
    private TaskDef taskDef;

    @Mock
    private TaskModel taskModel;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputSchemaVersion2() {
        when(workflow.getWorkflowDefinition()).thenReturn(workflow);
        when(workflow.getSchemaVersion()).thenReturn(2);
        Map<String, Object> result = parametersUtils.getTaskInput(new HashMap<>(), workflow, taskDef, "taskId");
        assertEquals(result, new HashMap<>());
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionInCloneMethod() throws Exception {
        when(objectMapper.writeValueAsBytes(any())).thenThrow(new Exception("Exception occurred"));
        parametersUtils.getTaskInput(new HashMap<>(), workflow, taskDef, "taskId");
    }

    @Test
    public void replaceNullInput() {
        Map<String, Object> result = parametersUtils.replace(null, "json");
        assertEquals(result, new HashMap<>());
    }

    @Test
    public void replaceNonNullInput() {
        Map<String, Object> result = parametersUtils.replace(Collections.singletonMap("key", "value"), "json");
        assertEquals(result, Collections.singletonMap("key", "value"));
    }

    @Test
    public void getWorkflowInput() {
        when(workflow.getInputTemplate()).thenReturn(Collections.singletonMap("key", "value"));
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflow, new HashMap<>());
        assertEquals(result, Collections.singletonMap("key", "value"));
    }

    @Test
    public void replaceVariables() {
        Object result = parametersUtils.replace("paramString");
        assertEquals(result, "paramString");
    }

    @Test
    public void testGetTaskInputV1() {
        when(workflow.getWorkflowDefinition()).thenReturn(workflow);
        when(workflow.getSchemaVersion()).thenReturn(1);
        Map<String, Object> params = new HashMap<>();
        params.put("key", "workflow.input.value");
        when(workflow.getInput()).thenReturn(Collections.singletonMap("value", "inputValue"));
        Map<String, Object> result = parametersUtils.getTaskInput(params, workflow, taskDef, "taskId");
        assertEquals(result, Collections.singletonMap("key", "inputValue"));
    }
}
