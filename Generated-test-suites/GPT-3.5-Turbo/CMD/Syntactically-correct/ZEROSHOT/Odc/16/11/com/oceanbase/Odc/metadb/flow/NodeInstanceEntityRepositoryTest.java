import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository repository;

    @Mock
    private NodeInstanceEntity entity1;
    @Mock
    private NodeInstanceEntity entity2;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.NODE;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> expectedEntities = new ArrayList<>(Arrays.asList(entity1, entity2));

        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedEntities.size(), actualEntities.size());
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedEntities = new ArrayList<>(Arrays.asList(entity1, entity2));

        Mockito.when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedEntities.size(), actualEntities.size());
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L));

        List<NodeInstanceEntity> expectedEntities = new ArrayList<>(Arrays.asList(entity1, entity2));

        Mockito.when(repository.findByIds(ids)).thenReturn(expectedEntities);

        List<NodeInstanceEntity> actualEntities = repository.findByIds(ids);

        assertEquals(expectedEntities.size(), actualEntities.size());
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.NODE;

        Mockito.when(repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }

}