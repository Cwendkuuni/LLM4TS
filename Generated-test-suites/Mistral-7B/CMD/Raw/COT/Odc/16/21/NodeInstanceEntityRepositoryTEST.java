package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeInstanceId;

import com.oceanbase.odc.metadb.flow.repository.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = Mockito.spy(new NodeInstanceEntity());
        expectedResult.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        Mockito.verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = Mockito.spy(new NodeInstanceEntity());
        expectedResult.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(1, result.size());
        Mockito.verify(nodeInstanceEntityRepository).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);

        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity = Mockito.spy(new NodeInstanceEntity());
        expectedResult.add(nodeInstanceEntity);

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(1, result.size());
        Mockito.verify(nodeInstanceEntityRepository).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 1;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedResult, result);
        Mockito.verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedResult = 1;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedResult, result);
        Mockito.verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = null;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenThrow(new IllegalArgumentException("flowableElementType is null"));

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenThrow(new IllegalArgumentException("instanceType is null"));

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNegativeInstanceId() {
        Long instanceId = -1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenThrow(new IllegalArgumentException("instanceId is negative"));

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullInstanceId() {
        Long instanceId = null;

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId))
                .thenThrow(new IllegalArgumentException("instanceId is null"));

        nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        Long instanceId = null;
        FlowNodeType instanceType = FlowNodeType.START;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenThrow(new IllegalArgumentException("instanceId is null"));

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenThrow(new IllegalArgumentException("instanceType is null"));

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}