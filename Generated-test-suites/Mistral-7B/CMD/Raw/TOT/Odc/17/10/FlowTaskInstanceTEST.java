import org.apache.commons.collections4.EmptyMap;import org.apache.commons.collections4.MapUtils;import org.apache.commons.lang3.StringUtils;import org.flowable.engine.common.impl.interceptor.CommandContext;import org.flowable.engine.impl.persistence.entity.ExecutionEntity;import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;import org.flowable.engine.impl.persistence.entity.TaskEntity;import org.flowable.engine.impl.test.PluggableFlowableTestBase;import org.flowable.engine.impl.test.annotation.FlowableTest;import org.flowable.engine.task.Task;import org.junit.Test;import com.oceanbase.odc.core.shared.constant.TaskType;import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;import com.oceanbase.odc.service.flow.FlowableAdaptor;import com.oceanbase.odc.service.flow.FlowTaskInstance;import com.oceanbase.odc.service.flow.FlowableTestHelper;import com.oceanbase.odc.service.flow.ServiceTaskStartedListener;import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;import static org.hamcrest.CoreMatchers.is;import static org.hamcrest.CoreMatchers.notNullValue;import static org.hamcrest.MatcherAssert.*; import static org.mockito.ArgumentMatchers.any;import static org.mockito.Mockito.*;

@FlowableTest
public class FlowTaskInstanceTest extends PluggableFlowableTestBase {

 private FlowTaskInstance testInstance;
 private FlowableAdaptor flowableAdaptor;
 private SequenceInstanceRepository sequenceRepository;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private EventPublisher eventPublisher;
 private TaskService taskService;
 private NodeInstanceEntity nodeInstance;
 private ServiceTaskStartedListener listener;

 @Before
 public void setup() {
 serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
 eventPublisher = mock(EventPublisher.class);
 taskService = mock(TaskService.class);
 sequenceRepository = mock(SequenceInstanceRepository.class);
 flowableAdaptor = new FlowableAdaptor(null, null, null, null, null, null, null, null, null, null, null, null);
 listener = new ServiceTaskStartedListener(null);
 when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);

 testInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false,
 false, new RuntimeDelegateMapper(null), flowableAdaptor, eventPublisher, taskService, null, null, serviceTaskInstanceRepository);
 }

 @Test
 public void testCreate() {
 // Given
 assertNull(testInstance.getId());
 // When
 testInstance.create();
 // Then
 assertNotNull(testInstance.getId());
 verify(serviceTaskInstanceRepository).save(any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testCreateWithEvent() {
 // Given
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setCreateTime(System.currentTimeMillis());
 entity.setUpdateTime(System.currentTimeMillis());
 ServiceTaskInstanceRepository serviceTaskInstanceRepository = new ServiceTaskInstanceRepository();
 serviceTaskInstanceRepository.save(entity);
 when(serviceTaskInstanceRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(entity);
 when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);

 testInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false,
 false, new RuntimeDelegateMapper(null), flowableAdaptor, eventPublisher, taskService, null, null, serviceTaskInstanceRepository);
 // When
 testInstance.create();
 // Then
 verify(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));
 }

 @Test
 public void testUpdate() {
 // Given
 testInstance.setId(1L);
 // When
 testInstance.update();
 // Then
 verifyZeroInteractions(serviceTaskInstanceRepository);
 }

 @Test
 public void testDelete() throws Exception {
 // Given
 testInstance.setId(1L);
 // When
 testInstance.doDelete(1L);
 // Then
 verify(serviceTaskInstanceRepository).deleteById(1L);
 }

 @Test
 public void testBindServiceTask() {
 // Given
 RuntimeDelegateMock targetDelegate = mock(RuntimeDelegateMock.class);
 // When
 testInstance.bindServiceTask(targetDelegate);
 // Then
 assertEquals(testInstance.getId(), targetDelegate.getTargetTaskInstanceId());
 assertEquals(FlowNodeStatus.EXECUTING, testInstance.getStatus());
 }

 @Test
 public void testDealloc() {
 // Given
 testInstance.alloc();
 // When
 testInstance.dealloc();
 // Then
 verifyZeroInteractions(eventPublisher);
 }

 @Test
 public void testAlloc() {
 // Given
 // When
 testInstance.alloc();
 // Then
 verify(eventPublisher).addEventListener(eq(listener));
 }

 @Test
 public void testComplete() {
 // Given
 CommandContext commandContext = mock(CommandContext.class);
 ProcessDefinitionEntity processDefinitionEntity = mock(ProcessDefinitionEntity.class);
 ExecutionEntity executionEntity = mock(ExecutionEntity.class);
 when(processDefinitionEntity.getId()).thenReturn(1L);
 when(processDefinitionEntity.getName()).thenReturn("testProcess");
 when(commandContext.getProcessDefinitionEntity()).thenReturn(processDefinitionEntity);
 when(commandContext.getExecutionEntity()).thenReturn(executionEntity);
 when(taskService.createTaskQuery().list()).thenReturn(Collections.emptyList());

 testInstance.alloc();
 testInstance.completel();

 // Then
 verify(eventPublisher).publishEvent(any(TaskInstanceCompletedEvent.class));
 }

 @Test
 public void testAbort() {
 // Given
 testInstance.alloc();
 // When
 testInstance.abort();
 // Then
 verifyZeroInteractions(taskService);
 verifyZeroInteractions(eventPublisher);
 }

 @Test
 public void testCancel() throws Exception {
 // Given
 RuntimeDelegateMock targetDelegate = mock(RuntimeDelegateMock.class);
 testInstance.bindServiceTask(targetDelegate);
 // When
 testInstance.cancel(true);
 // Then
 verify(targetDelegate).cancel(true);
 }

 @Test
 public void testIsDone() {
 // Given
 testInstance.alloc();
 // When
 // Then
 assertTrue(testInstance.isDone());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // Given
 testInstance.alloc();
 // When
 // Then
 assertTrue(testInstance.isPresentOnThisMachine());
 }

 @Test
 public void testGetResult() throws Exception {
 // Given
 RuntimeDelegateMock targetDelegate = mock(RuntimeDelegateMock.class);
 testInstance.bindServiceTask(targetDelegate);
 when(targetDelegate.get()).thenReturn("testResult");
 // When
 String result = testInstance.getResult();
 // Then
 assertThat(result, is(notNullValue()));
 assertEquals("testResult", result);
 }

 @Test
 public void testGetResultWithTimeout() throws Exception {
 // Given
 RuntimeDelegateMock targetDelegate = mock(RuntimeDelegateMock.class);
 testInstance.bindServiceTask(targetDelegate);
 when(targetDelegate.get(anyLong(), any(TimeUnit.class))).thenReturn("testResult");
 // When
 String result = testInstance.getResult(1000, TimeUnit.MILLISECONDS);
 // Then
 assertThat(result, is(notNullValue()));
 assertEquals("testResult", result);
 }

 @Test(expected = TimeoutException.class)
 public void testGetResultWithTimeoutException() throws Exception {
 // Given
 RuntimeDelegateMock targetDelegate = mock(RuntimeDelegateMock.class);
 testInstance.bindServiceTask(targetDelegate);
 when(targetDelegate.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException("TimeoutException"));
 // When
 // Then
 fail("Should throw TimeoutException");
 }

 @Test
 public void testGetTargetDelegateClass() {
 // Given
 // When
 Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = testInstance.getTargetDelegateClass();
 // Then
 assertTrue(RuntimeDelegateMock.class.isAssignableFrom(targetDelegateClass));
 }

 @Test
 public void testCompleteWithEvent() {
 // Given
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setStatus(FlowNodeStatus.PENDING.name());
 entity.setFlowInstanceId(1L);
 entity.setTaskType(TaskType.USER_TASK.name());
 when(serviceTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(entity));
 when(taskService.createTaskQuery().list()).thenReturn(Collections.singletonList(new TaskEntity()));
 when(eventPublisher.publishEvent(any(TaskInstanceCompletedEvent.class))).thenReturn(null);

 testInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false,
 false, new RuntimeDelegateMapper(null), flowableAdaptor, eventPublisher, taskService, null, null, serviceTaskInstanceRepository);
 testInstance.alloc();
 testInstance.complete(true);
 // Then
 verify(eventPublisher).publishEvent(any(TaskInstanceCompletedEvent.class));
 }

 @Test
 public void testCreateWithTargetTaskId() {
 // Given
 // When
 testInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false,
 false, new RuntimeDelegateMapper(null), flowableAdaptor, eventPublisher, taskService, null, null, null, 1L);
 // Then
 assertEquals(1L, testInstance.getTargetTaskId());
 }

 @Test
 public void testGetStatus() {
 // Given
 // When
 String status = testInstance.getStatus();
 // Then
 assertEquals(FlowNodeStatus.PENDING.name(), status);
 }

 @Test
 public void testGetNodeType() {
 // Given
 // When
 String nodeType = testInstance.getNodeType();
 // Then
 assertEquals(FlowNodeType.SERVICE_TASK.name(), nodeType);
 }

 @Test
 public void testGetFlowInstanceId() {
 // Given
 // When
 Long flowInstanceId = testInstance.getFlowInstanceId();
 // Then
 assertEquals(1L, flowInstanceId);
 }

 @Test
 public void testGetCreateTime() {
 // Given
 // When
 Long createTime = testInstance.getCreateTime();
 // Then
 assertEquals(System.currentTimeMillis(), createTime);
 }

 @Test
 public void testGetUpdateTime() {
 // Given
 // When
 Long updateTime = testInstance.getUpdateTime();
 // Then
 assertEquals(System.currentTimeMillis(), updateTime);
 }

 @Test
 public void testIsStartEndpoint() {
 // Given
 // When
 boolean startEndpoint = testInstance.isStartEndpoint();
 // Then
 assertFalse(startEndpoint);
 }

 @Test
 public void testIsEndEndpoint() {
 // Given
 // When
 boolean endEndpoint = testInstance.isEndEndpoint();
 // Then
 assertFalse(endEndpoint);
 }

 @Test
 public void testGetTaskType() {
 // Given
 // When
 String taskType = testInstance.getTaskType();
 // Then
 assertEquals(TaskType.USER_TASK.name(), taskType);
 }

 @Test
 public void testIsAutomaticTask() {
 // Given
 // When
 boolean isAutomaticTask = testInstance.isAutomaticTask();
 // Then
 assertFalse(isAutomaticTask);
 }

 @Test
 public void testGetPendingExpireIntervalSeconds() {
 // Given
 // When
 Long pendingExpireIntervalSeconds = testInstance.getPendingExpireIntervalSeconds();
 // Then
 assertEquals(0L, pendingExpireIntervalSeconds);
 }

 @Test
 public void testGetExecutionTime() {
 // Given
 // When
 Long executionTime = testInstance.getExecutionTime();
 // Then
 assertEquals(0L, executionTime);
 }

 @Test
 public void testGetFlowableElementType() {
 // Given
 // When
 FlowableElementType flowableElementType = testInstance.getCoreFlowableElementType();
 // Then
 assertEquals(FlowableElementType.SERVICE_TASK, flowableElementType);
 }

 @Test
 public void testGetExecutionStrategy() {
 // Given
 // When
 FlowTaskExecutionStrategy executionStrategy = testInstance.getExecutionStrategy();
 // Then
 assertEquals(FlowTaskExecutionStrategy.MANUAL, executionStrategy);
 }

 @Test
 public void testGetTargetTaskId() {
 // Given
 // When
 Long targetTaskId = testInstance.getTargetTaskId();
 // Then
 assertEquals(null, targetTaskId);
 }

 @Test
 public void testSetTargetTaskId() {
 // Given
 // When
 testInstance.setTargetTaskId(1L);
 // Then
 assertEquals(1L, testInstance.getTargetTaskId());
 }

 @Test
 public void testToString() {
 // Given
 // When
 String toString = testInstance.toString();
 // Then
 assertTrue(StringUtils.isNotBlank(toString));
 }
}