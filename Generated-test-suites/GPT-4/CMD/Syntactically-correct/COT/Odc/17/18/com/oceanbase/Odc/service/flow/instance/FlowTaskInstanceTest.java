package com.oceanbase.odc.service.flow.instance;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FlowTaskInstanceTest {
    private FlowTaskInstance taskInstance;
    private ServiceTaskInstanceRepository mockRepo;
    private TaskService mockService;
    private EventPublisher mockPublisher;

    @Before
    public void setUp() {
        mockRepo = Mockito.mock(ServiceTaskInstanceRepository.class);
        mockService = Mockito.mock(TaskService.class);
        mockPublisher = Mockito.mock(EventPublisher.class);
        
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        taskInstance = new FlowTaskInstance(TaskType.SIMPLE, Long.valueOf(1), Long.valueOf(1),
                strategyConfig, true, true, 
                mockService, mockPublisher, mockRepo);
    }
    
    @Test
    public void testComplete() {
        // Testing complete() method
        try {
            taskInstance.complete(false);
        } catch (UnsupportedOperationException e) {
            fail("Should not reach here");
        }
    }

    @Test
    public void testAbort() {
        // Testing abort() method, should behave the same way as the complete() method
        try {
            taskInstance.abort();
        } catch (UnsupportedOperationException e) {
            fail("Should not reach here");
        }
    }
    
    @Test
    public void testCancel() {
        // Testing cancel(), should throw exception if isPresentOnThisMachine() returns false
        try {
            taskInstance.cancel(true);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Exception message should be 'Task isn't on this machine'", "Task isn't on this machine", e.getMessage());
        }
    }
    
    @Test
    public void testIsDone() {
        // Testing isDone(), should throw exception if isPresentOnThisMachine() returns false
        try {
            taskInstance.isDone();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Exception message should be 'Task isn't on this machine'", "Task isn't on this machine", e.getMessage());
        }
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Testing getResult(), should throw exception if isPresentOnThisMachine() returns false
        try {
            taskInstance.getResult();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Exception message should be 'Task isn't on this machine'", "Task isn't on this machine", e.getMessage());
        }
    }
    
    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Testing getResult(long timeout, TimeUnit unit), should throw exception if isPresentOnThisMachine() returns false
        try {
            taskInstance.getResult(1000, TimeUnit.MILLISECONDS);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Exception message should be 'Task isn't on this machine'", "Task isn't on this machine", e.getMessage());
        }
    }
}

