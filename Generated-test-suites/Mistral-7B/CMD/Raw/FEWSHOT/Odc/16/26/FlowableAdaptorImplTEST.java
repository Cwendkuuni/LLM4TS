package com.oceanbase.odc.service.flow.test;

import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.FlowableElementType;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor.BaseFlowNodeInstance;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private FlowableAdaptorImpl flowableAdaptorImpl;
    private TaskService taskServiceMock;
    private FormService formServiceMock;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceRepositoryMock;

    @Autowired
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;

    @Before
    public void setup() {
        taskServiceMock = Mockito.mock(TaskService.class);
        formServiceMock = Mockito.mock(FormService.class);
        flowableAdaptorImpl = new FlowableAdaptorImpl();
        flowableAdaptorImpl.setTaskService(taskServiceMock);
        flowableAdaptorImpl.setFormService(formServiceMock);
        flowableAdaptor = flowableAdaptorImpl;
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "proc_inst_id";

        Assert.assertTrue(flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId).isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullFlowInstanceId() {
        String processInstanceId = "proc_inst_id";
        flowableAdaptor.setProcessInstanceId(null, processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNullProcessInstanceId() {
        Long flowInstanceId = 1L;
        flowableAdaptor.setProcessInstanceId(flowInstanceId, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "proc_inst_id";
        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceRepositoryMock.findByProcessInstanceId(processInstanceId)).thenReturn(List.of(
                new NodeInstanceEntity() {{
                    setId(flowInstanceId);
                }}));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceIdEmptyResult() {
        String processInstanceId = "proc_inst_id";

        Mockito.when(nodeInstanceRepositoryMock.findByProcessInstanceId(processInstanceId)).thenReturn(Collections.emptyList());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "process_def_id";

        Assert.assertTrue(flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId).isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessDefinitionIdWithNullFlowInstanceId() {
        String processDefinitionId = "process_def_id";
        flowableAdaptor.setProcessDefinitionId(null, processDefinitionId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessDefinitionIdWithNullProcessDefinitionId() {
        Long flowInstanceId = 1L;
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "process_def_id";
        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceRepositoryMock.findByProcessDefinitionId(processDefinitionId)).thenReturn(List.of(
                new NodeInstanceEntity() {{
                    setId(flowInstanceId);
                }}));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionIdEmptyResult() {
        String processDefinitionId = "process_def_id";

        Mockito.when(nodeInstanceRepositoryMock.findByProcessDefinitionId(processDefinitionId)).thenReturn(Collections.emptyList());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process_inst_id";

        Mockito.when(nodeInstanceRepositoryMock.findByInstanceId(flowInstanceId)).thenReturn(List.of(
                new NodeInstanceEntity() {{
                    setProcessInstanceId(processInstanceId);
                }}));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceIdEmptyResult() {
        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceRepositoryMock.findByInstanceId(flowInstanceId)).thenReturn(Collections.emptyList());

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENCE_FLOW;

        Mockito.when(nodeInstanceRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType)).thenReturn(List.of(
                new NodeInstanceEntity() {{
                    setActivityId("activity_id_1");
                    setName("name_1");
                }}));

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        Assert.assertEquals(1, result.size());
        FlowableElement flowableElement = result.get(0);
        Assert.assertEquals("activity_id_1", flowableElement.getActivityId());
        Assert.assertEquals("name_1", flowableElement.getName());
        Assert.assertEquals(flowableElementType, flowableElement.getType());
    }

    @Test
    public void testGetFlowableElementByTypeEmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENCE_FLOW;

        Mockito.when(nodeInstanceRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType)).thenReturn(Collections.emptyList());

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "task_activity_id";
        Long flowInstanceId = 1L;
        FlowTaskInstance expectedFlowTaskInstance = new FlowTaskInstance(
                new ServiceTaskInstanceEntity(), new OdcRuntimeDelegateMapper(), taskServiceMock, formServiceMock,
                eventPublisher, taskServiceMock, nodeRepositoryMock, sequenceRepositoryMock,
                serviceTaskInstanceRepositoryMock);

        Mockito.when(serviceTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(expectedFlowTaskInstance, result.get());
    }

    @Test
    public void testGetTaskInstanceByActivityIdEmptyResult() {
        String activityId = "task_activity_id";
        Long flowInstanceId = 1L;

        Mockito.when(serviceTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.empty());

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "approval_activity_id";
        Long flowInstanceId = 1L;
        FlowApprovalInstance expectedFlowApprovalInstance = new FlowApprovalInstance(
                new UserTaskInstanceEntity(), flowServiceMock, taskServiceMock, formServiceMock, eventPublisher,
                authenticationFacadeMock, nodeRepositoryMock, sequenceRepositoryMock,
                userTaskInstanceRepositoryMock);

        Mockito.when(userTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(expectedFlowApprovalInstance, result.get());
    }

    @Test
    public void testGetApprovalInstanceByActivityIdEmptyResult() {
        String activityId = "approval_activity_id";
        Long flowInstanceId = 1L;

        Mockito.when(userTaskInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
                activityId, flowInstanceId)).thenReturn(Optional.empty());

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "approval_name";
        Long flowInstanceId = 1L;
        FlowApprovalInstance expectedFlowApprovalInstance = new FlowApprovalInstance(
                new UserTaskInstanceEntity(), flowServiceMock, taskServiceMock, formServiceMock, eventPublisher,
                authenticationFacadeMock, nodeRepositoryMock, sequenceRepositoryMock,
                userTaskInstanceRepositoryMock);

        Mockito.when(userTaskInstanceRepositoryMock.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(expectedFlowApprovalInstance, result.get());
    }

    @Test
    public void testGetApprovalInstanceByNameEmptyResult() {
        String name = "approval_name";
        Long flowInstanceId = 1L;

        Mockito.when(userTaskInstanceRepositoryMock.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.empty());

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "gateway_activity_id";
        Long flowInstanceId = 1L;
        FlowGatewayInstance expectedFlowGatewayInstance = new FlowGatewayInstance(
                new GateWayInstanceEntity(), flowServiceMock, nodeRepositoryMock, sequenceRepositoryMock,
                gateWayInstanceRepositoryMock);

        Mockito.when(gateWayInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(new GateWayInstanceEntity()));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(expectedFlowGatewayInstance, result.get());
    }

    @Test
    public void testGetGatewayInstanceByActivityIdEmptyResult() {
        String activityId = "gateway_activity_id";
        Long flowInstanceId = 1L;

        Mockito.when(gateWayInstanceRepositoryMock.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.empty());

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        Long instanceId = 1L;
        BaseFlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("activity_id", "name", FlowableElementType.SEQUENCE_FLOW);

        Mockito.when(nodeInstanceRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                nodeInstance.getNodeType(), flowableElement.getType()))
                .thenReturn(List.of(new NodeInstanceEntity()));

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeInstanceRepositoryMock).save(Mockito.any(NodeInstanceEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFlowableElementWithNullNodeInstance() {
        FlowableElement flowableElement = new FlowableElement("activity_id", "name", FlowableElementType.SEQUENCE_FLOW);
        flowableAdaptor.setFlowableElement(null, flowableElement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFlowableElementWithNullFlowableElement() {
        BaseFlowNodeInstance nodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        flowableAdaptor.setFlowableElement(nodeInstance, null);
    }
}