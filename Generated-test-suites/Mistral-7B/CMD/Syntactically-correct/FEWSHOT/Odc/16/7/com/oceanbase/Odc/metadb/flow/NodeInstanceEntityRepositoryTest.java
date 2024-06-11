package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedNodeInstanceEntities, nodeInstanceEntities);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNodeInstanceEntities, nodeInstanceEntities);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedNodeInstanceEntities, nodeInstanceEntities);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedDeleteCount = 2;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(expectedDeleteCount);

        int deleteCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedDeleteCount, deleteCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expectedDeleteCount = 2;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(expectedDeleteCount);

        int deleteCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedDeleteCount, deleteCount);
    }

    @Test
    public void testEmptyFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Collections.emptyList();

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertFalse(nodeInstanceEntities.isEmpty());
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testEmptyFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Collections.emptyList();

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertFalse(nodeInstanceEntities.isEmpty());
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testEmptyFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> expectedNodeInstanceEntities = Collections.emptyList();

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedNodeInstanceEntities);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertFalse(nodeInstanceEntities.isEmpty());
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAll() {
        when(nodeInstanceEntityRepository.findAll()).thenCallRealMethod();

        nodeInstanceEntityRepository.findAll();
    }
}