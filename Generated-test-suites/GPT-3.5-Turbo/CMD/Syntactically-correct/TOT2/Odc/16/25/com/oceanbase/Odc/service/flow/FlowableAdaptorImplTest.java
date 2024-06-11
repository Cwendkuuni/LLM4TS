import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    
    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }
    
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "12345";
        
        Mockito.when(flowInstanceRepository.findAll(Mockito.any())).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        
        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        
        assertTrue(flowInstanceIdOptional.isPresent());
        assertEquals(Long.valueOf(1L), flowInstanceIdOptional.get());
    }
    
    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "54321";
        
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        
        Mockito.verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }
    
    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "54321";
        
        Mockito.when(flowInstanceRepository.findAll(Mockito.any())).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        
        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        
        assertTrue(flowInstanceIdOptional.isPresent());
        assertEquals(Long.valueOf(1L), flowInstanceIdOptional.get());
    }
    
    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        
        Optional<String> processInstanceIdOptional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        
        assertTrue(processInstanceIdOptional.isPresent());
        assertEquals("12345", processInstanceIdOptional.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType elementType = FlowableElementType.ELEMENT;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, elementType))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        assertEquals(1, flowableAdaptor.getFlowableElementByType(instanceId, instanceType, elementType).size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "1";
        Long flowInstanceId = 1L;

        Optional<ServiceTaskInstanceEntity> optional = Optional.of(new ServiceTaskInstanceEntity());
        Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(optional);

        Optional<FlowTaskInstance> taskInstanceOptional = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(taskInstanceOptional.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> optional = Optional.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(optional);

        Optional<FlowApprovalInstance> approvalInstanceOptional = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(approvalInstanceOptional.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "Task Name";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> optional = Optional.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(optional);

        Optional<FlowApprovalInstance> approvalInstanceOptional = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(approvalInstanceOptional.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "1";
        Long flowInstanceId = 1L;

        Optional<GateWayInstanceEntity> optional = Optional.of(new GateWayInstanceEntity());
        Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(optional);

        Optional<FlowGatewayInstance> gatewayInstanceOptional = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(gatewayInstanceOptional.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance();
        FlowableElement flowableElement = new FlowableElement("1", "Element", FlowableElementType.ELEMENT);

        Mockito.when(nodeInstanceEntityRepository.save(Mockito.any())).thenReturn(new NodeInstanceEntity());

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        Mockito.verify(nodeInstanceEntityRepository).save(Mockito.any());
    }

}