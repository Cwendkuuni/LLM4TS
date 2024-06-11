package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Test
    public void testInitCallable() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);
        assertNotNull(callable);
    }

    @Test
    public void testRun() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertEquals("activityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(TaskType.TASK_TYPE, baseRuntimeFlowableDelegate.getTaskType());
        assertEquals(1L, baseRuntimeFlowableDelegate.getFlowInstanceId().longValue());
        assertEquals(ExecutionStrategyConfig.DEFAULT_CONFIG, baseRuntimeFlowableDelegate.getStrategyConfig());
        assertNotNull(baseRuntimeFlowableDelegate.getEventPublisher());
        assertNotNull(baseRuntimeFlowableDelegate.getFlowableAdaptor());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    Thread.sleep(1000);
                    return "Test";
                };
            }
        };

        Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        baseRuntimeFlowableDelegate.run(delegateExecution);

        baseRuntimeFlowableDelegate.get(500, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        baseRuntimeFlowableDelegate.setTargetTaskInstanceId(1L);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        assertNotNull(taskInstance.getServiceTask());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        baseRuntimeFlowableDelegate.setFlowInstanceId(1L);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        Mockito.verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }
}