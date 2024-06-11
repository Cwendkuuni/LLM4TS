package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
    }

    @Test
    public void testIsDone() {
        assertEquals(false, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));
        assertEquals("Test", baseRuntimeFlowableDelegate.get(1, java.util.concurrent.TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws ExecutionException, InterruptedException {
        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));
        baseRuntimeFlowableDelegate.throwException(new RuntimeException("Test Exception"));
        baseRuntimeFlowableDelegate.get();
    }

}