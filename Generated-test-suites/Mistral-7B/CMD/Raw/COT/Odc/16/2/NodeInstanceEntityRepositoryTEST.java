package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType),
                new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType),
                new NodeInstanceEntity(3L, instanceId, instanceType, FlowableElementType.END));
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(3, result.size());
        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START;
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.emptyList();
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertTrue(result.isEmpty());
        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeType.TASK, FlowableElementType.START),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.TASK, FlowableElementType.END),
                new NodeInstanceEntity(3L, 1L, FlowNodeType.DECISION, FlowableElementType.INTERMEDIATE));
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(3, result.size());
        verify(nodeInstanceEntityRepository).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByFlowInstanceId_EmptyResult() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.emptyList();
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertTrue(result.isEmpty());
        verify(nodeInstanceEntityRepository).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeType.TASK, FlowableElementType.START),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.TASK, FlowableElementType.END),
                new NodeInstanceEntity(3L, 1L, FlowNodeType.DECISION, FlowableElementType.INTERMEDIATE));
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, result.size());
        verify(nodeInstanceEntityRepository).findByIds(ids);
    }

    @Test
    public void testFindByIds_EmptyResult() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstanceEntities = Collections.emptyList();
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertTrue(result.isEmpty());
        verify(nodeInstanceEntityRepository).findByIds(ids);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceId_IllegalArgumentException() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(null)).thenThrow(new IllegalArgumentException("instanceId is required"));

        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test
    public void testDeleteByFlowInstanceId_Success() {
        Long instanceId = 1L;
        int expectedResult = 3;
        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedResult, result);
        verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_Success() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expectedResult = 3;
        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedResult, result);
        verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceType_IllegalArgumentException() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenThrow(new IllegalArgumentException("instanceType is required"));

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

}