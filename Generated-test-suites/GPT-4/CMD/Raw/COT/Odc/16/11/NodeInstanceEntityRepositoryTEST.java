package com.oceanbase.odc.metadb.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    NodeInstanceEntity node;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType(){
        Long instanceId = 1L;
        FlowNodeType flowNodeType = new FlowNodeType();
        FlowableElementType flowableElementType = new FlowableElementType();
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, flowNodeType, flowableElementType)).thenReturn(Arrays.asList(node));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, flowNodeType, flowableElementType);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId(){
        Long instanceId = 1L;
        when(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId)).thenReturn(Arrays.asList(node));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindByIds(){
        Collection<Long> ids = Arrays.asList(1L, 2L);
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(Arrays.asList(node));
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(result);
        assertFalse(result.isEmpty());
     }

    @Test
    public void testDeleteByFlowInstanceId(){
        Long instanceId = 1L;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(result, 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType(){
        Long instanceId = 1L;
        FlowNodeType flowNodeType = new FlowNodeType();
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, flowNodeType)).thenReturn(1);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, flowNodeType);
        assertNotNull(result);
        assertEquals(result, 1);
    }
}