import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository repository;

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepository;
    @Mock
    private JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE_A;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setId(1L);
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setId(2L);
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(entity1, entity2);

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedEntities.size(), actualEntities.size());
        assertEquals(expectedEntities.get(0).getId(), actualEntities.get(0).getId());
        assertEquals(expectedEntities.get(1).getId(), actualEntities.get(1).getId());
    }

    // Add test cases for other methods like findByFlowInstanceId, findByIds, deleteByFlowInstanceId, deleteByInstanceIdAndInstanceType

}