package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Integer> baseRuntimeFlowableDelegate;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private RetryExecutor retryExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInit() {
        assertEquals(null, baseRuntimeFlowableDelegate.getReturnObject());
        assertNull(baseRuntimeFlowableDelegate.getThrown());
        assertFalse(baseRuntimeFlowableDelegate.isDone());
        assertEquals(0, baseRuntimeFlowableDelegate.getLatch().getCount());
        verify(eventPublisher, times(0)).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher, times(0)).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);

        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getTaskDefinitionKey()).thenReturn("taskDefinitionKey");
        when(execution.getCurrentActivityId()).thenReturn("activityId");

        baseRuntimeFlowableDelegate.run(execution);

        verify(eventPublisher).publishEvent(new ServiceTaskStartedEvent(baseRuntimeFlowableDelegate, any()));
        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", 1L);
        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
        verify(execution).setVariable("resultVariable", any());
        verify(execution).setVariable("statusVariable", FlowStatus.COMPLETED.name());
        verify(baseRuntimeFlowableDelegate).setFlowInstanceId(1L);
        verify(baseRuntimeFlowableDelegate).setTargetTaskInstanceId(anyLong());
        verify(baseRuntimeFlowableDelegate).setTaskType(any(TaskType.class));
        verify(baseRuntimeFlowableDelegate).setStrategyConfig(any(ExecutionStrategyConfig.class));
        verify(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(execution);
        verify(baseRuntimeFlowableDelegate).bindToFlowTaskInstance(any(FlowTaskInstance.class));
        verify(baseRuntimeFlowableDelegate).updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(baseRuntimeFlowableDelegate).preHandle(execution);

        verify(baseRuntimeFlowableDelegate.getLatch(), times(1)).await();
        verify(baseRuntimeFlowableDelegate).get(anyLong(), any(TimeUnit.class));
        verify(baseRuntimeFlowableDelegate, times(2)).isDone();
        verify(baseRuntimeFlowableDelegate).get();
        verify(baseRuntimeFlowableDelegate).get(anyLong(), any(TimeUnit.class));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);

        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));

        assertEquals(1, baseRuntimeFlowableDelegate.get().intValue());
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);

        baseRuntimeFlowableDelegate.run(mock(DelegateExecution.class));

        assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(10, TimeUnit.SECONDS));
    }

    @Test
    public void testInitTaskInstanceListeners() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        baseRuntimeFlowableDelegate.init(eventPublisher);

        verify(eventPublisher).addEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher).addEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testPreHandle() throws Exception {
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "flowInstanceId", 1L, () -> true);

        baseRuntimeFlowableDelegate.preHandle(mock(DelegateExecution.class));

        verifyNoMoreInteractions(flowableAdaptor);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testInitCallableNull() {
        baseRuntimeFlowableDelegate.initCallable(mock(DelegateExecution.class));

        verifyNoInteractions(flowableAdaptor);
    }

    @Test(expected = NullPointerException.class)
    public void testInitCallableNullExecution() {
        baseRuntimeFlowableDelegate.initCallable(null);
    }
}