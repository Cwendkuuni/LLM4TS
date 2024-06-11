package com.oceanbase.odc.service.flow.test;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private Long flowInstanceId;

    @Before
    public void setup() {
        flowInstanceId = 1L;
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity2.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, instanceType, flowableElementType);
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1.getInstanceId(), nodeInstanceEntities.get(0).getInstanceId());
        assertEquals(nodeInstanceEntity2.getInstanceId(), nodeInstanceEntities.get(1).getInstanceId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNotNull(nodeInstanceEntities);
        assertEquals(0, nodeInstanceEntities.size());

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNotNull(nodeInstanceEntities);
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity.getInstanceId(), nodeInstanceEntities.get(0).getInstanceId());
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(id1);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(id2);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setInstanceId(id3);
        nodeInstanceEntity3.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity3);

        List<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, nodeInstanceEntities.size());
        assertNull(nodeInstanceEntities.get(1));
        assertEquals(nodeInstanceEntity1.getInstanceId(), nodeInstanceEntities.get(0).getInstanceId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        int deletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(1, deletedCount);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNull(nodeInstanceEntities);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, deletedCount);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, FlowNodeType.START, FlowableElementType.TASK);
        assertNull(nodeInstanceEntities);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, FlowNodeType.END, FlowableElementType.TASK);
        assertNotNull(nodeInstanceEntities);
        assertEquals(1, nodeInstanceEntities.size());
    }
}