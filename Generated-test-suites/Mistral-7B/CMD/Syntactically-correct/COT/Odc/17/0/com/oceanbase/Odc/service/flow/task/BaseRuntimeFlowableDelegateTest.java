package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.FlowInstanceRepositoryMock;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Before
    public void setUp() {
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = new FlowInstanceRepositoryMock();
        activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
        taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
        delegate = new BaseRuntimeFlowableDelegate<String>() {
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
        delegate.init(eventPublisher);
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.activeTaskStatisticsListener = activeTaskStatisticsListener;
        delegate.taskInstanceCreatedlistener = taskInstanceCreatedlistener;
    }

    @Test
    public void testIsDone_whenDelegateIsDone_returnTrue() {
        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @Test
    public void testIsDone_whenDelegateIsNotDone_returnFalse() {
        assertFalse(delegate.isDone());
    }

    @Test
    public void testGet_whenDelegateIsDone_returnResult() throws Exception {
        delegate.returnObject = "result";
        delegate.done = true;
        assertSame("result", delegate.get());
    }

    @Test
    public void testGet_whenDelegateIsNotDone_throwInterruptedException() {
        assertThrows(InterruptedException.class, () -> delegate.get());
    }

    @Test
    public void testGet_whenTimeout_throwTimeoutException() throws Exception {
        delegate.latch.await(10, TimeUnit.MILLISECONDS);
        assertFalse(delegate.done);
        assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testGet_whenExceptionThrown_throwExecutionException() throws Exception {
        ArgumentCaptor<Exception> exceptionCaptor = ArgumentCaptor.forClass(Exception.class);
        delegate.thrown = new RuntimeException("Test exception");
        delegate.done = true;
        delegate.latch.countDown();
        assertThrows(ExecutionException.class, () -> delegate.get());
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);

        Method method = BaseRuntimeFlowableDelegate.class.getDeclaredMethod("preHandle", DelegateExecution.class);
        method.setAccessible(true);
        method.invoke(delegate, any(DelegateExecution.class));

        verify(activeTaskStatisticsListener).onTaskStarted(any(FlowTaskInstance.class));
        verify(taskInstanceCreatedlistener).onTaskInstanceCreated(any(FlowTaskInstance.class));
    }

    @Test
    public void testBindToFlowTaskInstance() throws Exception {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateFlowInstanceStatus_whenRepositoryIsNull_throwNullPointerException() {
        delegate.flowInstanceRepository = null;
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

    @Test
    public void testInitCallable_whenExecutionIsNull_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> delegate.initCallable(null));
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_whenInitializationFailed_throwServiceTaskError() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new RuntimeException("Test exception"));
        delegate.run(mock(DelegateExecution.class));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).publishEvent(any(ServiceTaskError.class));
    }

    @Test
    public void testPreHandle() {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.preHandle(execution);
    }

}

class FlowInstanceRepositoryMock implements FlowInstanceRepository {

    @Override
    public boolean updateStatusById(Long id, FlowStatus status) {
        return true;
    }

}