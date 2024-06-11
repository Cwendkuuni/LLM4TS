package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository testInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> result = testInstance.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals("Size of result list should be equal to expected result size.", expectedResult.size(), result.size());
        verify(nodeInstanceEntityRepository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = testInstance.findByFlowInstanceId(flowInstanceId);

        assertEquals("Size of result list should be equal to expected result size.", expectedResult.size(), result.size());
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());

        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = testInstance.findByIds(ids);

        assertEquals("Size of result list should be equal to expected result size.", expectedResult.size(), result.size());
        verify(nodeInstanceEntityRepository, times(1)).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedResult = 2;

        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int result = testInstance.deleteByFlowInstanceId(instanceId);

        assertEquals("Number of deleted records should be equal to expected result.", expectedResult, result);
        verify(nodeInstanceEntityRepository, times(1)).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        int expectedResult = 2;

        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int result = testInstance.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals("Number of deleted records should be equal to expected result.", expectedResult, result);
        verify(nodeInstanceEntityRepository, times(1)).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithEmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Collections.emptyList());

        testInstance.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceId() {
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        testInstance.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        testInstance.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, null, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        testInstance.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdWithNegativeNumber() {
        Long instanceId = -1L;

        testInstance.deleteByFlowInstanceId(instanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceId() {
        Long instanceId = null;
        FlowNodeType instanceType = FlowNodeType.START;

        testInstance.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;

        testInstance.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}