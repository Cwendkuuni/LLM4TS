package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_TYPE_A;
        
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertNotNull(result);
        assertEquals(2, result.size());
        // Add more assertion as needed
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Long flowInstanceId = 1L;
        
        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(result);
        // Add assertion for specific values
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        
        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertNotNull(result);
        // Add assertion for specific values
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Long flowInstanceId = 1L;

        int result = repository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(1, result);
        // Add more assertion as needed
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntityRepository repository = new NodeInstanceEntityRepository();

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_A;

        int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
        // Add more assertion as needed
    }

}