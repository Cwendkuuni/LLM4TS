import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepoMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        nodeInstanceEntityRepository = new NodeInstanceEntityRepository();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_A;

        List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setId(1L);
        node1.setInstanceId(instanceId);
        node1.setInstanceType(instanceType);
        node1.setFlowableElementType(flowableElementType);
        expectedNodes.add(node1);

        Mockito.when(nodeInstanceEntityRepoMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
        assertEquals(expectedNodes.get(0).getId(), actualNodes.get(0).getId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setId(1L);
        node1.setFlowInstanceId(flowInstanceId);
        expectedNodes.add(node1);

        Mockito.when(nodeInstanceEntityRepoMock.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
        assertEquals(expectedNodes.get(0).getId(), actualNodes.get(0).getId());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setId(1L);
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setId(2L);
        NodeInstanceEntity node3 = new NodeInstanceEntity();
        node3.setId(3L);
        expectedNodes.add(node1);
        expectedNodes.add(node2);
        expectedNodes.add(node3);

        Mockito.when(nodeInstanceEntityRepoMock.findByIds(ids))
                .thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
        assertEquals(expectedNodes.get(0).getId(), actualNodes.get(0).getId());
        assertEquals(expectedNodes.get(1).getId(), actualNodes.get(1).getId());
        assertEquals(expectedNodes.get(2).getId(), actualNodes.get(2).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int expectedDeletedCount = 1;

        Mockito.when(nodeInstanceEntityRepoMock.deleteByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedDeletedCount);

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(expectedDeletedCount, actualDeletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_B;
        int expectedDeletedCount = 1;

        Mockito.when(nodeInstanceEntityRepoMock.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(expectedDeletedCount);

        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedDeletedCount, actualDeletedCount);
    }
}