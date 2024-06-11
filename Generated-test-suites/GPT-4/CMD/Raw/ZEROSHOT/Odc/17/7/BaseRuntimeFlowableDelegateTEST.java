
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.TaskEntityImpl;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate delegate;

    @Before
    public void setup() {
        delegate = mock(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testIsDone() {
        delegate.isDone();
        verify(delegate).isDone();
    }

    @Test(expected = InterruptedException.class)
    public void testGet1() throws InterruptedException, ExecutionException {
        when(delegate.get()).thenThrow(InterruptedException.class);
        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGet2() throws ExecutionException, InterruptedException {
        when(delegate.get()).thenThrow(ExecutionException.class);
        delegate.get();
    }

    @Test
    public void testGet3() throws Exception {
        when(delegate.get()).thenReturn(null);
        assert(delegate.get() == null);
    }

    @Test(expected = InterruptedException.class)
    public void testGetWithTimeout1() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.get(1000, TimeUnit.MILLISECONDS)).thenThrow(InterruptedException.class);
        delegate.get(1000, TimeUnit.MILLISECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeout2() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.get(1000, TimeUnit.MILLISECONDS)).thenThrow(ExecutionException.class);
        delegate.get(1000, TimeUnit.MILLISECONDS);
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout3() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.get(1000, TimeUnit.MILLISECONDS)).thenThrow(TimeoutException.class);
        delegate.get(1000, TimeUnit.MILLISECONDS);
    }
    
    @Test
    public void testExecute() {
        DelegateExecution execution = new ExecutionEntityImpl();
        delegate.execute(execution);
        verify(delegate).execute(execution);
    }

    @Test
    public void testRun() {
        DelegateExecution execution = new ExecutionEntityImpl();
        delegate.run(execution);
        verify(delegate).run(execution);
    }
}
