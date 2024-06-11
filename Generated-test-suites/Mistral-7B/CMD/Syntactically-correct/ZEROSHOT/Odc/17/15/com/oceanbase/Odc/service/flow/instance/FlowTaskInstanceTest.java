package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

@Slf4j
public class FlowTaskInstanceTest {

 private FlowTaskInstance testInstance;
 private FlowableAdaptor flowableAdaptorMock;
 private NodeInstanceEntityRepository nodeRepositoryMock;
 private SequenceInstanceRepository sequenceRepositoryMock;
 private ServiceTaskInstanceRepository serviceTaskRepositoryMock;
 private TaskService taskServiceMock;
 private EventPublisher eventPublisherMock;
 private RuntimeDelegateMapper delegateConvertorMock;
 private ServiceTaskInstanceEntity taskInstanceEntityMock;
 private BaseRuntimeFlowableDelegate<?> targetTaskHandleMock;
 private ServiceTaskStartedListener listenerMock;

 @Before
 public void setup() {
 flowableAdaptorMock = Mockito.mock(FlowableAdaptor.class);
 nodeRepositoryMock = Mockito.mock(NodeInstanceEntityRepository.class);
 sequenceRepositoryMock = Mockito.mock(SequenceInstanceRepository.class);
 serviceTaskRepositoryMock = Mockito.mock(ServiceTaskInstanceRepository.class);
 taskServiceMock = Mockito.mock(TaskService.class);
 eventPublisherMock = Mockito.mock(EventPublisher.class);
 delegateConvertorMock = Mockito.mock(RuntimeDelegateMapper.class);
 listenerMock = Mockito.mock(ServiceTaskStartedListener.class);

 taskInstanceEntityMock = new ServiceTaskInstanceEntity();
 taskInstanceEntityMock.setId(1L);

 testInstance = new FlowTaskInstance(taskInstanceEntityMock, delegateConvertorMock, flowableAdaptorMock,
 eventPublisherMock, taskServiceMock, nodeRepositoryMock, sequenceRepositoryMock, serviceTaskRepositoryMock);
 }

 @Test
 public void testGetCoreFlowableElementType() {
 assertEquals(FlowableElementType.SERVICE_TASK, testInstance.getCoreFlowableElementType());
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = Collections.singletonList(testInstance);
 List<ServiceTaskInstanceEntity> entities = Mockito.mock(List.class);
 Mockito.when(serviceTaskRepositoryMock.batchCreate(entities)).thenReturn(entities);
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepositoryMock);
 assertEquals(instances, result);
 }

 @Test
 public void testCreate() {
 Verify.notNull(testInstance.getId(), "id");
 Verify.notNull(testInstance.getCreateTime(), "CreateTime");
 Verify.notNull(testInstance.getUpdateTime(), "UpdateTime");
 }

 @Test
 public void testUpdate() {
 testInstance.setId(1L);
 testInstance.setStatus(FlowNodeStatus.EXECUTING);
 testInstance.update();
 Mockito.verify(serviceTaskRepositoryMock).update(Mockito.any());
 }

 @Test
 public void testDelete() {
 testInstance.setId(1L);
 testInstance.doDelete(1L);
 Mockito.verify(serviceTaskRepositoryMock).deleteById(1L);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> targetMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(targetMock);
 assertSame(targetMock, testInstance.targetTaskHandle);
 }

 @Test
 public void testIsPresentOnThisMachine() {
 testInstance.targetTaskHandle = null;
 assertFalse(testInstance.isPresentOnThisMachine());

 testInstance.targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 assertTrue(testInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 testInstance.alloc();
 eventPublisherMock.removeEventListener(listenerMock);
 assertNull(testInstance.listener);
 }

 @Test
 public void testAlloc() {
 testInstance.dealloc();
 eventPublisherMock.addEventListener(listenerMock);
 assertSame(listenerMock, testInstance.listener);
 }

 @Test
 public void testConfirmExecute() {
 assertThrows(UnsupportedOperationException.class, testInstance::confirmExecute);
 }

 @Test
 public void testAbort() {
 assertThrows(UnsupportedOperationException.class, testInstance::abort);
 }

 @Test
 public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
 testInstance.alloc();
 targetTaskHandleMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(targetTaskHandleMock);
 testInstance.cancel(true);
 Mockito.verify(targetTaskHandleMock).cancel(true);
 }

 @Test
 public void testIsDone() {
 testInstance.alloc();
 targetTaskHandleMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(targetTaskHandleMock);
 Mockito.when(targetTaskHandleMock.isDone()).thenReturn(true);
 assertTrue(testInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 testInstance.alloc();
 targetTaskHandleMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(targetTaskHandleMock);
 Mockito.when(targetTaskHandleMock.get()).thenReturn("result");
 assertEquals("result", testInstance.getResult());
 }

 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 testInstance.alloc();
 targetTaskHandleMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(targetTaskHandleMock);
 Mockito.when(targetTaskHandleMock.get(10, TimeUnit.SECONDS)).thenThrow(new TimeoutException());
 assertThrows(TimeoutException.class, () -> testInstance.getResult(10, TimeUnit.SECONDS));
 }

 @Test
 public void testGetTargetDelegateClass() {
 Class<? extends BaseRuntimeFlowableDelegate<?>> result = testInstance.getTargetDelegateClass();
 assertEquals(BaseRuntimeFlowableDelegate.class, result.getComponentType());
 }

 @Test
 public void testResourceType() {
 assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, testInstance.resourceType());
 }

 @Test
 public void testComplete() {
 Verify.verify(flowableAdaptorMock.getFlowableElementByType(testInstance.getId(), FlowableElementType.USER_TASK,
 FlowableElement.class).isPresent(), "Can not find user task related to task instance");

 ArgumentCaptor<String> pidCaptor = ArgumentCaptor.forClass(String.class);
 Mockito.verify(taskServiceMock).createTaskQuery().processInstanceId(pidCaptor.capture()).list();
 Verify.verify(pidCaptor.getValue().equals(testInstance.getFlowInstanceId()));
 }
}