import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

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
        // Typical use case
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Edge case
        Long flowInstanceIdNotExist = 100L;
        String processInstanceIdNotExist = "54321";
        flowableAdaptor.setProcessInstanceId(flowInstanceIdNotExist, processInstanceIdNotExist);

        // Error scenario
        Long flowInstanceIdNull = null;
        String processInstanceIdNull = null;
        flowableAdaptor.setProcessInstanceId(flowInstanceIdNull, processInstanceIdNull);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Typical use case
        String processInstanceId = "12345";
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(flowInstanceId.isPresent());

        // Edge case
        String processInstanceIdNotExist = "54321";
        Optional<Long> flowInstanceIdNotExist = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceIdNotExist);
        assertFalse(flowInstanceIdNotExist.isPresent());

        // Error scenario
        String processInstanceIdNull = null;
        Optional<Long> flowInstanceIdNull = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceIdNull);
        assertFalse(flowInstanceIdNull.isPresent());
    }

    // Add test cases for the remaining methods

}