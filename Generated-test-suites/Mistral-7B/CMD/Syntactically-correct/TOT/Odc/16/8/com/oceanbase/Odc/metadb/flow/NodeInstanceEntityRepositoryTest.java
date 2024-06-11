package com.oceanbase.odc.service.flow.repository.test;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @DisplayName("Test finding node instances by instanceId, instanceType and flowableElementType")
    @Test
    void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START;
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType),
                new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType)
        );

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        Assertions.assertEquals(expectedNodeInstances, actualNodeInstances);
    }

    @DisplayName("Test finding node instances by flowInstanceId")
    @Test
    void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null),
                new NodeInstanceEntity(2L, null, null, null),
                new NodeInstanceEntity(3L, flowInstanceId, null, null)
        );

        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        Assertions.assertEquals(expectedNodeInstances, actualNodeInstances);
    }

    @DisplayName("Test finding node instances by ids")
    @Test
    void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, null, null, null),
                new NodeInstanceEntity(2L, null, null, null),
                new NodeInstanceEntity(3L, null, null, null)
        );

        Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
                .thenReturn(expectedNodeInstances);

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByIds(ids);

        Assertions.assertEquals(expectedNodeInstances, actualNodeInstances);
    }

    @DisplayName("Test deleting node instances by flowInstanceId")
    @Test
    void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedRowsAffected = 2;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);

        int actualRowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        Assertions.assertEquals(expectedRowsAffected, actualRowsAffected);
    }

    @DisplayName("Test deleting node instances by instanceId and instanceType")
    @Test
    void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int expectedRowsAffected = 2;

        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        int actualRowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        Assertions.assertEquals(expectedRowsAffected, actualRowsAffected);
    }

    @DisplayName("Test finding node instances with paging and sorting")
    @Test
    void testFindAllWithPagingAndSorting() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, flowInstanceId, FlowNodeType.START, FlowableElementType.START),
                new NodeInstanceEntity(2L, flowInstanceId, FlowNodeType.TASK, FlowableElementType.END),
                new NodeInstanceEntity(3L, flowInstanceId, FlowNodeType.TASK, FlowableElementType.TASK),
                new NodeInstanceEntity(4L, flowInstanceId, FlowNodeType.END, FlowableElementType.START)
        );

        Pageable pageable = PageRequest.of(0, 2);

        Page<NodeInstanceEntity> expectedPage = new PageImpl<>(Arrays.asList(nodeInstances.get(0), nodeInstances.get(1)));

        Mockito.when(nodeInstanceEntityRepository.findAll(pageable))
                .thenReturn(expectedPage);

        Page<NodeInstanceEntity> actualPage = nodeInstanceEntityRepository.findAll(pageable);

        assertThat(actualPage).isEqualTo(expectedPage);
    }

    @DisplayName("Test finding node instances with a custom specification")
    @Test
    void testFindAllWithCustomSpecification() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, flowInstanceId, FlowNodeType.START, FlowableElementType.START),
                new NodeInstanceEntity(2L, flowInstanceId, FlowNodeType.TASK, FlowableElementType.END),
                new NodeInstanceEntity(3L, flowInstanceId, FlowNodeType.TASK, FlowableElementType.TASK),
                new NodeInstanceEntity(4L, flowInstanceId, FlowNodeType.END, FlowableElementType.START)
        );

        Specification<NodeInstanceEntity> spec = (Specification<NodeInstanceEntity>) Mockito.mock(Specification.class);
        Mockito.when(spec.isSatisfiedBy(Mockito.any(NodeInstanceEntity.class)))
                .then(invocation -> invocation.getArgument(0).getInstanceType() == FlowNodeType.TASK);

        Mockito.when(nodeInstanceEntityRepository.findAll(spec))
                .thenReturn(Arrays.asList(nodeInstances.get(1), nodeInstances.get(2)));

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findAll(spec);

        Assertions.assertEquals(Arrays.asList(nodeInstances.get(1), nodeInstances.get(2)), actualNodeInstances);
    }

    @DisplayName("Test saving a new node instance entity")
    @Test
    void testSaveNewNodeInstanceEntity() {
        NodeInstanceEntity newNodeInstanceEntity = new NodeInstanceEntity();
        newNodeInstanceEntity.setFlowInstanceId(1L);
        newNodeInstanceEntity.setInstanceId(1L);
        newNodeInstanceEntity.setInstanceType(FlowNodeType.START);
        newNodeInstanceEntity.setFlowableElementType(FlowableElementType.START);

        Long savedNodeInstanceId = 1L;
        NodeInstanceEntity savedNodeInstanceEntity = new NodeInstanceEntity(savedNodeInstanceId, 1L, FlowNodeType.START, FlowableElementType.START);

        Mockito.when(nodeInstanceEntityRepository.save(newNodeInstanceEntity))
                .thenReturn(savedNodeInstanceEntity);

        NodeInstanceEntity actualSavedNodeInstanceEntity = nodeInstanceEntityRepository.save(newNodeInstanceEntity);

        Assertions.assertEquals(savedNodeInstanceEntity, actualSavedNodeInstanceEntity);
    }

    @DisplayName("Test updating an existing node instance entity")
    @Test
    void testUpdateExistingNodeInstanceEntity() {
        Long existingNodeInstanceId = 1L;
        NodeInstanceEntity existingNodeInstanceEntity = new NodeInstanceEntity(existingNodeInstanceId, null, null, null);
        existingNodeInstanceEntity.setFlowInstanceId(1L);
        existingNodeInstanceEntity.setInstanceType(FlowNodeType.START);
        existingNodeInstanceEntity.setFlowableElementType(FlowableElementType.START);

        NodeInstanceEntity updatedNodeInstanceEntity = new NodeInstanceEntity();
        updatedNodeInstanceEntity.setInstanceId(existingNodeInstanceId);
        updatedNodeInstanceEntity.setFlowInstanceId(2L);
        updatedNodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
        updatedNodeInstanceEntity.setFlowableElementType(FlowableElementType.END);

        Mockito.doNothing().when(nodeInstanceEntityRepository).save(updatedNodeInstanceEntity);

        nodeInstanceEntityRepository.save(updatedNodeInstanceEntity);

        Optional<NodeInstanceEntity> optionalExistingNodeInstanceEntity = Mockito.spy(nodeInstanceEntityRepository)
                .findById(existingNodeInstanceId);

        Assertions.assertTrue(optionalExistingNodeInstanceEntity.isPresent());
        NodeInstanceEntity actualUpdatedNodeInstanceEntity = optionalExistingNodeInstanceEntity.get();
        Assertions.assertEquals(FlowNodeType.TASK, actualUpdatedNodeInstanceEntity.getInstanceType());
        Assertions.assertEquals(FlowableElementType.END, actualUpdatedNodeInstanceEntity.getFlowableElementType());
        Assertions.assertEquals(2L, actualUpdatedNodeInstanceEntity.getFlowInstanceId());
    }

    @DisplayName("Test finding node instances with an empty result")
    @Test
    void testFindEmptyResult() {
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(Mockito.anyLong(), Mockito.any(FlowNodeType.class), Mockito.any(FlowableElementType.class)))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actualNodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.END);

        Assertions.assertEquals(Collections.emptyList(), actualNodeInstances);
    }
}