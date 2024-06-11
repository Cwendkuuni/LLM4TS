package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class NodeInstanceEntityRepositoryTest{

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryImpl nodeInstanceEntityRepositoryImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nie = new NodeInstanceEntity();
        List<NodeInstanceEntity> expected = Arrays.asList(nie);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class))).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepositoryImpl.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.DECISION, FlowableElementType.SEQUENCE_FLOW);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nie = new NodeInstanceEntity();
        List<NodeInstanceEntity> expected = Arrays.asList(nie);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepositoryImpl.findByFlowInstanceId(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nie = new NodeInstanceEntity();
        List<NodeInstanceEntity> expected = Arrays.asList(nie);
        when(nodeInstanceEntityRepository.findByIds(anyCollection())).thenReturn(expected);
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepositoryImpl.findByIds(Arrays.asList(1L));
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(anyLong())).thenReturn(1);
        int result = nodeInstanceEntityRepositoryImpl.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class))).thenReturn(1);
        int result = nodeInstanceEntityRepositoryImpl.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.DECISION);
        assertEquals(1, result);
    }
}