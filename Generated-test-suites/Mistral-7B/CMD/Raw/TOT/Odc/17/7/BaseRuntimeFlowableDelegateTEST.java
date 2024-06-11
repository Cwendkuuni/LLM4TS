package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Unit tests for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String ACTIVITY_ID = "activityId";
    private static final long FLOW_INSTANCE_ID = 1L;
    private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig();
    private static final FlowTaskInstance FLOW_TASK_INSTANCE = new FlowTaskInstance();

    private BaseRuntimeFlowableDelegate<Void> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private FlowableAdaptor flowableAdaptor;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Before
    public void setUp() {
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        activeTaskStatisticsListener = Mockito.spy(new ActiveTaskStatisticsListener(delegate));
        taskInstanceCreatedlistener = new TaskInstanceCreatedListener(delegate);
        delegate = new BaseRuntimeFlowableDelegate<Void>() {
            @Override
            protected Callable<Void> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
                // no-op
            }
        };
        delegate.init(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);
        delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);

        execution = Mockito.mock(DelegateExecution.class);
    }

    @After
    public void tearDown() {
        reset(eventPublisher, flowInstanceRepository, flowableAdaptor, activeTaskStatisticsListener,
                taskInstanceCreatedlistener);
    }

    @Test
    public void testRun_withSuccess() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenReturn(Optional.of(FLOW_INSTANCE_ID));

        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(FLOW_TASK_INSTANCE));

        delegate.bindToFlowTaskInstance(FLOW_TASK_INSTANCE);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        delegate.run(execution);

        verify(flowInstanceRepository, times(1)).updateStatusById(FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
    }

    @Test
    public void testRun_withException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenThrow(new RuntimeException("Exception occurred"));

        try {
            delegate.run(execution);
            fail("Expected an exception to be thrown");
        } catch (ServiceTaskError e) {
            verify(eventPublisher, times(1)).publishEvent(Mockito.any(ServiceTaskStartedEvent.class));
            verifyNoMoreInteractions(flowInstanceRepository);
        }
    }

    @Test
    public void testGet_whenDone() throws InterruptedException, ExecutionException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenReturn(Optional.of(FLOW_INSTANCE_ID));

        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(FLOW_TASK_INSTANCE));

        delegate.bindToFlowTaskInstance(FLOW_TASK_INSTANCE);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        delegate.run(execution);

        Future<Void> future = delegate.get();
        Void result = future.get();

        verify(flowInstanceRepository, times(1)).updateStatusById(FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
        assertNotNull(result);
    }

    @Test
    public void testGet_whenNotDone() throws InterruptedException, ExecutionException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenReturn(Optional.of(FLOW_INSTANCE_ID));

        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(FLOW_TASK_INSTANCE));

        delegate.bindToFlowTaskInstance(FLOW_TASK_INSTANCE);

        Future<Void> future = delegate.get();

        assertFalse(future.isDone());
    }

    @Test
    public void testGet_withTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenReturn(Optional.of(FLOW_INSTANCE_ID));

        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(FLOW_TASK_INSTANCE));

        delegate.bindToFlowTaskInstance(FLOW_TASK_INSTANCE);

        Future<Void> future = delegate.get(1, TimeUnit.SECONDS);

        assertFalse(future.isDone());
        assertThrows(TimeoutException.class, () -> future.get());
    }
}