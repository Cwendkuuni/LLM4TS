package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Test class for FlowTaskInstance
 *
 * @author yh263208
 * @since ODC_release_3.3.0
 */
@Slf4j
public class FlowTaskInstanceTest {

    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long ORGANIZATION_ID = 1L;
    private static final Long TARGET_TASK_ID = 1L;
    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
    private static final FlowNodeStatus INITIAL_STATUS = FlowNodeStatus.PENDING;
    private static final FlowNodeStatus EXECUTING_STATUS = FlowNodeStatus.EXECUTING;
    private static final FlowNodeStatus COMPLETED_STATUS = FlowNodeStatus.COMPLETED;
    private static final FlowTaskExecutionStrategy AUTO_EXECUTION_STRATEGY = FlowTaskExecutionStrategy.AUTO;
    private static final FlowTaskExecutionStrategy MANUAL_EXECUTION_STRATEGY = FlowTaskExecutionStrategy.MANUAL;

    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private TaskService taskService;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private RuntimeDelegateMapper delegateConvertor;

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskStartedListener listener;

    @Before
    public void setUp() {
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);

        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(NODE_TYPE), eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.emptyList());

        when(delegateConvertor.map(eq(TaskType.SOME_TASK_TYPE)))
                .thenReturn(Mockito.mock(BaseRuntimeFlowableDelegate.class));

        when(eventPublisher.publishEvent(any()))
                .thenReturn(new Object());

        listener = new ServiceTaskStartedListener(Mockito.mock(FlowTaskInstance.class));
        eventPublisher.addEventListener(listener);

        flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository,
                FLOW_INSTANCE_ID, ORGANIZATION_ID, INITIAL_STATUS, NODE_TYPE, AUTO_EXECUTION_STRATEGY, false, false);
    }

    @Test
    public void testCreateFlowTaskInstance() {
        FlowTaskInstance expectedInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(),
                delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository, FLOW_INSTANCE_ID, ORGANIZATION_ID,
                INITIAL_STATUS, NODE_TYPE, AUTO_EXECUTION_STRATEGY, false, false);

        verify(serviceTaskRepository, times(1)).save(argThat(s -> s.equals(expectedInstance.mapToTaskEntity())));
        assertEquals(expectedInstance.getId(), flowTaskInstance.getId());
        assertEquals(expectedInstance.getFlowInstanceId(), flowTaskInstance.getFlowInstanceId());
        assertEquals(expectedInstance.getOrganizationId(), flowTaskInstance.getOrganizationId());
        assertEquals(expectedInstance.getStatus(), flowTaskInstance.getStatus());
    }

    @Test
    public void testBindingServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        ArgumentCaptor<BaseRuntimeFlowableDelegate<?>> argument = ArgumentCaptor.forClass(
                BaseRuntimeFlowableDelegate.class);
        verify(flowTaskInstance).setTargetTaskHandle(argument.capture());
        assertEquals(target, argument.getValue());
        assertEquals(EXECUTING_STATUS, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeallocFlowTaskInstanceNotPresentOnMachine() {
        flowTaskInstance.dealloc();
    }

    @Test
    public void testAllocFlowTaskInstanceNotAllocated() {
        flowTaskInstance.alloc();

        ArgumentCaptor<ServiceTaskStartedListener> listenerCaptor = ArgumentCaptor.forClass(
                ServiceTaskStartedListener.class);
        verify(eventPublisher, times(1)).addEventListener(listenerCaptor.capture());
        assertEquals(listener, listenerCaptor.getValue());
    }

    @Test
    public void testAllocFlowTaskInstanceAlreadyAllocated() {
        flowTaskInstance.alloc();

        ArgumentCaptor<ServiceTaskStartedListener> listenerCaptor = ArgumentCaptor.forClass(
                ServiceTaskStartedListener.class);
        verify(eventPublisher, never()).addEventListener(listenerCaptor.capture());
    }

    @Test
    public void testDeallocFlowTaskInstance() {
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();

        ArgumentCaptor<ServiceTaskStartedListener> listenerCaptor = ArgumentCaptor.forClass(
                ServiceTaskStartedListener.class);
        verify(eventPublisher, times(1)).removeEventListener(listenerCaptor.capture());
        assertEquals(listener, listenerCaptor.getValue());
    }

    @Test
    public void testCreateAndBindServiceTaskInstance() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
                serviceTaskRepository, FLOW_INSTANCE_ID, ORGANIZATION_ID, INITIAL_STATUS, NODE_TYPE,
                MANUAL_EXECUTION_STRATEGY, false, false);

        flowTaskInstance.bindServiceTask(target);
        ArgumentCaptor<BaseRuntimeFlowableDelegate<?>> argument = ArgumentCaptor.forClass(
                BaseRuntimeFlowableDelegate.class);
        verify(flowTaskInstance).setTargetTaskHandle(argument.capture());
        assertEquals(target, argument.getValue());
        assertEquals(EXECUTING_STATUS, flowTaskInstance.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbortFlowTaskInstanceNotPresentOnMachine() {
        flowTaskInstance.abort();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConfirmExecuteFlowTaskInstanceNotPresentOnMachine() {
        flowTaskInstance.confirmExecute();
    }

    @Test
    public void testCancelFlowTaskInstance() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        doAnswer(invocation -> {
            throw new InterruptedException();
        }).when(target).get();

        flowTaskInstance.cancel(true);
        verify(taskService, times(1)).cancel(eq(target.getId()), eq(true));
    }

    @Test
    public void testIsDoneFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        when(target.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResultFlowTaskInstance() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        when(target.get()).thenReturn("Result");
        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultFlowTaskInstanceWithInterruptedException() throws InterruptedException,
            ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        doThrow(new InterruptedException()).when(target).get();
        try {
            flowTaskInstance.getResult();
        } finally {
            verify(taskService, times(1)).cancel(eq(target.getId()), eq(true));
        }
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultFlowTaskInstanceWithExecutionException() throws InterruptedException,
            ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        doThrow(new ExecutionException("Exception")).when(target).get();
        try {
            flowTaskInstance.getResult();
        } finally {
            verify(taskService, times(1)).cancel(eq(target.getId()), eq(true));
        }
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultFlowTaskInstanceWithTimeoutException() throws InterruptedException,
            ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        doThrow(new TimeoutException("Exception")).when(target).get(anyLong(), any(TimeUnit.class));
        try {
            flowTaskInstance.getResult(1000, TimeUnit.MILLISECONDS);
        } finally {
            verify(taskService, times(1)).cancel(eq(target.getId()), eq(true));
        }
    }

    @Test
    public void testCompleteFlowTaskInstance() {
        FlowableElement flowableElement = Mockito.mock(FlowableElement.class);
        Task task = Mockito.mock(Task.class);
        TaskInfo taskInfo = Mockito.mock(TaskInfo.class);

        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(NODE_TYPE), eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.singletonList(flowableElement));
        when(taskService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list())
                .thenReturn(Collections.singletonList(task));
        when(task.getName()).thenReturn("UserTask");
        when(taskInfo.getName()).thenReturn("UserTask");

        flowTaskInstance.complete(false);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(FLOW_INSTANCE_ID), eq(NODE_TYPE),
                eq(FlowableElementType.USER_TASK));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list();
        verify(taskService, times(1)).complete(eq(task.getId()), anyMap());
    }

    @Test
    public void testCompleteFlowTaskInstanceWithAbortedVariable() {
        FlowableElement flowableElement = Mockito.mock(FlowableElement.class);
        Task task = Mockito.mock(Task.class);
        TaskInfo taskInfo = Mockito.mock(TaskInfo.class);

        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(NODE_TYPE), eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.singletonList(flowableElement));
        when(taskService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list())
                .thenReturn(Collections.singletonList(task));
        when(task.getName()).thenReturn("UserTask");
        when(taskInfo.getName()).thenReturn("UserTask");

        Map<String, Object> variables = new HashMap<>();
        variables.put("aborted", true);

        flowTaskInstance.complete(true);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(FLOW_INSTANCE_ID), eq(NODE_TYPE),
                eq(FlowableElementType.USER_TASK));
        verify(taskService, times(1)).createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list();
        verify(taskService, times(1)).complete(eq(task.getId()), eq(variables));
    }

}