import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.flowable.engine.common.api.FlowableEngineConstants;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.test.PluggableFlowableTestBase;
import org.flowable.engine.impl.test.PluggableFlowableTestConfiguration;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class FlowTaskInstanceTest extends PluggableFlowableTestBase {

 private static final String PROCESS_DEF_KEY = "ProcessWithServiceTask";
 private static final String SERVICE_TASK_ID = "serviceTask";
 private static final long TASK_WAIT_TIME = 1000;
 private static final long TASK_WAIT_TIME_LONG = 5000;
 private static final int EXECUTION_TIMEOUT = 1000;
 private static final int EXECUTION_TIMEOUT_LONG = 5000;
 private static final int EXECUTION_TIMEOUT_SHORT = 100;

 @Before
 public void setup() {
 Context.setProcessEngineConfiguration(new PluggableFlowableTestConfiguration(testClass));
 }

 @After
 public void teardown() {
 deleteAllFlowableEngineResources();
 }

 @Deployment
 public String processDefinitionFile() {
 return "classpath:test-processes/ProcessWithServiceTask.bpmn20.xml";
 }

 @Test
 public void testFlowTaskInstanceCreation() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 // When
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);

 // Then
 assertThat(flowTaskInstance).isInstanceOf(FlowTaskInstance.class);
 assertThat(flowTaskInstance.getId()).isNotNull();
 assertThat(flowTaskInstance.getFlowInstanceId()).isEqualTo(processInstance.getId());
 assertThat(flowTaskInstance.getStatus()).isEqualTo(FlowNodeStatus.PENDING);
 assertThat(flowTaskInstance.isStartEndpoint()).isTrue();
 assertThat(flowTaskInstance.isEndEndPoint()).isFalse();
 }

 @Test
 public void testFlowTaskInstanceIsPresentOnThisMachine() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);

 // When
 BaseRuntimeFlowableDelegate<?> target = createMockServiceTaskDelegate();
 flowTaskInstance.bindServiceTask(target);

 // Then
 assertThat(flowTaskInstance.isPresentOnThisMachine()).isTrue();
 }

 @Test
 public void testFlowTaskInstanceDealloc() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);

 // When
 flowTaskInstance.dealloc();

 // Then
 assertThat(flowTaskInstance.isPresentOnThisMachine()).isFalse();
 }

 @Test
 public void testFlowTaskInstanceBindServiceTask() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);
 BaseRuntimeFlowableDelegate<?> target = createMockServiceTaskDelegate();

 // When
 flowTaskInstance.bindServiceTask(target);

 // Then
 assertThat(flowTaskInstance.getTargetTaskHandle()).isNotNull();
 }

 @Test
 public void testFlowTaskInstanceCancel() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);
 BaseRuntimeFlowableDelegate<?> target = createMockServiceTaskDelegate();

 flowTaskInstance.bindServiceTask(target);

 // When
 flowTaskInstance.cancel(true);

 // Then
 assertThat(flowTaskInstance.isPresentOnThisMachine()).isFalse();
 }

 @Test
 public void testFlowTaskInstanceCancelWithRuntimeException() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);
 BaseRuntimeFlowableDelegate<?> target = createMockServiceTaskDelegateWithException();

 flowTaskInstance.bindServiceTask(target);

 // When
 // Then exception should be thrown
 assertThatThrownBy(() -> flowTaskInstance.cancel(true))
 .isInstanceOf(RuntimeException.class)
 .hasMessageContaining("Exception in service task");
 }

 @Test
 public void testFlowTaskInstanceConfirmExecute() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);
 BaseRuntimeFlowableDelegate<?> target = createMockServiceTaskDelegate();

 flowTaskInstance.bindServiceTask(target);

 // When
 flowTaskInstance.confirmExecute();

 // Then
 Execution execution = managementService.createExecutionQuery()
 .processInstanceId(processInstance.getId())
 .singleResult();

 List<Execution> executions = managementService.createExecutionQuery()
 .list();
 List<TaskEntity> tasks = taskService.createTaskQuery().list();

 // Then
 assertThat(executions).size().isGreaterThanOrEqualTo(1);
 assertThat(executions.get(0).getId()).isNotNull();
 assertThat(executions.get(0).getExecutionStatus()).isEqualTo(FlowableEngineConstants.EXECUTION_STATUS_COMPLETED);

 // Process instance should be ended
 ProcessInstance processInstance1 = runtimeService.createProcessInstanceQuery()
 .processDefinitionId(processDefinition.getId())
 .singleResult();

 // Then
 assertThat(processInstance1).isNull();

 // Tasks should be completed
 List<TaskEntity> tasksAfterCompletion = taskService.createTaskQuery().list();

 // Then
 assertThat(tasksAfterCompletion).size().isGreaterThanOrEqualTo(1);
 }

 @Test
 public void testFlowTaskInstanceAbort() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);
 BaseRuntimeFlowableDelegate<?> target = createMockServiceTaskDelegate();

 flowTaskInstance.bindServiceTask(target);

 // When
 flowTaskInstance.abort();

 // Then
 Execution execution = managementService.createExecutionQuery()
 .processInstanceId(processInstance.getId())
 .singleResult();

 List<Execution> executions = managementService.createExecutionQuery()
 .list();
 List<TaskEntity> tasks = taskService.createTaskQuery().list();

 // Then
 assertThat(executions).size().isGreaterThanOrEqualTo(1);
 assertThat(executions.get(0).getId()).isNotNull();
 assertThat(executions.get(0).getExecutionStatus()).isEqualTo(FlowableEngineConstants.EXECUTION_STATUS_COMPLETED);

 // Process instance should be ended
 ProcessInstance processInstance1 = runtimeService.createProcessInstanceQuery()
 .processDefinitionId(processDefinition.getId())
 .singleResult();

 // Then
 assertThat(processInstance1).isNull();

 // Tasks should be completed
 List<TaskEntity> tasksAfterCompletion = taskService.createTaskQuery().list();

 // Then
 assertThat(tasksAfterCompletion).size().isGreaterThanOrEqualTo(1);
 }

 @Test
 public void testFlowTaskInstanceGetResult() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);
 BaseRuntimeFlowableDelegate<?> target = createMockServiceTaskDelegate();

 flowTaskInstance.bindServiceTask(target);

 String result = "Test Result";
 target.setResult(result);

 // When
 String resultFromInstance = null;
 try {
 resultFromInstance = (String) flowTaskInstance.getResult();
 } catch (InterruptedException e) {
 } catch (ExecutionException e) {
 }

 // Then
 assertThat(resultFromInstance).isEqualTo(result);
 }

 @Test
 public void testFlowTaskInstanceGetResultTimeout() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);
 BaseRuntimeFlowableDelegate<?> target = createMockServiceTaskDelegate();

 flowTaskInstance.bindServiceTask(target);

 // When
 String resultFromInstance = null;
 try {
 resultFromInstance = (String) flowTaskInstance.getResult(EXECUTION_TIMEOUT_LONG, TimeUnit.MILLISECONDS);
 } catch (InterruptedException e) {
 } catch (ExecutionException e) {
 } catch (TimeoutException e) {
 }

 // Then
 assertThat(resultFromInstance).isNull();
 }

 @Test
 public void testFlowTaskInstanceGetResultTimeoutShort() {
 // Given
 ProcessDefinitionEntity processDefinition = repositoryService.createProcessDefinitionQuery()
 .key(PROCESS_DEF_KEY).singleResult();
 ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEF_KEY);

 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new TaskType(SERVICE_TASK_ID), processInstance.getId(),
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, TASK_WAIT_TIME, TASK_WAIT_TIME_LONG), true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskRepository);
 BaseRuntimeFlowableDelegate<?> target = createMockServiceTaskDelegate();

 flowTaskInstance.bindServiceTask(target);

 // When
 String resultFromInstance = null;
 try {
 resultFromInstance = (String) flowTaskInstance.getResult(EXECUTION_TIMEOUT_SHORT, TimeUnit.MILLISECONDS);
 } catch (InterruptedException e) {
 } catch (ExecutionException e) {
 }

 // Then
 assertThat(resultFromInstance).isNull();
 }
}