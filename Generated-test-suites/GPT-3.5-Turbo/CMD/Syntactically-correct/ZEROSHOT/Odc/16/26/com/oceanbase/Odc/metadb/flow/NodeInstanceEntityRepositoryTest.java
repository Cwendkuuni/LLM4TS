import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;

    @Before
    public void setUp() {
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setInstanceId(101L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.NODE);
        
        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setInstanceId(102L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.NODE);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(101L, FlowNodeType.NODE, FlowableElementType.FLOW))
               .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(101L, FlowNodeType.NODE, FlowableElementType.FLOW);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId().longValue());
        assertEquals(2L, result.get(1).getId().longValue());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(201L))
               .thenReturn(Collections.singletonList(nodeInstanceEntity1));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(201L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId().longValue());
    }

    @Test
    public void testFindByIds() {
        Mockito.when(nodeInstanceEntityRepository.findByIds(Collections.singleton(1L)))
               .thenReturn(Collections.singletonList(nodeInstanceEntity1));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Collections.singleton(1L));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId().longValue());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(201L))
               .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(201L);
        assertTrue(result == 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(101L, FlowNodeType.NODE))
               .thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(101L, FlowNodeType.NODE);
        assertTrue(result == 1);
    }
}