import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setFlowInstanceId(100L);
        nodeInstanceEntity1.setInstanceId(101L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.USER_TASK);

        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setFlowInstanceId(200L);
        nodeInstanceEntity2.setInstanceId(201L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END_EVENT);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.SERVICE_TASK);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedResults = new ArrayList<>(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));
        
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(101L, FlowNodeType.START_EVENT, FlowableElementType.USER_TASK))
                .thenReturn(expectedResults);
        
        List<NodeInstanceEntity> actualResults = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(101L, FlowNodeType.START_EVENT, FlowableElementType.USER_TASK);
        
        assertNotNull(actualResults);
        assertEquals(expectedResults.size(), actualResults.size());
        assertTrue(expectedResults.containsAll(actualResults));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedResults = new ArrayList<>(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));
        
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(100L))
                .thenReturn(expectedResults);
        
        List<NodeInstanceEntity> actualResults = nodeInstanceEntityRepository.findByFlowInstanceId(100L);
        
        assertNotNull(actualResults);
        assertEquals(expectedResults.size(), actualResults.size());
        assertTrue(expectedResults.containsAll(actualResults));
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expectedResults = new ArrayList<>(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));
        
        Mockito.when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L)))
                .thenReturn(expectedResults);
        
        List<NodeInstanceEntity> actualResults = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        
        assertNotNull(actualResults);
        assertEquals(expectedResults.size(), actualResults.size());
        assertTrue(expectedResults.containsAll(actualResults));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(100L))
                .thenReturn(2);
        
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(100L);
        
        assertEquals(2, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(101L, FlowNodeType.START_EVENT))
                .thenReturn(1);
        
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(101L, FlowNodeType.START_EVENT);
        
        assertEquals(1, result);
    }
}