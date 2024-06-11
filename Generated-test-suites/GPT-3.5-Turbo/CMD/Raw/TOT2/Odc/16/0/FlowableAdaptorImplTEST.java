import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptor = new FlowableAdaptorImpl();

        // Test with valid flowInstanceId and processInstanceId
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Verify the set processInstanceId
        Optional<Long> retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(retrievedFlowInstanceId.isPresent());
        assertEquals(flowInstanceId, retrievedFlowInstanceId.get());

        // Test with invalid flowInstanceId
        Long invalidFlowInstanceId = 2L;
        flowableAdaptor.setProcessInstanceId(invalidFlowInstanceId, processInstanceId);

        // Verify the flowInstanceId not found
        Optional<Long> invalidFlowInstance = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("12345");
        assertFalse(invalidFlowInstance.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptor = new FlowableAdaptorImpl();

        // Test with valid flowInstanceId and processDefinitionId
        Long flowInstanceId = 1L;
        String processDefinitionId = "def123";
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        // Verify the set processDefinitionId
        Optional<Long> retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(retrievedFlowInstanceId.isPresent());
        assertEquals(flowInstanceId, retrievedFlowInstanceId.get());

        // Test with invalid flowInstanceId
        Long invalidFlowInstanceId = 2L;
        flowableAdaptor.setProcessDefinitionId(invalidFlowInstanceId, processDefinitionId);

        // Verify the flowInstanceId not found
        Optional<Long> invalidFlowInstance = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("def1234");
        assertFalse(invalidFlowInstance.isPresent());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        flowableAdaptor = new FlowableAdaptorImpl();

        // Test with valid processInstanceId
        String processInstanceId = "12345";
        Long flowInstanceId = 1L;
        Optional<Long> retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertFalse(retrievedFlowInstanceId.isPresent());

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Verify the flowInstanceId retrieved
        retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(retrievedFlowInstanceId.isPresent());
        assertEquals(flowInstanceId, retrievedFlowInstanceId.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        flowableAdaptor = new FlowableAdaptorImpl();

        // Test with valid processDefinitionId
        String processDefinitionId = "def123";
        Long flowInstanceId = 1L;
        Optional<Long> retrievedFlowInstanceId =
                flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertFalse(retrievedFlowInstanceId.isPresent());

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        // Verify the flowInstanceId retrieved
        retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(retrievedFlowInstanceId.isPresent());
        assertEquals(flowInstanceId, retrievedFlowInstanceId.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        flowableAdaptor = new FlowableAdaptorImpl();

        // Test with valid flowInstanceId
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        Optional<String> retrievedProcessInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertFalse(retrievedProcessInstanceId.isPresent());

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Verify the processInstanceId retrieved
        retrievedProcessInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(retrievedProcessInstanceId.isPresent());
        assertEquals(processInstanceId, retrievedProcessInstanceId.get());
    }

    // Additional test cases for other methods can be added here

}