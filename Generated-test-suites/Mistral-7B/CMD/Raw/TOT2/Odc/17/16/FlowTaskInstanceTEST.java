import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

 private static final Long TEST_ID = 1L;
 private static final Long TEST_FLOW_INSTANCE_ID = 1L;
 private static final Long TEST_ORGANIZATION_ID = 1L;
 private static final FlowableElementType TEST_NODE_TYPE = FlowableElementType.SERVICE_TASK;
 private static final TaskType TEST_TASK_TYPE = TaskType.TEST_TASK_TYPE;
 private static final ExecutionStrategyConfig TEST_STRATEGY_CONFIG = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 1L, 5L);
 private static final boolean TEST_START_ENDPOINT = true;
 private static final boolean TEST_END_ENDPOINT = true;
 private FlowTaskInstance testInstance;
 private EventPublisher eventPublisher;
 private TaskService taskService;
 private ServiceTaskInstanceRepository serviceTaskRepository;
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;
 private SequenceInstanceRepository sequenceInstanceRepository;
 private RuntimeDelegateMapper delegateConvertor;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 eventPublisher = mock(EventPublisher.class);
 taskService = mock(TaskService.class);
 serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
 nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
 sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
 delegateConvertor = mock(RuntimeDelegateMapper.class);
 when(delegateConvertor.map(any())).thenReturn(mock(BaseRuntimeFlowableDelegate.class));
 when(taskService.createTaskQuery().processInstanceId(TEST_FLOW_INSTANCE_ID).list()).thenReturn(Collections.emptyList());
 when(nodeInstanceEntityRepository.getByNodeInstanceId(any())).thenReturn(null);
 when(sequenceInstanceRepository.getByFlowInstanceId(TEST_FLOW_INSTANCE_ID)).thenReturn(null);
 testInstance = new FlowTaskInstance(TEST_TASK_TYPE, TEST_ORGANIZATION_ID, TEST_FLOW_INSTANCE_ID, TEST_STRATEGY_CONFIG, TEST_START_ENDPOINT, TEST_END_ENDPOINT, delegateConvertor, mock(FlowableAdaptor.class), eventPublisher, taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskRepository);
 }

 @Test
 public void testCreate() {
 doAnswer(new Answer<ServiceTaskInstanceEntity>() {
 @Override
 public ServiceTaskInstanceEntity answer(ServiceTaskInstanceEntity entity) throws Exception {
 ServiceTaskInstanceEntity savedEntity = new ServiceTaskInstanceEntity();
 savedEntity.setId(TEST_ID);
 savedEntity.setCreateTime(System.currentTimeMillis());
 savedEntity.setUpdateTime(System.currentTimeMillis());
 return savedEntity;
 }
 }).when(serviceTaskRepository).save(any());
 flowTaskInstance.create();
 assertEquals(TEST_ID, testInstance.getId());
 }

 @Test
 public void testUpdate() {
 doAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(Object obj) throws Throwable {
 return 1;
 }
 }).when(serviceTaskRepository).update((ServiceTaskInstanceEntity) any());
 flowTaskInstance.update();
 }

 @Test
 public void testDelete() {
 doAnswer(new Answer<Void>() {
 @Override
 public Void answer(Long instanceId) throws Throwable {
 return null;
 }
 }).when(serviceTaskRepository).deleteById(TEST_ID);
 flowTaskInstance.doDelete(TEST_ID);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(taskService.createTaskQuery().processInstanceId(TEST_FLOW_INSTANCE_ID).list()).thenReturn(Collections.singletonList(mock(Task.class)));
 doAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 flowTaskInstance.bindServiceTask(invocation.getArgument(0));
 }
 }).when(testInstance).setStatus(FlowNodeStatus.EXECUTING);
 testInstance.bindServiceTask(target);
 assertEquals(FlowNodeStatus.EXECUTING, testInstance.getStatus());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeallocNoListener() {
 testInstance.dealloc();
 }

 @Test
 public void testAlloc() {
 testInstance.alloc();
 }

 @Test
 public void testComplete() throws InterruptedException, ExecutionException, TimeoutException {
 doAnswer(new Answer<List<Task>>() {
 @Override
 public List<Task> answer(InvocationOnMock invocation) throws Throwable {
 return Collections.singletonList(mock(Task.class));
 }
 }).when(taskService).createTaskQuery().processInstanceId(TEST_FLOW_INSTANCE_ID).list();
 doAnswer(new Answer<Map<String, Object>>() {
 @Override
 public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable {
 Map<String, Object> variables = new HashMap<>();
 variables.put(ABORT_VARIABLE_NAME, false);
 return variables;
 }
 }).when(taskService).complete((String) any(), anyMap());
 when(nodeInstanceEntityRepository.getByNodeInstanceId(any())).thenReturn(mock(FlowableElement.class));
 when(flowableAdaptor.getFlowableElementByType(anyLong(), any(), any())).thenReturn(Collections.singletonList(mock(FlowableElement.class)));
 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(TEST_FLOW_INSTANCE_ID)).thenReturn(Optional.of(TEST_FLOW_INSTANCE_ID));
 testInstance.complete();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAbort() {
 testInstance.abort();
 }

 @Test
 public void testCancel() {
 doAnswer(new Answer<Void>() {
 @Override
 public Void answer(Boolean mayInterruptIfRunning) throws Throwable {
 return null;
 }
 }).when(testInstance.targetTaskHandle).cancel(mayInterruptIfRunning);
 testInstance.cancel(true);
 }

 @Test
 public void testIsDone() {
 doAnswer(new Answer<Boolean>() {
 @Override
 public Boolean answer(InvocationOnMock invocation) throws Throwable {
 return true;
 }
 }).when(testInstance.targetTaskHandle).isDone();
 assertEquals(true, testInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 Object result = new Object();
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return result;
 }
 }).when(testInstance.targetTaskHandle).get();
 assertEquals(result, testInstance.getResult());
 }

 @Test(expected = InterruptedException.class)
 public void testGetResultInterrupted() throws InterruptedException {
 testInstance.getResult();
 }

 @Test(expected = ExecutionException.class)
 public void testGetResultExecutionException() throws InterruptedException, ExecutionException {
 doThrow(new ExecutionException("Test exception")).when(testInstance.targetTaskHandle).get();
 testInstance.getResult();
 }

 @Test(expected = TimeoutException.class)
 public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new TimeoutException("Test timeout exception");
 }
 }).when(testInstance.targetTaskHandle).get(anyLong(), any(TimeUnit.class));
 testInstance.getResult(1L, TimeUnit.SECONDS);
 }

 @Test
 public void testGetTargetDelegateClass() {
 assertEquals(BaseRuntimeFlowableDelegate.class, testInstance.getTargetDelegateClass());
 }
}