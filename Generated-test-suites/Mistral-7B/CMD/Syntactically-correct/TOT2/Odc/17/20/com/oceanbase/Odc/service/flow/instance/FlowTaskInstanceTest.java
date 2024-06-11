import org.apache.commons.lang3.RandomUtils;import org.junit.Before;import org.junit.Test;import org.junit.runner.RunWith;import org.mockito.Mock;import org.mockito.MockitoAnnotations;import org.mockito.junit.MockitoJUnitRunner;import com.oceanbase.odc.common.event.EventPublisher;import com.oceanbase.odc.core.flow.model.FlowableElement;import com.oceanbase.odc.core.flow.model.FlowableElementType;import com.oceanbase.odc.core.flow.model.FlowNodeStatus;import com.oceanbase.odc.core.flow.model.FlowNodeType;import com.oceanbase.odc.core.shared.constant.ResourceType;import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;import com.oceanbase.odc.service.flow.FlowableAdaptor;import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;import com.oceanbase.odc.service.flow.FlowTaskInstance;import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class) public class FlowTaskInstanceTest {

 @Mock private EventPublisher eventPublisher;
 @Mock private TaskService taskService;
 @Mock private NodeInstanceEntityRepository nodeRepository;
 @Mock private SequenceInstanceRepository sequenceRepository;
 @Mock private ServiceTaskInstanceRepository serviceTaskRepository;
 @Mock private FlowableAdaptor flowableAdaptor;
 @Mock private ServiceTaskInstanceEntity taskInstanceEntity;
 @Mock private ServiceTaskStartedListener listener;

 private static final long TEST_FLOW_INSTANCE_ID = RandomUtils.nextLong(); private static final long TEST_ORGANIZATION_ID = RandomUtils.nextLong(); private static final long TEST_TASK_ID = RandomUtils.nextLong(); private static final long TEST_TARGET_TASK_ID = RandomUtils.nextLong(); private static final TaskType TEST_TASK_TYPE = TaskType.TEST_TASK_TYPE; private static final FlowNodeType TEST_NODE_TYPE = FlowNodeType.SERVICE_TASK; private static final FlowNodeStatus TEST_NODE_STATUS = FlowNodeStatus.EXECUTING; private static final FlowTaskExecutionStrategy TEST_STRATEGY = FlowTaskExecutionStrategy.MANUAL; private FlowTaskInstance flowTaskInstance;

 @Before public void setup() { MockitoAnnotations.initMocks(this); flowableAdaptor = mock(FlowableAdaptor.class); eventPublisher = mock(EventPublisher.class); taskService = mock(TaskService.class); nodeRepository = mock(NodeInstanceEntityRepository.class); sequenceRepository = mock(SequenceInstanceRepository.class); serviceTaskRepository = mock(ServiceTaskInstanceRepository.class); listener = mock(ServiceTaskStartedListener.class); flowTaskInstance = new FlowTaskInstance(
 new ServiceTaskInstanceEntity(), new RuntimeDelegateMapper(), flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 flowTaskInstance.setFlowInstanceId(TEST_FLOW_INSTANCE_ID); flowTaskInstance.setOrganizationId(TEST_ORGANIZATION_ID); flowTaskInstance.setTaskType(TEST_TASK_TYPE); flowTaskInstance.setNodeType(TEST_NODE_TYPE); flowTaskInstance.setStrategyConfig(new ExecutionStrategyConfig(TEST_STRATEGY)); flowTaskInstance.setId(TEST_TASK_ID); flowTaskInstance.setStatus(TEST_NODE_STATUS);
}

 @Test public void testCreate() { verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class)); }

 @Test public void testBatchCreate() { verify(serviceTaskRepository, times(1)).batchCreate(anyList()); }

 @Test public void testUpdate() { verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class)); }

 @Test public void testDelete() { verify(serviceTaskRepository, times(1)).deleteById(TEST_TASK_ID); }

 @Test public void testIsPresentOnThisMachine() { verify(targetTaskHandle, never()).isDone(); verify(targetTaskHandle, times(1)).getId(); verify(targetTaskHandle, times(1)).getTargetTaskInstanceId(); assertEquals(TEST_TASK_ID, flowTaskInstance.getId()); assertTrue(flowTaskInstance.isPresentOnThisMachine()); }

 @Test public void testIsNotPresentOnThisMachine() { verify(targetTaskHandle, never()).isDone(); verify(targetTaskHandle, never()).getId(); verify(targetTaskHandle, never()).getTargetTaskInstanceId(); assertFalse(flowTaskInstance.isPresentOnThisMachine()); }

 @Test public void testGetFlowableElementType() { assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType()); }

 @Test public void testBindServiceTask() { verify(targetTaskHandle, times(1)).bindServiceTask(flowableAdaptor); }

 @Test public void testDealloc() { verify(eventPublisher, times(1)).removeEventListener(listener); }

 @Test public void testAlloc() { verify(eventPublisher, times(1)).addEventListener(listener); }

 @Test public void testConfirmExecute() { verify(flowableAdaptor, times(1)).getFlowableElementByType(TEST_TASK_ID, TEST_NODE_TYPE, FlowableElementType.USER_TASK); verify(taskService, times(1)).createTaskQuery(); verify(taskService, times(1)).list(); verify(taskService, times(1)).complete(anyLong(), anyMap()); }

 @Test public void testAbort() { verify(flowableAdaptor, times(1)).getFlowableElementByType(TEST_TASK_ID, TEST_NODE_TYPE, FlowableElementType.USER_TASK); verify(taskService, times(1)).createTaskQuery(); verify(taskService, times(1)).list(); verify(taskService, times(1)).complete(anyLong(), anyMap()); verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(TEST_FLOW_INSTANCE_ID); }

 @Test public void testCancel() { verify(targetTaskHandle, times(1)).cancel(anyBoolean()); }

 @Test public void testIsDone() { verify(targetTaskHandle, times(1)).isDone(); }

 @Test public void testGetResult() throws Exception { verify(targetTaskHandle, times(1)).get(); }

 @Test public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException { verify(targetTaskHandle, times(1)).get(anyLong(), any(TimeUnit.class)); }

 @Test public void testGetResultError() throws InterruptedException, ExecutionException { verify(targetTaskHandle, times(1)).isDone(); verify(log, times(1)).warn(anyString(), any(Throwable.class)); }

 @Test public void testComplete() { verify(flowableAdaptor, times(1)).getFlowableElementByType(TEST_TASK_ID, TEST_NODE_TYPE, FlowableElementType.USER_TASK); verify(taskService, times(1)).createTaskQuery(); verify(taskService, times(1)).list(); verify(taskService, times(1)).complete(anyLong(), anyMap()); }

 @Test public void testCompleteWithAborted() { verify(flowableAdaptor, times(1)).getFlowableElementByType(TEST_TASK_ID, TEST_NODE_TYPE, FlowableElementType.USER_TASK); verify(taskService, times(1)).createTaskQuery(); verify(taskService, times(1)).list(); verify(taskService, times(1)).complete(anyLong(), anyMap()); verify(taskService, times(1)).complete(anyLong(), argThat(map -> map.containsKey(ABORT_VARIABLE_NAME) && map.get(ABORT_VARIABLE_NAME) == true)); }

}