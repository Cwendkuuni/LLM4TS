import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.test.AbstractTestProcessEngineConfiguration;
import org.flowable.engine.impl.test.FlowableTest;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;
import org.flowable.engine.task.TaskQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

public class FlowTaskInstanceTest extends FlowableTest {

 private static final String PROCESS_DEFINITION_KEY = "testProcessDefinitionKey";
 private static final String FLOW_INSTANCE_ID = "testFlowInstanceId";
 private static final String TASK_DEFINITION_KEY = "testTaskDefinitionKey";
 private static final String TASK_ID = "testTaskId";
 private static final String USER_ID = "testUserId";
 private static final String ORGANIZATION_ID = "testOrganizationId";
 private static final long WAIT_EXECUTION_EXPIRE_INTERVAL_SECONDS = 10L;
 private static final long EXECUTION_TIME = 5000L;
 private static final FlowNodeStatus FLOW_NODE_STATUS = FlowNodeStatus.PENDING;
 private static final FlowNodeType FLOW_NODE_TYPE = FlowNodeType.SERVICE_TASK;
 private static final TaskType TASK_TYPE = TaskType.SOME_TASK_TYPE;
 private static final Long TARGET_TASK_ID = 1L;
 private static final String ABORT_VARIABLE_NAME = "aborted";
 private static final boolean ABORT = false;
 private static final boolean DONE = true;
 private static final boolean IS_PRESENT_ON_THIS_MACHINE = true;
 private static final boolean MAY_INTERRUPT_IF_RUNNING = true;
 private static final String RESOURCE_TYPE = ResourceType.ODC_FLOW_TASK_INSTANCE.name();
 private static final Class<? extends BaseRuntimeFlowableDelegate<?>> TARGET_DELEGATE_CLASS = BaseRuntimeFlowableDelegate.class;
 private static final RuntimeService runtimeService;
 private static final TaskService taskService;
 private static final IdentityService identityService;
 private static final ProcessEngineConfiguration processEngineConfiguration;
 private static final NodeInstanceEntityRepository nodeInstanceEntityRepository;
 private static final ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private static final RuntimeDelegateMapper runtimeDelegateMapper;
 private static final FlowableAdaptor flowableAdaptor;
 private static final FlowTaskInstance flowTaskInstance;
 private static final BaseRuntimeFlowableDelegate<?> targetTaskHandle;
 private static final ServiceTaskInstanceEntity serviceTaskInstanceEntity;
 private static final ProcessInstance processInstance;
 private static final TaskEntity taskEntity;

 @Before
 public void setup() {
 processEngineConfiguration = new AbstractTestProcessEngineConfiguration() {
 @Override
 public void configureProcessEngine(ProcessEngineConfiguration engineConfiguration) {
 engineConfiguration.setJobExecutorActivate(false);
 engineConfiguration.setTaskExecutorActivate(false);
 engineConfiguration.setDatabaseType("h2");
 engineConfiguration.setJdbcUrl("jdbc:h2:mem:test");
 engineConfiguration.setJdbcDriver("org.h2.Driver");
 engineConfiguration.setJdbcUsername("sa");
 engineConfiguration.setJdbcPassword("sa");
 engineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);
 }
 };
 processEngineConfiguration.buildProcessEngine();
 runtimeService = processEngineConfiguration.getRuntimeService();
 taskService = processEngineConfiguration.getTaskService();
 identityService = processEngineConfiguration.getIdentityService();
 nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
 serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
 runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);
 flowableAdaptor = new FlowableAdaptor(nodeInstanceEntityRepository, serviceTaskInstanceRepository, runtimeDelegateMapper);

 serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setOrganizationId(ORGANIZATION_ID);
 serviceTaskInstanceEntity.setStatus(FLOW_NODE_STATUS);
 serviceTaskInstanceEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
 serviceTaskInstanceEntity.setStartEndpoint(true);
 serviceTaskInstanceEntity.setEndEndpoint(true);
 serviceTaskInstanceEntity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
 serviceTaskInstanceEntity.setTaskType(TASK_TYPE);
 serviceTaskInstanceEntity.setTargetTaskId(TARGET_TASK_ID);
 serviceTaskInstanceEntity.setWaitExecExpireIntervalSeconds(WAIT_EXECUTION_EXPIRE_INTERVAL_SECONDS);
 serviceTaskInstanceEntity.setExecutionTime(EXECUTION_TIME);

 processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY);

 taskEntity = new TaskEntity();
 taskEntity.setId(TASK_ID);
 taskEntity.setName(TASK_DEFINITION_KEY);
 taskEntity.setProcessInstanceId(processInstance.getId());
 taskEntity.setExecutionId(processInstance.getId());
 taskEntity.setCreateTime(System.currentTimeMillis());
 taskEntity.setUpdateTime(System.currentTimeMillis());
 taskEntity.setAssignee(USER_ID);
 taskEntity.setPriority(0);
 taskEntity.setDueDate(null);
 taskEntity.setDescription(null);
 taskEntity.setOwner(null);
 taskEntity.setTenantId(null);
 taskEntity.setDelegationState(null);
 taskEntity.setParentTaskId(null);
 taskEntity.setParentTaskExecutionId(null);
 taskEntity.setFormKey(null);
 taskEntity.setProcessDefinitionId(null);
 taskEntity.setProcessDefinitionKey(null);
 taskEntity.setExecution(new ExecutionEntityImpl());

 when(nodeInstanceEntityRepository.save(any())).thenReturn(serviceTaskInstanceEntity);
 when(serviceTaskInstanceRepository.save(any())).thenReturn(serviceTaskInstanceEntity);
 when(runtimeDelegateMapper.map(eq(TASK_TYPE), any())).thenReturn(targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class));

 flowTaskInstance = new FlowTaskInstance(null, serviceTaskInstanceEntity, flowableAdaptor, identityService, taskService,
 nodeInstanceEntityRepository, serviceTaskInstanceRepository, runtimeService, TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
 serviceTaskInstanceEntity.getStrategy(), true, true, runtimeDelegateMapper, flowableAdaptor, identityService, taskService);
 }

 @After
 public void teardown() {
 processEngineConfiguration.close();
 }

 @Test
 public void testCreate() {
 // given
 FlowTaskInstance expectedFlowTaskInstance = new FlowTaskInstance(null, serviceTaskInstanceEntity, flowableAdaptor,
 identityService, taskService, nodeInstanceEntityRepository, serviceTaskInstanceRepository, runtimeService,
 TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, serviceTaskInstanceEntity.getStrategy(), true, true, runtimeDelegateMapper,
 flowableAdaptor, identityService, taskService);

 // when
 flowTaskInstance.create();

 // then
 verify(nodeInstanceEntityRepository).save(eq(serviceTaskInstanceEntity));
 verify(serviceTaskInstanceRepository).save(eq(serviceTaskInstanceEntity));
 assertEquals(expectedFlowTaskInstance.getId(), flowTaskInstance.getId());
 assertEquals(expectedFlowTaskInstance.getFlowInstanceId(), flowTaskInstance.getFlowInstanceId());
 assertEquals(expectedFlowTaskInstance.getCreateTime(), flowTaskInstance.createTime);
 assertEquals(expectedFlowTaskInstance.getUpdateTime(), flowTaskInstance.updateTime);
 assertEquals(expectedFlowTaskInstance.getStatus(), flowTaskInstance.getStatus());
 assertEquals(expectedFlowTaskInstance.isStartEndpoint(), flowTaskInstance.isStartEndpoint());
 assertEquals(expectedFlowTaskInstance.isEndEndpoint(), flowTaskInstance.isEndEndpoint());
 assertEquals(expectedFlowTaskInstance.getNodeType(), flowTaskInstance.getNodeType());
 assertEquals(expectedFlowTaskInstance.getTaskType(), flowTaskInstance.getTaskType());
 assertEquals(expectedFlowTaskInstance.getOrganizationId(), flowTaskInstance.getOrganizationId());
 }

 @Test
 public void testUpdate() {
 // given
 FlowTaskInstance expectedFlowTaskInstance = new FlowTaskInstance(null, serviceTaskInstanceEntity, flowableAdaptor,
 identityService, taskService, nodeInstanceEntityRepository, serviceTaskInstanceRepository, runtimeService,
 TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, serviceTaskInstanceEntity.getStrategy(), true, true, runtimeDelegateMapper,
 flowableAdaptor, identityService, taskService);
 expectedFlowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);

 // when
 flowTaskInstance.update();

 // then
 verify(serviceTaskInstanceRepository).update(eq(serviceTaskInstanceEntity));
 assertEquals(expectedFlowTaskInstance.getStatus(), flowTaskInstance.getStatus());
 }

 @Test
 public void testBindServiceTask() {
 // given
 when(taskService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list().get(0).getId())
 .thenReturn(TASK_ID);
 when(taskService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list().get(0).getName())
 .thenReturn(TASK_DEFINITION_KEY);

 // when
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));

 // then
 verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // given
 when(targetTaskHandle != null).thenReturn(IS_PRESENT_ON_THIS_MACHINE);

 // when
 boolean result = flowTaskInstance.isPresentOnThisMachine();

 // then
 assertTrue(result);
 }

 @Test
 public void testIsPresentOnThisMachine_whenNotPresent() {
 // given
 when(targetTaskHandle != null).thenReturn(false);

 // when
 boolean result = flowTaskInstance.isPresentOnThisMachine();

 // then
 assertFalse(result);
 }

 @Test
 public void testDealloc() {
 // given
 when(eventPublisher.removeEventListener(any())).thenReturn(true);

 // when
 flowTaskInstance.dealloc();

 // then
 verify(eventPublisher).removeEventListener(flowTaskInstance.listener);
 }

 @Test
 public void testAlloc() {
 // given
 when(eventPublisher.addEventListener(any())).thenReturn(flowTaskInstance.listener);

 // when
 flowTaskInstance.alloc();

 // then
 verify(eventPublisher).addEventListener(flowTaskInstance.listener);
 }

 @Test
 public void testConfirmExecute() throws InterruptedException, ExecutionException {
 // given
 when(flowableAdaptor.getFlowableElementByType(eq(FLOW_INSTANCE_ID), eq(FLOW_NODE_TYPE), eq(FlowableElementType.USER_TASK)))
 .thenReturn(Collections.singletonList(new FlowableElement()));
 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID)).thenReturn(Optional.of(processInstance.getId()));

 // when
 flowTaskInstance.confirmExecute();

 // then
 List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
 assertTrue(tasks.size() == 1);
 assertTrue(tasks.get(0).getId().equals(TASK_ID));
 assertTrue(tasks.get(0).getStatus().equals(Task.Status.COMPLETED));
 }

 @Test
 public void testAbort() throws InterruptedException, ExecutionException {
 // given
 when(flowableAdaptor.getFlowableElementByType(eq(FLOW_INSTANCE_ID), eq(FLOW_NODE_TYPE), eq(FlowableElementType.USER_TASK)))
 .thenReturn(Collections.singletonList(new FlowableElement()));
 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID)).thenReturn(Optional.of(processInstance.getId()));
 when(taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0).getId())
 .thenReturn(TASK_ID);

 // when
 flowTaskInstance.abort();

 // then
 List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
 assertTrue(tasks.size() == 1);
 assertTrue(tasks.get(0).getStatus().equals(Task.Status.COMPLETED));
 assertTrue(tasks.get(0).getVariables().get(ABORT_VARIABLE_NAME).equals(ABORT));
 }

 @Test
 public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 when(taskService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list().get(0).getId())
 .thenReturn(TASK_ID);

 // when
 flowTaskInstance.cancel(MAY_INTERRUPT_IF_RUNNING);

 // then
 verify(targetTaskHandle).cancel(MAY_INTERRUPT_IF_RUNNING);
 }

 @Test
 public void testIsDone() {
 // given
 when(targetTaskHandle.isDone()).thenReturn(DONE);

 // when
 boolean result = flowTaskInstance.isDone();

 // then
 assertTrue(result);
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // given
 when(targetTaskHandle.get()).thenReturn("result");

 // when
 String result = (String) flowTaskInstance.getResult();

 // then
 assertEquals("result", result);
 }

 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // given
 when(targetTaskHandle.get(WAIT_EXECUTION_EXPIRE_INTERVAL_SECONDS, TimeUnit.SECONDS)).thenThrow(new TimeoutException());

 // when
 try {
 flowTaskInstance.getResult(WAIT_EXECUTION_EXPIRE_INTERVAL_SECONDS, TimeUnit.SECONDS);
 } catch (Exception e) {
 // then
 assertTrue(e instanceof TimeoutException);
 }
 }

 @Test
 public void testGetResultWithExecutionException() throws InterruptedException, ExecutionException {
 // given
 when(targetTaskHandle.get()).thenThrow(new ExecutionException("testException"));

 // when
 try {
 flowTaskInstance.getResult();
 } catch (Exception e) {
 // then
 assertEquals("testException", e.getMessage());
 }
 }

 @Test
 public void testGetResultWithInterruptedException() throws InterruptedException, ExecutionException {
 // given
 when(targetTaskHandle.get()).thenThrow(new InterruptedException("testInterruptedException"));

 // when
 try {
 flowTaskInstance.getResult();
 } catch (Exception e) {
 // then
 assertEquals("testInterruptedException", e.getMessage());
 }
 }

 @Test
 public void testResourceType() {
 // given
 assertEquals(RESOURCE_TYPE, flowTaskInstance.resourceType());
 }

 @Test
 public void testGetTargetDelegateClass() {
 // given
 assertEquals(TARGET_DELEGATE_CLASS, flowTaskInstance.getTargetDelegateClass());
 }
}