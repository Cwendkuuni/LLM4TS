package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType));
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertFalse(actualNodeInstanceEntities.isEmpty());
        assertTrue(actualNodeInstanceEntities.contains(nodeInstanceEntities.get(0)));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(1L, null, null, null),
                new NodeInstanceEntity(2L, flowInstanceId, null, null));
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertFalse(actualNodeInstanceEntities.isEmpty());
        assertTrue(actualNodeInstanceEntities.contains(nodeInstanceEntities.get(1)));
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(id1, null, null, null),
                new NodeInstanceEntity(id2, null, null, null));
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        Collection<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertFalse(actualNodeInstanceEntities.isEmpty());
        assertTrue(actualNodeInstanceEntities.containsAll(nodeInstanceEntities));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Long flowInstanceId = 2L;
        List<NodeInstanceEntity> nodeInstanceEntitiesBeforeDelete = Arrays.asList(new NodeInstanceEntity(1L, instanceId, null, null),
                new NodeInstanceEntity(3L, flowInstanceId, null, null),
                new NodeInstanceEntity(4L, flowInstanceId, null, null));
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntitiesBeforeDelete);

        int deletedNodesCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        List<NodeInstanceEntity> nodeInstanceEntitiesAfterDelete = nodeInstanceEntityRepository.findAll();
        assertFalse(nodeInstanceEntitiesAfterDelete.isEmpty());
        assertFalse(nodeInstanceEntitiesAfterDelete.containsAll(nodeInstanceEntitiesBeforeDelete));
        assertEquals(nodeInstanceEntitiesBeforeDelete.size() - deletedNodesCount, nodeInstanceEntitiesAfterDelete.size());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        List<NodeInstanceEntity> nodeInstanceEntitiesBeforeDelete = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, null),
                new NodeInstanceEntity(2L, null, null, null),
                new NodeInstanceEntity(3L, instanceId, instanceType, null));
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntitiesBeforeDelete);

        int deletedNodesCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        List<NodeInstanceEntity> nodeInstanceEntitiesAfterDelete = nodeInstanceEntityRepository.findAll();
        assertFalse(nodeInstanceEntitiesAfterDelete.isEmpty());
        assertFalse(nodeInstanceEntitiesAfterDelete.containsAll(nodeInstanceEntitiesBeforeDelete));
        assertEquals(nodeInstanceEntitiesBeforeDelete.size() - deletedNodesCount, nodeInstanceEntitiesAfterDelete.size());
    }

    @Test
    public void testFindEmptyByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertNull(nodeInstanceEntities);
    }

    @Test
    public void testFindEmptyByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNull(nodeInstanceEntities);
    }

}