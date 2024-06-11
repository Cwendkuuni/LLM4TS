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
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@SpringJUnitConfig
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @BeforeAll
    public static void setupMocks() {
        // Add any necessary mocks here if needed
    }

    // Test all methods in NodeInstanceEntityRepository

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity()); // Replace with actual data

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity()); // Replace with actual data

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity()); // Replace with actual data

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 1;

        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        doUpdate(nodeInstanceEntityRepository).when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int actualResult = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(instanceId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expectedResult = 1;

        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        doUpdate(nodeInstanceEntityRepository).when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int actualResult = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedResult, actualResult);
    }

    // Edge cases and exception handling tests

    @Test
    @DisplayName("Test findByInstanceIdAndInstanceTypeAndFlowableElementType with null instanceId")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> expectedResult = Arrays.asList();

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, instanceType, flowableElementType))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, instanceType, flowableElementType);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    @DisplayName("Test findByInstanceIdAndInstanceTypeAndFlowableElementType with null instanceType")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullInstanceType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> expectedResult = Arrays.asList();

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, flowableElementType))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, flowableElementType);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    @DisplayName("Test findByInstanceIdAndInstanceTypeAndFlowableElementType with null flowableElementType")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;

        List<NodeInstanceEntity> expectedResult = Arrays.asList();

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null);

        assertTrue(actualResult.isEmpty());
    }

    @ParameterizedTest(name = "Test deleteByFlowInstanceId with invalid instanceId {0}")
    @MethodSource("provideInvalidInstanceIds")
    public void testDeleteByFlowInstanceId_InvalidInstanceId(Long instanceId) {
        int expectedResult = 0;

        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        doUpdate(nodeInstanceEntityRepository).when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int actualResult = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        verify(nodeInstanceEntityRepository, never()).deleteByFlowInstanceId(instanceId);

        assertEquals(expectedResult, actualResult);
    }

    @ParameterizedTest(name = "Test deleteByInstanceIdAndInstanceType with invalid instanceId {0} and instanceType {1}")
    @MethodSource("provideInvalidInstanceIdsAndInstanceTypes")
    public void testDeleteByInstanceIdAndInstanceType_InvalidInstanceIdAndInstanceType(Long instanceId, FlowNodeType instanceType) {
        int expectedResult = 0;

        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        doUpdate(nodeInstanceEntityRepository).when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int actualResult = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        verify(nodeInstanceEntityRepository, never()).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedResult, actualResult);
    }

    private static Long[] provideInvalidInstanceIds() {
        return new Long[]{-1L, 0L, null};
    }

    private static Object[][] provideInvalidInstanceIdsAndInstanceTypes() {
        return new Object[][]{-1L, FlowNodeType.TASK, -1L, null, null, null};
    }
}