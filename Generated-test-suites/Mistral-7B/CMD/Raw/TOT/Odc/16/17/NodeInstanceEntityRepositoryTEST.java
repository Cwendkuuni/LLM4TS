package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstance;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceId;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @InjectMocks
    private NodeInstanceEntityRepository testSubject;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITI_SEQUENCE_FLOW;
        NodeInstanceEntity nodeInstanceEntity1 = createNodeInstanceEntity(1L, instanceId, instanceType, flowableElementType);
        NodeInstanceEntity nodeInstanceEntity2 = createNodeInstanceEntity(2L, instanceId, instanceType, flowableElementType);

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = testSubject.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity1 = createNodeInstanceEntity(1L, null, null, null);
        NodeInstanceEntity nodeInstanceEntity2 = createNodeInstanceEntity(2L, flowInstanceId, null, null);

        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = testSubject.findByFlowInstanceId(flowInstanceId);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);
        NodeInstanceEntity nodeInstanceEntity1 = createNodeInstanceEntity(1L, null, null, null);
        NodeInstanceEntity nodeInstanceEntity2 = createNodeInstanceEntity(2L, null, null, null);

        when(repository.findByIds(ids)).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = testSubject.findByIds(ids);

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedDeleteCount = 2;

        NodeInstanceEntity nodeInstanceEntity1 = createNodeInstanceEntity(1L, instanceId, null, null);
        NodeInstanceEntity nodeInstanceEntity2 = createNodeInstanceEntity(2L, instanceId, null, null);

        when(repository.findByFlowInstanceId(instanceId)).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        int actualDeleteCount = testSubject.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedDeleteCount, actualDeleteCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedDeleteCount = 2;

        NodeInstanceEntity nodeInstanceEntity1 = createNodeInstanceEntity(1L, instanceId, instanceType, null);
        NodeInstanceEntity nodeInstanceEntity2 = createNodeInstanceEntity(2L, instanceId, instanceType, null);

        when(repository.findByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(Arrays.asList(nodeInstanceEntity1,
                nodeInstanceEntity2));

        int actualDeleteCount = testSubject.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedDeleteCount, actualDeleteCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_noEntitiesFound() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        int expectedDeleteCount = 0;

        when(repository.findByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(Collections.emptyList());

        int actualDeleteCount = testSubject.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedDeleteCount, actualDeleteCount);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteByInstanceIdAndInstanceType_unsupportedOperation() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        NodeInstanceEntity nodeInstanceEntity = createNodeInstanceEntity(1L, instanceId, instanceType, null);

        when(repository.findByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(Collections.singletonList(nodeInstanceEntity));

        testSubject.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_noEntitiesFound() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITI_SEQUENCE_FLOW;

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = testSubject.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType);

        assertTrue(result.isEmpty());
    }

    private NodeInstanceEntity createNodeInstanceEntity(Long id, Long instanceId, FlowNodeType instanceType,
            FlowableElementType flowableElementType) {
        NodeInstanceEntity nodeInstanceEntity = mock(NodeInstanceEntity.class);
        when(nodeInstanceEntity.getId()).thenReturn(id);
        when(nodeInstanceEntity.getInstanceId()).thenReturn(instanceId);
        when(nodeInstanceEntity.getInstanceType()).thenReturn(instanceType);
        when(nodeInstanceEntity.getFlowableElementType()).thenReturn(flowableElementType);
        return nodeInstanceEntity;
    }
}