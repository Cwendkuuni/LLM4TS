import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
       
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "12345";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertEquals(Long.valueOf(1L), flowInstanceId.get());
    }

    // Add more test cases for other methods in FlowableAdaptorImpl

}