package com.oceanbase.odc.service.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testSetProcessInstanceId() {
        // Test case for setting process instance ID
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
        assertEquals(flowInstanceId, flowInstanceRepository.findById(flowInstanceId).get().getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Test case for getting flow instance ID by process instance ID
        String processInstanceId = "12345";
        long flowInstanceId = 1L;
        Mockito.when(flowInstanceRepository.findAll(Mockito.any())).thenReturn(Collections.singletonList(new FlowInstanceEntity()));
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertEquals(flowInstanceId, result.get().longValue());
    }

    // Additional test cases for other methods in the class can be added here

}