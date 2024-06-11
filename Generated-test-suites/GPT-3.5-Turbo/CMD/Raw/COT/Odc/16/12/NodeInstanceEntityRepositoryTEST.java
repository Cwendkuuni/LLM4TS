
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Spy
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expected = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(any(Long.class),
                any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(expected);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.ELEMENT_A);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expected = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByFlowInstanceId(any(Long.class))).thenReturn(expected);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> expected = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByIds(any(Collection.class))).thenReturn(expected);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(any(Long.class))).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(any(Long.class), any(FlowNodeType.class))).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);

        assertEquals(1, result);
    }
}
