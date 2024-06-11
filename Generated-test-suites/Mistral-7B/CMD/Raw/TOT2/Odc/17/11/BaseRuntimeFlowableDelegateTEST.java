package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @org.junit.Before
    public void setUp() {
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        RetryExecutor retryExecutor = Mockito.mock(RetryExecutor.class);
        ActiveTaskStatisticsListener activeTaskStatisticsListener = Mockito.mock(ActiveTaskStatisticsListener.class);

        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }

            @Override
            protected void initTargetTaskInstanceId(DelegateExecution execution) {
                // no-op
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
                // no-op
            }

            @Override
            protected void updateFlowInstanceStatus(FlowStatus flowStatus) {
                // no-op
            }
        };

        delegate.init(eventPublisher);
        delegate.retryExecutor = retryExecutor;
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.latch = new CountDownLatch(1);
        delegate.taskInstanceCreatedlistener = new TaskInstanceCreatedListener(delegate);
        delegate.activeTaskStatisticsListener = activeTaskStatisticsListener;

        this.delegate = delegate;
        this.eventPublisher = eventPublisher;
        this.flowableAdaptor = flowableAdaptor;
    }

    // Test for the constructor
    @org.junit.Test
    public void testConstructor() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<>();
        assertNotNull(delegate);
    }

    // Test for init method
    @org.junit.Test
    public void testInit() {
        delegate.init(eventPublisher);
        verify(eventPublisher, times(1)).addEventListener(activeTaskStatisticsListener);
        verify(eventPublisher, times(1)).addEventListener(taskInstanceCreatedlistener);
    }

    // Test for run method with success
    @org.junit.Test
    public void testRunWithSuccess() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));

        delegate.execute(Mockito.mock(DelegateExecution.class));

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(delegate, times(1)).preHandle(Mockito.any(DelegateExecution.class));
        verify(delegate, times(1)).run(Mockito.any(DelegateExecution.class));
    }

    // Test for run method with exception
    @org.junit.Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new RuntimeException("Test Exception"));

        delegate.execute(Mockito.mock(DelegateExecution.class));

        verify(delegate, times(1)).preHandle(Mockito.any(DelegateExecution.class));
        verify(delegate, never()).run(Mockito.any(DelegateExecution.class));
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, never()).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher, never()).removeEventListener(activeTaskStatisticsListener);

        throw new ServiceTaskError("Test Exception");
    }

    // Test for get method
    @org.junit.Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));
        when(delegate.call()).thenReturn(1);

        delegate.execute(Mockito.mock(DelegateExecution.class));
        delegate.get();

        verify(delegate, times(1)).run(Mockito.any(DelegateExecution.class));
        verify(delegate, times(1)).isDone();
        verify(delegate, times(1)).get();
    }

    // Test for get method with timeout
    @org.junit.Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));
        when(delegate.call()).thenReturn(null);

        delegate.execute(Mockito.mock(DelegateExecution.class));
        delegate.get(1, TimeUnit.SECONDS);

        verify(delegate, times(1)).run(Mockito.any(DelegateExecution.class));
        verify(delegate, times(1)).isDone();
        verify(delegate, times(1)).get(1, TimeUnit.SECONDS);

        throw new TimeoutException();
    }

    // Test for bindToFlowTaskInstance method
    @org.junit.Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);

        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

    // Test for updateFlowInstanceStatus method
    @org.junit.Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(delegate.retryExecutor, times(1)).run(() -> {
            boolean result = flowInstanceRepository.updateStatusById(delegate.flowInstanceId, FlowStatus.COMPLETED);
            return result;
        }, anyBoolean());
    }

    // Test for initTargetTaskInstanceId method
    @org.junit.Test
    public void testInitTargetTaskInstanceId() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));

        delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));
        verify(delegate, times(1)).flowInstanceId = 1L;
    }

    private BaseRuntimeFlowableDelegate<Integer> delegate;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
}