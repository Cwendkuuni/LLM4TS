import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.flowable.engine.common.api.FlowableEngineConfiguration;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.persistence.entity.ExecutionStatus;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.test.AbstractFlowableTest;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.Job;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapperTest;

@Slf4j
public class FlowTaskInstanceTest extends AbstractFlowableTest {

 private static final String PROCESS_DEFINITION_KEY = "testProcess";
 private static final long FLOW_INSTANCE_ID = RandomUtils.nextLong();
 private static final long TASK_ID = RandomUtils.nextLong();
 private static final String TASK_NAME = RandomStringUtils.randomAlphanumeric(10);
 private static final String ORGANIZATION_ID = "testOrganization";

 private FlowableEngineConfiguration engineConfiguration;
 private NodeInstanceEntityRepository nodeRepository;
 private SequenceInstanceRepository sequenceRepository;
 private ServiceTaskInstanceRepository serviceTaskRepository;
 private EventPublisher eventPublisher;
 private FlowableAdaptor flowableAdaptor;
 private RuntimeDelegateMapper delegateConvertor;
 private FlowTaskInstance flowTaskInstance;
 private ServiceTaskStartedListener listener;
 private ExecutionEntityManager executionEntityManager;

 @Before
 public void setUp() {
 engineConfiguration = Mockito.mock(FlowableEngineConfiguration.class);
 nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);
 sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
 serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 flowableAdaptor = new FlowableAdaptor(engineConfiguration, nodeRepository, sequenceRepository,
 serviceTaskRepository, eventPublisher);
 delegateConvertor = new RuntimeDelegateMapperTest().getDelegateMapper();

 listener = new ServiceTaskStartedListener(null);

 engineConfiguration.setEventPublisher(eventPublisher);
 engineConfiguration.setTaskService(Mockito.mock(TaskService.class));
 engineConfiguration.setNodeEntityManager(Mockito.mock(NodeEntityManager.class));
 engineConfiguration.setSequenceEntityManager(Mockito.mock(SequenceEntityManager.class));
 engineConfiguration.setExecutionEntityManager(executionEntityManager = Mockito.mock(ExecutionEntityManager.class));

 ProcessInstance processInstance = deployment()
 .deployAndReturn(PROCESS_DEFINITION_KEY)
 .createProcessInstanceById(String.valueOf(FLOW_INSTANCE_ID)).get();
 TaskEntity task = processInstance.getTasks().get(0);

 FlowNodeInstance nodeInstance = new FlowNodeInstance(FlowNodeType.USER_TASK, ORGANIZATION_ID, FLOW_INSTANCE_ID,
 FlowNodeStatus.PENDING, null, null, flowableAdaptor, nodeRepository, sequenceRepository);
 nodeInstance.setId(task.getId());
 nodeInstance.setName(TASK_NAME);

 flowTaskInstance = new FlowTaskInstance(taskType(FlowNodeType.SERVICE_TASK), ORGANIZATION_ID, FLOW_INSTANCE_ID,
 ExecutionStrategyConfig.builder().strategy(FlowTaskExecutionStrategy.MANUAL).build(), false, false,
 delegateConvertor, flowableAdaptor, eventPublisher, Mockito.mock(TaskService.class), nodeRepository,
 sequenceRepository, serviceTaskRepository);
 flowTaskInstance.setId(task.getId());
 flowTaskInstance.setName(TASK_NAME);
 }

 @After
 public void tearDown() {
 engineConfiguration.closeEngine();
 }

 @Test
 public void testCreateFlowTaskInstance() {
 // Test creating a new FlowTaskInstance
 flowTaskInstance.create();

 assertNotNull(flowTaskInstance.getId());
 assertNotNull(flowTaskInstance.getCreateTime());
 assertNotNull(flowTaskInstance.getUpdateTime());
 assertNotNull(flowTaskInstance.getFlowInstanceId());
 assertNotNull(flowTaskInstance.isPresentOnThisMachine());
 assertFalse(flowTaskInstance.isDone());
 assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
 assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
 assertEquals(ORGANIZATION_ID, flowTaskInstance.getOrganizationId());
 assertEquals(FLOW_INSTANCE_ID, flowTaskInstance.getFlowInstanceId());
 assertEquals(ExecutionStatus.NEW, getExecutionStatus(flowTaskInstance.getId()));
 }

 @Test
 public void testBindServiceTask() {
 // Test binding a service task to an instance
 BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 assertTrue(flowTaskInstance.isPresentOnThisMachine());
 assertEquals(target, flowTaskInstance.getTargetTaskHandle());
 }

 @Test
 public void testIsDone() {
 // Test checking if a task is done
 Execution execution = Mockito.mock(Execution.class);
 Mockito.when(executionEntityManager.findExecutionById(flowTaskInstance.getId())).thenReturn(execution);
 Mockito.when(execution.getStatus()).thenReturn(ExecutionStatus.COMPLETED);

 flowTaskInstance.isDone();
 assertTrue(flowTaskInstance.isDone());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIsDoneWithoutExecution() {
 // Test checking if a task is done without an execution
 flowTaskInstance.isDone();
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // Test getting the result of a service task
 Object result = "Test result";
 Mockito.when(targetTaskHandle.get()).thenReturn(result);
 Object resultFromTask = flowTaskInstance.getResult();
 assertEquals(result, resultFromTask);
 }

 @Test(expected = InterruptedException.class)
 public void testGetResultInterrupted() throws InterruptedException {
 // Test getting the result of a service task that is still running
 Mockito.when(targetTaskHandle.get()).thenThrow(new InterruptedException());
 flowTaskInstance.getResult();
 }

 @Test(expected = ExecutionException.class)
 public void testGetResultExecutionException() throws InterruptedException, ExecutionException {
 // Test getting the result of a service task that has thrown an exception
 Mockito.when(targetTaskHandle.get()).thenThrow(new ExecutionException("Test exception"));
 flowTaskInstance.getResult();
 }

 @Test(expected = TimeoutException.class)
 public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // Test getting the result of a service task with a timeout
 Mockito.when(targetTaskHandle.get(5000, TimeUnit.MILLISECONDS)).thenThrow(new TimeoutException());
 flowTaskInstance.getResult(5000, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testCancel() {
 // Test cancelling a service task
 flowTaskInstance.cancel(true);
 assertFalse(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCancelWithoutTask() {
 // Test cancelling a service task that is not present on this machine
 flowTaskInstance.cancel(true);
 }

 @Test
 public void testComplete() {
 // Test completing a service task
 flowTaskInstance.complete(false);
 assertTrue(flowTaskInstance.isDone());
 }

 @Test
 public void testCompleteWithAborted() {
 // Test completing a service task with aborted status
 flowTaskInstance.complete(true);
 assertTrue(flowTaskInstance.isDone());
 }

 @Test
 public void testCompleteWithoutExecution() {
 // Test completing a service task without an execution
 flowTaskInstance.complete(false);
 assertFalse(flowTaskInstance.isDone());
 }

 private ExecutionStatus getExecutionStatus(long executionId) {
 return (ExecutionStatus) engineConfiguration.getExecutionEntityManager().findExecutionById(executionId);
 }

 @Deployment(resources = {"test.bpmn"})
 private static String deployment() {
 return deployment();
 }

}