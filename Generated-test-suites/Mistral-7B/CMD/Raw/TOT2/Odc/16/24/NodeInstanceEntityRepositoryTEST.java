import org.junit.Test;
import org.junit.jupiters.api.Engine;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.TASK);
        assertNotNull(nodeInstanceEntities);
    }

    // Test for findByFlowInstanceId method
    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(nodeInstanceEntities);
    }

    // Test for findByIds method
    @Test
    public void testFindByIds() {
        Collection<Long> ids = List.of(1L, 2L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(nodeInstanceEntities);
    }

    // Test for deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    // Test for deleteByInstanceIdAndInstanceType method
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);
    }

    // Test for null flowInstanceId, instanceId, instanceType, and flowableElementType
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByNullFlowInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByNullInstanceId() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, FlowNodeType.START);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInvalidInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.INVALID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInvalidFlowableElementType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START, FlowableElementType.INVALID);
    }

    // Test for non-existing id(s) in the database
    @Test
    public void testDeleteNonExistingById() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(10L, FlowNodeType.START);
        assertEquals(0, result);
    }

    // Test for concurrent deletion of entities
    @Test(expected = OptimisticLockingFailureException.class)
    public void testDeleteConcurrently() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity1.setVersion(1);
        entity2.setVersion(1);

        nodeInstanceEntityRepository.save(entity1);
        nodeInstanceEntityRepository.save(entity2);

        nodeInstanceEntityRepository.delete(entity1);
        nodeInstanceEntityRepository.delete(entity2);

        nodeInstanceEntityRepository.delete(entity1);
    }

    // Test for empty collections of ids
    @Test
    public void testDeleteEmptyIds() {
        Collection<Long> ids = Collections.emptyList();
        int result = nodeInstanceEntityRepository.deleteByIds(ids);
        assertEquals(0, result);
    }
}