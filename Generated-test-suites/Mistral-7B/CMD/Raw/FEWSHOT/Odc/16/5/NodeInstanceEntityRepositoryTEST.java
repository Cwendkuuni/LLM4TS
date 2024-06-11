package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityId;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository;
    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;

    @Before
    public void setUp() {
        repository = mock(NodeInstanceEntityRepository.class);

        nodeInstanceEntity1 = new NodeInstanceEntity(new NodeInstanceEntityId(1L, "instance1"), FlowNodeType.START,
                FlowableElementType.PROCESS, null);

        nodeInstanceEntity2 = new NodeInstanceEntity(new NodeInstanceEntityId(2L, "instance2"), FlowNodeType.END,
                FlowableElementType.PROCESS, null);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START,
                FlowableElementType.PROCESS))
                .thenReturn(Arrays.asList(nodeInstanceEntity1));

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START,
                FlowableElementType.PROCESS);

        assertNotNull(result);
        assertTrue(result.contains(nodeInstanceEntity1));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeReturnsEmptyList() {
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START,
                FlowableElementType.PROCESS))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START,
                FlowableElementType.PROCESS);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(repository.findByFlowInstanceId(1L))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);

        assertNotNull(result);
        assertTrue(result.containsAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2)));
    }

    @Test
    public void testFindByIds() {
        when(repository.findByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = repository.findByIds(Arrays.asList(1L, 2L));

        assertNotNull(result);
        assertTrue(result.containsAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2)));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(repository.countAllByFlowInstanceId(1L)).thenReturn(2);

        int result = repository.deleteByFlowInstanceId(1L);
        assertFalse(repository.existsById(nodeInstanceEntity1.getId()));
        assertFalse(repository.existsById(nodeInstanceEntity2.getId()));
        assertEquals(2, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(repository.countAllByInstanceIdAndInstanceType(new NodeInstanceEntityId(1L, "instance1"), FlowNodeType.START))
                .thenReturn(1);

        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertFalse(repository.existsById(nodeInstanceEntity1.getId()));
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeReturnsZero() {
        when(repository.countAllByInstanceIdAndInstanceType(new NodeInstanceEntityId(1L, "instance1"), FlowNodeType.START))
                .thenReturn(0);

        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(0, result);
    }

    @Test
    public void testFindAll() {
        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity(new NodeInstanceEntityId(3L, "instance3"),
                FlowNodeType.PROCESS, FlowableElementType.DECISION, null);
        when(repository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3),
                        PageRequest.of(0, 10), (long) 3, 3));

        Page<NodeInstanceEntity> result = repository.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertTrue(result.getContent().containsAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3)));
        assertEquals(3, result.getTotalElements());
    }

}