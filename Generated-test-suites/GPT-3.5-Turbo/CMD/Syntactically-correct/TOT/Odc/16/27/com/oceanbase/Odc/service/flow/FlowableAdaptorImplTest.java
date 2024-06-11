import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "123456";
        
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        verify(flowInstanceRepository, times(1)).findById(flowInstanceId);
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
        
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "123456";
        
        when(flowInstanceRepository.findAll(any())).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertTrue(result.isPresent());
        assertEquals(new Long(1L), result.get());
        
        when(flowInstanceRepository.findAll(any())).thenReturn(Collections.emptyList());
        
        result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertFalse(result.isPresent());
    }
    
    // Add more test cases for other methods
    
}