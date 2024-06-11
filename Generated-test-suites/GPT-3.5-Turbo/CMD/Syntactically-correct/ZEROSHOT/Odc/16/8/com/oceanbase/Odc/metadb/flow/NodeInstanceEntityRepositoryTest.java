import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityService nodeInstanceEntityService;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        FlowNodeType nodeType = FlowNodeType.NODE;
        FlowableElementType elementType = FlowableElementType.TASK;

        nodeList.add(new NodeInstanceEntity());
        nodeList.add(new NodeInstanceEntity());

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(Mockito.anyLong(), Mockito.eq(nodeType), Mockito.eq(elementType)))
                .thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityService.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, nodeType, elementType);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        nodeList.add(new NodeInstanceEntity());
        nodeList.add(new NodeInstanceEntity());

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(Mockito.anyLong()))
                .thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityService.findByFlowInstanceId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        nodeList.add(new NodeInstanceEntity());
        nodeList.add(new NodeInstanceEntity());

        Mockito.when(nodeInstanceEntityRepository.findByIds(Mockito.any()))
                .thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityService.findByIds(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(Mockito.anyLong()))
                .thenReturn(2);

        int result = nodeInstanceEntityService.deleteByFlowInstanceId(1L);

        assertEquals(2, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        FlowNodeType nodeType = FlowNodeType.NODE;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(Mockito.anyLong(), Mockito.eq(nodeType)))
                .thenReturn(1);

        int result = nodeInstanceEntityService.deleteByInstanceIdAndInstanceType(1L, nodeType);

        assertEquals(1, result);
    }
}