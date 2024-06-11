
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository;

    @Before
    public void setUp() {
        repository = mock(NodeInstanceEntityRepository.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> expected = new ArrayList<>();
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expected);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expected = new ArrayList<>();
        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expected);

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);

        List<NodeInstanceEntity> expected = new ArrayList<>();
        when(repository.findByIds(ids)).thenReturn(expected);

        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        when(repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }
}
