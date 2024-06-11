package com.oceanbase.odc.metadb.flow;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
       List<NodeInstanceEntity> list = new ArrayList<>();
       when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(list);
       assertEquals(list, nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.SEQUENCE_FLOW));
    }

    @Test
    public void testFindByFlowInstanceId() {
       List<NodeInstanceEntity> list = new ArrayList<>();
       when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(list);
       assertEquals(list, nodeInstanceEntityRepository.findByFlowInstanceId(1L));
    }

    @Test
    public void testFindByIds() {
       List<NodeInstanceEntity> list = new ArrayList<>();
       when(nodeInstanceEntityRepository.findByIds(anyList())).thenReturn(list);
       assertEquals(list, nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L)));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
       when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);
       assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
       when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(1);
       assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START));
    }

    // Edge Cases and Exceptions
    @Test(expected = IllegalArgumentException.class)
    public void testFindByIds_withEmptyIds() {
        List<NodeInstanceEntity> list = nodeInstanceEntityRepository.findByIds(Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_withNullInstanceType() {
        List<NodeInstanceEntity> list = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null, FlowableElementType.SEQUENCE_FLOW);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByInstanceIdAndInstanceType_withNullInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
    }
}