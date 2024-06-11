package com.oceanbase.odc.service.flow.task;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Callable<String> callable;

    @Mock
    private Future<String> future;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> testObject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(execution.getId()).thenReturn(1L);
        when(execution.getExecutionId()).thenReturn(2L);
        when(execution.getVariables()).thenReturn(new Object());
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(3L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(retryExecutor.run(any(), any())).then(invocation -> invocation.getArgumentAt(0, 0).call());
        testObject.init(eventPublisher);
    }

    @After
    public void tearDown() {
        testObject.init(null);
        verify(eventPublisher).removeAllListeners(taskInstanceCreatedlistener);
        verify(eventPublisher).removeAllListeners(activeTaskStatisticsListener);
    }

    @Test
    public void testConstructor() {
        assertThat(testObject, notNullValue());
    }

    @Test
    public void testGetActivityId() {
        String expected = "activityId";
        when(execution.getCurrentActivityId()).thenReturn(expected);
        assertThat(testObject.getActivityId(), is(expected));
    }

    @Test
    public void testGetTargetTaskInstanceId() {
        Long expected = 1L;
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance().setId(expected)));
        assertThat(testObject.getTargetTaskInstanceId(), is(expected));
    }

    @Test
    public void testGetTaskType() {
        TaskType expected = TaskType.TASK_TYPE_1;
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance().setTaskType(expected)));
        assertThat(testObject.getTaskType(), is(expected));
    }

    @Test
    public void testGetFlowInstanceId() {
        Long expected = 3L;
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(expected));
        assertThat(testObject.getFlowInstanceId(), is(expected));
    }

    @Test
    public void testGetStrategyConfig() {
        ExecutionStrategyConfig expected = new ExecutionStrategyConfig();
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance().setStrategyConfig(expected)));
        assertThat(testObject.getStrategyConfig(), is(expected));
    }

    @Test
    public void testGetFlowableAdaptor() {
        assertThat(testObject.getFlowableAdaptor(), is(flowableAdaptor));
    }

    @Test
    public void testGetEventPublisher() {
        assertThat(testObject.getEventPublisher(), is(eventPublisher));
    }

    @Test
    public void testGetFlowInstanceRepository() {
        assertThat(testObject.getFlowInstanceRepository(), is(flowInstanceRepository));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        testObject.initTargetTaskInstanceId(execution);
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(any());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        testObject.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(testObject);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws InterruptedException, ExecutionException, TimeoutException {
        FlowStatus flowStatus = FlowStatus.COMPLETED;
        testObject.updateFlowInstanceStatus(flowStatus);
        verify(flowInstanceRepository, times(1)).updateStatusById(3L, flowStatus);
    }

    @Test
    public void testRun() throws Exception {
        Callable<String> callable = () -> "result";
        when(callable.call()).thenReturn("result");
        when(callable instanceof Callable<Future<String>>).thenReturn((Callable<Future<String>>) future);
        when(future.get()).thenReturn("result");
        testObject.run(execution);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        ArgumentCaptor<FlowStatus> flowStatusArgumentCaptor = ArgumentCaptor.forClass(FlowStatus.class);
        verify(flowInstanceRepository, times(1)).updateStatusById(3L, flowStatusArgumentCaptor.capture());
        FlowStatus actualFlowStatus = flowStatusArgumentCaptor.getValue();
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "flowInstanceId", 3L, () -> actualFlowStatus == FlowStatus.RUNNING);
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() throws Exception {
        Callable<String> callable = () -> {
            throw new Exception("Exception");
        };
        testObject.run(execution);
    }

    @Test
    public void testExecute() throws Exception {
        testObject.execute(execution);
        verify(eventPublisher, times(1)).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher, times(1)).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testIsDone() {
        testObject.done = true;
        assertThat(testObject.isDone(), is(true));
        testObject.done = false;
        assertThat(testObject.isDone(), is(false));
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Callable<String> callable = () -> "result";
        testObject = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };
        testObject.run(execution);
        String actualResult = testObject.get();
        assertThat(actualResult, is("result"));
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = () -> {
            throw new InterruptedException();
        };
        testObject = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };
        testObject.run(execution);
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = () -> {
            throw new ExecutionException("Exception");
        };
        testObject = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };
        testObject.run(execution);
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        testObject = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return null;
            }
        };
        testObject.run(execution);
        testObject.get(10, TimeUnit.MILLISECONDS);
    }
}