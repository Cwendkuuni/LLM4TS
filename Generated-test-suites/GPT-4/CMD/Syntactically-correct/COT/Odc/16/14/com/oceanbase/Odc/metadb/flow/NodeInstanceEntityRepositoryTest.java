package com.oceanbase.odc.metadb.flow;
import com.oceanbase.odc.metadb.flow.*;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Initialize a NodeInstanceEntity and persist
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        // populate nodeInstanceEntity with data
        entityManager.persist(nodeInstanceEntity);

        // Retrieve and assert
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assert(result.get(0).equals(nodeInstanceEntity));
    }

   @Test
    public void testFindByFlowInstanceId() {
        // Scenario similar to above test
    }

    @Test
    public void testFindByIds() {
        // Scenario similar to above test, use a collection of ids
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        // initialize nodeInstanceEntity and persist with data

        repository.deleteByFlowInstanceId(instanceId);
        // validation after deletion
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        // initialize nodeInstanceEntity and persist with data

        repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        // validation after deletion
    }
}