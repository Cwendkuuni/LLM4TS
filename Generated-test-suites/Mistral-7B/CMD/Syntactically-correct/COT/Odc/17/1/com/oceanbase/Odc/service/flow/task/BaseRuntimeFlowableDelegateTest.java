package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org. flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private DelegateExecution execution;

    @Mock
    private FlowTaskInstance taskInstance;

    @Mock
    private ServiceTaskStartedEvent serviceTaskStartedEvent;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(flowInstanceRepository.updateStatusById(eq(1L), eq(any(FlowStatus.class)))).thenReturn(true);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
    }

    @After
    public void cleanup() {
        reset(flowableAdaptor, eventPublisher, flowInstanceRepository, execution, taskInstance,
                serviceTaskStartedEvent, activeTaskStatisticsListener, taskInstanceCreatedlistener);
    }

    @Test
    public void testInit() {
        delegate.init(eventPublisher);
        verify(eventPublisher, times(2)).addEventListener(any());
    }

    @Test
    public void testGetActivityId() {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        assertEquals("activityId", delegate.getActivityId());
    }

    @Test
    public void testGetTargetTaskInstanceId() {
        when(execution.getVariable("targetTaskInstanceId")).thenReturn("targetTaskInstanceId");
        assertEquals("targetTaskInstanceId", delegate.getTargetTaskInstanceId());
    }

    @Test
    public void testGetTaskType() {
        when(execution.getVariable("taskType")).thenReturn(TaskType.TYPE1);
        assertEquals(TaskType.TYPE1, delegate.getTaskType());
    }

    @Test
    public void testGetFlowInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        assertEquals(1L, delegate.getFlowInstanceId());
    }

    @Test
    public void testGetStrategyConfig() {
        when(execution.getVariable("strategyConfig")).thenReturn(ExecutionStrategyConfig.builder().build());
        assertEquals(ExecutionStrategyConfig.builder().build(), delegate.getStrategyConfig());
    }

    @Test
    public void testRun() throws Exception {
        delegate.run(execution);
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testPreHandle() throws Exception {
        delegate.preHandle(execution);
        // PreHandle method should be empty
    }

    @Test(expected = ServiceTaskError.class)
    public void testInitializationFailed() throws Exception {
        doThrow(new Exception("Initialization failed")).when(flowableAdaptor)
                .getFlowInstanceIdByProcessDefinitionId(anyString());
        delegate.run(execution);
    }

    @Test(expected = ServiceTaskError.class)
    public void testExecutionFailed() throws Exception {
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new Exception("Execution failed"));
        doReturn(callable).when(delegate).initCallable(execution);

        delegate.run(execution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn(new Object());
        doReturn(callable).when(delegate).initCallable(execution);

        delegate.get();
        verify(execution, times(1)).getVariable("done");
        verify(execution, never()).isDone();
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn(new Object());
        doReturn(callable).when(delegate).initCallable(execution);

        delegate.get(1, TimeUnit.SECONDS);
        verify(execution, never()).getVariable("done");
        verify(execution, times(1)).isDone();
    }

    @Test
    public void testIsDone() {
        when(execution.getVariable("done")).thenReturn(true);
        assertTrue(delegate.isDone());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        when(taskInstance.getId()).thenReturn(1L);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(any());
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(eq(1L), eq(FlowStatus.COMPLETED));
    }
}