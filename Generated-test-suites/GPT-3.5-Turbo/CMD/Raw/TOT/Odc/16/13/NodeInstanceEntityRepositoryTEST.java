import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @InjectMocks
    @Spy
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private List<NodeInstanceEntity> nodeList;

    @Before
    public void setUp() {
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setInstanceId(1L);
        node1.setInstanceType(FlowNodeType.TYPE1);
        node1.setFlowInstanceId(101L);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setInstanceId(2L);
        node2.setInstanceType(FlowNodeType.TYPE2);
        node2.setFlowInstanceId(102L);

        nodeList = new ArrayList<>(Arrays.asList(node1, node2));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE1, FlowableElementType.ELEMENT1);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getInstanceId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(repository.findByFlowInstanceId(anyLong())).thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(101L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getInstanceId());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(repository.findByIds(anyCollection())).thenReturn(nodeList);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(repository.deleteByFlowInstanceId(anyLong())).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(101L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(repository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE1);

        assertEquals(1, result);
    }

}