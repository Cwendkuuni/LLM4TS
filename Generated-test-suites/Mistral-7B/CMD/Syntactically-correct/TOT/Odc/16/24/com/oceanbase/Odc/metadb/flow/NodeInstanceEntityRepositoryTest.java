package com.oceanbase.odc.service.flow.repository.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstance;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;
    private Long instanceId = 1L;
    private Long flowInstanceId = 1L;

    @Before
    public void setup() {
        nodeInstanceEntity = new NodeInstanceEntity(flowInstanceId, FlowNodeType.START, FlowableElementType.TASK, null, null, null, null, null, null, null);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);
    }

    @After
    public void teardown() {
        nodeInstanceEntityRepository.deleteAll();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowableElementType flowableElementType = FlowableElementType.GATEWAY;
        List<NodeInstanceEntity> foundNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.START, flowableElementType);
        assertNotNull(foundNodes);
        assertEquals(1, foundNodes.size());
        assertNull(foundNodes.get(0).getFlowableElementType()); // Initial node has null flowableElementType

        NodeInstanceEntity newNode = new NodeInstanceEntity(flowInstanceId, FlowNodeType.GATEWAY, flowableElementType, null, null, null, null, null, null, null);
        nodeInstanceEntityRepository.save(newNode);
        List<NodeInstanceEntity> nodesWithExpectFlowableElementType = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.GATEWAY, flowableElementType);
        assertFalse(nodesWithExpectFlowableElementType.isEmpty());
        assertEquals(2, nodesWithExpectFlowableElementType.size());

        nodeInstanceEntityRepository.delete(nodesWithExpectFlowableElementType.get(1));
        nodesWithExpectFlowableElementType = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.GATEWAY, flowableElementType);
        assertEquals(1, nodesWithExpectFlowableElementType.size());
        assertEquals(nodeInstanceEntity, nodesWithExpectFlowableElementType.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> foundNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNotNull(foundNodes);
        assertEquals(1, foundNodes.size());
        assertEquals(nodeInstanceEntity, foundNodes.get(0));

        NodeInstanceEntity newNode = new NodeInstanceEntity(flowInstanceId, FlowNodeType.GATEWAY, null, null, null, null, null, null, null, null);
        nodeInstanceEntityRepository.save(newNode);
        foundNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(2, foundNodes.size());

        nodeInstanceEntityRepository.delete(newNode);
        foundNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(1, foundNodes.size());
        assertEquals(nodeInstanceEntity, foundNodes.get(0));
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity newNode = new NodeInstanceEntity(flowInstanceId, FlowNodeType.GATEWAY, null, null, null, null, null, null, null, null);
        nodeInstanceEntityRepository.save(newNode);
        Long newNodeId = newNode.getId();

        List<Long> ids = Arrays.asList(instanceId.longValue(), newNodeId.longValue());
        List<NodeInstanceEntity> foundNodes = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(foundNodes);
        assertEquals(2, foundNodes.size());
        assertTrue(foundNodes.contains(nodeInstanceEntity));
        assertTrue(foundNodes.contains(newNode));

        nodeInstanceEntityRepository.delete(newNode);
        foundNodes = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(1, foundNodes.size());
        assertEquals(nodeInstanceEntity, foundNodes.get(0));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int deletedCountBeforeDelete = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId).intValue();
        assertEquals(1, deletedCountBeforeDelete);

        int deletedCountAfterDelete = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId).intValue();
        assertEquals(0, deletedCountAfterDelete);

        int deletedCountByEmptyQuery = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId).intValue();
        assertEquals(0, deletedCountByEmptyQuery);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int deletedCountBeforeDelete = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START).intValue();
        assertEquals(1, deletedCountBeforeDelete);

        int deletedCountAfterDelete = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.START).intValue();
        assertEquals(0, deletedCountAfterDelete);

        int deletedCountByEmptyQuery = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.GATEWAY).intValue();
        assertEquals(0, deletedCountByEmptyQuery);
    }
}