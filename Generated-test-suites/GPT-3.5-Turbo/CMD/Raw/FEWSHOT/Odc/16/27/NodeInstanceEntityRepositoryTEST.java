package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntityRepository", nodeInstanceEntityRepository);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        expectedNodes.add(node1);
        expectedNodes.add(node2);

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.NODE_TYPE;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_TYPE;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        expectedNodes.add(node1);
        expectedNodes.add(node2);

        Long flowInstanceId = 1L;

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
        NodeInstanceEntity node1 = new NodeInstanceEntity();
        NodeInstanceEntity node2 = new NodeInstanceEntity();
        expectedNodes.add(node1);
        expectedNodes.add(node2);

        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedNodes);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(actualNodes);
        assertEquals(expectedNodes.size(), actualNodes.size());
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
        FlowNodeType instanceType = FlowNodeType.NODE_TYPE;

        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
    }
}