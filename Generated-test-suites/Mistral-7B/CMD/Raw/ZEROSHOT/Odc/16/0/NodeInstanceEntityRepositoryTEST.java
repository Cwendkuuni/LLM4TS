package com.oceanbase.odc.metadb.flow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;
    private NodeInstanceEntity nodeInstanceEntity3;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        nodeInstanceEntity1 = new NodeInstanceEntity(1L, FlowNodeType.START, FlowableElementType.PROCESS, 1L);
        nodeInstanceEntity2 = new NodeInstanceEntity(2L, FlowNodeType.END, FlowableElementType.GATEWAY, 1L);
        nodeInstanceEntity3 = new NodeInstanceEntity(3L, FlowNodeType.PROCESS, FlowableElementType.PROCESS, 1L);

        // Prepopulate test data
        nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity3);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedNodeInstanceEntities.size(), actualNodeInstanceEntities.size());
        for (int i = 0; i < expectedNodeInstanceEntities.size(); i++) {
            NodeInstanceEntity expectedNodeInstanceEntity = expectedNodeInstanceEntities.get(i);
            NodeInstanceEntity actualNodeInstanceEntity = actualNodeInstanceEntities.get(i);
            assertEquals(expectedNodeInstanceEntity.getId(), actualNodeInstanceEntity.getId());
            assertEquals(expectedNodeInstanceEntity.getInstanceId(), actualNodeInstanceEntity.getInstanceId());
            assertEquals(expectedNodeInstanceEntity.getInstanceType(), actualNodeInstanceEntity.getInstanceType());
            assertEquals(expectedNodeInstanceEntity.getFlowableElementType(), actualNodeInstanceEntity.getFlowableElementType());
        }
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity3);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNodeInstanceEntities.size(), actualNodeInstanceEntities.size());
        for (int i = 0; i < expectedNodeInstanceEntities.size(); i++) {
            NodeInstanceEntity expectedNodeInstanceEntity = expectedNodeInstanceEntities.get(i);
            NodeInstanceEntity actualNodeInstanceEntity = actualNodeInstanceEntities.get(i);
            assertEquals(expectedNodeInstanceEntity.getId(), actualNodeInstanceEntity.getId());
            assertEquals(expectedNodeInstanceEntity.getInstanceId(), actualNodeInstanceEntity.getInstanceId());
            assertEquals(expectedNodeInstanceEntity.getInstanceType(), actualNodeInstanceEntity.getInstanceType());
            assertEquals(expectedNodeInstanceEntity.getFlowableElementType(), actualNodeInstanceEntity.getFlowableElementType());
        }
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 3L);

        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity3);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedNodeInstanceEntities.size(), actualNodeInstanceEntities.size());
        for (int i = 0; i < expectedNodeInstanceEntities.size(); i++) {
            NodeInstanceEntity expectedNodeInstanceEntity = expectedNodeInstanceEntities.get(i);
            NodeInstanceEntity actualNodeInstanceEntity = actualNodeInstanceEntities.get(i);
            assertEquals(expectedNodeInstanceEntity.getId(), actualNodeInstanceEntity.getId());
            assertEquals(expectedNodeInstanceEntity.getInstanceId(), actualNodeInstanceEntity.getInstanceId());
            assertEquals(expectedNodeInstanceEntity.getInstanceType(), actualNodeInstanceEntity.getInstanceType());
            assertEquals(expectedNodeInstanceEntity.getFlowableElementType(), actualNodeInstanceEntity.getFlowableElementType());
        }
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int numOfNodesBeforeDeletion = nodeInstanceEntityRepository.count();
        nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        int numOfNodesAfterDeletion = nodeInstanceEntityRepository.count();

        assertEquals(numOfNodesBeforeDeletion - 2, numOfNodesAfterDeletion);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        int numOfNodesBeforeDeletion = nodeInstanceEntityRepository.count();
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        int numOfNodesAfterDeletion = nodeInstanceEntityRepository.count();

        assertEquals(numOfNodesBeforeDeletion - 2, numOfNodesAfterDeletion);
    }

    @Test
    public void testFindEmptyList() {
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.PROCESS, FlowableElementType.PROCESS);

        assertTrue(actualNodeInstanceEntities.isEmpty());
    }

    @Test
    public void testFindNull() {
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, FlowNodeType.PROCESS, FlowableElementType.PROCESS);

        assertNull(actualNodeInstanceEntities);
    }
}