package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Unit test for {@link FlowTaskInstance}
 */
public class FlowTaskInstanceTest {

    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long NODE_ID = 1L;
    private static final Long TARGET_TASK_ID = 2L;
    private static final TaskType TASK_TYPE = TaskType.EMAIL;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = ExecutionStrategyConfig.builder()
            .strategy(FlowTaskExecutionStrategy.MANUAL)
            .pendingExpireIntervalSeconds(5L)
            .executionTime(5L)
            .build();
    private static final Long ORGANIZATION_ID = 1L;
    private static final boolean IS_START_ENDPOINT = true;
    private static final boolean IS_END_ENDPOINT = false;
    private static final boolean IS_PRESENT_ON_THIS_MACHINE = false;
    private static final boolean ABORTED = false;

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskStartedListener listener;
    private EventPublisher eventPublisher;
    private TaskService taskService;
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private SequenceInstanceRepository sequenceInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private FlowableAdaptor flowableAdaptor;
    private RuntimeDelegateMapper delegateConvertor;

    @Before
    public void setUp() {
        listener = mock(ServiceTaskStartedListener.class);
        eventPublisher = mock(EventPublisher.class);
        taskService = mock(TaskService.class);
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);

        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class)))
                .thenReturn(null);

        when(serviceTaskInstanceRepository.save(any(ServiceTaskInstanceEntity.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        when(serviceTaskInstanceRepository.update(any(ServiceTaskInstanceEntity.class)))
                .thenAnswer(invocation -> 1);

        when(delegateConvertor.map(any(TaskType.class)))
                .thenReturn(mock(BaseRuntimeFlowableDelegate.class));

        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK))
                .thenReturn(Collections.singletonList(createUserTaskElement()));

        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(FLOW_INSTANCE_ID.toString()));

        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.emptyList());

        when(taskService.createTaskQuery().processInstanceId(anyString()).filter(t -> anyString().equals(t.getName()))
                        .list())
                .thenReturn(Collections.singletonList(createTaskInfo()));

        when(taskService.complete(anyLong(), anyMap()))
                .thenReturn(null);

        when(nodeInstanceEntityRepository.findById(anyLong()))
                .thenReturn(Optional.of(createNodeEntity()));

        when(sequenceInstanceRepository.findById(anyLong()))
                .thenReturn(Optional.of(createSequenceEntity()));

        flowTaskInstance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, IS_START_ENDPOINT,
                IS_END_ENDPOINT, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
    }

    @Test
    public void testCreateFlowTaskInstance() {
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
        verify(serviceTaskInstanceRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdateFlowTaskInstance() {
        verify(serviceTaskInstanceRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDeleteFlowTaskInstance() {
        doNothing().when(serviceTaskInstanceRepository).deleteById(ANY_LONG);
        flowTaskInstance.delete(FLOW_INSTANCE_ID);
        verify(serviceTaskInstanceRepository, times(1)).deleteById(FLOW_INSTANCE_ID);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBindServiceTaskToAnotherInstance() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(FLOW_INSTANCE_ID + 1L);
        flowTaskInstance.bindServiceTask(target);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(eq(listener));
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(eq(listener));
    }

    @Test
    public void testComplete() throws InterruptedException, ExecutionException, TimeoutException {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(createTaskInfo()));

        verify(taskService, times(1)).complete(anyLong(), anyMap());
        flowTaskInstance.complete(ABORTED);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteWithoutTasks() throws InterruptedException, ExecutionException, TimeoutException {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.emptyList());
        flowTaskInstance.complete(ABORTED);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteManualExecutionTasks() throws InterruptedException, ExecutionException, TimeoutException {
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(createTaskInfo()));

        when(taskService.complete(anyLong(), anyMap()))
                .thenThrow(new UnsupportedOperationException("Manual tasks do not support this execution"));
        flowTaskInstance.complete(ABORTED);
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance
                .getTargetDelegateClass();
        assertEquals(BaseRuntimeFlowableDelegate.class, targetDelegateClass);
    }

    @Test
    public void testIsDone() {
        assertFalse(flowTaskInstance.isDone());

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        verify(target, times(1)).isDone();
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn(new Object());
        flowTaskInstance.bindServiceTask(target);

        assertEquals(new Object(), flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(5, TimeUnit.SECONDS)).thenReturn(new Object());
        flowTaskInstance.bindServiceTask(target);

        assertEquals(new Object(), flowTaskInstance.getResult(5, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultWithTimeoutExpired() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(5, TimeUnit.SECONDS)).thenThrow(new InterruptedException());
        flowTaskInstance.bindServiceTask(target);

        try {
            flowTaskInstance.getResult(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // expected
        }
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultWithExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new ExecutionException("execution exception"));
        flowTaskInstance.bindServiceTask(target);

        try {
            flowTaskInstance.getResult();
        } catch (ExecutionException e) {
            // expected
            assertEquals("execution exception", e.getMessage());
        }
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultWithInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new InterruptedException());
        flowTaskInstance.bindServiceTask(target);

        try {
            flowTaskInstance.getResult();
        } catch (InterruptedException e) {
            // expected
        }
    }

    private FlowableElement createUserTaskElement() {
        FlowableElement userTaskElement = mock(FlowableElement.class);
        when(userTaskElement.getName()).thenReturn("testUserTask");
        return userTaskElement;
    }

    private TaskInfo createTaskInfo() {
        TaskInfo taskInfo = mock(TaskInfo.class);
        when(taskInfo.getId()).thenReturn(NODE_ID);
        when(taskInfo.getName()).thenReturn("testUserTask");
        return taskInfo;
    }

    private ServiceTaskInstanceEntity createServiceTaskEntity() {
        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
        serviceTaskInstanceEntity.setStatus(FlowNodeStatus.EXECUTING);
        serviceTaskInstanceEntity.setTargetTaskId(TARGET_TASK_ID);
        serviceTaskInstanceEntity.setTaskType(TASK_TYPE);
        serviceTaskInstanceEntity.setWaitExecExpireIntervalSeconds(STRATEGY_CONFIG.getPendingExpireIntervalSeconds());
        serviceTaskInstanceEntity.setExecutionTime(STRATEGY_CONFIG.getExecutionTime());
        return serviceTaskInstanceEntity;
    }

    private NodeInstanceEntity createNodeEntity() {
        NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
        nodeEntity.setId(NODE_ID);
        nodeEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeEntity.setFlowableElementType(FlowableElementType.USER_TASK);
        nodeEntity.setStatus(FlowNodeStatus.EXECUTING);
        nodeEntity.setNodeType(FlowNodeType.USER_TASK);
        nodeEntity.setCreateTime(System.currentTimeMillis());
        nodeEntity.setUpdateTime(System.currentTimeMillis());
        return nodeEntity;
    }

    private SequenceInstanceEntity createSequenceEntity() {
        SequenceInstanceEntity sequenceEntity = new SequenceInstanceEntity();
        sequenceEntity.setId(NODE_ID);
        sequenceEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
        sequenceEntity.setCreateTime(System.currentTimeMillis());
        sequenceEntity.setUpdateTime(System.currentTimeMillis());
        return sequenceEntity;
    }

}