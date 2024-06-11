package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.TYPE_X;

        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        nodeList.add(new NodeInstanceEntity());

        // Mock the behavior of the repository
        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntityRepository", nodeList);

        assertEquals(nodeList, nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        nodeList.add(new NodeInstanceEntity());

        // Mock the behavior of the repository
        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntityRepository", nodeList);

        assertEquals(nodeList, nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId));
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L);

        List<NodeInstanceEntity> nodeList = new ArrayList<>();
        nodeList.add(new NodeInstanceEntity());

        // Mock the behavior of the repository
        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntityRepository", nodeList);

        assertEquals(nodeList, nodeInstanceEntityRepository.findByIds(ids));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        // Mock the behavior of the repository
        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntityRepository", 1);

        assertEquals(1, nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        // Mock the behavior of the repository
        ReflectionTestUtils.setField(nodeInstanceEntityRepository, "nodeInstanceEntityRepository", 1);

        assertEquals(1, nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType));
    }
}