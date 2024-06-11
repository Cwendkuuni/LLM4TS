import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository repository;

    private Long instanceId = 1L;
    private Long flowInstanceId = 1L;
    private FlowNodeType instanceType = FlowNodeType.START;
    private FlowableElementType flowableElementType = FlowableElementType.SEQUENCE;
    private Long id = 1L;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // ...
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        // ...
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullResult() {
        // ...
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Given
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(1L, instanceType, flowableElementType);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(2L, instanceType, flowableElementType);
        Mockito.when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(Arrays.asList(nodeInstanceEntity1));

        // When
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        // Then
        assertFalse(result.isEmpty());
        assertNotNull(result.get(0));
        assertNull(result.get(0).getId());
        assertEquals(flowInstanceId, result.get(0).getFlowInstanceId());
        assertEquals(instanceType, result.get(0).getInstanceType());
        assertEquals(flowableElementType, result.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByIds() {
        // Given
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(id, instanceType, flowableElementType);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(id + 1, instanceType, flowableElementType);
        Mockito.when(repository.findByIds(Arrays.asList(id, id + 1))).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        // When
        List<NodeInstanceEntity> result = repository.findByIds(Arrays.asList(id, id + 1));

        // Then
        assertFalse(result.isEmpty());
        assertNotNull(result.get(0));
        assertNull(result.get(0).getId());
        assertEquals(id, result.get(0).getId());
        assertEquals(instanceType, result.get(0).getInstanceType());
        assertEquals(flowableElementType, result.get(0).getFlowableElementType());

        assertFalse(result.isEmpty());
        assertNotNull(result.get(1));
        assertNull(result.get(1).getId());
        assertEquals(id + 1, result.get(1).getId());
        assertEquals(instanceType, result.get(1).getInstanceType());
        assertEquals(flowableElementType, result.get(1).getFlowableElementType());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(1L, instanceType, flowableElementType);
        Mockito.doNothing().when(repository).deleteByFlowInstanceId(flowInstanceId);

        // When
        int result = repository.deleteByFlowInstanceId(flowInstanceId);

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Given
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(1L, instanceType, flowableElementType);
        Mockito.doNothing().when(repository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // When
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullIds() {
        // Given
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        // When
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        assertFalse(result.isEmpty());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        // Given
        Mockito.when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        // When
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        assertFalse(result.isEmpty());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_NullIds() {
        // Given
        Mockito.doThrow(new RuntimeException("Failed to delete NodeInstanceEntity")).when(repository)
                .deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // When & Then
        assertThrows(RuntimeException.class, () -> repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_EmptyResult() {
        // Given
        Mockito.doNothing().when(repository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // When
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        assertEquals(0, result);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Add test cases for other methods here if needed
}