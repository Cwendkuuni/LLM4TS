import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.common.impl.id.DefaultIdentifierProvider;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.test.AbstractTestCase;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.TaskInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class FlowTaskInstanceTest extends PluggableFlowableTestCase {

 private FlowableAdaptor adaptor;
 private FlowTaskInstanceRepository repository;
 private FlowTaskInstance instance;
 private String processDefinitionKey;
 private String userId;
 private String taskDefinitionKey;
 private ProcessInstance processInstance;
 private TaskEntity task;

 @Before
 public void setUp() {
 super.initTest();
 processDefinitionKey = "processDefKey";
 userId = "user";
 taskDefinitionKey = "taskDefKey";
 adaptor = new FlowableAdaptorImpl();
 repository = (FlowTaskInstanceRepository) processEngineConfiguration.getRepositoryService().getImplementation(
 FlowTaskInstanceRepository.class);
 }

 @After
 public void tearDown() {
 if (processInstance != null) {
 processEngineConfiguration.getCommandExecutor().executeCommand("DetachProcessInstance", processInstance.getId());
 processEngineConfiguration.getCommandExecutor().executeCommand("DeleteAllTaskInstances", processInstance.getId());
 processEngineConfiguration.getCommandExecutor().executeCommand("DeleteProcessInstance", processInstance.getId());
 processInstance = null;
 }
 if (instance != null) {
 instance.dealloc();
 repository.deleteById(instance.getId());
 instance = null;
 }
 }

 @Test
 public void testCreateFlowTaskInstanceWithValidData() {
 // given - create a new FlowTaskInstance with valid data
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());

 // then - the FlowTaskInstance has been created
 assertEquals(FlowNodeStatus.PENDING, instance.getStatus());
 assertEquals(FlowNodeType.SERVICE_TASK, instance.getNodeType());
 assertSame(FlowNodeType.SERVICE_TASK, instance.getType());
 assertNull(instance.getTargetTaskInstanceId());
 assertNull(instance.getTargetTaskHandle());

 // when - save the FlowTaskInstance in the database
 instance.create();

 // then - the FlowTaskInstance is saved in the database
 FlowTaskInstance dbInstance = repository.findById(instance.getId());
 assertEquals(instance.getId(), dbInstance.getId());
 assertEquals(instance.getOrganizationId(), dbInstance.getOrganizationId());
 assertEquals(instance.getFlowInstanceId(), dbInstance.getFlowInstanceId());
 assertEquals(instance.getStatus(), dbInstance.getStatus());
 assertEquals(instance.getNodeType(), dbInstance.getNodeType());
 assertEquals(instance.getType(), dbInstance.getType());
 assertEquals(instance.getTargetTaskId(), dbInstance.getTargetTaskId());
 assertEquals(instance.getTaskType(), dbInstance.getTaskType());
 assertEquals(instance.getStrategyConfig().getStrategy(), dbInstance.getStrategyConfig().getStrategy());
 assertEquals(instance.getStrategyConfig().getPendingExpireIntervalSeconds(),
 dbInstance.getStrategyConfig().getPendingExpireIntervalSeconds());
 assertEquals(instance.getStrategyConfig().getExecutionTime(), dbInstance.getStrategyConfig().getExecutionTime());

 // when - retrieve the FlowTaskInstance from the database
 instance = repository.findById(instance.getId());

 // then - the retrieved FlowTaskInstance is identical to the original one
 assertEquals(instance.getId(), dbInstance.getId());
 assertEquals(instance.getOrganizationId(), dbInstance.getOrganizationId());
 assertEquals(instance.getFlowInstanceId(), dbInstance.getFlowInstanceId());
 assertEquals(instance.getStatus(), dbInstance.getStatus());
 assertEquals(instance.getNodeType(), dbInstance.getNodeType());
 assertEquals(instance.getType(), dbInstance.getType());
 assertEquals(instance.getTargetTaskId(), dbInstance.getTargetTaskId());
 assertEquals(instance.getTaskType(), dbInstance.getTaskType());
 assertEquals(instance.getStrategyConfig().getStrategy(), dbInstance.getStrategyConfig().getStrategy());
 assertEquals(instance.getStrategyConfig().getPendingExpireIntervalSeconds(),
 dbInstance.getStrategyConfig().getPendingExpireIntervalSeconds());
 assertEquals(instance.getStrategyConfig().getExecutionTime(), dbInstance.getStrategyConfig().getExecutionTime());
 }

 @Test
 public void testCreateFlowTaskInstanceWithExistingId() {
 // given - create a new FlowTaskInstance with an existing id
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());
 instance.setId(1L);

 // then - an exception is thrown
 assertFalse(instance::create);
 }

 @Test
 public void testCreateFlowTaskInstanceWithNullStrategyConfig() {
 // given - create a new FlowTaskInstance with a null strategy config
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(), null, true,
 true, adaptor, adaptor, getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());

 // then - an exception is thrown
 assertFalse(instance::create);
 }

 @Test
 public void testCreateFlowTaskInstanceWithNullTaskService() {
 // given - create a new FlowTaskInstance with a null task service
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, null,
 getTestEventPublisher(), null, getTestNodeInstanceEntityRepository(), getTestSequenceInstanceRepository(),
 getTestServiceTaskInstanceRepository());

 // then - an exception is thrown
 assertFalse(instance::create);
 }

 @Test
 public void testCreateFlowTaskInstanceWithNullEventPublisher() {
 // given - create a new FlowTaskInstance with a null event publisher
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor, null,
 getTestTaskService(), getTestNodeInstanceEntityRepository(), getTestSequenceInstanceRepository(),
 getTestServiceTaskInstanceRepository());

 // then - an exception is thrown
 assertFalse(instance::create);
 }

 @Test
 public void testCreateFlowTaskInstanceWithNullTaskType() {
 // given - create a new FlowTaskInstance with a null task type
 instance = new FlowTaskInstance(null, getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());

 // then - an exception is thrown
 assertFalse(instance::create);
 }

 @Test
 public void testCreateFlowTaskInstanceWithNullOrganizationId() {
 // given - create a new FlowTaskInstance with a null organization id
 instance = new FlowTaskInstance(new TaskType("someTaskType"), null, getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());

 // then - an exception is thrown
 assertFalse(instance::create);
 }

 @Test
 public void testCreateFlowTaskInstanceWithNullFlowInstanceId() {
 // given - create a new FlowTaskInstance with a null flow instance id
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), null,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());

 // then - an exception is thrown
 assertFalse(instance::create);
 }

 @Test
 public void testCreateFlowTaskInstanceWithEmptyName() {
 // given - create a new FlowTaskInstance with an empty name
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository(), RandomStringUtils.randomAlphanumeric(0));

 // then - an exception is thrown
 assertFalse(instance::create);
 }

 @Test
 public void testCreateAndBindFlowTaskInstanceToTargetTask() throws Exception {
 // given - create a new FlowTaskInstance
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());
 instance.create();

 // given - create a new TaskInstance for the user task
 processInstance = processEngine.getProcessEngine().getRuntimeService().startProcessInstanceByKey(processDefinitionKey);
 task = processInstance.getTaskList().get(0);
 assertEquals(1, processInstance.getTaskList().size());

 // when - bind the FlowTaskInstance to the target task
 instance.bindServiceTask(taskService.createTaskQuery().taskDefinitionKey(taskDefinitionKey).singleResult().getDelegate());

 // then - the FlowTaskInstance status is set to EXECUTING
 assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
 assertSame(instance.getTargetTaskInstanceId(), task.getId());
 assertSame(instance.getTargetTaskHandle(), task.getExecution());
 }

 @Test
 public void testCompleteFlowTaskInstance() throws Exception {
 // given - create a new FlowTaskInstance
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());
 instance.create();

 // given - complete the user task
 processInstance = processEngine.getProcessEngine().getRuntimeService()
 .startProcessInstanceByKey(processDefinitionKey);
 task = processInstance.getTaskList().get(0);
 processEngine.getRuntimeService().setVariable(task.getId(), "aborted", true);
 processEngine.getRuntimeService().complete(task.getId());

 // when - complete the FlowTaskInstance
 instance.complete(true);

 // then - the FlowTaskInstance status is set to COMPLETED
 assertEquals(FlowNodeStatus.COMPLETED, instance.getStatus());
 }

 @Test
 public void testCancelFlowTaskInstance() throws Exception {
 // given - create a new FlowTaskInstance
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());
 instance.create();

 // given - cancel the user task
 processInstance = processEngine.getProcessEngine().getRuntimeService()
 .startProcessInstanceByKey(processDefinitionKey);
 task = processInstance.getTaskList().get(0);
 processEngine.getRuntimeService().cancelTask(task.getId());

 // when - cancel the FlowTaskInstance
 instance.cancel(true);

 // then - the FlowTaskInstance status is set to CANCELED
 assertEquals(FlowNodeStatus.CANCELED, instance.getStatus());
 }

 @Test
 public void testGetResultFromFlowTaskInstance() throws Exception {
 // given - create a new FlowTaskInstance
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());
 instance.create();

 // given - complete the user task with a result
 processInstance = processEngine.getProcessEngine().getRuntimeService()
 .startProcessInstanceByKey(processDefinitionKey);
 task = processInstance.getTaskList().get(0);
 processEngine.getRuntimeService().setVariable(task.getId(), "result", "resultValue");
 processEngine.getRuntimeService().complete(task.getId());

 // when - get the result from the FlowTaskInstance
 Object result = instance.getResult();

 // then - the result is returned
 assertEquals("resultValue", result);
 }

 @Test
 public void testGetResultFromFlowTaskInstanceWithTimeout() throws Exception {
 // given - create a new FlowTaskInstance
 instance = new FlowTaskInstance(new TaskType("someTaskType"), getOrganizationId(), getTestFlowInstanceId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 0L), true, true, adaptor, adaptor,
 getTestEventPublisher(), getTestTaskService(), getTestNodeInstanceEntityRepository(),
 getTestSequenceInstanceRepository(), getTestServiceTaskInstanceRepository());
 instance.create();

 // given - complete the user task with a result after a timeout
 processInstance = processEngine.getProcessEngine().getRuntimeService()
 .startProcessInstanceByKey(processDefinitionKey);
 task = processInstance.getTaskList().get(0);
 processEngine.getRuntimeService().setVariable(task.getId(), "result", "resultValue");
 processEngine.getRuntimeService().signalEventReceived("flowable:signal:task:completed", task.getId());
 Thread.sleep(1000); // wait for the signal to be processed

 // when - get the result from the FlowTaskInstance with a timeout
 Object result = instance.getResult(500, TimeUnit.MILLISECONDS);

 // then - the result is not returned and a TimeoutException is thrown
 assertNull(result);
 assertSame(instance.getStatus(), FlowNodeStatus.PENDING);
 }

}