package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowInstanceRepository;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowInstanceRepositoryImpl flowInstanceRepositoryImpl;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Execution executionMock;

    @Mock
    private Callable<Object> callableMock;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> testee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(flowInstanceRepositoryImpl.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));
    }

    @Test
    public void testInit() {
        testee.init(eventPublisher);
        verify(eventPublisher).addEventListener(any(ActiveTaskStatisticsListener.class));
        verify(eventPublisher).addEventListener(any(TaskInstanceCreatedListener.class));
    }

    @Test
    public void testRun() throws Exception {
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        testee.run(execution);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getCurrentActivityId()).thenThrow(new Exception());
        testee.run(execution);
        verify(log, times(1)).warn("Initialization task failed", any(Exception.class));
        throw new ServiceTaskError(any(Exception.class));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        testee.run(executionMock);
        assertEquals(testee.get().getClass(), Object.class);
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
        testee.run(executionMock);
        testee.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        when(callableMock.call()).thenThrow(new Exception());
        testee.initCallable = () -> callableMock;
        testee.run(executionMock);
        testee.get();
        verify(log, times(1)).warn("Service task execution failed", any(Exception.class));
        throw new ExecutionException(any(Exception.class));
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        testee.run(executionMock);
        testee.get(1, TimeUnit.SECONDS);
        verify(log, times(1)).warn("Monitor task instance creation and bind tasks, taskInstanceId=, activityId=");
        throw new TimeoutException();
    }

    @Test
    public void testBindToFlowTaskInstance() {
        testee.bindToFlowTaskInstance(mock(FlowTaskInstance.class));
        verify(mock(FlowTaskInstance.class)).bindServiceTask(testee);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(1L, eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testInitCallable() {
        Callable<Object> callable = testee.initCallable(executionMock);
        assertNotNull(callable);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(flowableAdaptor);
        verifyNoMoreInteractions(flowInstanceRepository);
        verifyNoMoreInteractions(flowInstanceRepositoryImpl);
        verifyNoMoreInteractions(execution);
        verifyNoMoreInteractions(executionMock);
        verifyNoMoreInteractions(callableMock);
    }

}