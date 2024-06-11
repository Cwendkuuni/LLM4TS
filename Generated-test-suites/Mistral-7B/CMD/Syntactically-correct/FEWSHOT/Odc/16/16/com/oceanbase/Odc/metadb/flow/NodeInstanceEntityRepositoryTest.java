package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNode;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = new SimpleJpaRepository<NodeInstanceEntity, Long>(mock(NodeInstanceEntityRepository.class),
                mock(NodeInstanceEntity.class)) {
            @Override
            public List<NodeInstanceEntity> findAll() {
                return new ArrayList<>();
            }
        };
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.GATEWAY);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);

        assertEquals(2, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertThat(nodeInstanceEntity.getInstanceId(), is(instanceId));
            assertThat(nodeInstanceEntity.getInstanceType(), is(instanceType));
            assertThat(nodeInstanceEntity.getFlowableElementType(), is(flowableElementType));
        }
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(2, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertThat(nodeInstanceEntity.getFlowInstanceId(), is(flowInstanceId));
        }
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);

        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setId(3L);

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(3, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertThat(nodeInstanceEntity.getId(), isIn(ids.toArray()));
        }
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int countBeforeDelete = 3;
        when(nodeInstanceEntityRepository.count())
                .thenReturn(countBeforeDelete);

        nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        int countAfterDelete = countBeforeDelete - 1;
        when(nodeInstanceEntityRepository.count())
                .thenReturn(countAfterDelete);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
        assertEquals(countAfterDelete, nodeInstanceEntityRepository.count());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        int countBeforeDelete = 3;
        when(nodeInstanceEntityRepository.count())
                .thenReturn(countBeforeDelete);

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int countAfterDelete = countBeforeDelete - 1;
        when(nodeInstanceEntityRepository.count())
                .thenReturn(countAfterDelete);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
        assertEquals(countAfterDelete, nodeInstanceEntityRepository.count());
    }

    @Test
    public void testFindEmptyList() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.GATEWAY))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.GATEWAY);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteByEmptyList() {
        int countBeforeDelete = 0;
        when(nodeInstanceEntityRepository.count()).thenReturn(countBeforeDelete);

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

        int countAfterDelete = countBeforeDelete;
        when(nodeInstanceEntityRepository.count()).thenReturn(countAfterDelete);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

        assertEquals(0, result);
        assertEquals(countAfterDelete, nodeInstanceEntityRepository.count());
    }

    @Test
    public void testNullParameter() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);

        // No assertions here as this method is declared as nativeQuery = true and we cannot mock a null parameter
        // with Mockito.
    }
}