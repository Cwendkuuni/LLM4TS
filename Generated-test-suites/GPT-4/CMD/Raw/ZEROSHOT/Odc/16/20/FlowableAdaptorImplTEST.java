

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptorImpl.setProcessInstanceId(1L, "1");
        
        verify(flowInstanceRepository).updateProcessInstanceIdById(Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        when(flowInstanceRepository.findAll(Mockito.any())).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId("1");

        assertEquals(Optional.of(0L), result);
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptorImpl.setProcessDefinitionId(1L, "1");
        
        verify(flowInstanceRepository).updateProcessDefinitionIdById(Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        when(flowInstanceRepository.findAll(Mockito.any())).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        
        Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId("1");
        
        assertEquals(Optional.of(0L), result);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        when(flowInstanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
        
        Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(1L);
        
        assertEquals(Optional.empty(), result);
    }

    // Add similar unit tests for the other remaining methods in the FlowableAdaptorImpl class
}

