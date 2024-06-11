
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() throws Exception {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.SEQUENCE_FLOW)).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        when(nodeInstanceEntityRepository.findByIds(Collections.singleton(1L))).thenReturn(Arrays.asList(new NodeInstanceEntity()));
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT)).thenReturn(1);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.SEQUENCE_FLOW);
        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.SEQUENCE_FLOW);
    }

    @Test
    public void testFindByFlowInstanceId() {
        nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        verify(nodeInstanceEntityRepository).findByFlowInstanceId(1L);
    }

    @Test
    public void testFindByIds() {
        nodeInstanceEntityRepository.findByIds(Collections.singleton(1L));
        verify(nodeInstanceEntityRepository).findByIds(Collections.singleton(1L));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
        verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
    }
}
