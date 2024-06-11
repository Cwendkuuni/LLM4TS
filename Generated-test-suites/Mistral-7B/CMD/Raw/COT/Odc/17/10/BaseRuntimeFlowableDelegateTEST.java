package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @SuppressWarnings("unchecked")
    private static final BaseRuntimeFlowableDelegate<Object> TEST_INSTANCE = Mockito.spy(new BaseRuntimeFlowableDelegate<Object>() {
        @Override
        protected Callable<Object> initCallable(DelegateExecution execution) {
            return Mockito.mock(Callable.class);
        }
    });

    /**
     * Test method for constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        assertNotNull(TEST_INSTANCE);
    }

    /**
     * Test method for getActivityId.
     */
    @org.junit.Test
    public void testGetActivityId() {
        String activityId = TEST_INSTANCE.getActivityId();
        assertNull(activityId);

        TEST_INSTANCE.activityId = "test_activityId";
        assertEquals("test_activityId", TEST_INSTANCE.getActivityId());
    }

    /**
     * Test method for getTargetTaskInstanceId.
     */
    @org.junit.Test
    public void testGetTargetTaskInstanceId() {
        assertNull(TEST_INSTANCE.getTargetTaskInstanceId());
    }

    /**
     * Test method for getTaskType.
     */
    @org.junit.Test
    public void testGetTaskType() {
        assertNull(TEST_INSTANCE.getTaskType());
    }

    /**
     * Test method for getFlowInstanceId.
     */
    @org.junit.Test
    public void testGetFlowInstanceId() {
        assertNull(TEST_INSTANCE.getFlowInstanceId());
    }

    /**
     * Test method for getStrategyConfig.
     */
    @org.junit.Test
    public void testGetStrategyConfig() {
        assertNull(TEST_INSTANCE.getStrategyConfig());
    }

    /**
     * Test method for initCallable.
     * @throws Exception
     */
    @org.junit.Test(expected = Exception.class)
    public void testInitCallableThrowsException() throws Exception {
        when(TEST_INSTANCE.initCallable(Mockito.any(DelegateExecution.class))).thenThrow(new Exception());
        TEST_INSTANCE.run(Mockito.mock(DelegateExecution.class));
    }

    /**
     * Test method for run.
     * @throws Exception
     */
    @org.junit.Test
    public void testRun() throws Exception {
        Callable<Object> callable = Mockito.mock(Callable.class);
        when(TEST_INSTANCE.initCallable(Mockito.any(DelegateExecution.class))).thenReturn(callable);

        ServiceTaskStartedEvent event = Mockito.mock(ServiceTaskStartedEvent.class);
        TEST_INSTANCE.eventPublisher = Mockito.mock(EventPublisher.class);
        when(TEST_INSTANCE.eventPublisher.publishEvent(Mockito.any(ServiceTaskStartedEvent.class))).thenReturn(event);

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        TEST_INSTANCE.run(execution);

        verify(TEST_INSTANCE.eventPublisher).publishEvent(event);
    }

    /**
     * Test method for get.
     */
    @org.junit.Test
    public void testGet() {
        CountDownLatch latch = new CountDownLatch(1);
        TEST_INSTANCE.latch = latch;

        TEST_INSTANCE.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));
        TEST_INSTANCE.preHandle(Mockito.mock(DelegateExecution.class));

        try {
            TEST_INSTANCE.get();
            fail("Should throw InterruptedException or ExecutionException");
        } catch (InterruptedException e) {
            // Expected
        } catch (ExecutionException e) {
            assertNull(e.getCause());
        }

        latch.countDown();
    }

    /**
     * Test method for get with timeout.
     */
    @org.junit.Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch latch = new CountDownLatch(1);
        TEST_INSTANCE.latch = latch;

        TEST_INSTANCE.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));
        TEST_INSTANCE.preHandle(Mockito.mock(DelegateExecution.class));

        TEST_INSTANCE.get(1, TimeUnit.SECONDS);
    }

    /**
     * Test method for get done status.
     */
    @org.junit.Test
    public void testIsDone() {
        assertFalse(TEST_INSTANCE.isDone());

        TEST_INSTANCE.done = true;
        assertTrue(TEST_INSTANCE.isDone());
    }

    /**
     * Test method for initTargetTaskInstanceId.
     */
    @org.junit.Test
    public void testInitTargetTaskInstanceId() {
        TEST_INSTANCE.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));

        assertNotNull(TEST_INSTANCE.getFlowInstanceId());
        assertNotNull(TEST_INSTANCE.getTargetTaskInstanceId());
        assertNotNull(TEST_INSTANCE.getTaskType());
        assertNotNull(TEST_INSTANCE.getStrategyConfig());
    }

    /**
     * Test method for bindToFlowTaskInstance.
     */
    @org.junit.Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        TEST_INSTANCE.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).bindServiceTask(TEST_INSTANCE);
    }

    /**
     * Test method for updateFlowInstanceStatus.
     */
    @org.junit.Test
    public void testUpdateFlowInstanceStatus() {
        TEST_INSTANCE.updateFlowInstanceStatus(FlowStatus.STARTED);
        verify(TEST_INSTANCE.retryExecutor).run(() -> TEST_INSTANCE.flowInstanceRepository.updateStatusById(TEST_INSTANCE.getFlowInstanceId(), FlowStatus.STARTED), Objects::nonNull);
    }
}