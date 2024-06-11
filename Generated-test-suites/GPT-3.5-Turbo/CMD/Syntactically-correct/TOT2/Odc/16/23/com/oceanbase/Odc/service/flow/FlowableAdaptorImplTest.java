import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId_withValidInput() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertEquals(processInstanceId, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
    }

    @Test
    public void testSetProcessInstanceId_withNonExistingFlowInstanceId() {
        Long flowInstanceId = 999L;
        String processInstanceId = "999999";

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertFalse(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).isPresent());
    }

    // Add more test cases for other methods in the FlowableAdaptorImpl class

}