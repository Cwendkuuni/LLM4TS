package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class FlowableAdaptorImplTest {

    @Test
    public void testSetProcessInstanceId() {
        FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();

        flowableAdaptor.setProcessInstanceId(flowInstanceEntity.getId(), "processInstanceId");

        assertEquals(flowInstanceEntity.getProcessInstanceId(), "processInstanceId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId");

        assertTrue(flowInstanceIdOptional.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();

        flowableAdaptor.setProcessDefinitionId(flowInstanceEntity.getId(), "processDefinitionId");

        assertEquals(flowInstanceEntity.getProcessDefinitionId(), "processDefinitionId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");

        assertTrue(flowInstanceIdOptional.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Optional<String> processInstanceIdOptional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L);

        assertTrue(processInstanceIdOptional.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.APPROVAL_TASK, 
                FlowableElementType.EVENT_LABEL_TYPE);

        assertEquals(flowableElements.size(), 1);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Optional<FlowTaskInstance> optionalFlowTaskInstance = flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L);

        assertTrue(optionalFlowTaskInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Optional<FlowApprovalInstance> optionalFlowApprovalInstance = flowableAdaptor.getApprovalInstanceByActivityId("activityId", 1L);

        assertTrue(optionalFlowApprovalInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Optional<FlowApprovalInstance> optionalFlowApprovalInstance = flowableAdaptor.getApprovalInstanceByName("name", 1L);

        assertTrue(optionalFlowApprovalInstance.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();
        Optional<FlowGatewayInstance> optionalFlowGatewayInstance = flowableAdaptor.getGatewayInstanceByActivityId("activityId", 1L);

        assertTrue(optionalFlowGatewayInstance.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = mock(FlowableElement.class);
        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        // Assert may be dependent on the implementation of `setFlowableElement`
    }

}