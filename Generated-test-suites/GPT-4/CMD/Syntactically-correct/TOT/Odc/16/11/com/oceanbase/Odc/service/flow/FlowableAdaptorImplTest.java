package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {
    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Test
    public void setProcessInstanceId_FlowInstanceExists_UpdateSucceeds() {
        // Given
        Long flowInstanceId = 1L;
        String processInstanceId = "process1";
        FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        // When
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Then
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);        
    }

    @Test(expected = Exception.class) // replace Exception with the specific exception that the method throws in this scenario
    public void setProcessInstanceId_FlowInstanceDoesNotExist_ExceptionThrown() {
        // Given
        Long flowInstanceId = 1L;
        String processInstanceId = "process1";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());

        // When
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Then 
        // Expect exception
    }

    @Test(expected = Exception.class) // replace Exception with the specific exception that the method throws in this scenario 
    public void setProcessInstanceId_NoRowAffected_ExceptionThrown() {
        // Given
        Long flowInstanceId = 1L;
        String processInstanceId = "process1";
        FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(0);

        // When
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Then
        // Expect exception
    }
}