import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        // Typical use case
        Long flowInstanceId = 1L;
        String processInstanceId = "1234";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(processInstanceId, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
        
        // Edge case: Existing flowInstanceId, null processInstanceId
        flowableAdaptor.setProcessInstanceId(flowInstanceId, null);
        assertFalse(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).isPresent());
        
        // Error scenario: Invalid flowInstanceId
        Long invalidFlowInstanceId = 999L;
        assertThrows(Exception.class, () -> flowableAdaptor.setProcessInstanceId(invalidFlowInstanceId, processInstanceId));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Typical use case
        Long flowInstanceId = 1L;
        String processInstanceId = "1234";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(flowInstanceId, flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId).get());
        
        // Edge case: Non-existing processInstanceId
        String nonExistingProcessInstanceId = "9999";
        assertFalse(flowableAdaptor.getFlowInstanceIdByProcessInstanceId(nonExistingProcessInstanceId).isPresent());
    }

    // Additional test cases for the remaining methods can be added here

}