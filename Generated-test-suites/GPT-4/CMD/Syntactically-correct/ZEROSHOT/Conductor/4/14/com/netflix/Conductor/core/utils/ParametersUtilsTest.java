package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        String taskId = "taskId1";
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        String taskId = "taskId2";
        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        assertNotNull(result);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        Object json = "testJson";
        Map<String, Object> result = parametersUtils.replace(input, json);
        assertNotNull(result);
    }

    @Test
    public void testReplaceString() {
        String paramString = "${test.replacement}";
        Object result = parametersUtils.replace(paramString);
        assertNotNull(result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("testKey", "testValue");
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(inputParams, result);
    }
}
