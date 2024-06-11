package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.exception.OdcRuntimeException;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowInstanceRepository;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Callable<Object> callable;

    private BaseRuntimeFlowableDelegate<Object> testSubject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        testSubject = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return callable;
            }
        };
        testSubject.init(eventPublisher);
        testSubject.flowableAdaptor = flowableAdaptor;
        testSubject.eventPublisher = eventPublisher;
        testSubject.flowInstanceRepository = flowInstanceRepository;
    }

    @After
    public void cleanup() {
        reset(eventPublisher, flowableAdaptor, flowInstanceRepository, execution, callable);
    }

    @Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), eq(1L))).thenReturn(mock(FlowTaskInstance.class));

        testSubject.run(execution);

        verify(callable, times(1)).call();
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test(expected = OdcRuntimeException.class)
    public void testRunWithInitializationFailure() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new Exception());

        testSubject.run(execution);

        verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(execution, never()).setVariable("returnObject", any());
        verify(execution, never()).setVariable("thrown", any());
        verify(execution, never()).setVariable("done", anyBoolean());
        verify(latch, times(1)).countDown();
        verify(execution, never()).end();
        verify(execution, times(1)).setVariable("activityId", eq(execution.getCurrentActivityId()));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        when(callable.call()).thenReturn(new Object());

        testSubject.get();

        verify(latch, times(1)).await();
        verify(execution, times(1)).setVariable("returnObject", isA(Object.class));
        verify(execution, never()).setVariable("thrown", any());
        verify(execution, times(1)).setVariable("done", anyBoolean());
    }

    @Test(expected = InterruptedException.class)
    public void testGetWithInterruptedException() throws InterruptedException, ExecutionException {
        when(callable.call()).thenReturn(new Object());

        testSubject.get(1, TimeUnit.SECONDS);

        verify(latch, times(1)).await(eq(1), eq(TimeUnit.SECONDS));
        verify(execution, never()).setVariable("returnObject", any());
        verify(execution, never()).setVariable("thrown", any());
        verify(execution, times(1)).setVariable("done", anyBoolean());
        verify(latch, times(1)).countDown();
        throw new InterruptedException();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenReturn(new Object());

        testSubject.get(1, TimeUnit.SECONDS);

        verify(latch, times(1)).await(eq(1), eq(TimeUnit.SECONDS));
        verify(execution, never()).setVariable("returnObject", any());
        verify(execution, never()).setVariable("thrown", any());
        verify(execution, times(1)).setVariable("done", anyBoolean());
        verify(latch, times(1)).countDown();
        throw new TimeoutException();
    }

    @Test
    public void testIsDone() {
        testSubject.done = true;

        assertTrue(testSubject.isDone());
    }

    @Test
    public void testIsDoneFalse() {
        assertFalse(testSubject.isDone());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance mockTaskInstance = mock(FlowTaskInstance.class);

        testSubject.bindToFlowTaskInstance(mockTaskInstance);

        verify(mockTaskInstance, times(1)).bindServiceTask(testSubject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBindToFlowTaskInstanceWithInvalidTaskInstance() {
        FlowTaskInstance mockTaskInstance = mock(FlowTaskInstance.class);

        testSubject.bindToFlowTaskInstance(mockTaskInstance);

        verify(mockTaskInstance, times(1)).bindServiceTask(testSubject);
        verify(mockTaskInstance, times(1)).getId();
        assertNotEquals(testSubject.targetTaskInstanceId, mockTaskInstance.getId());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        testSubject.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(retryExecutor, times(1)).run(any(Callable.class), any(Function.class));
        verify(flowInstanceRepository, times(1)).updateStatusById(eq(1L), any(FlowStatus.class));
    }
}