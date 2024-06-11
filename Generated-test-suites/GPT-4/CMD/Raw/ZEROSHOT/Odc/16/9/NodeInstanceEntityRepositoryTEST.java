
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {NodeInstanceEntityRepository.class})
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.BOOLEAN;
        FlowableElementType flowableElementType = FlowableElementType.BINARY;
        List<NodeInstanceEntity> expected = new ArrayList<>();
        expected.add(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expected = new ArrayList<>();
        expected.add(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expected = new ArrayList<>();
        expected.add(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.BOOLEAN;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(1, result);
    }
}
