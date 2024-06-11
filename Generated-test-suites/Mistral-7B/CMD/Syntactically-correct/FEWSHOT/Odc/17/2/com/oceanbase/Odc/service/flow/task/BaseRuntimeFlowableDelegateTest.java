package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
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
import com.oceanbase.odc.core.shared.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private RetryExecutor retryExecutor;
    @Mock
    private Callable<Object> callableMock;
    @Mock
    private FlowTaskInstance flowTaskInstanceMock;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> testObj;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Optional.of(flowTaskInstanceMock));
        when(flowTaskInstanceMock.getId()).thenReturn(1L);
        when(flowTaskInstanceMock.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);
        when(flowTaskInstanceMock.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(flowableAdaptor.getTaskInstanceById(1L)).thenReturn(flowTaskInstanceMock);
        doNothing().when(eventPublisher).publishEvent(any());
        doNothing().when(eventPublisher).removeEventListener(any());
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        testObj.initTargetTaskInstanceId(execution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId("activityId", 1L);
        verify(flowableAdaptor, times(1)).getTaskInstanceById(1L);
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.EXECUTING);
    }

    @Test
    public void testRun() throws Exception {
        when(callableMock.call()).thenReturn(new Object());

        testObj.initCallable(execution)
                .call();

        verify(eventPublisher, times(1)).publishEvent(new ServiceTaskStartedEvent(testObj, any()));
        verify(execution, times(1)).dealloc();
        verify(eventPublisher, times(2)).removeEventListener(any());
        verifyNoMoreInteractions(callableMock);
    }

    @Test
    public void testRunWithException() throws Exception {
        when(callableMock.call()).thenThrow(new RuntimeException("Exception message"));

        testObj.initCallable(execution)
                .call();

        verify(eventPublisher, times(2)).publishEvent(any());
        verify(execution, times(1)).dealloc();
        verify(eventPublisher, times(2)).removeEventListener(any());
        verifyThrowable(new ServiceTaskError(new RuntimeException("Exception message")));
    }

    @Test
    public void testPreHandle() {
        testObj.preHandle(execution);
        verifyNoInteractions(execution);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGet() throws Exception {
        testObj.get();
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        testObj.init(eventPublisher);
        testObj.latch = latch;
        testObj.run(execution);
        latch.await();
        testObj.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws Exception {
        when(callableMock.call()).thenThrow(new RuntimeException("Exception message"));
        testObj.init(eventPublisher);
        testObj.initCallable(execution)
                .call();
        testObj.get();

        verify(eventPublisher, times(1)).publishEvent(new ServiceTaskStartedEvent(testObj, any()));
        verify(eventPublisher, times(1)).removeEventListener(any());
        verifyThrowable(new ExecutionException(new RuntimeException("Exception message")));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        testObj.bindToFlowTaskInstance(flowTaskInstanceMock);
        verify(flowTaskInstanceMock, times(1)).bindServiceTask(testObj);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        testObj.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(retryExecutor, times(1)).run(() -> {
            try {
                flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED);
                return true;
            } catch (Exception e) {
                return null;
            }
        }, anyBoolean());
    }
}