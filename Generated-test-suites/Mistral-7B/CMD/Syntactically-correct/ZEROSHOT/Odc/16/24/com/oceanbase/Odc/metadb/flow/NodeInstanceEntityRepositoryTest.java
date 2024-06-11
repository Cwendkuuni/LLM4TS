import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowInstance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK_NODE;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        // Given
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, new FlowInstance(1L)),
                new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType, new FlowInstance(2L))
        );

        // When
        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        // Given
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null, new FlowInstance(1L)),
                new NodeInstanceEntity(2L, null, null, null, new FlowInstance(1L))
        );

        // When
        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Then
        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // Given
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null, null),
                new NodeInstanceEntity(2L, null, null, null, null),
                new NodeInstanceEntity(3L, null, null, null, null)
        );

        // When
        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByIds(ids);

        // Then
        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        // Given
        int expectedDeletedCount = 2;
        List<NodeInstanceEntity> nodesToDelete = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, null, null, null),
                new NodeInstanceEntity(2L, instanceId, null, null, null)
        );

        // When
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        // Then
        assertEquals(expectedDeletedCount, actualDeletedCount);
        verify(nodeInstanceEntityRepository).deleteAll(any(Iterable.class));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK_NODE;

        // Given
        int expectedDeletedCount = 2;
        List<NodeInstanceEntity> nodesToDelete = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, null, null),
                new NodeInstanceEntity(2L, instanceId, instanceType, null, null)
        );

        // When
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        assertEquals(expectedDeletedCount, actualDeletedCount);
        verify(nodeInstanceEntityRepository).deleteAll(any(Iterable.class));
    }

    @Test
    public void testFindAll() {
        // Given
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null, null),
                new NodeInstanceEntity(2L, null, null, null, null),
                new NodeInstanceEntity(3L, null, null, null, null)
        );

        // When
        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findAll();

        // Then
        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testFindAllByFlowInstanceId() {
        Long flowInstanceId = 1L;

        // Given
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null, new FlowInstance(1L)),
                new NodeInstanceEntity(2L, null, null, null, new FlowInstance(1L))
        );

        // When
        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findAll(new PageRequest(0, 10, new Sort("flowInstanceId")))
                .getContent();

        // Then
        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testSave() {
        // Given
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(null, null, null, null, null);

        // When
        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        // Then
        assertNotNull(savedNodeInstanceEntity.getId());
    }

    @Test
    public void testCountByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;

        // When
        long expectedCount = 2;
        long actualCount = nodeInstanceEntityRepository.countByFlowInstanceId(flowInstanceId);

        // Then
        assertEquals(expectedCount, actualCount);
    }
}