package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;

/**
 * Test for BaseRuntimeFlowableDelegate class
 *
 * @author yh263208
 * @date 2023-03-27 15:02
 */
public class BaseRuntimeFlowableDelegateTest extends BaseFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> testDelegate;
    private ExecutorService executorService;

    @Before
    public void setUp() {
        testDelegate = spy(new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return mock(Callable.class);
            }
        });
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        executorService = Executors.newSingleThreadExecutor();
        testDelegate.retryExecutor = RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build();
        testDelegate.latch = new CountDownLatch(1);
        testDelegate.taskInstanceCreatedlistener = new TaskInstanceCreatedListener(testDelegate);
        testDelegate.activeTaskStatisticsListener = new ActiveTaskStatisticsListener(testDelegate);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(new Object());
        when(eventPublisher.removeEventListener(eq(taskInstanceCreatedlistener)))
                .thenReturn(true);
        when(eventPublisher.removeEventListener(eq(activeTaskStatisticsListener)))
                .thenReturn(true);
        when(flowInstanceRepository.updateStatusById(anyLong(), eq(FlowStatus.FAILED)))
                .thenReturn(true);
        testDelegate.init(eventPublisher);
    }

    @After
    public void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    public void testInit() {
        assertEquals("", testDelegate.getActivityId());
        assertEquals(0L, testDelegate.getTargetTaskInstanceId());
        assertEquals(TaskType.UNDEFINED, testDelegate.getTaskType());
        assertEquals(0L, testDelegate.getFlowInstanceId());
        assertNull(testDelegate.getStrategyConfig());
        assertNull(testDelegate.getReturnObject());
        assertNull(testDelegate.getThrown());
        assertFalse(testDelegate.isDone());
        assertEquals(1, testDelegate.latch.getCount());
        verifyNoMoreInteractions(testDelegate.flowableAdaptor, testDelegate.eventPublisher, testDelegate.flowInstanceRepository);
    }

    @Test
    public void testRunWithSuccess() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), eq(1L)))
                .thenReturn(mock(FlowTaskInstance.class));

        Callable<String> callable = mock(Callable.class);
        testDelegate.preHandle(mock(DelegateExecution.class));
        String result = testDelegate.call(mock(DelegateExecution.class));
        verify(callable).call();
        verify(testDelegate).updateFlowInstanceStatus(eq(FlowStatus.COMPLETED));
        assertEquals("success", result);
        assertTrue(testDelegate.isDone());
        assertEquals(0, testDelegate.latch.getCount());
    }

    @Test
    public void testRunWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("Initialization task failed"));

        Callable<String> callable = mock(Callable.class);
        testDelegate.preHandle(mock(DelegateExecution.class));
        try {
            testDelegate.call(mock(DelegateExecution.class));
            fail();
        } catch (ServiceTaskError e) {
            assertEquals("Initialization task failed", e.getMessage());
            verify(callable, never()).call();
            verify(testDelegate).updateFlowInstanceStatus(eq(FlowStatus.FAILED));
        }
        assertTrue(testDelegate.isDone());
        assertEquals(0, testDelegate.latch.getCount());
    }

    @Test
    public void testExecute() {
        testDelegate.execute(mock(DelegateExecution.class));
        verify(testDelegate).execute(any(DelegateExecution.class));
        verify(testDelegate).removeEventListener(eq(taskInstanceCreatedlistener));
        verify(testDelegate).removeEventListener(eq(activeTaskStatisticsListener));
    }

    @Test
    public void testIsDone() {
        assertFalse(testDelegate.isDone());
        testDelegate.done = true;
        assertTrue(testDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        testDelegate.get();
        assertTrue(testDelegate.isDone());
        assertNull(testDelegate.getThrown());
        assertNotNull(testDelegate.getReturnObject());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException, TimeoutException {
        testDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        testDelegate.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        testDelegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance).bindServiceTask(any(BaseRuntimeFlowableDelegate.class));
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        testDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(eq(1L), eq(FlowStatus.COMPLETED));
    }
}