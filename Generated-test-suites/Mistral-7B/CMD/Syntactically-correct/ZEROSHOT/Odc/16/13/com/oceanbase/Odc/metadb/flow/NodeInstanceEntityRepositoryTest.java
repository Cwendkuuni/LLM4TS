package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType),
                new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType));
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedNodeInstanceEntities);
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(2, actualNodeInstanceEntities.size());
        for (int i = 0; i < actualNodeInstanceEntities.size(); i++) {
            NodeInstanceEntity actualNodeInstanceEntity = actualNodeInstanceEntities.get(i);
            NodeInstanceEntity expectedNodeInstanceEntity = expectedNodeInstanceEntities.get(i);
            assertEquals(expectedNodeInstanceEntity.getInstanceId(), actualNodeInstanceEntity.getInstanceId());
            assertEquals(expectedNodeInstanceEntity.getInstanceType(), actualNodeInstanceEntity.getInstanceType());
            assertEquals(expectedNodeInstanceEntity.getFlowableElementType(), actualNodeInstanceEntity.getFlowableElementType());
        }
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null),
                new NodeInstanceEntity(2L, flowInstanceId, null, null),
                new NodeInstanceEntity(3L, flowInstanceId, null, null));
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedNodeInstanceEntities);
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(3, actualNodeInstanceEntities.size());
        for (NodeInstanceEntity actualNodeInstanceEntity : actualNodeInstanceEntities) {
            assertNull(actualNodeInstanceEntity.getInstanceId());
            assertEquals(flowInstanceId, actualNodeInstanceEntity.getFlowInstanceId());
        }
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null),
                new NodeInstanceEntity(2L, null, null, null));
        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedNodeInstanceEntities);
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, actualNodeInstanceEntities.size());
        for (NodeInstanceEntity actualNodeInstanceEntity : actualNodeInstanceEntities) {
            assertNull(actualNodeInstanceEntity.getInstanceId());
            assertNull(actualNodeInstanceEntity.getInstanceType());
            assertNull(actualNodeInstanceEntity.getFlowableElementType());
        }
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedDeletedCount = 2;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedDeletedCount, actualDeletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int expectedDeletedCount = 2;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        int actualDeletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedDeletedCount, actualDeletedCount);
    }

}