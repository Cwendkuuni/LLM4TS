package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);
        expectedList.add(nodeInstanceEntity1);
        Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        expectedList.add(nodeInstanceEntity1);
        Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId)).thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(id1);
        expectedList.add(nodeInstanceEntity1);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(id2);
        expectedList.add(nodeInstanceEntity2);
        Mockito.when(nodeInstanceEntityRepository.findByIds(ids)).thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int expectedCount = 2;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByFlowInstanceId(instanceId);
        int actualCount = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int expectedCount = 2;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        int actualCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_EmptyList() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int expectedCount = 0;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        int actualCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_NullInstanceType() {
        Long instanceId = 1L;
        int expectedCount = 0;
        Mockito.doNothing().when(nodeInstanceEntityRepository).deleteByInstanceIdAndInstanceType(instanceId, null);
        int actualCount = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, null);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> expectedList = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        expectedList.add(nodeInstanceEntity1);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        expectedList.add(nodeInstanceEntity2);
        Mockito.when(nodeInstanceEntityRepository.findAll()).thenReturn(expectedList);

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findAll();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testFindAllEmptyList() {
        Mockito.when(nodeInstanceEntityRepository.findAll()).thenReturn(Collections.emptyList());

        List<NodeInstanceEntity> actualList = nodeInstanceEntityRepository.findAll();
        assertTrue(actualList.isEmpty());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);
        Mockito.when(nodeInstanceEntityRepository.save(nodeInstanceEntity)).thenReturn(nodeInstanceEntity);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
        assertEquals(nodeInstanceEntity, savedNodeInstanceEntity);
    }

}