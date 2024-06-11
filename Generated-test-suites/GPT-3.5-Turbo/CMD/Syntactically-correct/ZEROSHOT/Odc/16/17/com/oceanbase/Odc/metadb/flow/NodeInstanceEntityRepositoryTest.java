import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        // Mocking the repository
        nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        expectedList.add(nodeInstanceEntity1);
        expectedList.add(nodeInstanceEntity2);

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        expectedList.add(nodeInstanceEntity1);
        expectedList.add(nodeInstanceEntity2);

        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        expectedList.add(nodeInstanceEntity1);
        expectedList.add(nodeInstanceEntity2);

        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId))
                .thenReturn(1);

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(1);

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, rowsAffected);
    }

}