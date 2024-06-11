import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;

    @BeforeAll
    public static void setupBeforeAll() {
        // Initialize mocks, if needed
    }

    @BeforeEach
    public void setup() {
        // Initialize test data, if needed
        NodeInstanceEntityRepository nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        this.nodeInstanceEntityRepository = nodeInstanceEntityRepository;

        nodeInstanceEntity1 = NodeInstanceEntity.builder()
                .id(1L)
                .instanceId(1L)
                .instanceType(FlowNodeType.START_EVENT)
                .flowInstanceId(1L)
                .flowableElementType(FlowableElementType.START_EVENT)
                .build();

        nodeInstanceEntity2 = NodeInstanceEntity.builder()
                .id(2L)
                .instanceId(2L)
                .instanceType(FlowNodeType.END_EVENT)
                .flowInstanceId(1L)
                .flowableElementType(FlowableElementType.END_EVENT)
                .build();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(nodeInstanceEntity1, nodeInstanceEntity2);
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);

        assertThat(result).containsExactly(nodeInstanceEntity1);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(nodeInstanceEntity1, nodeInstanceEntity2);
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertThat(result).containsExactly(nodeInstanceEntity1, nodeInstanceEntity2);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(nodeInstanceEntity1, nodeInstanceEntity2);
        Mockito.when(nodeInstanceEntityRepository.findByIds(Collections.singletonList(1L)))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Collections.singletonList(1L));

        assertThat(result).containsExactly(nodeInstanceEntity1);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int countBeforeDelete = 2;
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L))
                .thenReturnValue(countBeforeDelete);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertThat(result).isEqualTo(countBeforeDelete);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int countBeforeDelete = 2;
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT))
                .thenReturnValue(countBeforeDelete);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

        assertThat(result).isEqualTo(countBeforeDelete);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(nodeInstanceEntity1, nodeInstanceEntity2);
        Mockito.when(nodeInstanceEntityRepository.findAll())
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();

        assertThat(result).containsExactly(nodeInstanceEntity1, nodeInstanceEntity2);
    }

    @Test
    public void testFindAllByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = List.of(nodeInstanceEntity1, nodeInstanceEntity2);
        Mockito.when(nodeInstanceEntityRepository.findAll(Specification.where(NodeInstanceEntitySpecifications.hasFlowInstanceId(flowInstanceId))))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(Specification.where(NodeInstanceEntitySpecifications.hasFlowInstanceId(flowInstanceId)));

        assertThat(result).containsExactly(nodeInstanceEntity1, nodeInstanceEntity2);
    }

    private static Object[] nodeInstanceEntityArguments() {
        return new Object[] {nodeInstanceEntity1, nodeInstanceEntity2};
    }

    @ParameterizedTest
    @MethodSource("nodeInstanceEntityArguments")
    public void testMethodsWithTestAnnotations(@Autowired NodeInstanceEntityRepository nodeInstanceEntityRepository, NodeInstanceEntity nodeInstanceEntity) {
        // Test methods with @Test or other JUnit annotations, if any
    }

}