package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null));

        List<NodeInstanceEntity> actual = nodeInstanceInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.TASK, null), new NodeInstanceEntity(2L, 1L, FlowNodeType.TASK, FlowableElementType.TASK, null), new NodeInstanceEntity(3L, 1L, FlowNodeType.END, FlowableElementType.TASK, null));

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.TASK, null), new NodeInstanceEntity(2L, 1L, FlowNodeType.TASK, FlowableElementType.TASK, null), new NodeInstanceEntity(3L, 1L, FlowNodeType.END, FlowableElementType.TASK, null));

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int expected = 3; // assuming there are 3 NodeInstanceEntity records with flowInstanceId = 1L

        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        int expected = 1; // assuming there is 1 NodeInstanceEntity record with instanceId = 1L and instanceType = FlowNodeType.START

        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNullFlowInstanceId() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        Long instanceId = null;
        FlowNodeType instanceType = FlowNodeType.START;

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}