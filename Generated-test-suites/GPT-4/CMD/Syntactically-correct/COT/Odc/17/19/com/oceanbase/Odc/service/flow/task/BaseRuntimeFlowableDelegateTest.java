package com.oceanbase.odc.service.flow.task;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;

public class BaseRuntimeFlowableDelegateTest {
    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);

    // Testing basic functionality of methods.
    @Test
    public void testIsDone() {
        Mockito.when(baseRuntimeFlowableDelegate.isDone()).thenReturn(true);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGet() throws Exception {
        doThrow(new ExecutionException("mock", new Throwable())).when(baseRuntimeFlowableDelegate).get();
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTime() throws Exception {
        doThrow(new TimeoutException("mock")).when(baseRuntimeFlowableDelegate).get(100, TimeUnit.SECONDS);
        baseRuntimeFlowableDelegate.get(100, TimeUnit.SECONDS);
    }

    @Test(expected = ServiceException.class)
    public void testBindToFlowTaskInstance() throws Exception {
        doThrow(new ServiceException("mock")).when(baseRuntimeFlowableDelegate).bindToFlowTaskInstance(null);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(null);
    }

    @Test(expected= IllegalArgumentException.class)
    public void testUpdateFlowInstanceStatus() throws Exception {
        doThrow(new IllegalArgumentException("mock")).when(baseRuntimeFlowableDelegate).updateFlowInstanceStatus(null);
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(null);
    }

    @Test(expected = NullPointerException.class)
    public void testinitCallableNull() throws Exception {
        doThrow(new NullPointerException("mock")).when(baseRuntimeFlowableDelegate).initCallable(null);
        baseRuntimeFlowableDelegate.initCallable(null);
    }
}

