package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;
    private NodeInstanceEntity nodeInstanceEntity3;

    @Rule
    public TestRule springRule = new TestRule() {
        @Override
        public Statement statementsToExecuteBefore(@org.junit.runner.RunWithAnnotation Iteration annotation) {
            return super.statementsToExecuteBefore(annotation);
        }
    };

    @Before
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        nodeInstanceEntity1 = createNodeInstanceEntity(1L, FlowNodeType.START, FlowableElementType.PROCESS, 1L);
        nodeInstanceEntity2 = createNodeInstanceEntity(2L, FlowNodeType.END, FlowableElementType.PROCESS, 1L);
        nodeInstanceEntity3 = createNodeInstanceEntity(3L, FlowNodeType.TASK, FlowableElementType.PROCESS, 2L);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity3));
        when(nodeInstanceEntityRepository.findById(anyLong())).thenReturn(nodeInstanceEntity1);
        when(nodeInstanceEntityRepository.findByInstanceId(anyLong())).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3));
        when(nodeInstanceEntityRepository.findByIds(anyCollection())).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3));
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(1);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.PROCESS);
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity1, result.get(0));
    }

    @Test
    public void testFindByInstanceId() {
        NodeInstanceEntity result = nodeInstanceEntityRepository.findById(1L).get();
        assertEquals(nodeInstanceEntity1, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(3, result.size());
        assertTrue(result.contains(nodeInstanceEntity1));
        assertTrue(result.contains(nodeInstanceEntity2));
        assertTrue(result.contains(nodeInstanceEntity3));
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));
        assertEquals(3, result.size());
        assertTrue(result.contains(nodeInstanceEntity1));
        assertTrue(result.contains(nodeInstanceEntity2));
        assertTrue(result.contains(nodeInstanceEntity3));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(3, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceId() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, FlowNodeType.START, FlowableElementType.PROCESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null, FlowableElementType.PROCESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, null);
    }

    @Test
    public void testDeleteByFlowInstanceIdWithNullFlowInstanceId() {
        assertThrows(NullPointerException.class, () -> nodeInstanceEntityRepository.deleteByFlowInstanceId(null));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        assertThrows(NullPointerException.class, () -> nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, FlowNodeType.START));
    }

    private NodeInstanceEntity createNodeInstanceEntity(long id, FlowNodeType instanceType, FlowableElementType flowableElementType, long flowInstanceId) {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(id);
        nodeInstanceEntity.setInstanceId(id);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        return nodeInstanceEntity;
    }
}