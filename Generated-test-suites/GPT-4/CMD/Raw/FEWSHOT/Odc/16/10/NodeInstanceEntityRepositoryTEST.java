package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntityRepository mockNodeInstanceEntityRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        when(mockNodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, 
                FlowNodeType.START, FlowableElementType.BOUNDARY)).thenReturn(nodeInstanceEntities);
        assertEquals(nodeInstanceEntities, nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, 
                FlowNodeType.START, FlowableElementType.BOUNDARY));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        when(mockNodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(nodeInstanceEntities);
        assertEquals(nodeInstanceEntities, nodeInstanceEntityRepository.findByFlowInstanceId(1L));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        when(mockNodeInstanceEntityRepository.findByIds(ids)).thenReturn(nodeInstanceEntities);
        assertEquals(nodeInstanceEntities, nodeInstanceEntityRepository.findByIds(ids));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(mockNodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(mockNodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START)).thenReturn(1);
        assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START));
    }
}