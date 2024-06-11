package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import com.oceanbase.odc.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.model.NodeInstance;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private Long testInstanceId;
    private Long testFlowInstanceId;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        testInstanceId = 1L;
        testFlowInstanceId = 2L;
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.TASK;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(testInstanceId, testFlowInstanceId, instanceType,
                flowableElementType);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(testInstanceId, testFlowInstanceId, instanceType,
                flowableElementType);

        List<NodeInstanceEntity> expectedNodeInstances = Collections.singletonList(nodeInstanceEntity1);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(testInstanceId, instanceType,
                flowableElementType)).thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(testInstanceId, instanceType, flowableElementType);
        assertFalse(actualNodeInstances.isEmpty());
        assertNotNull(actualNodeInstances.get(0));
        assertEquals(testInstanceId, actualNodeInstances.get(0).getInstanceId());
        assertEquals(testFlowInstanceId, actualNodeInstances.get(0).getFlowInstanceId());
        assertEquals(instanceType, actualNodeInstances.get(0).getInstanceType());
        assertEquals(flowableElementType, actualNodeInstances.get(0).getFlowableElementType());

        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(testInstanceId, instanceType,
                flowableElementType);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyList() {
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        FlowNodeType instanceType = FlowNodeType.TASK;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class),
                eq(flowableElementType)))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(testInstanceId, instanceType, flowableElementType);
        assertTrue(actualNodeInstances.isEmpty());

        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(testInstanceId, instanceType,
                flowableElementType);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullResult() {
        FlowableElementType flowableElementType = null;
        FlowNodeType instanceType = FlowNodeType.TASK;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class),
                eq(flowableElementType)))
                .thenReturn(null);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(testInstanceId, instanceType, flowableElementType);
        assertNull(actualNodeInstances);

        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(testInstanceId, instanceType,
                flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(testInstanceId, testFlowInstanceId, FlowNodeType.TASK,
                FlowableElementType.TASK);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(testInstanceId + 1, testFlowInstanceId,
                FlowNodeType.TASK, FlowableElementType.TASK);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(testFlowInstanceId)).thenReturn(List.of(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(testFlowInstanceId);
        assertFalse(actualNodeInstances.isEmpty());
        assertNotNull(actualNodeInstances.get(0));
        assertNotNull(actualNodeInstances.get(1));

        verify(nodeInstanceEntityRepository).findByFlowInstanceId(testFlowInstanceId);
    }

    @Test
    public void testFindByFlowInstanceId_EmptyList() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(testFlowInstanceId)).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(testFlowInstanceId);
        assertTrue(actualNodeInstances.isEmpty());

        verify(nodeInstanceEntityRepository).findByFlowInstanceId(testFlowInstanceId);
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(testInstanceId, testFlowInstanceId, FlowNodeType.TASK,
                FlowableElementType.TASK);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(testInstanceId + 1, testFlowInstanceId,
                FlowNodeType.TASK, FlowableElementType.TASK);
        List<Long> ids = List.of(testInstanceId, testInstanceId + 1);

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(List.of(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertFalse(actualNodeInstances.isEmpty());
        assertNotNull(actualNodeInstances.get(0));
        assertNotNull(actualNodeInstances.get(1));

        verify(nodeInstanceEntityRepository).findByIds(ids);
    }

    @Test
    public void testFindByIds_EmptyList() {
        List<Long> ids = Collections.singletonList(testInstanceId);

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertTrue(actualNodeInstances.isEmpty());

        verify(nodeInstanceEntityRepository).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(testInstanceId, testFlowInstanceId, FlowNodeType.TASK,
                FlowableElementType.TASK);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(testFlowInstanceId);
        assertEquals(1, result);

        verify(nodeInstanceEntityRepository).deleteAll(argThat(nodeInstanceEntity -> nodeInstanceEntity.getFlowInstanceId()
                .equals(testFlowInstanceId)));
    }

    @Test
    public void testDeleteByFlowInstanceId_NoRecords() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(testFlowInstanceId);
        assertEquals(0, result);

        verify(nodeInstanceEntityRepository, never()).deleteAll(argThat(nodeInstanceEntity -> nodeInstanceEntity.getFlowInstanceId()
                .equals(testFlowInstanceId)));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(testInstanceId, testFlowInstanceId, FlowNodeType.TASK,
                FlowableElementType.TASK);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(testInstanceId, FlowNodeType.TASK);
        assertEquals(1, result);

        verify(nodeInstanceEntityRepository).delete(nodeInstanceEntity);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_NoRecords() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(testInstanceId, FlowNodeType.TASK);
        assertEquals(0, result);

        verify(nodeInstanceEntityRepository, never()).delete(argThat(nodeInstanceEntity -> nodeInstanceEntity.getInstanceId()
                .equals(testInstanceId) && nodeInstanceEntity.getInstanceType() == FlowNodeType.TASK));
    }

}