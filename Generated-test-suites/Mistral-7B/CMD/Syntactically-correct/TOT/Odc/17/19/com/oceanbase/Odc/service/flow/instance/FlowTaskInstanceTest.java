import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.engine.runtime.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.flow.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptorMock;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

@ExtendWith(MockitoExtension.class)
public class FlowTaskInstanceTest {

 private static FlowTaskInstance flowTaskInstance;
 @Mock
 private static TaskService taskService;
 @Mock
 private static RuntimeService runtimeService;
 @Mock
 private static NodeInstanceEntityRepository nodeInstanceRepository;
 @Mock
 private static SequenceInstanceRepository sequenceInstanceRepository;
 @Mock
 private static ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 @Mock
 private static FlowableAdaptor flowableAdaptor;
 @Mock
 private static EventPublisher eventPublisher;
 @Mock
 private static ServiceTaskStartedListener serviceTaskStartedListener;

 @BeforeAll
 public static void setup() {
 FlowableAdaptorMock flowableAdaptorMock = new FlowableAdaptorMock();
 flowableAdaptorMock.init(nodeInstanceRepository, sequenceInstanceRepository, serviceTaskInstanceRepository,
 runtimeService, taskService);
 FlowableAdaptor flowableAdaptor = Mockito.spy(flowableAdaptorMock);
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 ServiceTaskInstanceRepository serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 Mockito.when(serviceTaskInstanceRepository.save(serviceTaskInstanceEntity)).thenReturn(serviceTaskInstanceEntity);
 EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
 TaskService taskService = Mockito.mock(TaskService.class);
 RuntimeService runtimeService = Mockito.mock(RuntimeService.class);
 NodeInstanceEntityRepository nodeInstanceRepository = Mockito.mock(NodeInstanceEntityRepository.class);
 SequenceInstanceRepository sequenceInstanceRepository = Mockito.mock(SequenceInstanceRepository.class);
 FlowableAdaptor flowableAdaptor = Mockito.spy(flowableAdaptor);
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, TaskType.SERVICE_TASK, 1L, 1L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
 RuntimeDelegateMapper.DEFAULT, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository,
 sequenceInstanceRepository, serviceTaskInstanceRepository);
 flowTaskInstance.alloc();
 }

 @Test
 public void testCreateFlowTaskInstanceWithValidArguments() {
 // Test creating a new FlowTaskInstance using valid arguments
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, TaskType.SERVICE_TASK, 1L, new ExecutionStrategyConfig(
 FlowTaskExecutionStrategy.MANUAL), false, false, RuntimeDelegateMapper.DEFAULT, flowableAdaptor, eventPublisher,
 taskService, nodeInstanceRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
 Assertions.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 Assertions.assertEquals(1L, flowTaskInstance.getOrganizationId());
 Assertions.assertEquals(1L, flowTaskInstance.getFlowInstanceId());
 Assertions.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
 Assertions.assertFalse(flowTaskInstance.isStartEndpoint());
 Assertions.assertFalse(flowTaskInstance.isEndEndPoint());
 Assertions.assertNull(flowTaskInstance.getTargetTaskId());
 Assertions.assertEquals(null, flowTaskInstance.getStrategyConfig().getStrategy());
 Assertions.assertNull(flowTaskInstance.getStrategyConfig().getPendingExpireIntervalSeconds());
 Assertions.assertNull(flowTaskInstance.getStrategyConfig().getExecutionTime());
 }

 @Test
 public void testCreateFlowTaskInstanceWithInvalidArguments() {
 // Test creating a new FlowTaskInstance using invalid arguments
 Assertions.assertThrows(IllegalArgumentException.class,
 () -> new FlowTaskInstance(null, null, 1L, new ExecutionStrategyConfig(null), false, false,
 RuntimeDelegateMapper.DEFAULT, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository,
 sequenceInstanceRepository, serviceTaskInstanceRepository));
 }

 @Test
 public void testCreateFlowTaskInstanceWithValidStrategyConfig() {
 // Test creating a new FlowTaskInstance using a valid ExecutionStrategyConfig
 ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL);
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, TaskType.SERVICE_TASK, 1L, strategyConfig, false, false,
 RuntimeDelegateMapper.DEFAULT, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository,
 sequenceInstanceRepository, serviceTaskInstanceRepository);
 Assertions.assertEquals(FlowTaskExecutionStrategy.MANUAL, flowTaskInstance.getStrategyConfig().getStrategy());
 Assertions.assertEquals(null, flowTaskInstance.getStrategyConfig().getPendingExpireIntervalSeconds());
 Assertions.assertEquals(null, flowTaskInstance.getStrategyConfig().getExecutionTime());
 }

 @Test
 public void testCreateFlowTaskInstanceWithInvalidStrategyConfig() {
 // Test creating a new FlowTaskInstance using an invalid ExecutionStrategyConfig
 Assertions.assertThrows(IllegalArgumentException.class,
 () -> new FlowTaskInstance(null, TaskType.SERVICE_TASK, 1L, null, false, false,
 RuntimeDelegateMapper.DEFAULT, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository,
 sequenceInstanceRepository, serviceTaskInstanceRepository));
 }

 @Test
 public void testCreateFlowTaskInstanceWithExistingInstance() {
 // Test creating a new FlowTaskInstance when one already exists
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, TaskType.SERVICE_TASK, 1L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
 RuntimeDelegateMapper.DEFAULT, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository,
 sequenceInstanceRepository, serviceTaskInstanceRepository);
 Assertions.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 Assertions.assertEquals(1L, flowTaskInstance.getOrganizationId());
 Assertions.assertEquals(1L, flowTaskInstance.getFlowInstanceId());
 Assertions.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
 Assertions.assertFalse(flowTaskInstance.isStartEndpoint());
 Assertions.assertFalse(flowTaskInstance.isEndEndPoint());
 Assertions.assertNull(flowTaskInstance.getTargetTaskId());
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = Mockito.mock(ServiceTaskInstanceEntity.class);
 Mockito.when(serviceTaskInstanceRepository.findByFlowInstanceIdAndNodeTypeAndNodeName(1L, FlowableElementType.SERVICE_TASK,
 "testUserTask")).thenReturn(serviceTaskInstanceEntity);
 Assertions.assertThrows(IllegalArgumentException.class, () -> new FlowTaskInstance(null, TaskType.SERVICE_TASK, 1L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
 RuntimeDelegateMapper.DEFAULT, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository,
 sequenceInstanceRepository, serviceTaskInstanceRepository));
 }

 @Test
 public void testCreateFlowTaskInstanceWithValidTargetTaskId() {
 // Test creating a new FlowTaskInstance with a valid targetTaskId
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.SERVICE_TASK, 1L, new ExecutionStrategyConfig(
 FlowTaskExecutionStrategy.MANUAL), false, false, RuntimeDelegateMapper.DEFAULT, flowableAdaptor, eventPublisher,
 taskService, nodeInstanceRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
 flowTaskInstance.setTargetTaskId(1L);
 Assertions.assertEquals(1L, flowTaskInstance.getTargetTaskId());
 }

 @Test
 public void testCreateFlowTaskInstanceWithInvalidTargetTaskId() {
 // Test creating a new FlowTaskInstance with an invalid targetTaskId
 Assertions.assertThrows(IllegalArgumentException.class, () -> new FlowTaskInstance(1L, TaskType.SERVICE_TASK, 1L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
 RuntimeDelegateMapper.DEFAULT, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository,
 sequenceInstanceRepository, serviceTaskInstanceRepository).setTargetTaskId(-1L));
 }

 @Test
 public void testBindServiceTaskWithNullTarget() {
 // Test binding a new FlowTaskInstance with a null target
 Assertions.assertThrows(NullPointerException.class, () -> flowTaskInstance.bindServiceTask(null));
 }

 @Test
 public void testBindServiceTaskWithValidTarget() {
 // Test binding a new FlowTaskInstance with a valid target
 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 Assertions.assertNotNull(flowTaskInstance.getTargetTaskHandle());
 Assertions.assertEquals(target, flowTaskInstance.getTargetTaskHandle());
 }

 @Test
 public void testDealloc() {
 // Test deallocating a new FlowTaskInstance
 flowTaskInstance.dealloc();
 Assertions.assertNull(flowTaskInstance.getListener());
 }

 @Test
 public void testAlloc() {
 // Test allocating a new FlowTaskInstance
 flowTaskInstance.dealloc();
 flowTaskInstance.alloc();
 Assertions.assertNotNull(flowTaskInstance.getListener());
 }

 @Test
 public void testCompleteWithValidArguments() {
 // Test completing a FlowTaskInstance with valid arguments
 // Assuming the taskHandle is already mocked and set
 flowTaskInstance.complete(false);
 ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
 Mockito.when(runtimeService.createProcessInstanceQuery().processInstanceId(1L).singleResult()).thenReturn(processInstance);
 Mockito.when(processInstance.getName()).thenReturn("testName");
 Mockito.when(taskService.createTaskQuery().processInstanceId(1L).list()).thenReturn(Collections.singletonList(Mockito.mock(Task.class)));
 Mockito.when(taskService.complete(Mockito.anyLong())).thenReturn(true);
 Assertions.assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
 }

 @Test
 public void testCompleteWithInvalidArguments() {
 // Test completing a FlowTaskInstance with invalid arguments
 Assertions.assertThrows(IllegalStateException.class, () -> flowTaskInstance.complete(null));
 }

 @Test
 public void testCancelWithValidArguments() {
 // Test canceling a FlowTaskInstance with valid arguments
 // Assuming the taskHandle is already mocked and set
 flowTaskInstance.cancel(true);
 Mockito.when(taskService.cancel(Mockito.anyLong(), true)).thenReturn(true);
 Assertions.assertEquals(FlowNodeStatus.CANCELED, flowTaskInstance.getStatus());
 }

 @Test
 public void testCancelWithInvalidArguments() {
 // Test canceling a FlowTaskInstance with invalid arguments
 Assertions.assertThrows(IllegalStateException.class, () -> flowTaskInstance.cancel(null));
 }

 @Test
 public void testGetResultWithValidArguments() {
 // Test getting the result of a completed FlowTaskInstance with valid arguments
 // Assuming the taskHandle is already mocked and set
 Object result = flowTaskInstance.getResult();
 Assertions.assertNotNull(result);
 }

 @Test
 public void testGetResultWithInvalidArguments() {
 // Test getting the result of a completed FlowTaskInstance with invalid arguments
 Assertions.assertThrows(IllegalStateException.class, () -> flowTaskInstance.getResult(1, TimeUnit.SECONDS));
 }

 @Test
 public void testGetResultWithTimeoutWithValidArguments() {
 // Test getting the result of a completed FlowTaskInstance with a timeout
 // Assuming the taskHandle is already mocked and set
 Object result = flowTaskInstance.getResult(1, TimeUnit.SECONDS);
 Assertions.assertNotNull(result);
 }

 @Test
 public void testGetResultWithTimeoutWithInvalidArguments() {
 // Test getting the result of a completed FlowTaskInstance with a timeout and invalid arguments
 Assertions.assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1, TimeUnit.MILLISECONDS));
 }

 @Test
 public void testGetResultWithRunningTask() {
 // Test getting the result of a running FlowTaskInstance
 Assertions.assertThrows(IllegalStateException.class, () -> flowTaskInstance.getResult());
 }

 @Test
 public void testGetResultWithNullTaskHandle() {
 // Test getting the result of a null FlowTaskInstance
 Assertions.assertThrows(NullPointerException.class, () -> flowTaskInstance.getResult());
 }

 @Test
 public void testGetTargetDelegateClass() {
 // Test getting the target delegate class of a FlowTaskInstance
 Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();
 Assertions.assertEquals(BaseRuntimeFlowableDelegate.class, targetDelegateClass);
 }
}