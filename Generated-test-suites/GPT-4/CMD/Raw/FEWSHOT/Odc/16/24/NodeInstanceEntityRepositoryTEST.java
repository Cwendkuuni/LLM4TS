package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class NodeInstanceEntityRepositoryTest {

    @Autowired
    NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.CATCH_EVENT;
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertNotNull(result);
        // Add more specific checks as appropriate
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 2L;
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);
        assertNotNull(result);
        // Add more specific checks as appropriate
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(3L, 5L, 7L);
        List<NodeInstanceEntity> result = repository.findByIds(ids);
        assertNotNull(result);
        // Add more specific checks as appropriate
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 9L;
        int result = repository.deleteByFlowInstanceId(instanceId);
        assertTrue(result >= 0);
        // Add more specific checks as appropriate
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 10L;
        FlowNodeType instanceType = FlowNodeType.BOUNDARY_EVENT;
        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertTrue(result >= 0);
        // Add more specific checks as appropriate
    }
    
}