package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setUp() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(101L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.Execution);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);
        nodeInstanceEntity = entityManager.persist(nodeInstanceEntity);
        entityManager.flush();
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> found = nodeInstanceEntityRepository
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceEntity.getInstanceId(), nodeInstanceEntity.getInstanceType(), nodeInstanceEntity.getFlowableElementType());

        assertEquals(1, found.size());
        assertEquals(nodeInstanceEntity.getInstanceId(), found.get(0).getInstanceId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> found = nodeInstanceEntityRepository
                .findByFlowInstanceId(nodeInstanceEntity.getFlowInstanceId());

        assertEquals(1, found.size());
        assertEquals(nodeInstanceEntity.getFlowInstanceId(), found.get(0).getFlowInstanceId());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> found = nodeInstanceEntityRepository
                .findByIds(Arrays.asList(nodeInstanceEntity.getId()));

        assertEquals(1, found.size());
        assertEquals(nodeInstanceEntity.getId(), found.get(0).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int deleted = nodeInstanceEntityRepository
                .deleteByFlowInstanceId(nodeInstanceEntity.getFlowInstanceId());

        assertEquals(1, deleted);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int deleted = nodeInstanceEntityRepository
                .deleteByInstanceIdAndInstanceType(nodeInstanceEntity.getInstanceId(), nodeInstanceEntity.getInstanceType());

        assertEquals(1, deleted);
    }
}
