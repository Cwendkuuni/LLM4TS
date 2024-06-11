package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class FlowableAdaptorImplTest {
    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Mock
    private FlowInstanceEntity flowInstanceEntity;
    @Mock
    private TaskService taskService;
    @Mock
    private FormService formService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptor.setProcessInstanceId(1L, "processInstanceId");
        assertEquals("processInstanceId", flowInstanceEntity.getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        when(flowInstanceEntity.getProcessInstanceId()).thenReturn("processInstanceId");
        Optional<Long> optional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId");
        assertEquals(Optional.empty(), optional);
    }

    void testSetProcessDefinitionId() {
        flowableAdaptor.setProcessDefinitionId(1L, "processDefinitionId");
        assertEquals("processDefinitionId", flowInstanceEntity.getProcessDefinitionId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        when(flowInstanceEntity.getProcessDefinitionId()).thenReturn("processDefinitionId");
        Optional<Long> optional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertEquals(Optional.empty(), optional);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        when(flowInstanceEntity.getId()).thenReturn(1L);
        when(flowInstanceEntity.getProcessInstanceId()).thenReturn("processInstanceId");
        Optional<String> optional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L);
        assertEquals(Optional.empty(), optional);
    }

    @Test
    public void testGetFlowableElementByType() {
        flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.APPROVAL_TASK, FlowableElementType.PROCESS);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        when(flowInstanceEntity.getId()).thenReturn(1L);
        Optional<FlowApprovalInstance> optional = flowableAdaptor.getApprovalInstanceByActivityId("activityId", 1L);
        assertEquals(Optional.empty(), optional);
    }

    @Test
    public void testGetApprovalInstanceByName() {
        when(flowInstanceEntity.getId()).thenReturn(1L);
        Optional<FlowApprovalInstance> optional = flowableAdaptor.getApprovalInstanceByName("name", 1L);
        assertEquals(Optional.empty(), optional);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        when(flowInstanceEntity.getId()).thenReturn(1L);
        Optional<FlowGatewayInstance> optional = flowableAdaptor.getGatewayInstanceByActivityId("activityId", 1L);
        assertEquals(Optional.empty(), optional);
    }

    @Test
    public void testSetFlowableElement() {
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.PROCESS);
        when(flowInstanceEntity.getId()).thenReturn(1L);
        when(flowInstanceEntity.getFlowInstanceId()).thenReturn(1L);
        flowableAdaptor.setFlowableElement(flowInstanceEntity, flowableElement);
    }
}
