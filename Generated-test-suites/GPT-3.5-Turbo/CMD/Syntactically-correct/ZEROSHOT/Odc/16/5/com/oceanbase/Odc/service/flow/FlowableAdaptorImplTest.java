import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private TaskService taskService;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        taskService = mock(TaskService.class);

        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptor.setTaskService(taskService);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "12345";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity(flowInstanceId, processInstanceId);

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        assertEquals(processInstanceId, flowInstanceEntity.getProcessInstanceId());
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "12345";
        Long flowInstanceId = 1L;
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity(flowInstanceId, processInstanceId);

        when(flowInstanceRepository.findAll(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId)))
                .thenReturn(Arrays.asList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(flowInstanceId, result.get());
    }

    // Add test cases for other methods

}