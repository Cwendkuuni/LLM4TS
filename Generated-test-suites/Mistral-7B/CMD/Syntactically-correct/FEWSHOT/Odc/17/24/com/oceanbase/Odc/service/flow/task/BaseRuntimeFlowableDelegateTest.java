package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private RetryExecutor retryExecutor;
    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    @Mock
    private DelegateExecution execution;
    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;
    private Callable<Object> callable;
    private CountDownLatch latch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        delegate.init(eventPublisher);
        this.callable = mock(Callable.class);
        this.latch = new CountDownLatch(1);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        PreConditions.assertNotNull(delegate.getFlowInstanceId());
        PreConditions.assertNotNull(delegate.getTargetTaskInstanceId());
        PreConditions.assertNotNull(delegate.getTaskType());
        PreConditions.assertNotNull(delegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.ofNullable(taskInstanceCreatedlistener));
        delegate.run(execution);

        ArgumentCaptor<ServiceTaskStartedEvent> argumentCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(argumentCaptor.capture());
        assertEquals(delegate, argumentCaptor.getValue().getServiceTask());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.setReturnObject(new Object());
        assertEquals(delegate.get(), delegate.getReturnObject());
        assertEquals(delegate.get().getClass(), delegate.getReturnObject().getClass());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException {
        Thread.currentThread().interrupt();
        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException {
        RuntimeException exception = new RuntimeException();
        delegate.setThrown(exception);
        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.latch.await(1, TimeUnit.MILLISECONDS);
        delegate.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());
        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowStatus flowStatus = FlowStatus.COMPLETED;
        delegate.updateFlowInstanceStatus(flowStatus);
        verify(flowInstanceRepository, times(1)).updateStatusById(delegate.flowInstanceId, flowStatus);
    }

    @Test
    public void testInitCallable() {
        delegate.initCallable(execution);
        verify(execution, times(1)).getProcessDefinitionId();
    }
}