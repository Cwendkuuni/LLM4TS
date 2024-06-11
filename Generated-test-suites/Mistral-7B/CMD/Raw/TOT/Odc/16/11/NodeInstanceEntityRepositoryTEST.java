package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.model.dto.NodeInstanceDTO;
import com.oceanbase.odc.model.entity.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository repository;

    @InjectMocks
    private NodeInstanceEntityRepository testSubject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // given
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(1L);
        entity1.setInstanceType(FlowNodeType.START_EVENT);
        entity1.setFlowableElementType(FlowableElementType.PROCESS);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(2L);
        entity2.setInstanceType(FlowNodeType.END_EVENT);
        entity2.setFlowableElementType(FlowableElementType.PROCESS);

        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT,
                FlowableElementType.PROCESS)).thenReturn(Arrays.asList(entity1));
        when(repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(2L, FlowNodeType.END_EVENT,
                FlowableElementType.PROCESS)).thenReturn(Collections.emptyList());

        // when
        List<NodeInstanceEntity> result1 = testSubject.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.PROCESS);
        List<NodeInstanceEntity> result2 = testSubject.findByInstanceIdAndInstanceTypeAndFlowableElementType(2L,
                FlowNodeType.END_EVENT, FlowableElementType.PROCESS);

        // then
        assertEquals(1, result1.size());
        assertFalse(result2.isEmpty());
        assertNull(result2.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        // given
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(1L);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(1L);

        when(repository.findByFlowInstanceId(1L)).thenReturn(Arrays.asList(entity1, entity2));

        // when
        List<NodeInstanceEntity> result = testSubject.findByFlowInstanceId(1L);

        // then
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        // given
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setId(1L);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setId(2L);

        List<Long> ids = Arrays.asList(1L, 2L);

        when(repository.findByIds(ids)).thenReturn(Arrays.asList(entity1, entity2));

        // when
        List<NodeInstanceEntity> result = testSubject.findByIds(ids);

        // then
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // given
        Long instanceId = 1L;
        int expectedCountBefore = 3;
        int expectedCountAfter = 1;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(instanceId);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(instanceId);

        NodeInstanceEntity entity3 = new NodeInstanceEntity();
        entity3.setFlowInstanceId(2L);

        when(repository.countAllByFlowInstanceId(instanceId)).thenReturn(expectedCountBefore);

        // when
        int result = testSubject.deleteByFlowInstanceId(instanceId);

        // then
        assertEquals(expectedCountAfter, testSubject.countAllByFlowInstanceId(instanceId));
        assertEquals(expectedCountAfter, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int expectedCountBefore = 3;
        int expectedCountAfter = 1;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        entity2.setInstanceType(instanceType);

        NodeInstanceEntity entity3 = new NodeInstanceEntity();
        entity3.setInstanceId(2L);
        entity3.setInstanceType(FlowNodeType.END_EVENT);

        NodeInstanceEntity entity4 = new NodeInstanceEntity();
        entity4.setInstanceId(2L);
        entity4.setInstanceType(instanceType);

        when(repository.countAllByInstanceIdAndInstanceType(instanceId, instanceType)).thenReturn(expectedCountBefore);

        // when
        int result = testSubject.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // then
        assertEquals(expectedCountAfter, testSubject.countAllByInstanceIdAndInstanceType(instanceId, instanceType));
        assertEquals(expectedCountAfter, result);
    }

    @Test
    public void testSaveNewNodeInstance() {
        // given
        Long flowInstanceId = RandomStringUtils.randomNumeric(10).toLong();
        Long instanceId = RandomStringUtils.randomNumeric(10).toLong();
        FlowNodeType instanceType = FlowNodeType.PROCESS;
        FlowableElementType flowableElementType = FlowableElementType.PROCESS;

        // when
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);

        NodeInstanceEntity savedNodeInstanceEntity = testSubject.saveAndFlush(nodeInstanceEntity);

        // then
        assertNotNull(savedNodeInstanceEntity.getId());
        assertEquals(flowInstanceId, savedNodeInstanceEntity.getFlowInstanceId());
        assertEquals(instanceId, savedNodeInstanceEntity.getInstanceId());
        assertEquals(instanceType, savedNodeInstanceEntity.getInstanceType());
        assertEquals(flowableElementType, savedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testUpdateNodeInstance() {
        // given
        Long flowInstanceId = RandomStringUtils.randomNumeric(10).toLong();
        Long instanceId = RandomStringUtils.randomNumeric(10).toLong();
        Long newInstanceId = RandomStringUtils.randomNumeric(10).toLong();
        FlowNodeType newInstanceType = FlowNodeType.PROCESS;
        FlowableElementType newFlowableElementType = FlowableElementType.PROCESS;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.PROCESS);

        NodeInstanceEntity updatedNodeInstanceEntity = new NodeInstanceEntity();
        updatedNodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        updatedNodeInstanceEntity.setInstanceId(newInstanceId);
        updatedNodeInstanceEntity.setInstanceType(newInstanceType);
        updatedNodeInstanceEntity.setFlowableElementType(newFlowableElementType);

        NodeInstanceEntity savedNodeInstanceEntity = testSubject.saveAndFlush(nodeInstanceEntity);

        when(repository.findById(savedNodeInstanceEntity.getId())).thenReturn(savedNodeInstanceEntity);

        // when
        updatedNodeInstanceEntity = testSubject.saveAndFlush(updatedNodeInstanceEntity);

        // then
        assertEquals(newInstanceId, updatedNodeInstanceEntity.getInstanceId());
        assertEquals(newInstanceType, updatedNodeInstanceEntity.getInstanceType());
        assertEquals(newFlowableElementType, updatedNodeInstanceEntity.getFlowableElementType());
    }
}