package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
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

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private DelegateExecution execution;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
    }

    @Test
    public void testInit() {
        assertEquals(0, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertNull(baseRuntimeFlowableDelegate.getTaskType());
        assertNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertNull(baseRuntimeFlowableDelegate.getStrategyConfig());
        assertNull(baseRuntimeFlowableDelegate.getEventPublisher());
        assertNull(baseRuntimeFlowableDelegate.getFlowableAdaptor());
        assertNull(baseRuntimeFlowableDelegate.getFlowInstanceRepository());
        assertNull(baseRuntimeFlowableDelegate.getReturnObject());
        assertNull(baseRuntimeFlowableDelegate.getThrown());
        assertFalse(baseRuntimeFlowableDelegate.isDone());
        assertEquals(1, baseRuntimeFlowableDelegate.getLatch().getCount());
        assertNotNull(baseRuntimeFlowableDelegate.getRetryExecutor());
        assertNotNull(baseRuntimeFlowableDelegate.getLatch());
        verify(eventPublisher, times(0)).addEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(0)).addEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testRunWithSuccess() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(execution);

        verify(execution, times(1)).getCurrentActivityId();
        verify(execution, times(1)).getProcessDefinitionId();
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(baseRuntimeFlowableDelegate, times(1)).initTargetTaskInstanceId(execution);
        verify(baseRuntimeFlowableDelegate, times(1)).preHandle(execution);

        assertEquals("1", baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(TaskType.UNDEFINED, baseRuntimeFlowableDelegate.getTaskType());
        assertNull(baseRuntimeFlowableDelegate.getStrategyConfig());
        assertNull(baseRuntimeFlowableDelegate.getReturnObject());
        assertNull(baseRuntimeFlowableDelegate.getThrown());
        assertTrue(baseRuntimeFlowableDelegate.isDone());
        assertEquals(0, baseRuntimeFlowableDelegate.getLatch().getCount());
    }

    @Test
    public void testRunWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new Exception());

        try {
            baseRuntimeFlowableDelegate.run(execution);
            fail();
        } catch (ServiceTaskError e) {
            assertEquals("Initialization task failed", e.getMessage());
            assertTrue(baseRuntimeFlowableDelegate.isDone());
            assertEquals(0, baseRuntimeFlowableDelegate.getLatch().getCount());
        }

        verify(execution, times(1)).getCurrentActivityId();
        verify(execution, times(1)).getProcessDefinitionId();
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(baseRuntimeFlowableDelegate, times(1)).initTargetTaskInstanceId(execution);
        verify(baseRuntimeFlowableDelegate, times(0)).preHandle(execution);
        verify(baseRuntimeFlowableDelegate, times(1)).getThrown();
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get();

        verify(baseRuntimeFlowableDelegate, times(1)).get();
        verify(baseRuntimeFlowableDelegate, times(1)).call();
        verify(baseRuntimeFlowableDelegate, times(0)).get(anyLong(), any(TimeUnit.class));
        verify(baseRuntimeFlowableDelegate, times(0)).get(anyLong(), any(TimeUnit.class), any(TimeoutException.class));
        assertNull(baseRuntimeFlowableDelegate.getThrown());
        assertNotNull(baseRuntimeFlowableDelegate.getReturnObject());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(execution);

        try {
            baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            // expected
        }

        verify(baseRuntimeFlowableDelegate, times(1)).get(anyLong(), any(TimeUnit.class));
        verify(baseRuntimeFlowableDelegate, times(0)).get();
        verify(baseRuntimeFlowableDelegate, times(0)).get(anyLong(), any(TimeUnit.class), any(TimeoutException.class));
        assertNull(baseRuntimeFlowableDelegate.getReturnObject());
        assertNull(baseRuntimeFlowableDelegate.getThrown());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
        assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(TaskType.TASK_TYPE_1, baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = mock(Callable.class);

        when(baseRuntimeFlowableDelegate.initCallable(execution)).thenReturn(callable);

        verify(baseRuntimeFlowableDelegate, times(1)).initCallable(execution);
        assertEquals(callable, baseRuntimeFlowableDelegate.getCallable());
    }

}