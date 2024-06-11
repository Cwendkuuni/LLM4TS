package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util. Optional;
import java.util.concurrent.ExecutionException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableTestHelper;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.test.BaseTest;
import com.oceanbase.odc.service.flow.util.TestEventPublisher;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest extends BaseTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    private BaseRuntimeFlowableDelegate<Object> delegate;

    private Execution execution;

    private DelegateExecution delegateExecution;

    private final ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor =
            ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return mock(Callable.class);
            }

            @Override
            protected void preHandle(DelegateExecution execution) {}

            @Override
            protected void updateFlowInstanceStatus(FlowStatus flowStatus) {}
        };

        delegate.init(eventPublisher);
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.retryExecutor = retryExecutor;

        execution = mock(Execution.class);
        delegateExecution = mock(DelegateExecution.class);
        when(execution.getDelegateExecution()).thenReturn(delegateExecution);
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
    }

    @Test
    public void testRun_whenInitializationFails_throwsServiceTaskError() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new RuntimeException("Initialization failed"));

        delegate.run(execution);

        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        ServiceTaskStartedEvent event = serviceTaskStartedEventCaptor.getValue();
        assertEquals(ServiceTaskError.class, event.getErrorType());
        assertEquals("Initialization failed", event.getMessage());
        verify(eventPublisher).publishEvent(eq(ServiceTaskError.class), any());
    }

    @Test
    public void testRun_whenServiceTaskExecutionFails_throwsException() throws Exception {
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("Service task execution failed"));

        delegate.activityId = "activityId";
        delegate.initTargetTaskInstanceId(execution);
        delegate.preHandle(execution);

        try {
            delegate.run(execution);
            fail("Expected exception to be thrown");
        } catch (ServiceTaskError e) {
            assertEquals("Service task execution failed", e.getMessage());
            verify(callable).call();
            verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
            ServiceTaskStartedEvent event = serviceTaskStartedEventCaptor.getValue();
            assertEquals("SERVICE_TASK_EXECUTION_FAILED", event.getEventType());
            verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
            verifyZeroInteractions(flowInstanceRepository);
            verifyZeroInteractions(retryExecutor);
        }
    }

    @Test
    public void testRun_whenServiceTaskExecutionSucceeds_setsDoneAndLatchCountsDown() throws Exception {
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn(new Object());

        delegate.activityId = "activityId";
        delegate.initTargetTaskInstanceId(execution);
        delegate.preHandle(execution);

        Object result = delegate.run(execution);

        verify(callable).call();
        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        ServiceTaskStartedEvent event = serviceTaskStartedEventCaptor.getValue();
        assertEquals("SERVICE_TASK_EXECUTED_SUCCESSFULLY", event.getEventType());
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowInstanceRepository).updateStatusById(eq(Long.parseLong("flowInstanceId")), eq(FlowStatus.COMPLETED));
        verify(retryExecutor).run(any(), any());
        verify(eventPublisher).removeEventListener(eq(TaskInstanceCreatedListener.class));
        verify(eventPublisher).removeEventListener(eq(ActiveTaskStatisticsListener.class));
        assertEquals(result, delegate.get());
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet_whenDone_returnsReturnObject() throws InterruptedException, ExecutionException {
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn(new Object());

        delegate.activityId = "activityId";
        delegate.initTargetTaskInstanceId(execution);
        delegate.preHandle(execution);

        delegate.run(execution);

        Object result = delegate.get();

        assertEquals(result, delegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGet_whenNotDone_interruptedException() throws InterruptedException, ExecutionException {
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn(new Object());

        delegate.activityId = "activityId";
        delegate.initTargetTaskInstanceId(execution);
        delegate.preHandle(execution);

        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGet_whenExceptionThrown_executionException() throws InterruptedException, ExecutionException {
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("Exception thrown"));

        delegate.activityId = "activityId";
        delegate.initTargetTaskInstanceId(execution);
        delegate.preHandle(execution);

        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGet_whenTimeout_timeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn(new Object());

        delegate.activityId = "activityId";
        delegate.initTargetTaskInstanceId(execution);
        delegate.preHandle(execution);

        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance_whenTaskInstancesMatch_bindsTask() throws Exception {
        FlowTaskInstance taskInstance = FlowableTestHelper.createFlowTaskInstance(1L, TaskType.TEST_TASK_TYPE,
                FlowStatus.CREATED, new ExecutionStrategyConfig());

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(eq(delegate));
    }

    @Test
    public void testBindToFlowTaskInstance_whenTaskInstancesDoNotMatch_doesNotBindTask() throws Exception {
        FlowTaskInstance taskInstance = FlowableTestHelper.createFlowTaskInstance(2L, TaskType.TEST_TASK_TYPE,
                FlowStatus.CREATED, new ExecutionStrategyConfig());

        delegate.bindToFlowTaskInstance(taskInstance);

        verifyZeroInteractions(taskInstance);
    }

}