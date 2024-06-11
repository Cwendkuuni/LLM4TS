
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;

public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "123";
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "123";
        flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        Mockito.verify(flowInstanceRepository).findAll(Mockito.any());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "123";
        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
        Mockito.verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "123";
        flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        Mockito.verify(flowInstanceRepository).findAll(Mockito.any());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        Mockito.when(flowInstanceRepository.findById(flowInstanceId))
        .thenReturn(Optional.of(Mockito.mock(FlowInstanceEntity.class)));
        assertNotNull(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId));
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        assertNotNull(flowableAdaptor.getFlowableElementByType(instanceId, Mockito.mock(FlowNodeType.class),
                Mockito.mock(FlowableElementType.class)));
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        assertNotNull(flowableAdaptor.getTaskInstanceByActivityId("task1", 1L));
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        assertNotNull(flowableAdaptor.getApprovalInstanceByActivityId("task1", 1L));
    }

    @Test
    public void testGetApprovalInstanceByName() {
        assertNotNull(flowableAdaptor.getApprovalInstanceByName("task1", 1L));
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        assertNotNull(flowableAdaptor.getGatewayInstanceByActivityId("task1", 1L));
    }

    @Test
    public void testSetFlowableElement() {
        flowableAdaptor.setFlowableElement(Mockito.mock(BaseFlowNodeInstance.class),
        Mockito.mock(FlowableElement.class));
        Mockito.verify(nodeInstanceRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(Mockito.anyLong(),
        Mockito.any(), Mockito.any());
    }
}
