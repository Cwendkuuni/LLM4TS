package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_NODE;
        FlowableElementType flowableElementType = FlowableElementType.GATEWAY;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, "name1"),
                new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType, "name2"),
                new NodeInstanceEntity(3L, instanceId, FlowNodeType.END_NODE, flowableElementType, "name3")
        );
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(result.size() == 2);
        assertTrue(result.containsAll(Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, "name1"),
                new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType, "name2"))));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long instanceId = 1L;
        Long flowInstanceId = 10L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, FlowNodeType.START_NODE, FlowableElementType.TASK, "name1"),
                new NodeInstanceEntity(2L, instanceId, FlowNodeType.END_NODE, FlowableElementType.GATEWAY, "name2"),
                new NodeInstanceEntity(3L, flowInstanceId, FlowNodeType.START_NODE, FlowableElementType.TASK, "name3")
        );
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertTrue(result.size() == 1);
        assertTrue(result.get(0).getFlowInstanceId().equals(flowInstanceId));
    }

    @Test
    public void testFindByIds() {
        Long instanceId = 1L;
        Long id1 = 1L;
        Long id2 = 2L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(id1, instanceId, FlowNodeType.START_NODE, FlowableElementType.TASK, "name1"),
                new NodeInstanceEntity(id2, instanceId, FlowNodeType.END_NODE, FlowableElementType.GATEWAY, "name2")
        );
        List<Long> ids = Arrays.asList(id1, id2);
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertTrue(result.size() == 2);
        assertTrue(result.containsAll(nodeInstanceEntities));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Long flowInstanceId = 10L;
        Long id1 = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(id1, instanceId, FlowNodeType.START_NODE, FlowableElementType.TASK, "name1"),
                new NodeInstanceEntity(id1 + 1, instanceId, FlowNodeType.END_NODE, FlowableElementType.GATEWAY, "name2"),
                new NodeInstanceEntity(id1 + 2, flowInstanceId, FlowNodeType.START_NODE, FlowableElementType.TASK, "name3")
        );
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertTrue(result == 1);
        assertNull(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId).get(0));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        Long id1 = 1L;
        Long id2 = 2L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(id1, instanceId, FlowNodeType.START_NODE, FlowableElementType.TASK, "name1"),
                new NodeInstanceEntity(id2, instanceId, FlowNodeType.END_NODE, FlowableElementType.GATEWAY, "name2")
        );
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_NODE);

        assertTrue(result == 1);
        assertNull(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_NODE).get(0));
    }

    @Test
    public void testFindByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, FlowNodeType.START_NODE, FlowableElementType.TASK, "name1"),
                new NodeInstanceEntity(2L, instanceId, FlowNodeType.END_NODE, FlowableElementType.GATEWAY, "name2")
        );
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, FlowNodeType.START_NODE);

        assertTrue(result.size() == 1);
        assertTrue(result.get(0).getInstanceId().equals(instanceId));
        assertTrue(result.get(0).getInstanceType().equals(FlowNodeType.START_NODE));
    }

    @Test
    public void testFindEmptyListByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.START_NODE, FlowableElementType.GATEWAY);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeNull() {
        Long instanceId = 1L;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, null);

        assertTrue(result.isEmpty());
    }
}