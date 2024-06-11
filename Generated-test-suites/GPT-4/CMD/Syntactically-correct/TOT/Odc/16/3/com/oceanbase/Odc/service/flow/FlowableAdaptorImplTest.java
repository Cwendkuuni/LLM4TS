package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowableAdaptorImpl flowableAdaptor;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        doNothing().when(flowableAdaptor).setProcessInstanceId(anyLong(), anyString());
        flowableAdaptor.setProcessInstanceId(1L, "Testing");
        verify(flowableAdaptor, times(1)).setProcessInstanceId(1L, "Testing");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessInstanceId(anyString())).thenReturn(Optional.of(1L));
        assertEquals(Optional.of(1L), flowableAdaptor.getFlowInstanceIdByProcessInstanceId("Testing"));
    }

    // ... other methods test

    @Test(expected = Exception.class)
    public void testSetFlowableElement() {
        // Arrange
        BaseFlowNodeInstance mockNode = mock(BaseFlowNodeInstance.class);
        FlowableElement mockElement = mock(FlowableElement.class);

        // Act
        flowableAdaptor.setFlowableElement(mockNode, mockElement);

        // Assert
        verify(mockNode, times(1)).getId();
        verify(mockElement, times(1)).getType();
    }
}