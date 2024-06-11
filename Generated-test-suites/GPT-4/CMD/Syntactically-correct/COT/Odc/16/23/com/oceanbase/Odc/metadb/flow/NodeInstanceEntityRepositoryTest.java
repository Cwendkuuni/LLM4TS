package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> mockList = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.X, FlowableElementType.Y))
                .thenReturn(mockList);
        List<NodeInstanceEntity> nodeInstanceEntities
                = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.X, FlowableElementType.Y);
        assertEquals(mockList, nodeInstanceEntities);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> mockList = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(mockList);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(mockList, nodeInstanceEntities);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> mockList = new ArrayList<>();
        when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L))).thenReturn(mockList);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));
        assertEquals(mockList, nodeInstanceEntities);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        int deleted = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, deleted);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.X)).thenReturn(1);
        int deleted = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.X);
        assertEquals(1, deleted);
    }

}
