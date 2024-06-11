package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NodeInstanceEntityRepositoryTest {

    NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);

    // Test findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.PROCESS);
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.PROCESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullArgument() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
    }

    // Test findByFlowInstanceId method
    @Test
    public void testFindByFlowInstanceId() {
        nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceId_NullArgument() {
        nodeInstanceEntityRepository.findByFlowInstanceId(null);
    }

    // Test findByIds method
    @Test
    public void testFindByIds() {
        nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        verify(nodeInstanceEntityRepository, times(1)).findByIds(Arrays.asList(1L, 2L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIds_EmptyList() {
        nodeInstanceEntityRepository.findByIds(new ArrayList<Long>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIds_NullArgument() {
        nodeInstanceEntityRepository.findByIds(null);
    }

    // Test deleteByFlowInstanceId method
    @Test
    public void testDeleteByFlowInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceId_NullArgument() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    // Test deleteByInstanceIdAndInstanceType method
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceType_NullArgument() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, null);
    }
}
