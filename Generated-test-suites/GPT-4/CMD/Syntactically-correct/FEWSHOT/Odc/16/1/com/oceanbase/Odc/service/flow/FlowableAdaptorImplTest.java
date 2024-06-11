
package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;


import java.util.Optional;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowableAdaptorImplTest {
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = mock(FlowableAdaptorImpl.class);
    }

    @Test
    public void testSetProcessInstanceId() {
        // Setup your mock expectations and execution results
        flowableAdaptor.setProcessInstanceId(1L, "processInstanceId");

        // Assertions and verifications
        // Assuming processInstanceId is stored in a map with flowInstanceId as key
        assertEquals("processInstanceId", flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Assuming processInstanceId for FlowInstanceId 1L is "processInstanceId"
        when(flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId")).thenReturn(Optional.of(1L));

        // Assertions and verifications
        assertTrue(flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId").isPresent());
        assertEquals(Optional.of(1L), flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId"));
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Setup your mock expectations and execution results
        flowableAdaptor.setProcessDefinitionId(1L, "processDefinitionId");

        // Assertions and verifications
        // Assuming processDefinitionId is stored in a map with flowInstanceId as key
        assertEquals("processDefinitionId", flowableAdaptor.getProcessDefinitionIdByFlowInstanceId(1L));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // Assuming processDefinitionId for FlowInstanceId 1L is "processDefinitionId"
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));

        // Assertions and verifications
        assertTrue(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId").isPresent());
        assertEquals(Optional.of(1L), flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"));
    }

    // More tests to cover other methods ...

}
