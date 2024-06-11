package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
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
import com.oceanbase.odc.service.flow.test.TestUtils;

/**
 * Test cases for BaseRuntimeFlowableDelegate
 */
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
    private CountDownLatch latch;
    @Mock
    private RetryExecutor retryExecutor;
    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    @Mock
    private Callable<Object> callableMock;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> testee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testee.init(eventPublisher);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(true);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));
        when(latch.await(longThat(l -> l <= 0), anyLong()))
                .thenReturn(false);
        when(latch.await(anyLong(), anyLong()))
                .thenReturn(true);
        when(retryExecutor.run(any(Callable.class), any(Function.class)))
                .thenReturn(Optional.of(null));
        when(callableMock.call())
                .thenReturn("Test result");
    }

    @Test
    public void testInitCallable() {
        PreConditions.notNull(testee.initCallable(execution), "initCallable");
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(execution.getProcessDefinitionId());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(execution.getCurrentActivityId(), anyLong());
    }

    @Test(expected = ServiceTaskError.class)
    public void testInitializationTaskFailed() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(execution.getProcessDefinitionId()))
                .thenThrow(new RuntimeException("Initialization error"));
        testee.run(execution);
    }

    @Test
    public void testPreHandle() {
        testee.preHandle(execution);
        verify(testee, never()).get();
        verify(testee, never()).cancel(true);
    }

    @Test(expected = ServiceTaskError.class)
    public void testServiceTaskExecutionFailed() {
        when(retryExecutor.run(callableMock, any(Function.class)))
                .thenReturn(Optional.ofNull());
        try {
            testee.call();
        } catch (Exception e) {
            throw new ServiceTaskError(e);
        }
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws Exception {
        testee.init(eventPublisher);
        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        testee.run(execution);
        TestUtils.waitForExecutionToComplete(testee);
        testee.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws Exception {
        when(callableMock.call())
                .thenThrow(new RuntimeException("Test exception"));
        try {
            testee.call();
        } catch (Exception e) {
            throw new ExecutionException(e);
        }
        TestUtils.waitForExecutionToComplete(testee);
        testee.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws Exception {
        when(latch.await(longThat(l -> l > 5000), anyLong()))
                .thenReturn(false);
        testee.init(eventPublisher);
        testee.run(execution);
        testee.get(5000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testIsDone() {
        assertFalse(testee.isDone());
        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        TestUtils.waitForExecutionToComplete(testee);
        assertTrue(testee.isDone());
    }

    @Test
    public void testGet() throws Exception {
        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        TestUtils.waitForExecutionToComplete(testee);
        assertSame("Test result", testee.get());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        testee.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance, times(1)).bindServiceTask(testee);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(testee.getFlowInstanceId(), FlowStatus.COMPLETED);
    }
}