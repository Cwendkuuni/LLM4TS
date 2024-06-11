
package com.oceanbase.odc.metadb.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private NodeInstanceEntity nodeInstanceEntity;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.SERVICE_TASK;
        
        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType)).thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);

        List<NodeInstanceEntity> expected = Arrays.asList(nodeInstanceEntity);
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expected);

        List<NodeInstanceEntity> actual = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int expected = 1;
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId)).thenReturn(expected);

        int actual = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        int expected = 1;
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(expected);

        int actual = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expected, actual);
    }
}
