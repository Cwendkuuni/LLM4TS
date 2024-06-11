package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowNodeInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeInstanceId;
import com.oceanbase.odc.service.flow.model.FlowNodeInstanceStatus;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepositoryImpl;

/**
 * Test class for NodeInstanceEntityRepository
 *
 * @author yh263208
 * @date 2022-02-09 14:18
 * @since ODC_release_3.3.0
 */
public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = new NodeInstanceEntityRepositoryImpl();
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.PROCESS);
        nodeInstanceEntity.setStatus(FlowNodeInstanceStatus.STARTED);
        nodeInstanceEntity.setName("Test Start Event");
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.PROCESS))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.PROCESS);

        assertThat(result).isNotNull();
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));

        verify(nodeInstanceEntityRepository).findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.PROCESS);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertThat(result).isNotNull();
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));

        verify(nodeInstanceEntityRepository).findByFlowInstanceId(1L);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findByIds(new ArrayList<Long>() {{ add(1L); }})).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(new ArrayList<Long>() {{ add(1L); }});

        assertThat(result).isNotNull();
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity, result.get(0));

        verify(nodeInstanceEntityRepository).findByIds(new ArrayList<Long>() {{ add(1L); }});
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int countBeforeDelete = 2;
        when(nodeInstanceEntityRepository.countAllByFlowInstanceId(1L)).thenReturn(countBeforeDelete);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        verify(nodeInstanceEntityRepository).deleteAll(argThat(entity -> entity.getFlowInstanceId().equals(1L)));
        assertEquals(countBeforeDelete, nodeInstanceEntityRepository.countAllByFlowInstanceId(1L) + result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int countBeforeDelete = 3;
        when(nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT)).thenReturn(countBeforeDelete);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

        verify(nodeInstanceEntityRepository).deleteAll(argThat(entity -> entity.getInstanceId().equals(1L)
                && entity.getInstanceType().equals(FlowNodeType.START_EVENT)));
        assertEquals(countBeforeDelete, nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT) + result);
    }

    @Test
    public void testSave() {
        when(nodeInstanceEntityRepository.save(nodeInstanceEntity)).thenReturn(nodeInstanceEntity);

        NodeInstanceEntity result = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        assertEquals(nodeInstanceEntity, result);
        verify(nodeInstanceEntityRepository).save(nodeInstanceEntity);
    }

    @Test
    public void testSaveWithNullEntity() {
        assertNull(nodeInstanceEntityRepository.save(null));
    }

    @Test
    public void testFindById() {
        when(nodeInstanceEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(nodeInstanceEntity));

        NodeInstanceEntity result = nodeInstanceEntityRepository.findById(1L).orElse(null);

        assertEquals(nodeInstanceEntity, result);
        verify(nodeInstanceEntityRepository).findById(1L);
    }

    @Test
    public void testFindByIdWithNullEntity() {
        assertNull(nodeInstanceEntityRepository.findById(1L).orElse(null));
        verify(nodeInstanceEntityRepository).findById(1L);
    }

    @Test
    public void testExistsById() {
        when(nodeInstanceEntityRepository.existsById(1L)).thenReturn(true);

        boolean result = nodeInstanceEntityRepository.existsById(1L);

        assertTrue(result);
        verify(nodeInstanceEntityRepository).existsById(1L);
    }

    @Test
    public void testExistsByIdWithFalse() {
        when(nodeInstanceEntityRepository.existsById(1L)).thenReturn(false);

        boolean result = nodeInstanceEntityRepository.existsById(1L);

        assertFalse(result);
        verify(nodeInstanceEntityRepository).existsById(1L);
    }

    @Test
    public void testCountAll() {
        when(nodeInstanceEntityRepository.countAll()).thenReturn(10L);

        long result = nodeInstanceEntityRepository.countAll();

        assertEquals(10, (int) result);
        verify(nodeInstanceEntityRepository).countAll();
    }

    @Test
    public void testCountAllByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT)).thenReturn(3L);

        long result = nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

        assertEquals(3, (int) result);
        verify(nodeInstanceEntityRepository).countAllByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findAll()).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();

        assertEquals(nodeInstanceEntities, result);
        verify(nodeInstanceEntityRepository).findAll();
    }

    @Test
    public void testFindAllByInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findAllByInstanceId(1L)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAllByInstanceId(1L);

        assertEquals(nodeInstanceEntities, result);
        verify(nodeInstanceEntityRepository).findAllByInstanceId(1L);
    }

    @Test
    public void testFindAllByInstanceIdAndInstanceType() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findAllByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT))
                .thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAllByInstanceIdAndInstanceType(1L,
                FlowNodeType.START_EVENT);

        assertEquals(nodeInstanceEntities, result);
        verify(nodeInstanceEntityRepository).findAllByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
    }

    @Test
    public void testFindAllByInstanceType() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findAllByInstanceType(FlowNodeType.START_EVENT)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAllByInstanceType(FlowNodeType.START_EVENT);

        assertEquals(nodeInstanceEntities, result);
        verify(nodeInstanceEntityRepository).findAllByInstanceType(FlowNodeType.START_EVENT);
    }

    @Test
    public void testFindAllByFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findAllByFlowableElementType(FlowableElementType.PROCESS)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAllByFlowableElementType(FlowableElementType.PROCESS);

        assertEquals(nodeInstanceEntities, result);
        verify(nodeInstanceEntityRepository).findAllByFlowableElementType(FlowableElementType.PROCESS);
    }

    @Test
    public void testFindAllByStatus() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(nodeInstanceEntity);

        when(nodeInstanceEntityRepository.findAllByStatus(FlowNodeInstanceStatus.STARTED)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAllByStatus(FlowNodeInstanceStatus.STARTED);

        assertEquals(nodeInstanceEntities, result);
        verify(nodeInstanceEntityRepository).findAllByStatus(FlowNodeInstanceStatus.STARTED);
    }
}