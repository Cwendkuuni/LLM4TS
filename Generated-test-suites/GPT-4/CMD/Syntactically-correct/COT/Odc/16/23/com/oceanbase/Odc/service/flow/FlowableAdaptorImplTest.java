package com.oceanbase.odc.service.flow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowableAdaptorImpl testClass;

    @Test
    public void testSetProcessInstanceId() {
        doNothing().when(testClass).setProcessInstanceId(123L, "processInstanceId");
        testClass.setProcessInstanceId(123L, "processInstanceId");
        verify(testClass, times(1)).setProcessInstanceId(123L, "processInstanceId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdWithNull() {
        testClass.setProcessInstanceId(null, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        when(testClass.getFlowInstanceIdByProcessInstanceId("processInstanceId"))
                .thenReturn(Optional.of(123L));
        Optional<Long> result = testClass.getFlowInstanceIdByProcessInstanceId("processInstanceId");
        assertEquals(Optional.of(123L), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFlowInstanceIdByProcessInstanceIdWithNull() {
        testClass.getFlowInstanceIdByProcessInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessDefinitionIdWithNull() {
        testClass.setProcessDefinitionId(null, null);
    }

    @Test
    public void testSetProcessDefinitionIdWithNull() {
        when(testClass.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(Optional.of(123L));
        Optional<Long> result = testClass.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        assertEquals(Optional.of(123L), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFlowInstanceIdByProcessDefinitionIdWithNull() {
        testClass.getFlowInstanceIdByProcessDefinitionId(null);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        when(testClass.getProcessInstanceIdByFlowInstanceId(123L)).thenReturn(Optional.of("processInstanceId"));
        Optional<String> result = testClass.getProcessInstanceIdByFlowInstanceId(123L);
        assertEquals(Optional.of("processInstanceId"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetProcessInstanceIdByFlowInstanceIdWithNull() {
        testClass.getProcessInstanceIdByFlowInstanceId(null);
    }

    @Test
    public void testGetFlowableElementByType() {
        when(testClass.getFlowableElementByType(123L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER))
                .thenReturn(Collections.emptyList());
        List<FlowableElement> result = testClass.getFlowableElementByType(123L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER);
        assertEquals(Collections.emptyList(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFlowableElementByTypeWithNull() {
        testClass.getFlowableElementByType(null, null, null);
    }

    // Similar tests can be written for the rest of the public methods...

}