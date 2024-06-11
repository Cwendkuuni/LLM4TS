package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeInstance;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:spring-test.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    // Test findByInstanceIdAndInstanceTypeAndFlowableElementType
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, 1L));
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_Empty() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertTrue(actualNodeInstanceEntities.isEmpty());
    }

    // Test findByFlowInstanceId
    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(1L, null, null, null, flowInstanceId), new NodeInstanceEntity(2L, null, null, null, flowInstanceId));
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    @Test
    public void testFindByFlowInstanceId_Empty() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertTrue(actualNodeInstanceEntities.isEmpty());
    }

    // Test findByIds
    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(1L, null, null, null, null), new NodeInstanceEntity(2L, null, null, null, null));
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(expectedNodeInstanceEntities, actualNodeInstanceEntities);
    }

    @Test
    public void testFindByIds_Empty() {
        List<Long> ids = Collections.emptyList();
        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertTrue(actualNodeInstanceEntities.isEmpty());
    }

    // Test deleteByFlowInstanceId
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Long flowInstanceId = 1L;
        int expectedNumberOfDeletedNodes = 2; // Assuming there are already 2 nodes with the given flowInstanceId
        int actualNumberOfDeletedNodes = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(expectedNumberOfDeletedNodes, actualNumberOfDeletedNodes);
    }

    @Test
    public void testDeleteByFlowInstanceId_ZeroDeleted() {
        Long instanceId = 1L;
        Long flowInstanceId = 1L;
        int actualNumberOfDeletedNodes = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(0, actualNumberOfDeletedNodes);
    }

    // Test deleteByInstanceIdAndInstanceType
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumberOfDeletedNodes = 1;
        int actualNumberOfDeletedNodes = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedNumberOfDeletedNodes, actualNumberOfDeletedNodes);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_ZeroDeleted() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int actualNumberOfDeletedNodes = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(0, actualNumberOfDeletedNodes);
    }
}