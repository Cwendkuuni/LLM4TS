import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.EVENT, FlowableElementType.ACTIVITY);
        Assert.assertTrue("Test findByInstanceIdAndInstanceTypeAndFlowableElementType has failed", !result.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong()))
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        Assert.assertTrue("Test findByFlowInstanceId has failed", !result.isEmpty());
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(any()))
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        Assert.assertTrue("Test findByIds has failed", !result.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong()))
                .thenReturn(1);  // 1 indicates a successful delete operation

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        Assert.assertEquals("Test deleteByFlowInstanceId has failed", 1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class)))
                .thenReturn(1);  // 1 indicates a successful delete operation

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.EVENT);
        Assert.assertEquals("Test deleteByInstanceIdAndInstanceType has failed", 1, result);
    }
}