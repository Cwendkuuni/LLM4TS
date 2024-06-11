import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        // Typical use case
        flowableAdaptor.setProcessInstanceId(1L, "12345");
        
        // Edge case
        flowableAdaptor.setProcessInstanceId(999L, "54321");
        
        // Error scenario
        assertThrows(IllegalArgumentException.class, () -> {
            flowableAdaptor.setProcessInstanceId(null, "67890");
        });
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Typical use case
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("12345");
        assertTrue(result.isPresent());
        
        // Edge case
        result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("99999");
        assertFalse(result.isPresent());
        
        // Error scenario
        assertThrows(IllegalArgumentException.class, () -> {
            flowableAdaptor.getFlowInstanceIdByProcessInstanceId(null);
        });
    }

    // Add test cases for each method in the FlowableAdaptorImpl class

}