package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity expected = new NodeInstanceEntity();
        expected.setInstanceId(instanceId);
        expected.setInstanceType(instanceType);
        expected.setFlowableElementType(flowableElementType);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actual);
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullParameters() {
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);

        assertNull(actual);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        for (NodeInstanceEntity nodeInstanceEntity : expected) {
            nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        }

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(actual);
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void testFindByFlowInstanceId_EmptyResult() {
        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;

        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        for (NodeInstanceEntity nodeInstanceEntity : expected) {
            nodeInstanceEntity.setId(id1);
        }

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(Arrays.asList(id1, id2));

        assertNotNull(actual);
        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void testFindByIds_EmptyResult() {
        List<Long> ids = Collections.emptyList();

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int expected = 2;
        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertFalse(actual < expected);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        int expected = 1;
        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertFalse(actual < expected);
    }

}