package com.netflix.conductor.core.utils;

import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock; 
import org.mockito.MockitoAnnotations;

import java.util.*;

public class ParametersUtilsTest {

    @Mock
    private ObjectMapper mockObjectMapper;

    private ParametersUtils parametersUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(mockObjectMapper);
    }

    @Test
    public void testGetTaskInput() {
        //initialize test data
        Map<String, Object> inputParams = new HashMap<>(); // populate the map with test data
        WorkflowModel workflow = mock(WorkflowModel.class); // populated with test data
        WorkflowDef workflowDef = mock(WorkflowDef.class); // populated with test data
        TaskDef taskDefinition = mock(TaskDef.class); // populated with test data
        String taskId = "1"; // set test data

        //run the method to be tested
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        //Verify the results
        assertNotNull(result);
        // Add more assertions based on expected results
    }

    // Repeat similar tests for other methods in ParametersUtils class
    // ....

    @Test(expected = RuntimeException.class) 
    public void testClone() throws Exception {
        //initialize test data
        Map<String, Object> inputParams = new HashMap<>(); // populate the map with test data

        //setup the mock to throw an exception
        when(mockObjectMapper.writeValueAsBytes(inputParams)).thenThrow(new IOException());

        //run the method to be tested
        parametersUtils.clone(inputParams);
    }
    
    // add more tests to cover various edge cases and exception handling scenarios
    //...
}
