package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
@Slf4j
@MockitoJUnitRunner
public class BaseRuntimeFlowableDelegateTest {

    @MockBean
    private FlowableAdaptor flowableAdaptor;

    @MockBean
    private EventPublisher eventPublisher;

    @MockBean
    private FlowInstanceRepository flowInstanceRepository;

    @Autowired
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Autowired
    private RetryExecutor retryExecutor;

    @Autowired
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Autowired
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @org.junit.Before
    public void setUp() {
        delegate.init(eventPublisher);
    }

    @org.junit.Test
    public void testRun_shouldCallPreHandle() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.run(execution);
        verify(execution).execute(); // execute the delegate execution
        verify(delegate).preHandle(execution); // preHandle method is called
    }

    @org.junit.Test
    public void testRun_shouldInitializeTargetTaskInstanceId() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        PreConditions.stubReturnValue(true, ResourceType.ODC_FLOW_INSTANCE, null, "processDefinitionId",
                Optional::of);
        PreConditions.stubReturnValue(true, ResourceType.ODC_FLOW_TASK_INSTANCE, null, "activityId",
                Optional::of);

        delegate.run(execution);

        verify(execution).getCurrentActivityId(); // getCurrentActivityId method is called
        verify(delegate).initTargetTaskInstanceId(execution); // initTargetTaskInstanceId method is called
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId"); // getFlowInstanceIdByProcessDefinitionId method is called
        verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", anyLong()); // getTaskInstanceByActivityId method is called
    }

    @org.junit.Test
    public void testRun_shouldUpdateFlowInstanceStatus() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        PreConditions.stubReturnValue(true, ResourceType.ODC_FLOW_INSTANCE, null, "processDefinitionId",
                Optional::of);
        PreConditions.stubReturnValue(true, ResourceType.ODC_FLOW_TASK_INSTANCE, null, "activityId",
                Optional::of);
        PreConditions.stubReturnValue(true, null, null, FlowStatus.COMPLETED);

        delegate.run(execution);

        verify(delegate).updateFlowInstanceStatus(FlowStatus.COMPLETED); // updateFlowInstanceStatus method is called
    }

    @org.junit.Test
    public void testGet_shouldReturnValueFromCallable() throws InterruptedException, ExecutionException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);
        PreConditions.stubReturnValue(true, null, null, "result");

        delegate.initCallable(execution) = callable;
        delegate.run(execution);

        verify(callable).call(); // call method is called
        verify(delegate).done = true; // done flag is set to true
        assertNull(delegate.thrown); // thrown exception is null
        assertEquals("result", delegate.get()); // get method returns the result
    }

    @org.junit.Test
    public void testGet_shouldThrowExceptionFromCallable() throws InterruptedException, ExecutionException {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = mock(Callable.class);
        PreConditions.stubThrowException(new RuntimeException("Test Exception"));

        delegate.initCallable(execution) = callable;
        delegate.run(execution);

        verify(callable).call(); // call method is called
        verify(delegate).done = true; // done flag is set to true
        assertNotNull(delegate.thrown); // thrown exception is not null
    }

    @org.junit.Test
    public void testGet_shouldThrowInterruptedException() throws InterruptedException {
        DelegateExecution execution = mock(DelegateExecution.class);
        CountDownLatch latch = new CountDownLatch(1);
        delegate.latch = latch;

        delegate.run(execution);

        latch.await();
        verify(delegate).done = true; // done flag is set to true
        assertNotNull(delegate.thrown); // thrown exception is not null
        assertTrue(delegate.thrown instanceof InterruptedException); // thrown exception is an InterruptedException
    }

    @org.junit.Test
    public void testGet_shouldThrowTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        DelegateExecution execution = mock(DelegateExecution.class);
        CountDownLatch latch = new CountDownLatch(1);
        delegate.latch = latch;

        delegate.run(execution);

        latch.await(1, TimeUnit.SECONDS);
        verify(delegate).done = false; // done flag is not set to true
        verify(delegate).thrown != null; // thrown exception is not null
        assertTrue(delegate.thrown instanceof TimeoutException); // thrown exception is a TimeoutException
    }

    @org.junit.Test
    public void testBindToFlowTaskInstance_shouldBindServiceTask() throws Exception {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(delegate);
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatus_shouldUpdateStatusInRepository() throws Exception {
        PreConditions.stubReturnValue(true, null, null, FlowStatus.COMPLETED);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(delegate.flowInstanceId, FlowStatus.COMPLETED);
    }

    /**
     * Add more test cases as needed, for example, testing the edge cases and exception handling scenarios
     */

    

    @org.junit.After
    public void tearDown() {
        delegate.eventPublisher.removeEventListener(activeTaskStatisticsListener);
        delegate.eventPublisher.removeEventListener(taskInstanceCreatedlistener);
    }

    
}