package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityId;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private FlowNodeType instanceType;
    private FlowableElementType flowableElementType;
    private Long instanceId = 1L;
    private Long flowInstanceId = 1L;
    private Long anotherInstanceId = 2L;
    private Collection<Long> ids;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        instanceType = mock(FlowNodeType.class);
        flowableElementType = mock(FlowableElementType.class);
        ids = Collections.singletonList(instanceId);
    }

    @Test
    @Transactional
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, instanceType,
                flowableElementType)).thenReturn(Collections.singletonList(nodeInstanceEntity));

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, instanceType, flowableElementType);

        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(Collections.singletonList(nodeInstanceEntity));

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(anotherInstanceId);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(List.of(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        int countBeforeDelete = 2;
        when(nodeInstanceEntityRepository.count()).thenReturn(countBeforeDelete);

        nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        verify(nodeInstanceEntityRepository).deleteAllInBatch();
        verify(nodeInstanceEntityRepository).flush();

        int countAfterDelete = countBeforeDelete - 1;
        when(nodeInstanceEntityRepository.count()).thenReturn(countAfterDelete);

        assertEquals(countAfterDelete, nodeInstanceEntityRepository.count());
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        int countBeforeDelete = 2;
        when(nodeInstanceEntityRepository.count()).thenReturn(countBeforeDelete);

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        verify(nodeInstanceEntityRepository).deleteAllInBatch();
        verify(nodeInstanceEntityRepository).flush();

        int countAfterDelete = countBeforeDelete - 1;
        when(nodeInstanceEntityRepository.count()).thenReturn(countAfterDelete);

        assertEquals(countAfterDelete, nodeInstanceEntityRepository.count());
    }
}