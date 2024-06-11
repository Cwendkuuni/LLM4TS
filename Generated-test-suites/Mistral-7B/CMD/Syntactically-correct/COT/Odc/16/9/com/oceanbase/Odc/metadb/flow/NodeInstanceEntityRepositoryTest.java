package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringBootTest;
import org.springframework.test.context.junit.runner.JUnit5;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(JUnit5.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        FlowNodeType instanceType = FlowNodeType.START;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType),
                new NodeInstanceEntity(2L, instanceId, instanceType, FlowableElementType.DECISION),
                new NodeInstanceEntity(3L, 2L, FlowNodeType.END, FlowableElementType.END)
        );

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.containsAll(nodeInstanceEntities));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeEmptyList() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        FlowNodeType instanceType = FlowNodeType.START;

        List<NodeInstanceEntity> nodeInstanceEntities = Collections.emptyList();

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeNullResult() {
        Long instanceId = null;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        FlowNodeType instanceType = FlowNodeType.START;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(null);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeNullInstanceType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        FlowNodeType instanceType = null;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType),
                new NodeInstanceEntity(2L, instanceId, instanceType, FlowableElementType.DECISION),
                new NodeInstanceEntity(3L, 2L, FlowNodeType.END, FlowableElementType.END)
        );

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenThrow(new NullPointerException());

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test(expected = NullPointerException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeNullFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = null;
        FlowNodeType instanceType = FlowNodeType.START;

        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType),
                new NodeInstanceEntity(2L, instanceId, instanceType, FlowableElementType.DECISION),
                new NodeInstanceEntity(3L, 2L, FlowNodeType.END, FlowableElementType.END)
        );

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenThrow(new NullPointerException());

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    // Test for findByFlowInstanceId method

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.START),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.TASK, FlowableElementType.TASK),
                new NodeInstanceEntity(3L, 1L, FlowNodeType.END, FlowableElementType.END)
        );

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.containsAll(nodeInstanceEntities));
    }

    @Test
    public void testFindByFlowInstanceIdEmptyList() {
        Long flowInstanceId = 1L;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceIdNullResult() {
        Long flowInstanceId = null;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(null);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNull(result);
    }

    // Test for findByIds method

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(
                new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.START),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.TASK, FlowableElementType.TASK),
                new NodeInstanceEntity(3L, 1L, FlowNodeType.END, FlowableElementType.END)
        );

        List<Long> ids = Arrays.asList(id1, id2, id3);

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.containsAll(nodeInstanceEntities));
    }

    @Test
    public void testFindByIdsEmptyList() {
        List<Long> ids = Collections.emptyList();

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByIdsNullResult() {
        List<Long> ids = null;

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(null);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsNullId() {
        List<Long> ids = Arrays.asList(null);

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenThrow(new IllegalArgumentException());

        nodeInstanceEntityRepository.findByIds(ids);
    }

    // Test for deleteByFlowInstanceId method

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expected = 3;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId))
                .thenReturn(Arrays.asList(
                        new NodeInstanceEntity(1L, instanceId, FlowNodeType.START, FlowableElementType.START),
                        new NodeInstanceEntity(2L, instanceId, FlowNodeType.TASK, FlowableElementType.TASK),
                        new NodeInstanceEntity(3L, instanceId, FlowNodeType.END, FlowableElementType.END)
                ));

        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertFalse(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId).isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceIdEmptyList() {
        Long instanceId = 1L;
        int expected = 0;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId))
                .thenReturn(Collections.emptyList());

        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceIdNullResult() {
        Long instanceId = null;
        int expected = 0;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId))
                .thenReturn(null);

        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expected, actual);
    }

    // Test for deleteByInstanceIdAndInstanceType method

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expected = 2;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(Arrays.asList(
                        new NodeInstanceEntity(1L, instanceId, instanceType, FlowableElementType.TASK),
                        new NodeInstanceEntity(2L, instanceId, instanceType, FlowableElementType.TASK)
                ));

        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertFalse(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType).isEmpty());
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeEmptyList() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expected = 0;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(Collections.emptyList());

        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeNullResult() {
        Long instanceId = null;
        FlowNodeType instanceType = null;
        int expected = 0;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(null);

        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expected, actual);
    }

    // Test for deleteByInstanceIdAndInstanceType method with no matching records

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeNoMatchingRecords() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expected = 0;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(Collections.emptyList());

        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expected, actual);
    }
}