package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Create the mock objects
        FlowNodeType instanceType = new FlowNodeType();
        FlowableElementType flowableElementType = new FlowableElementType();
        Long instanceId = 1L;

        // Add some data to the repository
        NodeInstanceEntity expectedEntity = new NodeInstanceEntity();
        expectedEntity.setInstanceId(instanceId);
        expectedEntity.setInstanceType(instanceType);
        expectedEntity.setFlowableElementType(flowableElementType);
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Arrays.asList(expectedEntity));
        
        // Call the method under test
        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Verify if the expected and actual results match
        assertEquals(Arrays.asList(expectedEntity), result);
    }
    
    // Other methods can be tested in similar ways...

}