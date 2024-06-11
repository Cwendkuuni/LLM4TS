import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Given
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.PROCESS);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.GATEWAY);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.TASK);

        // Then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(nodeInstanceEntity1));
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Given
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        // Then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(2));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        int initialCount = nodeInstanceEntityRepository.findAll().size();

        // When
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        // Then
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();
        int deletedCount = initialCount - result.size();
        assertEquals(deletedCount, 2);
    }

    @Test(expected = NullPointerException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceId() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null,
                FlowNodeType.START_EVENT, FlowableElementType.TASK);

        // Then - expected exception
    }

    @Test(expected = NullPointerException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null,
                FlowableElementType.TASK);

        // Then - expected exception
    }

    @Test(expected = NullPointerException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, null);

        // Then - expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithInvalidInstanceType() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, new FlowNodeType("Invalid"),
                FlowableElementType.TASK);

        // Then - expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithInvalidFlowableElementType() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, new FlowableElementType("Invalid"));

        // Then - expected exception
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, FlowNodeType.START_EVENT);

        // Then - expected exception
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);

        // Then - expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithInvalidInstanceType() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, new FlowNodeType("Invalid"));

        // Then - expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByIdsWithNullIds() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.findByIds(null);

        // Then - expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByIdsWithEmptyIds() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.deleteByIds(Collections.emptyList());

        // Then - expected exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullFlowInstanceId() {
        // Given
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

        // When
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);

        // Then - expected exception
    }

}