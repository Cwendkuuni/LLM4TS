package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;

import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper mockObjectMapper;

    @InjectMocks
    private ParametersUtils parametersUtilsUnderTest;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>(); 
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task_1";
        
        // Mock ObjectMapper
        when(mockObjectMapper.readValue(any(), any())).thenReturn(new HashMap<>());

        Map<String, Object> result = parametersUtilsUnderTest.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertEquals("Expecting a map size of 0", 0, result.size());
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>(); 
        WorkflowModel workflow = new WorkflowModel();
        String taskId = "task_1";
        TaskDef taskDefinition = new TaskDef();

        // Mock ObjectMapper
        when(mockObjectMapper.readValue(any(), any())).thenReturn(new HashMap<>());

        Map<String, Object> result = parametersUtilsUnderTest.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

        assertEquals("Expecting a map size of 0", 0, result.size());
    }

    //... add more methods here
}