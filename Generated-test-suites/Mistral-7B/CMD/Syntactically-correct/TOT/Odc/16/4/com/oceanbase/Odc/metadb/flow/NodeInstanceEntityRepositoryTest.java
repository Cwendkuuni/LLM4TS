package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityService nodeInstanceEntityService;

    private Long flowInstanceId;
    private Long instanceId;
    private Long instanceTypeId;
    private Long flowableElementTypeId;
    private NodeInstanceEntity nodeInstanceEntity;
    private List<NodeInstanceEntity> nodeInstanceEntities;

    @Before
    public void setUp() {
        flowInstanceId = UUID.randomUUID().longValue();
        instanceId = UUID.randomUUID().longValue();
        instanceTypeId = 1L;
        flowableElementTypeId = 1L;
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.PROCESS);
        nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId,
                FlowNodeType.TASK, FlowableElementType.PROCESS))
                .thenReturn(nodeInstanceEntities);
        List<NodeInstanceEntity> result = nodeInstanceEntityService.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId,
                FlowNodeType.TASK, FlowableElementType.PROCESS);
        assertEquals(nodeInstanceEntities, result);
        verify(nodeInstanceEntityRepository, times(1))
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, FlowNodeType.TASK,
                        FlowableElementType.PROCESS);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId,
                FlowNodeType.TASK, FlowableElementType.PROCESS))
                .thenReturn(new ArrayList<>());
        List<NodeInstanceEntity> result = nodeInstanceEntityService.findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId,
                FlowNodeType.TASK, FlowableElementType.PROCESS);
        assertTrue(result.isEmpty());
        verify(nodeInstanceEntityRepository, times(1))
                .findByInstanceIdAndInstanceTypeAndFlowableElementType(flowInstanceId, FlowNodeType.TASK,
                        FlowableElementType.PROCESS);
    }

    // Add other test cases for other methods in the repository interface and their expected behavior here
}