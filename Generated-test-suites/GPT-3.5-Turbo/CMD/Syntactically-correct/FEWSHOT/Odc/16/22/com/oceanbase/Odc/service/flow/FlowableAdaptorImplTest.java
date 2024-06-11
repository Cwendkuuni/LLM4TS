//Import Statements
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

//Test Class
public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor = new FlowableAdaptorImpl();

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptor.setProcessInstanceId(1L, "12345");
        assertEquals(Optional.of("12345"), flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L));
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        assertEquals(Optional.of(1L), flowableAdaptor.getFlowInstanceIdByProcessInstanceId("12345"));
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptor.setProcessDefinitionId(1L, "DEF_ID_123");
        assertEquals(Optional.of("DEF_ID_123"), flowableAdaptor.getProcessDefinitionIdByFlowInstanceId(1L));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        assertEquals(Optional.of(1L), flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("DEF_ID_123"));
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        assertNotNull(flowableAdaptor.getTaskInstanceByActivityId("task_id_1", 1L));
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        assertNotNull(flowableAdaptor.getApprovalInstanceByActivityId("approval_1", 1L));
    }

    @Test
    public void testGetApprovalInstanceByName() {
        assertNotNull(flowableAdaptor.getApprovalInstanceByName("Approval Task", 1L));
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        assertNotNull(flowableAdaptor.getGatewayInstanceByActivityId("gateway_1", 1L));
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        nodeInstance.setId(1L);
        nodeInstance.setNodeType(FlowNodeType.SERVICE_TASK);
        nodeInstance.setFlowInstanceId(1L);
        FlowableElement flowableElement = new FlowableElement("activity_id_1", "Task 1", FlowableElementType.TASK);
        
        assertEquals(0, flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.TASK).size());
        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
        assertEquals(1, flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.TASK).size());
    }

}