import org.apache.commons.lang3.RandomUtils;import org.flowable.engine.common.impl.interceptor.CommandContext;import org.flowable.engine.impl.context.Context;import org.flowable.engine.impl.persistence.entity.ExecutionEntity;import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;import org.flowable.engine.impl.persistence.entity.TaskEntity;import org.flowable.engine.impl.test.PluggableProcessEngineTestCase;import org.flowable.engine.runtime.ProcessInstance;import org.flowable.engine.task.Task;import org.flowable.task.api.TaskQuery;import org.junit.Before;import org.junit.Test;import java.util.Collections;import java.util.HashMap;import java.util.List;import java.util.Map;import java.util.Random;import java.util.UUID;

public class FlowTaskInstanceTest extends PluggableProcessEngineTestCase {

 private static final String PROCESS_DEFINITION_KEY = "testProcessDefinition";
 private static final String SERVICE_TASK_NAME = "testServiceTask";
 private static final String USER_TASK_NAME = "testUserTask";
 private static final String TASK_TYPE = "testTaskType";
 private static final int EXECUTION_TIME_MILLIS = 1000;
 private static final int PENDING_EXPIRE_INTERVAL_SECONDS = 5;
 private static final int EXECUTION_STRATEGY_AUTO = 0;
 private static final int EXECUTION_STRATEGY_MANUAL = 1;
 private static final int EXECUTION_STRATEGY_EAGER = 2;
 private static final int EXECUTION_STRATEGY_LAZY = 3;
 private static final String ORGANIZATION_ID = "testOrganizationId";
 private static final String FLOW_INSTANCE_ID = UUID.randomUUID().toString();
 private static final String TARGET_TASK_ID = "testTargetTaskId";
 private static final int RETRY_COUNT = 5;
 private static final long TIMEOUT = 5000;
 private static final long RANDOM_DELAY = 100;
 private static final long RANDOM_SLEEP = 500;
 private static final boolean ABORT = false;
 private ProcessInstance processInstance;
 private FlowTaskInstance flowTaskInstance;
 private ExecutionEntity executionEntity;
 private TaskEntity userTaskEntity;
 private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
 private CommandContext commandContext;
 private ExecutionEntityManager executionEntityManager;
 private Random random = new Random();

 @Before
 public void setUp() {
 super.initTestProcessEngine(false);
 Context.setProcessEngineConfiguration(processEngineConfiguration);
 processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
 executeTask(USER_TASK_NAME);
 serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setOrganizationId(ORGANIZATION_ID);
 serviceTaskInstanceEntity.setStrategy(EXECUTION_STRATEGY_AUTO);
 serviceTaskInstanceEntity.setTaskType(TASK_TYPE);
 serviceTaskInstanceEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
 serviceTaskInstanceEntity.setTargetTaskId(TARGET_TASK_ID);
 serviceTaskInstanceEntity.setWaitExecExpireIntervalSeconds(PENDING_EXPIRE_INTERVAL_SECONDS);
 serviceTaskInstanceEntity.setExecutionTime(EXECUTION_TIME_MILLIS);
 serviceTaskInstanceEntity = processEngine.getServiceTaskInstanceRepository().save(serviceTaskInstanceEntity);
 Verify.notNull(serviceTaskInstanceEntity.getId(), "serviceTaskInstanceEntity id");
 flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, getRuntimeDelegateMapper(),
 getFlowableAdaptor(), getEventPublisher(), getTaskService(), getNodeInstanceEntityRepository(),
 getSequenceInstanceRepository(), getServiceTaskInstanceRepository());
 executionEntity = new ExecutionEntity();
 executionEntityManager = getExecutionEntityManager();
 commandContext = executionEntityManager.getContext();
 commandContext.setProcessInstance(processInstance);
 }

 @Test
 public void testCreateFlowTaskInstance() {
 // Test creating a new FlowTaskInstance
 FlowTaskInstance newFlowTaskInstance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
 new ExecutionStrategyConfig(EXECUTION_STRATEGY_AUTO), false, false, getRuntimeDelegateMapper(),
 getFlowableAdaptor(), getEventPublisher(), getTaskService(), getNodeInstanceEntityRepository(),
 getSequenceInstanceRepository(), getServiceTaskInstanceRepository());
 newFlowTaskInstance.create();
 Verify.notNull(newFlowTaskInstance.getId(), "id");
 Verify.notNull(newFlowTaskInstance.getCreateTime(), "CreateTime");
 Verify.notNull(newFlowTaskInstance.getUpdateTime(), "UpdateTime");
 }

 @Test
 public void testBindServiceTask() {
 // Test binding a service task to FlowTaskInstance
 BaseRuntimeFlowableDelegate<?> target = getMockRuntimeDelegate();
 flowTaskInstance.bindServiceTask(target);
 Verify.notNull(target.getTargetTaskInstanceId(), "target.getTargetTaskInstanceId()");
 Verify.assertEquals(flowTaskInstance.getId(), target.getTargetTaskInstanceId(), "flowTaskInstance.getId() != target.getTargetTaskInstanceId()");
 }

 @Test
 public void testDeallocate() {
 // Test deallocating FlowTaskInstance
 flowTaskInstance.dealloc();
 Verify.assertNull(flowTaskInstance.getListener(), "flowTaskInstance.getListener() != null");
 }

 @Test
 public void testAllocate() {
 // Test allocating FlowTaskInstance
 flowTaskInstance.alloc();
 Verify.assertNotNull(flowTaskInstance.getListener(), "flowTaskInstance.getListener() == null");
 }

 @Test
 public void testConfirmExecute() {
 // Test confirming execute
 flowTaskInstance.confirmExecute();
 TaskEntity userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompleted(userTaskEntity);
 }

 @Test
 public void testAbort() {
 // Test aborting
 flowTaskInstance.abort();
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompletedWithVariable(userTaskEntity, ABORT_VARIABLE_NAME, ABORT);
 }

 @Test
 public void testCancelWithRunning() {
 // Test cancelling a running task
 flowTaskInstance.cancel(true);
 try {
 flowTaskInstance.getResult();
 fail("Should fail with InterruptedException");
 } catch (InterruptedException e) {
 // Expected
 }
 }

 @Test
 public void testCancelWithNotRunning() {
 // Test cancelling a not running task
 flowTaskInstance.cancel(false);
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompleted(userTaskEntity);
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // Test checking if a task is present on this machine
 Verify.assertTrue(flowTaskInstance.isPresentOnThisMachine(), "flowTaskInstance.isPresentOnThisMachine() == false");
 }

 @Test
 public void testIsDone() {
 // Test checking if a task is done
 Verify.assertTrue(flowTaskInstance.isDone(), "flowTaskInstance.isDone() == false");
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException, TimeoutException {
 // Test getting result
 Object result = flowTaskInstance.getResult();
 Verify.assertNotNull(result, "result == null");
 }

 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // Test getting result with timeout
 Object result = flowTaskInstance.getResult(TIMEOUT, TimeUnit.MILLISECONDS);
 Verify.assertNotNull(result, "result == null");
 }

 @Test
 public void testResourceType() {
 // Test getting resource type
 Verify.assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, flowTaskInstance.resourceType(),
 "flowTaskInstance.resourceType() != ResourceType.ODC_FLOW_TASK_INSTANCE");
 }

 @Test
 public void testGetTargetDelegateClass() {
 // Test getting target delegate class
 Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();
 Verify.assertEquals(getMockRuntimeDelegate().getClass(), targetDelegateClass,
 "flowTaskInstance.getTargetDelegateClass() != getMockRuntimeDelegate().getClass()");
 }

 @Test
 public void testBatchCreate() {
 // Test batch creating FlowTaskInstances
 List<FlowTaskInstance> newFlowTaskInstances = Collections.nCopies(5, new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
 new ExecutionStrategyConfig(EXECUTION_STRATEGY_AUTO), false, false, getRuntimeDelegateMapper(),
 getFlowableAdaptor(), getEventPublisher(), getTaskService(), getNodeInstanceEntityRepository(),
 getSequenceInstanceRepository(), getServiceTaskInstanceRepository()));
 List<FlowTaskInstance> createdFlowTaskInstances = FlowTaskInstance.batchCreate(newFlowTaskInstances,
 getServiceTaskInstanceRepository());
 Verify.assertEquals(newFlowTaskInstances.size(), createdFlowTaskInstances.size(),
 "newFlowTaskInstances.size() != createdFlowTaskInstances.size()");
 for (FlowTaskInstance instance : createdFlowTaskInstances) {
 Verify.notNull(instance.getId(), "id");
 Verify.notNull(instance.getCreateTime(), "CreateTime");
 Verify.notNull(instance.getUpdateTime(), "UpdateTime");
 }
 }

 @Test
 public void testDelete() {
 // Test deleting a FlowTaskInstance
 long instanceId = flowTaskInstance.getId();
 flowTaskInstance.doDelete(instanceId);
 FlowTaskInstance deletedFlowTaskInstance = getServiceTaskInstanceRepository().findById(instanceId);
 Verify.assertNull(deletedFlowTaskInstance, "deletedFlowTaskInstance != null");
 }

 @Test
 public void testIsPresentOnThisMachineWithDeletedInstance() {
 // Test checking if a deleted task is present on this machine
 Verify.assertFalse(flowTaskInstance.isPresentOnThisMachine(), "flowTaskInstance.isPresentOnThisMachine() == true");
 }

 @Test
 public void testCompleteWithAbortedVariable() {
 // Test completing a task with aborted variable
 processEngine.getRuntimeService().setVariable(processInstance.getId(), ABORT_VARIABLE_NAME, ABORT);
 flowTaskInstance.complete(ABORT);
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompletedWithVariable(userTaskEntity, ABORT_VARIABLE_NAME, ABORT);
 }

 @Test
 public void testCompleteWithNoVariable() {
 // Test completing a task with no variable
 flowTaskInstance.complete(false);
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompleted(userTaskEntity);
 }

 @Test
 public void testCompleteWithException() {
 // Test completing a task with exception
 try {
 flowTaskInstance.complete(true);
 fail("Should fail with IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testCompleteWithTaskNotFound() {
 // Test completing a task not found
 try {
 FlowTaskInstance notFoundInstance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID + "_notFound",
 new ExecutionStrategyConfig(EXECUTION_STRATEGY_AUTO), false, false, getRuntimeDelegateMapper(),
 getFlowableAdaptor(), getEventPublisher(), getTaskService(), getNodeInstanceEntityRepository(),
 getSequenceInstanceRepository(), getServiceTaskInstanceRepository());
 notFoundInstance.complete(ABORT);
 fail("Should fail with IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testCompleteWithExecutionStrategyAuto() {
 // Test completing a task with execution strategy AUTO
 processEngine.getRuntimeService().setVariable(processInstance.getId(), "executionStrategy",
 EXECUTION_STRATEGY_AUTO);
 flowTaskInstance.complete(ABORT);
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompletedWithVariable(userTaskEntity, ABORT_VARIABLE_NAME, ABORT);
 }

 @Test
 public void testCompleteWithExecutionStrategyManual() {
 // Test completing a task with execution strategy MANUAL
 processEngine.getRuntimeService().setVariable(processInstance.getId(), "executionStrategy",
 EXECUTION_STRATEGY_MANUAL);
 flowTaskInstance.complete(ABORT);
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompletedWithVariable(userTaskEntity, ABORT_VARIABLE_NAME, ABORT);
 }

 @Test
 public void testCompleteWithExecutionStrategyEager() {
 // Test completing a task with execution strategy EAGER
 processEngine.getRuntimeService().setVariable(processInstance.getId(), "executionStrategy",
 EXECUTION_STRATEGY_EAGER);
 flowTaskInstance.complete(ABORT);
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompletedWithVariable(userTaskEntity, ABORT_VARIABLE_NAME, ABORT);
 }

 @Test
 public void testCompleteWithExecutionStrategyLazy() {
 // Test completing a task with execution strategy LAZY
 processEngine.getRuntimeService().setVariable(processInstance.getId(), "executionStrategy",
 EXECUTION_STRATEGY_LAZY);
 flowTaskInstance.complete(ABORT);
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompletedWithVariable(userTaskEntity, ABORT_VARIABLE_NAME, ABORT);
 }

 @Test
 public void testCompleteWithExecutionStrategyMixed() {
 // Test completing a task with mixed execution strategies
 processEngine.getRuntimeService().setVariable(processInstance.getId(), "executionStrategy",
 new ExecutionStrategyConfig(EXECUTION_STRATEGY_AUTO, EXECUTION_STRATEGY_LAZY));
 flowTaskInstance.complete(ABORT);
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompletedWithVariable(userTaskEntity, ABORT_VARIABLE_NAME, ABORT);
 }

 @Test
 public void testCompleteWithExecutionStrategyNull() {
 // Test completing a task with null execution strategy
 processEngine.getRuntimeService().setVariable(processInstance.getId(), "executionStrategy", null);
 flowTaskInstance.complete(ABORT);
 userTaskEntity = getTaskQuery().taskQueryByProcessInstanceId(processInstance.getId()).singleResult();
 verifyTaskCompleted(userTaskEntity);
 }

 private void executeTask(String taskName) {
 Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstance.getId())
 .taskName(taskName).singleResult();
 processEngine.getTaskService().complete(task.getId());
 }

 private void verifyTaskCompleted(TaskEntity taskEntity) {
 Verify.assertNotNull(taskEntity, "taskEntity == null");
 Verify.assertEquals(FlowNodeStatus.COMPLETED, taskEntity.getStatus(), "taskEntity.getStatus() != FlowNodeStatus.COMPLETED");
 }

 private void verifyTaskCompletedWithVariable(TaskEntity taskEntity, String variableName, boolean variableValue) {
 Verify.assertNotNull(taskEntity, "taskEntity == null");
 Verify.assertEquals(FlowNodeStatus.COMPLETED, taskEntity.getStatus(), "taskEntity.getStatus() != FlowNodeStatus.COMPLETED");
 Map<String, Object> variables = taskEntity.getVariables();
 Verify.assertNotNull(variables, "variables == null");
 Verify.assertEquals(variableValue, variables.get(variableName), "variables.get(\"" + variableName + "\") != " + variableValue);
 }

 private BaseRuntimeFlowableDelegate<?> getMockRuntimeDelegate() {
 return mock(BaseRuntimeFlowableDelegate.class);
 }

 private ExecutionEntityManager getExecutionEntityManager() {
 return (ExecutionEntityManager) processEngine.getManagementService().getExecutionEntityManager();
 }

}