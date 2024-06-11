package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.test.FlowableTestUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInit() {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        assertEquals("", delegate.getActivityId());
        assertEquals(null, delegate.getTargetTaskInstanceId());
        assertEquals(null, delegate.getTaskType());
        assertEquals(0L, delegate.getFlowInstanceId());
        assertEquals(null, delegate.getStrategyConfig());
        assertNull(delegate.getFlowableAdaptor());
        assertNull(delegate.getEventPublisher());
        assertNull(delegate.getFlowInstanceRepository());
        assertNull(delegate.getReturnObject());
        assertNull(delegate.getThrown());
        assertFalse(delegate.isDone());
        assertEquals(1, delegate.getLatch().getCount());
        assertNotNull(delegate.getRetryExecutor());
        assertNotNull(delegate.getTaskInstanceCreatedlistener());
        assertNotNull(delegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testRun() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        RetryExecutor retryExecutor = Mockito.mock(RetryExecutor.class);
        TaskInstanceCreatedListener taskInstanceCreatedlistener = Mockito.mock(TaskInstanceCreatedListener.class);
        ActiveTaskStatisticsListener activeTaskStatisticsListener = Mockito.mock(ActiveTaskStatisticsListener.class);

        delegate.init(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
        delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("result");

        delegate.run(execution);

        verify(eventPublisher).publishEvent(Mockito.eq(new ServiceTaskStartedEvent(delegate, Mockito.any())));
        verify(callable).call();
        verify(delegate.getLatch()).countDown();
        verify(execution).getProcessDefinitionId();
        verify(delegate.getFlowableAdaptor()).getFlowInstanceIdByProcessDefinitionId(execution.getProcessDefinitionId());
        verify(delegate.getFlowInstanceRepository()).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.COMPLETED);
        verify(taskInstanceCreatedlistener).onCreated(Mockito.any(FlowTaskInstance.class));
        verify(activeTaskStatisticsListener).onActiveTaskChanged(Mockito.eq(delegate), Mockito.eq(FlowStatus.COMPLETED),
                Mockito.anyLong());

        assertEquals("result", delegate.getReturnObject());
        assertNull(delegate.getThrown());
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        RetryExecutor retryExecutor = Mockito.mock(RetryExecutor.class);
        TaskInstanceCreatedListener taskInstanceCreatedlistener = Mockito.mock(TaskInstanceCreatedListener.class);
        ActiveTaskStatisticsListener activeTaskStatisticsListener = Mockito.mock(ActiveTaskStatisticsListener.class);

        delegate.init(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
        delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("result");

        delegate.run(execution);

        assertTrue(delegate.getLatch().await(1, TimeUnit.SECONDS));

        String result = delegate.get();

        assertEquals("result", result);
        assertNull(delegate.getThrown());
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        RetryExecutor retryExecutor = Mockito.mock(RetryExecutor.class);
        TaskInstanceCreatedListener taskInstanceCreatedlistener = Mockito.mock(TaskInstanceCreatedListener.class);
        ActiveTaskStatisticsListener activeTaskStatisticsListener = Mockito.mock(ActiveTaskStatisticsListener.class);

        delegate.init(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
        delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenCallRealMethod();

        delegate.run(execution);

        assertFalse(delegate.getLatch().await(1, TimeUnit.SECONDS));

        try {
            delegate.get(1, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            // expected
        }
    }

    @Test
    public void testPreHandle() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        delegate.preHandle(execution);

        verifyZeroInteractions(eventPublisher);
    }

    @Test
    public void testExecute() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();

        delegate.execute(Mockito.mock(DelegateExecution.class));

        verify(delegate.getTaskInstanceCreatedlistener()).onDestroyed(Mockito.any(FlowTaskInstance.class));
        verify(delegate.getActiveTaskStatisticsListener()).onActiveTaskChanged(Mockito.eq(delegate), Mockito.eq(FlowStatus.END),
                Mockito.anyLong());
    }

    @Test
    public void testIsDone() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();

        assertFalse(delegate.isDone());

        delegate.run(Mockito.mock(DelegateExecution.class));

        assertTrue(delegate.isDone());
    }

    @Test
    public void testGetReturnObject() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();

        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("result");

        delegate.initCallable = callable;

        assertEquals("result", delegate.get());
    }

    @Test
    public void testGetThrown() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();

        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenThrow(new Exception("failed"));

        delegate.initCallable = callable;

        assertEquals("failed", delegate.getThrown().getMessage());
        assertNull(delegate.getReturnObject());
    }

    @Test
    public void testGetDone() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();

        delegate.run(Mockito.mock(DelegateExecution.class));

        assertTrue(delegate.isDone());
    }

    @Test
    public void testGetLatch() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();

        assertEquals(1, delegate.getLatch().getCount());
    }

    @Test
    public void testRetryExecutor() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new TestBaseRuntimeFlowableDelegate();

        when(delegate.getFlowableAdaptor().getFlowInstanceIdByProcessDefinitionId(Mockito.anyString())).thenAnswer(
                invocation -> Long.parseLong(invocation.getArgument(0).toString()));

        delegate.init(Mockito.mock(EventPublisher.class));

        delegate.retryExecutor.run(() -> {
            try {
                return delegate.getFlowableAdaptor().getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
            } catch (Exception e) {
                return null;
            }
        }, Objects::nonNull);

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE,
                "processDefinitionId", "processDefinitionId",
                Mockito.anyLong());
    }

    private static class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "result";
        }

        @Override
        protected void preHandle(DelegateExecution execution) {
            FlowableTestUtils.sleep(100);
        }
    }
}