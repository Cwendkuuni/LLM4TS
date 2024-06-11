
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    private BaseRuntimeFlowableDelegate<String> runtimeFlowableDelegate;

    @Before
    public void setUp() {
        runtimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
    }

    @Test
    public void testIsDone() {
        assertFalse(runtimeFlowableDelegate.isDone());
        runtimeFlowableDelegate.run(execution);
        assertTrue(runtimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        runtimeFlowableDelegate.run(execution);
        assertEquals("Test", runtimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        runtimeFlowableDelegate.run(execution);
        assertEquals("Test", runtimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGetException() throws InterruptedException, ExecutionException {
        runtimeFlowableDelegate.run(execution);
        runtimeFlowableDelegate.get();
    }
}

