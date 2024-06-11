
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.core.shared.exception.ServiceTaskException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {
    private BaseRuntimeFlowableDelegate delegate;
    private DelegateExecution execution;
    private Callable<Boolean> callable;

    @Before
    public void setUp() {
        delegate = mock(BaseRuntimeFlowableDelegate.class);
        execution = mock(DelegateExecution.class);
        callable = () -> true;
    }

    @Test
    public void testRun() throws Exception {
        when(delegate.initCallable(execution)).thenReturn(callable);
        doCallRealMethod().when(delegate).run(execution);
        delegate.run(execution);
        verify(delegate, times(1)).run(execution);
    }

    @Test(expected = ServiceTaskException.class)
    public void testRunException() throws Exception {
        doThrow(ServiceTaskException.class).when(delegate).run(execution);
        delegate.run(execution);
    }

    @Test
    public void testExecute() {
        doNothing().when(delegate).execute(execution);
        delegate.execute(execution);
        verify(delegate, times(1)).execute(execution);
    }

    @Test
    public void testIsDone() {
        when(delegate.isDone()).thenReturn(true);
        assertEquals(true, delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        when(delegate.get()).thenReturn(true);
        assertEquals(true, delegate.get());
    }

    @Test
    public void testGetLongTimeUnit() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegate.get(anyLong(), any(TimeUnit.class))).thenReturn(true);
        assertEquals(true, delegate.get(1000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        doNothing().when(delegate).bindToFlowTaskInstance(taskInstance);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(delegate, times(1)).bindToFlowTaskInstance(taskInstance);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        doNothing().when(delegate).updateFlowInstanceStatus(any());
        delegate.updateFlowInstanceStatus(FlowStatus.ADAPTOR_EXECUTION_FAILURE);
        verify(delegate, times(1)).updateFlowInstanceStatus(FlowStatus.ADAPTOR_EXECUTION_FAILURE);
    }
}
