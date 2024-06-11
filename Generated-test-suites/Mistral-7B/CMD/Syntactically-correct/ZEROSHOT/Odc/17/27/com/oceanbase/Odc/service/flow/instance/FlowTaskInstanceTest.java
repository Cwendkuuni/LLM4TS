package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;

import org.apache.commons.collections4.Collectors;
import org.apache.commons.collections4.ListUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.task.Task;
import org.flowable.engine.task.TaskQuery;
import org.flowable.task.service.event.EventType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowTaskInstanceTest {

 private static final Long FLOW_INSTANCE_ID = 1L;
 private static final Long ORGANIZATION_ID = 1L;
 private static final Long NODE_ID = 1L;
 private static final Long TASK_TYPE_ID = 1L;
 private static final Long TARGET_TASK_ID = 2L;
 private static final FlowableExecutionStrategyConfig STRATEGY_CONFIG = new FlowableExecutionStrategyConfig();
 private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
 private static final FlowTaskExecutionStrategy FLOW_EXECUTION_STRATEGY = FlowTaskExecutionStrategy.MANUAL;

 private FlowTaskInstanceFlowableAdaptor flowableAdaptor;
 private NodeInstanceEntityRepository nodeInstanceRepository;
 private SequenceInstanceRepository sequenceInstanceRepository;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private EventPublisher eventPublisher;
 private RuntimeService runtimeService;

 @Before
 public void setUp() {
 flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
 nodeInstanceRepository = Mockito.mock(NodeInstanceEntityRepository.class);
 sequenceInstanceRepository = Mockito.mock(SequenceInstanceRepository.class);
 serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 runtimeService = Mockito.mock(RuntimeService.class);

 Mockito.when(nodeInstanceRepository.findById(NODE_ID)).thenReturn(Optional.ofNullable(new NodeInstanceEntity()));
 Mockito.when(sequenceInstanceRepository.findByNodeInstanceId(NODE_ID)).thenReturn(ListUtils.emptyList());
 Mockito.when(serviceTaskInstanceRepository.findByFlowInstanceId(FLOW_INSTANCE_ID)).thenReturn(ListUtils.emptyList());
 }

 @After
 public void tearDown() {
 // reset mocks
 }

 @Test
 public void testFlowTaskInstanceCreation() {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);

 assertEquals(NODE_ID, instance.getId());
 assertEquals(ORGANIZATION_ID, instance.getOrganizationId());
 assertEquals(FLOW_INSTANCE_ID, instance.getFlowInstanceId());
 assertEquals(FlowNodeType.SERVICE_TASK, instance.getNodeType());
 assertEquals(FlowNodeStatus.CREATED, instance.getStatus());
 assertEquals(FLOW_EXECUTION_STRATEGY, instance.strategyConfig.getStrategy());
 assertNull(instance.getTargetTaskId());
 assertFalse(instance.isStartEndpoint());
 assertFalse(instance.isEndEndPoint());
 }

 @Test
 public void testFlowTaskInstanceCreationWithTargetTaskId() {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository, TARGET_TASK_ID);

 assertEquals(NODE_ID, instance.getId());
 assertEquals(ORGANIZATION_ID, instance.getOrganizationId());
 assertEquals(FLOW_INSTANCE_ID, instance.getFlowInstanceId());
 assertEquals(FlowNodeType.SERVICE_TASK, instance.getNodeType());
 assertEquals(FlowNodeStatus.CREATED, instance.getStatus());
 assertEquals(FLOW_EXECUTION_STRATEGY, instance.strategyConfig.getStrategy());
 assertEquals(TARGET_TASK_ID, instance.getTargetTaskId());
 assertFalse(instance.isStartEndpoint());
 assertFalse(instance.isEndEndPoint());
 }

 @Test
 public void testFlowTaskInstanceSaveToDatabase() {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);

 instance.create();
 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).save(Mockito.any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testFlowTaskInstanceUpdateInDatabase() {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);

 instance.create();
 instance.update();
 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).update(Mockito.any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testFlowTaskInstanceDeletion() {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);

 instance.create();
 instance.delete(NODE_ID);
 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).deleteById(NODE_ID);
 }

 @Test
 public void testBindingTaskToFlowTaskInstance() {
 BaseRuntimeFlowableDelegate targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);

 assertEquals(FlowNodeStatus.PENDING, instance.getStatus());
 instance.bindServiceTask(targetTaskHandle);
 assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
 }

 @Test
 public void testUnbindingTaskFromFlowTaskInstance() {
 BaseRuntimeFlowableDelegate targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);

 instance.bindServiceTask(targetTaskHandle);
 instance.dealloc();
 assertEquals(FlowNodeStatus.PENDING, instance.getStatus());
 }

 @Test
 public void testTaskExecutionWithAbort() {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);
 instance.create();

 Mockito.when(flowableAdaptor.getFlowableElementByType(NODE_ID, NODE_TYPE, FlowableElementType.USER_TASK))
 .thenReturn(List.of(new FlowableElement()));

 Mockito.when(runtimeService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list())
 .thenReturn(List.of(Mockito.mock(Task.class)));

 instance.abort();

 Mockito.verify(flowableAdaptor, Mockito.times(1)).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 Mockito.verify(runtimeService, Mockito.times(1)).createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list();
 Mockito.verify(flowableAdaptor, Mockito.times(1)).getFlowableElementByType(NODE_ID, NODE_TYPE, FlowableElementType.USER_TASK);
 Mockito.verify(eventPublisher, Mockito.atLeastOnce()).publishEvent(Mockito.any(EventType.class));
 }

 @Test
 public void testTaskExecutionWithConfirm() {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);
 instance.create();

 Mockito.when(flowableAdaptor.getFlowableElementByType(NODE_ID, NODE_TYPE, FlowableElementType.USER_TASK))
 .thenReturn(List.of(new FlowableElement()));

 Mockito.when(runtimeService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list())
 .thenReturn(List.of(Mockito.mock(Task.class)));

 instance.confirmExecute();

 Mockito.verify(flowableAdaptor, Mockito.times(1)).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 Mockito.verify(runtimeService, Mockito.times(1)).createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list();
 Mockito.verify(flowableAdaptor, Mockito.times(1)).getFlowableElementByType(NODE_ID, NODE_TYPE, FlowableElementType.USER_TASK);
 Mockito.verify(eventPublisher, Mockito.atLeastOnce()).publishEvent(Mockito.any(EventType.class));
 }

 @Test
 public void testTaskExecutionWithCancel() {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);
 instance.create();
 BaseRuntimeFlowableDelegate targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 instance.bindServiceTask(targetTaskHandle);

 Mockito.when(flowableAdaptor.getFlowableElementByType(NODE_ID, NODE_TYPE, FlowableElementType.USER_TASK))
 .thenReturn(List.of(new FlowableElement()));

 Mockito.when(runtimeService.createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list())
 .thenReturn(List.of(Mockito.mock(Task.class)));

 instance.cancel(true);

 Mockito.verify(flowableAdaptor, Mockito.times(1)).getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 Mockito.verify(runtimeService, Mockito.times(1)).createTaskQuery().processInstanceId(FLOW_INSTANCE_ID).list();
 Mockito.verify(flowableAdaptor, Mockito.times(1)).getFlowableElementByType(NODE_ID, NODE_TYPE, FlowableElementType.USER_TASK);
 Mockito.verify(targetTaskHandle, Mockito.times(1)).cancel(true);
 Mockito.verify(eventPublisher, Mockito.atLeastOnce()).publishEvent(Mockito.any(EventType.class));
 }

 @Test
 public void testTaskExecutionWithGetResult() throws InterruptedException, ExecutionException, TimeoutException {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);
 instance.create();
 BaseRuntimeFlowableDelegate targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 instance.bindServiceTask(targetTaskHandle);
 Mockito.when(targetTaskHandle.get()).thenReturn("Result");

 String result = instance.getResult().get();
 assertEquals("Result", result);
 }

 @Test
 public void testTaskExecutionWithGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE_ID, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
 false, false, flowableAdaptor, eventPublisher, runtimeService, nodeInstanceRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);
 instance.create();
 BaseRuntimeFlowableDelegate targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 instance.bindServiceTask(targetTaskHandle);

 Mockito.when(targetTaskHandle.get()).thenThrow(new InterruptedException());
 Mockito.when(targetTaskHandle.get(1, TimeUnit.SECONDS)).thenReturn("Result");

 String result = instance.getResult(1, TimeUnit.SECONDS);
 assertEquals("Result", result);
 }
}