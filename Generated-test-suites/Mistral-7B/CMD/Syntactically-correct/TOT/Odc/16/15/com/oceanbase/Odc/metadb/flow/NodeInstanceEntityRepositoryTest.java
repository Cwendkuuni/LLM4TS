package com.oceanbase.odc.service.flow.repository;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.entity.NodeInstanceEntity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long INSTANCE_ID = 1L;
    private static final Long INSTANCE_ID_2 = 2L;
    private static final Long INSTANCE_ID_3 = 3L;
    private static final Long INSTANCE_ID_NON_EXISTENT = -1L;

    private static final Long FLOWABLE_ELEMENT_TYPE_ID = 1L;
    private static final Long FLOW_NODE_TYPE_ID = FlowNodeType.PROCESS.getId();

    private static final Long[] IDS = {INSTANCE_ID, INSTANCE_ID_2, INSTANCE_ID_3};

    @Before
    public void setup() {
        // Add test data
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity1.setInstanceId(INSTANCE_ID);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.PROCESS);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.START_EVENT);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity2.setInstanceId(INSTANCE_ID_2);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.PROCESS);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.INTERMEDIATE_EVENT);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setFlowInstanceId(FLOW_INSTANCE_ID);
        nodeInstanceEntity3.setInstanceId(INSTANCE_ID_3);
        nodeInstanceEntity3.setInstanceType(FlowNodeType.PROCESS);
        nodeInstanceEntity3.setFlowableElementType(FlowableElementType.END_EVENT);
        nodeInstanceEntityRepository.save(nodeInstanceEntity3);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID, FlowNodeType.PROCESS, FlowableElementType.START_EVENT);
        assertEquals(1, result.size());
        NodeInstanceEntity nodeInstanceEntity = result.get(0);
        assertEquals(INSTANCE_ID, nodeInstanceEntity.getInstanceId());
        assertEquals(FlowNodeType.PROCESS, nodeInstanceEntity.getInstanceType());
        assertEquals(FlowableElementType.START_EVENT, nodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(FLOW_INSTANCE_ID);
        assertEquals(3, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertEquals(FLOW_INSTANCE_ID, nodeInstanceEntity.getFlowInstanceId());
        }
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(INSTANCE_ID, INSTANCE_ID_2));
        assertEquals(2, result.size());
        for (NodeInstanceEntity nodeInstanceEntity : result) {
            assertTrue(Arrays.asList(INSTANCE_ID, INSTANCE_ID_2).contains(nodeInstanceEntity.getInstanceId()));
        }
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(FLOW_INSTANCE_ID);
        assertEquals(3, result);
        List<NodeInstanceEntity> resultAfterDelete = nodeInstanceEntityRepository.findByFlowInstanceId(FLOW_INSTANCE_ID);
        assertTrue(resultAfterDelete.isEmpty());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(INSTANCE_ID, FlowNodeType.PROCESS);
        assertEquals(1, result);
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(INSTANCE_ID).orElse(null);
        assertNull(nodeInstanceEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNonExistentInstance() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(INSTANCE_ID_NON_EXISTENT, FlowNodeType.PROCESS);
        fail("Expected an IllegalArgumentException");
    }
}