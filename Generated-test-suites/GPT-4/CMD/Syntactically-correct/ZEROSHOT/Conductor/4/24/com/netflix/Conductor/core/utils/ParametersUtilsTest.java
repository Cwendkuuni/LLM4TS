package com.netflix.conductor.core.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        String taskId = "testTaskId";
        
        WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
        Mockito.when(workflow.getWorkflowDefinition()).thenReturn(workflowDef);
        Mockito.when(workflowDef.getSchemaVersion()).thenReturn(2);
        
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReplaceStringParam() {
        String input = "${my_param_path}";
        Object replaced = parametersUtils.replace(input);
        assertEquals("", replaced.toString());
    }

    @Test
    public void testReplaceMapInput() {
        Map<String, Object> input = new HashMap<>();
        input.put("param1", "${my_param_path1}");
        input.put("param2", "${my_param_path2}");
        
        Map<String, Object> replaced = parametersUtils.replace(input, "{}");
        
        assertEquals("", replaced.get("param1"));
        assertEquals("", replaced.get("param2"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${my_param_path1}");
        inputParams.put("param2", "${my_param_path2}");
        
        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        
        assertEquals("${my_param_path1}", result.get("param1"));
        assertEquals("${my_param_path2}", result.get("param2"));
    }
}