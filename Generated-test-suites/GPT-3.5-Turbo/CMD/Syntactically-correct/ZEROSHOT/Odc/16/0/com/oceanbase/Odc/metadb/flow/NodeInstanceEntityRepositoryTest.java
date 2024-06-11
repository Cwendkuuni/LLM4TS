import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutor;

    @Before
    public void setUp() {
        nodeInstanceEntityRepository = new NodeInstanceEntityRepository();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();

        List<NodeInstanceEntity> expectedEntities = Arrays.asList(entity1, entity2);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actualEntities);
        assertEquals(2, actualEntities.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();

        List<NodeInstanceEntity> expectedEntities = Arrays.asList(entity1, entity2);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualEntities);
        assertEquals(2, actualEntities.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        NodeInstanceEntity entity2 = new NodeInstanceEntity();

        List<NodeInstanceEntity> expectedEntities = Arrays.asList(entity1, entity2);

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(actualEntities);
        assertEquals(2, actualEntities.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
                .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }
}