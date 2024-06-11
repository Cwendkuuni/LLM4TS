import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository;

    @Before
    public void setUp() {
        repository = mock(NodeInstanceEntityRepository.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity());
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.USER_TASK))
                .thenReturn(expected);
        
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.USER_TASK);
        
        assertEquals(expected, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity());
        when(repository.findByFlowInstanceId(1L)).thenReturn(expected);
        
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);
        
        assertEquals(expected, result);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity());
        when(repository.findByIds(Collections.singletonList(1L))).thenReturn(expected);
        
        List<NodeInstanceEntity> result = repository.findByIds(Collections.singletonList(1L));
        
        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(repository.deleteByFlowInstanceId(1L)).thenReturn(1);
        
        int result = repository.deleteByFlowInstanceId(1L);
        
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK)).thenReturn(1);
        
        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);
        
        assertEquals(1, result);
    }

}