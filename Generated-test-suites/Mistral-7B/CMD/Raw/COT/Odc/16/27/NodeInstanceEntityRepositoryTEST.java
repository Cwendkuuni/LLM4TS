package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNode;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepositoryImpl;
import com.oceanbase.odc.metadb.flow.test.builder.NodeInstanceEntityBuilder;
import com.oceanbase.odc.metadb.flow.test.builder.NodeInstanceRepositoryTestBuilder;

/**
 * Test class for NodeInstanceEntityRepository
 */
public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        nodeInstanceEntityRepository = new NodeInstanceEntityRepositoryImpl();
    }

    // Test case for findByInstanceIdAndInstanceTypeAndFlowableElementType
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        FlowNodeType instanceType = FlowNodeType.PARALLEL_GATEWAY;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(new NodeInstanceEntityBuilder()
                .withFlowInstanceId(1L)
                .withInstanceId(instanceId)
                .withInstanceType(instanceType)
                .withFlowableElementType(flowableElementType)
                .build(),
                new NodeInstanceEntityBuilder()
                        .withFlowInstanceId(2L)
                        .withInstanceId(instanceId)
                        .withInstanceType(FlowNodeType.SEQUENTIAL_GATEWAY)
                        .withFlowableElementType(flowableElementType)
                        .build());
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(2, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertEquals(instanceId, nodeInstanceEntity.getInstanceId());
            assertEquals(instanceType, nodeInstanceEntity.getInstanceType());
            assertEquals(flowableElementType, nodeInstanceEntity.getFlowableElementType());
        }
    }

    // Test case for findByFlowInstanceId
    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(new NodeInstanceEntityBuilder()
                .withFlowInstanceId(flowInstanceId)
                .build(),
                new NodeInstanceEntityBuilder()
                        .withFlowInstanceId(flowInstanceId)
                        .build());
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(2, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertEquals(flowInstanceId, nodeInstanceEntity.getFlowInstanceId());
        }
    }

    // Test case for findByIds
    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(new NodeInstanceEntityBuilder()
                .withId(1L)
                .build(),
                new NodeInstanceEntityBuilder()
                        .withId(2L)
                        .build());
        when(nodeInstanceEntityRepository.findByIds(ids)).thenAnswer(new Answer<List<NodeInstanceEntity>>() {
            @Override
            public List<NodeInstanceEntity> answer(InvocationOnMock invocation) throws Throwable {
                return nodeInstanceEntities;
            }
        });

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertEquals(ids.get(nodeInstanceEntity.getId() - 1), nodeInstanceEntity.getId());
        }
    }

    // Test case for deleteByFlowInstanceId
    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedRowsAffected = 2;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(expectedRowsAffected);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedRowsAffected, result);
    }

    // Test case for deleteByInstanceIdAndInstanceType
    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PARALLEL_GATEWAY;
        int expectedRowsAffected = 1;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(expectedRowsAffected);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedRowsAffected, result);
    }

    // Test case for empty findByInstanceIdAndInstanceTypeAndFlowableElementType
    @Test
    public void testEmptyFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        FlowNodeType instanceType = FlowNodeType.PARALLEL_GATEWAY;
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test case for null findByInstanceIdAndInstanceTypeAndFlowableElementType
    @Test
    public void testNullFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = null;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        FlowNodeType instanceType = FlowNodeType.PARALLEL_GATEWAY;
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType));
    }

    // Test case for empty findByFlowInstanceId
    @Test
    public void testEmptyFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test case for null findByFlowInstanceId
    @Test
    public void testNullFindByFlowInstanceId() {
        Long flowInstanceId = null;
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId));
    }

    // Test case for empty findByIds
    @Test
    public void testEmptyFindByIds() {
        Collection<Long> ids = Collections.emptyList();
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test case for null findByIds
    @Test
    public void testNullFindByIds() {
        Collection<Long> ids = null;
        when(nodeInstanceEntityRepository.findByIds(ids)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> nodeInstanceEntityRepository.findByIds(ids));
    }

    // Test case for deleteByFlowInstanceId with empty result
    @Test
    public void testDeleteByFlowInstanceIdWithEmptyResult() {
        Long instanceId = 1L;
        when(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId)).thenReturn(Collections.emptyList());
        int expectedRowsAffected = 0;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(expectedRowsAffected);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedRowsAffected, result);
    }

    // Test case for deleteByInstanceIdAndInstanceType with empty result
    @Test
    public void testDeleteByInstanceIdAndInstanceTypeWithEmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PARALLEL_GATEWAY;
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(Collections.emptyList());
        int expectedRowsAffected = 0;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(expectedRowsAffected);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedRowsAffected, result);
    }

}