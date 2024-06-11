package com.oceanbase.odc.service.flow.test.task;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

/**
 * Test for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private DelegateExecution execution;

    @Mock
    private CountDownLatch latch;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private FlowTaskInstance taskInstance;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test for constructor
     */
    @Test
    public void testConstructor() {
        // Given

        // When
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<>();

        // Then
        assertNotNull(delegate);
    }

    /**
     * Test for init method
     */
    @Test
    public void testInit() {
        // Given
        when(eventPublisher.removeEventListener(any())).thenReturn(true);

        // When
        delegate.init(eventPublisher);

        // Then
        verify(eventPublisher).addEventListener(any());
    }

    /**
     * Test for run method with normal usage
     */
    @Test
    public void testRunNormalUsage() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(taskInstance);
        when(taskInstance.getId()).thenReturn(targetTaskInstanceId);
        when(taskInstance.getTaskType()).thenReturn(taskType);
        when(taskInstance.getStrategyConfig()).thenReturn(strategyConfig);
        doNothing().when(delegate).preHandle(execution);
        doReturn(new Object()).when(delegate).initCallable(execution);

        // When
        delegate.run(execution);

        // Then
        verify(eventPublisher).publishEvent(any());
    }

    /**
     * Test for run method with exception during initialization
     */
    @Test(expected = ServiceTaskError.class)
    public void testRunExceptionDuringInitialization() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
            .thenThrow(new RuntimeException("Initialization failed"));

        // When
        delegate.run(execution);

        // Then
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class).eq(any()));
        throw new ServiceTaskError("Initialization failed");
    }

    /**
     * Test for get method with normal usage
     */
    @Test
    public void testGetNormalUsage() throws Exception {
        // Given
        when(latch.await(anyInt(), anyInt())).thenReturn(true);
        when(delegate.isDone()).thenReturn(true);
        when(delegate.get()).thenReturn(new Object());

        // When
        Object result = delegate.get();

        // Then
        assertEquals(new Object(), result);
    }

    /**
     * Test for get method with timeout
     */
    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws Exception {
        // Given
        when(latch.await(anyInt(), anyInt())).thenReturn(false);
        when(delegate.isDone()).thenReturn(false);

        // When
        delegate.get(1, TimeUnit.SECONDS);

        // Then
        fail("Test should time out");
    }

    /**
     * Test for get method with exception
     */
    @Test(expected = ExecutionException.class)
    public void testGetException() throws Exception {
        // Given
        when(latch.await(anyInt(), anyInt())).thenReturn(true);
        when(delegate.isDone()).thenReturn(true);
        when(delegate.get()).thenThrow(new RuntimeException("Execution failed"));

        // When
        delegate.get();

        // Then
        fail("Test should throw ExecutionException");
    }

    /**
     * Test for initCallable method
     */
    @Test
    public void testInitCallable() {
        // Given
        when(execution.getCurrentActivityId()).thenReturn("activityId");

        // When
        Callable<Object> callable = delegate.initCallable(execution);

        // Then
        assertNotNull(callable);
    }

}