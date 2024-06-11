package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig.ExecutionTime;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig.Strategy;
import com.oceanbase.odc.service.flow.model.FlowNodeType.FlowNodeTypeEnum;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

 private FlowTaskInstance flowTaskInstance;
 private ServiceTaskInstanceRepository serviceTaskRepository;
 private NodeInstanceEntityRepository nodeRepository;
 private SequenceInstanceRepository sequenceRepository;
 private FlowableAdaptor flowableAdaptor;
 private EventPublisher eventPublisher;
 private TaskService taskService;
 private RuntimeDelegateMapper delegateConvertor;

 @Before
 public void setup() {
 serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
 nodeRepository = mock(NodeInstanceEntityRepository.class);
 sequenceRepository = mock(SequenceInstanceRepository.class);
 flowableAdaptor = mock(FlowableAdaptor.class);
 eventPublisher = mock(EventPublisher.class);
 taskService = mock(TaskService.class);
 delegateConvertor = mock(RuntimeDelegateMapper.class);

 flowTaskInstance = new FlowTaskInstance(
 new ServiceTaskInstanceEntity(), delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
 sequenceRepository, serviceTaskRepository);
 }

 @Test
 public void testCreateFlowTaskInstance() {
 // given
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setOrganizationId(1L);
 entity.setStatus(FlowNodeStatus.PENDING);
 entity.setFlowInstanceId(1L);
 entity.setCreateTime(1L);
 entity.setUpdateTime(1L);
 entity.setStartEndpoint(true);
 entity.setEndEndpoint(false);
 entity.setTargetTaskId(1L);
 entity.setStrategy(new ExecutionStrategyConfig(Strategy.MANUAL, ExecutionTime.IMMEDIATE));
 entity.setTaskType(TaskType.TASK_TYPE_1);

 when(nodeRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
 when(serviceTaskRepository.save(entity)).thenReturn(entity);

 // when
 flowTaskInstance.create();

 // then
 assertEquals(entity.getId(), flowTaskInstance.getId());
 assertEquals(entity.getCreateTime(), flowTaskInstance.getCreateTime());
 assertEquals(entity.getUpdateTime(), flowTaskInstance.getUpdateTime());
 assertEquals(entity.getOrganizationId(), flowTaskInstance.getOrganizationId());
 assertEquals(entity.getFlowInstanceId(), flowTaskInstance.getFlowInstanceId());
 assertEquals(entity.getStatus(), flowTaskInstance.getStatus());
 assertEquals(entity.isStartEndpoint(), flowTaskInstance.isStartEndpoint());
 assertEquals(entity.isEndEndpoint(), flowTaskInstance.isEndEndPoint());
 assertEquals(entity.getTaskType(), flowTaskInstance.getTaskType());
 assertEquals(entity.getStrategy(), flowTaskInstance.getStrategyConfig().getStrategy());
 assertEquals(entity.getTargetTaskId(), flowTaskInstance.getTargetTaskId());
 assertSame(entity, flowTaskInstance.getEntity());

verify(nodeRepository).findById(entity.getId());
verify(serviceTaskRepository).save(entity);
verify(eventPublisher).publishEvent(new TaskInstanceCreatedEvent(flowTaskInstance));
 }

 @Test
 public void testUpdateFlowTaskInstance() {
 // given
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setOrganizationId(1L);
 entity.setStatus(FlowNodeStatus.PENDING);
 entity.setFlowInstanceId(1L);
 entity.setCreateTime(1L);
 entity.setUpdateTime(1L);
 entity.setStartEndpoint(true);
 entity.setEndEndpoint(false);
 entity.setTargetTaskId(1L);
 entity.setStrategy(new ExecutionStrategyConfig(Strategy.MANUAL, ExecutionTime.IMMEDIATE));
 entity.setTaskType(TaskType.TASK_TYPE_1);

 when(nodeRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

 // when
 flowTaskInstance.update();

 // then
 assertEquals(entity.getId(), flowTaskInstance.getId());
 assertEquals(entity.getStatus(), flowTaskInstance.getStatus());
 assertEquals(entity.getTargetTaskId(), flowTaskInstance.getTargetTaskId());
 assertEquals(entity.getTaskType(), flowTaskInstance.getTaskType());
 assertEquals(entity.getStrategy(), flowTaskInstance.getStrategyConfig().getStrategy());

verify(nodeRepository).findById(entity.getId());
verifyZeroInteractions(serviceTaskRepository);
 }

 @Test
 public void testBatchCreateFlowTaskInstances() {
 // given
 List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

 // when
 List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

 // then
 assertEquals(entities.size(), instances.size());
 }

 @Test
 public void testValidNotExists() {
 // given
 Long instanceId = 1L;

 // when
 // then
 assertThrows(IllegalArgumentException.class, () -> flowTaskInstance.validNotExists(instanceId));
 }

 @Test
 public void testValidExists() {
 // given
 Long instanceId = 1L;

 // when
 // then
 assertThrows(IllegalArgumentException.class, () -> flowTaskInstance.validExists(instanceId));
 }

 @Test
 public void testDelete() {
 // given
 Long instanceId = 1L;

 // when
 // then
 verify(serviceTaskRepository).deleteById(instanceId);
 }

 @Test
 public void testBindServiceTask() {
 // given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(target.getClass());

 // when
 flowTaskInstance.bindServiceTask(target);

 // then
 assertEquals(target, flowTaskInstance.getTargetTaskHandle());
 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(target.getClass());
 when(taskService.createExecutableTaskQuery().taskDefinitionKey(TaskType.TASK_TYPE_1.name())).thenReturn(null);

 flowTaskInstance.bindServiceTask(target);

 // then
 assertFalse(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 // given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(target.getClass());
 when(taskService.createExecutableTaskQuery().taskDefinitionKey(TaskType.TASK_TYPE_1.name()))
 .thenReturn(Collections.singletonList(mock(Task.class)));
 when(eventPublisher.removeEventListener(any())).thenReturn(true);

 flowTaskInstance.bindServiceTask(target);
 flowTaskInstance.alloc();
 flowTaskInstance.dealloc();

 // then
 assertNull(flowTaskInstance.getListener());
 }

 @Test
 public void testAlloc() {
 // given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(target.getClass());
 when(taskService.createExecutableTaskQuery().taskDefinitionKey(TaskType.TASK_TYPE_1.name()))
 .thenReturn(Collections.singletonList(mock(Task.class)));
 when(eventPublisher.addEventListener(any())).thenReturn(true);

 flowTaskInstance.bindServiceTask(target);
 flowTaskInstance.dealloc();
 flowTaskInstance.alloc();

 // then
 assertSame(flowTaskInstance.getListener(), mock(ServiceTaskStartedListener.class));
 }

 @Test
 public void testConfirmExecute() {
 // given
 when(flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.USER_TASK.getCode(),
 FlowableElementType.USER_TASK))
 .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
 when(taskService.createTaskQuery().processInstanceId(anyString()).list())
 .thenReturn(Collections.singletonList(mock(Task.class)));

 // when
 flowTaskInstance.confirmExecute();

 // then
 verify(flowableAdaptor).getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.USER_TASK.getCode(),
 FlowableElementType.USER_TASK);
 verify(taskService).complete(anyLong(), anyMap());
 }

 @Test
 public void testAbort() {
 // given
 when(flowableAdaptor.getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.USER_TASK.getCode(),
 FlowableElementType.USER_TASK))
 .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
 when(taskService.createTaskQuery().processInstanceId(anyString()).list())
 .thenReturn(Collections.singletonList(mock(Task.class)));

 // when
 flowTaskInstance.abort();

 // then
 verify(flowableAdaptor).getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.USER_TASK.getCode(),
 FlowableElementType.USER_TASK);
 verify(taskService).complete(anyLong(), anyMap());
 verify(taskService).complete(anyLong(), anyMap());
 }

 @Test
 public void testCancel() {
 // given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(target.getClass());
 when(taskService.createExecutableTaskQuery().taskDefinitionKey(TaskType.TASK_TYPE_1.name()))
 .thenReturn(Collections.singletonList(mock(Task.class)));
 when(taskService.createTaskQuery().processInstanceId(anyString()).list())
 .thenReturn(Collections.singletonList(mock(Task.class)));

 // when
 // then
 flowTaskInstance.bindServiceTask(target);
 assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.cancel(false));
 }

 @Test
 public void testIsDone() {
 // given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(target.getClass());
 when(taskService.createExecutableTaskQuery().taskDefinitionKey(TaskType.TASK_TYPE_1.name()))
 .thenReturn(Collections.singletonList(mock(Task.class)));
 when(target.isDone()).thenReturn(false);

 // when
 // then
 assertFalse(flowTaskInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(target.getClass());
 when(taskService.createExecutableTaskQuery().taskDefinitionKey(TaskType.TASK_TYPE_1.name()))
 .thenReturn(Collections.singletonList(mock(Task.class)));
 when(target.get()).thenReturn("result");

 // when
 String result = flowTaskInstance.getResult();

 // then
 assertEquals("result", result);
 }

 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(target.getClass());
 when(taskService.createExecutableTaskQuery().taskDefinitionKey(TaskType.TASK_TYPE_1.name()))
 .thenReturn(Collections.singletonList(mock(Task.class)));
 when(target.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());

 // when
 // then
 assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1L, TimeUnit.SECONDS));
 }

 @Test
 public void testGetTargetDelegateClass() {
 // given
 ArgumentCaptor<TaskType> captor = ArgumentCaptor.forClass(TaskType.class);

 // when
 Class<?> clazz = flowTaskInstance.getTargetDelegateClass();

 // then
 assertEquals(clazz, delegateConvertor.map(captor.capture().getValue()).getClass());
 }

}