import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity nodeInstanceEntity;

    @BeforeEach
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        nodeInstanceEntity = new NodeInstanceEntity();
    }

    // Test method: findByInstanceIdAndInstanceTypeAndFlowableElementType
    @ParameterizedTest
    @MethodSource("provideTestCasesForFindByInstanceIdAndInstanceTypeAndFlowableElementType")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType(Long instanceId, FlowNodeType instanceType,
            FlowableElementType flowableElementType, List<NodeInstanceEntity> expectedResult) {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(expectedResult);
        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);
        assertEquals(expectedResult, actualResult);
    }

    private static Stream<Arguments> provideTestCasesForFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        return Stream.of(
                Arguments.arguments(1L, FlowNodeType.START, FlowableElementType.TASK, new ArrayList<>()),
                Arguments.arguments(1L, FlowNodeType.END, FlowableElementType.TASK, new ArrayList<>()),
                Arguments.arguments(1L, FlowNodeType.GATEWAY, FlowableElementType.SEQUENTIAL_GATEWAY, new ArrayList<>()),
                Arguments.arguments(1L, FlowNodeType.GATEWAY, FlowableElementType.EXCLUSIVE_GATEWAY, new ArrayList<>()),
                Arguments.arguments(1L, FlowNodeType.GATEWAY, FlowableElementType.PARALLEL_GATEWAY, new ArrayList<>()),
                Arguments.arguments(1L, FlowNodeType.GATEWAY, FlowableElementType.INCLUSIVE_GATEWAY, new ArrayList<>())
        );
    }

    // Test method: findByFlowInstanceId
    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(List.of(nodeInstanceEntity));
        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(1, actualResult.size());
    }

    // Test method: findByIds
    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByIds(Collections.singletonList(1L))).thenReturn(nodeInstanceEntities);
        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByIds(Collections.singletonList(1L));
        assertEquals(1, actualResult.size());
    }

    // Test method: deleteByFlowInstanceId
    @Test
    public void testDeleteByFlowInstanceId() {
        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(1L);
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(anyLong());
    }

    // Test method: deleteByInstanceIdAndInstanceType
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class));
    }
}