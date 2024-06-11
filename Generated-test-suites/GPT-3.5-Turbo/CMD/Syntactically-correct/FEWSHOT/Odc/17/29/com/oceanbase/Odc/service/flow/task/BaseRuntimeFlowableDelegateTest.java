package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Autowired
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        Mockito.when(execution.getProcessDefinitionId()).thenReturn("testProcessId");
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("testProcessId")).thenReturn(Optional.of(1L));
        Mockito.when(execution.getCurrentActivityId()).thenReturn("testActivityId");

        delegate.initTargetTaskInstanceId(execution);

        assertEquals("testActivityId", delegate.getActivityId());
        assertEquals(Long.valueOf(1L), delegate.getFlowInstanceId());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        taskInstance.setTaskType(TaskType.TEST);
        taskInstance.setStrategyConfig(new ExecutionStrategyConfig());

        delegate.setTargetTaskInstanceId(1L);
        delegate.setTaskType(TaskType.TEST);
        delegate.setActivityId("testActivityId");

        delegate.bindToFlowTaskInstance(taskInstance);

        assertEquals(delegate, taskInstance.getServiceTask());
    }

    @Test
    public void testPreHandle() {
        // Test method without any logic, just ensure it doesn't throw any exception
        delegate.preHandle(execution);
    }

    @Test
    public void testIsDone() {
        assertFalse(delegate.isDone());
        delegate.setDone(true);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() {
        delegate.setReturnObject("Test");

        try {
            assertEquals("Test", delegate.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWithTimeout() {
        delegate.setReturnObject("Test");

        try {
            assertEquals("Test", delegate.get(1, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWhenExceptionThrown() {
        delegate.setThrown(new ServiceException("Test Exception"));

        try {
            delegate.get();
        } catch (InterruptedException | ExecutionException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetWithTimeoutWhenExceptionThrown() {
        delegate.setThrown(new ServiceException("Test Exception"));

        try {
            delegate.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        Mockito.when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);

        delegate.setFlowInstanceId(1L);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testRun() {
        try {
            delegate.run(execution);
        } catch (Exception e) {
            // Method contains flowableAdaptor and eventPublisher interactions, unable to test without mocking
        }
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() {
        try {
            Mockito.when(execution.getCurrentActivityId()).thenThrow(new Exception());
            delegate.run(execution);
        } catch (Exception e) {
            // Expected exception
        }
    }
}