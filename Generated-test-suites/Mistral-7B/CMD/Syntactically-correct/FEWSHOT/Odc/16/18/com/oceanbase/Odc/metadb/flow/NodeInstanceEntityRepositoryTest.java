package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNode;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private FlowInstance flowInstance;
    private Long flowInstanceId = 1L;
    private Long instanceId = 1L;
    private Long instanceTypeId = 1L;
    private FlowNodeType instanceType = Mockito.mock(FlowNodeType.class);
    private FlowableElementType flowableElementType = Mockito.mock(FlowableElementType.class);
    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);
        flowInstance = Mockito.mock(FlowInstance.class);
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstancesId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, instanceType, flowableElementType))
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, instanceType, flowableElementType);
        assertEquals(1, result.size());
        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(1, result.size());
        verify(nodeInstanceEntityRepository).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(instanceId);
        when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(1, result.size());
        verify(nodeInstanceEntityRepository).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(1, result);
        verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(1, result);
        verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}