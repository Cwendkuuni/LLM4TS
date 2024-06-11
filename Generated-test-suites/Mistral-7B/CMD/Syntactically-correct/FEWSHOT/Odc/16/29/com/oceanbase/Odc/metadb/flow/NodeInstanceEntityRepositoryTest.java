package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Specification;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntitySpecifications;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository;
    private Specification<NodeInstanceEntity> specification;

    @Before
    public void setup() {
        repository = mock(NodeInstanceEntityRepository.class);
        specification = mock(NodeInstanceEntitySpecifications.class);
    }

    @Test
    @Transactional
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENTIAL;
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(new NodeInstanceEntity(instanceId, instanceType, flowableElementType));

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> nodes = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedNodes, nodes);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(new NodeInstanceEntity(1L, FlowNodeType.START, FlowableElementType.SEQUENTIAL),
                new NodeInstanceEntity(2L, FlowNodeType.END, FlowableElementType.SEQUENTIAL));

        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> nodes = repository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedNodes, nodes);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(new NodeInstanceEntity(1L, FlowNodeType.START, FlowableElementType.SEQUENTIAL),
                new NodeInstanceEntity(2L, FlowNodeType.END, FlowableElementType.SEQUENTIAL),
                new NodeInstanceEntity(3L, FlowNodeType.PROCESS, FlowableElementType.PARALLEL));

        when(repository.findByIds(ids)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> nodes = repository.findByIds(ids);

        assertEquals(expectedNodes, nodes);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedRowsAffected = 2;

        repository.deleteByFlowInstanceId(instanceId);

        verifyDeleteByFlowInstanceId(instanceId, expectedRowsAffected);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedRowsAffected = 1;

        repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        verifyDeleteByInstanceIdAndInstanceType(instanceId, instanceType, expectedRowsAffected);
    }

    private void verifyDeleteByFlowInstanceId(Long instanceId, int expectedRowsAffected) {
        verify(repository).deleteByFlowInstanceId(instanceId);
        verify(repository, times(expectedRowsAffected)).flush();
    }

    private void verifyDeleteByInstanceIdAndInstanceType(Long instanceId, FlowNodeType instanceType, int expectedRowsAffected) {
        verify(repository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        verify(repository, times(expectedRowsAffected)).flush();
    }

    @Test
    public void testEmptyFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENTIAL;
        List<NodeInstanceEntity> expectedNodes = Collections.emptyList();

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> nodes = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testNullFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = null;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENTIAL;
        List<NodeInstanceEntity> expectedNodes = Collections.emptyList();

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> nodes = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNull(nodes);
    }

    @Test
    public void testEmptyFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodes = Collections.emptyList();

        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> nodes = repository.findByFlowInstanceId(flowInstanceId);

        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testNullFindByFlowInstanceId() {
        Long flowInstanceId = null;
        List<NodeInstanceEntity> expectedNodes = Collections.emptyList();

        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> nodes = repository.findByFlowInstanceId(flowInstanceId);

        assertNull(nodes);
    }

    @Test
    public void testEmptyFindByIds() {
        List<Long> ids = Collections.emptyList();
        List<NodeInstanceEntity> expectedNodes = Collections.emptyList();

        when(repository.findByIds(ids)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> nodes = repository.findByIds(ids);

        assertTrue(nodes.isEmpty());
    }

    @Test
    public void testNullFindByIds() {
        List<Long> ids = null;
        List<NodeInstanceEntity> expectedNodes = Collections.emptyList();

        when(repository.findByIds(ids)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> nodes = repository.findByIds(ids);

        assertNull(nodes);
    }
}