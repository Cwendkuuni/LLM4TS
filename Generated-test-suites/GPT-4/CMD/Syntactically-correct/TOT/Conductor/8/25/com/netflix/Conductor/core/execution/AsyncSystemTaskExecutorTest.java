package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest { 

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock 
    private ExecutionDAOFacade executionDAOFacade;
  
    @Mock 
    private QueueDAO queueDAO;
    
    @Mock 
    private MetadataDAO metadataDAO;
    
    @Mock 
    private WorkflowExecutor workflowExecutor;
    
    @Mock 
    private ConductorProperties conductorProperties;

    @Before 
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test 
    public void testExecute() {
        // Prepare testing data

        // Call tested method
        
        // Verify interactions and result
    }

    @Test 
    public void testExecute_errorCase() {
        // Prepare testing data

        // Call tested method

        // Verify interactions and result
    }

    private TaskModel mockTaskModel() {
        // Mock TaskModel object
    }
    
    private WorkflowModel mockWorkflowModel() {
        // Mock WorkflowModel object
    }

    private WorkflowSystemTask mockWorkflowSystemTask() {
        // Mock WorkflowSystemTask object
    }
    
    private void doMockBehavior() {
        // Set several behaviour to mock objects
    }
}
