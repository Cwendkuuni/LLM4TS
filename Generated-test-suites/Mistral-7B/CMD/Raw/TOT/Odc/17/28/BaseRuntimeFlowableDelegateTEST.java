package com.oceanbase.odc.service.flow.test;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> delegate;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private DelegateExecution execution;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = delegate.initCallable(execution);
        verify(flowableAdaptor, times(2)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        assertEquals(Callable.class, callable.getClass());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testRun() throws Exception {
        Callable<String> callable = delegate.initCallable(execution);
        doNothing().when(delegate).preHandle(execution);
        when(callable.call()).thenReturn("result");
        when(eventPublisher.publishEvent(any())).thenReturn(null);

        delegate.run(execution);

        verify(execution, times(1)).setVariable("returnObject", eq("result"));
        verify(eventPublisher, times(1)).publishEvent(any());
        verify(delegate, times(1)).done = true;
    }

    @Test
    public void testException() throws Exception {
        Callable<String> callable = delegate.initCallable(execution);
        doThrow(new RuntimeException("Test Exception")).when(callable);

        delegate.run(execution);

        verify(execution, times(1)).setVariable("thrown", eq(new RuntimeException("Test Exception")));
        verify(delegate, times(1)).done = true;
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    public void testTimeout() throws Exception {
        Callable<String> callable = delegate.initCallable(execution);
        doThrow(new TimeoutException("Test Timeout")).when(callable);

        delegate.run(execution);

        verify(execution, times(1)).setVariable("thrown", eq(new TimeoutException("Test Timeout")));
        verify(delegate, times(1)).done = true;
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    public void testInterruptedException() throws Exception {
        Callable<String> callable = delegate.initCallable(execution);
        doThrow(new InterruptedException("Test InterruptedException")).when(callable);

        delegate.run(execution);

        verify(execution, times(1)).setVariable("thrown", eq(new InterruptedException("Test InterruptedException")));
        verify(delegate, times(1)).done = true;
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    public void testExecutionException() throws Exception {
        Callable<String> callable = delegate.initCallable(execution);
        doThrow(new ExecutionException(new RuntimeException("Test ExecutionException"))).when(callable);

        delegate.run(execution);

        verify(execution, times(1)).setVariable("thrown", eq(new ExecutionException(new RuntimeException("Test ExecutionException"))));
        verify(delegate, times(1)).done = true;
        verify(eventPublisher, never()).publishEvent(any());
    }
}