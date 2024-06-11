import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

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

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.nodeInstanceRepository = nodeInstanceRepository;
        flowableAdaptor.serviceTaskInstanceRepository = serviceTaskInstanceRepository;
        flowableAdaptor.eventPublisher = eventPublisher;
        flowableAdaptor.nodeRepository = nodeRepository;
        flowableAdaptor.sequenceRepository = sequenceRepository;
        flowableAdaptor.flowInstanceRepository = flowInstanceRepository;
        flowableAdaptor.userTaskInstanceRepository = userTaskInstanceRepository;
        flowableAdaptor.taskService = taskService;
        flowableAdaptor.formService = formService;
        flowableAdaptor.authenticationFacade = authenticationFacade;
        flowableAdaptor.gateWayInstanceRepository = gateWayInstanceRepository;
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "123456";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId)).thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository).findById(flowInstanceId);
        verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "123456";

        when(flowInstanceRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(1L, result.get().longValue());

        verify(flowInstanceRepository).findAll(any(Specification.class));
    }

    // Add more test methods for other functionalities of FlowableAdaptorImpl
    
}