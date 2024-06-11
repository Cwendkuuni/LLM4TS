import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

 private static final Long FLOW_INSTANCE_ID = 1L;
 private static final Long TASK_ID = 1L;
 private static final Long TARGET_TASK_ID = 2L;
 private static final TaskType TASK_TYPE = TaskType.USER_TASK;
 private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;
 @Mock
 private NodeInstanceEntityRepository nodeInstanceRepository;
 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(flowableAdaptor.getFlowableElementByType(anyLong(), NODE_TYPE, FlowableElementType.USER_TASK)).thenReturn(Collections.emptyList());
 when(serviceTaskRepository.batchCreate(anyList())).thenReturn(Collections.emptyList());
 when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(new ServiceTaskInstanceEntity());
 when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);
 }

 @Test
 public void testCreateFlowTaskInstanceWithValidParameters() {
 // Given
 FlowTaskExecutionStrategy strategy = FlowTaskExecutionStrategy.MANUAL;
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TASK_TYPE, 1L, FLOW_INSTANCE_ID, strategy, false, false,
 new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService, nodeInstanceRepository,
 sequenceRepository, serviceTaskRepository);

 // When
 flowTaskInstance.create();

 // Then
 verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
 }

 @Test(expected = RuntimeException.class)
 public void testCreateFlowTaskInstanceWithExistingInstance() {
 // Given
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TASK_TYPE, 1L, FLOW_INSTANCE_ID, FlowTaskExecutionStrategy.MANUAL,
 false, false, new RuntimeDelegateMapper(), new FlowableAdaptor(), eventPublisher, taskService, nodeInstanceRepository,
 sequenceRepository, serviceTaskRepository);

 // When & Then
 Assert.assertThrows(RuntimeException.class, () -> flowTaskInstance.create());
 verify(serviceTaskRepository, never()).save(any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testBindServiceTask() {
 // Given
 BaseRuntimeFlowableDelegate<Object> target = mock(BaseRuntimeFlowableDelegate.class);

 // When
 flowTaskInstance.bindServiceTask(target);

 // Then
 verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
 }

 @Test
 public void testDealloc() {
 // Given
 flowTaskInstance.alloc();

 // When
 flowTaskInstance.dealloc();

 // Then
 verify(eventPublisher, times(1)).removeEventListener(flowTaskInstance.listener);
 }

 @Test
 public void testAlloc() {
 // Given
 flowTaskInstance.dealloc();
 // When
 flowTaskInstance.alloc();
 // Then
 verify(eventPublisher, times(1)).addEventListener(flowTaskInstance.listener);
 }

 @Test
 public void testConfirmExecute() {
 // Given
 doReturn(Optional.of(FLOW_INSTANCE_ID)).when(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 when(taskService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list()).thenReturn(Collections.emptyList());
 OngoingStubbing<List<FlowableElement>> elements = stub(flowableAdaptor.getFlowableElementByType(anyLong(), NODE_TYPE, FlowableElementType.USER_TASK));
 elements.thenReturn(Collections.singletonList(new FlowableElement()));

 // When
 flowTaskInstance.confirmExecute();

 // Then
 verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 verify(taskService, times(1)).complete(anyLong(), anyMap());
 }

 @Test
 public void testAbort() {
 // Given
 doReturn(Optional.of(FLOW_INSTANCE_ID)).when(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 when(taskService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list()).thenReturn(Collections.emptyList());
 OngoingStubbing<List<FlowableElement>> elements = stub(flowableAdaptor.getFlowableElementByType(anyLong(), NODE_TYPE, FlowableElementType.USER_TASK));
 elements.thenReturn(Collections.singletonList(new FlowableElement()));

 // When
 flowTaskInstance.abort();

 // Then
 verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 verify(taskService, times(1)).complete(anyLong(), anyMap());
 }

 @Test
 public void testCancel() {
 // Given
 doReturn(Optional.of(FLOW_INSTANCE_ID)).when(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 when(taskService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list()).thenReturn(Collections.singletonList(new TaskInfo()));

 // When
 flowTaskInstance.cancel(false);

 // Then
 verify(taskService, times(1)).cancel(anyLong(), false);
 }

 @Test
 public void testGetResult() {
 // Given
 BaseRuntimeFlowableDelegate<String> target = mock(BaseRuntimeFlowableDelegate.class);
 doReturn(target).when(flowTaskInstance).getTargetDelegateClass();
 when(target.get()).thenReturn("result");

 // When
 String result = flowTaskInstance.getResult();

 // Then
 Assert.assertEquals("result", result);
 }

 @Test(expected = InterruptedException.class)
 public void testGetResultWithInterruptedException() {
 // Given
 BaseRuntimeFlowableDelegate<String> target = mock(BaseRuntimeFlowableDelegate.class);
 doReturn(target).when(flowTaskInstance).getTargetDelegateClass();

 // When & Then
 Assert.assertThrows(InterruptedException.class, () -> flowTaskInstance.getResult());
 }

 @Test(expected = ExecutionException.class)
 public void testGetResultWithExecutionException() {
 // Given
 BaseRuntimeFlowableDelegate<String> target = mock(BaseRuntimeFlowableDelegate.class);
 doReturn(target).when(flowTaskInstance).getTargetDelegateClass();
 when(target.get()).thenThrow(new ExecutionException("test exception"));

 // When & Then
 Assert.assertThrows(ExecutionException.class, () -> flowTaskInstance.getResult());
 }

 @Test(expected = TimeoutException.class)
 public void testGetResultWithTimeoutException() {
 // Given
 BaseRuntimeFlowableDelegate<String> target = mock(BaseRuntimeFlowableDelegate.class);
 doReturn(target).when(flowTaskInstance).getTargetDelegateClass();
 doReturn(Optional.of(1000L)).when(taskService).getTimeout(anyLong(), any(Class.class));
 when(target.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException("test exception"));

 // When & Then
 Assert.assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1000L, TimeUnit.MILLISECONDS));
 }
}