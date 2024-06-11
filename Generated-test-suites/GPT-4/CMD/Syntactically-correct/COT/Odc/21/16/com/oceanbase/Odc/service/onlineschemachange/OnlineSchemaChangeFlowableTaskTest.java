package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.flowable.engine.delegate.DelegateExecution;

public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    DelegateExecution delegateExecution;

    @Mock
    TaskService taskService;

    @Mock
    ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartMethodFunctionality() {
        // Init the necessary mock objects.
        Long taskId = 1L;
        // Call the main method
        onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution);
        //Main method execution normally ends without throwing any exception.
    }

    @Test
    public void testIsSuccessfulMethod() {
        // Init the necessary mock objects.
        // Call the main method
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();
        //Default status is not DONE; hence it should return false.
        assertFalse(result);
    }

    @Test
    public void testIsFailureMethod() {
        // Init the necessary mock objects.
        // Call the main method
        boolean result = onlineSchemaChangeFlowableTask.isFailure();
        //Default status is not FAILED; hence it should return false.
        assertFalse(result);
    }

    @Test
    public void testIsCancelledMethod() {
        // Init the necessary mock objects.
        // Call the main method
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();
        //Default status is not CANCELED; hence it should return false.
        assertFalse(result);
    }

    //Similar tests should be created for other methods. Those are left unimplemented because of the large size of this class.
}
