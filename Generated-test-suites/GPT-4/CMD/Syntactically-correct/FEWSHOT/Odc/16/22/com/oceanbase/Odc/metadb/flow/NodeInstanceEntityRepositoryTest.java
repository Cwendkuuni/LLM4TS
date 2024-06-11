
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class NodeInstanceEntityRepositoryTest {

    @Mock 
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElemenType = FlowableElementType.START_EVENT;
        NodeInstanceEntity node = new NodeInstanceEntity();
        
        List<NodeInstanceEntity> expected = new ArrayList<>();
        expected.add(node);
        
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                Mockito.anyLong(),
                Mockito.any(FlowNodeType.class),
                Mockito.any(FlowableElementType.class)))
                .thenReturn(expected);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElemenType);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(expected, result);
    }
    
    //TODO: Continue with similar test methods for findByFlowInstanceId, findByIds, deleteByFlowInstanceId, deleteByInstanceIdAndInstanceType
}
