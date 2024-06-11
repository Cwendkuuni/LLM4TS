package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityId;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.SEQUENTIAL;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(instanceId, instanceType), null));

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedResult, actualResult);
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START), null), new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END), null));

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedResult, actualResult);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START), null), new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END), null));

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedResult, actualResult);
        verify(nodeInstanceEntityRepository, times(1)).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int expectedResult = 2;

        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);
        doCallRealMethod().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        when(nodeInstanceEntityRepository.findAll()).thenReturn(Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START), null), new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.END), null), new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.START), null)));

        int actualResult = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedResult, actualResult);
        verify(nodeInstanceEntityRepository, times(2)).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        int expectedResult = 1;

        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        doCallRealMethod().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        when(nodeInstanceEntityRepository.findAll()).thenReturn(Arrays.asList(new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START), null), new NodeInstanceEntity(new NodeInstanceEntityId(2L, FlowNodeType.END), null), new NodeInstanceEntity(new NodeInstanceEntityId(1L, FlowNodeType.START), null)));

        int actualResult = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedResult, actualResult);
        verify(nodeInstanceEntityRepository, times(2)).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}