import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {
  
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.flowInstanceRepository = mock(FlowInstanceRepository.class);
        flowableAdaptor.nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        flowableAdaptor.serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        flowableAdaptor.userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        flowableAdaptor.gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        when(flowableAdaptor.flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(processInstanceId, flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "12345";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        when(flowableAdaptor.flowInstanceRepository.findAll(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId))).thenReturn(List.of(entity));
        assertEquals(1L, flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId).get().longValue());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "def123";
        when(flowableAdaptor.flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        assertEquals(processDefinitionId, flowableAdaptor.getProcessDefinitionIdByFlowInstanceId(flowInstanceId).get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "def123";
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(1L);
        when(flowableAdaptor.flowInstanceRepository.findAll(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId))).thenReturn(List.of(entity));
        assertEquals(1L, flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId).get().longValue());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId("12345");
        when(flowableAdaptor.flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(entity));
        assertEquals("12345", flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId).get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setActivityId("activity123");
        entity.setName("Task1");
        entity.setFlowableElementType(FlowableElementType.TASK);
        when(flowableAdaptor.nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(List.of(entity));
        List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertFalse(flowableElements.isEmpty());
        assertEquals(1, flowableElements.size());
        assertEquals("activity123", flowableElements.get(0).getActivityId());
        assertEquals("Task1", flowableElements.get(0).getName());
        assertEquals(FlowableElementType.TASK, flowableElements.get(0).getType());
    }

    // Additional test cases for other methods...

}