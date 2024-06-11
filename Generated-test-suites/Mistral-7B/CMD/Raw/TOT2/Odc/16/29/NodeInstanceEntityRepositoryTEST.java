import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNode;

public class NodeInstanceEntityRepositoryTest {

    private static final Long INSTANCE_ID = 1L;
    private static final Long FLOW_INSTANCE_ID = 2L;
    private static final Long ID = 3L;
    private static final FlowNodeType FLOW_NODE_TYPE = mock(FlowNodeType.class);
    private static final FlowableElementType FLOWABLE_ELEMENT_TYPE = mock(FlowableElementType.class);

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, FLOW_NODE_TYPE, FLOWABLE_ELEMENT_TYPE))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, FLOW_NODE_TYPE, FLOWABLE_ELEMENT_TYPE);

        assertNotNull(result);
        assertNotNull(result.get(0));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyList() {
        NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, FLOW_NODE_TYPE, FLOWABLE_ELEMENT_TYPE))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, FLOW_NODE_TYPE, FLOWABLE_ELEMENT_TYPE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullArguments() {
        NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_InvalidArguments() {
        NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, null, null))
                .thenThrow(IllegalArgumentException.class);

        try {
            repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyPage() {
        NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);
        Pageable pageable = mock(Pageable.class);

        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        Page<NodeInstanceEntity> result = repository.findAll(pageable);

        assertEquals(0, result.getSize());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_Exception() {
        NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);

        doThrow(new RuntimeException("Test Exception")).when(repository)
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, FLOW_NODE_TYPE, FLOWABLE_ELEMENT_TYPE);

        try {
            repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, FLOW_NODE_TYPE, FLOWABLE_ELEMENT_TYPE);
            fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository() {
            @Override
            public List<NodeInstanceEntity> findByFlowInstanceId(Long flowInstanceId) {
                NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
                nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
                return Arrays.asList(nodeInstanceEntity);
            }
        };

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(FLOW_INSTANCE_ID);

        assertNotNull(result);
        assertNotNull(result.get(0));
        assertEquals(FLOW_INSTANCE_ID, result.get(0).getFlowInstanceId());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        Long id1 = 1L;
        Long id2 = 2L;

        when(repository.findByIds(Arrays.asList(id1, id2))).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = repository.findByIds(Arrays.asList(id1, id2));

        assertNotNull(result);
        assertNotNull(result.get(0));
        assertEquals(id1, result.get(0).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);

        int countBeforeDelete = 3;
        when(repository.count()).thenReturn(countBeforeDelete);

        repository.deleteByFlowInstanceId(FLOW_INSTANCE_ID);

        verify(repository, times(1)).deleteInBatch(argThat(nodeInstanceEntity -> nodeInstanceEntity.getFlowInstanceId()
                .equals(FLOW_INSTANCE_ID)));

        int countAfterDelete = countBeforeDelete - 1;
        when(repository.count()).thenReturn(countAfterDelete);

        assertEquals(countAfterDelete, repository.count());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntityRepository repository = mock(NodeInstanceEntityRepository.class);

        int countBeforeDelete = 3;
        when(repository.count()).thenReturn(countBeforeDelete);

        repository.deleteByInstanceIdAndInstanceType(ID, FLOW_NODE_TYPE);

        verify(repository, times(1)).delete(argThat(nodeInstanceEntity -> nodeInstanceEntity.getId().equals(ID)
                && nodeInstanceEntity.getInstanceType().equals(FLOW_NODE_TYPE)));

        int countAfterDelete = countBeforeDelete - 1;
        when(repository.count()).thenReturn(countAfterDelete);

        assertEquals(countAfterDelete, repository.count());
    }
}