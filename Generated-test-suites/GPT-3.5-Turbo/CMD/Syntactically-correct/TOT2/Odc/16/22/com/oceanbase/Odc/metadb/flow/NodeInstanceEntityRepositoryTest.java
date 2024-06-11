import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryImpl nodeInstanceEntityRepositoryImpl;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.TYPE_X;

        List<NodeInstanceEntity> expectedNodes = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepositoryImpl.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 2L;

        List<NodeInstanceEntity> expectedNodes = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepositoryImpl.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> expectedNodes = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepositoryImpl.findByIds(ids);

        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int deletedCount = 2;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(deletedCount);

        int result = nodeInstanceEntityRepositoryImpl.deleteByFlowInstanceId(instanceId);

        assertEquals(deletedCount, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        int deletedCount = 2;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(deletedCount);

        int result = nodeInstanceEntityRepositoryImpl.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(deletedCount, result);
    }
}