package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<NodeInstanceEntity> query;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(entityManager.createQuery(anyString(), eq(NodeInstanceEntity.class))).thenReturn(query);
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SEQUENCE_FLOW, FlowableElementType.SEQUENCE_FLOW);
        verify(entityManager, times(1)).createQuery(anyString(), eq(NodeInstanceEntity.class));
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(entityManager.createQuery(anyString(), eq(NodeInstanceEntity.class))).thenReturn(query);
        nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        verify(entityManager, times(1)).createQuery(anyString(), eq(NodeInstanceEntity.class));
    }

    @Test
    public void testFindByIds() {
        when(entityManager.createQuery(anyString(), eq(NodeInstanceEntity.class))).thenReturn(query);
        nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        verify(entityManager, times(1)).createQuery(anyString(), eq(NodeInstanceEntity.class));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(entityManager.createQuery(anyString(), eq(NodeInstanceEntity.class))).thenReturn(query);
        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        verify(entityManager, times(1)).createQuery(anyString(), eq(NodeInstanceEntity.class));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(entityManager.createQuery(anyString(), eq(NodeInstanceEntity.class))).thenReturn(query);
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.SEQUENCE_FLOW);
        verify(entityManager, times(1)).createQuery(anyString(), eq(NodeInstanceEntity.class));
    }
}