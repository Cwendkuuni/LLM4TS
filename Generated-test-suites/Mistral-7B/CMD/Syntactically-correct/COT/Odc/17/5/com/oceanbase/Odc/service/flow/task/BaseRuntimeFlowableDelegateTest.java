package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.FlowableRuntimeException;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

/**
 * Test cases for BaseRuntimeFlowableDelegate class
 *
 * @author yh263208
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor;

    @Captor
    private ArgumentCaptor<ActiveTaskStatisticsListener> activeTaskStatisticsListenerCaptor;

    @Captor
    private ArgumentCaptor<TaskInstanceCreatedListener> taskInstanceCreatedlistenerCaptor;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "Result";
                    }
                };
            }
        };
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.flowableAdaptor = flowableAdaptor;
    }

    @Test
    public void testConstructor() {
        assertNotNull(delegate);
        assertNull(delegate.returnObject);
        assertNull(delegate.thrown);
        assertNull(delegate.done);
        assertNotNull(delegate.latch);
        assertNotNull(delegate.retryExecutor);
        assertNotNull(delegate.taskInstanceCreatedlistener);
        assertNotNull(delegate.activeTaskStatisticsListener);
    }

    @Test(expected = ServiceTaskError.class)
    public void testInitializationTaskFailed() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any(String.class))).thenThrow(new Exception());
        delegate.run(mock(DelegateExecution.class));
    }

    @Test
    public void testRunMethod() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any(String.class))).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(String.class), anyLong())).thenReturn(mock(FlowTaskInstance.class));
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(true);
        delegate.run(mock(DelegateExecution.class));
        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        ServiceTaskStartedEvent event = serviceTaskStartedEventCaptor.getValue();
        assertNotNull(event);
        assertEquals(delegate, event.getDelegate());
        assertEquals(Thread.currentThread(), event.getThread());
    }

    @Test
    public void testGetMethod() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any(String.class))).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(String.class), anyLong())).thenReturn(mock(FlowTaskInstance.class));
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(true);
        T result = delegate.get();
        assertNotNull(result);
        assertEquals("Result", result);
        verify(delegate.latch).await();
    }

    @Test(expected = InterruptedException.class)
    public void testGetMethodInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any(String.class))).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(String.class), anyLong())).thenReturn(mock(FlowTaskInstance.class));
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(true);
        delegate.latch.countDown();
        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetMethodExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any(String.class))).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(String.class), anyLong())).thenThrow(new Exception());
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(true);
        delegate.run(mock(DelegateExecution.class));
        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetMethodTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any(String.class))).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(String.class), anyLong())).thenReturn(mock(FlowTaskInstance.class));
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(true);
        delegate.latch.countDown();
        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testIsDoneMethod() throws Exception {
        assertFalse(delegate.isDone());
        delegate.run(mock(DelegateExecution.class));
        assertTrue(delegate.isDone());
    }

    @Test
    public void testPreHandleMethod() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.preHandle(execution);
    }

    @Test
    public void testExecuteMethod() throws Exception {
        delegate.execute(mock(DelegateExecution.class));
    }

    @Test
    public void testUpdateFlowInstanceStatusMethod() throws Exception {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(delegate.flowInstanceId, FlowStatus.COMPLETED);
    }

    @Test(expected = ServiceTaskError.class)
    public void testUpdateFlowInstanceStatusMethodException() throws Exception {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenThrow(new FlowableRuntimeException());
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

    @Test
    public void testInitCallableMethod() {
        Callable<String> callable = delegate.initCallable(mock(DelegateExecution.class));
        assertNotNull(callable);
    }

    @Test
    public void testBindToFlowTaskInstanceMethod() throws Exception {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).bindServiceTask(delegate);
    }
}