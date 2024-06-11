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
        // Test setting process instance id
        flowableAdaptor.setProcessInstanceId(1L, "12345");
        // Verify the process instance id is set
        assertEquals("12345", flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L).get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test getting flow instance id by process instance id
        assertEquals(1L, flowableAdaptor.getFlowInstanceIdByProcessInstanceId("12345").get().longValue());
        // Test case with multiple flow instance ids
        assertEquals(2L, flowableAdaptor.getFlowInstanceIdByProcessInstanceId("54321").get().longValue());
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Test setting process definition id
        flowableAdaptor.setProcessDefinitionId(1L, "def123");
        // Verify the process definition id is set
        assertEquals("def123", flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("def123").get());
    }

    // Add more test cases for other methods...

}