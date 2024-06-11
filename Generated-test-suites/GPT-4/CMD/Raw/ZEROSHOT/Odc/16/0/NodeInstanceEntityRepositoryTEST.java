
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class NodeInstanceEntityRepositoryTest {
    
    @Mock
    NodeInstanceEntityRepository nodeInstanceEntityRepositoryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(nodeInstanceEntity);

        when(nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any()))
        .thenReturn(nodeInstanceEntities);

        nodeInstanceEntityRepositoryMock.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.USER_TASK, FlowableElementType.BPMN_ELEMENT);
        
        verify(nodeInstanceEntityRepositoryMock, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any());
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(nodeInstanceEntity);
        
        when(nodeInstanceEntityRepositoryMock.findByFlowInstanceId(anyLong())).thenReturn(nodeInstanceEntities);
        
        nodeInstanceEntityRepositoryMock.findByFlowInstanceId(1L);
        
        verify(nodeInstanceEntityRepositoryMock, times(1)).findByFlowInstanceId(anyLong());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(nodeInstanceEntity);
        
        when(nodeInstanceEntityRepositoryMock.findByIds(any())).thenReturn(nodeInstanceEntities);
        
        nodeInstanceEntityRepositoryMock.findByIds(Arrays.asList(1L, 2L));
        
        verify(nodeInstanceEntityRepositoryMock, times(1)).findByIds(any());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(anyLong())).thenReturn(1);
        
        nodeInstanceEntityRepositoryMock.deleteByFlowInstanceId(1L);
        
        verify(nodeInstanceEntityRepositoryMock, times(1)).deleteByFlowInstanceId(anyLong());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(anyLong(), any())).thenReturn(1);
        
        nodeInstanceEntityRepositoryMock.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.USER_TASK);
        
        verify(nodeInstanceEntityRepositoryMock, times(1)).deleteByInstanceIdAndInstanceType(anyLong(), any());
    }
    
}
