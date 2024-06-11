package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Test
    public void testSetProcessInstanceId() {
        // Test case for setProcessInstanceId method
        Long flowInstanceId = 1L;
        String processInstanceId = "123";
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        Optional<String> retrievedProcessInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        
        assertTrue(retrievedProcessInstanceId.isPresent());
        assertEquals(processInstanceId, retrievedProcessInstanceId.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test case for getFlowInstanceIdByProcessInstanceId method
        String processInstanceId = "123";

        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertFalse(flowInstanceId.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        // Test case for setProcessDefinitionId method
        Long flowInstanceId = 1L;
        String processDefinitionId = "def123";

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        Optional<String> retrievedProcessDefinitionId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(retrievedProcessDefinitionId.isPresent());
        assertEquals(processDefinitionId, retrievedProcessDefinitionId.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        // Test case for getFlowInstanceIdByProcessDefinitionId method
        String processDefinitionId = "def123";

        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertFalse(flowInstanceId.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        // Test case for getProcessInstanceIdByFlowInstanceId method
        Long flowInstanceId = 1L;
        
        Optional<String> processInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertFalse(processInstanceId.isPresent());
    }

    // Add more test methods to cover all methods in FlowableAdaptorImpl class

}