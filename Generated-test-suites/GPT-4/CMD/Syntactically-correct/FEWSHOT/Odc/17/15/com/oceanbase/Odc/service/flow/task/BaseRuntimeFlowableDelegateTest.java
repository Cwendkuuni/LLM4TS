package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Integer> delegate;
    private Callable<Integer> callable;
    private DelegateExecution execution;

    @Before
    public void setUp() {
        delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
        callable = () -> 1;
        Mockito.when(delegate.initCallable(Mockito.any())).thenReturn(callable);
        execution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testIsDone() throws Exception {
        delegate.run(execution);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        delegate.run(execution);
        assertEquals(1, (int) delegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws Exception {
        Mockito.when(delegate.initCallable(Mockito.any())).thenThrow(new RuntimeException());
        delegate.run(execution);
        delegate.get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        delegate.run(execution);
        assertEquals(1, (int) delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws Exception {
        delegate.get(1, TimeUnit.NANOSECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeoutAndException() throws Exception {
        Mockito.when(delegate.initCallable(Mockito.any())).thenThrow(new RuntimeException());
        delegate.run(execution);
        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.ACTIVE);
        Mockito.verify(delegate, Mockito.times(1)).updateFlowInstanceStatus(Mockito.any());
    }

    @Test
    public void testExecute() {
        delegate.execute(execution);
        Mockito.verify(delegate, Mockito.times(1)).execute(Mockito.any());
    }

    @Test
    public void testRun() throws Exception {
        delegate.run(execution);
        Mockito.verify(delegate, Mockito.times(1)).run(Mockito.any());
    }
    
    @Test(expected = RuntimeException.class)
    public void testRunException() throws Exception {
        Mockito.when(delegate.initCallable(Mockito.any())).thenThrow(new RuntimeException());
        delegate.run(execution);
    }
}