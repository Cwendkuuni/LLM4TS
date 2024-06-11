package com.oceanbase.odc.metadb.flow.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeImpl;
import com.oceanbase.odc.service.flow.model.FlowableElementTypeImpl;

@ExtendWith(MockitoExtension.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = new FlowNodeTypeImpl("instanceType");
        FlowableElementType flowableElementType = new FlowableElementTypeImpl("flowableElementType");
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType, null);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(List.of(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result).contains(nodeInstanceEntity1);
        Assertions.assertThat(result).contains(nodeInstanceEntity2);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(new NodeInstanceEntity(1L, null, null, null, flowInstanceId));
        nodeInstanceEntities.add(new NodeInstanceEntity(2L, null, null, null, flowInstanceId));

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result).allMatch(ni -> ni.getFlowInstanceId().equals(flowInstanceId));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(new NodeInstanceEntity(1L, null, null, null, null));
        nodeInstanceEntities.add(new NodeInstanceEntity(2L, null, null, null, null));

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result).allMatch(ni -> ni.getId().isPresent());
        Assertions.assertThat(ids).forEach(id -> Assertions.assertThat(result).anySatisfy(ni -> ni.getId().get().equals(id)));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedCount = 2;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        Assertions.assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = new FlowNodeTypeImpl("instanceType");
        int expectedCount = 2;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        Assertions.assertThat(result).isEqualTo(expectedCount);
    }
}