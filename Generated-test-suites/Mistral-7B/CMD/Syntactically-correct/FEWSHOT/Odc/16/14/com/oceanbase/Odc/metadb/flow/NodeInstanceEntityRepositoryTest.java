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
import com.oceanbase.odc.metadb.flow.entity.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowInstance;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;
    private Long instanceId = 1L;
    private Long flowInstanceId = 1L;
    private FlowableElementType flowableElementType = FlowableElementType.START;
    private FlowNodeType instanceType = FlowNodeType.START;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = Mockito.spy(NodeInstanceEntityRepository.class);
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);

        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntity2.setFlowableElementType(flowableElementType);

        when(nodeInstanceEntityRepository.findById(instanceId)).thenReturn(nodeInstanceEntity1);
        when(nodeInstanceEntityRepository.findAll()).thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals("Returned expected NodeInstanceEntity", nodeInstanceEntity1, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        verify(nodeInstanceEntityRepository, times(1)).findByFlowInstanceId(flowInstanceId);
        assertEquals("Returned expected number of NodeInstanceEntities", 2, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(instanceId, 2L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        verify(nodeInstanceEntityRepository, times(1)).findByIds(ids);
        assertEquals("Returned expected number of NodeInstanceEntities", 2, nodeInstanceEntities.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int deletedCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        verify(nodeInstanceEntityRepository, times(1)).deleteAllInBatch();
        assertEquals("Expected number of deleted NodeInstanceEntities", 2, deletedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int deletedCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        verify(nodeInstanceEntityRepository, times(1)).delete(nodeInstanceEntity1);
        assertEquals("Expected number of deleted NodeInstanceEntities", 1, deletedCount);
    }
}