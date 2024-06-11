package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.flow.model.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        FlowNodeType instanceType = FlowNodeType.START;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType),
                new NodeInstanceEntity(2L, instanceId, instanceType, FlowableElementType.END),
                new NodeInstanceEntity(3L, instanceId, FlowNodeType.DECISION, FlowableElementType.PROCESS),
                new NodeInstanceEntity(4L, instanceId, FlowNodeType.TASK, FlowableElementType.PROCESS),
                new NodeInstanceEntity(5L, instanceId, FlowNodeType.GATEWAY, FlowableElementType.END)
        );

        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        List<NodeInstanceEntity> foundNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(1, foundNodeInstanceEntities.size());
        assertEquals(1L, foundNodeInstanceEntities.get(0).getId());
        assertEquals(instanceId, foundNodeInstanceEntities.get(0).getInstanceId());
        assertEquals(instanceType, foundNodeInstanceEntities.get(0).getInstanceType());
        assertEquals(flowableElementType, foundNodeInstanceEntities.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null),
                new NodeInstanceEntity(2L, flowInstanceId, null, null),
                new NodeInstanceEntity(3L, flowInstanceId, null, null),
                new NodeInstanceEntity(4L, null, null, null),
                new NodeInstanceEntity(5L, flowInstanceId, null, null)
        );

        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        List<NodeInstanceEntity> foundNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(3, foundNodeInstanceEntities.size());
        assertFalse(foundNodeInstanceEntities.isEmpty());
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(id1, null, null, null),
                new NodeInstanceEntity(id2, null, null, null),
                new NodeInstanceEntity(id3, null, null, null)
        );

        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        Collection<Long> ids = Arrays.asList(id1, id2);

        List<NodeInstanceEntity> foundNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, foundNodeInstanceEntities.size());
        assertTrue(foundNodeInstanceEntities.contains(nodeInstanceEntities.get(0)));
        assertTrue(foundNodeInstanceEntities.contains(nodeInstanceEntities.get(1)));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Long flowInstanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertNotNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()));
        assertEquals(0, result);

        nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertNotNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()));
        assertEquals(0, result);

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeThrowsExceptionWhenInstanceTypeIsNull() {
        Long instanceId = 1L;

        assertThrows(IllegalArgumentException.class, () -> nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, null));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeThrowsExceptionWhenInstanceIdIsNull() {
        FlowNodeType instanceType = FlowNodeType.START;

        assertThrows(IllegalArgumentException.class, () -> nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, instanceType));
    }

    @Test
    public void testFindEmptyListWhenNoNodesFound() {
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.emptyList();

        assertEquals(0, nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null, null).size());
        assertEquals(0, nodeInstanceEntityRepository.findByFlowInstanceId(null).size());
        assertEquals(0, nodeInstanceEntityRepository.findByIds(Collections.emptyList()).size());
    }

}