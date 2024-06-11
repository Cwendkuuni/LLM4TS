package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class NodeInstanceEntityRepositoryTest {

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;
        FlowableElementType flowableElementType = FlowableElementType.TYPE1;

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Collection<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertNotNull(result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Long instanceId = 1L;

        int result = repository.deleteByFlowInstanceId(instanceId);

        assertEquals(0, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE1;

        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(0, result);
    }
}