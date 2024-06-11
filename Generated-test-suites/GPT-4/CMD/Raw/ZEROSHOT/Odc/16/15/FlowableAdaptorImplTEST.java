
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

public class FlowableAdaptorImplTest {
    
    @Mock
    private NodeInstanceEntityRepository nodeInstanceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
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
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetProcessInstanceId() {
        //You can add implement code mockup for testing here
    }
    
    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        //You can add implement code mockup for testing here
    }

    @Test
    public void testSetProcessDefinitionId() {
        //You can add implement code mockup for testing here
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        //You can add implement code mockup for testing here
    }
    
    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        //You can add implement code mockup for testing here
    }
    
    @Test
    public void testGetFlowableElementByType() {
        //You can add implement code mockup for testing here
    }
    
    @Test
    public void testGetTaskInstanceByActivityId() {
        //You can add implement code mockup for testing here
    }
    
    @Test
    public void testGetApprovalInstanceByActivityId() {
        //You can add implement code mockup for testing here
    }

    @Test
    public void testGetApprovalInstanceByName() {
        //You can add implement code mockup for testing here
    }
    
    @Test
    public void testGetGatewayInstanceByActivityId() {
        //You can add implement code mockup for testing here
    }

    @Test
    public void testSetFlowableElement() {
        //You can add implement code mockup for testing here
    }

}
