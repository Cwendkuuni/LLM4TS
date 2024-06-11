import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class FlowTaskInstanceTest {

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
 private RuntimeDelegateMapper delegateConvertor;
 @Mock
 private FlowableAdaptor flowableAdaptor;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @BeforeEach
 void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 void testCreateFlowTaskInstance() {
 // Given
 TaskType taskType = new TaskType("testTaskType");
 Long organizationId = 1L;
 Long flowInstanceId = 1L;
 ExecutionStrategyConfig executionStrategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL);
 boolean startEndpoint = true;
 boolean endEndPoint = false;

 // When
 flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, executionStrategyConfig, startEndpoint, endEndPoint, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 // Then
 Assertions.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 Assertions.assertEquals(organizationId, flowTaskInstance.getOrganizationId());
 Assertions.assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
 Assertions.assertEquals(startEndpoint, flowTaskInstance.isStartEndpoint());
 Assertions.assertEquals(endEndPoint, flowTaskInstance.isEndEndPoint());
 Assertions.assertEquals(executionStrategyConfig.getStrategy(), flowTaskInstance.getStrategy());

 verify(nodeRepository, times(1)).save(any());
 verify(sequenceRepository, times(1)).save(any());
 verify(serviceTaskRepository, times(1)).save(any());
 verify(eventPublisher, times(1)).publishEvent(any());
 }

 @Test
 void testBatchCreate() {
 // Given
 List<FlowTaskInstance> flowTaskInstances = Collections.singletonList(new FlowTaskInstance(new TaskType("testTaskType"), 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), true, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository));

 // When
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(flowTaskInstances, serviceTaskRepository);

 // Then
 Assertions.assertEquals(flowTaskInstances, result);
 }

 @Test
 void testGetCoreFlowableElementType() {
 // Given

 // When
 FlowableElementType result = flowTaskInstance.getCoreFlowableElementType();

 // Then
 Assertions.assertEquals(FlowableElementType.SERVICE_TASK, result);
 }

 @Test
 void testBindServiceTask() {
 // Given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 // When
 flowTaskInstance.bindServiceTask(target);

 // Then
 Assertions.assertNotNull(flowTaskInstance.getTargetTaskHandle());
 Assertions.assertEquals(flowTaskInstance, flowTaskInstance.getTargetTaskHandle().getTaskInstance());
 }

 @Test
 void testAbort() {
 // Given
 // When
 flowTaskInstance.abort();

 // Then
 verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId());
 List<Task> tasks = mock(List.class);
 when(taskService.createTaskQuery().processInstanceId(anyString()).list()).thenReturn(tasks);
 tasks.forEach(task -> verify(taskService, times(1)).complete(task.getId(), anyMap()));
 }

 @Test
 void testComplete() {
 // Given
 // When
 flowTaskInstance.complete(false);

 // Then
 verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK);
 String processInstanceId = "testProcessInstanceId";
 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId())).thenReturn(processInstanceId);
 List<Task> tasks = mock(List.class);
 when(taskService.createTaskQuery().processInstanceId(processInstanceId).list()).thenReturn(tasks);
 tasks.forEach(task -> verify(taskService, times(1)).complete(task.getId(), anyMap()));
 }

 @Test
 void testCancel() {
 // Given
 // When
 flowTaskInstance.cancel(true);

 // Then
 verify(targetTaskHandle, times(1)).cancel(true);
 }

 @Test
 void testIsDone() {
 // Given
 // When
 // Then
 Assertions.assertTrue(flowTaskInstance.isDone());
 }

 @Test
 void testGetResult() throws InterruptedException, ExecutionException {
 // Given
 Object result = new Object();
 when(targetTaskHandle.get()).thenReturn(result);

 // When
 Object resultFromTest = flowTaskInstance.getResult();

 // Then
 Assertions.assertEquals(result, resultFromTest);
 }

 @Test
 void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 Object result = new Object();
 when(targetTaskHandle.get(3L, TimeUnit.SECONDS)).thenReturn(result);

 // When
 Object resultFromTest = flowTaskInstance.getResult(3L, TimeUnit.SECONDS);

 // Then
 Assertions.assertEquals(result, resultFromTest);
 }
}