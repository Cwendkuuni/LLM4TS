import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;
 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;
 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private BaseRuntimeFlowableDelegate<?> targetTaskHandle;
 @Mock
 private RuntimeDelegateMapper delegateConvertor;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private TaskService taskService;
 @Mock
 private NodeInstanceEntityRepository nodeRepository;
 @Mock
 private SequenceInstanceRepository sequenceRepository;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(delegateConvertor.map(TaskType.USER_TASK)).thenReturn(RuntimeDelegateMapper.USER_TASK_DELEGATE_CLASS);
 when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenAnswer(i -> i.getArguments()[0]);
 when(taskService.createTaskQuery()).thenReturn(mock(List.class));
 when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
         .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of("12345"));
 }

 @Test
 public void testCreateFlowTaskInstance() {
 // Test creating a new FlowTaskInstance with valid arguments
 FlowTaskInstance newFlowTaskInstance = new FlowTaskInstance(TaskType.USER_TASK, 1L, 1L,
         new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10L), false, false,
         delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
         serviceTaskRepository);

 // Verify that the instance was created and saved in the repository
 verify(serviceTaskRepository, times(1)).save(new FlowTaskInstanceEntity(1L, 1L, FlowNodeType.SERVICE_TASK,
         1L, "12345", FlowNodeStatus.CREATED, FlowableElementType.SERVICE_TASK, false, false, null, null,
         FlowTaskExecutionStrategy.MANUAL, 10L, null, null, null, null));

 // Test publishing the TaskInstanceCreatedEvent
 verify(eventPublisher, times(1)).publishEvent(isA(TaskInstanceCreatedEvent.class));
 }

 @Test
 public void testCreateFlowTaskInstanceWithNullArguments() {
 // Test creating a new FlowTaskInstance with null arguments
 FlowTaskInstance newFlowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false,
         delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
         serviceTaskRepository);

 // Verify that an IllegalArgumentException is thrown
 try {
 newFlowTaskInstance.create();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBindServiceTask() {
 // Test binding a service task to the FlowTaskInstance
 BaseRuntimeFlowableDelegate<?> targetDelegate = mock(BaseRuntimeFlowableDelegate.class);
 when(targetDelegate.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
 when(serviceTaskRepository.findById(flowTaskInstance.getId())).thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

 flowTaskInstance.bindServiceTask(targetDelegate);

 // Verify that the status is set to EXECUTING
 verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
 }

 @Test
 public void testCancelTask() throws InterruptedException, ExecutionException, TimeoutException {
 // Test canceling a task that is not present on this machine
 doReturn(false).when(flowTaskInstance).isPresentOnThisMachine();

 try {
 flowTaskInstance.cancel(true);
 fail("IllegalStateException expected");
 } catch (IllegalStateException e) {
 // Expected
 }

 // Test canceling a task that is present on this machine
 doReturn(true).when(flowTaskInstance).isPresentOnThisMachine();
 doReturn(targetTaskHandle).when(flowTaskInstance).getTargetTaskHandle();
 when(targetTaskHandle.isDone()).thenReturn(false);
 when(targetTaskHandle.cancel(true)).thenReturn(null);

 flowTaskInstance.cancel(true);

 // Verify that the cancel method was called on the target task handle
 verify(targetTaskHandle).cancel(true);
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // Test getting the result of a completed task
 doReturn(true).when(flowTaskInstance).isPresentOnThisMachine();
 doReturn("Result").when(flowTaskInstance).getTargetTaskHandle().get();

 String result = flowTaskInstance.getResult();

 assertEquals("Result", result);
 }
}