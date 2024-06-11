package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
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
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Tests for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    private DelegateExecution execution;
    private BaseRuntimeFlowableDelegate<String> delegate;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private CountDownLatch latch;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.execution = mock(DelegateExecution.class);
        this.delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected void preHandle(DelegateExecution execution) {}

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "result";
                    }
                };
            }
        };

        this.flowableAdaptor = mock(FlowableAdaptor.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.flowInstanceRepository = mock(FlowInstanceRepository.class);
        this.retryExecutor = mock(RetryExecutor.class);
        this.latch = new CountDownLatch(1);
        this.activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);

        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(true);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(123L));

        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        when(flowableAdaptor.getTaskInstanceById(anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);

        when(retryExecutor.run(any(Callable.class)))
                .thenAnswer(invocation -> invocation.getArgument(0).call());

        when(retryExecutor.retryIntervalMillis(anyInt()))
                .thenReturn(1000);

        when(retryExecutor.retryTimes(anyInt()))
                .thenReturn(3);

        this.delegate.init(eventPublisher);
    }

    @Test
    public void testRun() {
        this.delegate.run(this.execution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(flowableAdaptor, times(1)).getTaskInstanceById(anyLong());
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(this.delegate, times(1)).preHandle(this.execution);
        verify(this.delegate, times(1)).initCallable(this.execution);
        verify(this.delegate, times(1)).updateFlowInstanceStatus(eq(FlowStatus.COMPLETED));
        verify(this.latch, times(1)).await();

        ArgumentCaptor<ServiceTaskStartedEvent> eventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        ServiceTaskStartedEvent publishedEvent = eventCaptor.getValue();
        assertNotNull(publishedEvent);
        assertEquals(this.delegate, publishedEvent.getTask());
        assertEquals(Thread.currentThread(), publishedEvent.getThread());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        this.delegate.run(this.execution);
        this.latch.await();

        assertEquals("result", this.delegate.get());
        assertEquals(this.delegate.get(), this.delegate.get());
        assertEquals(this.delegate.get(), this.delegate.get(0, TimeUnit.MILLISECONDS));
        assertEquals(this.delegate.get(), this.delegate.get(100, TimeUnit.MILLISECONDS));

        verify(this.delegate, times(1)).isDone();
        verify(this.delegate, times(1)).get();
        verify(this.delegate, times(1)).get(anyLong(), any(TimeUnit.class));
        verify(this.delegate, times(1)).get(anyLong(), any(TimeUnit.class), any(TimeoutException.class));
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        this.delegate.run(this.execution);
        this.latch.await();

        assertTrue(this.delegate.isDone());
        assertFalse(this.delegate.isDone());
        this.delegate.done = false;
        assertFalse(this.delegate.isDone());
        this.delegate.done = true;
        assertTrue(this.delegate.isDone());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunException() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        this.delegate.run(this.execution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(this.delegate, never()).preHandle(any());
        verify(this.delegate, never()).initCallable(any());
        verify(this.delegate, never()).updateFlowInstanceStatus(eq(FlowStatus.COMPLETED));
        verify(this.latch, never()).await();

        ArgumentCaptor<ServiceTaskError> errorCaptor = ArgumentCaptor.forClass(ServiceTaskError.class);
        verify(eventPublisher, times(1)).handleEvent(errorCaptor.capture());
        assertEquals("Test exception", errorCaptor.getValue().getCause().getMessage());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        this.delegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(this.delegate);
        verify(this.delegate, times(1)).updateFlowInstanceStatus(eq(FlowStatus.STARTED));
    }

}