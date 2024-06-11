import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;
    
    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process123";
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        // Verify that the update method is called
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }
    
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "process123";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(new ArrayList<FlowInstanceEntity>());
        
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertEquals(Optional.empty(), flowInstanceId);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "def123";
        
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        
        // Verify that the update method is called
        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "def123";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(new ArrayList<FlowInstanceEntity>());
        
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        
        assertEquals(Optional.empty(), flowInstanceId);
    }

    // More test methods can be added here for other methods in the FlowableAdaptorImpl class

}