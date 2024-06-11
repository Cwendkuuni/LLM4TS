package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.Optional;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;
    
    @Mock
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
    }

    @Test
    public void testRun() throws Exception {
        delegate.run(delegateExecution);
        verify(delegate, times(1)).run(delegateExecution);
    }

    @Test
    public void testExecute() {
        delegate.execute(delegateExecution);
        verify(delegate, times(1)).execute(delegateExecution);
    }

    @Test
    public void testIsDone() throws Exception {
        delegate.run(delegateExecution);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        delegate.run(delegateExecution);

        Object result = delegate.get();
        assertEquals(result, delegate.returnObject);

        delegate.done = true;
        delegate.thrown = new Exception("test");

        try {
            delegate.get();
        } catch (ExecutionException e) {
            assertEquals("java.util.concurrent.ExecutionException: java.lang.Exception: test", e.toString());
        }
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        delegate.run(delegateExecution);

        Object result = delegate.get(1L, TimeUnit.SECONDS);
        assertEquals(result, delegate.returnObject);

        delegate.done = true;
        delegate.thrown = new Exception("test");
        try {
            delegate.get(1L, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            assertEquals("java.util.concurrent.ExecutionException: java.lang.Exception: test", e.toString());
        }

        delegate.done = false;
        try {
            delegate.get(1L, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            assertEquals("java.util.concurrent.TimeoutException", e.toString());
        }
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.setId(delegate.targetTaskInstanceId);
        delegate.bindToFlowTaskInstance(instance);
        verify(delegate, times(1)).bindToFlowTaskInstance(instance);
    }
}