package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.test.BaseTest;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest extends BaseTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private Execution execution;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> delegate;

    private DelegateExecution executionMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(eventPublisher.publishEvent(isA(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(new FlowTaskInstance());
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenAnswer(
                invocation -> runtimeService.getProcessInstanceById(invocation.getArgument(0)).getId());
        when(retryExecutor.run(any(Callable.class))).thenAnswer(callable -> callable.call());
        when(retryExecutor.build()).thenReturn(retryExecutor);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getVariables()).thenReturn(execution.getVariables());
        when(execution.getVariables().get(anyString())).thenReturn(null);
        when(execution.getVariables().putValue(anyString(), any())).thenAnswer(invocation -> {
            executionMock = execution;
            return null;
        });
        when(execution.getVariables().remove(anyString())).thenAnswer(invocation -> null);

        delegate.eventPublisher = eventPublisher;
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.retryExecutor = retryExecutor;
        delegate.taskInstanceCreatedlistener = taskInstanceCreatedlistener;
        delegate.activeTaskStatisticsListener = activeTaskStatisticsListener;
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        delegate.initTargetTaskInstanceId(executionMock);
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(eq(activityId), eq(1L));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.initTargetTaskInstanceId(executionMock);
        delegate.prepare();
        delegate.call();
        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        Future<String> future = delegate.get();
        assertEquals(delegate.returnObject, future.get());
    }

    @Test
    public void testGetExecution() {
        delegate.initTargetTaskInstanceId(executionMock);
        delegate.prepare();
        delegate.call();
        verify(execution, times(1)).setVariables(anyMap());
        assertEquals(execution, delegate.getExecution());
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionWithException() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.getVariables().get(anyString())).thenReturn(new Exception("Exception"));
        delegate.initTargetTaskInstanceId(executionMock);
        delegate.prepare();
        delegate.call();
        delegate.getExecution();
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws InterruptedException, ExecutionException, TimeoutException {
        Thread.currentThread().interrupt();
        delegate.initTargetTaskInstanceId(executionMock);
        delegate.prepare();
        delegate.call();
        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.initTargetTaskInstanceId(executionMock);
        delegate.prepare();
        delegate.call();
        Thread.sleep(1001);
        delegate.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = delegate.initCallable(executionMock);
        assertNotNull(callable);
    }

    @Test
    public void testRun() throws Exception {
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenReturn("result");
        delegate.activityId = "activityId";
        delegate.targetTaskInstanceId = 1L;
        delegate.flowInstanceId = 1L;
        delegate.strategyConfig = new ExecutionStrategyConfig();

        delegate.run(executionMock);
        verify(execution, times(1)).setVariables(anyMap());
        verify(eventPublisher, times(1)).publishEvent(isA(ServiceTaskStartedEvent.class));

        delegate.preHandle(executionMock);
        callRealMethod(delegate, "run", executionMock);
        verify(callable, times(1)).call();
        assertEquals("result", delegate.returnObject);
    }
}