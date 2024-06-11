package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParametersUtilsTest {
    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    
    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }
        
    @Test
    public void testGetTaskInput() {
         // initialize required data for test
        Map<String,Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        WorkflowModel workflowModel = new WorkflowModel(); 
        TaskDef taskDefinition = new TaskDef(); 
        String taskId = "task1";
        
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, taskId);
        
        // test assertions
        assertNotNull(taskInput);
    }

    @Test
    public void testGetTaskInputV2() {
        // initialize required data for test
        Map<String,Object> input = new HashMap<>();
        input.put("param1", "value1");
        WorkflowModel workflowModel = new WorkflowModel(); 
        TaskDef taskDefinition = new TaskDef(); 
        String taskId = "task1";
        
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(input, workflowModel, taskDefinition, taskId);
        
        // test assertions
        assertNotNull(taskInput);
    }

    @Test
    public void testReplaceVariables() {
        String param = "{ 'name': 'John', 'task': { 'description': 'testing' } }";
        
        Object replaced = parametersUtils.replace(param);
        
        // test assertions
        assertNotNull(replaced);
        assertEquals(param, replaced.toString());
    }

    @Test
    public void testReplace() {
        // initialize required data for test
        Map<String,Object> input = new HashMap<>();
        input.put("name", "${info.name}");
        String json = "{ 'info': { 'name': 'John' } }";

        Map<String, Object> replaced = parametersUtils.replace(input, json);
        
        // test assertions
        assertNotNull(replaced);
        assertEquals("John", replaced.get("name"));
    }
}