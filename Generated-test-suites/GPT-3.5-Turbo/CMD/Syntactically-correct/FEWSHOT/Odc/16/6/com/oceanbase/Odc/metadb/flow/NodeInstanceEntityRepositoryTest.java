package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_1;
        FlowableElementType flowableElementType = FlowableElementType.ELEMENT_TYPE_1;
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        
        assertNotNull(result);
        // Add more assertions based on the actual data in the database
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        
        assertNotNull(result);
        // Add more assertions based on the actual data in the database
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        
        assertNotNull(result);
        // Add more assertions based on the actual data in the database
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        
        assertEquals(1, result);
        // Add more assertions based on the actual data in the database
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TYPE_1;
        
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        
        assertEquals(1, result);
        // Add more assertions based on the actual data in the database
    }
}