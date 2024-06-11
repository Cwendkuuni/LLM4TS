package com.oceanbase.odc.metadb.flow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.SpecificationSupport;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntitySpecifications;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeImpl;
import com.oceanbase.odc.service.flow.model.FlowableElementTypeImpl;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;
    private NodeInstanceEntity nodeInstanceEntity3;
    private NodeInstanceEntity nodeInstanceEntity4;
    private FlowInstance flowInstance;

    @Before
    public void setup() {
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeTypeImpl.START_EVENT);
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementTypeImpl.TASK);
        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeTypeImpl.TASK);
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementTypeImpl.TASK);
        nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setId(3L);
        nodeInstanceEntity3.setInstanceId(3L);
        nodeInstanceEntity3.setInstanceType(FlowNodeTypeImpl.END_EVENT);
        nodeInstanceEntity3.setFlowInstanceId(2L);
        nodeInstanceEntity3.setFlowableElementType(FlowableElementTypeImpl.TASK);
        nodeInstanceEntity4 = new NodeInstanceEntity();
        nodeInstanceEntity4.setId(4L);
        nodeInstanceEntity4.setInstanceId(4L);
        nodeInstanceEntity4.setInstanceType(FlowNodeTypeImpl.TASK);
        nodeInstanceEntity4.setFlowInstanceId(null);
        flowInstance = new FlowInstance();
        flowInstance.setId(1L);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        expectedResult.add(nodeInstanceEntity1);

        given(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeTypeImpl.START_EVENT,
                FlowableElementTypeImpl.TASK))
                .willReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeTypeImpl.START_EVENT, FlowableElementTypeImpl.TASK);

        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity1, result.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        expectedResult.add(nodeInstanceEntity1);
        expectedResult.add(nodeInstanceEntity2);

        given(nodeInstanceEntityRepository.findByFlowInstanceId(1L))
                .willReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        expectedResult.add(nodeInstanceEntity2);
        expectedResult.add(nodeInstanceEntity3);

        given(nodeInstanceEntityRepository.findByIds(new ArrayList<Long>() {{ add(2L); add(3L); }}))
                .willReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(new ArrayList<Long>() {{ add(2L); add(3L); }});

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int expectedResult = 2;

        given(nodeInstanceEntityRepository.countByFlowInstanceId(1L))
                .willReturn(2);

        given(nodeInstanceEntityRepository.deleteByFlowInstanceId(1L));

        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        verify(nodeInstanceEntityRepository, times(1)).deleteAllInBatch();
        verify(nodeInstanceEntityRepository, times(1)).flush();

        assertEquals(expectedResult, nodeInstanceEntityRepository.count(new SpecificationSupport<NodeInstanceEntity>() {}));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int expectedResult = 1;

        given(nodeInstanceEntityRepository.countByInstanceIdAndInstanceType(1L, FlowNodeTypeImpl.START_EVENT))
                .willReturn(1);

        given(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeTypeImpl.START_EVENT));

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeTypeImpl.START_EVENT);

        verify(nodeInstanceEntityRepository, times(1)).deleteAllInBatch();
        verify(nodeInstanceEntityRepository, times(1)).flush();

        assertEquals(expectedResult, nodeInstanceEntityRepository.count(new SpecificationSupport<NodeInstanceEntity>() {}));
    }

    @Test
    public void testFindAll() {
        Pageable pageable = mock(Pageable.class);
        Page<NodeInstanceEntity> expectedResult = new PageImpl<NodeInstanceEntity>(List.of(nodeInstanceEntity1, nodeInstanceEntity2,
                nodeInstanceEntity3, nodeInstanceEntity4), new PageRequest(0, 10), 1L);

        given(nodeInstanceEntityRepository.findAll(pageable))
                .willReturn(expectedResult);

        Page<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(pageable);

        assertEquals(4, result.getTotalElements());
        assertEquals(List.of(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3, nodeInstanceEntity4), result.getContent());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntityToSave = new NodeInstanceEntity();
        nodeInstanceEntityToSave.setInstanceId(5L);
        nodeInstanceEntityToSave.setInstanceType(FlowNodeTypeImpl.START_EVENT);
        nodeInstanceEntityToSave.setFlowInstanceId(2L);
        nodeInstanceEntityToSave.setFlowableElementType(FlowableElementTypeImpl.TASK);

        given(nodeInstanceEntityRepository.saveAndFlush(nodeInstanceEntityToSave))
                .willReturn(nodeInstanceEntityToSave);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.saveAndFlush(nodeInstanceEntityToSave);

        assertEquals(nodeInstanceEntityToSave, savedNodeInstanceEntity);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeAndFlowInstanceIdIsNotNull() {
        given(nodeInstanceEntityRepository.countByInstanceIdAndInstanceTypeAndFlowInstanceIdIsNotNull(1L,
                FlowNodeTypeImpl.START_EVENT, 1L))
                .willReturn(1);

        given(nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceTypeAndFlowInstanceIdIsNotNull(1L,
                FlowNodeTypeImpl.START_EVENT, 1L));

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceTypeAndFlowInstanceIdIsNotNull(1L,
                FlowNodeTypeImpl.START_EVENT, 1L);

        verify(nodeInstanceEntityRepository, times(1)).deleteAllInBatch();
        verify(nodeInstanceEntityRepository, times(1)).flush();

        assertEquals(0, nodeInstanceEntityRepository.count(new SpecificationSupport<NodeInstanceEntity>() {}));
    }

    @Test
    public void testFindByInstanceTypeAndFlowInstanceId() {
        List<NodeInstanceEntity> expectedResult = new ArrayList<>();
        expectedResult.add(nodeInstanceEntity2);
        expectedResult.add(nodeInstanceEntity3);

        given(nodeInstanceEntityRepository.findByInstanceTypeAndFlowInstanceId(FlowNodeTypeImpl.TASK, 1L))
                .willReturn(expectedResult);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceTypeAndFlowInstanceId(FlowNodeTypeImpl.TASK,
                1L);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByInstanceId() {
        NodeInstanceEntity expectedResult = nodeInstanceEntity1;

        given(nodeInstanceEntityRepository.findById(1L))
                .willReturn(expectedResult);

        NodeInstanceEntity result = nodeInstanceEntityRepository.findById(1L).get();

        assertEquals(nodeInstanceEntity1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdNull() {
        nodeInstanceEntityRepository.findById(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceTypeNull() {
        nodeInstanceEntityRepository.findByInstanceType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowableElementTypeNull() {
        nodeInstanceEntityRepository.findByFlowableElementType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceIdNull() {
        nodeInstanceEntityRepository.findByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdNull() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeAndFlowInstanceIdIsNull() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceTypeAndFlowInstanceIdIsNull(1L,
                FlowNodeTypeImpl.START_EVENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeAndInstanceIdIsNull() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceTypeAndInstanceIdIsNull(1L,
                FlowNodeTypeImpl.START_EVENT);
    }
}