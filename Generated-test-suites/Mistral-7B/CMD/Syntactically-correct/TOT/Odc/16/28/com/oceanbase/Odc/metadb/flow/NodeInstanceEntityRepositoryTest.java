package com.oceanbase.odc.metadb.flow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeInstance;
import com.oceanbase.odc.service.flow.model.FlowInstance;

import com.oceanbase.odc.metadb.flow.entity.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.NodeInstanceEntityRepository;

/**
 * Test for {@link NodeInstanceEntityRepository}
 *
 * @author yh263208
 * @date 2022-02-09 14:18
 * @since ODC_release_3.3.0
 */
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @InjectMocks
    private NodeInstanceEntityRepository testee;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testee = new NodeInstanceEntityRepository() {
            @Override
            public NodeInstanceEntityRepository repository() {
                return repository;
            }
        };
    }

    @After
    public void tearDown() {
        repository = mock(NodeInstanceEntityRepository.class);
        testee = new NodeInstanceEntityRepository() {
            @Override
            public NodeInstanceEntityRepository repository() {
                return repository;
            }
        };
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        Long nodeInstanceId = 2L;
        FlowNodeType instanceType = FlowNodeType.GATEWAY;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(nodeInstanceId);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = testee.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType);

        ArgumentCaptor<List<NodeInstanceEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType);
        assertEquals(nodeInstanceId, result.get(0).getId());
        assertEquals(instanceId, result.get(0).getInstanceId());
        assertEquals(instanceType, result.get(0).getInstanceType());
        assertEquals(flowableElementType, result.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = testee.findByFlowInstanceId(flowInstanceId);

        assertEquals(nodeInstanceEntities.size(), result.size());
        for (NodeInstanceEntity entity : nodeInstanceEntities) {
            assertEquals(entity.getId(), result.get(result.indexOf(entity)).getId());
            assertEquals(entity.getInstanceId(), result.get(result.indexOf(entity)).getInstanceId());
            assertEquals(entity.getInstanceType(), result.get(result.indexOf(entity)).getInstanceType());
            assertEquals(entity.getFlowableElementType(), result.get(result.indexOf(entity)).getFlowableElementType());
        }
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(repository.findByIds(Arrays.asList(id1, id2))).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = testee.findByIds(Arrays.asList(id1, id2));

        assertEquals(nodeInstanceEntities.size(), result.size());
        for (NodeInstanceEntity entity : nodeInstanceEntities) {
            assertEquals(entity.getId(), result.get(result.indexOf(entity)).getId());
            assertEquals(entity.getInstanceId(), result.get(result.indexOf(entity)).getInstanceId());
            assertEquals(entity.getInstanceType(), result.get(result.indexOf(entity)).getInstanceType());
            assertEquals(entity.getFlowableElementType(), result.get(result.indexOf(entity)).getFlowableElementType());
        }
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int countBeforeDelete = 3;

        when(repository.countAllByFlowInstanceId(instanceId)).thenReturn(countBeforeDelete);

        testee.deleteByFlowInstanceId(instanceId);

        verify(repository).deleteAllByFlowInstanceId(instanceId);
        verify(repository, times(countBeforeDelete)).delete(any(NodeInstanceEntity.class));
        assertEquals(0, testee.countAllByFlowInstanceId(instanceId));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        Long nodeInstanceId = 2L;
        int countBeforeDelete = 3;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(nodeInstanceId);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(FlowNodeType.GATEWAY);

        when(repository.countAllByInstanceIdAndInstanceType(instanceId, FlowNodeType.GATEWAY)).thenReturn(countBeforeDelete);

        testee.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.GATEWAY);

        verify(repository).deleteAllByInstanceIdAndInstanceType(instanceId, FlowNodeType.GATEWAY);
        verify(repository, times(countBeforeDelete)).delete(any(NodeInstanceEntity.class));
        assertEquals(0, testee.countAllByInstanceIdAndInstanceType(instanceId, FlowNodeType.GATEWAY));
        assertNull(testee.findById(nodeInstanceId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceId() {
        testee.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, FlowNodeType.GATEWAY,
                FlowableElementType.TASK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        testee.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null,
                FlowableElementType.TASK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        testee.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.GATEWAY, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullFlowInstanceId() {
        testee.deleteByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        testee.deleteByInstanceIdAndInstanceType(null, FlowNodeType.GATEWAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        testee.deleteByInstanceIdAndInstanceType(1L, null);
    }
}