import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.FlowableElementType;
import com.oceanbase.odc.service.flow.TaskService;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.test.util.TestMockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

 private FlowTaskInstance instance;
 private EventPublisher eventPublisher;
 private ServiceTaskInstanceRepository serviceTaskRepository;
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;
 private SequenceInstanceRepository sequenceInstanceRepository;
 private FlowableAdaptor flowableAdaptor;
 private TaskService taskService;
 private RuntimeDelegateMapper delegateConvertor;
 private BaseRuntimeFlowableDelegate<?> targetTaskHandleMock;
 private ServiceTaskStartedListener listener;

 @Before
 public void setUp() {
 serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);
 sequenceInstanceRepository = Mockito.mock(SequenceInstanceRepository.class);
 flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 taskService = Mockito.mock(TaskService.class);
 delegateConvertor = Mockito.mock(RuntimeDelegateMapper.class);
 targetTaskHandleMock = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskRepository);
 listener = new ServiceTaskStartedListener(instance);
 }

 @After
 public void tearDown() {
 reset(serviceTaskRepository, nodeInstanceEntityRepository, sequenceInstanceRepository, flowableAdaptor,
 taskService, eventPublisher, delegateConvertor, targetTaskHandleMock);
 }

 @Test
 public void testCreate() {
 // given
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setCreateTime(System.currentTimeMillis());
 entity.setUpdateTime(System.currentTimeMillis());
 // when
 instance.create();
 // then
 assertEquals(entity.getId(), instance.getId());
 assertEquals(entity.getCreateTime(), instance.getCreateTime());
 assertEquals(entity.getUpdateTime(), instance.getUpdateTime());
 verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testUpdate() {
 // given
 instance.setId(1L);
 instance.setStatus(FlowNodeStatus.EXECUTING);
 // when
 instance.update();
 // then
 verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testBatchCreate() {
 // given
 List<FlowTaskInstance> instances = TestMockito.createListOfInstances(10);
 // when
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
 // then
 assertEquals(instances.size(), result.size());
 for (int i = 0; i < instances.size(); i++) {
 assertEquals(instances.get(i).getId(), result.get(i).getId());
 }
 verify(serviceTaskRepository, times(instances.size())).save(anyListOf(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testGetCoreFlowableElementType() {
 // given
 // when
 FlowableElementType result = instance.getCoreFlowableElementType();
 // then
 assertEquals(FlowableElementType.SERVICE_TASK, result);
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // given
 // when
 assertFalse(instance.isPresentOnThisMachine());
 // when
 instance.bindServiceTask(targetTaskHandleMock);
 // then
 assertTrue(instance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 // given
 instance.alloc();
 // when
 instance.dealloc();
 // then
 verify(eventPublisher, times(1)).removeEventListener(listener);
 }

 @Test
 public void testAlloc() {
 // given
 instance.dealloc();
 // when
 instance.alloc();
 // then
 verify(eventPublisher, times(1)).addEventListener(listener);
 }

 @Test
 public void testBindServiceTask() {
 // given
 // when
 instance.bindServiceTask(targetTaskHandleMock);
 // then
 assertEquals(instance.getId(), targetTaskHandleMock.getTargetTaskInstanceId());
 assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testBindServiceTaskDifferentId() {
 // given
 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 // when
 // then
 instance.bindServiceTask(target);
 }

 @Test
 public void testConfirmExecute() {
 // given
 // when
 instance.confirmExecute();
 // then
 // verify that all related user tasks are completed
 ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);
 verify(taskService, times(instance.getElements().size())).complete(any(String.class), eq(variablesCaptor.capture()));

 Map<String, Object> variables = variablesCaptor.getValue();
 assertTrue(variables.containsKey(FlowTaskInstance.ABORT_VARIABLE_NAME));
 assertFalse((Boolean) variables.get(FlowTaskInstance.ABORT_VARIABLE_NAME));
 }

 @Test
 public void testAbort() {
 // given
 // when
 instance.abort();
 // then
 // verify that all related user tasks are completed
 ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);
 verify(taskService, times(instance.getElements().size())).complete(any(String.class), eq(variablesCaptor.capture()));

 Map<String, Object> variables = variablesCaptor.getValue();
 assertTrue(variables.containsKey(FlowTaskInstance.ABORT_VARIABLE_NAME));
 assertTrue((Boolean) variables.get(FlowTaskInstance.ABORT_VARIABLE_NAME));
 }

 @Test
 public void testCancel() {
 // given
 // when
 instance.cancel(true);
 // then
 verify(targetTaskHandleMock, times(1)).cancel(true);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCancelNotPresent() {
 // given
 // when
 instance.cancel(true);
 // then
 // exception is thrown because the task isn't present on this machine
 }

 @Test
 public void testIsDone() {
 // given
 when(targetTaskHandleMock.isDone()).thenReturn(true);
 // when
 assertTrue(instance.isDone());
 }

 @Test
 public void testGetResult() throws Exception {
 // given
 when(targetTaskHandleMock.get()).thenReturn("result");
 // when
 String result = instance.getResult();
 // then
 assertEquals("result", result);
 }

 @Test
 public void testGetResultTimeout() throws Exception {
 // given
 when(targetTaskHandleMock.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());
 // when
 // then
 try {
 instance.getResult(100, TimeUnit.MILLISECONDS);
 fail("Exception expected");
 } catch (TimeoutException e) {
 // expected
 }
 }

 @Test
 public void testGetResultInterrupted() throws Exception {
 // given
 when(targetTaskHandleMock.get()).thenThrow(new InterruptedException());
 // when
 // then
 try {
 instance.getResult();
 fail("Exception expected");
 } catch (InterruptedException e) {
 // expected
 }
 }

 @Test
 public void testGetTargetDelegateClass() {
 // given
 // when
 Class<? extends BaseRuntimeFlowableDelegate<?>> result = instance.getTargetDelegateClass();
 // then
 assertEquals(BaseRuntimeFlowableDelegate.class, result);
 }

 @Test
 public void testComplete() {
 // given
 // when
 instance.complete(false);
 // then
 verify(taskService, times(instance.getElements().size())).complete(any(String.class), eq(anyMap()));
 }

 @Test
 public void testMapToTaskEntity() {
 // given
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 FlowTaskInstance instance = new FlowTaskInstance(null, FlowTaskExecutionStrategy.MANUAL, false, false,
 delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeInstanceEntityRepository,
 sequenceInstanceRepository, serviceTaskRepository);
 // when
 ServiceTaskInstanceEntity result = FlowTaskInstance.mapToTaskEntity(instance);
 // then
 assertEquals(instance.getId(), result.getId());
 assertEquals(instance.getOrganizationId(), result.getOrganizationId());
 assertEquals(instance.getStatus(), result.getStatus());
 assertEquals(instance.getFlowInstanceId(), result.getFlowInstanceId());
 assertEquals(instance.isStartEndpoint(), result.isStartEndpoint());
 assertEquals(instance.isEndEndpoint(), result.isEndEndpoint());
 assertEquals(instance.getStrategy(), result.getStrategy());
 assertEquals(instance.getTaskType(), result.getTaskType());
 assertEquals(instance.getTargetTaskId(), result.getTargetTaskId());
 assertEquals(instance.getWaitExecExpireIntervalSeconds(), result.getWaitExecExpireIntervalSeconds());
 assertEquals(instance.getExecutionTime(), result.getExecutionTime());
 }
}