import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.task.Task;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@Slf4j
public class FlowTaskInstanceTest {

 private static final String FLOW_FILE = "FlowTaskInstanceTest.bpmn20.xml";
 private static final String PROCESS_KEY = "FlowTaskInstanceTest";
 private static final String USER_TASK_NAME = "UserTask";
 private static final String SERVICE_TASK_NAME = "ServiceTask";
 private static final int WAIT_SEC = 5;

 @Rule
 public FlowableTestRule flowableTestRule = new FlowableTestRule();
 private RuntimeService runtimeService;
 private Map<String, Object> variables;
 private FlowTaskInstance flowTaskInstance;
 private List<Task> tasks;

 @Before
 public void setUp() {
 runtimeService = flowableTestRule.getRuntimeService();
 variables = new HashMap<>();
 flowableTestRule.deployProcessModel(FLOW_FILE);
 }

 @After
 public void tearDown() {
 flowableTestRule.cleanUp();
 }

 @Test
 @Deployment(resources = FLOW_FILE)
 public void testCreateFlowTaskInstance() {
 // given
 TaskService taskService = Mockito.mock(TaskService.class);
 EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
 RuntimeDelegateMapper delegateConvertor = new RuntimeDelegateMapper();
 FlowableAdaptor flowableAdaptor = new FlowableAdaptor(runtimeService, taskService, eventPublisher);
 ServiceTaskInstanceRepository serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 SequenceInstanceRepository sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
 NodeInstanceEntityRepository nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("TaskType1"), 1L, 1L,
 new ExecutionStrategyConfig(ExecutionStrategy.MANUAL), false, false, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 // when
 flowTaskInstance.create();

 // then
 assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
 assertNotNull(flowTaskInstance.getId());
 assertNotNull(flowTaskInstance.getCreateTime());
 assertNotNull(flowTaskInstance.getUpdateTime());
 assertTrue(flowTaskInstance.isStartEndpoint());
 assertFalse(flowTaskInstance.isEndEndpoint());
 assertNull(flowTaskInstance.getTargetTaskId());
 assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 assertEquals("TaskType1", flowTaskInstance.getTaskType().getName());
 assertEquals(ExecutionStrategyConfig.ExecutionStrategy.MANUAL, flowTaskInstance.getStrategyConfig().getStrategy());
 assertEquals(1L, flowTaskInstance.getFlowInstanceId());
 assertEquals(1L, flowTaskInstance.getOrganizationId());
 // cleanup
 flowableTestRule.deleteProcessInstance(flowTaskInstance.getFlowInstanceId(), "ALL");
 }

 @Test
 @Deployment(resources = FLOW_FILE)
 public void testCreateFlowTaskInstanceWithTargetTaskId() {
 // given
 String targetTaskId = Long.toString(RandomStringUtils.randomLong(10));
 TaskService taskService = Mockito.mock(TaskService.class);
 EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
 RuntimeDelegateMapper delegateConvertor = new RuntimeDelegateMapper();
 FlowableAdaptor flowableAdaptor = new FlowableAdaptor(runtimeService, taskService, eventPublisher);
 ServiceTaskInstanceRepository serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 SequenceInstanceRepository sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
 NodeInstanceEntityRepository nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("TaskType1"), 1L, 1L,
 new ExecutionStrategyConfig(ExecutionStrategy.MANUAL), false, false, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 flowTaskInstance.setTargetTaskId(Long.parseLong(targetTaskId));

 // when
 flowTaskInstance.create();

 // then
 assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
 assertNotNull(flowTaskInstance.getId());
 assertNotNull(flowTaskInstance.getCreateTime());
 assertNotNull(flowTaskInstance.getUpdateTime());
 assertTrue(flowTaskInstance.isStartEndpoint());
 assertFalse(flowTaskInstance.isEndEndpoint());
 assertEquals(Long.parseLong(targetTaskId), flowTaskInstance.getTargetTaskId());
 assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 assertEquals("TaskType1", flowTaskInstance.getTaskType().getName());
 assertEquals(ExecutionStrategyConfig.ExecutionStrategy.MANUAL, flowTaskInstance.getStrategyConfig().getStrategy());
 assertEquals(1L, flowTaskInstance.getFlowInstanceId());
 assertEquals(1L, flowTaskInstance.getOrganizationId());
 // cleanup
 flowableTestRule.deleteProcessInstance(flowTaskInstance.getFlowInstanceId(), "ALL");
 }

 @Test
 @Deployment(resources = FLOW_FILE)
 public void testBindServiceTask() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 String targetTaskId = Long.toString(RandomStringUtils.randomLong(10));
 TaskService taskService = Mockito.mock(TaskService.class);
 EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
 RuntimeDelegateMapper delegateConvertor = new RuntimeDelegateMapper();
 FlowableAdaptor flowableAdaptor = new FlowableAdaptor(runtimeService, taskService, eventPublisher);
 ServiceTaskInstanceRepository serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 SequenceInstanceRepository sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
 NodeInstanceEntityRepository nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);

 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setId(1L);
 serviceTaskInstanceEntity.setTargetTaskInstanceId(null);
 ServiceTaskInstance serviceTaskInstance = new ServiceTaskInstance(serviceTaskInstanceEntity, delegateConvertor,
 flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 ServiceTaskInstanceRepository serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 FlowableAdaptor flowableAdaptorMock = Mockito.mock(FlowableAdaptor.class);
 EventPublisher eventPublisherMock = Mockito.mock(EventPublisher.class);
 TaskService taskServiceMock = Mockito.mock(TaskService.class);
 NodeInstanceEntityRepository nodeRepositoryMock = Mockito.mock(NodeInstanceEntityRepository.class);
 SequenceInstanceRepository sequenceRepositoryMock = Mockito.mock(SequenceInstanceRepository.class);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("TaskType1"), 1L, 1L,
 new ExecutionStrategyConfig(ExecutionStrategy.MANUAL), false, false, delegateConvertor, flowableAdaptorMock,
 eventPublisherMock, taskServiceMock, nodeRepositoryMock, sequenceRepositoryMock, serviceTaskInstanceRepository);

 // when
 flowTaskInstance.bindServiceTask(target);

 // then
 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 assertEquals(target, flowTaskInstance.getTargetTaskHandle());

 // cleanup
 flowableTestRule.deleteProcessInstance(flowTaskInstance.getFlowInstanceId(), "ALL");
 }

 @Test
 @Deployment(resources = FLOW_FILE)
 public void testCompleteFlowTaskInstance() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 String targetTaskId = Long.toString(RandomStringUtils.randomLong(10));
 TaskService taskService = Mockito.mock(TaskService.class);
 EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
 RuntimeDelegateMapper delegateConvertor = new RuntimeDelegateMapper();
 FlowableAdaptor flowableAdaptor = new FlowableAdaptor(runtimeService, taskService, eventPublisher);
 ServiceTaskInstanceRepository serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 SequenceInstanceRepository sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
 NodeInstanceEntityRepository nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);

 FlowableElement userTask = new FlowableElement(FlowNodeType.USER_TASK, USER_TASK_NAME, null, null);
 FlowableElement serviceTask = new FlowableElement(FlowNodeType.SERVICE_TASK, SERVICE_TASK_NAME, null, null);
 List<FlowableElement> elements = Collections.singletonList(userTask);
 List<Task> tasksBefore = flowableTestRule.getTaskQuery().list();

 // when
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("TaskType1"), 1L, 1L,
 new ExecutionStrategyConfig(ExecutionStrategy.MANUAL), false, false, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 flowTaskInstance.bindServiceTask(taskService);
 flowTaskInstance.complete();

 // then
 List<Task> tasksAfter = flowableTestRule.getTaskQuery().list();
 assertEquals(tasksBefore.size() - 1, tasksAfter.size());
 assertEquals(tasksAfter.get(0).getId(), tasksBefore.get(0).getId());
 assertEquals(FlowNodeStatus.EXECUTED, tasksAfter.get(0).getStatus());

 // cleanup
 flowableTestRule.deleteProcessInstance(flowTaskInstance.getFlowInstanceId(), "ALL");
 }

 @Test(expected = UnsupportedOperationException.class)
 @Deployment(resources = FLOW_FILE)
 public void testCompleteFlowTaskInstanceAutomatic() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 String targetTaskId = Long.toString(RandomStringUtils.randomLong(10));
 TaskService taskService = Mockito.mock(TaskService.class);
 EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
 RuntimeDelegateMapper delegateConvertor = new RuntimeDelegateMapper();
 FlowableAdaptor flowableAdaptor = new FlowableAdaptor(runtimeService, taskService, eventPublisher);
 ServiceTaskInstanceRepository serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 SequenceInstanceRepository sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
 NodeInstanceEntityRepository nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);

 FlowableElement userTask = new FlowableElement(FlowNodeType.USER_TASK, USER_TASK_NAME, null, null);
 FlowableElement serviceTask = new FlowableElement(FlowNodeType.SERVICE_TASK, SERVICE_TASK_NAME, null, null);
 List<FlowableElement> elements = Collections.singletonList(userTask);
 List<Task> tasksBefore = flowableTestRule.getTaskQuery().list();

 // when
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType("TaskType1"), 1L, 1L,
 new ExecutionStrategyConfig(ExecutionStrategy.AUTO), false, false, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 flowTaskInstance.bindServiceTask(taskService);
 flowTaskInstance.complete();

 // then
 List<Task> tasksAfter = flowableTestRule.getTaskQuery().list();
 assertEquals(tasksBefore.size(), tasksAfter.size());
 assertSame(tasksBefore.get(0), tasksAfter.get(0));

 // cleanup
 flowableTestRule.deleteProcessInstance(flowTaskInstance.getFlowInstanceId(), "ALL");
 }

}