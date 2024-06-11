package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * BaseRuntimeFlowableDelegateTest
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private Execution execution;

    @Mock
    private Callable<Object> callable;

    @Mock
    private Future<Object> future;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(flowTaskInstance);
    }

    @Test
    public void testInitialize() {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
        verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));
        verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
    }

    @Test
    public void testRun_success() throws Exception {
        when(callable.call()).thenReturn(new Object());
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(flowTaskInstance);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
        baseRuntimeFlowableDelegate.run(execution);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, eq(FlowStatus.RUNNING));
        verify(callable, times(1)).call();
    }

    @Test
    public void testRun_exception() throws Exception {
        when(callable.call()).thenThrow(new RuntimeException());
        baseRuntimeFlowableDelegate.run(execution);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), eq(FlowStatus.RUNNING));
        verify(callable, never()).call();
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenReturn(new Object());
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get();
        verify(latch, times(1)).await();
        verify(latch, times(1)).countDown();
    }

    @Test(expected = InterruptedException.class)
    public void testGet_interrupted() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get();
        baseRuntimeFlowableDelegate.get(5, TimeUnit.SECONDS);
        throw new InterruptedException();
    }

    @Test(expected = ExecutionException.class)
    public void testGet_executionException() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenThrow(new ExecutionException(new RuntimeException()));
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGet_timeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get(5, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, eq(FlowStatus.COMPLETED));
    }
}