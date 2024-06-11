package com.oceanbase.odc.metadb.flow.test;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-test.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setup() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.PROCESS);
    }

    @After
    public void teardown() {
        // Clear the repository after each test
        nodeInstanceEntityRepository.deleteAll();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        nodeInstanceEntityRepository.save(nodeInstanceEntity);
        Long instanceId = nodeInstanceEntity.getInstanceId();
        FlowNodeType instanceType = nodeInstanceEntity.getInstanceType();
        FlowableElementType flowableElementType = nodeInstanceEntity.getFlowableElementType();
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(nodeInstanceEntity);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(1, actualNodes.size());
        assertEquals(expectedNodes, actualNodes);
    }

    @Test
    public void testFindByFlowInstanceId() {
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);
        Long flowInstanceId = nodeInstanceEntity.getFlowInstanceId();

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(1, actualNodes.size());
        assertNotNull(actualNodes.get(0).getNodeInstanceId());
    }

    @Test
    public void testFindByIds() {
        nodeInstanceEntityRepository.save(nodeInstanceEntity);
        Long instanceId = nodeInstanceEntity.getInstanceId();

        List<Long> ids = Collections.singletonList(instanceId);

        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(1, actualNodes.size());
        assertNotNull(actualNodes.get(0).getNodeInstanceId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        nodeInstanceEntityRepository.save(nodeInstanceEntity);
        Long flowInstanceId = nodeInstanceEntity.getFlowInstanceId();

        int deletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(1, deletedCount);
        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findAll();
        assertEquals(0, actualNodes.size());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        nodeInstanceEntityRepository.save(nodeInstanceEntity);
        Long instanceId = nodeInstanceEntity.getInstanceId();
        FlowNodeType instanceType = nodeInstanceEntity.getInstanceType();

        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, deletedCount);
        List<NodeInstanceEntity> actualNodes = nodeInstanceEntityRepository.findAll();
        assertEquals(0, actualNodes.size());
    }
}