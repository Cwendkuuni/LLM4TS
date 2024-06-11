package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setup() {
        delegate = spy(BaseRuntimeFlowableDelegate.class);
        delegate.init(null);
    }

    @Test
    public void testRunSuccess() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        doNothing().when(delegate).run(execution);
        delegate.execute(execution);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testRunException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        Exception e = new Exception("Test Exception");
        doThrow(e).when(delegate).run(execution);
        try {
            delegate.execute(execution);
        } catch (Exception ex) {
            assertEquals(e, ex);
        }
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.execute(null);
        assertEquals(null, delegate.get());
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.execute(null);
        try {
            delegate.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            assertEquals(null, delegate.get());
        }
    }

    @Test
    public void testPreHandle() {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.preHandle(execution);
        // add assertions here
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);
        // add assertions here
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.IN_PROGRESS);
        // add assertions here
    }

    @Test
    public void testInitCallable() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = delegate.initCallable(execution);
        // add assertions here
    }
}