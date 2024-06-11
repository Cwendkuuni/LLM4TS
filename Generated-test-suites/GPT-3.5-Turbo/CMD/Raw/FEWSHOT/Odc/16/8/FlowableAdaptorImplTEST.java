/*
 * Test cases for the class FlowableAdaptorImpl
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    // Mock dependencies
    private NodeInstanceEntityRepository nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
    private FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
    private UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
    private GateWayInstanceRepository gateWayInstanceRepository = mock(GateWayInstanceRepository.class);

    @Before
    public void setup() {
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepository);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "123";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "123";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(List.of(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().longValue());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "456";
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "456";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(List.of(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().longValue());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId_NotFound() {
        String processDefinitionId = "789";
        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(List.of());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals("123", result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId_NotFound() {
        Long flowInstanceId = 2L;
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertFalse(result.isPresent());
    }

}