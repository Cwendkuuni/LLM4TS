import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OGuard;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@Slf4j
public class FlowTaskInstanceTest {

 private FlowTaskInstance flowTaskInstance;
 private TaskService taskServiceMock;
 private EventPublisher eventPublisherMock;
 private ServiceTaskInstanceRepository serviceTaskRepositoryMock;
 private FlowableAdaptor flowableAdaptorMock;
 private NodeInstanceEntityRepository nodeRepositoryMock;
 private SequenceInstanceRepository sequenceRepositoryMock;

 @BeforeEach
 public void setup() {
 taskServiceMock = mock(TaskService.class);
 eventPublisherMock = mock(EventPublisher.class);
 serviceTaskRepositoryMock = mock(ServiceTaskInstanceRepository.class);
 flowableAdaptorMock = mock(FlowableAdaptor.class);
 nodeRepositoryMock = mock(NodeInstanceEntityRepository.class);
 sequenceRepositoryMock = mock(SequenceInstanceRepository.class);

 RuntimeDelegateMapper delegateConvertor = new RuntimeDelegateMapper();
 flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor, flowableAdaptorMock,
 eventPublisherMock, taskServiceMock, nodeRepositoryMock, sequenceRepositoryMock, serviceTaskRepositoryMock);
 }

 @Test
 public void testCreateFlowTaskInstance() {
 // Given
 TaskService taskService = Mockito.spy(taskServiceMock);
 EventPublisher eventPublisher = Mockito.spy(eventPublisherMock);
 ServiceTaskInstanceRepository serviceTaskRepository = Mockito.spy(serviceTaskRepositoryMock);
 FlowableAdaptor flowableAdaptor = Mockito.spy(flowableAdaptorMock);
 NodeInstanceEntityRepository nodeRepository = Mockito.spy(nodeRepositoryMock);
 SequenceInstanceRepository sequenceRepository = Mockito.spy(sequenceRepositoryMock);

 when(taskService.saveTask(any()))
 .thenAnswer(new Answer<Task>() {
 @Override
 public Task answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 });

 when(eventPublisher.publishEvent(any()))
 .thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 });

 when(serviceTaskRepositoryMock.save(any()))
 .thenAnswer(new Answer<ServiceTaskInstanceEntity>() {
 @Override
 public ServiceTaskInstanceEntity answer(InvocationOnMock invocation) throws Throwable {
 ServiceTaskInstanceEntity entity = (ServiceTaskInstanceEntity) invocation.getArguments()[0];
 return entity;
 }
 });

 when(flowableAdaptorMock.getFlowableElementByType(anyLong(), any(), any()))
 .thenReturn(Collections.emptyList());

 // When
 flowTaskInstance.create();

 // Then
 verify(eventPublisherMock, times(1)).publishEvent(any());
 }

 @Test
 public void testBatchCreateFlowTaskInstances() {
 // Given
 List<FlowTaskInstance> instances = Arrays.asList(new FlowTaskInstance(1L, TaskType.TASK_TYPE_1),
 new FlowTaskInstance(2L, TaskType.TASK_TYPE_2));
 ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

 // When
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepositoryMock);

 // Then
 Assert.assertEquals(instances.size(), result.size());
 for (int i = 0; i < instances.size(); i++) {
 Assert.assertEquals(instances.get(i).getId(), result.get(i).getId());
 }
 }

 @Test
 public void testUpdateFlowTaskInstance() {
 // Given
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_1, 1L, 1L, true, true,
 new RuntimeDelegateMapper(), new FlowableAdaptor(), new EventPublisher(), new TaskService(),
 new NodeInstanceEntityRepository(), new SequenceInstanceRepository(), new ServiceTaskInstanceRepository());
 when(serviceTaskRepositoryMock.update(any()))
 .thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });

 // When
 flowTaskInstance.update();

 // Then
 verify(serviceTaskRepositoryMock, times(1)).update(any());
 }

 @Test
 public void testDeleteFlowTaskInstance() {
 // Given
 Long instanceId = 1L;
 ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

 // When
 flowTaskInstance.doDelete(instanceId);

 // Then
 verify(serviceTaskRepositoryMock, times(1)).deleteById(instanceId);
 }

 @Test
 public void testBindServiceTask() {
 // Given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);

 // Then
 Assert.assertNotNull(flowTaskInstance.getTargetTaskHandle());
 Assert.assertEquals(flowTaskInstance.getTargetTaskHandle(), target);
 Assert.assertEquals(flowTaskInstance.getStatus(), FlowNodeStatus.EXECUTING);
 }

 @Test
 public void testDealloc() {
 // Given
 ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
 flowTaskInstance.alloc();
 flowTaskInstance.dealloc();

 // Then
 verify(eventPublisherMock, times(1)).removeEventListener(listener);
 verify(eventPublisherMock, never()).addEventListener(any());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // Given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);

 // Then
 Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testIsDone() {
 // Given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(target.isDone())
 .thenAnswer(new Answer<Boolean>() {
 @Override
 public Boolean answer(InvocationOnMock invocation) throws Throwable {
 return true;
 }
 });
 flowTaskInstance.bindServiceTask(target);

 // Then
 Assert.assertTrue(flowTaskInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // Given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(target.get())
 .thenReturn("Test result");
 flowTaskInstance.bindServiceTask(target);

 // When & Then
 Assert.assertEquals("Test result", flowTaskInstance.getResult());
 }

 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // Given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(target.get(1L, TimeUnit.SECONDS))
 .thenThrow(new TimeoutException("Test timeout"));
 flowTaskInstance.bindServiceTask(target);

 // Then
 Assert.assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1L, TimeUnit.SECONDS));
 }

 @Test
 public void testAbort() {
 // Given
 flowTaskInstance.abort();

 // Then
 verify(flowableAdaptorMock, times(1)).getFlowableElementByType(anyLong(), any(), any());
 verify(taskServiceMock, times(1)).complete(anyLong(), anyMap());
 }

 @Test
 public void testCancel() throws InterruptedException, ExecutionException {
 // Given
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(target.isDone())
 .thenReturn(false);
 when(target.cancel(anyBoolean()))
 .thenReturn(true);

 flowTaskInstance.bindServiceTask(target);

 // When
 flowTaskInstance.cancel(true);

 // Then
 verify(target, times(1)).cancel(true);
 }

 @AfterEach
 public void teardown() {
 reset(taskServiceMock, eventPublisherMock, serviceTaskRepositoryMock, flowableAdaptorMock, nodeRepositoryMock,
 sequenceRepositoryMock);
 }
}