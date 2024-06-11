package com.oceanbase.odc.service.flow.task;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.oceanbase.odc.common.event.EventPublisher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private BaseRuntimeFlowableDelegate delegate;
    @Mock
    private DelegateExecution execution;
    @Mock
    private Callable mockCallable;
    @Mock
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRun() throws Exception {
        when(delegate.initCallable(execution)).thenReturn(mockCallable);
        when(mockCallable.call()).thenReturn(null);
        delegate.run(execution);
        verify(mockCallable, times(1)).call();
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithInitializationException() throws Exception {
        when(delegate.initCallable(execution)).thenThrow(new RuntimeException());
        delegate.run(execution);
    }

    @Test(expected = RuntimeException.class)
    public void testRunWithExecutionException() throws Exception {
        when(delegate.initCallable(execution)).thenReturn(mockCallable);
        when(mockCallable.call()).thenThrow(new RuntimeException());
        delegate.run(execution);
    }

    @Test
    public void testIsDone() {
        Assert.assertFalse(delegate.isDone());
        delegate.run(execution);
        Assert.assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        when(delegate.initCallable(execution)).thenReturn(mockCallable);
        when(mockCallable.call()).thenReturn(null);
        delegate.run(execution);
        Assert.assertNull(delegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGet_throwExecutionException() throws Exception {
        when(delegate.initCallable(execution)).thenThrow(new RuntimeException());
        delegate.run(execution);
        delegate.get();
    }

    @Test
    public void testGetTimeout() throws Exception {
        when(delegate.initCallable(execution)).thenReturn(mockCallable);
        when(mockCallable.call()).thenReturn(null);
        delegate.run(execution);
        Assert.assertNull(delegate.get(5, TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGetTimeout_throwExecutionException() throws Exception {
        when(delegate.initCallable(execution)).thenThrow(new RuntimeException());
        delegate.run(execution);
        delegate.get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testInit() {
        delegate.init(eventPublisher);
        verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));
        verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
    }
}
