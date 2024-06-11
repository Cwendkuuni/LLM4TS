package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtilsTest;

    @Mock
    private ObjectMapper objectMapper;
  
    @Mock
    private WorkflowModel workflow;
    
    @Mock
    private TaskDef taskDefinition;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        parametersUtilsTest = new ParametersUtils(objectMapper);
    }
  
    @Test
    public void testGetTaskInput(){
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "1";
      
        Map<String, Object> result = parametersUtilsTest.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertNotNull(result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "1";
      
        Map<String, Object> result = parametersUtilsTest.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);
        assertNotNull(result);
    }

    @Test
    public void testReplaceWithMap() {
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> json = new HashMap<>();
      
        Map<String, Object> result = parametersUtilsTest.replace(inputParams, json);
        assertNotNull(result);
    }
  
    @Test
    public void testReplaceWithJsonString(){
        Map<String, Object> inputParams = new HashMap<>();
        String json = "{\"name\":\"James\", \"age\":32}";
       
        Map<String, Object> result = parametersUtilsTest.replace(inputParams, json);
        assertNotNull(result);
    }

    @Test
    public void testReplaceWithStringParam() {
        String paramString = "{\"name\":\"James\", \"age\":32}";

        Object result = parametersUtilsTest.replace(paramString);
        assertNotNull(result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams =  new HashMap<>();
      
        Map<String, Object> result = parametersUtilsTest.getWorkflowInput(workflowDef, inputParams);
        assertNotNull(result);
    }
}