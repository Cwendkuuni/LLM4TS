package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

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

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> testObject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(flowableAdaptor);
        verifyNoMoreInteractions(flowInstanceRepository);
    }

    @Test
    public void testInitCallable() {
        // Given
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));

        // When
        Callable<String> callable = testObject.initCallable(mock(DelegateExecution.class));

        // Then
        assertNotNull(callable);
    }

    @Test(expected = NullPointerException.class)
    public void testInitCallableNullExecution() {
        // When
        Callable<String> callable = testObject.initCallable(null);

        // Then
        fail("Callable should not be null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitCallableNullFlowableAdaptor() {
        // Given
        testObject.flowableAdaptor = null;

        // When
        try {
            testObject.init(eventPublisher);
        } catch (Exception e) {
            // Then
            fail("Exception should not be thrown");
        }

        // When
        Callable<String> callable = testObject.initCallable(mock(DelegateExecution.class));

        // Then
        fail("Callable should not be created without FlowableAdaptor");
    }

    @Test
    public void testPreHandle() {
        // Given
        DelegateExecution execution = mock(DelegateExecution.class);

        // When
        testObject.preHandle(execution);

        // Then
        verifyNoInteractions(eventPublisher);
        verifyNoInteractions(flowableAdaptor);
        verifyNoInteractions(flowInstanceRepository);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        // Given
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));
        Callable<String> callable = mock(Callable.class);

        // When
        testObject.init(eventPublisher);
        testObject.bindToFlowTaskInstance(mock(FlowTaskInstance.class));
        testObject.preHandle(mock(DelegateExecution.class));
        testObject.execute(mock(DelegateExecution.class));
        testObject.get();

        // Then
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.taskInstanceCreatedlistener));
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.activeTaskStatisticsListener));
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(callable, times(1)).call();
        verify(testObject, times(1)).updateFlowInstanceStatus(eq(FlowStatus.COMPLETED));
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException {
        // Given
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));
        Callable<String> callable = mock(Callable.class);

        // When
        testObject.init(eventPublisher);
        testObject.bindToFlowTaskInstance(mock(FlowTaskInstance.class));
        testObject.preHandle(mock(DelegateExecution.class));
        testObject.execute(mock(DelegateExecution.class));

        // Then
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.taskInstanceCreatedlistener));
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.activeTaskStatisticsListener));
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(callable, times(0)).call();

        // When
        testObject.get();

        // Then
        fail("InterruptedException should be thrown");
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException {
        // Given
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException());

        // When
        testObject.init(eventPublisher);
        testObject.bindToFlowTaskInstance(mock(FlowTaskInstance.class));
        testObject.preHandle(mock(DelegateExecution.class));
        testObject.execute(mock(DelegateExecution.class));

        // Then
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.taskInstanceCreatedlistener));
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.activeTaskStatisticsListener));
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());

        // When
        testObject.get();

        // Then
        verify(callable).call();
        verify(testObject, times(1)).updateFlowInstanceStatus(eq(FlowStatus.FAILED));
        verify(testObject.thrown, isInstanceOf(RuntimeException.class));
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));
        Callable<String> callable = mock(Callable.class);

        // When
        testObject.init(eventPublisher);
        testObject.bindToFlowTaskInstance(mock(FlowTaskInstance.class));
        testObject.preHandle(mock(DelegateExecution.class));
        testObject.execute(mock(DelegateExecution.class));

        // Then
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.taskInstanceCreatedlistener));
        verify(eventPublisher, times(2)).removeEventListener(eq(testObject.activeTaskStatisticsListener));
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());

        // When
        testObject.get(5, TimeUnit.SECONDS);

        // Then
        verify(callable, times(0)).call();
        verify(testObject, times(1)).updateFlowInstanceStatus(eq(FlowStatus.TIMEOUT));
        verify(testObject.thrown, isInstanceOf(TimeoutException.class));
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // Given
        when(flowInstanceRepository.updateStatusById(anyLong(), anyInt()))
                .thenReturn(new ExecutionStatusUpdateResult(true, null));

        // When
        testObject.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Then
        verify(flowInstanceRepository).updateStatusById(eq(testObject.flowInstanceId), eq(FlowStatus.COMPLETED.getCode()));
    }

}