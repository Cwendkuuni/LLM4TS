
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testInitCallable() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<Object> callable = baseRuntimeFlowableDelegate.initCallable(execution);
        Assert.assertNotNull(callable);
    }

    @Test(expected = ExecutionException.class)
    public void testGetThrowsException() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutThrowsException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.get(100, TimeUnit.MILLISECONDS);
    }
}
