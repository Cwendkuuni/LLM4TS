package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepositoryImpl;
import com.oceanbase.odc.metadb.flow.model.FlowInstance;
import com.oceanbase.odc.metadb.flow.service.FlowNodeService;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeConstant;
import com.oceanbase.odc.service.flow.model.FlowableElementTypeConstant;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private FlowNodeService flowNodeService;

    @Before
    public void setup() {
        flowNodeService = Mockito.mock(FlowNodeService.class);
        nodeInstanceEntityRepository = new NodeInstanceEntityRepositoryImpl(flowNodeService);
    }

    @After
    public void tearDown() {
        reset(flowNodeService);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeConstant.TASK_NODE;
        FlowableElementType flowableElementType = FlowableElementTypeConstant.PARALLEL_GATEWAY;
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, instanceType, flowableElementType),
                new NodeInstanceEntity(2L, 1L, instanceType, flowableElementType)
        );

        when(flowNodeService.findNodeTypeByName(instanceType.getName())).thenReturn(instanceType);
        when(flowNodeService.findFlowableElementTypeByName(flowableElementType.getName())).thenReturn(flowableElementType);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedNodeInstances, actualNodeInstances);
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeTypeConstant.START_EVENT, null),
                new NodeInstanceEntity(2L, 1L, FlowNodeTypeConstant.TASK_NODE, null),
                new NodeInstanceEntity(3L, 1L, FlowNodeTypeConstant.END_EVENT, null)
        );

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNodeInstances, actualNodeInstances);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeTypeConstant.START_EVENT, null),
                new NodeInstanceEntity(2L, 1L, FlowNodeTypeConstant.TASK_NODE, null),
                new NodeInstanceEntity(3L, 1L, FlowNodeTypeConstant.END_EVENT, null)
        );

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedNodeInstances, actualNodeInstances);
        verify(nodeInstanceEntityRepository, times(1)).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int expectedRowsAffected = 3;

        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(flowInstanceId);

        int actualRowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(expectedRowsAffected, actualRowsAffected);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeConstant.TASK_NODE;
        int expectedRowsAffected = 2;

        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int actualRowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedRowsAffected, actualRowsAffected);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test
    public void testFindEmptyListByFlowInstanceId() {
        Long flowInstanceId = 1L;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(Collections.emptyList(), actualNodeInstances);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testDeleteByFlowInstanceId_EmptyList() {
        Long flowInstanceId = 1L;
        int expectedRowsAffected = 0;

        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(flowInstanceId);

        int actualRowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(expectedRowsAffected, actualRowsAffected);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_EmptyList() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeConstant.TASK_NODE;
        int expectedRowsAffected = 0;

        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int actualRowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedRowsAffected, actualRowsAffected);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyList() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeConstant.TASK_NODE;
        FlowableElementType flowableElementType = FlowableElementTypeConstant.SEQUENTIAL_GATEWAY;

        when(flowNodeService.findNodeTypeByName(instanceType.getName())).thenReturn(instanceType);
        when(flowNodeService.findFlowableElementTypeByName(flowableElementType.getName())).thenReturn(flowableElementType);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertFalse(actualNodeInstances.isEmpty());
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }
}