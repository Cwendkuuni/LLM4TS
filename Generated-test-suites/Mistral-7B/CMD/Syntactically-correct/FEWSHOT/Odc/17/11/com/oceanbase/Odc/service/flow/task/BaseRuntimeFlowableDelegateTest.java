package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateUnitTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

@Slf4j
public class BaseRuntimeFlowableDelegateUnitTest {

    @Test
    public void testInit() {
        BaseRuntimeFlowableDelegate<Object> delegate = new MyBaseRuntimeFlowableDelegate();
        assertNotNull(delegate.getRetryExecutor());
        assertNotNull(delegate.getLatch());
        assertNotNull(delegate.getTaskInstanceCreatedlistener());
        assertNotNull(delegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testRun_Normal() throws Exception {
        BaseRuntimeFlowableDelegate<Object> delegate = new MyBaseRuntimeFlowableDelegate();
        MockitoAnnotations.initMocks(this);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(Mockito.anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong())).thenReturn(new FlowTaskInstance(1L, "taskType", null, null, null, null, null, null, null));
        doReturn(new TaskInstanceCreatedListener(delegate)).when(eventPublisher).addEventListener(Mockito.any(TaskInstanceCreatedListener.class));
        doReturn(new ActiveTaskStatisticsListener(delegate)).when(eventPublisher).addEventListener(Mockito.any(ActiveTaskStatisticsListener.class));

        delegate.init(Mockito.mock(EventPublisher.class));
        when(execution.getVariables()).thenReturn(new Object[] {});
        delegate.run(execution);
        verify(execution).setVariable("activityId", "activityId");
        verify(eventPublisher).publishEvent(new ServiceTaskStartedEvent(delegate, Mockito.any(Thread.class)));
        verify(delegate.getFlowableAdaptor()).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(delegate.getFlowableAdaptor()).getTaskInstanceByActivityId("activityId", 1L);
    }

    @Test
    public void testRun_InitializationFailed() throws Exception {
        BaseRuntimeFlowableDelegate<Object> delegate = new MyBaseRuntimeFlowableDelegate();
        MockitoAnnotations.initMocks(this);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenThrow(new Exception());

        try {
            delegate.init(Mockito.mock(EventPublisher.class));
            fail();
        } catch (Exception e) {
            // expected
        }

        verify(log, times(1)).warn("Initialization task failed", any(Exception.class));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<Object> delegate = new MyBaseRuntimeFlowableDelegate();
        MockitoAnnotations.initMocks(this);
        CountDownLatch latch = new CountDownLatch(1);
        when(delegate.getLatch()).thenReturn(latch);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        delegate.init(Mockito.mock(EventPublisher.class));
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");
        when(delegate.initCallable(execution)).thenReturn(callable);
        when(execution.getId()).thenReturn(1L);

        delegate.run(execution);
        delegate.get();

        verify(delegate.getLatch()).await();
        verify(delegate).setDone(true);
        verify(delegate.getReturnObject()).equals("result");
    }

    @Test
    public void testGet_Interrupted() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<Object> delegate = new MyBaseRuntimeFlowableDelegate();
        MockitoAnnotations.initMocks(this);
        CountDownLatch latch = new CountDownLatch(1);
        when(delegate.getLatch()).thenReturn(latch);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        delegate.init(Mockito.mock(EventPublisher.class));
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");
        when(delegate.initCallable(execution)).thenReturn(callable);
        when(execution.getId()).thenReturn(1L);

        delegate.run(execution);
        latch.countDown();

        try {
            delegate.get(1, TimeUnit.MILLISECONDS);
            fail();
        } catch (TimeoutException e) {
            // expected
        }
    }

    @Test
    public void testGet_Exception() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<Object> delegate = new MyBaseRuntimeFlowableDelegate();
        MockitoAnnotations.initMocks(this);
        CountDownLatch latch = new CountDownLatch(1);
        when(delegate.getLatch()).thenReturn(latch);
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        delegate.init(Mockito.mock(EventPublisher.class));
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException());
        when(delegate.initCallable(execution)).thenReturn(callable);
        when(execution.getId()).thenReturn(1L);

        delegate.run(execution);
        latch.countDown();

        try {
            delegate.get();
            fail();
        } catch (ExecutionException e) {
            assertEquals("Caused by: java.lang.RuntimeException", e.getCause().getMessage());
        }
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<Object> delegate = new MyBaseRuntimeFlowableDelegate();
        FlowTaskInstance taskInstance = new FlowTaskInstance(1L, "taskType", null, null, null, null, null, null, null);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance).bindServiceTask(delegate);
        verify(log).info("Monitor task instance creation and bind tasks, taskInstanceId={}, activityId={}", taskInstance.getId(), delegate.getActivityId());
    }

    @Test
    public void testBindToFlowTaskInstance_WrongTaskInstance() {
        BaseRuntimeFlowableDelegate<Object> delegate = new MyBaseRuntimeFlowableDelegate();
        FlowTaskInstance taskInstance = new FlowTaskInstance(2L, "taskType", null, null, null, null, null, null, null);
        delegate.bindToFlowTaskInstance(taskInstance);
        verifyZeroInteractions(taskInstance);
        verifyZeroInteractions(log);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        BaseRuntimeFlowableDelegate<Object> delegate = new MyBaseRuntimeFlowableDelegate();
        when(flowInstanceRepository.updateStatusById(Mockito.anyLong(), FlowStatus.COMPLETED)).thenReturn(true);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(delegate.getRetryExecutor()).run(() -> {
            try {
                return flowInstanceRepository.updateStatusById(Mockito.anyLong(), Mockito.eq(FlowStatus.COMPLETED));
            } catch (Exception e) {
                return null;
            }
        }, isA(Boolean.class));
    }

    interface MyBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<Object> {
        Callable<Object> initCallable(DelegateExecution execution);
    }

}