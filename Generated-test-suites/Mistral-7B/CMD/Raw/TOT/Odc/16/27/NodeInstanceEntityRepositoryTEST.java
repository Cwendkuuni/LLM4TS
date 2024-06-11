package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowInstance;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        Long instanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, new FlowInstance(1L), null),
                new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType, new FlowInstance(2L), null),
                new NodeInstanceEntity(3L, instanceId + 1, instanceType, flowableElementType, new FlowInstance(3L), null)
        );

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(1L, result.get(0).getInstanceId());
        assertEquals(instanceType, result.get(0).getInstanceType());
        assertEquals(flowableElementType, result.get(0).getFlowableElementType());
        assertEquals(nodeInstanceEntities.get(0).getFlowInstance(), result.get(0).getFlowInstance());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithEmptyResult() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        Long instanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeType.TASK, FlowableElementType.START_EVENT, new FlowInstance(flowInstanceId), null),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.TASK, FlowableElementType.END_EVENT, new FlowInstance(flowInstanceId), null)
        );

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(1L, result.get(0).getInstanceId());
        assertEquals(FlowNodeType.TASK, result.get(0).getInstanceType());
        assertEquals(FlowableElementType.START_EVENT, result.get(0).getFlowableElementType());
        assertEquals(nodeInstanceEntities.get(0).getFlowInstance(), result.get(0).getFlowInstance());
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(id1, null, null, null, null, null),
                new NodeInstanceEntity(id2, null, null, null, null, null)
        );

        Mockito.when(nodeInstanceEntityRepository.findByIds(Arrays.asList(id1, id2)))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(id1, id2));

        assertEquals(2, result.size());
        assertEquals(id1, result.get(0).getId());
        assertEquals(id2, result.get(1).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        Long flowInstanceId = 1L;

        int result = 2;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(flowInstanceId);

        result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(result, 2);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;

        int result = 2;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(result, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullFlowInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, FlowNodeType.TASK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
    }

}