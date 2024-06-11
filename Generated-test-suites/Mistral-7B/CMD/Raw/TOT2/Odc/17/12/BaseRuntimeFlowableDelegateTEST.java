package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> delegateUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(true);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L))
                .thenReturn(new FlowTaskInstance());
        when(flowInstanceRepository.updateStatusById(1L, any(FlowStatus.class)))
                .thenReturn(true);
        when(retryExecutor.run(any(Callable.class), any(Function.class)))
                .thenReturn(true);
    }

    @Test
    public void testInit() {
        delegateUnderTest.init(eventPublisher);
        verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testRun_DelegateExecutionIsNull() {
        DelegateExecution execution = null;
        when(delegateUnderTest.initCallable(execution)).thenReturn(mock(Callable.class));
        delegateUnderTest.run(execution);
        verify(execution, never()).getCurrentActivityId();
    }

    @Test
    public void testRun_InitializeTargetTaskInstanceId() {
        String processDefinitionId = "processDefinitionId";
        String activityId = "activityId";
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);

        when(delegateUnderTest.initCallable(execution)).thenReturn(callable);
        delegateUnderTest.run(execution);

        ArgumentCaptor<String> processDefinitionIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId(processDefinitionIdCaptor.capture());
        PreConditions.assertValidEquals("processDefinitionId", processDefinitionIdCaptor.getValue());

        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId(activityId, eq(1L));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(delegateUnderTest, times(1)).initTargetTaskInstanceId(execution);
        verify(delegateUnderTest, times(1)).bindToFlowTaskInstance(any(FlowTaskInstance.class));
    }

    @Test
    public void testRun_PreHandle() {
        DelegateExecution execution = mock(DelegateExecution.class);

        delegateUnderTest.run(execution);
        verify(delegateUnderTest, times(1)).preHandle(execution);
    }

    @Test
    public void testRun_ThrowsException() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new Exception("Test Exception"));

        delegateUnderTest.run(execution);
        verify(execution, times(1)).getCurrentActivityId();
        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId("activityId", eq(1L));
        verify(delegateUnderTest, times(1)).initTargetTaskInstanceId(execution);
        verify(delegateUnderTest, times(1)).bindToFlowTaskInstance(any(FlowTaskInstance.class));
        verify(delegateUnderTest, times(1)).preHandle(execution);
        verify(delegateUnderTest, times(1)).updateFlowInstanceStatus(FlowStatus.FAILED);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskError.class));
        verify(DelegateExecution.class, times(1)).setVariable("thrown", any(Exception.class));
        verify(delegateUnderTest, times(1)).done = true;
        verify(delegateUnderTest, times(1)).latch.countDown();
    }

    @Test
    public void testGet() throws Exception, InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);
        String result = "result";

        when(callable.call()).thenReturn(result);

        delegateUnderTest.run(execution);
        verify(delegateUnderTest, times(1)).preHandle(execution);
        delegateUnderTest.get();
        verify(delegateUnderTest, times(1)).isDone();
        verify(delegateUnderTest, times(1)).done = true;
        verify(delegateUnderTest, times(1)).latch.await();
        verify(delegateUnderTest, times(1)).thrown = null;
        verify(delegateUnderTest, times(1)).returnObject = result;
    }

    @Test
    public void testGet_TimeOut() throws Exception, InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);

        delegateUnderTest.run(execution);
        verify(delegateUnderTest, times(1)).preHandle(execution);

        when(callable.call()).thenThrow(new RuntimeException("Test Timeout"));
        delegateUnderTest.get(1, TimeUnit.SECONDS);
        verify(delegateUnderTest, times(1)).isDone();
        verify(delegateUnderTest, times(1)).done = true;
        verify(delegateUnderTest, times(1)).latch.await(anyLong(), any(TimeUnit.class));
        verify(delegateUnderTest, times(1)).thrown = instanceOf(TimeoutException.class);
        verify(delegateUnderTest, times(1)).returnObject = null;
    }

    @Test
    public void testGet_Interrupted() throws Exception, InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);

        delegateUnderTest.run(execution);
        verify(delegateUnderTest, times(1)).preHandle(execution);

        when(callable.call()).thenThrow(new InterruptedException());
        delegateUnderTest.get();
        verify(delegateUnderTest, times(1)).isDone();
        verify(delegateUnderTest, times(1)).done = true;
        verify(delegateUnderTest, times(1)).latch.await();
        verify(delegateUnderTest, times(1)).thrown = instanceOf(InterruptedException.class);
        verify(delegateUnderTest, times(1)).returnObject = null;
    }

    @Test
    public void testGet_ExecutionException() throws Exception, InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);

        delegateUnderTest.run(execution);
        verify(delegateUnderTest, times(1)).preHandle(execution);

        when(callable.call()).thenThrow(new ExecutionException("Test Exception"));
        delegateUnderTest.get();
        verify(delegateUnderTest, times(1)).isDone();
        verify(delegateUnderTest, times(1)).done = true;
        verify(delegateUnderTest, times(1)).latch.await();
        verify(delegateUnderTest, times(1)).thrown = instanceOf(ExecutionException.class);
        verify(delegateUnderTest, times(1)).returnObject = null;
    }

    @Test
    public void testGet_OtherException() throws Exception, InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<String> callable = mock(Callable.class);

        delegateUnderTest.run(execution);
        verify(delegateUnderTest, times(1)).preHandle(execution);

        when(callable.call()).thenThrow(new RuntimeException("Test Other Exception"));
        delegateUnderTest.get();
        verify(delegateUnderTest, times(1)).isDone();
        verify(delegateUnderTest, times(1)).done = true;
        verify(delegateUnderTest, times(1)).latch.await();
        verify(delegateUnderTest, times(1)).thrown = instanceOf(RuntimeException.class);
        verify(delegateUnderTest, times(1)).returnObject = null;
    }
}