package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableTestUtil;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.test.BaseRuntimeFlowableDelegateTest;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest extends BaseRuntimeFlowableDelegate {

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String ACTIVITY_ID = "activityId";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TARGET_TASK_INSTANCE_ID = 1L;
    private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig();
    private static final FlowInstanceRepository FLOW_INSTANCE_REPOSITORY = Mockito.mock(FlowInstanceRepository.class);
    private static final EventPublisher EVENT_PUBLISHER = Mockito.mock(EventPublisher.class);
    private static final FlowableAdaptor FLOWABLE_ADAPTOR = Mockito.mock(FlowableAdaptor.class);
    private static final RetryExecutor RETRY_EXECUTOR = RetryExecutor.builder().build();
    private static final CountDownLatch LATCH = new CountDownLatch(1);
    private static final ActiveTaskStatisticsListener ACTIVE_TASK_STATISTICS_LISTENER = Mockito.mock(ActiveTaskStatisticsListener.class);
    private static final TaskInstanceCreatedListener TASK_INSTANCE_CREATED_LISTENER = Mockito.mock(TaskInstanceCreatedListener.class);
    private BaseRuntimeFlowableDelegate<Object> delegate;
    private ProcessInstance processInstance;
    private Callable<Object> callable;

    @Before
    public void setUp() {
        delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected void initTargetTaskInstanceId(DelegateExecution execution) {
                super.initTargetTaskInstanceId(execution);
                PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "flowInstanceId", FLOW_INSTANCE_ID,
                        () -> FLOW_INSTANCE_REPOSITORY.findById(FLOW_INSTANCE_ID));
            }

            @Override
            protected void bindToFlowTaskInstance(FlowTaskInstance taskInstance) {
                super.bindToFlowTaskInstance(taskInstance);
            }

            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                callable = Mockito.mock(Callable.class);
                return callable;
            }
        };

        delegate.setFlowableAdaptor(FLOWABLE_ADAPTOR);
        delegate.setEventPublisher(EVENT_PUBLISHER);
        delegate.setFlowInstanceRepository(FLOW_INSTANCE_REPOSITORY);
        delegate.setRetryExecutor(RETRY_EXECUTOR);
        delegate.setActiveTaskStatisticsListener(ACTIVE_TASK_STATISTICS_LISTENER);
        delegate.setTaskInstanceCreatedlistener(TASK_INSTANCE_CREATED_LISTENER);

        delegate.init(EVENT_PUBLISHER);

        processInstance = FlowableTestUtil.createProcessInstance(PROCESS_DEFINITION_ID);
    }

    @After
    public void tearDown() {
        LATCH.countDown();
        FLOWABLE_ADAPTOR.deleteProcessInstance(processInstance.getId());
    }

    @Test
    public void testRun() throws Exception {
        when(FLOWABLE_ADAPTOR.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(FLOW_INSTANCE_ID);
        when(FLOWABLE_ADAPTOR.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID)).thenReturn(Optional.of(new FlowTaskInstance()));

        delegate.run(Mockito.mock(DelegateExecution.class));

        verify(EVENT_PUBLISHER, times(1)).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        when(FLOWABLE_ADAPTOR.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID)).thenThrow(new RuntimeException());

        delegate.run(Mockito.mock(DelegateExecution.class));

        verify(EVENT_PUBLISHER, times(1)).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));

        fail("Expected ServiceTaskError to be thrown");
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));

        verify(FLOW_INSTANCE_REPOSITORY, times(1)).findById(FLOW_INSTANCE_ID);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenReturn(new Object());

        delegate.get();

        assertTrue(delegate.isDone());
        assertNull(delegate.getThrown());
        assertNotNull(delegate.getReturnObject());

        LATCH.await();
        verify(ACTIVE_TASK_STATISTICS_LISTENER, times(1)).onStatisticsChange(any());
        verify(TASK_INSTANCE_CREATED_LISTENER, times(1)).onTaskCreated(any());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenReturn(new Object());

        LATCH.countDown();
        delegate.get();

        fail("Expected InterruptedException to be thrown");
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenThrow(new ExecutionException("Test ExecutionException"));

        delegate.get();

        fail("Expected ExecutionException to be thrown");
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.get(1, TimeUnit.SECONDS);

        fail("Expected TimeoutException to be thrown");
    }
}