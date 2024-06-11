import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryServiceImpl nodeInstanceEntityRepositoryService;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SOME_TYPE;
        FlowableElementType flowableElementType = FlowableElementType.SOME_ELEMENT_TYPE;
        
        List<NodeInstanceEntity> result = new ArrayList<>();
        result.add(new NodeInstanceEntity());
        
        when(nodeInstanceEntityRepository
             .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
             .thenReturn(result);
        
        List<NodeInstanceEntity> found = nodeInstanceEntityRepositoryService
                                           .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        
        assertNotNull(found);
        assertEquals(1, found.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        
        List<NodeInstanceEntity> result = new ArrayList<>();
        result.add(new NodeInstanceEntity());
        
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(result);

        List<NodeInstanceEntity> found = nodeInstanceEntityRepositoryService.findByFlowInstanceId(flowInstanceId);
        
        assertNotNull(found);
        assertEquals(1, found.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        
        List<NodeInstanceEntity> result = new ArrayList<>();
        result.add(new NodeInstanceEntity());
        
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(result);

        List<NodeInstanceEntity> found = nodeInstanceEntityRepositoryService.findByIds(ids);
        
        assertNotNull(found);
        assertEquals(1, found.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int deleted = nodeInstanceEntityRepositoryService.deleteByFlowInstanceId(instanceId);
        
        assertTrue(deleted > 0);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SOME_TYPE;
        
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int deleted = nodeInstanceEntityRepositoryService.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        
        assertTrue(deleted > 0);
    }
    
}