import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeRepositoryMock;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

 private static final long FLOW_INSTANCE_ID = 1L;
 private static final long ORGANIZATION_ID = 1L;
 private static final long TASK_ID = 1L;
 private static final long TARGET_TASK_ID = 2L;
 private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
 private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5000L);
 private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
 private static final FlowNodeStatus FLOW_NODE_STATUS = FlowNodeStatus.PENDING;
 private static final FlowNodeStatus FLOW_NODE_STATUS_EXECUTING = FlowNodeStatus.EXECUTING;
 private static final FlowNodeStatus FLOW_NODE_STATUS_COMPLETED = FlowNodeStatus.COMPLETED;
 private static final String PROCESS_INSTANCE_ID = "processInstanceId";

 private TaskService taskService;
 private EventPublisher eventPublisher;
 private NodeInstanceEntityRepository nodeRepository;
 private ServiceTaskInstanceRepository serviceTaskRepository;
 private FlowableAdaptor flowableAdaptor;
 private FlowTaskInstance flowTaskInstance;
 private RuntimeDelegateMapper runtimeDelegateMapper;
 private NodeRepositoryMock nodeRepositoryMock;

 @Before
 public void setUp() {
 taskService = mock(TaskService.class);
 eventPublisher = mock(EventPublisher.class);
 nodeRepository = mock(NodeInstanceEntityRepository.class);
 serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
 flowableAdaptor = mock(FlowableAdaptor.class);
 runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);
 nodeRepositoryMock = new NodeRepositoryMock();
 when(nodeRepository.getById(NODE_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID)).thenReturn(nodeRepositoryMock);
 when(nodeRepositoryMock.getFlowableElementByType(NODE_TYPE, FLOW_INSTANCE_ID, FlowableElementType.USER_TASK))
 .thenReturn(Collections.singletonList(new FlowableElement("UserTask")));
 when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(new ServiceTaskInstanceEntity());
 when(serviceTaskRepository.deleteById(anyLong())).thenReturn(1);
 when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(1);
 when(taskService.createTaskQuery().processInstanceId(PROCESS_INSTANCE_ID).list())
 .thenReturn(Collections.singletonList(new TaskInfo("Task1", "", "", "", "", PROCESS_INSTANCE_ID, null, null, null)));
 when(runtimeDelegateMapper.map(eq(TASK_TYPE))).thenReturn(BaseRuntimeFlowableDelegate.class);
 when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);
 when(eventPublisher.removeEventListener(any(ServiceTaskStartedListener.class))).thenReturn(null);
 when(eventPublisher.addEventListener(any(ServiceTaskStartedListener.class))).thenReturn(null);
 flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeRepository, serviceTaskRepository);
 }

 @Test
 public void testGetCoreFlowableElementType() {
 assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
 assertTrue(result.size() == 1);
 assertEquals(flowTaskInstance, result.get(0));
 }

 @Test
 public void testCreate() throws Exception {
 flowTaskInstance.create();
 assertNotNull(flowTaskInstance.getId());
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateAlreadyExists() throws Exception {
 flowTaskInstance.create();
 flowTaskInstance.create();
 }

 @Test
 public void testUpdate() throws Exception {
 flowTaskInstance.update();
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateDoesNotExist() throws Exception {
 FlowTaskInstance newFlowTaskInstance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, false, false, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeRepository, serviceTaskRepository);
 newFlowTaskInstance.update();
 }

 @Test
 public void testDelete() throws Exception {
 flowTaskInstance.delete(FLOW_INSTANCE_ID);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeleteDoesNotExist() throws Exception {
 FlowTaskInstance newFlowTaskInstance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, false, false, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeRepository, serviceTaskRepository);
 newFlowTaskInstance.delete(FLOW_INSTANCE_ID);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 assertEquals(flowTaskInstance, target.getTargetTaskInstance());
 assertEquals(FLOW_NODE_STATUS_EXECUTING, flowTaskInstance.getStatus());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 assertFalse(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testIsPresentOnThisMachineWithBoundDelegate() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 flowTaskInstance.dealloc();
 assertNull(flowTaskInstance.getListener());
 }

 @Test
 public void testAlloc() {
 assertNull(flowTaskInstance.getListener());
 flowTaskInstance.alloc();
 assertNotNull(flowTaskInstance.getListener());
 }

 @Test
 public void testConfirmExecute() throws Exception {
 flowTaskInstance.confirmExecute();
 assertEquals(FLOW_NODE_STATUS_COMPLETED, flowTaskInstance.getStatus());
 }

 @Test
 public void testAbort() throws Exception {
 flowTaskInstance.abort();
 assertEquals(FLOW_NODE_STATUS_COMPLETED, flowTaskInstance.getStatus());
 }

 @Test
 public void testCancel() throws Exception {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 flowTaskInstance.cancel(true);
 assertTrue(flowTaskInstance.isDone());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCancelWithoutBoundDelegate() throws Exception {
 flowTaskInstance.cancel(true);
 }

 @Test
 public void testIsDone() throws Exception {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 assertTrue(flowTaskInstance.isDone());
 }

 @Test
 public void testGetResult() throws Exception {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 Object result = flowTaskInstance.getResult();
 assertNotNull(result);
 }

 @Test(expected = InterruptedException.class)
 public void testGetResultInterrupted() throws Exception {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 Thread.sleep(100);
 flowTaskInstance.getResult();
 }

 @Test(expected = ExecutionException.class)
 public void testGetResultExecutionException() throws Exception {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 when(taskService.getExecution(anyLong(), anyLong())).thenThrow(new ExecutionException("Error"));
 flowTaskInstance.getResult();
 }

 @Test(expected = TimeoutException.class)
 public void testGetResultTimeoutException() throws Exception {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
 when(taskService.getExecution(anyLong(), anyLong())).thenReturn(null);
 flowTaskInstance.getResult(1000, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testGetTargetDelegateClass() {
 Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();
 assertEquals(BaseRuntimeFlowableDelegate.class, targetDelegateClass);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetTargetDelegateClassWithWrongTaskType() {
 Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();
 fail("Should not reach this point");
 }

 @Test
 public void testComplete() throws Exception {
 flowTaskInstance.complete(false);
 assertEquals(FLOW_NODE_STATUS_COMPLETED, flowTaskInstance.getStatus());
 }

 @Test
 public void testCompleteWithAborted() throws Exception {
 flowTaskInstance.complete(true);
 assertEquals(FLOW_NODE_STATUS_COMPLETED, flowTaskInstance.getStatus());
 }

}