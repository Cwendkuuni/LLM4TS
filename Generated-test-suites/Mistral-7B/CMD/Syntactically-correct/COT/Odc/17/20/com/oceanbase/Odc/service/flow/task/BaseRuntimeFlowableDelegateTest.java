package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.test.util.TestUtils;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setup() {
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = Mockito.spy(RetryExecutor.builder().build());
        taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
        activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);

        delegate = new BaseRuntimeFlowableDelegate<>() {
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

        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(true);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);

        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(TestUtils.createFlowTaskInstance());

        when(flowableAdaptor.getFlowInstanceRepository()).thenReturn(flowInstanceRepository);

        delegate.init(eventPublisher);
        execution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testRun() throws Exception {
        delegate.run(execution);
        verify(eventPublisher).publishEvent(new ServiceTaskStartedEvent(delegate, any()));
        assertEquals(FlowStatus.ACTIVE, delegate.getFlowStatus());
        Mockito.verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.ACTIVE);
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("test exception"));

        delegate.run(execution);
        verify(eventPublisher).publishEvent(new ServiceTaskStartedEvent(delegate, any()));
        assertEquals(FlowStatus.ERROR, delegate.getFlowStatus());
        Mockito.verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.ERROR);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.run(execution);
        assertEquals("result", delegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
        Thread.currentThread().interrupt();
        delegate.run(execution);
        try {
            delegate.get();
        } catch (InterruptedException e) {
            throw e;
        }
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("test exception"));

        delegate.run(execution);
        try {
            delegate.get();
        } catch (ExecutionException e) {
            throw e;
        }
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.run(execution);
        Mockito.verify(flowInstanceRepository, never())
                .updateStatusById(delegate.getFlowInstanceId(), FlowStatus.COMPLETED);
        Mockito.verify(flowInstanceRepository, never())
                .updateStatusById(delegate.getFlowInstanceId(), FlowStatus.FAILED);
        delegate.get(1, TimeUnit.SECONDS);
        Mockito.verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.FAILED);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        delegate.initTargetTaskInstanceId(execution);
        assertEquals(1L, delegate.getFlowInstanceId());
        assertEquals(TaskType.TEST_TASK_TYPE, delegate.getTaskType());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = TestUtils.createFlowTaskInstance();
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        Mockito.verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), FlowStatus.COMPLETED);
    }

    @Test
    public void testPreHandle() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        delegate.preHandle(execution);
        verifyZeroInteractions(flowableAdaptor);
    }

}