package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringBootTest;
import org.springframework.test.context.junit.runner.JUnitRunners;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowInstance;

@RunWith(JUnitRunners.JUnit4.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null),
                new NodeInstanceEntity(2L, instanceId, instanceType, FlowableElementType.GATEWAY, null),
                new NodeInstanceEntity(3L, instanceId, FlowNodeType.START_EVENT, flowableElementType, null));

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(instanceId, result.get(0).getInstanceId());
        assertEquals(instanceType, result.get(0).getInstanceType());
        assertEquals(flowableElementType, result.get(0).getFlowableElementType());
        assertEquals(result.get(1).getId(), 2L);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeEmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType)).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeNullResult() {
        Long instanceId = null;
        FlowNodeType instanceType = null;
        FlowableElementType flowableElementType = null;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType)).thenReturn(null);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNull(result);
    }

    // Test for findByFlowInstanceId method

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null, new FlowInstance(1L, null, null)),
                new NodeInstanceEntity(2L, flowInstanceId, null, null, new FlowInstance(1L, null, null)),
                new NodeInstanceEntity(3L, null, null, null, new FlowInstance(2L, null, null)));

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository
                .findByFlowInstanceId(flowInstanceId);

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(flowInstanceId, result.get(0).getFlowInstanceId());
        assertNull(result.get(0).getInstanceId());
        assertNull(result.get(0).getInstanceType());
        assertNull(result.get(0).getFlowableElementType());
        assertEquals(result.get(1).getId(), 2L);
    }

    @Test
    public void testFindByFlowInstanceIdEmptyResult() {
        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository
                .findByFlowInstanceId(flowInstanceId);

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceIdNullResult() {
        Long flowInstanceId = null;

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(null);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository
                .findByFlowInstanceId(flowInstanceId);

        assertNull(result);
    }

    // Test for findByIds method

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null, null),
                new NodeInstanceEntity(2L, null, null, null, null),
                new NodeInstanceEntity(3L, null, null, null, null));

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getId());
        assertNull(result.get(0).getInstanceId());
        assertNull(result.get(0).getInstanceType());
        assertNull(result.get(0).getFlowableElementType());
        assertEquals(result.get(1).getId(), 2L);
        assertNull(result.get(1).getInstanceId());
        assertNull(result.get(1).getInstanceType());
        assertNull(result.get(1).getFlowableElementType());
        assertEquals(result.get(2).getId(), 3L);
        assertNull(result.get(2).getInstanceId());
        assertNull(result.get(2).getInstanceType());
        assertNull(result.get(2).getFlowableElementType());
    }

    @Test
    public void testFindByIdsEmptyResult() {
        Collection<Long> ids = new ArrayList<>();

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindByIdsNullResult() {
        Collection<Long> ids = null;

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(null);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertNull(result);
    }

    // Test for deleteByFlowInstanceId method

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Long flowInstanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(1L, instanceId, null, null, null);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(1, result);
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()));
    }

    @Test
    public void testDeleteByFlowInstanceIdNoEntities() {
        Long flowInstanceId = 1L;

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdNullFlowInstanceId() {
        Long flowInstanceId = null;

        nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
    }

    // Test for deleteByInstanceIdAndInstanceType method

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(1L, instanceId, instanceType, null, null);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(2L, instanceId, null, null, null);
        nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
        assertNull(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType).get(0));
        assertEquals(1, nodeInstanceEntityRepository.findByInstanceId(instanceId).size());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeNoEntities() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeNullInstanceId() {
        Long instanceId = null;
        FlowNodeType instanceType = FlowNodeType.PROCESS;

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeNullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}