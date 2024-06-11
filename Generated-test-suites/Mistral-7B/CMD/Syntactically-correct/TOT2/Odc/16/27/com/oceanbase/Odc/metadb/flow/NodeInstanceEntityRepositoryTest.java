package com.oceanbase.odc.metadb.flow.test;

import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        Assertions.assertFalse(nodeInstanceEntities.isEmpty());
        // Add assertions for other required fields based on your data
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Test case for findByFlowInstanceId method
    }

    @Test
    public void testFindByIds() {
        // Test case for findByIds method
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Test case for deleteByFlowInstanceId method
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Test case for deleteByInstanceIdAndInstanceType method
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NoRecordsFound() {
        Long instanceId = -1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        Assertions.assertTrue(nodeInstanceEntities.isEmpty());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_InstanceIdIsNegative() {
        Long instanceId = -1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        });
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_InstanceTypeIsNull() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        Assertions.assertThrows(NullPointerException.class, () -> {
            nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, flowableElementType);
        });
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_FlowableElementTypeIsNull() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        Assertions.assertThrows(NullPointerException.class, () -> {
            nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null);
        });
    }
}