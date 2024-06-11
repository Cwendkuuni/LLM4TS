import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

 private FlowTaskInstance testInstance;
 private ServiceTaskInstanceRepository serviceTaskRepository;
 private EventPublisher eventPublisher;
 private TaskService taskService;
 private NodeInstanceEntityRepository nodeRepository;
 private SequenceInstanceRepository sequenceRepository;
 private RuntimeDelegateMapper delegateConvertor;
 private FlowableAdaptor flowableAdaptor;
 private ServiceTaskInstanceEntity taskEntity;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 taskService = Mockito.mock(TaskService.class);
 nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);
 sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
 delegateConvertor = Mockito.mock(RuntimeDelegateMapper.class);
 flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
 testInstance = new FlowTaskInstance(null, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
 nodeRepository, sequenceRepository, serviceTaskRepository);
 }

 @Test
 public void testCreate() {
 // Given
 TaskType taskType = TaskType.USER_TASK;
 ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 60L, 5L);
 FlowTaskInstance testInstance = new FlowTaskInstance(taskType, 1L, 1L, strategyConfig, true, true,
 delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
 serviceTaskRepository);
 // When
 testInstance.create();
 // Then
 Mockito.verify(serviceTaskRepository).save(Mockito.any());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreate_alreadyExists() {
 // Given
 ServiceTaskInstanceEntity entity = Mockito.mock(ServiceTaskInstanceEntity.class);
 Mockito.when(serviceTaskRepository.save(entity)).thenReturn(entity);
 testInstance = new FlowTaskInstance(null, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
 nodeRepository, sequenceRepository, serviceTaskRepository);
 testInstance.create();
 Mockito.verify(serviceTaskRepository, Mockito.times(1)).save(entity);
 // When
 // Then
 testInstance.create();
 }

 @Test
 public void testUpdate() {
 // Given
 testInstance.setStatus(FlowNodeStatus.EXECUTING);
 // When
 testInstance.update();
 // Then
 Mockito.verify(serviceTaskRepository).update(Mockito.any());
 }

 @Test
 public void testDelete() {
 // Given
 long instanceId = 1L;
 // When
 testInstance.doDelete(instanceId);
 // Then
 Mockito.verify(serviceTaskRepository).deleteById(instanceId);
 }

 @Test
 public void testBindServiceTask() {
 // Given
 BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(mockDelegate);
 Mockito.verify(testInstance).setStatus(FlowNodeStatus.EXECUTING);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBindServiceTask_wrongInstanceId() {
 // Given
 BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.setId(1L);
 testInstance.bindServiceTask(mockDelegate);
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // Given
 BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(mockDelegate);
 // When
 // Then
 Assert.assertTrue(testInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 // Given
 ServiceTaskStartedListener listener = Mockito.mock(ServiceTaskStartedListener.class);
 testInstance.alloc();
 testInstance.dealloc();
 Mockito.verify(eventPublisher).removeEventListener(listener);
 }

 @Test
 public void testAlloc() {
 // Given
 ServiceTaskStartedListener listener = Mockito.mock(ServiceTaskStartedListener.class);
 testInstance.dealloc();
 testInstance.alloc();
 Mockito.verify(eventPublisher).addEventListener(listener);
 }

 @Test
 public void testConfirmExecute() {
 // Given
 testInstance.complete(false);
 // When
 testInstance.confirmExecute();
 // Then
 // TODO: Implement test for confirmExecute()
 }

 @Test
 public void testAbort() {
 // Given
 testInstance.complete(true);
 // When
 testInstance.abort();
 // Then
 // TODO: Implement test for abort()
 }

 @Test
 public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(mockDelegate);
 // When
 testInstance.cancel(true);
 // Then
 Mockito.verify(mockDelegate).cancel(true);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCancel_notPresentOnThisMachine() {
 // Given
 // When
 testInstance.cancel(false);
 // Then
 // Then
 }

 @Test
 public void testIsDone() {
 // Given
 BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(mockDelegate);
 // When
 // Then
 Assert.assertTrue(testInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // Given
 BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(mockDelegate);
 Mockito.when(mockDelegate.get()).thenReturn("test result");
 // When
 String result = testInstance.getResult();
 // Then
 Assert.assertEquals("test result", result);
 }

 @Test(expected = InterruptedException.class)
 public void testGetResult_interrupted() throws InterruptedException, ExecutionException {
 // Given
 BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(mockDelegate);
 // When
 testInstance.getResult();
 // Then
 // Then
 }

 @Test(expected = ExecutionException.class)
 public void testGetResult_executionException() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(mockDelegate);
 Mockito.when(mockDelegate.get()).thenThrow(new ExecutionException("test execution exception"));
 // When
 // Then
 // Then
 }

 @Test(expected = TimeoutException.class)
 public void testGetResult_timeoutException() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 BaseRuntimeFlowableDelegate mockDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 testInstance.bindServiceTask(mockDelegate);
 Mockito.when(mockDelegate.get(Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenThrow(new TimeoutException());
 // When
 // Then
 // Then
 }

 @Test
 public void testGetTargetDelegateClass() {
 // Given
 Class<? extends BaseRuntimeFlowableDelegate<?>> expected = BaseRuntimeFlowableDelegate.class;
 // When
 Class<? extends BaseRuntimeFlowableDelegate<?>> result = testInstance.getTargetDelegateClass();
 // Then
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testBatchCreate() {
 // Given
 List<FlowTaskInstance> instances = Collections.singletonList(testInstance);
 // When
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
 // Then
 Assert.assertEquals(instances, result);
 }
}