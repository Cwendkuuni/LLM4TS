import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.oceanbase.odc.metadb.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowInstanceEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(nodeInstanceEntities);
        assertEquals(1, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EdgeCases() {
        Long instanceId = -1L;
        FlowableElementType flowableElementType = null;
        FlowNodeType instanceType = null;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Test with wrong instanceId, null flowableElementType, and null instanceType
        assertNotNull(nodeInstanceEntities);
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(nodeInstanceEntities);
        assertEquals(2, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByFlowInstanceId_EdgeCases() {
        Long flowInstanceId = -1L;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Test with negative flowInstanceId and empty result
        assertNotNull(nodeInstanceEntities);
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(nodeInstanceEntities);
        assertEquals(3, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByIds_EdgeCases() {
        Collection<Long> ids = null;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        // Test with empty ids list and wrong ids
        assertNotNull(nodeInstanceEntities);
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertNotNull(result);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertNotNull(result);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_EdgeCases() {
        Long instanceId = -1L;
        FlowNodeType instanceType = FlowNodeType.END_EVENT;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Test with negative instanceId and wrong instanceType
        assertNotNull(result);
        assertEquals(0, result);
    }
}