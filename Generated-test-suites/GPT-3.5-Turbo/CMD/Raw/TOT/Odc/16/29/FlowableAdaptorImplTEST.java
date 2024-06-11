import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Before
    public void setUp() {
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "def-123";
        List<FlowInstanceEntity> entityList = new ArrayList<>();
        entityList.add(new FlowInstanceEntity());
        
        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))).thenReturn(entityList);
        
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertEquals(Optional.of(1L), result);
    }

    // Additional test cases for other methods...

}