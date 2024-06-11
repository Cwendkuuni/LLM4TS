package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.exception.ServiceException;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegateTest.TestCallable;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.net.ssl.*", "org.apache.http.conn.*", "org.apache.http.client.*"})
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Integer> baseRuntimeFlowableDelegate;
    private EventPublisher eventPublisher;
    private RetryExecutor retryExecutor;
    private FlowTaskInstance flowTaskInstance;
    private FlowInstanceRepository flowInstanceRepository;
    private CountDownLatch latch;
    private Callable<Integer> callable;

    @Before
    public void setup() {
        eventPublisher = mock(EventPublisher.class);
        retryExecutor = mock(RetryExecutor.class);
        flowTaskInstance = mock(FlowTaskInstance.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        latch = new CountDownLatch(1);
        callable = new TestCallable();
        BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        PowerMockito.mockStatic(FlowableAdaptor.class);
        PowerMockito.when(FlowableAdaptor.class, "getFlowInstanceIdByProcessDefinitionId", anyString())
                .thenReturn(1L);

        baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
        baseRuntimeFlowableDelegate.retryExecutor = retryExecutor;
        baseRuntimeFlowableDelegate.flowableAdaptor = PowerMockito.mock(FlowableAdaptor.class);
        baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
        baseRuntimeFlowableDelegate.init();
        this.baseRuntimeFlowableDelegate = baseRuntimeFlowableDelegate;
    }

    @Test
    public void testInit() {
        assertEquals(null, baseRuntimeFlowableDelegate.getReturnObject());
        assertEquals(null, baseRuntimeFlowableDelegate.getThrown());
        assertEquals(false, baseRuntimeFlowableDelegate.isDone());
        assertEquals(0, baseRuntimeFlowableDelegate.getLatch().getCount());
    }

    @Test
    public void testRun() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowInstanceRepository.updateStatusById(anyLong(), anyInt()))
                .thenAnswer(new Answer<Boolean>() {
                    @Override
                    public Boolean answer(InvocationOnMock invocation) throws Throwable {
                        FlowStatus status = (FlowStatus) invocation.getArguments()[0];
                        System.out.println("Update status to " + status);
                        return true;
                    }
                });

        PreConditions.setup();
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.RUNNING));
        verifyNoMoreInteractions(flowInstanceRepository);
        verifyNoMoreInteractions(flowableAdaptor);

        ArgumentCaptor<TaskType> taskTypeArgumentCaptor = ArgumentCaptor.forClass(TaskType.class);
        verify(flowTaskInstance, times(1)).bindServiceTask(eq(baseRuntimeFlowableDelegate));
        assertEquals(FlowStatus.RUNNING, baseRuntimeFlowableDelegate.getFlowInstanceStatus());
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(mock(DelegateExecution.class));
        verify(flowTaskInstance, times(1)).dealloc();
        verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testIsDone() {
        baseRuntimeFlowableDelegate.done = true;
        assertEquals(true, baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.done = false;
        assertEquals(false, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.latch.countDown();
        baseRuntimeFlowableDelegate.done = true;
        baseRuntimeFlowableDelegate.returnObject = 1;

        assertEquals(1, baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException {
        baseRuntimeFlowableDelegate.latch.countDown();
        baseRuntimeFlowableDelegate.done = true;
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        Exception exception = new Exception();
        baseRuntimeFlowableDelegate.thrown = exception;
        baseRuntimeFlowableDelegate.done = true;
        baseRuntimeFlowableDelegate.latch.countDown();
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.latch.await(1, TimeUnit.SECONDS);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetFutureResult() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenReturn(1);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        baseRuntimeFlowableDelegate.init();
        baseRuntimeFlowableDelegate.callable = callable;
        baseRuntimeFlowableDelegate.latch = latch;

        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));
        baseRuntimeFlowableDelegate.get();

        verify(callable, times(1)).call();
        verifyNoMoreInteractions(callable);
        verify(latch, times(1)).await();
        verify(latch, times(1)).countDown();
    }

    @Test(expected = ServiceException.class)
    public void testGetServiceException() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenThrow(new ServiceException());
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        baseRuntimeFlowableDelegate.init();
        baseRuntimeFlowableDelegate.callable = callable;
        baseRuntimeFlowableDelegate.latch = latch;

        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));
        baseRuntimeFlowableDelegate.get();

        verify(callable, times(1)).call();
        verifyThrows(ServiceException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testInitCallable() throws Exception {
        Method method = BaseRuntimeFlowableDelegate.class.getDeclaredMethod("initCallable", DelegateExecution.class);
        method.setAccessible(true);
        baseRuntimeFlowableDelegate.activityId = "activityId";
        baseRuntimeFlowableDelegate.flowInstanceId = 1L;
        baseRuntimeFlowableDelegate.targetTaskInstanceId = 2L;

        TestCallable testCallable = new TestCallable();
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(flowTaskInstance);

        method.invoke(baseRuntimeFlowableDelegate, mock(DelegateExecution.class));

        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(eq("activityId"), eq(1L));
        verify(flowTaskInstance, times(1)).bindServiceTask(eq(baseRuntimeFlowableDelegate));
        assertEquals(1L, baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals("activityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(2L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
    }

}

class TestCallable implements Callable<Integer> {

    @Override
    public Integer call() {
        return 1;
    }

}