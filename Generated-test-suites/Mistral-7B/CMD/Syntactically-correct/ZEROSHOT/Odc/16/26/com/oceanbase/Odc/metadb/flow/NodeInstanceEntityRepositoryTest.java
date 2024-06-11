package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setup() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.PROCESS);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.ACTIVITY);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.PROCESS,
                FlowableElementType.ACTIVITY)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.PROCESS, FlowableElementType.ACTIVITY);

        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);
        Mockito.when(nodeInstanceEntityRepository.findByIds(Collections.singletonList(nodeInstanceEntity.getInstanceId()))).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Collections.singletonList(nodeInstanceEntity.getInstanceId()));

        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = 1;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(1L);

        int actualResult = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(result, actualResult);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = 1;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.PROCESS);

        int actualResult = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.PROCESS);

        assertEquals(result, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeNullInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);
        Mockito.when(nodeInstanceEntityRepository.findAll()).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();

        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));
    }

    @Test
    public void testCount() {
        long result = 1;
        Mockito.when(nodeInstanceEntityRepository.count()).thenReturn(result);

        long actualResult = nodeInstanceEntityRepository.count();

        assertEquals(result, actualResult);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveNullNodeInstanceEntity() {
        nodeInstanceEntityRepository.save(null);
    }

    @Test
    public void testSave() {
        Mockito.when(nodeInstanceEntityRepository.save(nodeInstanceEntity)).thenReturn(nodeInstanceEntity);

        NodeInstanceEntity result = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        assertNotNull(result);
        assertEquals(nodeInstanceEntity, result);
    }

    @Test
    public void testFindByIdNullId() {
        assertNull(nodeInstanceEntityRepository.findById(null));
    }

    @Test
    public void testFindById() {
        Mockito.when(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getInstanceId())).thenReturn(java.util.Optional.of(nodeInstanceEntity));

        NodeInstanceEntity result = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getInstanceId()).get();

        assertNotNull(result);
        assertEquals(nodeInstanceEntity, result);
    }

    @Test
    public void testExistsById() {
        Mockito.when(nodeInstanceEntityRepository.existsById(nodeInstanceEntity.getInstanceId())).thenReturn(true);

        boolean result = nodeInstanceEntityRepository.existsById(nodeInstanceEntity.getInstanceId());

        assertTrue(result);
    }

    @Test
    public void testExistsByIdFalse() {
        Mockito.when(nodeInstanceEntityRepository.existsById(1L)).thenReturn(false);

        boolean result = nodeInstanceEntityRepository.existsById(1L);

        assertFalse(result);
    }

    @Test
    public void testFindAllByFlowInstanceIdOrderByInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);
        Mockito.when(nodeInstanceEntityRepository.findAll(Mockito.any(org.springframework.data.domain.Sort.class))).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(Mockito.argThat(sort -> sort.isSorted()));

        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));
    }

}