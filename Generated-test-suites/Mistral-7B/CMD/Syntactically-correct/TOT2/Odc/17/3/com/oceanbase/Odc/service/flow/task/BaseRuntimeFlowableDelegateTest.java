package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private CountDownLatch latch;

    @Mock
    private DelegateExecution execution;

    @Mock
    private ServiceTaskStartedEvent serviceTaskStartedEvent;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> delegateUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(serviceTaskStartedEvent);
        when(flowInstanceRepository.updateStatusById(1L, any(FlowStatus.class))).thenReturn(true);
    }

    @Test
    public void testIsDone_whenDone_thenReturnsTrue() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.get()).thenReturn(delegateUnderTest);
        when(latch.await()).thenReturn(true);
        when(latch.getCount()).thenReturn(0);
        when(delegateUnderTest.isDone()).thenReturn(true);
        assertTrue(delegateUnderTest.isDone());
    }

    @Test
    public void testIsDone_whenNotDone_thenReturnsFalse() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.get()).thenReturn(delegateUnderTest);
        when(latch.await()).thenReturn(false);
        when(delegateUnderTest.isDone()).thenReturn(false);
        assertFalse(delegateUnderTest.isDone());
    }

    @Test
    public void testGet_whenNotDone_thenThrowsInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.get()).thenReturn(delegateUnderTest);
        when(latch.await()).thenThrow(new InterruptedException());
        try {
            delegateUnderTest.get();
            fail("Expected InterruptedException");
        } catch (InterruptedException e) {
            // expected
        }
    }

    @Test
    public void testGet_whenDoneWithException_thenThrowsExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.get()).thenReturn(delegateUnderTest);
        when(latch.await()).thenReturn(true);
        when(delegateUnderTest.thrown).thenReturn(new RuntimeException("Exception"));
        when(delegateUnderTest.done).thenReturn(true);
        try {
            delegateUnderTest.get();
            fail("Expected ExecutionException");
        } catch (ExecutionException e) {
            assertSame(delegateUnderTest.thrown, e.getCause());
        }
    }

    @Test
    public void testGet_whenDoneWithoutException_thenReturnsResult() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.get()).thenReturn(delegateUnderTest);
        when(latch.await()).thenReturn(true);
        when(delegateUnderTest.returnObject).thenReturn("result");
        when(delegateUnderTest.done).thenReturn(true);
        String result = delegateUnderTest.get();
        assertEquals("result", result);
    }

    @Test
    public void testGetTimeoutException_whenNotDone_thenThrowsTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.get()).thenReturn(delegateUnderTest);
        when(latch.await(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());
        try {
            delegateUnderTest.get(5, TimeUnit.SECONDS);
            fail("Expected TimeoutException");
        } catch (TimeoutException e) {
            // expected
        }
    }

    @Test
    public void testInit_whenEventPublisherIsNotNull_thenInitializesEventPublisher() {
        delegateUnderTest = new BaseRuntimeFlowableDelegate<>();
        delegateUnderTest.init(eventPublisher);
        verify(eventPublisher).addEventListener(any(ActiveTaskStatisticsListener.class));
        verify(eventPublisher).addEventListener(any(TaskInstanceCreatedListener.class));
    }

    @Test
    public void testRun_whenInitializeSuccess_thenCallsPreHandleAndCallable() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(2L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

        ArgumentCaptor<FlowStatus> flowStatusArgumentCaptor = ArgumentCaptor.forClass(FlowStatus.class);
        delegateUnderTest.init(eventPublisher);
        delegateUnderTest.run(execution);
        verify(flowInstanceRepository).updateStatusById(anyLong(), flowStatusArgumentCaptor.capture());
        assertEquals(FlowStatus.COMPLETED, flowStatusArgumentCaptor.getValue());
    }

    @Test
    public void testPreHandle_whenCalled_thenDoesNothing() throws Exception {
        delegateUnderTest.preHandle(execution);
    }

    @Test
    public void testExecute_whenCalled_thenRemovesListeners() throws Exception {
        delegateUnderTest.execute(execution);
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

}