package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Execution strategyConfig;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private ServiceTaskStartedEvent serviceTaskStartedEvent;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setLatch(new CountDownLatch(1));
        delegate.setRetryExecutor(RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build());
        delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);
    }

    /**
     * Test initialization method
     */
    @Test
    public void testInit() {
        delegate.init(eventPublisher);
        verify(eventPublisher).addEventListener(activeTaskStatisticsListener);
        verify(eventPublisher).addEventListener(taskInstanceCreatedlistener);
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        assertNotNull(delegate);
    }

    /**
     * Test getter methods
     */
    @Test
    public void testGetters() {
        assertEquals(delegate.getClass().getName(), delegate.getClass().getSimpleName());
        assertNull(delegate.getActivityId());
        assertNull(delegate.getTargetTaskInstanceId());
        assertNull(delegate.getTaskType());
        assertNull(delegate.getFlowInstanceId());
        assertNull(delegate.getStrategyConfig());
    }

    /**
     * Test run method with exception
     */
    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new RuntimeException("FlowInstanceError"));
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong()))
                .thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(strategyConfig);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(1L);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        delegate.run(execution);

        verify(eventPublisher).publishEvent(serviceTaskStartedEvent);
        verify(execution).getCurrentActivityId();
    }

    /**
     * Test run method with no exception
     */
    @Test
    public void testRunWithNoException() {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong()))
                .thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(strategyConfig);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(1L);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        delegate.run(execution);

        verify(eventPublisher).publishEvent(serviceTaskStartedEvent);
        verify(execution).getCurrentActivityId();
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", 1L);
        verify(flowTaskInstance).bindServiceTask(delegate);
        verify(flowInstanceRepository).updateStatusById(1L, eq(FlowStatus.COMPLETED));
    }

    /**
     * Test execute method
     */
    @Test
    public void testExecute() {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong()))
                .thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(strategyConfig);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        delegate.execute(execution);

        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", 1L);
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    /**
     * Test isDone method
     */
    @Test
    public void testIsDone() {
        delegate.done = true;
        assertTrue(delegate.isDone());

        delegate.done = false;
        assertFalse(delegate.isDone());
    }

    /**
     * Test get method
     */
    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong()))
                .thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(strategyConfig);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        Object result = delegate.get();

        assertEquals(result, delegate.returnObject);
    }

    /**
     * Test get method with timeout
     */
    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong()))
                .thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(strategyConfig);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        delegate.get(1, TimeUnit.SECONDS);
    }

    /**
     * Test get method with exception
     */
    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws InterruptedException, ExecutionException {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new RuntimeException("FlowInstanceError"));
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong()))
                .thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(strategyConfig);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        delegate.get();
    }

    /**
     * Test initTargetTaskInstanceId method
     */
    @Test
    public void testInitTargetTaskInstanceId() {
        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        delegate.initTargetTaskInstanceId(execution);

        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", 1L);
    }

    /**
     * Test bindToFlowTaskInstance method
     */
    @Test
    public void testBindToFlowTaskInstance() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong()))
                .thenReturn(flowTaskInstance);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance).bindServiceTask(delegate);
    }

    /**
     * Test updateFlowInstanceStatus method
     */
    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(1L);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(1L, eq(FlowStatus.COMPLETED));
    }

    /**
     * Test initCallable method should throw exception
     */
    @Test(expected = RuntimeException.class)
    public void testInitCallableWithException() {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong()))
                .thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(strategyConfig);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(1L);

        when(initCallable(execution)).thenThrow(new RuntimeException("InitCallableError"));

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        delegate.run(execution);
    }

    /**
     * Test initCallable method with no exception
     */
    @Test
    public void testInitCallableWithNoException() {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", anyLong()))
                .thenReturn(flowTaskInstance);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(strategyConfig);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(1L);

        Callable<Object> callable = mock(Callable.class);
        when(initCallable(execution)).thenReturn(callable);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        delegate.run(execution);

        verify(callable).call();
    }
}