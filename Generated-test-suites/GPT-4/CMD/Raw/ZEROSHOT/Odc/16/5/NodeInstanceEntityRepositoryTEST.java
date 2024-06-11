
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity node1;
    
    private NodeInstanceEntity node2;
    
    @Before
    public void setUp() {
        node1 = new NodeInstanceEntity();
        node1.setId(1L);
        node1.setInstanceId(10L);
        node1.setFlowInstanceId(20L);
        node1.setInstanceType(FlowNodeType.START);
        node1.setFlowableElementType(FlowableElementType.START_EVENT);

        node2 = new NodeInstanceEntity();
        node2.setId(2L);
        node2.setInstanceId(10L);
        node2.setFlowInstanceId(20L);
        node2.setInstanceType(FlowNodeType.END);
        node2.setFlowableElementType(FlowableElementType.END_EVENT);
    }

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(node1));
        
        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(10L, FlowNodeType.START, FlowableElementType.START_EVENT);
        assertEquals(1, nodes.size());
        assertEquals(Long.valueOf(1L), nodes.get(0).getId());
    }

    @Test
    public void findByFlowInstanceIdTest() {
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(any(Long.class)))
                .thenReturn(Arrays.asList(node1, node2));

        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByFlowInstanceId(20L);
        assertEquals(2, nodes.size());
    }

    @Test
    public void findByIdsTest() {
        Mockito.when(nodeInstanceEntityRepository.findByIds(any(List.class)))
                .thenReturn(Arrays.asList(node1, node2));

        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        assertEquals(2, nodes.size());
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any(Long.class)))
                .thenReturn(1);

        int deleted = nodeInstanceEntityRepository.deleteByFlowInstanceId(20L);
        assertEquals(1, deleted);
    }

    @Test
    public void deleteByInstanceIdAndInstanceTypeTest() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class)))
                .thenReturn(1);

        int deleted = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(10L, FlowNodeType.START);
        assertEquals(1, deleted);
    }
}
