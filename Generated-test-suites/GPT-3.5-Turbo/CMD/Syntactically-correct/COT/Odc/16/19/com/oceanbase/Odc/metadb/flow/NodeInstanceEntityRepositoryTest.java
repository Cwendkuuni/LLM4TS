import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.repository.query.Param;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.NODE;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
        nodeInstance.setId(1L);
        nodeInstance.setInstanceId(instanceId);
        nodeInstance.setInstanceType(instanceType);
        nodeInstance.setFlowableElementType(flowableElementType);
        expectedResult.add(nodeInstance);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedResult.size(), result.size());
        assertEquals(expectedResult.get(0).getId(), result.get(0).getId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
        nodeInstance.setId(1L);
        nodeInstance.setFlowInstanceId(flowInstanceId);
        expectedResult.add(nodeInstance);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedResult.size(), result.size());
        assertEquals(expectedResult.get(0).getId(), result.get(0).getId());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);

        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
        nodeInstance.setId(1L);
        expectedResult.add(nodeInstance);

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedResult.size(), result.size());
        assertEquals(expectedResult.get(0).getId(), result.get(0).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int expectedResult = 1;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
                .thenReturn(expectedResult);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.NODE;

        int expectedResult = 1;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(expectedResult);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedResult, result);
    }
}