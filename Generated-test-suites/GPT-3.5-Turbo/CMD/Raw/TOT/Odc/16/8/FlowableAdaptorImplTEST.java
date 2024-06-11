import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setup() {
        flowableAdaptor = new FlowableAdaptorImpl();
        // Mocking dependencies
        flowableAdaptor.flowInstanceRepository = mock(FlowInstanceRepository.class);
        flowableAdaptor.nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        flowableAdaptor.serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        flowableAdaptor.userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        flowableAdaptor.gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
        flowableAdaptor.eventPublisher = mock(EventPublisher.class);
        flowableAdaptor.nodeRepository = mock(NodeInstanceEntityRepository.class);
        flowableAdaptor.sequenceRepository = mock(SequenceInstanceRepository.class);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";

        when(flowableAdaptor.flowInstanceRepository.findById(flowInstanceId))
                .thenReturn(Optional.of(new FlowInstanceEntity(flowInstanceId, processInstanceId)));
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowableAdaptor.flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }
    
    // Add test cases for other methods as required

}