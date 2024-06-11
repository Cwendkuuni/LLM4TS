package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.TASK;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = List.of(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType));
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertTrue(expectedNodeInstanceEntities.equals(actualNodeInstanceEntities));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(1L, 1L, FlowNodeType.TASK, FlowableElementType.USER_TASK);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(2L, 1L, FlowNodeType.TASK, FlowableElementType.USER_TASK);
        expectedNodeInstanceEntities.add(nodeInstanceEntity1);
        expectedNodeInstanceEntities.add(nodeInstanceEntity2);
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertTrue(expectedNodeInstanceEntities.equals(actualNodeInstanceEntities));
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = List.of(new NodeInstanceEntity(1L, 1L, FlowNodeType.TASK, FlowableElementType.USER_TASK),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.TASK, FlowableElementType.USER_TASK));
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertTrue(expectedNodeInstanceEntities.equals(actualNodeInstanceEntities));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedDeletedCount = 2;
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertTrue(expectedDeletedCount == actualDeletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expectedDeletedCount = 2;
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertTrue(expectedDeletedCount == actualDeletedCount);
    }

    @Test
    public void testFindEmptyListByInvalidInstanceId() {
        Long instanceId = -1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.TASK, FlowableElementType.START_EVENT);
        assertTrue(expectedNodeInstanceEntities.equals(actualNodeInstanceEntities));
    }

    @Test
    public void testFindNullByInvalidInstanceId() {
        Long instanceId = null;
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, FlowNodeType.TASK, FlowableElementType.START_EVENT);
        assertNull(actualNodeInstanceEntities);
    }

    @Test
    public void testDeleteByInvalidInstanceId() {
        Long instanceId = -1L;
        int expectedDeletedCount = 0;
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.TASK);
        assertTrue(expectedDeletedCount == actualDeletedCount);
    }

    @Test
    public void testDeleteByNullInstanceId() {
        Long instanceId = null;
        int expectedDeletedCount = 0;
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, FlowNodeType.TASK);
        assertTrue(expectedDeletedCount == actualDeletedCount);
    }

    @Test
    public void testFindDeleteEmptyListByInvalidFlowInstanceId() {
        Long instanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
        assertTrue(expectedNodeInstanceEntities.equals(actualNodeInstanceEntities));
        int expectedDeletedCount = 2;
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertTrue(expectedDeletedCount == actualDeletedCount);
        List<NodeInstanceEntity> actualNodeInstanceEntitiesAfterDeletion = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
        assertFalse(actualNodeInstanceEntitiesAfterDeletion.isEmpty());
    }

    @Test
    public void testFindDeleteEmptyListByInvalidIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(-1L);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertTrue(expectedNodeInstanceEntities.equals(actualNodeInstanceEntities));
        int expectedDeletedCount = 2;
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertTrue(expectedDeletedCount == actualDeletedCount);
        List<NodeInstanceEntity> actualNodeInstanceEntitiesAfterDeletion = nodeInstanceEntityRepository.findByIds(ids);
        assertTrue(actualNodeInstanceEntitiesAfterDeletion.isEmpty());
    }

    @Test
    public void testFindDeleteByValidInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expectedDeletedCount = 2;
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertTrue(expectedDeletedCount == actualDeletedCount);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, FlowableElementType.START_EVENT);
        assertTrue(actualNodeInstanceEntities.isEmpty());
    }

    @Test
    public void testFindDeleteByInvalidInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expectedDeletedCount = 0;
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(10L, instanceType);
        assertTrue(expectedDeletedCount == actualDeletedCount);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, FlowableElementType.START_EVENT);
        assertTrue(expectedNodeInstanceEntities.isEmpty());
    }
}