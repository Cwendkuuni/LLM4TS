package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {
    @InjectMocks
    NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expected = Arrays.asList(this.nodeInstanceEntity);
        when(this.nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START, FlowableElementType.ACTIVITY)
        ).thenReturn(expected);
        List<NodeInstanceEntity> actual = this.nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START, FlowableElementType.ACTIVITY);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expected = Arrays.asList(this.nodeInstanceEntity);
        when(this.nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(expected);
        List<NodeInstanceEntity> actual = this.nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expected = Arrays.asList(this.nodeInstanceEntity);
        when(this.nodeInstanceEntityRepository.findByIds(Arrays.asList(1L))).thenReturn(expected);
        List<NodeInstanceEntity> actual = this.nodeInstanceEntityRepository.findByIds(Arrays.asList(1L));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(this.nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        int actual = this.nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, actual);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(this.nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START)).thenReturn(1);
        int actual = this.nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, actual);
    }
}
