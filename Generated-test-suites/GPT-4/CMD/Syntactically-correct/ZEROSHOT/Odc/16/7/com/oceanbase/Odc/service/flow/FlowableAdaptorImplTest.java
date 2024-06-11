package com.oceanbase.odc.service.flow;

import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowableAdaptorImplTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    
    @Mock
    private EventPublisher eventPublisher;
    
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @Mock
    private TaskService taskService;
    
    @Mock
    private FormService formService;
    
    @Mock
    private AuthenticationFacade authenticationFacade;
    
    @Mock
    private GateWayInstanceRepository gateWayInstanceRepository;    

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptorImpl;

    @Before
    public void setUp(){
        flowableAdaptorImpl = new FlowableAdaptorImpl();
    }
  
    @Test
    public void testSetProcessInstanceId() {
        // Mock FlowInstanceEntity and FlowInstanceRepository
        FlowInstanceEntity entity = Mockito.mock(FlowInstanceEntity.class);
        when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(flowInstanceRepository.updateProcessInstanceIdById(1L, "processId")).thenReturn(1);

        // Make call to method
        flowableAdaptorImpl.setProcessInstanceId(1L, "processId");

        // Verify the interaction with FlowInstanceRepository
        Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(1L, "processId");
    }
  
    // Add all the test cases for each methods here..

}
