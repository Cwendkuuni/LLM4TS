
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.delegate.ActivityBehavior;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;
    @Mock
    private DelegateExecution delegateExecution;

    @Before
    public void setUp() {
        doCallRealMethod().when(baseRuntimeFlowableDelegate).execute(delegateExecution);
        doCallRealMethod().when(baseRuntimeFlowableDelegate).run(delegateExecution);
        doCallRealMethod().when(baseRuntimeFlowableDelegate).isDone();
        when(baseRuntimeFlowableDelegate.get()).thenCallRealMethod();
        when(delegateExecution.getCurrentActivityId()).thenReturn("1");
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(delegateExecution);

        verify(baseRuntimeFlowableDelegate, times(1)).execute(delegateExecution);
        verify(baseRuntimeFlowableDelegate, times(1)).run(delegateExecution);
    }

    @Test
    public void testRun() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(baseRuntimeFlowableDelegate, times(1)).run(delegateExecution);
    }

    @Test
    public void testIsDone() {
        boolean done = baseRuntimeFlowableDelegate.isDone();

        assertTrue(done);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Object result = baseRuntimeFlowableDelegate.get();

        assertEquals(result, null);
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws InterruptedException, ExecutionException {
        doThrow(new ExecutionException("test", new Throwable())).when(baseRuntimeFlowableDelegate).get();

        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegate.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());

        baseRuntimeFlowableDelegate.get(1L, TimeUnit.SECONDS);
    }
}
