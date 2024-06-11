package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-test.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setup() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(nodeInstanceEntities);
        assertEquals(1, nodeInstanceEntities.size());
        assertNotNull(nodeInstanceEntities.get(0));
        assertEquals(instanceId, nodeInstanceEntities.get(0).getInstanceId());
        assertEquals(instanceType, nodeInstanceEntities.get(0).getInstanceType());
        assertEquals(flowableElementType, nodeInstanceEntities.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(nodeInstanceEntities);
        assertEquals(1, nodeInstanceEntities.size());
        assertNotNull(nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity.getFlowInstanceId(), nodeInstanceEntities.get(0).getFlowInstanceId());
    }

    @Test
    public void testFindByIds() {
        Long id1 = nodeInstanceEntity.getId();
        Long id2 = 2L;

        List<Long> ids = new ArrayList<Long>();
        ids.add(id1);
        ids.add(id2);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(nodeInstanceEntities);
        assertEquals(2, nodeInstanceEntities.size());
        assertNotNull(nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity.getId(), nodeInstanceEntities.get(0).getId());
        assertNull(nodeInstanceEntities.get(1));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int deletedRows = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);

        assertEquals(1, deletedRows);
        assertNull(nodeInstanceEntities);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        int deletedRows = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findOne(nodeInstanceEntity.getId());

        assertEquals(1, deletedRows);
        assertNull(nodeInstanceEntity);
    }

}