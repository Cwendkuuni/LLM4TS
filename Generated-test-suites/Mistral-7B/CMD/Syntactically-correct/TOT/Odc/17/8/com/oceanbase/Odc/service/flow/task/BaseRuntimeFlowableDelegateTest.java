package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.FlowStatus;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.TestUtils;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.test.BaseFlowableTest;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest extends BaseFlowableTest {

    @MockBean
    private EventPublisher eventPublisher;

    @MockBean
    private FlowInstanceRepository flowInstanceRepository;

    @MockBean
    private FlowableAdaptor flowableAdaptor;

    @Autowired
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @MockBean
    private RetryExecutor retryExecutor;

    @MockBean
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @MockBean
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setUp() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Mockito.mock(FlowTaskInstance.class));
    }

    @Test
    public void testInitTask() throws Exception {
        DelegateExecution execution = TestUtils.createExecution();

        Callable<Object> callable = mock(Callable.class);
        delegate.initCallable(execution) = callable;

        delegate.init(eventPublisher);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = TestUtils.createExecution();

        Callable<Object> callable = mock(Callable.class);
        delegate.initCallable(execution) = callable;

        delegate.init(eventPublisher);

        when(callable.call()).thenReturn(TestUtils.getDefaultObject());

        delegate.run(execution);

        verify(callable, times(1)).call();
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGet() throws Exception {
        DelegateExecution execution = TestUtils.createExecution();

        Callable<Object> callable = mock(Callable.class);
        delegate.initCallable(execution) = callable;

        delegate.init(eventPublisher);

        when(callable.call()).thenReturn(TestUtils.getDefaultObject());

        Future<Object> future = delegate.get();

        assertEquals(TestUtils.getDefaultObject(), future.get());
        verify(callable, times(1)).call();
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        DelegateExecution execution = TestUtils.createExecution();

        Callable<Object> callable = mock(Callable.class);
        delegate.initCallable(execution) = callable;

        delegate.init(eventPublisher);

        when(callable.call()).thenReturn(TestUtils.getDefaultObject());

        ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEventCaptor.capture());

        Object defaultObject = TestUtils.getDefaultObject();
        ServiceTaskStartedEvent event = serviceTaskStartedEventCaptor.getValue();
        assertEquals(defaultObject, event.getServiceTask());

        Future<Object> future = delegate.get(1, TimeUnit.SECONDS);

        assertEquals(defaultObject, future.get());
        verify(callable, times(1)).call();
        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEventCaptor.capture());
        assertEquals(defaultObject, serviceTaskStartedEventCaptor.getValue().getServiceTask());
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGetWithTimeoutException() throws Exception {
        DelegateExecution execution = TestUtils.createExecution();

        Callable<Object> callable = mock(Callable.class);
        delegate.initCallable(execution) = callable;

        delegate.init(eventPublisher);

        when(callable.call()).thenThrow(new RuntimeException("Task exception"));

        ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEventCaptor.capture());

        Object defaultObject = TestUtils.getDefaultObject();
        ServiceTaskStartedEvent event = serviceTaskStartedEventCaptor.getValue();
        assertEquals(defaultObject, event.getServiceTask());

        try {
            delegate.get(1, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            // expected
        }
        verify(callable, never()).call();
        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEventCaptor.capture());
        assertEquals(defaultObject, serviceTaskStartedEventCaptor.getValue().getServiceTask());
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testInterruptedException() throws Exception {
        DelegateExecution execution = TestUtils.createExecution();

        Callable<Object> callable = mock(Callable.class);
        delegate.initCallable(execution) = callable;

        delegate.init(eventPublisher);

        when(callable.call()).thenReturn(TestUtils.getDefaultObject());

        Thread thread = new Thread(() -> delegate.get());
        thread.interrupt();

        verify(callable, never()).call();
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testDone() throws Exception {
        DelegateExecution execution = TestUtils.createExecution();

        Callable<Object> callable = mock(Callable.class);
        delegate.initCallable(execution) = callable;

        delegate.init(eventPublisher);

        delegate.done = true;

        verify(callable, never()).call();
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testThrownException() throws Exception {
        DelegateExecution execution = TestUtils.createExecution();

        Callable<Object> callable = mock(Callable.class);
        delegate.initCallable(execution) = callable;

        delegate.init(eventPublisher);

        when(callable.call()).thenThrow(new RuntimeException("Task exception"));

        delegate.done = true;

        verify(callable, never()).call();
        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        FlowInstance flowInstance = Mockito.mock(FlowInstance.class);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(flowInstance);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test
    public void testBindToFlowTaskInstance() throws Exception {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);

        delegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(delegate);
    }
}