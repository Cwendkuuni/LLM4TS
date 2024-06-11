import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FlowTaskInstanceTest {

 @Mock
 private RuntimeService runtimeService;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private NodeInstanceEntityRepository nodeRepository;
 @Mock
 private SequenceInstanceRepository sequenceRepository;
 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;
 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @BeforeEach
 void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 void testCreateFlowTaskInstance() throws Exception {
 // Given
 FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
 when(nodeRepository.findById(anyLong())).thenReturn(Optional.empty());
 when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(new ServiceTaskInstanceEntity());
 when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);

 // When
 flowTaskInstance = new FlowTaskInstance(new TaskType("TaskType"), 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
 new RuntimeDelegateMapper(), flowableAdaptor, eventPublisher, runtimeService, nodeRepository, sequenceRepository, serviceTaskRepository);

 // Then
 Assertions.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 Assertions.assertEquals(1L, flowTaskInstance.getOrganizationId());
 Assertions.assertEquals(1L, flowTaskInstance.getFlowInstanceId());
 Assertions.assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
 Assertions.assertNull(flowTaskInstance.getTargetTaskId());
 Assertions.assertNull(flowTaskInstance.getCreateTime());
 Assertions.assertNull(flowTaskInstance.getUpdateTime());
 Assertions.assertTrue(flowTaskInstance.isStartEndpoint());
 Assertions.assertTrue(flowTaskInstance.isEndEndpoint());
 Assertions.assertNull(flowTaskInstance.getId());
 Assertions.assertNull(flowTaskInstance.getFlowableAdaptor());
 Assertions.assertNull(flowTaskInstance.getNodeRepository());
 Assertions.assertNull(flowTaskInstance.getSequenceRepository());
 Assertions.assertNull(flowTaskInstance.getServiceTaskRepository());
 Assertions.assertNull(flowTaskInstance.getStrategyConfig());
 Assertions.assertNull(flowTaskInstance.getTaskType());
 Assertions.assertNull(flowTaskInstance.getTaskService());
 Assertions.assertNull(flowTaskInstance.getEventPublisher());
 Assertions.assertNull(flowTaskInstance.getDelegateConvertor());
 Assertions.assertNull(flowTaskInstance.getTargetTaskHandle());
 Assertions.assertNull(flowTaskInstance.getListener());

 // Verify
 Assertions.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
 }

 @Test
 void testCreateFlowTaskInstanceWithExistingNode() throws Exception {
 // Given
 when(nodeRepository.findById(anyLong())).thenReturn(Optional.of(new NodeInstanceEntity()));
 doThrow(new RuntimeException("Error")).when(serviceTaskRepository).save(any(ServiceTaskInstanceEntity.class));
 doThrow(new RuntimeException("Error")).when(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));

 // When & Then
 Assertions.assertThrows(RuntimeException.class, () -> new FlowTaskInstance(new TaskType("TaskType"), 1L, 1L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false, new RuntimeDelegateMapper(),
 new FlowableAdaptor(), eventPublisher, runtimeService, nodeRepository, sequenceRepository, serviceTaskRepository));
 }

 @Test
 void testBindServiceTask() {
 // Given
 BaseRuntimeFlowableDelegate mockTarget = mock(BaseRuntimeFlowableDelegate.class);

 // When
 flowTaskInstance.bindServiceTask(mockTarget);

 // Then
 Assertions.assertEquals(mockTarget, flowTaskInstance.getTargetTaskHandle());
 Assertions.assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 }

 @Test
 void testDealloc() {
 // Given
 ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
 flowTaskInstance.alloc();
 flowTaskInstance.addListener(listener);

 // When
 flowTaskInstance.dealloc();

 // Then
 Assertions.assertNull(flowTaskInstance.getListener());
 }

 @Test
 void testAlloc() {
 // Given

 // When
 flowTaskInstance.alloc();

 // Then
 Assertions.assertNotNull(flowTaskInstance.getListener());
 }

 @Test
 void testConfirmExecute() {
 // Given
 List<Task> tasks = Collections.singletonList(mock(Task.class));
 when(runtimeService.createTaskQuery().list()).thenReturn(tasks);

 // When
 flowTaskInstance.confirmExecute();

 // Then
 Assertions.assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
 }

 @Test
 void testAbort() {
 // Given
 List<Task> tasks = Collections.singletonList(mock(Task.class));
 when(runtimeService.createTaskQuery().list()).thenReturn(tasks);

 // When
 flowTaskInstance.abort();

 // Then
 Assertions.assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
 }

 @Test
 void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 List<Task> tasks = Collections.singletonList(mock(Task.class));
 when(runtimeService.createTaskQuery().list()).thenReturn(tasks);
 when(taskService.getTaskById(anyLong())).thenReturn(tasks.get(0));

 // When
 flowTaskInstance.alloc();
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 flowTaskInstance.cancel(true);

 // Then
 Assertions.assertFalse(flowTaskInstance.isPresentOnThisMachine());
 Assertions.assertEquals(FlowNodeStatus.CANCELED, flowTaskInstance.getStatus());
 }

 @Test
 void testIsDone() {
 // Given
 List<Task> tasks = Collections.singletonList(mock(Task.class));
 when(runtimeService.createTaskQuery().list()).thenReturn(tasks);
 when(taskService.getTaskById(anyLong())).thenReturn(tasks.get(0));
 when(taskService.getTaskById(anyLong())).thenReturn(null);

 // When
 flowTaskInstance.alloc();
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));

 // Then
 Assertions.assertFalse(flowTaskInstance.isDone());

 // When
 flowTaskInstance.getResult();

 // Then
 Assertions.assertTrue(flowTaskInstance.isDone());
 }

 @Test
 void testGetResult() throws InterruptedException, ExecutionException {
 // Given
 List<Task> tasks = Collections.singletonList(mock(Task.class));
 when(runtimeService.createTaskQuery().list()).thenReturn(tasks);
 when(taskService.getTaskById(anyLong())).thenReturn(tasks.get(0));
 when(taskService.getTaskById(anyLong())).thenReturn(null);

 // When
 flowTaskInstance.alloc();
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));

 // Then
 Assertions.assertThrows(IllegalStateException.class, () -> flowTaskInstance.getResult());

 // When
 flowTaskInstance.getResult(5, TimeUnit.SECONDS);

 // Then
 Assertions.assertNotNull(flowTaskInstance.getResult());
 }

 @Test
 void testGetTargetDelegateClass() {
 // Given

 // When
 Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();

 // Then
 Assertions.assertEquals(BaseRuntimeFlowableDelegate.class, result);
 }
}