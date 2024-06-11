package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;

@SuppressWarnings("unchecked")
public class FlowableAdaptorImplTest {

 private FlowableAdaptor flowableAdaptor;
 private NodeInstanceRepository nodeInstanceRepository;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

 @Before
 public void setup() {
 flowableAdaptor = new FlowableAdaptorImpl();
 nodeInstanceRepository = Mockito.mock(NodeInstanceRepository.class);
 serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
 flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
 }

 @Test
 public void testSetProcessInstanceId() {
 Long flowInstanceId = 1L;
 String processInstanceId = "testProcessInstanceId";
 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
 Mockito.verify(nodeInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 String processInstanceId = "testProcessInstanceId";
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setProcessInstanceId(processInstanceId);
 Mockito.when(nodeInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(flowInstanceEntity));
 Long flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId).get();
 assertEquals(flowInstanceId, flowInstanceEntity.getId());
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId_notFound() {
 String processInstanceId = "testProcessInstanceId";
 Mockito.when(nodeInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
 assertFalse(flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId).isPresent());
 }

 @Test
 public void testSetProcessDefinitionId() {
 Long flowInstanceId = 1L;
 String processDefinitionId = "testProcessDefinitionId";
 flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
 Mockito.verify(nodeInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 String processDefinitionId = "testProcessDefinitionId";
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setProcessDefinitionId(processDefinitionId);
 Mockito.when(nodeInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(flowInstanceEntity));
 Long flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId).get();
 assertEquals(flowInstanceId, flowInstanceEntity.getId());
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId_notFound() {
 String processDefinitionId = "testProcessDefinitionId";
 Mockito.when(nodeInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
 assertFalse(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId).isPresent());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 Long flowInstanceId = 1L;
 String processInstanceId = "testProcessInstanceId";
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setProcessInstanceId(processInstanceId);
 Mockito.when(nodeInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
 assertEquals(processInstanceId, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId_notFound() {
 Long flowInstanceId = 1L;
 Mockito.when(nodeInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());
 assertNull(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId));
 }

 @Test
 public void testGetFlowableElementByType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
 FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
 List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
 flowableElementType);
 assertFalse(elements.isEmpty());
 }

 @Test
 public void testGetFlowableElementByType_notFound() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
 FlowableElementType flowableElementType = FlowableElementType.END_EVENT;
 List<FlowableElement> elements = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
 flowableElementType);
 assertTrue(elements.isEmpty());
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 String activityId = "testActivityId";
 Long flowInstanceId = 1L;
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
 FlowTaskInstance taskInstance = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId).get();
 assertSame(taskInstance, new FlowTaskInstance(serviceTaskInstanceEntity, new OdcRuntimeDelegateMapper(),
 flowableAdaptor, null, null, null, null, null, null));
 }

 @Test
 public void testGetTaskInstanceByActivityId_notFound() {
 String activityId = "testActivityId";
 Long flowInstanceId = 1L;
 Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.empty());
 assertFalse(flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId).isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 String activityId = "testActivityId";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
 FlowApprovalInstance approvalInstance = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId).get();
 assertSame(approvalInstance, new FlowApprovalInstance(userTaskInstanceEntity, flowableAdaptor, null, null,
 null, null, null, null, null));
 }

 @Test
 public void testGetApprovalInstanceByName() {
 String name = "testName";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(Optional.of(userTaskInstanceEntity));
 FlowApprovalInstance approvalInstance = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId).get();
 assertSame(approvalInstance, new FlowApprovalInstance(userTaskInstanceEntity, flowableAdaptor, null, null,
 null, null, null, null, null));
 }

 @Test
 public void testGetApprovalInstanceByName_notFound() {
 String name = "testName";
 Long flowInstanceId = 1L;
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(Optional.empty());
 assertFalse(flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId).isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 String activityId = "testActivityId";
 Long flowInstanceId = 1L;
 GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
 Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
 FlowGatewayInstance gatewayInstance = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId).get();
 assertSame(gatewayInstance, new FlowGatewayInstance(gateWayInstanceEntity, flowableAdaptor, null, null,
 null, null));
 }

 @Test
 public void testGetGatewayInstanceByActivityId_notFound() {
 String activityId = "testActivityId";
 Long flowInstanceId = 1L;
 Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId)).thenReturn(Optional.empty());
 assertFalse(flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId).isPresent());
 }

 @Test
 public void testSetFlowableElement() {
 Long nodeInstanceId = 1L;
 FlowableElement flowableElement = new FlowableElement("testActivityId", "testName", FlowableElementType.SERVICE_TASK);
 FlowNodeInstance nodeInstance = new BaseFlowNodeInstance(nodeInstanceId, FlowNodeType.SERVICE_TASK);
 flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
 List<NodeInstanceEntity> entities = nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
 nodeInstance.getNodeType(), flowableElement.getType());
 assertEquals(1, entities.size());
 assertEquals(flowableElement.getActivityId(), entities.get(0).getActivityId());
 assertEquals(flowableElement.getName(), entities.get(0).getName());
 assertEquals(flowableElement.getType(), entities.get(0).getFlowableElementType());
 }
}