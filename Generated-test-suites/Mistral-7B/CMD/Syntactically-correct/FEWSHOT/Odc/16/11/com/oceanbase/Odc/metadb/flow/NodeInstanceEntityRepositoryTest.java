package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupitermock.SpringJUnitConfig;
import org.springframework.test.context.junit.jupitermock.SpringJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@SpringJUnitConfig(classes = NodeInstanceEntityRepositoryTest.TestConfig.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null, null));
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(1L, null, null, null, flowInstanceId, null));
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        Collection<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> expected = Arrays.asList(new NodeInstanceEntity(id1, null, null, null, null, null), new NodeInstanceEntity(id2, null, null, null, null, null));
        List<NodeInstanceEntity> result = repository.findByIds(ids);
        Assert.assertEquals(expected, result);

        ids = Collections.singletonList(null);
        result = repository.findByIds(ids);
        Assert.assertNull(result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expected = 1;
        int result = repository.deleteByFlowInstanceId(instanceId);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expected = 1;
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assert.assertEquals(expected, result);
    }

    static class TestConfig {
        // Empty configuration class
    }
}