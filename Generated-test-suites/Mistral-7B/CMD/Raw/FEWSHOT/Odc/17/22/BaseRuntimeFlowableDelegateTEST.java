package com.oceanbase.odc.service.flow.task;

import static com.oceanbase.odc.core.shared.constant.FlowStatus.COMPLETED;
import static com.oceanbase.odc.core.shared.constant.FlowStatus.FAILED;
import static com.oceanbase.odc.core.shared.constant.FlowStatus.READY;
import static com.oceanbase.odc.core.shared.constant.FlowStatus.RUNNING;
import static com.oceanbase.odc.core.shared.constant.TaskType.TASK_TYPE_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.flowable.engine.common.util.EngineUtil.getVariable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.constant.ResourceType;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;

class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegateTest {

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> eventCaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Override
    public void setUp() {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));
    }

    @Test
    public void testInit() {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.init(eventPublisher);
        verify(eventPublisher).addEventListener(activeTaskStatisticsListener);
        verify(eventPublisher).addEventListener(taskInstanceCreatedlistener);
    }

    @Test
    public void testRun_success() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getVariables()).thenReturn(mock(Map.class));
        when(eventPublisher.publishEvent(eventCaptor.capture())).thenReturn(null);

        delegate.run(execution);
        delegate.preHandle(execution);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn(new Object());
        delegate.bindCallable(callable);

        assertThat(delegate.get()).isInstanceOf(Object.class);
        assertThat(getVariable(execution, "returnObject")).isEqualTo(new Object());
        verify(activeTaskStatisticsListener).onActiveTaskStatistics(any(), any(), any());
        verify(taskInstanceCreatedlistener).onTaskInstanceCreated(any());
    }

    @Test
    public void testRun_exception() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getVariables()).thenReturn(mock(Map.class));
        when(eventPublisher.publishEvent(eventCaptor.capture())).thenReturn(null);

        delegate.run(execution);
        delegate.preHandle(execution);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("TestException"));
        delegate.bindCallable(callable);

        assertThatThrownBy(() -> delegate.get())
                .isInstanceOf(ExecutionException.class)
                .hasMessageContaining("TestException");
        verify(activeTaskStatisticsListener).onActiveTaskStatistics(any(), any(), any());
        verify(taskInstanceCreatedlistener).onTaskInstanceCreated(any());
    }

    @Test
    public void testRun_initException() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getVariables()).thenReturn(mock(Map.class));
        when(eventPublisher.publishEvent(eventCaptor.capture())).thenReturn(null);

        delegate.run(execution);
        delegate.preHandle(execution);

        Callable<Object> callable = mock(Callable.class);
        Throwable initException = new RuntimeException("InitException");
        when(callable.call()).thenCallRealMethod();
        when(callable.call()).thenThrow(initException);
        delegate.bindCallable(callable);

        assertThatThrownBy(() -> delegate.get())
                .isInstanceOf(ServiceTaskError.class)
                .hasMessageContaining("Initialization task failed")
                .hasCause(initException);
        verify(activeTaskStatisticsListener).onActiveTaskStatistics(any(), any(), any());
        verify(taskInstanceCreatedlistener).onTaskInstanceCreated(any());
    }

    @Test
    public void testGet_running() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getVariables()).thenReturn(mock(Map.class));
        when(eventPublisher.publishEvent(eventCaptor.capture())).thenReturn(null);

        delegate.run(execution);
        delegate.preHandle(execution);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenCallRealMethod();
        delegate.bindCallable(callable);

        delegate.get(1, TimeUnit.SECONDS);

        verify(activeTaskStatisticsListener).onActiveTaskStatistics(any(), eq(RUNNING), any());
    }

    @Test
    public void testGet_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getVariables()).thenReturn(mock(Map.class));
        when(eventPublisher.publishEvent(eventCaptor.capture())).thenReturn(null);

        delegate.run(execution);
        delegate.preHandle(execution);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenCallRealMethod();

        delegate.get(0, TimeUnit.SECONDS);

        verify(activeTaskStatisticsListener).onActiveTaskStatistics(any(), eq(READY), any());
    }

    @Test
    public void testGet_completed() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getVariables()).thenReturn(mock(Map.class));
        when(eventPublisher.publishEvent(eventCaptor.capture())).thenReturn(null);

        delegate.run(execution);
        delegate.preHandle(execution);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenReturn(new Object());
        delegate.bindCallable(callable);

        delegate.get();

        verify(activeTaskStatisticsListener).onActiveTaskStatistics(any(), eq(COMPLETED), any());
    }

    @Test
    public void testGet_failed() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getVariables()).thenReturn(mock(Map.class));
        when(eventPublisher.publishEvent(eventCaptor.capture())).thenReturn(null);

        delegate.run(execution);
        delegate.preHandle(execution);

        Callable<Object> callable = mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("TestException"));
        delegate.bindCallable(callable);

        delegate.get();

        verify(activeTaskStatisticsListener).onActiveTaskStatistics(any(), eq(FAILED), any());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testInitCallable_null() {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();

        assertThatThrownBy(() -> delegate.bindCallable(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Callable");
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<Object> delegate = new TestBaseRuntimeFlowableDelegate();

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "flowInstanceId", 1L,
                () -> delegate.updateFlowInstanceStatus(RUNNING));

        verify(flowInstanceRepository).updateStatusById(1L, RUNNING);
    }
}

class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<Object> {

    @Override
    protected Callable<Object> initCallable(DelegateExecution execution) {
        return mock(Callable.class);
    }
}