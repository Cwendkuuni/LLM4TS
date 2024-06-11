package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.FlowStatus;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInit() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        assertNotNull(delegate.getRetryExecutor());
        assertNotNull(delegate.getLatch());
        assertNotNull(delegate.getTaskInstanceCreatedlistener());
        assertNotNull(delegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = spy(new BaseRuntimeFlowableDelegate<>());
        when(delegate.initCallable(Mockito.any())).thenReturn(Mockito.mock(Callable.class));

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.run(execution);

        verify(delegate, times(1)).initCallable(execution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        CountDownLatch latch = Mockito.mock(CountDownLatch.class);
        delegate.latch = latch;
        when(latch.getCount()).thenReturn(1L);

        delegate.init(Mockito.mock(EventPublisher.class));
        String result = delegate.get();

        assertNotNull(result);
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        CountDownLatch latch = Mockito.mock(CountDownLatch.class);
        delegate.latch = latch;
        when(latch.getCount()).thenReturn(1L);

        delegate.init(Mockito.mock(EventPublisher.class));
        try {
            delegate.get(1, TimeUnit.SECONDS);
            fail("Expected TimeoutException");
        } catch (TimeoutException e) {
            // expected
        }
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        assertFalse(delegate.isDone());

        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGetCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        Callable<String> callable = Mockito.mock(Callable.class);
        when(delegate.initCallable(Mockito.any())).thenReturn(callable);

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.run(execution);

        verify(delegate, times(1)).initCallable(execution);
        assertEquals(callable, delegate.getCallable());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);

        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        FlowStatus flowStatus = FlowStatus.COMPLETED;

        delegate.updateFlowInstanceStatus(flowStatus);

        verify(delegate.getRetryExecutor()).run(() -> {
            try {
                flowInstanceRepository.updateStatusById(delegate.getFlowInstanceId(), flowStatus);
                return true;
            } catch (Exception e) {
                return null;
            }
        }, isA(Boolean.class));
    }

    @Test
    public void testExecute() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        delegate.execute(execution);

        verify(delegate, times(1)).execute(execution);
        verify(delegate, times(1)).removeEventListener(delegate.getTaskInstanceCreatedlistener());
        verify(delegate, times(1)).removeEventListener(delegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testRun() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = Mockito.mock(Callable.class);
        when(delegate.initCallable(execution)).thenReturn(callable);

        delegate.run(execution);

        verify(delegate, times(1)).initTargetTaskInstanceId(execution);
        verify(delegate, times(1)).preHandle(execution);
        verify(delegate, times(1)).call(callable);
        verify(delegate, times(1)).done = true;
        verify(delegate, times(1)).latch.countDown();
        verify(delegate, times(1)).eventPublisher.publishEvent(Mockito.any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        FlowableAdaptor flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);

        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(Optional.of(1L));
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);

        delegate.init(eventPublisher);
        delegate.initTargetTaskInstanceId(execution);

        assertEquals(1L, delegate.getFlowInstanceId());
        assertEquals(FlowStatus.COMPLETED, delegate.getStrategyConfig().getStatus());
    }

    @Test
    public void testPreHandle() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        delegate.preHandle(execution);

        verify(delegate, times(0)).call(Mockito.any(Callable.class));
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<>();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = Mockito.mock(Callable.class);
        when(delegate.initCallable(execution)).thenReturn(callable);

        doThrow(new RuntimeException("Exception")).when(callable).call();

        delegate.run(execution);

        verify(delegate, times(1)).initTargetTaskInstanceId(execution);
        verify(delegate, times(1)).preHandle(execution);
        verify(delegate, times(1)).thrown = new RuntimeException("Exception");
        verify(delegate, times(1)).done = true;
        verify(delegate, times(1)).latch.countDown();
        verify(delegate, times(1)).eventPublisher.publishEvent(Mockito.any(ServiceTaskStartedEvent.class));
    }
}