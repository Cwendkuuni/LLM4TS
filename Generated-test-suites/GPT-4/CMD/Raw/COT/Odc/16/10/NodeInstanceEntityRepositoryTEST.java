
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.any;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity node = new NodeInstanceEntity();
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any())).thenReturn(Collections.singletonList(node));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.HUMAN);
        assertEquals(result.size(), 1);
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any());
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity node = new NodeInstanceEntity();
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(Collections.singletonList(node));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(result.size(), 1);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(anyLong());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity node = new NodeInstanceEntity();
        when(nodeInstanceEntityRepository.findByIds(any())).thenReturn(Collections.singletonList(node));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L));
        assertEquals(result.size(), 1);
        verify(nodeInstanceEntityRepository, times(1)).findByIds(any());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(result, 1);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(anyLong());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any())).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(result, 1);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(anyLong(), any());
    }
}
