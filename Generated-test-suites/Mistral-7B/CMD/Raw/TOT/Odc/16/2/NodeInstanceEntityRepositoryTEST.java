package com.oceanbase.odc.metadb.flow.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START;

        FlowInstance flowInstance = Mockito.mock(FlowInstance.class);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(Mockito.mock(NodeInstanceEntity.class));
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(Mockito.mock(NodeInstanceEntity.class), Mockito.mock(NodeInstanceEntity.class));
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(Mockito.mock(NodeInstanceEntity.class), Mockito.mock(NodeInstanceEntity.class));
        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 2;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int actualResult = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expectedResult = 2;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int actualResult = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFindEmptyByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START;

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actualResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId_Empty() {
        Long instanceId = 1L;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int actualResult = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(0, actualResult);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_Empty() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int actualResult = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(0, actualResult);
    }
}