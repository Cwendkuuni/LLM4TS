
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution executionMock;

    @Before
    public void setUp() {
        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        executionMock = mock(DelegateExecution.class);
        when(executionMock.getCurrentActivityId()).thenReturn("activityId");
    }

    @Test
    public void testGetBeforeExecutionCompletesShouldBlock() throws InterruptedException, ExecutionException {
        new Thread(() -> {
            try {
                delegate.run(executionMock);
            } catch (Exception e) {
                // Ignore
            }
        }).start();

        assertEquals("Test", delegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetAfterExecutionThrowsExceptionShouldThrowExecutionException() throws InterruptedException, ExecutionException {
        new Thread(() -> {
            try {
                throw new ServiceTaskError(new Exception());
            } catch (ServiceTaskError e) {
                // Ignore
            }
        }).start();

        delegate.get();
    }

    @Test
    public void testIsDoneAfterExecutionCompletesShouldReturnTrue() throws InterruptedException, ExecutionException {
        delegate.run(executionMock);
        assertEquals(true, delegate.isDone());
    }
    
}
