import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Before
    public void setUp() {
        flowableAdaptor = new FlowableAdaptorImpl();
        // Mocking dependencies
        flowableAdaptor.flowInstanceRepository = mock(FlowInstanceRepository.class);
        flowableAdaptor.nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        flowableAdaptor.taskService = mock(TaskService.class);
        flowableAdaptor.serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        flowableAdaptor.userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        flowableAdaptor.gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "p123";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        when(flowableAdaptor.flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowableAdaptor.flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "p123";
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        when(flowableAdaptor.flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId))))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(Long.valueOf(1L), result.get());
    }

    // Test other methods in a similar fashion

}