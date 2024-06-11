
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {
    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    
    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    private Long instanceId = 1L;
    private FlowNodeType instanceType = FlowNodeType.START;
    private FlowableElementType flowableElementType = FlowableElementType.TASK;

    @Before
    public void setUp() throws Exception {
        nodeInstanceEntity = new NodeInstanceEntity();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(new ArrayList<>());
        assertEquals(0, nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType).size());
        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(new ArrayList<>());
        assertEquals(0, nodeInstanceEntityRepository.findByFlowInstanceId(anyLong()).size());
        verify(nodeInstanceEntityRepository).findByFlowInstanceId(anyLong());
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(anyList())).thenReturn(new ArrayList<>());
        assertEquals(0, nodeInstanceEntityRepository.findByIds(anyList()).size());
        verify(nodeInstanceEntityRepository).findByIds(anyList());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, result);
        verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(0, result);
        verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}
