
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    // Preparing the data
    @Before
    public void setUp() {
        // TO-DO: Instantiate data samples
        // Ex. this.sampleNode = new Node("Sample");
    }

    @Test
    public void testSetProcessInstanceId() {
        // TO-DO: Add test case contents
        // The test way is verify whether method called expected times
        // verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(any(Long.class));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // TO-DO: Add test case contents
        // The test way is use Mockito.when() to simulate specified behavior of mocked methods
        // and then validate the returned value by assertEquals().
    }

    @Test
    public void testSetProcessDefinitionId() {
        // TO-DO: Add test case contents
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // TO-DO: Add test case contents
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // TO-DO: Add test case contents
    }

    @Test
    public void testGetFlowableElementByType() {
        // TO-DO: Add test case contents
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        // TO-DO: Add test case contents
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        // TO-DO: Add test case contents
    }

    @Test
    public void testGetApprovalInstanceByName() {
        // TO-DO: Add test case contents
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        // TO-DO: Add test case contents
    }

    @Test
    public void testSetFlowableElement() {
        // TO-DO: Add test case contents
    }

    @Test
    public void testInnerConvert() {
        // TO-DO: Add test case contents
    }
}
