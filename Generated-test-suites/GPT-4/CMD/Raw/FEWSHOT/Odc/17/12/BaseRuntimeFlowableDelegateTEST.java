
package com.oceanbase.odc.service.flow.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setup() throws Exception {
        baseRuntimeFlowableDelegate = new MockBaseRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("123");
        baseRuntimeFlowableDelegate.run(execution);
    }

    @Test
    public void testGetActivityId() {
        assertEquals("123", baseRuntimeFlowableDelegate.getActivityId());
    }

    @Test
    public void testIsDone() {
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected=ExecutionException.class)
    public void testGetException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("123");
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        long start = System.currentTimeMillis();
        String result = baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS);
        long elapsed = System.currentTimeMillis() - start;
        assertEquals("Test", result);
        assertTrue(elapsed < 1000);
    }

    @Test(expected=ExecutionException.class)
    public void testGetExceptionTimeout() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("123");
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS);
    }

    private static class MockBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test";
        }
    }
}
