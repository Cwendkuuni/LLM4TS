package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    private static final String TEST_ACTIVITY_ID = "testActivityId";
    private static final String TEST_PROCESS_DEFINITION_ID = "testProcessDefinitionId";
    private static final String TEST_FLOW_INSTANCE_ID = "testFlowInstanceId";
    private static final String TEST_TARGET_TASK_INSTANCE_ID = "testTargetTaskInstanceId";
    private static final TaskType TEST_TASK_TYPE = TaskType.TEST_TASK_TYPE;
    private static final ExecutionStrategyConfig TEST_STRATEGY_CONFIG = new ExecutionStrategyConfig();

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private BaseRuntimeFlowableDelegate<Object> spyDelegate;
    private Callable<Object> callableMock;
    private EventPublisher eventPublisherMock;
    private FlowableAdaptor flowableAdaptorMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;

    @Before
    public void setUp() {
        eventPublisherMock = mock(EventPublisher.class);
        flowableAdaptorMock = mock(FlowableAdaptor.class);
        flowInstanceRepositoryMock = mock(FlowInstanceRepository.class);
        callableMock = mock(Callable.class);
        delegate = new BaseRuntimeFlowableDelegate<>();
        delegate.setFlowableAdaptor(flowableAdaptorMock);
        delegate.setEventPublisher(eventPublisherMock);
        delegate.setFlowInstanceRepository(flowInstanceRepositoryMock);
        spyDelegate = spy(delegate);
        when(spyDelegate.initCallable(any(DelegateExecution.class))).thenReturn(callableMock);
    }

    @After
    public void tearDown() {
        reset(eventPublisherMock, flowableAdaptorMock, flowInstanceRepositoryMock, callableMock);
    }

    @Test
    public void testInitCallable() throws Exception {
        // Given
        DelegateExecution executionMock = mock(DelegateExecution.class);

        // When
        Callable<Object> result = spyDelegate.initCallable(executionMock);

        // Then
        verify(callableMock).call();
        assertEquals(callableMock, result);
    }

    @Test
    public void testRun() throws Exception {
        // Given
        DelegateExecution executionMock = mock(DelegateExecution.class);
        when(executionMock.getCurrentActivityId()).thenReturn(TEST_ACTIVITY_ID);
        when(executionMock.getProcessDefinitionId()).thenReturn(TEST_PROCESS_DEFINITION_ID);
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(TEST_PROCESS_DEFINITION_ID)).thenReturn(TEST_FLOW_INSTANCE_ID);
        when(flowableAdaptorMock.getTaskInstanceByActivityId(TEST_ACTIVITY_ID, TEST_FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowTaskInstance()));
        when(flowInstanceRepositoryMock.updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED)).thenReturn(true);

        // When
        spyDelegate.run(executionMock);

        // Then
        verify(callableMock).call();
        verify(eventPublisherMock).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyNoMoreInteractions(callableMock);
        verify(spyDelegate).preHandle(executionMock);
        verify(spyDelegate).updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepositoryMock).updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
    }

    @Test(expected = ExecutionException.class)
    public void testRunWithException() throws Exception {
        // Given
        DelegateExecution executionMock = mock(DelegateExecution.class);
        when(executionMock.getCurrentActivityId()).thenReturn(TEST_ACTIVITY_ID);
        when(executionMock.getProcessDefinitionId()).thenReturn(TEST_PROCESS_DEFINITION_ID);
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(TEST_PROCESS_DEFINITION_ID)).thenReturn(TEST_FLOW_INSTANCE_ID);
        when(flowableAdaptorMock.getTaskInstanceByActivityId(TEST_ACTIVITY_ID, TEST_FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowTaskInstance()));
        when(flowInstanceRepositoryMock.updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED)).thenReturn(false);

        // When
        spyDelegate.run(executionMock);

        // Then
        verify(callableMock).call();
        verify(eventPublisherMock).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(spyDelegate).preHandle(executionMock);
        verify(spyDelegate).updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepositoryMock).updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
        throw new ExecutionException(new ServiceTaskError("Test error"));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        // Given
        DelegateExecution executionMock = mock(DelegateExecution.class);
        when(executionMock.getCurrentActivityId()).thenReturn(TEST_ACTIVITY_ID);
        when(executionMock.getProcessDefinitionId()).thenReturn(TEST_PROCESS_DEFINITION_ID);
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(TEST_PROCESS_DEFINITION_ID)).thenReturn(TEST_FLOW_INSTANCE_ID);
        when(flowableAdaptorMock.getTaskInstanceByActivityId(TEST_ACTIVITY_ID, TEST_FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowTaskInstance()));
        when(flowInstanceRepositoryMock.updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED)).thenReturn(true);

        // When
        Object result = spyDelegate.get();

        // Then
        verify(callableMock).call();
        verify(eventPublisherMock).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyNoMoreInteractions(callableMock);
        verify(spyDelegate).preHandle(executionMock);
        verify(spyDelegate).updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepositoryMock).updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
        assertEquals(null, spyDelegate.thrown);
        assertEquals(true, spyDelegate.done);
        assertEquals(result, spyDelegate.returnObject);
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        DelegateExecution executionMock = mock(DelegateExecution.class);
        when(executionMock.getCurrentActivityId()).thenReturn(TEST_ACTIVITY_ID);
        when(executionMock.getProcessDefinitionId()).thenReturn(TEST_PROCESS_DEFINITION_ID);
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(TEST_PROCESS_DEFINITION_ID)).thenReturn(TEST_FLOW_INSTANCE_ID);
        when(flowableAdaptorMock.getTaskInstanceByActivityId(TEST_ACTIVITY_ID, TEST_FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowTaskInstance()));
        when(flowInstanceRepositoryMock.updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED)).thenReturn(true);

        // When
        spyDelegate.get();

        // Then
        verify(callableMock).call();
        verify(eventPublisherMock).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyNoMoreInteractions(callableMock);
        verify(spyDelegate).preHandle(executionMock);
        verify(spyDelegate).updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepositoryMock).updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
        throw new InterruptedException();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        DelegateExecution executionMock = mock(DelegateExecution.class);
        when(executionMock.getCurrentActivityId()).thenReturn(TEST_ACTIVITY_ID);
        when(executionMock.getProcessDefinitionId()).thenReturn(TEST_PROCESS_DEFINITION_ID);
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(TEST_PROCESS_DEFINITION_ID)).thenReturn(TEST_FLOW_INSTANCE_ID);
        when(flowableAdaptorMock.getTaskInstanceByActivityId(TEST_ACTIVITY_ID, TEST_FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowTaskInstance()));
        when(flowInstanceRepositoryMock.updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED)).thenReturn(true);

        // When
        spyDelegate.get(1, TimeUnit.SECONDS);

        // Then
        verify(callableMock).call();
        verify(eventPublisherMock).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyNoMoreInteractions(callableMock);
        verify(spyDelegate).preHandle(executionMock);
        verify(spyDelegate).updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepositoryMock).updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
        throw new TimeoutException();
    }

    @Test
    public void testPreHandle() throws Exception {
        // Given
        DelegateExecution executionMock = mock(DelegateExecution.class);

        // When
        spyDelegate.preHandle(executionMock);

        // Then
        verifyNoMoreInteractions(callableMock);
    }

    @Test(expected = NullPointerException.class)
    public void testPreHandleWithNullExecution() throws Exception {
        // Given
        DelegateExecution executionMock = null;

        // When
        spyDelegate.preHandle(executionMock);

        // Then
        verifyNoMoreInteractions(callableMock);
        throw new NullPointerException("execution");
    }
}