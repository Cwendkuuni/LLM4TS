import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeList.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE_A))
                .thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.TYPE_A);

        assertEquals(1, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeList.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
                .thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertEquals(1, result.size());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeList.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByIds(new ArrayList<>()))
                .thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(new ArrayList<>());

        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L))
                .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE1))
                .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE1);

        assertEquals(1, result);
    }
}