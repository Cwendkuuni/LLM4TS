package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "process_123";
        assertEquals(0, flowInstanceRepository.count());
        
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        
        Optional<String> retrievedProcessInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertEquals(processInstanceId, retrievedProcessInstanceId.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "process_456";
        assertEquals(0, flowInstanceRepository.count());
        
        flowableAdaptor.setProcessInstanceId(2L, processInstanceId);
        
        Optional<Long> retrievedFlowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(2L, retrievedFlowInstanceId.get().longValue());
    }

    @After
    public void tearDown() {
        // clean up if needed
    }

}