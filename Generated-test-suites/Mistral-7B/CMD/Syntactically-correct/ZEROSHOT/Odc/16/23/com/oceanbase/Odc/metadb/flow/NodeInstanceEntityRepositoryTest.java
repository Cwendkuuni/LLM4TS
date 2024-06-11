import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.test.base.BaseTest;

@SpringBootTest
@SpringJUnitConfig(classes = NodeInstanceEntityRepositoryTest.class)
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest extends BaseTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;

    @BeforeEach
    public void setup() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.PROCESS);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        Long instanceId = 1L;
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        Assertions.assertEquals(1, nodeInstanceEntities.size());
        Assertions.assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        Assertions.assertEquals(1, nodeInstanceEntities.size());
        Assertions.assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        Collection<Long> ids = List.of(id1, id2);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        Assertions.assertEquals(2, nodeInstanceEntities.size());
        Assertions.assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        int deletedRows = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        Assertions.assertEquals(1, deletedRows);
        Assertions.assertFalse(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()).isPresent());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        Long instanceId = 1L;

        int deletedRows = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        Assertions.assertEquals(1, deletedRows);
        Assertions.assertFalse(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()).isPresent());
    }
}