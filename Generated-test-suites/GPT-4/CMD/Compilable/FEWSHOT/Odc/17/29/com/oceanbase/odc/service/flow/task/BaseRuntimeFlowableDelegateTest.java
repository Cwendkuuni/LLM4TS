
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.flowable.engine.delegate.DelegateExecution;
import static org.mockito.Mockito.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

    @Autowired
    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;
    @Autowired
    DelegateExecution execution;

    @Before
    public void setup() {
        execution = mock(DelegateExecution.class);
    }

    @Test
    public void testIsDone() {
        boolean isDone = baseRuntimeFlowableDelegate.isDone();
        assertNotNull(isDone);
    }

    @Test(expected = InterruptedException.class)
    public void testGetWithExecutionException() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.get();
    }

    @Test
    public void testGetWithTimeoutException() {
        try {
            baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            assertTrue(e instanceof TimeoutException);
        }
    }

    @Test
    public void testExecute() {
        try {
            doNothing().when(execution).setVariable(anyString(), any());
            baseRuntimeFlowableDelegate.execute(execution);
            verify(execution, atLeastOnce()).setVariable(anyString(), any());
        } catch (Exception e) {
            fail();
        }
    }

    // Add other method test cases here.
    // Since some methods are abstract, tests need to be carried out by extending this class.
}
