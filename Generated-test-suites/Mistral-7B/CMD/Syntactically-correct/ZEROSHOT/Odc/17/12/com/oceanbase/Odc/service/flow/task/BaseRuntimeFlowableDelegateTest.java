package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.FlowableAdaptorMock;
import com.oceanbase.odc.service.flow.event.EventPublisherMock;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceMock;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListenerMock;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListenerMock;

public class BaseRuntimeFlowableDelegateTest extends BaseFlowableDelegateTest {

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String ACTIVITY_ID = "activityId";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TARGET_TASK_INSTANCE_ID = 1L;
    private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig();

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
    private DelegateExecution delegateExecution;
    private EventPublisherMock eventPublisher;
    private FlowableAdaptorMock flowableAdaptor;
    private FlowInstanceRepository mockFlowInstanceRepository;
    private TaskInstanceCreatedListenerMock taskInstanceCreatedlistener;
    private ActiveTaskStatisticsListenerMock activeTaskStatisticsListener;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) throws Exception {
                initCallableMock.answer(callRealMethod());
                return initCallableMock.getMock();
            }
        };
        eventPublisher = Mockito.mock(EventPublisher.class);
        flowableAdaptor = new FlowableAdaptorMock();
        mockFlowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        taskInstanceCreatedlistener = new TaskInstanceCreatedListenerMock();
        activeTaskStatisticsListener = new ActiveTaskStatisticsListenerMock();
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(mockFlowInstanceRepository);
        baseRuntimeFlowableDelegate.init(eventPublisher);
        delegateExecution = createDelegateExecution();
        when(mockFlowInstanceRepository.updateStatusById(FLOW_INSTANCE_ID, any(FlowStatus.class)))
                .thenReturn(true);
    }

    @After
    public void tearDown() {
        reset(eventPublisher, flowableAdaptor, mockFlowInstanceRepository, taskInstanceCreatedlistener,
                activeTaskStatisticsListener);
    }

    @Test
    public void testRun() throws InterruptedException, ExecutionException, TimeoutException {
        initTargetTaskInstanceId(delegateExecution);
        baseRuntimeFlowableDelegate.run(delegateExecution);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test(expected = ExecutionException.class)
    public void testRunException() throws InterruptedException, ExecutionException, TimeoutException {
        doThrow(new RuntimeException("Test Exception")).when(baseRuntimeFlowableDelegate).initCallable(any());
        baseRuntimeFlowableDelegate.run(delegateExecution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        given(initCallableMock.getMock().call()).willReturn("Test Result");
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertEquals("Test Result", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
        Thread.currentThread().interrupt();
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        doThrow(new RuntimeException("Test Exception")).when(baseRuntimeFlowableDelegate).initCallable(any());
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(delegateExecution);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testIsDone() {
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstanceMock flowTaskInstance = createFlowTaskInstanceMock();
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance, times(1)).bindServiceTask(eq(baseRuntimeFlowableDelegate));
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(mockFlowInstanceRepository, times(1)).updateStatusById(eq(FLOW_INSTANCE_ID), eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testPreHandle() throws Exception {
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);
        verifyNoMoreInteractions(eventPublisher, flowableAdaptor, mockFlowInstanceRepository, taskInstanceCreatedlistener,
                activeTaskStatisticsListener);
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        verify(eventPublisher, times(1)).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher, times(1)).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testInit() {
        eventPublisher.addEventHandler(eq(ActiveTaskStatisticsListenerMock.class), any());
        eventPublisher.addEventHandler(eq(TaskInstanceCreatedListenerMock.class), any());
    }

}