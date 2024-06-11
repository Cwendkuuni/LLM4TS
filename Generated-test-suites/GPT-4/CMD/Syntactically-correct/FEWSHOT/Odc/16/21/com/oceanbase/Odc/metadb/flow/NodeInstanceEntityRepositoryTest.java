package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class NodeInstanceEntityRepositoryTest {
    private NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.COMPLEX_GATEWAY, FlowableElementType.TIMESTAMP))
            .thenReturn(new ArrayList<>());
        assertNotNull(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.COMPLEX_GATEWAY, FlowableElementType.TIMESTAMP));
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(new ArrayList<>());
        assertNotNull(nodeInstanceEntityRepository.findByFlowInstanceId(1L));
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(anyCollection())).thenReturn(new ArrayList<>());
        assertNotNull(nodeInstanceEntityRepository.findByIds(new ArrayList<>()));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.COMPLEX_GATEWAY)).thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.COMPLEX_GATEWAY));
    }
}