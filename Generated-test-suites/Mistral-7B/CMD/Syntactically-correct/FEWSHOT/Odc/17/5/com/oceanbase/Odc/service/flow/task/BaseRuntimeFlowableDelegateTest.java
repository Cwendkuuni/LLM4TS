package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
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
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    /**
     * Test initialization
     */
    @Test
    public void testInitialization() {
        BaseRuntimeFlowableDelegate<String> delegate = Mockito.spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }
        });

        assertNotNull(delegate.getLatch());
        assertNotNull(delegate.getRetryExecutor());
        assertNotNull(delegate.getTaskInstanceCreatedlistener());
        assertNotNull(delegate.getActiveTaskStatisticsListener());
    }

    /**
     * Test initialization with EventPublisher
     */
    @Test
    public void testInitializationWithEventPublisher() {
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }
        };

        delegate.init(eventPublisher);

        verify(eventPublisher).addEventListener(delegate.getActiveTaskStatisticsListener());
        verify(eventPublisher).addEventListener(delegate.getTaskInstanceCreatedlistener());
    }

    /**
     * Test get method
     */
    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = Mockito.spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                Callable<String> callable = Mockito.mock(Callable.class);
                when(callable.call()).thenReturn("result");
                return callable;
            }
        });

        CountDownLatch latch = delegate.getLatch();
        latch.await();

        assertEquals("result", delegate.get());
    }

    /**
     * Test get with timeout
     */
    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = Mockito.spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }
        });

        delegate.get(1, TimeUnit.SECONDS);
    }

    /**
     * Test isDone method
     */
    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<String> delegate = Mockito.spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return null;
            }
        });

        assertFalse(delegate.isDone());

        delegate.setDone(true);

        assertTrue(delegate.isDone());
    }

    /**
     * Test updateFlowInstanceStatus method
     */
    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }
        };

        delegate.setFlowInstanceRepository(flowInstanceRepository);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    /**
     * Test initTaskInstanceId method
     */
    @Test
    public void testInitTaskInstanceId() {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }
        };

        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");

        delegate.initTargetTaskInstanceId(execution);

        assertEquals("processDefinitionId", delegate.getFlowInstanceId());

        String activityId = "activityId";
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(taskInstance);

        delegate.bindToFlowTaskInstance(taskInstance);

        assertEquals(1L, delegate.getFlowInstanceId());
        assertEquals("activityId", delegate.getActivityId());
        assertEquals(TaskType.TASK_TYPE_1, delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
    }

}