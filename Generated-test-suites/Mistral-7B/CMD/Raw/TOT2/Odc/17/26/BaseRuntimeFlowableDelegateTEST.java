package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
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
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private DelegateExecution execution;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.taskInstanceCreatedlistener = taskInstanceCreatedlistener;
        delegate.activeTaskStatisticsListener = activeTaskStatisticsListener;
    }

    @Test
    public void testInitialize() {
        delegate.init(eventPublisher);
        verify(eventPublisher).addEventListener(eq(taskInstanceCreatedlistener));
        verify(eventPublisher).addEventListener(eq(activeTaskStatisticsListener));
    }

    @Test
    public void testRun_Success() throws Exception {
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), 1L)).thenReturn(mock(FlowTaskInstance.class));

        delegate.run(execution);

        verify(execution, times(1)).getCurrentActivityId();
        verify(execution, times(1)).getProcessDefinitionId();
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), 1L);
        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
        verify(callable, times(1)).call();
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_Failure() throws Exception {
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException());
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), 1L)).thenReturn(mock(FlowTaskInstance.class));

        delegate.run(execution);

        verify(execution, times(1)).getCurrentActivityId();
        verify(execution, times(1)).getProcessDefinitionId();
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), 1L);
        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
        verify(callable, times(1)).call();
        verify(logger, times(1)).warn(anyString(), any(Throwable.class));
        verify(execution, times(1)).setVariable("thrown", eq(new ServiceTaskError(any(Throwable.class))));
        verify(execution, times(1)).setVariable("done", eq(true));
        verify(latch, times(1)).countDown();
        throw new ServiceTaskError(any(Throwable.class));
    }

    @Test
    public void testGet_Success() throws InterruptedException, ExecutionException {
        when(latch.getCount()).thenReturn(0);
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");

        delegate.get();

        verify(callable, times(1)).call();
        verify(latch, times(1)).await();
        verify(thrown, nullValue());
        verify(returnObject, eq("result"));
    }

    @Test(expected = InterruptedException.class)
    public void testGet_InterruptedException() throws InterruptedException, ExecutionException {
        when(latch.getCount()).thenReturn(0);
        Callable<String> callable = mock(Callable.class);

        delegate.get();

        verify(callable, times(1)).call();
        verify(latch, times(1)).await();
        verify(thrown, nullValue());
        verify(returnObject, nullValue());
        throw new InterruptedException();
    }

    @Test(expected = ExecutionException.class)
    public void testGet_ExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        when(latch.getCount()).thenReturn(0);
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException());

        delegate.get();

        verify(callable, times(1)).call();
        verify(latch, times(1)).await();
        verify(thrown, instanceOf(RuntimeException.class));
        verify(returnObject, nullValue());
        throw new ExecutionException(any(Throwable.class));
    }

    @Test(expected = TimeoutException.class)
    public void testGet_TimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        when(latch.getCount()).thenReturn(0);
        Callable<String> callable = mock(Callable.class);

        delegate.get(1, TimeUnit.MILLISECONDS);

        verify(callable, times(1)).call();
        verify(latch, times(1)).await(eq(1), eq(TimeUnit.MILLISECONDS));
        verify(thrown, instanceOf(TimeoutException.class));
        verify(returnObject, nullValue());
        throw new TimeoutException();
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        when(taskInstance.getId()).thenReturn(1L);
        when(taskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(eq(delegate));
        verify(logger).info("Monitor task instance creation and bind tasks, taskInstanceId=1L, activityId={}", delegate.getActivityId());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        Optional<Boolean> result = Optional.of(true);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(retryExecutor).run(() -> {
            try {
                return flowInstanceRepository.updateStatusById(eq(delegate.getFlowInstanceId()), any(FlowStatus.class));
            } catch (Exception e) {
                return null;
            }
        }, eq(result));
    }

    @Test
    public void testInitCallable() {
        // Test initialization of callable should be implemented in subclasses
        assertNull(delegate.initCallable(execution));
    }
}