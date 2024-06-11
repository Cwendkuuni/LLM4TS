package com.oceanbase.odc.metadb.flow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any())).thenReturn(Arrays.asList(nodeInstanceEntity));
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.TASK);
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(Arrays.asList(nodeInstanceEntity));
        nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(anyLong());
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(anyList())).thenReturn(Arrays.asList(nodeInstanceEntity));
        nodeInstanceEntityRepository.findByIds(Arrays.asList(1L));
        verify(nodeInstanceEntityRepository, times(1)).findByIds(anyList());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(anyLong());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any())).thenReturn(1);
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(anyLong(), any());
    }
  
    // Edge case tests go here
    @Test(expected = NullPointerException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullValues() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testfindByFlowInstanceIdWithNullValues() {
        nodeInstanceEntityRepository.findByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsWithEmptyList() {
        nodeInstanceEntityRepository.findByIds(Arrays.asList());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByFlowInstanceIdWithNullValues() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullValues() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, null);
    }
}