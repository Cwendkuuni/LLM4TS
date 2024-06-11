package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNode;
import com.oceanbase.odc.service.flow.model.FlowNodeInstance;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository;
    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;
    private List<NodeInstanceEntity> nodeInstanceEntities;

    @Before
    public void setUp() {
        repository = mock(NodeInstanceEntityRepository.class);
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowInstanceId(1L);

        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END);
        nodeInstanceEntity2.setFlowInstanceId(1L);

        nodeInstanceEntities = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);
    }

    @Test
    @Transactional
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.PROCESS))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START,
                FlowableElementType.PROCESS);

        assertEquals(nodeInstanceEntities, result);
        verify(repository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START,
                FlowableElementType.PROCESS);
    }

    @Test
    @Transactional
    public void testFindByFlowInstanceId() {
        when(repository.findByFlowInstanceId(1L)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);

        assertEquals(nodeInstanceEntities, result);
        verify(repository, times(1)).findByFlowInstanceId(1L);
    }

    @Test
    @Transactional
    public void testFindByIds() {
        when(repository.findByIds(Arrays.asList(1L, 2L))).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = repository.findByIds(Arrays.asList(1L, 2L));

        assertEquals(nodeInstanceEntities, result);
        verify(repository, times(1)).findByIds(Arrays.asList(1L, 2L));
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        when(repository.deleteByFlowInstanceId(1L)).thenReturn(1);

        int result = repository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
        verify(repository, times(1)).deleteByFlowInstanceId(1L);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        when(repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START)).thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);

        assertEquals(1, result);
        verify(repository, times(1)).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
    }
}