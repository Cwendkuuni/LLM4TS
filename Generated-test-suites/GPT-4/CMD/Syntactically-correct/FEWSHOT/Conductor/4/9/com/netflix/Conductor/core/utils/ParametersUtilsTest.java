package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.WorkflowModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
      }
   
    @Test
    public void testClone() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("testKey", "testValue");
    
        Map<String, Object> cloneMap = parametersUtils.clone(inputMap);
    
        assertNotNull(cloneMap);
        assertEquals(inputMap.size(), cloneMap.size());
        assertEquals(inputMap.get("testKey"), cloneMap.get("testKey"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("testInput", "${testJsonPath}");

        Object json = "{\"testJsonPath\":\"testValue\"}";

        Map<String, Object> resultMap = parametersUtils.replace(inputMap, json);

        assertNotNull(resultMap);
        assertEquals("testValue", resultMap.get("testInput"));
    }

    @Test
    public void testReplaceString() {
        String paramString = "${test.variable}";

        Object result = parametersUtils.replace(paramString);

        assertNotNull(result);
        assertEquals("", result.toString());
    }

    @Test
    public void testGetTaskInput() {
        TaskDef taskDefinition = Mockito.mock(TaskDef.class);
        WorkflowModel workflow = Mockito.mock(WorkflowModel.class);
        Mockito.when(workflow.getWorkflowDefinition())
          .thenReturn(new WorkflowDef());
        Mockito.when(workflow.getWorkflowDefinition().getSchemaVersion())
          .thenReturn(2);
        
        Map<String, Object> inputParams = Collections.emptyMap();
        String taskId = "testTaskId";

        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertNotNull(taskInput);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = Mockito.mock(WorkflowDef.class);
        Mockito.when(workflowDef.getInputTemplate())
          .thenReturn(Collections.singletonMap("testInput", "testValue"));

        Map<String, Object> inputParams = parametersUtils.getWorkflowInput(workflowDef, Collections.emptyMap());

        assertNotNull(inputParams);
        assertEquals("testValue", inputParams.get("testInput"));
    }
}