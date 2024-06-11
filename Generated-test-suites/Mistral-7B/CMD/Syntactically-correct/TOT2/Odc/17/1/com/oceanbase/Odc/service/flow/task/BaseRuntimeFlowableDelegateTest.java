package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.FlowStatus;
import com.oceanbase.odc.core.shared.PreConditions;
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
 *
 * @author yh263208
 * @date 2022-02-14 14:06
 * @since ODC_release_3.3.0
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;
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

    private BaseRuntimeFlowableDelegate<String> testee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "Test Data";
                    }
                };
            }
        };
        testee.init(eventPublisher);
        testee.flowableAdaptor = flowableAdaptor;
        testee.eventPublisher = eventPublisher;
        testee.flowInstanceRepository = flowInstanceRepository;
        testee.retryExecutor = retryExecutor;
        testee.taskInstanceCreatedlistener = taskInstanceCreatedlistener;
        testee.activeTaskStatisticsListener = activeTaskStatisticsListener;
        when(execution.getProcessDefinitionId()).thenReturn("Test Process Definition ID");
    }

    @Test
    public void testRun_Success() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("Test Process Definition ID"))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("Test Activity ID", 1L))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        // When
        testee.run(execution);

        // Then
        verify(eventPublisher).publishEvent(ArgumentMatchers.<ServiceTaskStartedEvent>any());
        verifyNoMoreInteractions(flowableAdaptor, flowInstanceRepository, retryExecutor, taskInstanceCreatedlistener,
                activeTaskStatisticsListener);
        assertNotNull(testee.get());
        assertEquals("Test Data", testee.get());
    }

    @Test(expected = ExecutionException.class)
    public void testRun_ExceptionDuringInitialization() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("Test Process Definition ID"))
                .thenThrow(new RuntimeException());

        // When
        testee.run(execution);

        // Then
        verify(eventPublisher).publishEvent(ArgumentMatchers.<ServiceTaskStartedEvent>any());
        verify(execution).getException();
        verify(taskInstanceCreatedlistener).onTaskInstanceCreated(ArgumentMatchers.<FlowTaskInstance>any());
        verify(activeTaskStatisticsListener).onTaskStarted(ArgumentMatchers.<BaseRuntimeFlowableDelegate<?>>any());
        assertEquals(ServiceTaskError.class, testee.getException().getClass());
    }

    @Test(expected = ExecutionException.class)
    public void testRun_ExceptionDuringExecution() throws Exception {
        // Given
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("Test Process Definition ID"))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("Test Activity ID", 1L))
                .thenReturn(Optional.of(new FlowTaskInstance()));
        when(execution.getCurrentActivityId()).thenReturn("Test Activity ID");

        doThrow(new RuntimeException()).when(testee).preHandle(execution);

        // When
        testee.run(execution);

        // Then
        verify(eventPublisher).publishEvent(ArgumentMatchers.<ServiceTaskStartedEvent>any());
        verify(execution).getException();
        verify(taskInstanceCreatedlistener).onTaskInstanceCreated(ArgumentMatchers.<FlowTaskInstance>any());
        verify(activeTaskStatisticsListener).onTaskStarted(ArgumentMatchers.<BaseRuntimeFlowableDelegate<?>>any());
        assertEquals(RuntimeException.class, testee.getException().getCause().getClass());
    }

    @Test
    public void testGet_Success() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        testee.initTargetTaskInstanceId(execution);

        // When
        testee.get();

        // Then
        verifyNoMoreInteractions(flowableAdaptor, flowInstanceRepository, retryExecutor, taskInstanceCreatedlistener,
                activeTaskStatisticsListener);
        assertNotNull(testee.get());
        assertEquals("Test Data", testee.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGet_Interrupted() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        testee.initTargetTaskInstanceId(execution);
        testee.latch.await();
        testee.done = false;

        // When & Then
        throw new InterruptedException();
    }

    @Test(expected = ExecutionException.class)
    public void testGet_ExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        testee.initTargetTaskInstanceId(execution);
        testee.thrown = new RuntimeException();
        testee.done = true;

        // When
        testee.get();

        // Then
        verifyNoMoreInteractions(flowableAdaptor, flowInstanceRepository, retryExecutor, taskInstanceCreatedlistener,
                activeTaskStatisticsListener);
        assertEquals(RuntimeException.class, testee.getException().getClass());
    }

    @Test(expected = TimeoutException.class)
    public void testGet_TimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        testee.initTargetTaskInstanceId(execution);
        testee.done = false;

        // When
        testee.get(10, TimeUnit.MILLISECONDS);

        // Then
        verifyNoMoreInteractions(flowableAdaptor, flowInstanceRepository, retryExecutor, taskInstanceCreatedlistener,
                activeTaskStatisticsListener);
        assertTrue(testee.isDone());
        assertNull(testee.get());
        assertTrue(testee.isDone());
    }

    @Test
    public void testBindToFlowTaskInstance() throws Exception {
        // Given
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);

        // When
        testee.bindToFlowTaskInstance(flowTaskInstance);

        // Then
        verify(flowTaskInstance).bindServiceTask(testee);
        verifyNoMoreInteractions(flowableAdaptor, flowInstanceRepository, retryExecutor, taskInstanceCreatedlistener,
                activeTaskStatisticsListener);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        // Given
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "flowInstanceId", 1L);

        // When
        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Then
        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
        verifyNoMoreInteractions(flowableAdaptor, flowInstanceRepository, retryExecutor, taskInstanceCreatedlistener,
                activeTaskStatisticsListener);
    }
}