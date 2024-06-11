import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryImpl;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_A;

        List<NodeInstanceEntity> expectedResults = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        expectedResults.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType)).thenReturn(expectedResults);

        List<NodeInstanceEntity> actualResults = nodeInstanceEntityRepositoryImpl
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actualResults);
        assertFalse(actualResults.isEmpty());
        assertEquals(expectedResults.size(), actualResults.size());
        assertEquals(expectedResults.get(0).getId(), actualResults.get(0).getId());
        assertEquals(expectedResults.get(0).getInstanceId(), actualResults.get(0).getInstanceId());
        assertEquals(expectedResults.get(0).getInstanceType(), actualResults.get(0).getInstanceType());
        assertEquals(expectedResults.get(0).getFlowableElementType(), actualResults.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedResults = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        expectedResults.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResults);

        List<NodeInstanceEntity> actualResults = nodeInstanceEntityRepositoryImpl.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualResults);
        assertFalse(actualResults.isEmpty());
        assertEquals(expectedResults.size(), actualResults.size());
        assertEquals(expectedResults.get(0).getId(), actualResults.get(0).getId());
        assertEquals(expectedResults.get(0).getFlowInstanceId(), actualResults.get(0).getFlowInstanceId());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> expectedResults = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setId(3L);
        expectedResults.add(nodeInstanceEntity1);
        expectedResults.add(nodeInstanceEntity2);
        expectedResults.add(nodeInstanceEntity3);

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedResults);

        List<NodeInstanceEntity> actualResults = nodeInstanceEntityRepositoryImpl.findByIds(ids);

        assertNotNull(actualResults);
        assertFalse(actualResults.isEmpty());
        assertEquals(expectedResults.size(), actualResults.size());
        assertEquals(expectedResults.get(0).getId(), actualResults.get(0).getId());
        assertEquals(expectedResults.get(1).getId(), actualResults.get(1).getId());
        assertEquals(expectedResults.get(2).getId(), actualResults.get(2).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int rowsAffected = nodeInstanceEntityRepositoryImpl.deleteByFlowInstanceId(instanceId);

        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int rowsAffected = nodeInstanceEntityRepositoryImpl.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, rowsAffected);
    }

}