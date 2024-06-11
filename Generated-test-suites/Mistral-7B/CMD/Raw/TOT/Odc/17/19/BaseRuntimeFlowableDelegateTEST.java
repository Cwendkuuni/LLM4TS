package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}.
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private Callable<String> callableMock;
    private FlowableAdaptor flowableAdaptorMock;
    private EventPublisher eventPublisherMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;
    private DelegateExecution delegateExecutionMock;
    private TaskInstanceCreatedListener taskInstanceCreatedlistenerMock;
    private ActiveTaskStatisticsListener activeTaskStatisticsListenerMock;

    @Before
    public void setUp() {
        this.callableMock = mock(Callable.class);
        this.flowableAdaptorMock = mock(FlowableAdaptor.class);
        this.eventPublisherMock = mock(EventPublisher.class);
        this.flowInstanceRepositoryMock = mock(FlowInstanceRepository.class);
        this.delegateExecutionMock = mock(DelegateExecution.class);
        this.taskInstanceCreatedlistenerMock = mock(TaskInstanceCreatedListener.class);
        this.activeTaskStatisticsListenerMock = mock(ActiveTaskStatisticsListener.class);

        when(this.flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));

        when(this.flowableAdaptorMock.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        when(this.flowInstanceRepositoryMock.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(true);

        this.delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected void preHandle(DelegateExecution execution) throws Exception {
                // empty implementation
            }

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return this.callableMock;
            }
        };

        this.delegate.init(this.eventPublisherMock);

        when(this.eventPublisherMock.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);

        this.delegate.flowableAdaptor = this.flowableAdaptorMock;
        this.delegate.eventPublisher = this.eventPublisherMock;
        this.delegate.flowInstanceRepository = this.flowInstanceRepositoryMock;

        this.delegate.initTargetTaskInstanceId(this.delegateExecutionMock);

        this.delegate.taskInstanceCreatedlistener = this.taskInstanceCreatedlistenerMock;
        this.delegate.activeTaskStatisticsListener = this.activeTaskStatisticsListenerMock;
    }

    @Test
    public void testRun_CallableThrowsException_UpdatesFlowStatus() throws Exception {
        when(this.callableMock.call()).thenThrow(new Exception());

        this.delegate.run(this.delegateExecutionMock);

        ArgumentCaptor<FlowStatus> statusCaptor = ArgumentCaptor.forClass(FlowStatus.class);
        verify(this.flowInstanceRepositoryMock).updateStatusById(this.delegate.flowInstanceId, statusCaptor.capture());
        assertEquals(FlowStatus.FAILED, statusCaptor.getValue());
    }

    @Test
    public void testRun_CallableReturnsValue_UpdatesFlowStatus() throws Exception {
        when(this.callableMock.call()).thenReturn("Hello World");

        String result = this.delegate.get();

        ArgumentCaptor<FlowStatus> statusCaptor = ArgumentCaptor.forClass(FlowStatus.class);
        verify(this.flowInstanceRepositoryMock).updateStatusById(this.delegate.flowInstanceId, statusCaptor.capture());
        assertEquals(FlowStatus.COMPLETED, statusCaptor.getValue());
        assertEquals("Hello World", result);
    }

    @Test(expected = TimeoutException.class)
    public void testGet_CallableTakesTooLongToComplete_ThrowsTimeoutException() throws Exception {
        when(this.callableMock.call())
                .thenCallRealMethod(); // let it take a long time to complete

        this.delegate.get(5, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGet_CallableThrowsException_ThrowsExecutionException() throws Exception {
        when(this.callableMock.call()).thenThrow(new Exception());

        this.delegate.get();
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        this.delegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance).bindServiceTask(this.delegate);
    }
}