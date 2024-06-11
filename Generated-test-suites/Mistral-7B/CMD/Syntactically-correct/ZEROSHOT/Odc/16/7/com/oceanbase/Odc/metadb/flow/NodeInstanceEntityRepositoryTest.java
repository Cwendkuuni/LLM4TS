package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity node1;
    private NodeInstanceEntity node2;
    private NodeInstanceEntity node3;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        node1 = new NodeInstanceEntity();
        node1.setInstanceId(1L);
        node1.setInstanceType(FlowNodeType.START_EVENT);
        node1.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceEntityRepository.save(node1);

        node2 = new NodeInstanceEntity();
        node2.setInstanceId(2L);
        node2.setInstanceType(FlowNodeType.END_EVENT);
        node2.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceEntityRepository.save(node2);

        node3 = new NodeInstanceEntity();
        node3.setInstanceId(3L);
        node3.setInstanceType(FlowNodeType.TASK);
        node3.setFlowableElementType(FlowableElementType.GATEWAY);
        nodeInstanceEntityRepository.save(node3);
    }

    @After
    public void tearDown() {
        Arrays.asList(node1, node2, node3).forEach(node -> nodeInstanceEntityRepository.delete(node));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.TASK);
        assertEquals(1, nodes.size());
        assertEquals(node1, nodes.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = node1.getFlowInstanceId();
        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(3, nodes.size());
        assertTrue(nodes.contains(node1));
        assertTrue(nodes.contains(node2));
        assertTrue(nodes.contains(node3));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, nodes.size());
        assertTrue(nodes.contains(node1));
        assertTrue(nodes.contains(node2));
        assertTrue(nodes.contains(node3));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = node1.getFlowInstanceId();
        int numBeforeDelete = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId).size();
        nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        int numAfterDelete = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId).size();
        assertEquals(numBeforeDelete - 1, numAfterDelete);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = node1.getInstanceId();
        FlowNodeType instanceType = node1.getInstanceType();
        int numBeforeDelete = nodeInstanceEntityRepository.findAll().size();
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        int numAfterDelete = nodeInstanceEntityRepository.findAll().size();
        assertEquals(numBeforeDelete - 1, numAfterDelete);
    }

    @Test
    public void testFindEmpty() {
        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(4L,
                FlowNodeType.START_EVENT, FlowableElementType.TASK);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testFindNull() {
        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null,
                FlowNodeType.START_EVENT, FlowableElementType.TASK);
        assertEquals(0, nodes.size());
    }
}