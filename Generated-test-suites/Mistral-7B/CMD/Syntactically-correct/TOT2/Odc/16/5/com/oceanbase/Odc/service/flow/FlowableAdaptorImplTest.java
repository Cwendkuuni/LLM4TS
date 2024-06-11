import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SuppressWarnings("unchecked")
public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private EventPublisher eventPublisher;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private FlowInstanceRepository flowInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private TaskService taskService;
    private FormService formService;
    private AuthenticationFacade authenticationFacade;
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        flowableAdaptor = new FlowableAdaptorImpl();
        nodeInstanceRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
        eventPublisher = Mockito.mock(EventPublisher.class);
        nodeRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        sequenceRepository = Mockito.mock(SequenceInstanceRepository.class);
        flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);
        taskService = Mockito.mock(TaskService.class);
        formService = Mockito.mock(FormService.class);
        authenticationFacade = Mockito.mock(AuthenticationFacade.class);
        gateWayInstanceRepository = Mockito.mock(GateWayInstanceRepository.class);

        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setEventPublisher(eventPublisher);
        flowableAdaptor.setNodeRepository(nodeRepository);
        flowableAdaptor.setSequenceRepository(sequenceRepository);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptor.setTaskService(taskService);
        flowableAdaptor.setFormService(formService);
        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepository);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";

        Mockito.doNothing().when(flowInstanceRepository).updateProcessInstanceIdById(ArgumentMatchers.eq(flowInstanceId),
                ArgumentMatchers.eq(processInstanceId));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        Mockito.verify(flowInstanceRepository).updateProcessInstanceIdById(ArgumentMatchers.eq(flowInstanceId),
                ArgumentMatchers.eq(processInstanceId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdNullFlowInstanceId() {
        String processInstanceId = "testProcessInstanceId";

        flowableAdaptor.setProcessInstanceId(null, processInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProcessInstanceIdNullProcessInstanceId() {
        Long flowInstanceId = 1L;

        flowableAdaptor.setProcessInstanceId(flowInstanceId, null);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "testProcessInstanceId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        Mockito.when(flowInstanceRepository.findAll(ArgumentMatchers.any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        Mockito.verify(flowInstanceRepository).findAll(ArgumentMatchers.any(Specification.class));
        assertEquals(flowInstanceId, result.get());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceIdEmptyResult() {
        String processInstanceId = "testProcessInstanceId";

        Mockito.when(flowInstanceRepository.findAll(ArgumentMatchers.any(Specification.class)))
                .thenReturn(Collections.emptyList());

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        Mockito.verify(flowInstanceRepository).findAll(ArgumentMatchers.any(Specification.class));
        assertNull(result);
    }

    // Add more test cases for other methods in the FlowableAdaptorImpl class

}