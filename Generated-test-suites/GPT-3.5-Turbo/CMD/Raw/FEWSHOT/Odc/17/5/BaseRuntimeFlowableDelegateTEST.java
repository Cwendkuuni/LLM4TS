package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private DelegateExecution execution;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

        when(flowableAdaptor.getTaskInstanceByActivityId("TestActivity", 1L)).thenReturn(Optional.of(flowTaskInstance));
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("TestDefinitionId")).thenReturn(Optional.of(Optional.of(1L)));

        delegate.initTargetTaskInstanceId(execution);

        assertEquals("TestActivity", delegate.getActivityId());
        assertEquals(1L, delegate.getTargetTaskInstanceId().longValue());
        assertEquals(TaskType.TASK, delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
        assertEquals(1L, delegate.getFlowInstanceId().longValue());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        delegate.setTargetTaskInstanceId(1L);
        delegate.setActivityId("TestActivity");

        delegate.bindToFlowTaskInstance(taskInstance);

        assertEquals(delegate.getTargetTaskInstanceId(), taskInstance.getId());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.setFlowInstanceId(1L);

        FlowStatus flowStatus = FlowStatus.STARTED;
        delegate.updateFlowInstanceStatus(flowStatus);

        // Verify the update has been called and successful
    }

    @Test
    public void testExecute() {
        delegate.execute(execution);

        // Verify event listeners have been added and removed
    }

    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());

        delegate.setDone(true);

        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.setReturnObject("Test Result");

        assertEquals("Test Result", delegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.setReturnObject("Test Result");

        assertEquals("Test Result", delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws InterruptedException, ExecutionException {
        delegate.setThrown(new Exception());

        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.setDone(false);

        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = delegate.initCallable(execution);

        assertNotNull(callable);
        try {
            String result = callable.call();
            assertEquals("Test Callable", result);
        } catch (Exception e) {
            fail("Exception thrown during callable execution");
        }
    }

    @Test
    public void testRun() throws Exception {
        delegate.run(execution);

        // Verify the execution of run method
    }

    @Test
    public void testPreHandle() {
        // Test case for preHandle method
    }
}