import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @MockBean
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        NodeInstanceEntity node = Mockito.mock(NodeInstanceEntity.class);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SOME_TYPE, FlowableElementType.SOME_TYPE))
                .thenReturn(Arrays.asList(node));

        List<NodeInstanceEntity> found = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SOME_TYPE, FlowableElementType.SOME_TYPE);
        assertThat(found).contains(node);
    }

    @Test
    public void findByFlowInstanceIdTest() {
        NodeInstanceEntity node = Mockito.mock(NodeInstanceEntity.class);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(Arrays.asList(node));

        List<NodeInstanceEntity> found = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertThat(found).contains(node);
    }

    @Test
    public void findByIdsTest() {
        NodeInstanceEntity node = Mockito.mock(NodeInstanceEntity.class);
        when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L)))
                .thenReturn(Arrays.asList(node));

        List<NodeInstanceEntity> found = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));
        assertThat(found).contains(node);
    }

    @Test
    public void deleteByFlowInstanceIdTest() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        assertThat(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).isEqualTo(1);
    }
    
    @Test
    public void deleteByInstanceIdAndInstanceTypeTest() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SOME_TYPE)).thenReturn(1);
        assertThat(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SOME_TYPE)).isEqualTo(1);
    }
}