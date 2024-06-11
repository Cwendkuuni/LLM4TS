
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutor;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private List<NodeInstanceEntity> testNodeInstances;

    @Before
    public void setup() {
        testNodeInstances = new ArrayList<>();
        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
        nodeInstance1.setId(1L);
        nodeInstance1.setInstanceId(101L);
        nodeInstance1.setInstanceType(FlowNodeType.TYPE_A);
        nodeInstance1.setFlowInstanceId(201L);
        testNodeInstances.add(nodeInstance1);

        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
        nodeInstance2.setId(2L);
        nodeInstance2.setInstanceId(102L);
        nodeInstance2.setInstanceType(FlowNodeType.TYPE_B);
        nodeInstance2.setFlowInstanceId(202L);
        testNodeInstances.add(nodeInstance2);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(101L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A))
                .thenReturn(testNodeInstances);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(101L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), 1L);
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(201L))
                .thenReturn(testNodeInstances);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(201L);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), 1L);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(testNodeInstances);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(result.size(), 2);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(201L))
                .thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(201L);
        assertEquals(result, 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(101L, FlowNodeType.TYPE_A))
                .thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(101L, FlowNodeType.TYPE_A);
        assertEquals(result, 1);
    }
}
