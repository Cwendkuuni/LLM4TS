
package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

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

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId(){
      //TODO: Implement method test case
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId(){
      //TODO: Implement method test case
    }

    @Test
    public void testSetProcessDefinitionId(){
      //TODO: Implement method test case
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId(){
      //TODO: Implement method test case
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId(){
      //TODO: Implement method test case
    }

    @Test
    public void testGetFlowableElementByType(){
      //TODO: Implement method test case
    }

    @Test
    public void testGetTaskInstanceByActivityId(){
      //TODO: Implement method test case
    }

    @Test
    public void testGetApprovalInstanceByActivityId(){
      //TODO: Implement method test case
    }

    @Test
    public void testGetApprovalInstanceByName(){
      //TODO: Implement method test case
    }

    @Test
    public void testGetGatewayInstanceByActivityId(){
      //TODO: Implement method test case
    }

    @Test
    public void testSetFlowableElement(){
      //TODO: Implement method test case
    }

}
