import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryTest nodeInstanceEntityRepositoryTest;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_A;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> nodeList = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
            .thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> nodeList = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
            .thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> nodeList = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);

        when(nodeInstanceEntityRepository.findByIds(ids))
            .thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }

}