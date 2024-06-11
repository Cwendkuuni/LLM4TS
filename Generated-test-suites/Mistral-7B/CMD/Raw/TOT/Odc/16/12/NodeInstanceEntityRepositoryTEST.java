package com.oceanbase.odc.service.flow.repository;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntityId;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@SpringJUnitConfig(classes = NodeInstanceEntityRepository.class)
class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;
    private NodeInstanceEntityId nodeInstanceEntityId;
    private NodeInstanceEntityId anotherNodeInstanceEntityId;

    @BeforeEach
    void setUp() {
        nodeInstanceEntity = new NodeInstanceEntity(1L, new NodeInstanceEntityId(1L, FlowNodeType.TASK, FlowableElementType.PROCESS), null, 1L);
        nodeInstanceEntityId = nodeInstanceEntity.getId();
        anotherNodeInstanceEntityId = new NodeInstanceEntityId(2L, FlowNodeType.GATEWAY, FlowableElementType.DECISION);
    }

    @Test
    void findByInstanceIdAndInstanceTypeAndFlowableElementTypeTest() {
        NodeInstanceEntity anotherNodeInstanceEntity = new NodeInstanceEntity(2L, anotherNodeInstanceEntityId, null, 1L);

        NodeInstanceEntity expectedNodeInstanceEntity = Mockito.spy(nodeInstanceEntity);
        doReturn(expectedNodeInstanceEntity).when(nodeInstanceEntityRepository).findOne(nodeInstanceEntityId);

        NodeInstanceEntity actualNodeInstanceEntity = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.PROCESS)
                .get(0);

        Assertions.assertSame(expectedNodeInstanceEntity, actualNodeInstanceEntity);

        Mockito.verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.PROCESS);

        Mockito.verifyZeroInteractions(nodeInstanceEntityRepository);
    }

    @Test
    void findByFlowInstanceIdTest() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(actualNodeInstanceEntities);
        assertEquals(1, actualNodeInstanceEntities.size());
        Assertions.assertSame(nodeInstanceEntity, actualNodeInstanceEntities.get(0));

        Mockito.verify(nodeInstanceEntityRepository).findByFlowInstanceId(1L);
    }

    @Test
    void findByIdsTest() {
        NodeInstanceEntity anotherNodeInstanceEntity = new NodeInstanceEntity(2L, anotherNodeInstanceEntityId, null, 1L);

        when(nodeInstanceEntityRepository.findById(nodeInstanceEntityId.getId())).thenReturn(java.util.Optional.of(nodeInstanceEntity));
        when(nodeInstanceEntityRepository.findById(anotherNodeInstanceEntityId.getId())).thenReturn(java.util.Optional.of(anotherNodeInstanceEntity));

        List<NodeInstanceEntity> expectedNodeInstanceEntities = Arrays.asList(nodeInstanceEntity, anotherNodeInstanceEntity);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findByIds(Arrays.asList(nodeInstanceEntityId.getId(), anotherNodeInstanceEntityId.getId()));

        Assertions.assertSame(expectedNodeInstanceEntities, actualNodeInstanceEntities);

        Mockito.verify(nodeInstanceEntityRepository).findById(nodeInstanceEntityId.getId());
        Mockito.verify(nodeInstanceEntityRepository).findById(anotherNodeInstanceEntityId.getId());
    }

    @Test
    void deleteByFlowInstanceIdTest() {
        when(nodeInstanceEntityRepository.countAllByFlowInstanceId(1L)).thenReturn(1);

        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        Mockito.verify(nodeInstanceEntityRepository).deleteAllByFlowInstanceId(1L);
        Mockito.verifyZeroInteractions(nodeInstanceEntityRepository);
    }

    @Test
    void deleteByInstanceIdAndInstanceTypeTest() {
        when(nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.TASK)).thenReturn(1);

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);

        Mockito.verify(nodeInstanceEntityRepository).deleteAllByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);
        Mockito.verifyZeroInteractions(nodeInstanceEntityRepository);
    }

    @Test
    void testFindAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(nodeInstanceEntity, new NodeInstanceEntity(2L, anotherNodeInstanceEntityId, null, 1L));

        when(nodeInstanceEntityRepository.findAll()).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findAll();

        Assertions.assertSame(nodeInstanceEntities, actualNodeInstanceEntities);

        Mockito.verify(nodeInstanceEntityRepository).findAll();
    }

    @Test
    void testFindAllWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(nodeInstanceEntity, new NodeInstanceEntity(2L, anotherNodeInstanceEntityId, null, 1L));
        Page<NodeInstanceEntity> page = Mockito.mock(Page.class);
        when(page.getContent()).thenReturn(nodeInstanceEntities);

        when(nodeInstanceEntityRepository.findAll(pageable)).thenReturn(page);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.findAll(pageable).getContent();

        Assertions.assertSame(nodeInstanceEntities, actualNodeInstanceEntities);

        Mockito.verify(nodeInstanceEntityRepository).findAll(pageable);
    }

    @Test
    void testCountAllByFlowInstanceId() {
        when(nodeInstanceEntityRepository.countAllByFlowInstanceId(1L)).thenReturn(1);

        int actualResult = nodeInstanceEntityRepository.countAllByFlowInstanceId(1L);

        assertEquals(1, actualResult);

        Mockito.verify(nodeInstanceEntityRepository).countAllByFlowInstanceId(1L);
    }

    @Test
    void testCountAllByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.TASK)).thenReturn(1);

        int actualResult = nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);

        assertEquals(1, actualResult);

        Mockito.verify(nodeInstanceEntityRepository).countAllByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);
    }

    @Test
    void testSave() {
        when(nodeInstanceEntityRepository.save(nodeInstanceEntity)).thenReturn(nodeInstanceEntity);

        NodeInstanceEntity actualNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        Assertions.assertSame(nodeInstanceEntity, actualNodeInstanceEntity);

        Mockito.verify(nodeInstanceEntityRepository).save(nodeInstanceEntity);
    }

    @Test
    void testSaveAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(nodeInstanceEntity, new NodeInstanceEntity(2L, anotherNodeInstanceEntityId, null, 1L));

        when(nodeInstanceEntityRepository.saveAll(nodeInstanceEntities)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> actualNodeInstanceEntities = nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        Assertions.assertSame(nodeInstanceEntities, actualNodeInstanceEntities);

        Mockito.verify(nodeInstanceEntityRepository).saveAll(nodeInstanceEntities);
    }

    @Test
    void testFindOne() {
        NodeInstanceEntity expectedNodeInstanceEntity = Mockito.spy(nodeInstanceEntity);
        doReturn(expectedNodeInstanceEntity).when(nodeInstanceEntityRepository).findOne(nodeInstanceEntityId);

        NodeInstanceEntity actualNodeInstanceEntity = nodeInstanceEntityRepository.findOne(nodeInstanceEntityId);

        Assertions.assertSame(expectedNodeInstanceEntity, actualNodeInstanceEntity);

        Mockito.verify(nodeInstanceEntityRepository).findOne(nodeInstanceEntityId);
    }

    @Test
    void testExistsById() {
        when(nodeInstanceEntityRepository.existsById(nodeInstanceEntityId.getId())).thenReturn(true);

        boolean actualResult = nodeInstanceEntityRepository.existsById(nodeInstanceEntityId.getId());

        assertTrue(actualResult);

        Mockito.verify(nodeInstanceEntityRepository).existsById(nodeInstanceEntityId.getId());
    }

    @Test
    void testExistsByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.existsByInstanceIdAndInstanceType(1L, FlowNodeType.TASK)).thenReturn(true);

        boolean actualResult = nodeInstanceEntityRepository.existsByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);

        assertTrue(actualResult);

        Mockito.verify(nodeInstanceEntityRepository).existsByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);
    }

    @Test
    void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.findById(nodeInstanceEntityId.getId())).thenReturn(java.util.Optional.of(nodeInstanceEntity));

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);

        Mockito.verify(nodeInstanceEntityRepository).delete(nodeInstanceEntity);
        Mockito.verifyZeroInteractions(nodeInstanceEntityRepository);
    }

    @Test
    void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findAllByFlowInstanceId(1L)).thenReturn(Arrays.asList(nodeInstanceEntity));

        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        Mockito.verify(nodeInstanceEntityRepository).deleteAllByFlowInstanceId(1L);
        Mockito.verifyZeroInteractions(nodeInstanceEntityRepository);
    }
}