package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNode;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeEnum;
import com.oceanbase.odc.service.flow.model.FlowableElementTypeEnum;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceRepositoryTest;

public class NodeInstanceEntityRepositoryTest extends NodeInstanceRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity expectedNodeInstanceEntity = createNodeInstanceEntity();
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Arrays.asList(expectedNodeInstanceEntity));

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeEnum.START;
        FlowableElementType flowableElementType = FlowableElementTypeEnum.PROCESS;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(1, nodeInstanceEntities.size());
        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity expectedNodeInstanceEntity = createNodeInstanceEntity();
        when(nodeInstanceEntityRepository.findByFlowInstanceId(anyLong()))
                .thenReturn(Collections.singletonList(expectedNodeInstanceEntity));

        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(1, nodeInstanceEntities.size());
        verify(nodeInstanceEntityRepository).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity expectedNodeInstanceEntity1 = createNodeInstanceEntity();
        NodeInstanceEntity expectedNodeInstanceEntity2 = createNodeInstanceEntity();

        when(nodeInstanceEntityRepository.findByIds(argThat(c -> c.containsAll(Arrays.asList(1L, 2L)))))
                .thenReturn(Arrays.asList(expectedNodeInstanceEntity1, expectedNodeInstanceEntity2));

        Collection<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, nodeInstanceEntities.size());
        verify(nodeInstanceEntityRepository).findByIds(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(anyLong());

        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
        verify(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class));

        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeTypeEnum.START;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, result);
        verify(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }
}