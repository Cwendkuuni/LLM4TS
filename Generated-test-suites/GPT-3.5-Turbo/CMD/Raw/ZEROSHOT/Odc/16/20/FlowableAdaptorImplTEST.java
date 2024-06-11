import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;

    @Test
    public void testSetProcessInstanceId() {
        // Setup
        Long flowInstanceId = 1L;
        String processInstanceId = "process1";

        // Mocks
        FlowInstanceEntity entity = mock(FlowInstanceEntity.class);
        when(entity.getId()).thenReturn(flowInstanceId);

        Optional<FlowInstanceEntity> optional = Optional.of(entity);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);

        // Test
        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Verify
        verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        // Setup
        String processInstanceId = "process1";
        Long flowId = 1L;

        // Mocks
        FlowInstanceEntity entity = mock(FlowInstanceEntity.class);
        when(entity.getId()).thenReturn(flowId);

        List<FlowInstanceEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        Specification<FlowInstanceEntity> specification = mock(Specification.class);
        when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

        // Test
        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        // Verify
        assertTrue(result.isPresent());
        assertEquals(flowId, result.get());
    }

    // Add more test cases for all other methods in FlowableAdaptorImpl here

}