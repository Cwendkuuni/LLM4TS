package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;

    @Before
    public void setup() {
        delegate = spy(BaseRuntimeFlowableDelegate.class);
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        doNothing().when(delegate).run(any());
        doReturn(eventPublisher).when(delegate).getEventPublisher();
        doReturn(flowableAdaptor).when(delegate).getFlowableAdaptor();
    }


    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        doThrow(ServiceTaskError.class).when(delegate).run(execution);

        delegate.execute(execution);
    }

    @Test
    public void testExecute() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.execute(execution);

        verify(eventPublisher, times(1)).removeEventListener(any());
    }

    @Test
    public void testIsDone() {
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        String returnValue = "success";
        doReturn(returnValue).when(delegate).get();

        assertEquals(returnValue, delegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws Exception {
        Exception executionException = new InterruptedException();
        doThrow(executionException).when(delegate).get();

        delegate.get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        String returnValue = "success";
        doReturn(returnValue).when(delegate).get(anyLong(), any());

        assertEquals(returnValue, delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws Exception {
        doThrow(new TimeoutException()).when(delegate).get(anyLong(), any());

        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.ACTIVE);

        verify(delegate, times(1)).updateFlowInstanceStatus(any());
    }
}