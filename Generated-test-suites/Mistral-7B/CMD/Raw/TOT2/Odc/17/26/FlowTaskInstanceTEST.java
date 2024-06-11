import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FlowTaskInstanceTest {

 private static final Long FLOW_INSTANCE_ID = 1L;
 private static final Long ORGANIZATION_ID = 1L;
 private static final Long TASK_TYPE = TaskType.TASK_A.getValue();
 private static final ExecutionStrategyConfig STRATEGY_CONFIG = ExecutionStrategyConfig.builder()
         .strategy(FlowTaskExecutionStrategy.MANUAL)
         .pendingExpireIntervalSeconds(10)
         .executionTime(100)
         .build();
 private static final FlowNodeType NODE_TYPE = FlowNodeType.USER_TASK;
 private static final Long TARGET_TASK_ID = 2L;
 private static final Long ID = 3L;
 private static final Long CREATE_TIME = 1L;
 private static final Long UPDATE_TIME = 2L;
 private static final boolean START_ENDPOINT = true;
 private static final boolean END_ENDPOINT = false;

 @Spy
 private FlowableAdaptor flowableAdaptor;
 @InjectMocks
 private FlowTaskInstance flowTaskInstance;
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

 @BeforeEach
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(NODE_TYPE), any(FlowableElementType.class)))
 .thenReturn(Collections.singletonList(new FlowableElement()));
 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID)).thenReturn(Optional.of(1L));
 when(nodeRepository.findById(anyLong())).thenReturn(Optional.of(new NodeInstanceEntity()));
 when(sequenceRepository.findByNodeInstanceId(anyLong())).thenReturn(Optional.empty());
 when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class)))
 .thenAnswer(invocation -> invocation.getArgument(0));
 when(serviceTaskRepository.batchCreate(anyList()))
 .thenAnswer(invocation -> invocation.getArgument(0));
 when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class)))
 .thenAnswer(invocation -> 1);
 when(delegateConvertor.map(eq(TASK_TYPE)))
 .thenReturn(Mockito.mock(BaseRuntimeFlowableDelegate.class));
 }

 @Test
 public void testCreate() {
 // given
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, START_ENDPOINT, END_ENDPOINT,
 flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 // when
 instance.create();
 // then
 assertEquals(ID, instance.getId());
 assertNotNull(instance.getCreateTime());
 assertNotNull(instance.getUpdateTime());
 verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testBatchCreate() {
 // given
 List<FlowTaskInstance> instances = Arrays.asList(new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, START_ENDPOINT, END_ENDPOINT,
 flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository),
 new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, START_ENDPOINT, END_ENDPOINT,
 flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository));
 // when
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
 // then
 assertEquals(2, result.size());
 for (FlowTaskInstance instance : result) {
 assertEquals(ID, instance.getId());
 }
 verify(serviceTaskRepository, times(1)).batchCreate(anyList());
 }

 @Test
 public void testUpdate() {
 // given
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, START_ENDPOINT, END_ENDPOINT,
 flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 instance.setId(ID);
 instance.setStatus(FlowNodeStatus.EXECUTING);
 // when
 instance.update();
 // then
 assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
 verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testDelete() {
 // given
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG, START_ENDPOINT, END_ENDPOINT,
 flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 instance.setId(ID);
 // when
 flowTaskInstance.doDelete(ID);
 // then
 verify(serviceTaskRepository, times(1)).deleteById(ID);
 }

 @Test
 public void testBindServiceTask() {
 // given
 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 // when
 flowTaskInstance.bindServiceTask(target);
 // then
 assertEquals(flowTaskInstance, target.getTargetTaskInstance());
 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 }

 @Test
 public void testDealloc() {
 // given
 ServiceTaskStartedListener listener = Mockito.spy(new ServiceTaskStartedListener(flowTaskInstance));
 // when
 flowTaskInstance.alloc();
 flowTaskInstance.dealloc();
 // then
 verify(eventPublisher, times(1)).removeEventListener(listener);
 }

 @Test
 public void testConfirmExecute() throws InterruptedException, ExecutionException {
 // given
 List<FlowableElement> elements = Arrays.asList(new FlowableElement());
 when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(NODE_TYPE), any(FlowableElementType.class)))
 .thenReturn(elements);
 // when
 flowTaskInstance.confirmExecute();
 // then
 verify(taskService, times(elements.size())).complete(anyLong());
 }

 @Test
 public void testAbort() throws InterruptedException, ExecutionException {
 // given
 List<FlowableElement> elements = Arrays.asList(new FlowableElement());
 when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(NODE_TYPE), any(FlowableElementType.class)))
 .thenReturn(elements);
 // when
 flowTaskInstance.abort();
 // then
 verify(taskService, times(elements.size())).complete(anyLong());
 verify(taskService, times(elements.size())).complete(anyLong());
 // and verify that aborted flag has been set
 Map<String, Object> variables = new HashMap<>();
 variables.put(FlowTaskInstance.ABORT_VARIABLE_NAME, true);
 for (Task task : elements) {
 TaskInfo taskInfo = taskService.createTaskQuery().taskId(task.getId()).singleResult();
 assertEquals(Variables.variableLocal("aborted"), taskInfo.getVariableValues().get(FlowTaskInstance.ABORT_VARIABLE_NAME));
 taskService.complete(task.getId(), variables);
 }
 }

 @Test
 public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 // when
 flowTaskInstance.cancel(true);
 // then
 verify(target, times(1)).cancel(true);
 }

 @Test
 public void testIsDone() {
 // given
 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 // when
 baseRuntimeFlowableDelegateMockito(target).isDone();
 // then
 assertTrue(flowTaskInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // given
 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 // when
 when(target.get()).thenReturn("result");
 String result = flowTaskInstance.getResult();
 // then
 assertEquals("result", result);
 }

 @Test
 public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 // when
 when(target.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());
 assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1L, TimeUnit.SECONDS));
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // given
 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 // when
 assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testGetTargetDelegateClass() {
 // given
 assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
 }
}