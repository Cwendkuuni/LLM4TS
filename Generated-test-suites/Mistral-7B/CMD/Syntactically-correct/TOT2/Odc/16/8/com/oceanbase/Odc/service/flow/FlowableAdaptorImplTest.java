package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowableElementTestBuilder;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.instance.FlowableAdaptorTestUtil;

/**
 * Test class for {@link FlowableAdaptorImpl}
 *
 * @author yh263208
 * @date 2022-02-17 20:16
 */
public class FlowableAdaptorImplTest {

    private FlowableAdaptor adaptor;
    private FlowableAdaptorImpl impl;

    @Before
    public void setup() {
        impl = new FlowableAdaptorImpl();
        adaptor = impl;
    }

    // Test for setProcessInstanceId method
    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";
        int affectRows = 1;

        Mockito.doReturn(1).when(impl.flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId,
                processInstanceId);

        adaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(impl.flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
        Mockito.verify(impl.log).info(
                "Binding Flow/Process Instance Id exception, flowInstanceId=" + flowInstanceId + ", processInstanceId="
                        + processInstanceId + ", affectRows=" + affectRows);
        Mockito.verifyNoMoreInteractions(impl.flowInstanceRepository);
    }

    // Test for getFlowInstanceIdByProcessInstanceId method
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";
        Long flowInstanceId = 1L;

        Mockito.when(impl.flowInstanceRepository.findAll(
                Mockito.argThat(specification -> specification.is(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))))
                .thenReturn(FlowableAdaptorTestUtil.getFlowInstanceEntities());

        Optional<Long> result = adaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertEquals(flowInstanceId, result.get());
    }

    // Test for setProcessDefinitionId method
    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";
        int affectRows = 1;

        Mockito.doReturn(1).when(impl.flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId,
                processDefinitionId);

        adaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Mockito.verify(impl.flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
        Mockito.verify(impl.log).info(
                "Binding Flow/Process Instance Id exception, flowInstanceId=" + flowInstanceId + ", processDefinitionId="
                        + processDefinitionId + ", affectRows=" + affectRows);
        Mockito.verifyNoMoreInteractions(impl.flowInstanceRepository);
    }

    // Test for getFlowInstanceIdByProcessDefinitionId method
    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";
        Long flowInstanceId = 1L;

        Mockito.when(impl.flowInstanceRepository.findAll(
                Mockito.argThat(specification -> specification.is(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))))
                .thenReturn(FlowableAdaptorTestUtil.getFlowInstanceEntities());

        Optional<Long> result = adaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertEquals(flowInstanceId, result.get());
    }

    // Test for getProcessInstanceIdByFlowInstanceId method
    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        Mockito.when(impl.flowInstanceRepository.findById(flowInstanceId))
                .thenReturn(Optional.of(FlowableAdaptorTestUtil.getFlowInstanceEntity()));

        Optional<String> result = adaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertEquals(processInstanceId, result.get());
    }

    // Test for getFlowableElementByType method
    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<FlowableElement> result = adaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        FlowableElement flowableElement = result.get(0);
        assertEquals(instanceId, flowableElement.getId());
        assertEquals(instanceType, flowableElement.getType());
        assertEquals(FlowableElementType.START_EVENT, flowableElement.getType());
    }

    // Test for getTaskInstanceByActivityId method
    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Long taskInstanceId = 1L;

        Mockito.when(impl.serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId))
                .thenReturn(Optional.of(FlowableAdaptorTestUtil.getServiceTaskInstanceEntity()));

        Optional<FlowTaskInstance> result = adaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(taskInstanceId, result.get().getId());
        assertEquals(activityId, result.get().getActivityId());
    }

    // Test for getApprovalInstanceByActivityId method
    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Long approvalInstanceId = 1L;

        Mockito.when(impl.userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId))
                .thenReturn(Optional.of(FlowableAdaptorTestUtil.getUserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(approvalInstanceId, result.get().getId());
        assertEquals(activityId, result.get().getActivityId());
    }

    // Test for getApprovalInstanceByName method
    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;
        Long approvalInstanceId = 1L;

        Mockito.when(impl.userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
                .thenReturn(Optional.of(FlowableAdaptorTestUtil.getUserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> result = adaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertEquals(approvalInstanceId, result.get().getId());
        assertEquals(name, result.get().getName());
    }

    // Test for getGatewayInstanceByActivityId method
    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;
        Long gatewayInstanceId = 1L;

        Mockito.when(impl.gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId))
                .thenReturn(Optional.of(FlowableAdaptorTestUtil.getGateWayInstanceEntity()));

        Optional<FlowGatewayInstance> result = adaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertEquals(gatewayInstanceId, result.get().getId());
        assertEquals(activityId, result.get().getActivityId());
    }

    // Test for setFlowableElement method
    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new FlowableElementTestBuilder().nodeType(FlowNodeType.USER_TASK)
                .activityId("activityId").build();
        FlowableElement flowableElement = new FlowableElementTestBuilder().type(FlowableElementType.START_EVENT)
                .activityId("activityId").name("name").build();

        Mockito.when(impl.nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), flowableElement.getType()))
                .thenReturn(Collections.emptyList());

        adaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(impl.nodeInstanceRepository).save(Mockito.argThat(entity -> {
            assertEquals(nodeInstance.getId(), entity.getInstanceId());
            assertEquals(nodeInstance.getNodeType(), entity.getInstanceType());
            assertEquals(flowableElement.getType(), entity.getFlowableElementType());
            assertEquals(flowableElement.getActivityId(), entity.getActivityId());
            assertEquals(flowableElement.getName(), entity.getName());
            return true;
        }));
    }
}