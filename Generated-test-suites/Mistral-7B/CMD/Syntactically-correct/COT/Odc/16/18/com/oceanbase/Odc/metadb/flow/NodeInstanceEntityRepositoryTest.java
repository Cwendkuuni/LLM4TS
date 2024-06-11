import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig(classes = NodeInstanceEntityRepository.class)
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @BeforeEach
    public void setup() {
        // Set up mocks if needed
    }

    // Test case for findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedResult, result);
    }

    // Test case for findByFlowInstanceId method
    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedResult, result);
    }

    // Test case for findByIds method
    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedResult, result);
    }

    // Test case for deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 2;

        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedResult, result);
    }

    // Test case for deleteByInstanceIdAndInstanceType method
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedResult = 2;

        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedResult, result);
    }

    // Test edge cases and exception handling
    @DisplayName("Test findByInstanceIdAndInstanceTypeAndFlowableElementType with null parameters")
    @ParameterizedTest
    @MethodSource("provideNullParameters")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullParameters(@Param("instanceId") Long instanceId,
            @Param("instanceType") FlowNodeType instanceType,
            @Param("flowableElementType") FlowableElementType flowableElementType) {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType));
    }

    // Test data source method with empty result set
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithEmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedResult = Collections.emptyList();

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(result.isEmpty());
    }

    // Test data source method with no records found
    @Test
    public void testFindByFlowInstanceIdWithNoRecordsFound() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Collections.emptyList();

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertTrue(result.isEmpty());
    }

    // Test deleteByFlowInstanceId with no records to delete
    @Test
    public void testDeleteByFlowInstanceIdWithNoRecordsToDelete() {
        Long instanceId = 1L;
        int expectedResult = 0;

        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedResult, result);
    }

    // Test deleteByInstanceIdAndInstanceType with no records to delete
    @Test
    public void testDeleteByInstanceIdAndInstanceTypeWithNoRecordsToDelete() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedResult = 0;

        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedResult, result);
    }

    // Test data source method with invalid IDs
    @DisplayName("Test findByIds with invalid IDs")
    @ParameterizedTest
    @MethodSource("provideInvalidIds")
    public void testFindByIdsWithInvalidIds(@Param("ids") Collection<Long> ids) {
        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenThrow(org.springframework.dao.InvalidDataAccessApiUsageException.class);

        assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> nodeInstanceEntityRepository.findByIds(ids));
    }

    // Data provider for null parameters test
    static Object[][] provideNullParameters() {
        return new Object[][]{
                {null, null, null},
                {1L, null, null},
                {null, FlowNodeType.START, null},
                {1L, FlowNodeType.START, null},
                {1L, null, FlowableElementType.PROCESS},
                {1L, FlowNodeType.START, null}
        };
    }

    // Data provider for invalid IDs test
    static Collection<Long> provideInvalidIds() {
        return Arrays.asList(-1L, Long.MAX_VALUE);
    }
}