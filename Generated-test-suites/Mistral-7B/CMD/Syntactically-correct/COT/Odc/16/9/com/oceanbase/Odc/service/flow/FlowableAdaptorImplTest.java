package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Slf4j
public class FlowableAdaptorImplTest {

 private FlowableAdaptor adaptor;
 private TaskService taskService;
 private FormService formService;
 private EventPublisher eventPublisher;
 private AuthenticationFacade authenticationFacade;
 private NodeInstanceEntityRepository nodeInstanceRepository;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private GateWayInstanceRepository gateWayInstanceRepository;
 private UserTaskInstanceRepository userTaskInstanceRepository;
 private FlowInstanceRepository flowInstanceRepository;
 private SequenceInstanceRepository sequenceRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 adaptor = new FlowableAdaptorImpl();
 taskService = mock(TaskService.class);
 formService = mock(FormService.class);
 eventPublisher = mock(EventPublisher.class);
 authenticationFacade = mock(AuthenticationFacade.class);
 nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
 serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
 gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
 userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
 flowInstanceRepository = mock(FlowInstanceRepository.class);
 sequenceRepository = mock(SequenceInstanceRepository.class);
 when(taskService.getTaskInstanceById(any(String.class), any(String.class))).thenReturn(mock(ServiceTaskInstanceEntity.class));
 when(formService.getTaskFormData(any(String.class), any(String.class))).thenReturn("{}");
 }

 @Test
 public void testSetProcessInstanceId() {
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";
 adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
 }

 @Test(expected = AssertionError.class)
 public void testSetProcessInstanceIdWithInvalidFlowInstanceId() {
 Long flowInstanceId = null;
 String processInstanceId = "processInstanceId";
 adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
 }

 @Test(expected = AssertionError.class)
 public void testSetProcessInstanceIdWithInvalidProcessInstanceId() {
 Long flowInstanceId = 1L;
 String processInstanceId = null;
 adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 String processInstanceId = "processInstanceId";
 Optional<Long> result = adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
 Long expectedFlowInstanceId = 1L;
 assertEquals(expectedFlowInstanceId, result.get());
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceIdWithEmptyResult() {
 String processInstanceId = "processInstanceId";
 when(flowInstanceRepository.findByProcessInstanceId(processInstanceId)).thenReturn(Collections.emptyList());
 Optional<Long> result = adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
 assertEquals(Optional.empty(), result);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceIdWithMultipleResult() {
 String processInstanceId = "processInstanceId";
 List<FlowInstanceEntity> flowInstanceEntities = Arrays.asList(
 new FlowInstanceEntity(1L, processInstanceId, null),
 new FlowInstanceEntity(2L, processInstanceId, null));
 when(flowInstanceRepository.findByProcessInstanceId(processInstanceId)).thenReturn(flowInstanceEntities);
 Optional<Long> result = adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
 assertEquals(Optional.of(2L), result);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 String processDefinitionId = "processDefinitionId";
 Optional<Long> result = adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
 Long expectedFlowInstanceId = 1L;
 assertEquals(expectedFlowInstanceId, result.get());
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionIdWithEmptyResult() {
 String processDefinitionId = "processDefinitionId";
 when(flowInstanceRepository.findByProcessDefinitionId(processDefinitionId)).thenReturn(Collections.emptyList());
 Optional<Long> result = adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
 assertEquals(Optional.empty(), result);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionIdWithMultipleResult() {
 String processDefinitionId = "processDefinitionId";
 List<FlowInstanceEntity> flowInstanceEntities = Arrays.asList(
 new FlowInstanceEntity(1L, null, processDefinitionId),
 new FlowInstanceEntity(2L, null, processDefinitionId));
 when(flowInstanceRepository.findByProcessDefinitionId(processDefinitionId)).thenReturn(flowInstanceEntities);
 Optional<Long> result = adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
 assertEquals(Optional.of(1L), result);
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";
 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity(flowInstanceId,
 processInstanceId, null)));
 Optional<String> result = adaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
 assertEquals(processInstanceId, result.get());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceIdWithEmptyResult() {
 Long flowInstanceId = 1L;
 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());
 Optional<String> result = adaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
 assertEquals(Optional.empty(), result);
 }

 @Test
 public void testGetFlowableElementByType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.SEQUENCE;
 FlowableElementType flowableElementType = FlowableElementType.GATEWAY;
 List<FlowableElement> result = adaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
 assertEquals(2, result.size());
 }

 @Test
 public void testGetFlowableElementByTypeWithEmptyResult() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.SEQUENCE;
 FlowableElementType flowableElementType = FlowableElementType.GATEWAY;
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
 flowableElementType)).thenReturn(Collections.emptyList());
 List<FlowableElement> result = adaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 Optional<FlowTaskInstance> result = adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
 FlowTaskInstance expectedResult = new FlowTaskInstance(
 new ServiceTaskInstanceEntity(1L, activityId, flowInstanceId, null, null, null, null, null, null, null),
 new OdcRuntimeDelegateMapper(), taskService, eventPublisher, null, nodeRepository, sequenceRepository,
 serviceTaskInstanceRepository);
 assertEquals(expectedResult, result.get());
 }

 @Test
 public void testGetTaskInstanceByActivityIdWithEmptyResult() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.empty());
 Optional<FlowTaskInstance> result = adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
 assertEquals(Optional.empty(), result);
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
 FlowApprovalInstance expectedResult = new FlowApprovalInstance(
 new UserTaskInstanceEntity(1L, activityId, flowInstanceId, null, null, null, null, null, null, null, null),
 adaptor, taskService, formService, authenticationFacade, nodeRepository, sequenceRepository,
 userTaskInstanceRepository);
 assertEquals(expectedResult, result.get());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 String name = "name";
 Long flowInstanceId = 1L;
 Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByName(name, flowInstanceId);
 FlowApprovalInstance expectedResult = new FlowApprovalInstance(
 new UserTaskInstanceEntity(1L, null, flowInstanceId, name, null, null, null, null, null, null, null),
 adaptor, taskService, formService, authenticationFacade, nodeRepository, sequenceRepository,
 userTaskInstanceRepository);
 assertEquals(expectedResult, result.get());
 }

 @Test
 public void testGetApprovalInstanceByNameWithEmptyResult() {
 String name = "name";
 Long flowInstanceId = 1L;
 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId)).thenReturn(Optional.empty());
 Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByName(name, flowInstanceId);
 assertEquals(Optional.empty(), result);
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 Optional<FlowGatewayInstance> result = adaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
 FlowGatewayInstance expectedResult = new FlowGatewayInstance(
 new GateWayInstanceEntity(1L, activityId, flowInstanceId, null, null, null), adaptor, null, sequenceRepository,
 gateWayInstanceRepository);
 assertEquals(expectedResult, result.get());
 }

 @Test
 public void testGetGatewayInstanceByActivityIdWithEmptyResult() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId)).thenReturn(Optional.empty());
 Optional<FlowGatewayInstance> result = adaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
 assertEquals(Optional.empty(), result);
 }

 @Test
 public void testSetFlowableElement() {
 BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
 nodeInstance.setId(1L);
 nodeInstance.setNodeType(FlowNodeType.SEQUENCE);
 nodeInstance.setFlowInstanceId(1L);
 FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.SEQUENCE);
 adaptor.setFlowableElement(nodeInstance, flowableElement);
 }

 @Test(expected = AssertionError.class)
 public void testSetFlowableElementWithNullFlowableElement() {
 BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
 nodeInstance.setId(1L);
 nodeInstance.setNodeType(FlowNodeType.SEQUENCE);
 nodeInstance.setFlowInstanceId(1L);
 adaptor.setFlowableElement(nodeInstance, null);
 }

 @Test(expected = AssertionError.class)
 public void testSetFlowableElementWithExistingElement() {
 BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
 nodeInstance.setId(1L);
 nodeInstance.setNodeType(FlowNodeType.SEQUENCE);
 nodeInstance.setFlowInstanceId(1L);
 FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.SEQUENCE);
 nodeInstance.setFlowableElement(flowableElement);
 adaptor.setFlowableElement(nodeInstance, flowableElement);
 }
}