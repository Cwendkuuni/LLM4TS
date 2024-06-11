import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

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
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(entity1, entity2));
        
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.TYPE_X);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(repository.findByFlowInstanceId(anyLong()))
                .thenReturn(Arrays.asList(entity1));
        
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);

        assertEquals(1, result.size());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        when(repository.findByIds(ids))
                .thenReturn(Arrays.asList(entity1, entity2));
        
        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(repository.deleteByFlowInstanceId(anyLong()))
                .thenReturn(1);
        
        int result = repository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(repository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class)))
                .thenReturn(1);
        
        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);

        assertEquals(1, result);
    }

}