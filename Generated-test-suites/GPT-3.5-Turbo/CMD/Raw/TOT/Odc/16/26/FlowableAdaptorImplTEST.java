import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        // Typical Use Case
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        // Edge Case
        flowInstanceId = 2L;
        processInstanceId = "67890";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        // Error Scenario
        Long nonExistingFlowInstanceId = 3L;
        String nonExistingProcessInstanceId = "abcde";
        assertThrows(Exception.class, () -> {
            flowableAdaptor.setProcessInstanceId(nonExistingFlowInstanceId, nonExistingProcessInstanceId);
        });

    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Typical Use Case
        String processInstanceId = "12345";
        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(flowInstanceId.isPresent());
        
        // Edge Case
        processInstanceId = "67890";
        flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(flowInstanceId.isPresent());
        
        // Error Scenario
        String nonExistingProcessInstanceId = "abcde";
        flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(nonExistingProcessInstanceId);
        assertTrue(flowInstanceId.isEmpty());
    }

    // Add test cases for all other methods in FlowableAdaptorImpl

}