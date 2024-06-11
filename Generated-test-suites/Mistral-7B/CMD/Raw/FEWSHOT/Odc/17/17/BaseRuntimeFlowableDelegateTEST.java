package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test cases for BaseRuntimeFlowableDelegate class
 *
 * @author yh263208
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private Execution execution;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private Callable<String> callableMock;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> delegateUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(eventPublisher.publishEvent(Mockito.any(ServiceTaskStartedEvent.class))).thenReturn(true);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(mock(FlowTaskInstance.class));
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);
        when(callableMock.call()).thenReturn("result");
    }

    @Test
    public void testInit() {
        assertNotNull(delegateUnderTest.getRetryExecutor());
        assertNotNull(delegateUnderTest.getLatch());
        assertNotNull(delegateUnderTest.getTaskInstanceCreatedlistener());
        assertNotNull(delegateUnderTest.getActiveTaskStatisticsListener());
    }

    @Test
    public void testInitCallable() {
        when(delegateUnderTest.initCallable(execution)).thenReturn(callableMock);
        assertEquals("result", delegateUnderTest.get());
    }

    @Test
    public void testIsDone() {
        assertTrue(delegateUnderTest.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegateUnderTest.init(eventPublisher);
        delegateUnderTest.bindToFlowTaskInstance(mock(FlowTaskInstance.class));

        ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventArgCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisher).publishEvent(serviceTaskStartedEventArgCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventArgCaptor.getValue();
        assertEquals("Monitor task instance creation and bind tasks, taskInstanceId=, activityId=", serviceTaskStartedEvent.getMessage());

        delegateUnderTest.run(execution);

        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testInitTaskInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(mock(FlowTaskInstance.class));

        delegateUnderTest.initTargetTaskInstanceId(execution);

        assertEquals(1L, delegateUnderTest.getFlowInstanceId());
        assertEquals("activityId", delegateUnderTest.getActivityId());
        assertEquals(TaskType.TYPE_1, delegateUnderTest.getTaskType());
        assertNotNull(delegateUnderTest.getStrategyConfig());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegateUnderTest.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }
}