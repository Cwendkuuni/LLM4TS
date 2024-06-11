import org.apache.commons.lang3.RandomUtils;import org.junit.Assert;import org.junit.Before;import org.junit.Test;import org.mockito.Mockito;import org.mockito.MockitoAnnotations;import org.flowable.engine.impl.interceptor.CommandContext;import org.flowable.engine.impl.persistence.entity.ExecutionEntity;import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;import org.flowable.engine.impl.persistence.entity.TaskEntity;import org.flowable.engine.impl.persistence.entity.VariableEntity;import org.flowable.engine.runtime.Execution;import org.flowable.engine.runtime.ProcessInstance;import org.flowable.engine.runtime.Task;import org.flowable.engine.runtime.VariableScope;import org.flowable.task.api.ExecutionQuery;import org.flowable.task.api.ProcessInstanceQuery;import org.flowable.task.api.TaskQuery;

import com.oceanbase.odc.core.shared.constant.TaskType;import com.oceanbase.odc.service.flow.FlowableAdaptor;import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;import com.oceanbase.odc.service.flow.FlowTaskInstance;import com.oceanbase.odc.service.flow.FlowTaskInstanceRepository;import com.oceanbase.odc.service.flow.FlowTaskInstanceRepositoryImpl;import com.oceanbase.odc.service.flow.RuntimeDelegateMapper;import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;import com.oceanbase.odc.service.flow.model.FlowNodeStatus;import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;

import java.util.Collections;import java.util.HashMap;import java.util.List;import java.util.Map;import java.util.Optional;import java.util.concurrent.TimeUnit;

/**
 * Test class for FlowTaskInstance
 */
public class FlowTaskInstanceTest {

 private static final String PROCESS_INSTANCE_ID = "processInstanceId";
 private static final String FLOW_INSTANCE_ID = "flowInstanceId";
 private static final String TASK_INSTANCE_ID = "taskInstanceId";
 private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
 private static final long WAIT_EXEC_EXPIRE_INTERVAL = 1000L;
 private static final long EXECUTION_TIME = 5000L;
 private static final FlowNodeStatus FLOW_NODE_STATUS = FlowNodeStatus.PENDING;
 private static final String EXECUTION_ID = "executionId";
 private static final String TARGET_TASK_ID = "targetTaskId";
 private static final long ORGANIZATION_ID = 1L;
 private static final long ID = 1L;
 private static final int RANDOM_NUMBER = RandomUtils.nextInt(100);
 private FlowTaskInstanceRepository flowTaskInstanceRepository;
 private FlowTaskInstance instance;
 private FlowableAdaptor flowableAdaptor;
 private RuntimeDelegateMapper runtimeDelegateMapper;
 private FlowableAdaptorImpl flowableAdaptorImpl;
 private ProcessInstance processInstance;
 private Task task;
 private Execution execution;
 private CommandContext commandContext;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 processInstance = Mockito.mock(ProcessInstance.class);
 task = Mockito.mock(Task.class);
 execution = Mockito.mock(Execution.class);
 commandContext = Mockito.mock(CommandContext.class);
 flowTaskInstanceRepository = new FlowTaskInstanceRepositoryImpl();
 flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
 flowableAdaptorImpl = new FlowableAdaptorImpl();
 runtimeDelegateMapper = Mockito.mock(RuntimeDelegateMapper.class);

 instance = new FlowTaskInstance(null, TASK_TYPE, ORGANIZATION_ID, FLOW_NODE_STATUS, false, false,
 runtimeDelegateMapper, flowableAdaptor, ORGANIZATION_ID, FLOWABLE_ADAPTOR,
 commandContext, flowTaskInstanceRepository, flowableAdaptorImpl, flowableAdaptorImpl,
 FLOW_INSTANCE_ID);

 Mockito.when(commandContext.getVariableScope()).thenReturn(new VariableScope());
 Mockito.when(flowableAdaptor.getFlowableElementByType(ID, FlowNodeType.USER_TASK, FlowableElementType.USER_TASK))
 .thenReturn(Collections.emptyList());
 Mockito.when(processInstance.getId()).thenReturn(PROCESS_INSTANCE_ID);
 Mockito.when(task.getId()).thenReturn(TASK_INSTANCE_ID);
 Mockito.when(execution.getId()).thenReturn(EXECUTION_ID);
 Mockito.when(execution.getProcessInstanceId()).thenReturn(PROCESS_INSTANCE_ID);
 Mockito.when(commandContext.getProcessInstanceQuery()).thenReturn(Mockito.mock(ProcessInstanceQuery.class));
 Mockito.when(commandContext.getExecutionQuery()).thenReturn(Mockito.mock(ExecutionQuery.class));
 Mockito.when(commandContext.getTaskQuery()).thenReturn(Mockito.mock(TaskQuery.class));
 }

 @Test
 public void testCreate() {
 // Create test
 instance.create();

 // Assertions
 Assert.assertNotNull(instance.getId());
 Assert.assertNotNull(instance.getCreateTime());
 Assert.assertNotNull(instance.getUpdateTime());
 Assert.assertEquals(FLOW_NODE_STATUS, instance.getStatus());
 Assert.assertEquals(FLOW_INSTANCE_ID, instance.getFlowInstanceId());
 Assert.assertEquals(ORGANIZATION_ID, instance.getOrganizationId());
 Assert.assertEquals(ID, instance.getId());
 Assert.assertEquals(FLOW_NODE_STATUS, instance.getStatus());
 }

 @Test
 public void testComplete() {
 // Create test
 instance.create();

 // Setup
 Mockito.when(flowableAdaptor.getFlowableElementByType(ID, FlowNodeType.USER_TASK, FlowableElementType.USER_TASK))
 .thenReturn(Collections.singletonList(Mockito.mock(FlowableElement.class)));
 Mockito.when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID))
 .thenReturn(Optional.of(PROCESS_INSTANCE_ID));
 Mockito.when(taskService.createTaskQuery().processInstanceId(PROCESS_INSTANCE_ID).list())
 .thenReturn(Collections.singletonList(task));
 Mockito.when(taskService.complete(task.getId(), Mockito.anyMap()))
 .thenReturn(null);

 // Test
 instance.complete(false);

 // Assertions
 Mockito.verify(commandContext).getVariableScope();
 Mockito.verify(flowableAdaptor).getFlowableElementByType(ID, FlowNodeType.USER_TASK, FlowableElementType.USER_TASK);
 Mockito.verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 Mockito.verify(taskService).createTaskQuery().processInstanceId(PROCESS_INSTANCE_ID).list();
 Mockito.verify(taskService).complete(task.getId(), Mockito.anyMap());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testComplete_automaticTasks() {
 // Create test
 instance.create();

 // Setup
 instance.strategyConfig = ExecutionStrategyConfig.from(new FlowTaskExecutionStrategy(FlowTaskExecutionStrategy.AUTO));

 // Test
 instance.complete(false);

 // Assertions
 // None
 }

 @Test
 public void testGetTargetDelegateClass() {
 // Test
 Class<?> targetDelegateClass = instance.getTargetDelegateClass();

 // Assertions
 Assert.assertNotNull(targetDelegateClass);
 }

 @Test
 public void testIsDone() {
 // Setup
 instance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));

 // Test
 boolean result = instance.isDone();

 // Assertions
 Assert.assertFalse(result);
 }

 @Test
 public void testIsDone_done() {
 // Setup
 instance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));
 Mockito.when(commandContext.getVariableScope()).thenReturn(new VariableScope());
 Mockito.when(taskService.createTaskQuery().processInstanceId(PROCESS_INSTANCE_ID).list())
 .thenReturn(Collections.singletonList(task));
 Mockito.when(task.getStatus()).thenReturn(Task.Status.COMPLETED);

 // Test
 boolean result = instance.isDone();

 // Assertions
 Assert.assertTrue(result);
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // Setup
 instance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));
 Mockito.when(commandContext.getVariableScope()).thenReturn(new VariableScope());
 Mockito.when(taskService.createTaskQuery().processInstanceId(PROCESS_INSTANCE_ID).list())
 .thenReturn(Collections.singletonList(task));
 Mockito.when(task.getExecutionResult()).thenReturn("result");

 // Test
 Object result = instance.getResult();

 // Assertions
 Assert.assertEquals("result", result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetResult_notPresent() {
 // Test
 Object result = instance.getResult();

 // Assertions
 // None
 }

 @Test
 public void testCancel() {
 // Setup
 instance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));

 // Test
 instance.cancel(true);

 // Assertions
 Mockito.verify(commandContext).getVariableScope();
 Mockito.verify(commandContext).getTaskQuery();
 Mockito.verify(taskService).cancelTaskInterruptably(TASK_INSTANCE_ID, true);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCancel_notPresent() {
 // Test
 instance.cancel(true);

 // Assertions
 // None
 }

 @Test
 public void testConfirmExecute() {
 // Test
 instance.confirmExecute();

 // Assertions
 Mockito.verify(flowableAdaptor).getFlowableElementByType(ID, FlowNodeType.USER_TASK, FlowableElementType.USER_TASK);
 Mockito.verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 Mockito.verify(taskService).createTaskQuery().processInstanceId(PROCESS_INSTANCE_ID).list();
 Mockito.verify(taskService).complete(Mockito.any(Task.class), Mockito.anyMap());
 Mockito.verify(flowableAdaptor).triggerEvent(Mockito.eq(TaskInstanceCreatedEvent.class),
 Mockito.any(TaskInstanceCreatedEvent.class));
 }

 @Test
 public void testAbort() {
 // Test
 instance.abort();

 // Assertions
 Mockito.verify(flowableAdaptor).getFlowableElementByType(ID, FlowNodeType.USER_TASK, FlowableElementType.USER_TASK);
 Mockito.verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 Mockito.verify(taskService).createTaskQuery().processInstanceId(PROCESS_INSTANCE_ID).list();
 Mockito.verify(taskService).complete(Mockito.any(Task.class), Mockito.anyMap());
 Mockito.verify(flowableAdaptor).triggerEvent(Mockito.eq(TaskInstanceCreatedEvent.class),
 Mockito.any(TaskInstanceCreatedEvent.class));

 // Assert abort variable
 Map<String, Object> variables = new HashMap<>();
 variables.put(ABORT_VARIABLE_NAME, true);
 Mockito.when(taskService.createTaskQuery().processInstanceId(PROCESS_INSTANCE_ID).list())
 .thenReturn(Collections.singletonList(task));
 Mockito.when(task.getVariables())
 .thenReturn(new VariableScope() {
 @Override
 public Map<String, Object> getVariablesLocal() {
 return variables;
 }
 });
 Mockito.when(taskService.complete(task.getId(), Mockito.anyMap()))
 .thenReturn(null);
 instance.complete(true);

 // Assertions
 Assert.assertTrue(variables.getBoolean(ABORT_VARIABLE_NAME));
 }
}