package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class NodeInstanceEntityRepositoryTest {
    NodeInstanceEntityRepository nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.BLOCK, FlowableElementType.SEQUENCE_FLOW))
            .thenReturn(Arrays.asList(nodeInstanceEntity));
        assertNotNull(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.BLOCK, FlowableElementType.SEQUENCE_FLOW));
        assertEquals(nodeInstanceEntity, nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.BLOCK, FlowableElementType.SEQUENCE_FLOW).get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
            .thenReturn(Arrays.asList(nodeInstanceEntity));
        assertNotNull(nodeInstanceEntityRepository.findByFlowInstanceId(1L));
        assertEquals(nodeInstanceEntity, nodeInstanceEntityRepository.findByFlowInstanceId(1L).get(0));
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        Mockito.when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L)))
            .thenReturn(Arrays.asList(nodeInstanceEntity));
        assertNotNull(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L)));
        assertEquals(nodeInstanceEntity, nodeInstanceEntityRepository.findByIds(Arrays.asList(1L)).get(0));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L))
            .thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.BLOCK))
            .thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.BLOCK));
    }
}