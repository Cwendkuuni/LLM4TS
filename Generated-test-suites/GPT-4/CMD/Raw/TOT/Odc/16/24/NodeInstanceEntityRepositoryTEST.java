package com.oceanbase.odc.metadb.flow;

import static org.mockito.BDDMockito.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository repository;

    @Mock
    private NodeInstanceEntityRepository mockRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long id = 100L;
        FlowNodeType flowNodeType = FlowNodeType.SOME_FLOW_NODE_TYPE;
        FlowableElementType flowableElementType = FlowableElementType.SOME_FLOWABLE_ELEMENT_TYPE;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        
        given(mockRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(id, flowNodeType, flowableElementType))
            .willReturn(Collections.singletonList(nodeInstanceEntity));
        
        List<NodeInstanceEntity> actualEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(id, flowNodeType, flowableElementType);
        assertEquals(1, actualEntities.size());
        assertEquals(nodeInstanceEntity, actualEntities.get(0));
    }

    // Similarly, test cases for other methods can be written....

}