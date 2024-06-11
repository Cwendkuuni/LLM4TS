

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any())).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SOME_TYPE, FlowableElementType.SOME_TYPE);
        Assert.assertEquals(expected, actual);
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        Assert.assertEquals(expected, actual);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(anyLong());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByIds(any())).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L));
        Assert.assertEquals(expected, actual);
        verify(nodeInstanceEntityRepository, times(1)).findByIds(any());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int expected = 1;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(expected);
        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        Assert.assertEquals(expected, actual);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(anyLong());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int expected = 1;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any())).thenReturn(expected);
        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SOME_TYPE);
        Assert.assertEquals(expected, actual);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(anyLong(), any());
    }
}
