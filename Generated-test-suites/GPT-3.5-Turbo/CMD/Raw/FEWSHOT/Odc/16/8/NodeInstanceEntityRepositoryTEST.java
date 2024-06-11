package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class NodeInstanceEntityRepositoryTest {

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TYPE_A, FlowableElementType.TYPE_X);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(1L);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        List<NodeInstanceEntity> result = repository.findByIds(Collections.singleton(1L));
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        int result = repository.deleteByFlowInstanceId(1L);
        assertEquals(0, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        int result = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TYPE_A);
        assertEquals(0, result);
    }
}