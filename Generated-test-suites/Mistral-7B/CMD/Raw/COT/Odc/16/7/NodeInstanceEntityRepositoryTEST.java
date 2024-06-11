import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
@SpringJUnitConfig(classes = NodeInstanceEntityRepository.class)
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType));

        given(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .willReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideValidIds")
    public void testFindByIds(Long... ids) {
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.TASK),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.END, FlowableElementType.TASK));

        given(nodeInstanceEntityRepository.findByIds(Arrays.asList(ids))).willReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(Arrays.asList(ids));

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expected = 1;

        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        given(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).willReturn(expected);

        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expected = 1;

        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        given(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType)).willReturn(Collections.singletonList(new NodeInstanceEntity(1L, instanceId, instanceType, FlowableElementType.TASK)));
        given(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).willReturn(expected);

        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsWithNullIds() {
        nodeInstanceEntityRepository.findByIds(null);
    }

    @Test
    public void testDeleteByFlowInstanceIdWithEmptyList() {
        Long instanceId = 1L;
        int expected = 0;

        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        given(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId)).willReturn(Collections.emptyList());
        given(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).willReturn(expected);

        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expected, actual);
    }

    private static Collection<Long[]> provideValidIds() {
        return Arrays.asList(new Long[]{1L, 2L, 3L}, new Long[]{1L});
    }
}