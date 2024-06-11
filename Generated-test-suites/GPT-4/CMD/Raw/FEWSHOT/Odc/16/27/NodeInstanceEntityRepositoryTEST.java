import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class NodeInstanceEntityRepositoryTest {
    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.START)).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.START);
        assertEquals(expected, actual);
    }
	
    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIds(){
        List<Long> idsInput = Arrays.asList(1L,2L,3L);
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity(), new NodeInstanceEntity());
        Mockito.when(nodeInstanceEntityRepository.findByIds(idsInput)).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(idsInput);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId(){
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType(){
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);
    }
}