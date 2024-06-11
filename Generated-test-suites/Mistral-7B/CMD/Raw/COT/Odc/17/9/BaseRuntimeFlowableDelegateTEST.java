package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private FlowInstanceRepository flowInstanceRepository;
    private CountDownLatch latch;
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        this.flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        this.eventPublisher = Mockito.mock(EventPublisher.class);
        this.execution = Mockito.mock(DelegateExecution.class);
        this.latch = new CountDownLatch(1);
        this.delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return Mockito.mock(Callable.class);
            }

            @Override
            protected void preHandle(DelegateExecution execution) {}
        };
        delegate.init(eventPublisher);
        delegate.flowInstanceRepository = flowInstanceRepository;
    }

    @Test
    public void testInit() {
        delegate.init(eventPublisher);
        verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));
        verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
    }

    @Test
    public void testRun() throws Exception {
        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.activityId = "activityId";
        delegate.initTargetTaskInstanceId(execution);
        delegate.preHandle(execution);
        when(callable.call()).thenReturn("result");

        delegate.run(execution);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        assertEquals("result", delegate.get());
    }

    @Test
    public void testRunWithException() throws Exception {
        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.activityId = "activityId";
        delegate.initTargetTaskInstanceId(execution);
        when(callable.call()).thenThrow(new ServiceTaskError("error message"));

        try {
            delegate.run(execution);
            fail();
        } catch (ServiceTaskError e) {
            assertEquals("error message", e.getMessage());
        }

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(0)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(0)).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @Test(expected = ServiceTaskError.class)
    public void testUpdateFlowInstanceStatusWithException() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
        throw new RuntimeException();
    }

    @Test
    public void testGet() throws Exception {
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("result");
        delegate.returnObject = "result";
        delegate.done = true;

        delegate.get();
        verify(latch, times(1)).await();
        verifyNoMoreInteractions(callable);
        assertEquals("result", delegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws Exception {
        Callable<String> callable = Mockito.mock(Callable.class);
        delegate.returnObject = "result";
        delegate.done = true;
        Thread interruptionThread = new Thread(() -> {
            latch.countDown();
            latch.await();
        });
        interruptionThread.interrupt();

        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws Exception {
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenThrow(new ServiceTaskError("error message"));

        try {
            delegate.get();
            fail();
        } catch (ExecutionException e) {
            assertEquals("error message", e.getCause().getMessage());
        }
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws Exception {
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("result");

        try {
            delegate.get(10, TimeUnit.MILLISECONDS);
            fail();
        } catch (TimeoutException e) {
            // expected
        }
    }

    @Test
    public void testIsDone() {
        assertTrue(delegate.isDone());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

}