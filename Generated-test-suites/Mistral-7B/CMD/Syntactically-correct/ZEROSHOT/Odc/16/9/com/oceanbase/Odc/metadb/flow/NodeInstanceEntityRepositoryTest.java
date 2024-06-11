package com.oceanbase.odc.metadb.flow.repository;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepository testInstance;

    @Before
    public void setUp() {
        // Set up mock repository
        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
        nodeInstance1.setId(1L);
        nodeInstance1.setFlowInstanceId(1L);
        nodeInstance1.setInstanceId(1L);
        nodeInstance1.setInstanceType(FlowNodeType.TASK);

        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
        nodeInstance2.setId(2L);
        nodeInstance2.setFlowInstanceId(1L);
        nodeInstance2.setInstanceId(2L);
        nodeInstance2.setInstanceType(FlowNodeType.SEQUENCE);

        Mockito.when(nodeInstanceEntityRepository.findAll()).thenReturn(Arrays.asList(nodeInstance1, nodeInstance2));
        Mockito.when(nodeInstanceEntityRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(nodeInstance1));
        Mockito.when(nodeInstanceEntityRepository.save(Mockito.any(NodeInstanceEntity.class))).then(invocation -> invocation.getArgument(0));
        Mockito.when(nodeInstanceEntityRepository.deleteById(Mockito.anyLong())).thenReturn(1);

        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(Mockito.anyLong(),
                Mockito.eq(FlowNodeType.TASK), Mockito.eq(FlowableElementType.START))).thenReturn(Collections.singletonList(nodeInstance1));
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(Mockito.anyLong(),
                Mockito.eq(FlowNodeType.TASK), Mockito.eq(FlowableElementType.END))).thenReturn(Collections.emptyList());
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(Mockito.anyLong())).thenReturn(Arrays.asList(nodeInstance1, nodeInstance2));
        Mockito.when(nodeInstanceEntityRepository.findByIds(Mockito.any(Collection.class))).thenReturn(Arrays.asList(nodeInstance1, nodeInstance2));
        Mockito.when(nodeInstanceEntityRepository.deleteByFlowInstanceId(Mockito.anyLong())).thenReturn(2);
        Mockito.when(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(Mockito.anyLong(), Mockito.eq(FlowNodeType.TASK))).thenReturn(1);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> result = testInstance.findAll();
        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity result = testInstance.findById(1L).get();
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(FlowNodeType.TASK, result.getInstanceType());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        NodeInstanceEntity savedEntity = testInstance.save(entity);
        assertNotNull(savedEntity);
        assertEquals(savedEntity, testInstance.findById(savedEntity.getId()).get());
    }

    @Test
    public void testDeleteById() {
        int result = testInstance.deleteByFlowInstanceId(1L);
        assertEquals(2, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = testInstance.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> result = testInstance.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.START);
        assertEquals(1, result.size());
        NodeInstanceEntity nodeInstance = result.get(0);
        assertEquals(1L, nodeInstance.getId());
        assertEquals(FlowNodeType.TASK, nodeInstance.getInstanceType());
        assertEquals(FlowableElementType.START, nodeInstance.getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> result = testInstance.findByFlowInstanceId(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> result = testInstance.findByIds(Arrays.asList(1L, 2L));
        assertEquals(2, result.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCount() {
        testInstance.count();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExistsById() {
        testInstance.existsById(1L);
    }

    @Test
    public void testFindAllByFlowInstanceIdOrderByIdDesc() {
        Page<NodeInstanceEntity> result = testInstance.findAll(Pageable.unpaged());
        assertEquals(2, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(1L, result.getContent().get(1).getId());
    }

    @Test
    public void testFindAllByInstanceType() {
        List<NodeInstanceEntity> result = testInstance.findAll(Example.of(new NodeInstanceEntity()));
        assertEquals(2, result.size());
    }

    @Test
    public void testFindAllByInstanceTypeOrderByIdDesc() {
        List<NodeInstanceEntity> result = testInstance.findAll(Example.of(new NodeInstanceEntity()), Pageable.ofSize(10, Direction.DESC, "id"));
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
    }
}