package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityId;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private FlowInstanceEntity flowInstanceEntity;

    @Before
    public void setUp() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.PROCESS);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.PROCESS);

        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setInstanceId(3L);
        nodeInstanceEntity3.setFlowInstanceId(1L);
        nodeInstanceEntity3.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity3.setFlowableElementType(FlowableElementType.PROCESS);

        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity1);
        nodeInstanceEntities.add(nodeInstanceEntity2);
        nodeInstanceEntities.add(nodeInstanceEntity3);

        flowInstanceEntity = mock(FlowInstanceEntity.class);
        when(flowInstanceEntity.getId()).thenReturn(1L);

        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong())).thenReturn(nodeInstanceEntities);
        when(nodeInstanceEntityRepository.findById(anyLong())).thenReturn(nodeInstanceEntity1);
        when(nodeInstanceEntityRepository.save(any(NodeInstanceEntity.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        Long instanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(nodeInstanceEntities);
        assertEquals(3, nodeInstanceEntities.size());
        assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity1));
        assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity2));
        assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity3));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(nodeInstanceEntities);
        assertEquals(3, nodeInstanceEntities.size());
        assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity1));
        assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity2));
        assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity3));
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(nodeInstanceEntities);
        assertEquals(2, nodeInstanceEntities.size());
        assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity1));
        assertFalse(nodeInstanceEntities.contains(nodeInstanceEntity3));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int deletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(3, deletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;

        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, deletedCount);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(nodeInstanceEntity);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAll() {
        nodeInstanceEntityRepository.findAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAllByOrderBy() {
        Sort sort = Sort.by(Sort.Direction.ASC, "instanceId");
        nodeInstanceEntityRepository.findAll(sort);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCount() {
        nodeInstanceEntityRepository.count();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExistsById() {
        nodeInstanceEntityRepository.existsById(1L);
    }
}