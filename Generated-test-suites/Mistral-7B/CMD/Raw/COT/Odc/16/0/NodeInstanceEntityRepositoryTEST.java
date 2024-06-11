import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ContextConfiguration(classes = NodeInstanceEntityRepository.class)
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @BeforeAll
    public static void beforeAll() {
        // Initialize mocks
    }

    @AfterAll
    public static void afterAll() {
        // Perform cleanup if needed
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(Mockito.mock(NodeInstanceEntity.class));

        given(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .willReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(Mockito.mock(NodeInstanceEntity.class), Mockito.mock(NodeInstanceEntity.class));

        given(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).willReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertThat(result).hasSize(expectedResult.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(Mockito.mock(NodeInstanceEntity.class), Mockito.mock(NodeInstanceEntity.class), Mockito.mock(NodeInstanceEntity.class));

        given(nodeInstanceEntityRepository.findByIds(ids)).willReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertThat(result).hasSize(expectedResult.size());
    }

    @Test
    @ParameterizedTest
    @MethodSource("deleteByFlowInstanceIdDataProvider")
    public void testDeleteByFlowInstanceId(@Param("instanceId") Long instanceId, @Param("expectedResult") int expectedResult) {
        given(nodeInstanceEntityRepository.countByFlowInstanceId(instanceId)).willReturn((long) 3);

        int actualResult = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @ParameterizedTest
    @MethodSource("deleteByInstanceIdAndInstanceTypeDataProvider")
    public void testDeleteByInstanceIdAndInstanceType(@Param("instanceId") Long instanceId, @Param("instanceType") FlowNodeType instanceType,
                                                      @Param("expectedResult") int expectedResult) {
        given(nodeInstanceEntityRepository.countByInstanceIdAndInstanceType(instanceId, instanceType)).willReturn((long) 3);

        int actualResult = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    // Add test methods for other methods if necessary

    @Test
    public void testCountAll() {
        long expectedResult = 10;
        given(nodeInstanceEntityRepository.countAll()).willReturn(expectedResult);

        long actualResult = nodeInstanceEntityRepository.countAll();

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    // Add other test methods as needed

    private static Object[][] deleteByFlowInstanceIdDataProvider() {
        return new Object[][]{
                {1L, 3},
                {-1L, 0}
        };
    }

    private static Object[][] deleteByInstanceIdAndInstanceTypeDataProvider() {
        return new Object[][]{
                {1L, FlowNodeType.START, 2},
                {-1L, FlowNodeType.END, 0}
        };
    }
}