package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private RetryExecutor retryExecutor;
    @Mock
    private DelegateExecution execution;
    @Mock
    private FlowTaskInstance flowTaskInstance;
    @Mock
    private Callable<Object> callable;

    private BaseRuntimeFlowableDelegate<Object> testObject;
    private CountDownLatch latch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testObject = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return callable;
            }
        };
        testObject.setEventPublisher(eventPublisher);
        testObject.setFlowableAdaptor(flowableAdaptor);
        testObject.setFlowInstanceRepository(flowInstanceRepository);
        testObject.setRetryExecutor(retryExecutor);
        latch = new CountDownLatch(1);
    }

    @Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(flowTaskInstance);

        testObject.run(execution);

        verify(execution).setVariable("activityId", "activityId");
        verify(execution).setVariable("targetTaskInstanceId", 1L);
        verify(execution).setVariable("taskType", any());
        verify(execution).setVariable("flowInstanceId", 1L);
        verify(execution).setVariable("strategyConfig", any());

        ArgumentCaptor<ServiceTaskStartedEvent> eventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        ServiceTaskStartedEvent event = eventCaptor.getValue();
        assertEquals(testObject, event.getServiceTask());
        assertEquals(Thread.currentThread(), event.getThread());

        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", 1L);
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));

        verify(callable).call();
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenThrow(new RuntimeException());

        try {
            testObject.run(execution);
            fail();
        } catch (ServiceTaskError e) {
            assertEquals("Initialization task failed", e.getMessage());
        }

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, never()).getTaskInstanceByActivityId("activityId", 1L);
        verify(callable, never()).call();
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(flowTaskInstance);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn(new Object());
        testObject.setCallable(callable);

        testObject.run(execution);
        testObject.get();

        verify(callable).call();
        verify(flowInstanceRepository).updateStatusById(1L, eq(FlowStatus.COMPLETED));
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(flowTaskInstance);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenCallRealMethod();

        testObject.setCallable(callable);
        testObject.run(execution);

        latch.countDown();
        testObject.get();

        verify(callable).call();
        verify(flowInstanceRepository).updateStatusById(1L, eq(FlowStatus.COMPLETED));

        ArgumentCaptor<InterruptedException> exceptionCaptor = ArgumentCaptor.forClass(InterruptedException.class);
        verifyThrown(execution::get, exceptionCaptor.capture());
        InterruptedException exception = exceptionCaptor.getValue();
        assertTrue(exception.getMessage().contains("interrupted"));
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(flowTaskInstance);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException());
        testObject.setCallable(callable);

        testObject.run(execution);

        try {
            testObject.get();
            fail();
        } catch (ExecutionException e) {
            assertEquals(callable.call(), e.getCause());
        }

        verify(callable).call();
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(flowTaskInstance);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenCallRealMethod();

        testObject.setCallable(callable);
        testObject.run(execution);

        latch.await(3, TimeUnit.SECONDS);
        testObject.get(3, TimeUnit.SECONDS);

        verify(callable).call();
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));

        ArgumentCaptor<TimeoutException> exceptionCaptor = ArgumentCaptor.forClass(TimeoutException.class);
        verifyThrown(testObject::get, exceptionCaptor.capture());
        TimeoutException exception = exceptionCaptor.getValue();
        assertTrue(exception.getMessage().contains("timed out"));
    }

    @Test
    public void testPreHandle() throws Exception {
        testObject.preHandle(execution);
        verifyNoMoreInteractions(execution);
    }

    @Test
    public void testBindToFlowTaskInstance() throws Exception {
        testObject.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).bindServiceTask(testObject);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        testObject.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(1L, eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testInitializeCallableWithNullExecution() {
        assertNull(testObject.initCallable(null));
    }

    @Test(expected = NullPointerException.class)
    public void testInitializeCallableWithNullCallable() {
        testObject.initCallable(null);
    }

    @Test
    public void testIsDone() throws Exception {
        assertFalse(testObject.isDone());
        testObject.done();
        assertTrue(testObject.isDone());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithoutDone() throws Exception {
        testObject.get();
    }

    private void verifyThrown(Callable<Object> callable, Throwable expected) throws Throwable {
        try {
            callable.call();
            fail();
        } catch (Throwable e) {
            assertEquals(expected.getClass(), e.getClass());
            if (expected instanceof InvocationTargetException) {
                verifyThrown(((InvocationTargetException) expected).getTargetException(), expected.getMessage());
            } else {
                assertEquals(expected.getMessage(), e.getMessage());
            }
        }
    }
}