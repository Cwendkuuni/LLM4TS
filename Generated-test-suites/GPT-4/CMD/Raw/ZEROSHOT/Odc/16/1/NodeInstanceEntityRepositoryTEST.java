
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @MockBean
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(Collections.singletonList(nodeInstanceEntity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(result.size(), 1);
        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        Long flowInstanceId = 1L;
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(Collections.singletonList(nodeInstanceEntity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(result.size(), 1);
        verify(nodeInstanceEntityRepository).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        Long id1 = 1L;
        when(nodeInstanceEntityRepository.findByIds(Arrays.asList(id1))).thenReturn(Collections.singletonList(nodeInstanceEntity));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(id1));
        assertEquals(result.size(), 1);
        verify(nodeInstanceEntityRepository).findByIds(Arrays.asList(id1));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(result, 1);
        verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(result, 1);
        verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}
