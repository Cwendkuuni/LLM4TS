package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.test.util.TestUtils;
import com.oceanbase.odc.service.flow.test.util.TestUtils.TestExecution;
import com.oceanbase.odc.service.flow.test.util.TestUtils.TestFlowableAdaptor;
import com.oceanbase.odc.service.flow.test.util.TestUtils.TestFlowInstanceRepository;
import com.oceanbase.odc.service.flow.test.util.TestUtils.TestFlowTaskInstance;

/**
 * Test cases for {@link BaseRuntimeFlowableDelegate} class
 *
 * @author yh263208
 * @date 2023-03-18 14:28
 */
public class BaseRuntimeFlowableDelegateTest {

    @Captor
    private ArgumentCaptor<TaskInstanceCreatedListener> taskInstanceCreatedlistenerCaptor;
    @Captor
    private ArgumentCaptor<ActiveTaskStatisticsListener> activeTaskStatisticsListenerCaptor;

    private TestExecution execution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private BaseRuntimeFlowableDelegate<String> delegate;

    @org.junit.Before
    public void setUp() {
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.init(eventPublisher);
        execution = TestUtils.createTestExecution();
    }

    @org.junit.Test
    public void testIsDone() {
        assertFalse(delegate.isDone());
        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @org.junit.Test
    public void testGetReturnsDoneResult() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(TestUtils.createTestFlowTaskInstance()));
        delegate.initTargetTaskInstanceId(execution);
        delegate.returnObject = "done";
        delegate.done = true;
        String result = delegate.get();
        verify(eventPublisher).publishEvent(Mockito.eq(new ServiceTaskStartedEvent(delegate, any())));
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistenerCaptor.capture());
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListenerCaptor.capture());
        assertEquals("done", result);
    }

    @org.junit.Test(expected = InterruptedException.class)
    public void testGetReturnsInterruptedException() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.latch = latch;
        delegate.done = true;
        latch.countDown();
        delegate.get();
    }

    @org.junit.Test(expected = ExecutionException.class)
    public void testGetReturnsExecutionException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.empty());
        delegate.initTargetTaskInstanceId(execution);
        Exception exception = new Exception("Initialization task failed");
        delegate.thrown = exception;
        delegate.done = true;
        delegate.latch.countDown();
        delegate.get();
    }

    @org.junit.Test(expected = TimeoutException.class)
    public void testGetReturnsTimeoutException() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        delegate.latch = latch;
        delegate.done = false;
        delegate.get(1, TimeUnit.SECONDS);
    }

    @org.junit.Test
    public void testBindToFlowTaskInstance() {
        TestFlowTaskInstance testFlowTaskInstance = TestUtils.createTestFlowTaskInstance();
        delegate.bindToFlowTaskInstance(testFlowTaskInstance);
        assertSame(delegate, testFlowTaskInstance.getServiceTask());
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(delegate.flowInstanceId, FlowStatus.COMPLETED);
    }

    @org.junit.Test
    public void testPreHandle() throws Exception {
        String activityId = "testActivityId";
        when(execution.getCurrentActivityId()).thenReturn(activityId);
        delegate.preHandle(execution);
        verify(flowableAdaptor).getTaskInstanceByActivityId(activityId, delegate.flowInstanceId);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testInitCallableReturnsNull() {
        delegate.initCallable(execution) == null;
    }

    private static class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return null;
        }

    }

}