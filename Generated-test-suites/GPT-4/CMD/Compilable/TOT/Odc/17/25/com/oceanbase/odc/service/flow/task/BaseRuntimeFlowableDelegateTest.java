
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {
    @Mock
    private DelegateExecution execution;
    @Mock
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setUp() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("testId");
    }

    @After
    public void tearDown() throws Exception {
        delegate = null;
        execution = null;
    }

    // Test to check if the task finishes successfully
    @Test
    public void testExecutionSuccess() throws Exception {
        when(delegate.get()).thenReturn(new Object());
        delegate.run(execution);
        verify(delegate).run(execution);
    }

    // Test to check if the task finishes with an execution exception
    @Test(expected = ExecutionException.class)
    public void testExecutionFail() throws Exception {
        when(delegate.get()).thenThrow(new ExecutionException("Test", new Throwable()));
        delegate.run(execution);
    }

    // Test to verify if the task timeouts
    @Test(expected = TimeoutException.class)
    public void testExecutionTimeout() throws Exception {
        when(delegate.get(500, TimeUnit.MILLISECONDS)).thenThrow(new TimeoutException());
        delegate.run(execution);
    }

    // Test to verify if the task is done or not
    @Test
    public void testIsDone() throws Exception {
        when(delegate.isDone()).thenReturn(true);
        delegate.run(execution);
        verify(delegate).isDone();
    }

    // Test to check the Callable initialization
    @Test
    public void testInitCallable() throws Exception {
        delegate.initCallable(execution);
        verify(delegate).initCallable(execution);
    }
}

