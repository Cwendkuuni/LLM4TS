package com.netflix.conductor.core.execution;
import com.netflix.conductor.core.execution.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private WorkflowSystemTask systemTask;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @After
    public void tearDown() {
        // any clean up code if required
    }

    @Test
    public void testExecute_TaskAlreadyCompleted() {
        //Mocks and test case code
    }

    @Test
    public void testExecute_TaskNotScheduled() {
        //Mocks and test case code
    }

    @Test
    public void testExecute_ConcurrentExecutionLimitExceeded() {
        //Mocks and test case code
    }

    @Test
    public void testExecute_RateLimitExceeded() {
        //Mocks and test case code
    }

    @Test
    public void testExecute_InProgressTask() {
        //Mocks and test case code
    }
    
    @Test
    public void testExecute_FWStatusTerminal_Case() {
         //Mocks and test case code
    }

    @Test
    public void testExecute_NonExistentTaskId() {
        //Mocks and test case code
    }

    @Test(expected = Exception.class)
    public void testExecute_Exception() {
        // Code to instantiate executor
        doThrow(new Exception()).when(executionDAOFacade).getTaskModel(any(String.class));
        asyncSystemTaskExecutor.execute(systemTask, "taskId");
        verify(executionDAOFacade).getTaskModel("taskId");

    }
}