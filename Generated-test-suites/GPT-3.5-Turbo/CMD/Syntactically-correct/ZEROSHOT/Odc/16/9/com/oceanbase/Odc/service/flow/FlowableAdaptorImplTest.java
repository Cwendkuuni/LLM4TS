import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;
    @Mock
    private FlowInstanceRepository flowInstanceRepositoryMock;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepositoryMock;

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceEntityRepositoryMock);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepositoryMock);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepositoryMock);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepositoryMock);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepositoryMock);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "P12345";
        Long flowInstanceId = 1L;

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);

        Mockito.when(flowInstanceRepositoryMock.findAll(Mockito.any())).thenReturn(List.of(entity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "PD12345";
        Long flowInstanceId = 1L;

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setId(flowInstanceId);

        Mockito.when(flowInstanceRepositoryMock.findAll(Mockito.any())).thenReturn(List.of(entity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "P12345";

        FlowInstanceEntity entity = new FlowInstanceEntity();
        entity.setProcessInstanceId(processInstanceId);

        Mockito.when(flowInstanceRepositoryMock.findById(flowInstanceId)).thenReturn(Optional.of(entity));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(processInstanceId, result.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType elementType = FlowableElementType.TASK;

        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setActivityId("A12345");
        entity.setName("Task1");
        entity.setFlowableElementType(elementType);

        Mockito.when(nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, elementType))
                .thenReturn(List.of(entity));

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType, elementType);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        FlowableElement flowableElement = result.get(0);
        assertEquals(entity.getActivityId(), flowableElement.getActivityId());
        assertEquals(entity.getName(), flowableElement.getName());
        assertEquals(entity.getFlowableElementType(), flowableElement.getType());
    }

    // Add more test methods for other functionalities

}