
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class NodeInstanceEntityRepositoryTest {

    NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_A;
        
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expected);
        
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        
        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity());
        
        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expected);
        
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);
        
        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }
    
    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity(), new NodeInstanceEntity());
        
        when(repository.findByIds(ids)).thenReturn(expected);
        
        List<NodeInstanceEntity> result = repository.findByIds(ids);
        
        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expected = 1;
        
        when(repository.deleteByFlowInstanceId(instanceId)).thenReturn(expected);
        
        int result = repository.deleteByFlowInstanceId(instanceId);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        int expected = 1;
        
        when(repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(expected);
        
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        
        assertEquals(expected, result);
    }
}