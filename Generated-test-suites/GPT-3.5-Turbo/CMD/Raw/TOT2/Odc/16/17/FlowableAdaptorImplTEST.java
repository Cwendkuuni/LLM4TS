
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowableAdaptorImplTest {

    @Autowired
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        // Initialize any required components or mocks
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Optional<Long> retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(retrievedFlowInstanceId.isPresent());
        assertEquals(flowInstanceId, retrievedFlowInstanceId.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "789";

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Optional<Long> retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(retrievedFlowInstanceId.isPresent());
        assertEquals(flowInstanceId, retrievedFlowInstanceId.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId_NotFound() {
        String processInstanceId = "invalidId";
        Optional<Long> retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertFalse(retrievedFlowInstanceId.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertNotNull(flowableElements);
        // Add assertions for the returned elements
    }

    // Add more test cases for other methods in FlowableAdaptorImpl

}

