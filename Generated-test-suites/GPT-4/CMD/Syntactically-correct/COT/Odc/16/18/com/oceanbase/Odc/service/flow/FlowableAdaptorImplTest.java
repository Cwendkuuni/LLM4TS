package com.oceanbase.odc.service.flow;
import junit.framework.TestCase;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;
import java.util.Optional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest extends TestCase {

    @Mock
    FlowableAdaptorImpl objectUnderTest;

    public void testSetProcessInstanceId() {
    when(objectUnderTest.setProcessInstanceId(any(Long.class), any(String.class))).thenReturn(null);
    assertNotNull(objectUnderTest.setProcessInstanceId(1L, "processInstanceId"));
    }

    public void testGetFlowInstanceIdByProcessInstanceId() {
    when(objectUnderTest.getFlowInstanceIdByProcessInstanceId(any(String.class))).thenReturn(Optional.of(1L));
    assertEquals(Optional.of(1L), objectUnderTest.getFlowInstanceIdByProcessInstanceId("processInstanceId"));
    }

    public void testSetProcessDefinitionId() {
    when(objectUnderTest.setProcessDefinitionId(any(Long.class), any(String.class))).thenReturn(null);
    assertNotNull(objectUnderTest.setProcessDefinitionId(1L, "processDefinitionId"));
    }

    public void testGetFlowInstanceIdByProcessDefinitionId() {
    when(objectUnderTest.getFlowInstanceIdByProcessDefinitionId(any(String.class))).thenReturn(Optional.of(1L));
    assertEquals(Optional.of(1L), objectUnderTest.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"));
    }

    public void testGetProcessInstanceIdByFlowInstanceId() {
    when(objectUnderTest.getProcessInstanceIdByFlowInstanceId(any(Long.class))).thenReturn(Optional.of("processInstanceId"));
    assertEquals(Optional.of("processInstanceId"), objectUnderTest.getProcessInstanceIdByFlowInstanceId(1L));
    }

    public void testGetFlowableElementByType() {
    when(objectUnderTest.getFlowableElementByType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
            .thenReturn(Collections.emptyList());
    assertTrue(objectUnderTest.getFlowableElementByType(1L, FlowNodeType.APPROVAL_TASK, FlowableElementType.APPROVAL).isEmpty());
    }

    public void testGetTaskInstanceByActivityId() {
    when(objectUnderTest.getTaskInstanceByActivityId(any(String.class), any(Long.class))).thenReturn(Optional.empty());
    assertFalse(objectUnderTest.getTaskInstanceByActivityId("activityId", 1L).isPresent());
    }

    public void testGetApprovalInstanceByActivityId() {
    when(objectUnderTest.getApprovalInstanceByActivityId(any(String.class), any(Long.class))).thenReturn(Optional.empty());
    assertFalse(objectUnderTest.getApprovalInstanceByActivityId("activityId", 1L).isPresent());
    }

    public void testGetApprovalInstanceByName() {
    when(objectUnderTest.getApprovalInstanceByName(any(String.class), any(Long.class))).thenReturn(Optional.empty());
    assertFalse(objectUnderTest.getApprovalInstanceByName("name", 1L).isPresent());
    }

    public void testGetGatewayInstanceByActivityId() {
    when(objectUnderTest.getGatewayInstanceByActivityId(any(String.class), any(Long.class))).thenReturn(Optional.empty());
    assertFalse(objectUnderTest.getGatewayInstanceByActivityId("activityId", 1L).isPresent());
    }

    public void testSetFlowableElement() {
    when(objectUnderTest.setFlowableElement(any(BaseFlowNodeInstance.class), any(FlowableElement.class))).thenReturn(null);
    assertNotNull(objectUnderTest.setFlowableElement(new BaseFlowNodeInstance(), new FlowableElement()));
    }
}