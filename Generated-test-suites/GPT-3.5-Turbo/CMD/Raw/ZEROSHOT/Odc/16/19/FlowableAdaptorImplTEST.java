package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class FlowableAdaptorImplTest {

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";

        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertEquals(Optional.of(processInstanceId), flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId));
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "67890";
        Long flowInstanceId = 2L;

        when(flowInstanceRepository.findAll(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId)))
                .thenReturn(Collections.singletonList(new FlowInstanceEntity(flowInstanceId, processDefinitionId)));

        assertEquals(Optional.of(flowInstanceId), flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId));
    }

    // Add more test methods for other methods in FlowableAdaptorImpl

}