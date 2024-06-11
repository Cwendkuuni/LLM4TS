package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.core.shared.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptorTest;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String ACTIVITY_ID = "activityId";
    private static final String FLOW_INSTANCE_ID = "flowInstanceId";
    private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig();
    private static final FlowStatus FLOW_STATUS = FlowStatus.FLOW_STATUS_COMPLETED;

    private BaseRuntimeFlowableDelegate<Object> delegate;
    private DelegateExecution execution;
    private Callable<Object> callable;
    private Execution strategyConfigCallable;
    private FlowTaskInstance taskInstance;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        execution = mock(DelegateExecution.class);
        callable = mock(Callable.class);
        strategyConfigCallable = mock(Callable.class);
        taskInstance = mock(FlowTaskInstance.class);

        when(execution.getProcessDefinitionId()).thenReturn(PROCESS_DEFINITION_ID);
        when(execution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);

        delegate = new BaseRuntimeFlowableDelegate<>();
        delegate.init(eventPublisher);
        PreConditions.init(flowableAdaptor, flowInstanceRepository);
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
    }

    @Test
    public void testInitTaskInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID)).thenAnswer(new Answer<Optional<Long>>() {
            @Override
            public Optional<Long> answer(InvocationOnMock invocation) throws Throwable {
                return Optional.of(FLOW_INSTANCE_ID);
            }
        });

        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID)).thenReturn(taskInstance);

        delegate.init(execution);

        verify(flowableAdaptor, Mockito.times(2)).getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
        verify(flowableAdaptor, Mockito.times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
        verify(taskInstance, Mockito.times(1)).dealloc();

        assertSame(FLOW_INSTANCE_ID, delegate.getFlowInstanceId());
        assertSame(taskInstance, delegate.getTargetTaskInstance());
        assertSame(TASK_TYPE, delegate.getTaskType());
        assertSame(FLOW_INSTANCE_ID, delegate.getFlowInstanceId());
        assertSame(STRATEGY_CONFIG, delegate.getStrategyConfig());
    }

    @Test
    public void testRunWithCallableExecution() throws Exception {
        when(callable.call()).thenReturn(new Object());

        delegate.initCallable(execution)
                .call();

        verify(callable, Mockito.times(1)).call();
        assertTrue(delegate.isDone());
        assertNull(delegate.getThrown());
        assertSame(new Object(), delegate.get());
    }

    @Test
    public void testRunWithExecutionException() throws Exception {
        Exception exception = new Exception();
        when(callable.call()).thenThrow(exception);

        delegate.initCallable(execution)
                .call();

        verify(callable, Mockito.times(1)).call();
        assertTrue(delegate.isDone());
        assertSame(exception, delegate.getThrown());
        assertNull(delegate.get());
    }

    @Test
    public void testRunWithInterruptedException() throws Exception {
        when(callable.call()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new InterruptedException();
            }
        });

        delegate.initCallable(execution)
                .call();

        verify(callable, Mockito.times(1)).call();
        assertTrue(delegate.isDone());
        assertSame(InterruptedException.class, delegate.getThrown().getClass());
        assertNull(delegate.get());
    }

    @Test
    public void testRunWithTimeoutException() throws Exception {
        when(callable.call()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Thread.sleep(1001);
                return new Object();
            }
        });

        delegate.initCallable(execution)
                .call(500, TimeUnit.MILLISECONDS);

        verify(callable, Mockito.times(1)).call();
        assertTrue(delegate.isDone());
        assertSame(TimeoutException.class, delegate.getThrown().getClass());
        assertNull(delegate.get());
    }

    @Test
    public void testRunWithServiceTaskError() throws Exception {
        Exception exception = new Exception();
        when(callable.call()).thenThrow(exception);

        doCallRealMethod(() -> delegate.run(execution)).thenThrow(new ServiceTaskError(exception));

        delegate.initCallable(execution)
                .call();

        verify(callable, Mockito.times(1)).call();
        assertTrue(delegate.isDone());
        assertSame(exception, delegate.getThrown());
        assertNull(delegate.get());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, Mockito.times(1)).bindServiceTask(delegate);
        verify(eventPublisher, Mockito.times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(FLOW_INSTANCE_ID, FLOW_STATUS))
                .thenAnswer(new Answer<Boolean>() {
                    @Override
                    public Boolean answer(InvocationOnMock invocation) throws Throwable {
                        return true;
                    }
                });

        delegate.updateFlowInstanceStatus(FLOW_STATUS);

        verify(flowInstanceRepository, Mockito.times(1)).updateStatusById(FLOW_INSTANCE_ID, FLOW_STATUS);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testUpdateFlowInstanceStatusWithException() {
        when(flowInstanceRepository.updateStatusById(FLOW_INSTANCE_ID, FLOW_STATUS))
                .thenThrow(new Exception());

        delegate.updateFlowInstanceStatus(FLOW_STATUS);

        verify(flowInstanceRepository, Mockito.times(1)).updateStatusById(FLOW_INSTANCE_ID, FLOW_STATUS);
        assertTrue(delegate.isDone());
        assertSame(new Exception(), delegate.getThrown());
        assertNull(delegate.get());
    }

    private void doCallRealMethod(Method method) throws Throwable {
        method.setAccessible(true);
        method.invoke(delegate, execution);
    }
}