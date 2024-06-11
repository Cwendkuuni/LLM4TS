
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Integer> delegate;
    private DelegateExecution delegateExecution;
    private Future<Integer> future;

    @Before
    public void setUp() {
        delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
        delegateExecution = Mockito.mock(DelegateExecution.class);
        future = Mockito.mock(Future.class);
    }

    @Test
    public void isDone() {
        when(delegate.isDone()).thenReturn(false);
        assertFalse(delegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void get_withExceptionThrown() throws ExecutionException, InterruptedException {
        when(delegate.get()).thenThrow(ExecutionException.class);
        delegate.get();
    }

    @Test
    public void get() throws ExecutionException, InterruptedException {
        when(delegate.get()).thenReturn(10);
        assertEquals((Integer) 10, delegate.get());
    }

    @Test(expected = TimeoutException.class)
    public void get_withTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        when(delegate.get(1, TimeUnit.SECONDS)).thenThrow(TimeoutException.class);
        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void get_withTimeoutAndReturnValue() throws ExecutionException, InterruptedException, TimeoutException {
        when(delegate.get(1, TimeUnit.SECONDS)).thenReturn(10);
        Integer result = delegate.get(1, TimeUnit.SECONDS);
        assertEquals((Integer) 10, result);
    }

    @Test(expected = ServiceTaskError.class)
    public void run_shouldThrowServiceTaskError() throws Exception {
        Mockito.doThrow(ServiceTaskError.class).when(delegate).run(delegateExecution);
        delegate.run(delegateExecution);
    }

    @Test
    public void run_noExceptionThrown() throws Exception {
        Mockito.doNothing().when(delegate).run(delegateExecution);
        delegate.run(delegateExecution);
        // If no exception thrown, it's a success.
    }

    @Test(expected = IllegalArgumentException.class)
    public void execute_shouldThrowIllegalArgumentException() {
        Mockito.doThrow(IllegalArgumentException.class).when(delegate).execute(delegateExecution);
        delegate.execute(delegateExecution);
    }

    @Test
    public void execute_noExceptionThrown() {
        Mockito.doNothing().when(delegate).execute(delegateExecution);
        delegate.execute(delegateExecution);
        // If no exception thrown, it's a success.
    }
}
