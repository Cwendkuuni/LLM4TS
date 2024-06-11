
package com.oceanbase.odc.metadb.flow;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setup() {
        // Initialize mocks before tests
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 123L;
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        FlowableElementType flowableElementType = FlowableElementType.STEP;
        
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), nodeInstanceEntity);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 123L;

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), nodeInstanceEntity);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(123L, 456L);

        when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), nodeInstanceEntity);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 123L;

        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(result, 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 123L;
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;

        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(result, 1);
    }
}
