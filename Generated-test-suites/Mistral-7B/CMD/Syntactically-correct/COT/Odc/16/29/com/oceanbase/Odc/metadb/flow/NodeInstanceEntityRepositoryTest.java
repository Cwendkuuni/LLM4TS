import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-metadb.xml")
@Transactional
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    // Test method: findByInstanceIdAndInstanceTypeAndFlowableElementType
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        expectedResult.add(nodeInstanceEntity);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, instanceType, flowableElementType);
        // No need to assert anything, an IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, flowableElementType);
        // No need to assert anything, an IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null);
        // No need to assert anything, an IllegalArgumentException should be thrown
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        expectedResult.add(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        expectedResult.add(nodeInstanceEntity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceIdWithNullFlowInstanceId() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(null);
        // No need to assert anything, an IllegalArgumentException should be thrown
    }

    // Test method: findByIds
    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        Collection<Long> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(id1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(id2);

        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        expectedResult.add(nodeInstanceEntity1);
        expectedResult.add(nodeInstanceEntity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsWithNullIds() {
        Collection<Long> ids = null;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        // No need to assert anything, an IllegalArgumentException should be thrown
    }

    // Test method: deleteByFlowInstanceId
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedNumberOfDeletedEntities = 2;

        for (int i = 0; i < expectedNumberOfDeletedEntities; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            nodeInstanceEntity.setFlowInstanceId(instanceId);
            nodeInstanceEntityRepository.save(nodeInstanceEntity);
        }

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        Assert.assertEquals(expectedNumberOfDeletedEntities, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
        // No need to assert anything, an IllegalArgumentException should be thrown
    }

    // Test method: deleteByInstanceIdAndInstanceType
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumberOfDeletedEntities = 2;

        for (int i = 0; i < expectedNumberOfDeletedEntities; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            nodeInstanceEntity.setInstanceId(instanceId);
            nodeInstanceEntity.setInstanceType(instanceType);
            nodeInstanceEntityRepository.save(nodeInstanceEntity);
        }

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assert.assertEquals(expectedNumberOfDeletedEntities, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.START;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, instanceType);
        // No need to assert anything, an IllegalArgumentException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, null);
        // No need to assert anything, an IllegalArgumentException should be thrown
    }

}