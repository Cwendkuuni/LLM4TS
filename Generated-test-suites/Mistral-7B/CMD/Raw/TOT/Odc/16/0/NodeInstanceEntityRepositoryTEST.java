package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.ExampleMatcher;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.entity.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity mockNodeInstanceEntity;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = Mockito.spy(NodeInstanceEntityRepositoryImpl.class);
        mockNodeInstanceEntity = Mockito.mock(NodeInstanceEntity.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        NodeInstanceEntity expectedNodeInstanceEntity = new NodeInstanceEntity();
        expectedNodeInstanceEntity.setInstanceId(instanceId);
        expectedNodeInstanceEntity.setInstanceType(instanceType);
        expectedNodeInstanceEntity.setFlowableElementType(flowableElementType);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(Collections.singletonList(expectedNodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), expectedNodeInstanceEntity);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        expectedNodeInstanceEntities.add(nodeInstanceEntity1);
        expectedNodeInstanceEntities.add(nodeInstanceEntity2);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(result.size(), 2);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> expectedNodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(ids.get(0));
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(ids.get(1));
        expectedNodeInstanceEntities.add(nodeInstanceEntity1);
        expectedNodeInstanceEntities.add(nodeInstanceEntity2);

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(result.size(), 2);
        verify(nodeInstanceEntityRepository, times(1)).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int expectedRowsAffected = 2;

        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(result, expectedRowsAffected);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        int expectedRowsAffected = 2;

        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(result, expectedRowsAffected);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}