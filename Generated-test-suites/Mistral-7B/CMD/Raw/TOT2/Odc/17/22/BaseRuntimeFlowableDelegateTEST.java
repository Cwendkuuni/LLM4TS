package com.oceanbase.odc.service.flow.task;

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
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @BeforeAll
    static void setupClass() {
        AnnotatedClassLoader classLoader = new AnnotatedClassLoader(BaseRuntimeFlowableDelegate.class);
        PowerMockito.mockStatic(org.flowable.engine.contexts.Context.class, classLoader);
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(FlowableExecutionEngine.class, Mockito.mock(ClassLoader.class));
        delegate = new BaseRuntimeFlowableDelegate<>();
        PowerMockito.doReturn(mock(FlowTaskInstance.class)).when(flowableAdaptor).getTaskInstanceByActivityId(anyString(), anyLong());
        PowerMockito.doReturn(mock(FlowableExecutionEngine.class)).when(FlowableExecutionEngine.class).get(any());
        Mockito.doNothing().when(eventPublisher).addEventListener(any());
        Mockito.doNothing().when(eventPublisher).removeEventListener(any());
    }

    @Test
    void testInitCallable() {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = delegate.initCallable(execution);
        assertNotNull(callable);
    }

    @Test
    void testInit() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = Mockito.mock(Callable.class);
        Mockito.when(delegate.initCallable(execution)).thenReturn(callable);

        delegate.init(eventPublisher);
        verify(eventPublisher).addEventListener(any());
    }

    @Test
    void testRun_Success() throws Exception {
        String activityId = "activityId";
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;
        Long targetTaskInstanceId = 1L;
        TaskType taskType = TaskType.TASK_TYPE_1;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();

        DelegateExecution execution = mock(DelegateExecution.class);
        Mockito.when(execution.getCurrentActivityId()).thenReturn(activityId);
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.of(flowInstanceId));
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId)).thenReturn(Mockito.mock(FlowTaskInstance.class));

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(Mockito.mock(FlowTaskInstance.class));

        PowerMockito.when(FlowableExecutionEngine.class.get(any())).thenReturn(execution);

        delegate.run(execution);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    void testRun_Exception() throws Exception {
        String activityId = "activityId";
        String processDefinitionId = "processDefinitionId";

        DelegateExecution execution = mock(DelegateExecution.class);
        Mockito.when(execution.getCurrentActivityId()).thenReturn(activityId);
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenThrow(new RuntimeException());

        delegate.init(eventPublisher);

        Exception exception = assertThrows(ServiceTaskError.class, () -> delegate.run(execution));
        assertEquals("Initialization task failed", exception.getMessage());
    }

    @Test
    void testGet_Success() throws InterruptedException, ExecutionException, TimeoutException {
        String activityId = "activityId";
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;
        Long targetTaskInstanceId = 1L;
        TaskType taskType = TaskType.TASK_TYPE_1;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();

        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = Mockito.mock(Callable.class);
        Mockito.when(delegate.initCallable(execution)).thenReturn(callable);

        delegate.init(eventPublisher);
        delegate.bindToFlowTaskInstance(Mockito.mock(FlowTaskInstance.class));

        PowerMockito.when(FlowableExecutionEngine.class.get(any())).thenReturn(execution);

        delegate.run(execution);
        delegate.get();

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    void testGet_Exception() throws InterruptedException, ExecutionException, TimeoutException {
        String activityId = "activityId";
        String processDefinitionId = "processDefinitionId";

        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = Mockito.mock(Callable.class);
        Mockito.when(delegate.initCallable(execution)).thenThrow(new RuntimeException());

        delegate.init(eventPublisher);

        Exception exception = assertThrows(ExecutionException.class, () -> delegate.get());
        assertNotNull(exception.getCause());
        assertTrue(exception.getMessage().startsWith("Service task execution failed"));
    }

    @Test
    void testGet_Timeout() throws InterruptedException, ExecutionException, TimeoutException {
        String activityId = "activityId";
        String processDefinitionId = "processDefinitionId";

        DelegateExecution execution = mock(DelegateExecution.class);
        Callable<Object> callable = Mockito.mock(Callable.class);
        Mockito.when(delegate.initCallable(execution)).thenReturn(callable);

        delegate.init(eventPublisher);

        TimeoutException timeoutException = assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.SECONDS));
        assertTrue(timeoutException.getMessage().startsWith("Task execution timed out"));
    }

    @Test
    void testUpdateFlowInstanceStatus() throws Exception {
        Long flowInstanceId = 1L;
        FlowStatus flowStatus = FlowStatus.COMPLETED;

        delegate.updateFlowInstanceStatus(flowStatus);
        verify(flowInstanceRepository).updateStatusById(flowInstanceId, flowStatus);
    }
}