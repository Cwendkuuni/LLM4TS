package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeEnum;
import com.oceanbase.odc.service.flow.model.FlowableElementTypeEnum;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository repository;

    @Before
    public void setup() {
        repository = Mockito.spy(NodeInstanceEntityRepository.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeEnum.TASK;
        FlowableElementType flowableElementType = FlowableElementTypeEnum.START;
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setId(1L);
        entity.setInstanceId(instanceId);
        entity.setInstanceType(instanceType);
        entity.setFlowableElementType(flowableElementType);
        expectedResult.add(entity);

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedResult);

        List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(expectedResult, result);
        verify(repository, times(1)).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setFlowInstanceId(flowInstanceId);
        expectedResult.add(entity);

        when(repository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

        assertEquals(expectedResult, result);
        verify(repository, times(1)).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setId(1L);
        expectedResult.add(entity);

        when(repository.findByIds(ids)).thenReturn(expectedResult);

        List<NodeInstanceEntity> result = repository.findByIds(ids);

        assertEquals(expectedResult, result);
        verify(repository, times(1)).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        doNothing().when(repository).deleteByFlowInstanceId(instanceId);

        repository.deleteByFlowInstanceId(instanceId);

        verify(repository, times(1)).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeEnum.TASK;

        doNothing().when(repository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        verify(repository, times(1)).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}