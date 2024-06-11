package com.oceanbase.odc.service.flow.repository;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity expectedNodeInstanceEntity = new NodeInstanceEntity(
                1L,
                new NodeInstanceEntityId(1L, FlowNodeType.PARALLEL_GATEWAY, FlowableElementType.START_EVENT),
                "instanceName",
                1L,
                null);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), eq(FlowNodeType.PARALLEL_GATEWAY), eq(FlowableElementType.START_EVENT)))
                .thenReturn(Collections.singletonList(expectedNodeInstanceEntity));

        NodeInstanceEntity actualNodeInstanceEntity = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.PARALLEL_GATEWAY, FlowableElementType.START_EVENT)
                .get(0);

        Assertions.assertEquals(expectedNodeInstanceEntity, actualNodeInstanceEntity);
    }

    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), eq(FlowNodeType.PARALLEL_GATEWAY), eq(FlowableElementType.START_EVENT)))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.PARALLEL_GATEWAY, FlowableElementType.START_EVENT);

        assertTrue(actualNodeInstanceEntities.isEmpty());
    }

    @Test
    void testFindByFlowInstanceId() {
        NodeInstanceEntity expectedNodeInstanceEntity1 = new NodeInstanceEntity(
                1L,
                new NodeInstanceEntityId(1L, FlowNodeType.PARALLEL_GATEWAY, FlowableElementType.START_EVENT),
                "instanceName1",
                1L,
                null);

        NodeInstanceEntity expectedNodeInstanceEntity2 = new NodeInstanceEntity(
                2L,
                new NodeInstanceEntityId(2L, FlowNodeType.SEQUENTIAL_GATEWAY, FlowableElementType.END_EVENT),
                "instanceName2",
                1L,
                null);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
                .thenReturn(Arrays.asList(expectedNodeInstanceEntity1, expectedNodeInstanceEntity2));

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        Assertions.assertEquals(2, actualNodeInstanceEntities.size());
        Assertions.assertEquals(expectedNodeInstanceEntity1, actualNodeInstanceEntities.get(0));
        Assertions.assertEquals(expectedNodeInstanceEntity2, actualNodeInstanceEntities.get(1));
    }

    @Test
    void testFindByIds() {
        NodeInstanceEntity expectedNodeInstanceEntity1 = new NodeInstanceEntity(
                1L,
                new NodeInstanceEntityId(1L, FlowNodeType.PARALLEL_GATEWAY, FlowableElementType.START_EVENT),
                "instanceName1",
                1L,
                null);

        NodeInstanceEntity expectedNodeInstanceEntity2 = new NodeInstanceEntity(
                2L,
                new NodeInstanceEntityId(2L, FlowNodeType.SEQUENTIAL_GATEWAY, FlowableElementType.END_EVENT),
                "instanceName2",
                1L,
                null);

        when(nodeInstanceEntityRepository)
                .then(simpleMock -> {
                    List<NodeInstanceEntity> nodes = Arrays.asList(expectedNodeInstanceEntity1, expectedNodeInstanceEntity2);
                    when(nodes.stream()).thenReturn(nodes.stream());
                    return nodes.stream().map(n -> nodeInstanceEntityRepository.getOne(n.getId())).toList();
                });

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));

        Assertions.assertEquals(2, actualNodeInstanceEntities.size());
        Assertions.assertEquals(expectedNodeInstanceEntity1, actualNodeInstanceEntities.get(0));
        Assertions.assertEquals(expectedNodeInstanceEntity2, actualNodeInstanceEntities.get(1));
    }

    @Test
    void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(
                1L,
                new NodeInstanceEntityId(1L, FlowNodeType.PARALLEL_GATEWAY, FlowableElementType.START_EVENT),
                "instanceName1",
                instanceId,
                null);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(
                2L,
                new NodeInstanceEntityId(2L, FlowNodeType.SEQUENTIAL_GATEWAY, FlowableElementType.END_EVENT),
                "instanceName2",
                instanceId,
                null);

        when(nodeInstanceEntityRepository)
                .then(simpleMock -> {
                    List<NodeInstanceEntity> nodes = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);
                    when(nodes.stream()).thenReturn(nodes.stream());
                    when(nodeInstanceEntityRepository.findAll()).thenReturn(nodes);
                    return null;
                });

        Executable testDeleteByFlowInstanceId = () -> nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        int countBeforeDelete = nodeInstanceEntityRepository.findAll().size();

        testDeleteByFlowInstanceId.execute();

        int countAfterDelete = nodeInstanceEntityRepository.findAll().size();

        verify(nodeInstanceEntityRepository).deleteAll(eq(nodeInstanceEntityRepository.findByFlowInstanceId(instanceId)));
        assertEquals(countBeforeDelete - 2, countAfterDelete);
    }

    @Test
    void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PARALLEL_GATEWAY;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity(
                1L,
                new NodeInstanceEntityId(1L, FlowNodeType.PARALLEL_GATEWAY, FlowableElementType.START_EVENT),
                "instanceName1",
                instanceId,
                null);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity(
                2L,
                new NodeInstanceEntityId(2L, FlowNodeType.SEQUENTIAL_GATEWAY, FlowableElementType.END_EVENT),
                "instanceName2",
                instanceId,
                null);

        when(nodeInstanceEntityRepository)
                .then(simpleMock -> {
                    List<NodeInstanceEntity> nodes = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);
                    when(nodes.stream()).thenReturn(nodes.stream());
                    when(nodeInstanceEntityRepository.findAll()).thenReturn(nodes);
                    return null;
                });

        Executable testDeleteByInstanceIdAndInstanceType = () -> nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int countBeforeDelete = nodeInstanceEntityRepository.findAll().size();

        testDeleteByInstanceIdAndInstanceType.execute();

        int countAfterDelete = nodeInstanceEntityRepository.findAll().size();

        verify(nodeInstanceEntityRepository).deleteAll(eq(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType)));
        assertEquals(countBeforeDelete - 1, countAfterDelete);
    }

    @Test
    void testDeleteByInstanceIdAndInstanceType_EmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.PARALLEL_GATEWAY;

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType))
                .thenReturn(Collections.emptyList());

        Executable testDeleteByInstanceIdAndInstanceType = () -> nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int countBeforeDelete = nodeInstanceEntityRepository.findAll().size();

        testDeleteByInstanceIdAndInstanceType.execute();

        int countAfterDelete = nodeInstanceEntityRepository.findAll().size();

        verifyNoInteractions(nodeInstanceEntityRepository);
        assertEquals(countBeforeDelete, countAfterDelete);
    }
}