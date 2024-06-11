package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.PROCESS, null),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.PROCESS, FlowableElementType.PROCESS, null),
                new NodeInstanceEntity(3L, 1L, FlowNodeType.END, FlowableElementType.PROCESS, null));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, 1L, FlowNodeType.START, FlowableElementType.PROCESS, null),
                new NodeInstanceEntity(2L, 1L, FlowNodeType.PROCESS, FlowableElementType.PROCESS, null),
                new NodeInstanceEntity(3L, 1L, FlowNodeType.END, FlowableElementType.PROCESS, null));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 3;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedResult = 1;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsWithNullIds() {
        nodeInstanceEntityRepository.findByIds(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNegativeInstanceId() {
        Long instanceId = -1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        Long instanceId = null;
        FlowNodeType instanceType = FlowNodeType.START;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

}