package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;

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
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Flow node instance tests
 *
 * @author yh263208
 * @date 2023-03-22 13:34
 * @since ODC_release_3.3.0
 */
@Slf4j
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private ServiceTaskStartedListener listener;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private TaskInfo taskInfo;
    @Mock
    private Task task;
    @Mock
    private FlowTaskInstance instance;
    @Mock
    private FlowableElementType userTaskType;

    private FlowTaskInstance sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sut = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(new ServiceTaskInstanceEntity());
        when(nodeRepository.getById(anyLong())).thenReturn(new NodeInstanceEntity());
        when(sequenceRepository.getById(anyLong())).thenReturn(new SequenceInstanceEntity());
        when(serviceTaskRepository.deleteById(anyLong())).thenReturn(true);
        when(taskService.createTaskQuery()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().processInstanceId(anyString())).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(task));
        when(taskService.complete(anyLong(), anyMap())).thenReturnVoid();
        when(taskInfo.getId()).thenReturn(1L);
        when(taskInfo.getName()).thenReturn("userTaskName");
        when(instance.getTaskType()).thenReturn(TaskType.USER_TASK);
        when(instance.getId()).thenReturn(1L);
        when(instance.getFlowInstanceId()).thenReturn(1L);
        when(instance.getOrganizationId()).thenReturn(1L);
        when(instance.getStatus()).thenReturn(FlowNodeStatus.PENDING);
        when(instance.isStartEndpoint()).thenReturn(true);
        when(instance.isEndEndPoint()).thenReturn(false);
        when(instance.strategyConfig.getStrategy()).thenReturn(FlowTaskExecutionStrategy.MANUAL);
        when(instance.strategyConfig.getPendingExpireIntervalSeconds()).thenReturn(30L);
        when(instance.strategyConfig.getExecutionTime()).thenReturn(10L);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK)).thenReturn(Collections.singletonList(userTaskType));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("processInstanceId"));

    }

    @Test
    public void testFlowTaskInstanceCreation() {
        assertEquals(FlowNodeType.SERVICE_TASK, sut.getNodeType());
        assertEquals(1, sut.getId());
        assertEquals(1, sut.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, sut.getStatus());
        assertEquals(1, sut.getFlowInstanceId());
        assertEquals(true, sut.isStartEndpoint());
        assertEquals(false, sut.isEndEndPoint());
        assertEquals(TaskType.USER_TASK, sut.getTaskType());
        assertEquals(FlowTaskExecutionStrategy.MANUAL, sut.strategyConfig.getStrategy());
        assertEquals(30, sut.strategyConfig.getPendingExpireIntervalSeconds());
        assertEquals(10, sut.strategyConfig.getExecutionTime());
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(sut);
        List<ServiceTaskInstanceEntity> entities = serviceTaskRepository.batchCreate(instances.stream()
                .map(FlowTaskInstance::mapToTaskEntity).collect(Collectors.toList()));
        assertEquals(1, entities.size());
        assertEquals(1, entities.get(0).getId());
        assertEquals(1, entities.get(0).getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, entities.get(0).getStatus());
        assertEquals(1, entities.get(0).getFlowInstanceId());
        assertEquals(true, entities.get(0).isStartEndpoint());
        assertEquals(false, entities.get(0).isEndEndPoint());
        assertEquals(TaskType.USER_TASK, entities.get(0).getTaskType());
        assertEquals(FlowTaskExecutionStrategy.MANUAL, entities.get(0).getStrategy());
        assertEquals(30, entities.get(0).getPendingExpireIntervalSeconds());
        assertEquals(10, entities.get(0).getExecutionTime());
    }

    @Test
    public void testCreate() {
        sut.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdate() {
        sut.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDelete() {
        sut.doDelete(1L);
        verify(serviceTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        sut.bindServiceTask(target);
        verify(sut).setStatus(FlowNodeStatus.EXECUTING);
        verify(sut).setTargetTaskHandle(target);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        sut.bindServiceTask(target);
        assertTrue(sut.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        sut.bindServiceTask(target);
        verify(target, times(1)).isDone();
        assertTrue(sut.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        sut.bindServiceTask(target);
        Object result = sut.getResult();
        verify(target, times(1)).get();
        assertEquals(result, target.get());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        sut.bindServiceTask(target);
        Object result = sut.getResult(10, TimeUnit.SECONDS);
        verify(target, times(1)).get(10, TimeUnit.SECONDS);
        assertEquals(result, target.get(10, TimeUnit.SECONDS));
    }

    @Test
    public void testComplete() {
        sut.complete(false);
        verify(taskService, times(1)).complete(taskInfo.getId(), anyMap());
    }

    @Test
    public void testAbort() {
        sut.abort();
        verify(taskService, times(1)).complete(taskInfo.getId(), anyMap());
        assertTrue(sut.getVariables().getOrDefault(ABORT_VARIABLE_NAME, false));
    }

    @Test
    public void testCancel() {
        sut.cancel(true);
        verify(taskService, times(1)).cancel(taskInfo.getId(), true);
    }

}