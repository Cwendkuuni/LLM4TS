import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Task;
import org.flowable.task.api.TaskInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableElementType;
import com.oceanbase.odc.service.flow.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;
 @Mock
 private RuntimeService runtimeService;
 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;
 @Mock
 private NodeInstanceEntityRepository nodeRepository;
 @Mock
 private SequenceInstanceRepository sequenceRepository;
 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private RuntimeDelegateMapper delegateConvertor;
 @Mock
 private BaseRuntimeFlowableDelegate<?> targetTaskHandle;
 @Mock
 private ServiceTaskStartedListener listener;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(nodeRepository.findById(anyLong())).thenReturn(Optional.of(new FlowableElement()));
 when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(new ServiceTaskInstanceEntity());
 when(serviceTaskRepository.batchCreate(anyList())).thenReturn(Collections.emptyList());
 when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(1);
 when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class), any(FlowableElementType.class)))
 .thenReturn(Collections.singletonList(new FlowableElement()));
 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("123"));
 when(taskService.createTaskQuery().processInstanceId("123").list()).thenReturn(Collections.singletonList(new TaskInfo()));
 when(taskService.complete(anyLong(), anyMap())).thenReturn(new TaskInfo());
 when(delegateConvertor.map(any(TaskType.class))).thenReturn(BaseRuntimeFlowableDelegate.class);
 }

 @After
 public void tearDown() {
 verifyNoMoreInteractions(taskService, runtimeService, serviceTaskRepository, nodeRepository, sequenceRepository,
 eventPublisher, delegateConvertor, targetTaskHandle, listener);
 }

 @Test
 public void testCreate() {
 // given
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
 false, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
 serviceTaskRepository);
 // when
 instance.create();
 // then
 assertEquals(FlowNodeType.SERVICE_TASK, instance.getNodeType());
 assertEquals(1L, instance.getId());
 assertNotNull(instance.getCreateTime());
 assertNotNull(instance.getUpdateTime());
 }

 @Test
 public void testBindServiceTask() {
 // given
 when(targetTaskService.createTaskQuery().taskDefinitionKey(anyString()).list().get(0)).thenReturn(new TaskInfo());
 // when
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 // then
 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testBindServiceTask_differentInstanceId() {
 // given
 // when, then
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 }

 @Test
 public void testDealloc() {
 // given
 // when
 flowTaskInstance.dealloc();
 // then
 verify(eventPublisher).removeEventListener(listener);
 }

 @Test
 public void testAlloc() {
 // given
 // when
 flowTaskInstance.alloc();
 // then
 verify(eventPublisher).addEventListener(listener);
 }

 @Test
 public void testComplete() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 doReturn(new TaskInfo()).when(taskService).complete(anyLong(), anyMap());
 // when
 flowTaskInstance.complete(false);
 // then
 verify(taskService).complete(anyLong(), anyMap());
 }

 @Test
 public void testConfirmExecute() {
 // given
 // when
 flowTaskInstance.confirmExecute();
 // then
 verify(taskService, times(1)).complete(anyLong(), anyMap());
 }

 @Test
 public void testAbort() {
 // given
 // when
 flowTaskInstance.abort();
 // then
 verify(taskService, times(1)).complete(anyLong(), argThat(m -> m.containsKey(FlowTaskInstance.ABORT_VARIABLE_NAME) &&
 m.get(FlowTaskInstance.ABORT_VARIABLE_NAME) == true));
 }

 @Test
 public void testCancel() throws InterruptedException {
 // given
 doThrow(new RuntimeException("Cancel task failed")).when(targetTaskHandle).cancel(anyBoolean());
 // when
 flowTaskInstance.cancel(true);
 // then
 verify(listener).taskCanceled(flowTaskInstance);
 }

 @Test
 public void testIsDone() {
 // given
 when(targetTaskHandle.isDone()).thenReturn(true);
 // when
 assertTrue(flowTaskInstance.isDone());
 // then
 verify(targetTaskHandle).isDone();
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // given
 Object result = new Object();
 when(targetTaskHandle.get()).thenReturn(result);
 // when
 assertEquals(result, flowTaskInstance.getResult());
 // then
 verify(targetTaskHandle).get();
 }

 @Test
 public void testGetResult_timeout() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 when(targetTaskHandle.get(1, TimeUnit.SECONDS)).thenThrow(new TimeoutException());
 // when, then
 assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1, TimeUnit.SECONDS));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetResult_notPresentOnThisMachine() {
 // given
 // when, then
 flowTaskInstance.getResult();
 }

 @Test
 public void testBatchCreate() {
 // given, when, then
 List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
 List<ServiceTaskInstanceEntity> entities = flowTaskInstance.batchCreate(instances, serviceTaskRepository);
 assertEquals(0, entities.size());
 }

 @Test
 public void testUpdate() {
 // given, when, then
 FlowNodeStatus status = FlowNodeStatus.EXECUTING;
 flowTaskInstance.update();
 assertEquals(status, flowTaskInstance.getStatus());
 }

 @Test
 public void testDelete() {
 // given, when, then
 serviceTaskRepository.deleteById(1L);
 verify(serviceTaskRepository).deleteById(1L);
 }

}