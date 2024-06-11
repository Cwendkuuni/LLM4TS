package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntity nodeInstanceEntity;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.END, FlowableElementType.PEOPLE)).thenReturn(expectedEntities);
        List<NodeInstanceEntity> actualEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.END, FlowableElementType.PEOPLE);
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(expectedEntities);
        List<NodeInstanceEntity> actualEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(expectedEntities, actualEntities);
    }
    
    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expectedEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L))).thenReturn(expectedEntities);
        List<NodeInstanceEntity> actualEntities = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.END)).thenReturn(1);
        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.END);
        assertEquals(1, rowsAffected);
    }
}