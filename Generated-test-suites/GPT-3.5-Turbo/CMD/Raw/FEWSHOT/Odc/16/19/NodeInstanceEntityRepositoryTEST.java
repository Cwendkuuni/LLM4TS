package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryImpl nodeInstanceEntityRepositoryImpl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SOME_TYPE;
        FlowableElementType flowableElementType = FlowableElementType.SOME_ELEMENT_TYPE;

        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntities.add(nodeInstanceEntity1);
        nodeInstanceEntities.add(nodeInstanceEntity2);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntities.add(nodeInstanceEntity1);
        nodeInstanceEntities.add(nodeInstanceEntity2);

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntities.add(nodeInstanceEntity1);
        nodeInstanceEntities.add(nodeInstanceEntity2);

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SOME_TYPE;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(1, result);
    }
}