
package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import org.junit.jupiter.api.After;
import org.junit.jupiter.api.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setUp() {
        nodeInstanceEntity = new NodeInstanceEntity();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
            .thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.SEQUENCE_FLOW);
        assertEquals(expected, actual);

        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(expected, actual);

        verify(nodeInstanceEntityRepository).findByFlowInstanceId(anyLong());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expected = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByIds(any())).thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        assertEquals(expected.size(), actual.size());

        verify(nodeInstanceEntityRepository).findByIds(any());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);

        assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));

        verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(anyLong());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class)))
            .thenReturn(1);

        assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START));

        verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class));
    }

    @After
    public void tearDown() {
        nodeInstanceEntity = null;
    }
}
