package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        // Given
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.GATEWAY);

        List<NodeInstanceEntity> expectedResult = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertFalse(result.isEmpty());
        assertTrue(result.containsAll(expectedResult));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        // Given
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);

        List<NodeInstanceEntity> expectedResult = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Then
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertFalse(result.isEmpty());
        assertTrue(result.containsAll(expectedResult));
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;

        // Given
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(id1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(id2);

        List<NodeInstanceEntity> expectedResult = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);

        Mockito.when(nodeInstanceEntityRepository.findByIds(Arrays.asList(id1, id2))).thenReturn(expectedResult);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(id1, id2));

        // Then
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertFalse(result.isEmpty());
        assertTrue(result.containsAll(expectedResult));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedNumOfDeletedRows = 2;

        // Given
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        // When
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        // Then
        verify(nodeInstanceEntityRepository, times(expectedNumOfDeletedRows)).delete(any(NodeInstanceEntity.class));
        assertEquals(expectedNumOfDeletedRows, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedNumOfDeletedRows = 2;

        // Given
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);

        Mockito.doNothing().when(nodeInstanceEntityRepository).delete(nodeInstanceEntity1);
        Mockito.doNothing().when(nodeInstanceEntityRepository).delete(nodeInstanceEntity2);

        // When
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        verify(nodeInstanceEntityRepository, times(expectedNumOfDeletedRows)).delete(any(NodeInstanceEntity.class));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeEmptyList() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        // Given
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // When
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        verifyNoInteractions(nodeInstanceEntityRepository);
    }
}