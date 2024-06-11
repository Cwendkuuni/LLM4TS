package com.oceanbase.odc.service.flow.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity nodeInstance1, nodeInstance2, nodeInstance3;

    @Before
    public void init() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        nodeInstance1 = new NodeInstanceEntity(1L, "instanceName1", FlowNodeType.START, FlowableElementType.TASK, 1L);
        nodeInstance2 = new NodeInstanceEntity(2L, "instanceName2", FlowNodeType.END, FlowableElementType.TASK, 2L);
        nodeInstance3 = new NodeInstanceEntity(3L, "instanceName3", FlowNodeType.PROCESS, FlowableElementType.TASK, 3L);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START,
                FlowableElementType.TASK))
                .thenReturn(Arrays.asList(nodeInstance1));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START, FlowableElementType.TASK);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(nodeInstance1.getInstanceId(), result.get(0).getInstanceId());
        assertEquals(nodeInstance1.getInstanceName(), result.get(0).getInstanceName());
        assertEquals(nodeInstance1.getInstanceType(), result.get(0).getInstanceType());
        assertEquals(nodeInstance1.getFlowableElementType(), result.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
                .thenReturn(Arrays.asList(nodeInstance1, nodeInstance2, nodeInstance3));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(result);
        assertEquals(3, result.size());
        for (NodeInstanceEntity nodeInstance : result) {
            assertNotNull(nodeInstance);
            assertNotNull(nodeInstance.getInstanceId());
            assertNotNull(nodeInstance.getInstanceName());
            assertNotNull(nodeInstance.getInstanceType());
            assertNotNull(nodeInstance.getFlowableElementType());
        }
    }

    @Test
    public void testFindByIds() {
        when(nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(nodeInstance1, nodeInstance2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
        assertNotNull(result);
        assertEquals(2, result.size());
        for (NodeInstanceEntity nodeInstance : result) {
            assertNotNull(nodeInstance);
            assertNotNull(nodeInstance.getInstanceId());
            assertNotNull(nodeInstance.getInstanceName());
            assertNotNull(nodeInstance.getInstanceType());
            assertNotNull(nodeInstance.getFlowableElementType());
        }
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(nodeInstanceEntityRepository.countAllByFlowInstanceId(1L)).thenReturn((long) 3);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(3, result);
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1))
                .deleteInBatch(Mockito.anyCollection());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.START)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1))
                .delete(Mockito.any(NodeInstanceEntity.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindAll() {
        when(nodeInstanceEntityRepository.findAll(Mockito.any(Pageable.class)))
                .thenThrow(new UnsupportedOperationException());

        Page<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(Mockito.mock(Pageable.class));
    }

    @Test
    public void testFindAllPaged() {
        List<NodeInstanceEntity> nodeInstances = Arrays.asList(nodeInstance1, nodeInstance2, nodeInstance3);
        Pageable pageable = Mockito.mock(Pageable.class);
        when(nodeInstanceEntityRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(nodeInstances, pageable, nodeInstances.size()));

        Page<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(pageable);
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertNotNull(result.getContent());
        assertEquals(nodeInstances.size(), result.getSize());
        assertEquals(1, result.getNumber());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity savedNodeInstance = new NodeInstanceEntity();
        when(nodeInstanceEntityRepository.save(Mockito.any(NodeInstanceEntity.class)))
                .thenReturn(nodeInstance1);

        NodeInstanceEntity result = nodeInstanceEntityRepository.save(savedNodeInstance);
        assertNotNull(result);
        assertEquals(nodeInstance1.getInstanceId(), result.getInstanceId());
        assertEquals(nodeInstance1.getInstanceName(), result.getInstanceName());
        assertEquals(nodeInstance1.getInstanceType(), result.getInstanceType());
        assertEquals(nodeInstance1.getFlowableElementType(), result.getFlowableElementType());
    }

    @Test
    public void testFindById() {
        when(nodeInstanceEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(nodeInstance1));

        NodeInstanceEntity result = nodeInstanceEntityRepository.findById(1L).get();
        assertNotNull(result);
        assertEquals(nodeInstance1.getInstanceId(), result.getInstanceId());
        assertEquals(nodeInstance1.getInstanceName(), result.getInstanceName());
        assertEquals(nodeInstance1.getInstanceType(), result.getInstanceType());
        assertEquals(nodeInstance1.getFlowableElementType(), result.getFlowableElementType());
    }

    @Test
    public void testFindByInstanceNameContaining() {
        when(nodeInstanceEntityRepository.findByInstanceNameContaining("instanceName"))
                .thenReturn(Arrays.asList(nodeInstance1, nodeInstance2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceNameContaining("instanceName");
        assertNotNull(result);
        assertEquals(2, result.size());
        for (NodeInstanceEntity nodeInstance : result) {
            assertNotNull(nodeInstance);
            assertNotNull(nodeInstance.getInstanceId());
            assertNotNull(nodeInstance.getInstanceName());
            assertNotNull(nodeInstance.getInstanceType());
            assertNotNull(nodeInstance.getFlowableElementType());
            assertEquals(nodeInstance1.getInstanceName().contains("instanceName"), true);
            assertEquals(nodeInstance2.getInstanceName().contains("instanceName"), true);
        }
    }

    @Test
    public void testFindByInstanceType() {
        when(nodeInstanceEntityRepository.findByInstanceType(FlowNodeType.PROCESS)).thenReturn(Arrays.asList(nodeInstance3));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceType(FlowNodeType.PROCESS);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertEquals(nodeInstance3.getInstanceId(), result.get(0).getInstanceId());
        assertEquals(nodeInstance3.getInstanceName(), result.get(0).getInstanceName());
        assertEquals(nodeInstance3.getInstanceType(), result.get(0).getInstanceType());
        assertEquals(nodeInstance3.getFlowableElementType(), result.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowableElementType() {
        when(nodeInstanceEntityRepository.findByFlowableElementType(FlowableElementType.TASK)).thenReturn(Arrays.asList(nodeInstance1,
                nodeInstance2, nodeInstance3));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowableElementType(FlowableElementType.TASK);
        assertNotNull(result);
        assertEquals(3, result.size());
        for (NodeInstanceEntity nodeInstance : result) {
            assertNotNull(nodeInstance);
            assertEquals(nodeInstance1.getFlowableElementType(), nodeInstance.getFlowableElementType());
            assertEquals(nodeInstance2.getFlowableElementType(), nodeInstance.getFlowableElementType());
            assertEquals(nodeInstance3.getFlowableElementType(), nodeInstance.getFlowableElementType());
        }
    }

    @Test
    public void testFindByFlowInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.findByFlowInstanceIdAndInstanceType(1L, FlowNodeType.PROCESS))
                .thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceIdAndInstanceType(1L,
                FlowNodeType.PROCESS);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCountAll() {
        when(nodeInstanceEntityRepository.countAll()).thenReturn(3L);

        long result = nodeInstanceEntityRepository.countAll();
        assertEquals(3, result);
    }

    @Test
    public void testCountAllByFlowInstanceId() {
        when(nodeInstanceEntityRepository.countAllByFlowInstanceId(1L)).thenReturn(2L);

        long result = nodeInstanceEntityRepository.countAllByFlowInstanceId(1L);
        assertEquals(2, result);
    }

    @Test
    public void testCountAllByInstanceIdAndInstanceType() {
        when(nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.PROCESS))
                .thenReturn(1L);

        long result = nodeInstanceEntityRepository.countAllByInstanceIdAndInstanceType(1L, FlowNodeType.PROCESS);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByInstanceId() {
        when(nodeInstanceEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(nodeInstance1));

        int result = nodeInstanceEntityRepository.deleteByInstanceId(1L);
        assertEquals(1, result);
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1))
                .delete(Mockito.any(NodeInstanceEntity.class));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(nodeInstanceEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(nodeInstance1));

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.PROCESS, FlowableElementType.TASK);
        assertEquals(1, result);
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1))
                .delete(Mockito.any(NodeInstanceEntity.class));
    }

    @Test
    public void testFindByInstanceIdGreaterThan() {
        when(nodeInstanceEntityRepository.findByInstanceIdGreaterThan(1L)).thenReturn(Arrays.asList(nodeInstance2,
                nodeInstance3));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdGreaterThan(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (NodeInstanceEntity nodeInstance : result) {
            assertNotNull(nodeInstance);
            assertNotNull(nodeInstance.getInstanceId());
            assertNotNull(nodeInstance.getInstanceName());
            assertNotNull(nodeInstance.getInstanceType());
            assertNotNull(nodeInstance.getFlowableElementType());
            assertFalse(nodeInstance.getInstanceId() == 1L);
        }
    }

    @Test
    public void testDeleteAll() {
        when(nodeInstanceEntityRepository.countAll()).thenReturn(3L);

        int result = nodeInstanceEntityRepository.deleteAll();
        assertEquals(3, result);
        Mockito.verify(nodeInstanceEntityRepository, Mockito.times(1))
                .deleteAllInBatch(Mockito.anyCollection());
    }
}