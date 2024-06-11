package com.oceanbase.odc.service.flow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {
    
    @Autowired
    private FlowableAdaptorImpl flowableAdapterImpl;
    
    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // Arrange
        Long flowInstanceId = 1L;
        Optional<FlowInstanceEntity> optionalEntity =Optional.of(new FlowInstanceEntity());

        // Act
        doReturn(optionalEntity).when(flowInstanceRepository).findById(flowInstanceId);
        Optional<String> processInstanceIdActual = flowableAdapterImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        
        // Assert
        assertEquals(Optional.empty(), processInstanceIdActual);
    }

    @Test(expected = NullPointerException.class)
    public void testGetProcessInstanceIdByFlowInstanceId_NullFlowInstanceIdException(){
        // Act
        flowableAdapterImpl.getProcessInstanceIdByFlowInstanceId(null);
    }
}