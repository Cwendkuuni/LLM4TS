package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private List<NodeInstanceEntity> nodeInstanceEntities;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        nodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(1L, FlowNodeType.START, FlowableElementType.TASK, "test1"),
                new NodeInstanceEntity(2L, FlowNodeType.END, FlowableElementType.TASK, "test2"),
                new NodeInstanceEntity(3L, FlowNodeType.PROCESSOR, FlowableElementType.TASK, "test3"));
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class),
                any(FlowableElementType.class))).thenReturn(nodeInstanceEntities);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(nodeInstanceEntities);
        when(nodeInstanceEntityRepository.findByIds(anyCollection())).thenReturn(nodeInstanceEntities);
        when(nodeInstanceEntityRepository.countAll()).thenReturn(3L);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESSOR;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(1, result.size());
        assertEquals(new NodeInstanceEntity(1L, FlowNodeType.START, FlowableElementType.TASK, "test1"), result.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, result.size());
        verify(nodeInstanceEntityRepository, times(1)).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        verify(nodeInstanceEntityRepository, times(1)).deleteAllInBatch();
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PROCESSOR;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        verify(nodeInstanceEntityRepository, times(1)).deleteAllInBatch();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteAll() {
        nodeInstanceEntityRepository.deleteAll();
    }

    @Test
    public void testCountAll() {
        Long result = nodeInstanceEntityRepository.countAll();
        assertEquals(3, result.intValue());
    }
}