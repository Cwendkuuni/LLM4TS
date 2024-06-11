
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setup() {
        delegate = Mockito.spy(new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        });
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        DelegateExecution execution = mock(DelegateExecution.class);

        when(execution.getProcessDefinitionId()).thenReturn("process123");
        doReturn(Optional.empty()).when(delegate.flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("process123");

        delegate.initTargetTaskInstanceId(execution);

        verify(delegate, times(1)).updateFlowInstanceStatus(any());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);

        delegate.setTargetTaskInstanceId(1L);
        delegate.setActivityId("activity123");

        delegate.bindToFlowTaskInstance(taskInstance);

        assertEquals(delegate.getTargetTaskInstanceId(), taskInstance.getId());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.setFlowInstanceId(1L);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        assertNotNull(delegate.getFlowInstanceId());
    }

    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.get();
        assertNull(delegate.getReturnObject());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.get(1000, TimeUnit.MILLISECONDS);
        assertNull(delegate.getReturnObject());
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.run(execution);

        assertNotNull(delegate.getReturnObject());
    }
}
