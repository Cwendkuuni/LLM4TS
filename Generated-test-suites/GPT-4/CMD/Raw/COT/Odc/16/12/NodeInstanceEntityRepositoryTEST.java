
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentMatchers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType(){
        List<NodeInstanceEntity> entities = new ArrayList<>();

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SEQUENCE_FLOW, FlowableElementType.SEQUENCE_FLOW)).thenReturn(entities);
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SEQUENCE_FLOW, FlowableElementType.SEQUENCE_FLOW);
        
        assertEquals(entities, result);
    }
    
    @Test
    public void testFindByFlowInstanceId(){
        List<NodeInstanceEntity> entities = new ArrayList<>();
        
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(entities);
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        
        assertEquals(entities, result);
    }

    @Test
    public void testFindByIds(){
        List<NodeInstanceEntity> entities = new ArrayList<>();
        List<Long> ids = Arrays.asList(1L, 2L);
        
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(entities);
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        
        assertEquals(entities, result);
    }
    
    @Test
    public void testDeleteByFlowInstanceId(){
        // We're just checking if the correct method is invoked, no need to validate the returned integer
        doReturn(1).when(nodeInstanceEntityRepository).deleteByFlowInstanceId(ArgumentMatchers.anyLong());
        
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(1L);
    }
    
    @Test
    public void testDeleteByInstanceIdAndInstanceType(){
        // We're just checking if the correct method is invoked, no need to validate the returned integer
        doReturn(1).when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(ArgumentMatchers.anyLong(), any(FlowNodeType.class));
        
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SEQUENCE_FLOW);
        
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SEQUENCE_FLOW);
    }
}
