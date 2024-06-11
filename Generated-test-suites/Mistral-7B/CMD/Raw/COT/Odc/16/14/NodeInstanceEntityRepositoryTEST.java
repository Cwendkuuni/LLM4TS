import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        // Add your assertions here
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        // Add your assertions here
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        // Add your assertions here
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        // Add your assertions here
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        // Add your assertions here
    }

    @DisplayName("Test finding node instances with null parameters")
    @ParameterizedTest
    @MethodSource("testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNulls")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNulls(@Param("instanceId") Long instanceId,
                                                                               @Param("instanceType") FlowNodeType instanceType,
                                                                               @Param("flowableElementType") FlowableElementType flowableElementType) {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        // Add your assertions here
    }

    @DisplayName("Test deleting node instances with null instanceId")
    @ParameterizedTest
    @MethodSource("testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId")
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId(@Param("instanceId") Long instanceId,
                                                                      @Param("instanceType") FlowNodeType instanceType) {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        // Add your assertions here
    }

    static Object[][] testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNulls() {
        return new Object[][]{
                arguments(null, FlowNodeType.START, FlowableElementType.TASK),
                arguments(1L, null, FlowableElementType.TASK),
                arguments(1L, FlowNodeType.START, null),
                arguments(null, null, FlowableElementType.TASK),
                arguments(null, FlowNodeType.START, null),
                arguments(1L, null, null)
        };
    }

    static Object[][] testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        return new Object[][]{
                arguments(null, FlowNodeType.START),
                arguments(1L, null)
        };
    }

    @AfterEach
    public void tearDown() {
        // Add your cleanup code here, if necessary
    }

}