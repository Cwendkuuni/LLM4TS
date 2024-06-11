import org.apache.commons.lang3.RandomUtils; import org.flowable.engine.common.impl.interceptor.CommandContext; import org.flowable.engine.common.impl.interceptor.ServiceTaskInterceptor; import org.flowable.engine.impl.context.Context; import org.flowable.engine.impl.persistence.entity.ExecutionEntity; import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl; import org.flowable.engine.impl.persistence.entity.TaskEntity; import org.flowable.engine.impl.persistence.entity.TaskInstanceEntity; import org.flowable.engine.impl.test.PlainTestBase; import org.flowable.engine.impl.test.annotation.FlowableTest; import org.flowable.engine.impl.test.util.TestUtil; import org.flowable.engine.repository.Deployment; import org.flowable.engine.repository.ProcessDefinition; import org.flowable.engine.runtime.Execution; import org.flowable.engine.runtime.ProcessInstance; import org.flowable.engine.test.DeploymentHelper; import org.flowable.task.api.ExecutionQuery; import org.flowable.task.api.ProcessComponentTaskDefinition; import org.junit.After; import org.junit.Assert; import org.junit.Before; import org.junit.Test;

import java.util.List; import java.util.Map; import java.util.concurrent.ExecutionException; import java.util.concurrent.TimeUnit;

@FlowableTest public class FlowTaskInstanceTest extends PlainTestBase {

 private static final String PROCESS_DEFINITION_KEY = "flow_task_instance_test";
 private static final String FLOW_NODE_TYPE = "ServiceTask";
 private static final String FLOW_NODE_NAME = "Test Service Task";
 private static final String TASK_TYPE = "testTaskType";
 private static final String USER_TASK_NAME = "User Task";
 private ProcessDefinition processDefinition;
 private Deployment deployment;
 private Long flowInstanceId;
 private Long flowNodeInstanceId;
 private Long targetTaskId;
 private FlowTaskInstance flowTaskInstance;

 @Before public void setUp() throws Exception {
 deployment = DeploymentHelper.deployTestClasspathResourceAsZip("FlowTaskInstanceTest.bpmn20.xml");
 processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
 flowInstanceId = processInstanceFactory.startProcessInstanceByKey(processDefinition.getId(), null);
 flowNodeInstanceId = createServiceTaskInstance(FLOW_NODE_TYPE, FLOW_NODE_NAME, TASK_TYPE, flowInstanceId);
 targetTaskId = createUserTaskInstance(USER_TASK_NAME, flowInstanceId);
 }

 @After public void tearDown() throws Exception {
 CommandContext context = executionContext.getCommandContext();
 context.getExecutionManager().clearInterceptorStack();
 executionContext = null;
 removalExecutorService.shutdownNow();
 repositoryService.deleteDeployment(deployment.getId(), true);
 repositoryService.deleteProcessDefinition(processDefinition.getId());
 }

 @Test public void testFlowTaskInstanceCreation() {
 FlowTaskInstance instance = new FlowTaskInstance(new TaskType(TASK_TYPE), getTestOrganizationId(), flowInstanceId,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), true, false, delegateConvertor,
 flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 Assert.assertEquals(flowNodeInstanceId, instance.getId());
 Assert.assertTrue(instance.isStartEndpoint());
 Assert.assertFalse(instance.isEndEndPoint());
 Assert.assertEquals(FlowNodeType.SERVICE_TASK, instance.getNodeType());
 Assert.assertEquals(flowInstanceId, instance.getFlowInstanceId());
 Assert.assertEquals(FlowNodeStatus.CREATED, instance.getStatus());
 }

 @Test public void testFlowTaskInstanceExecution() throws InterruptedException, ExecutionException, TimeoutException {
 CommandContext context = executionContext.getCommandContext();
 context.getExecutionManager().setInterceptor(new ServiceTaskInterceptor() {
 @Override public void beforeServiceTask(String executionId, String taskDefinitionKey, String name, @NonNull Map<String, ?> variables, @NonNull List<Object> executionVariables, @NonNull Execution execution) {
 super.beforeServiceTask(executionId, taskDefinitionKey, name, variables, executionVariables, execution);
 flowTaskInstance = (FlowTaskInstance) execution.getBusinessKey();
 }
 });

 ProcessComponentTaskDefinition taskDefinition =
 processDefinition.findModelElementInstanceByTypeAndName(Bpmn.TASK, FLOW_NODE_NAME, true);
 Assert.assertNotNull(taskDefinition);
 context.setVariableLocal("flowTaskInstance", flowTaskInstance);
 context.setVariableLocal("targetTaskId", targetTaskId);
 execution = runtimeService.executeTask(taskDefinition.getId(), context);
 Assert.assertEquals(FlowNodeStatus.EXECUTED, flowTaskInstance.getStatus());
 Assert.assertEquals(targetTaskId, flowTaskInstance.getTargetTaskId());
 }

 @Test public void testFlowTaskInstanceManualExecution() throws InterruptedException, ExecutionException, TimeoutException {
 CommandContext context = executionContext.getCommandContext();
 context.getExecutionManager().setInterceptor(new ServiceTaskInterceptor() {
 @Override public void beforeServiceTask(String executionId, String taskDefinitionKey, String name, @NonNull Map<String, ?> variables, @NonNull List<Object> executionVariables, @NonNull Execution execution) {
 super.beforeServiceTask(executionId, taskDefinitionKey, name, variables, executionVariables, execution);
 flowTaskInstance = (FlowTaskInstance) execution.getBusinessKey();
 }
 });

 execution = runtimeService.executeTaskAt(taskDefinition.getId(), flowInstanceId, context);
 Assert.assertEquals(FlowNodeStatus.EXECUTED, flowTaskInstance.getStatus());
 Assert.assertEquals(targetTaskId, flowTaskInstance.getTargetTaskId());
 }

 @Test public void testFlowTaskInstanceAbortExecution() throws InterruptedException, ExecutionException, TimeoutException {
 CommandContext context = executionContext.getCommandContext();
 context.getExecutionManager().setInterceptor(new ServiceTaskInterceptor() {
 @Override public void beforeServiceTask(String executionId, String taskDefinitionKey, String name, @NonNull Map<String, ?> variables, @NonNull List<Object> executionVariables, @NonNull Execution execution) {
 super.beforeServiceTask(executionId, taskDefinitionKey, name, variables, executionVariables, execution);
 flowTaskInstance = (FlowTaskInstance) execution.getBusinessKey();
 }
 });

 execution = runtimeService.executeTaskAt(taskDefinition.getId(), flowInstanceId, context);
 Assert.assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
 }

 @Test public void testFlowTaskInstanceCancelExecution() throws InterruptedException, ExecutionException, TimeoutException {
 CommandContext context = executionContext.getCommandContext();
 context.getExecutionManager().setInterceptor(new ServiceTaskInterceptor() {
 @Override public void beforeServiceTask(String executionId, String taskDefinitionKey, String name, @NonNull Map<String, ?> variables, @NonNull List<Object> executionVariables, @NonNull Execution execution) {
 super.beforeServiceTask(executionId, taskDefinitionKey, name, variables, executionVariables, execution);
 flowTaskInstance = (FlowTaskInstance) execution.getBusinessKey();
 }
 });

 execution = runtimeService.executeTaskAt(taskDefinition.getId(), flowInstanceId, context);
 Assert.assertEquals(FlowNodeStatus.CANCELED, flowTaskInstance.getStatus());
 }

 @Test public void testFlowTaskInstanceCompleteExecution() throws InterruptedException, ExecutionException, TimeoutException {
 CommandContext context = executionContext.getCommandContext();
 context.getExecutionManager().setInterceptor(new ServiceTaskInterceptor() {
 @Override public void beforeServiceTask(String executionId, String taskDefinitionKey, String name, @NonNull Map<String, ?> variables, @NonNull List<Object> executionVariables, @NonNull Execution execution) {
 super.beforeServiceTask(executionId, taskDefinitionKey, name, variables, executionVariables, execution);
 flowTaskInstance = (FlowTaskInstance) execution.getBusinessKey();
 }
 });

 execution = runtimeService.executeTaskAt(taskDefinition.getId(), flowInstanceId, context);
 Assert.assertEquals(FlowNodeStatus.COMPLETED, flowTaskInstance.getStatus());
 }

 @Test public void testFlowTaskInstancePendingExpire() throws InterruptedException, ExecutionException, TimeoutException {
 executionService.setActivitiEnginePlugin(new ActivitiEnginePlugin() {
 @Override public void onNewExecution(Execution execution) {
 super.onNewExecution(execution);
 if (execution.getBusinessKey() != null && execution.getBusinessKey().equals(flowTaskInstance)) {
 try {
 Thread.sleep(strategyConfig.getPendingExpireIntervalSeconds() * 1000 + RandomUtils.nextInt(100));
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }
 }
 });

 execution = runtimeService.executeTaskAt(taskDefinition.getId(), flowInstanceId, context);
 Assert.assertEquals(FlowNodeStatus.PENDING_EXPIRED, flowTaskInstance.getStatus());
 }

 private Long createServiceTaskInstance(String nodeType, String nodeName, String taskType, Long flowInstanceId) {
 FlowableAdaptor adaptor = new FlowableAdaptor();
 EventPublisher eventPublisher = new EventPublisher();
 TaskService taskService = context.getTaskService();
 NodeInstanceEntityRepository nodeRepository = new NodeInstanceEntityRepository();
 SequenceInstanceRepository sequenceRepository = new SequenceInstanceRepository();
 ServiceTaskInstanceRepository serviceTaskRepository = new ServiceTaskInstanceRepository();
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(taskType), getTestOrganizationId(), flowInstanceId,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), true, false, delegateConvertor,
 adaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 long instanceId = serviceTaskRepository.save(flowTaskInstance.toEntity()).getId();
 return instanceId;
 }

 private Long createUserTaskInstance(String nodeName, Long flowInstanceId) {
 ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) engineConfiguration;
 ProcessEngine processEngine = configuration.buildProcessEngine();
 ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY, null);
 TaskService taskService = processEngine.getTaskService();
 ProcessComponentTaskDefinition userTaskDefinition =
 processInstance.getProcessDefinition().findModelElementInstanceByTypeAndName(Bpmn.TASK, nodeName, true);
 TaskEntity userTask = taskService.newTask(userTaskDefinition.getId(), processInstance.getId());
 TaskInstanceEntity taskInstance = userTask.execute();
 Long taskInstanceId = taskInstance.getId();
 return taskInstanceId;
 }
}