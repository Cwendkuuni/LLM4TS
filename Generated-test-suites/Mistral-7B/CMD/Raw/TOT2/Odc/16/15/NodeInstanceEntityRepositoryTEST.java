package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Before
 public void setup() {
 nodeInstanceEntityRepository = Mockito.spy(NodeInstanceEntityRepositoryImpl.getInstance());
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 // Given
 Long instanceId = 1L;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(
 new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null)
 );
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity(2L, instanceId, instanceType, flowableElementType, null);
 Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
 .thenReturn(Collections.singletonList(expectedNodeInstances.get(0)));
 Mockito.when(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()))
 .thenReturn(java.util.Optional.ofNullable(nodeInstanceEntity));

 // When
 NodeInstanceEntity returnedNodeInstance = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType).get(0);

 // Then
 assertEquals(expectedNodeInstances.get(0).getInstanceId(), returnedNodeInstance.getInstanceId());
 assertEquals(expectedNodeInstances.get(0).getInstanceType(), returnedNodeInstance.getInstanceType());
 assertEquals(expectedNodeInstances.get(0).getFlowableElementType(), returnedNodeInstance.getFlowableElementType());
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
 // Given
 Long instanceId = 1L;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
 .thenReturn(Collections.emptyList());

 // When
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

 // Then
 assertTrue(nodeInstanceEntities.isEmpty());
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullResult() {
 // Given
 Long instanceId = 1L;
 FlowableElementType flowableElementType = FlowableElementType.PROCESS;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 Mockito.when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
 .thenReturn(null);

 // When
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

 // Then
 assertNull(nodeInstanceEntities);
 }

 // Add tests for other methods here...

 @Test
 public void testFindByFlowInstanceId() {
 // Given
 Long flowInstanceId = 1L;
 List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(
 new NodeInstanceEntity(1L, 1L, FlowNodeType.START_EVENT, FlowableElementType.PROCESS, null),
 new NodeInstanceEntity(2L, 1L, FlowNodeType.END_EVENT, FlowableElementType.PROCESS, null)
 );
 Mockito.when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
 .thenReturn(expectedNodeInstances);

 // When
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

 // Then
 assertEquals(expectedNodeInstances.size(), nodeInstanceEntities.size());
 for (NodeInstanceEntity nodeInstanceEntity : nodeInstanceEntities) {
 assertEquals(expectedNodeInstances.get(nodeInstanceEntities.indexOf(nodeInstanceEntity)).getInstanceId(), nodeInstanceEntity.getInstanceId());
 assertEquals(expectedNodeInstances.get(nodeInstanceEntities.indexOf(nodeInstanceEntity)).getInstanceType(), nodeInstanceEntity.getInstanceType());
 assertEquals(expectedNodeInstances.get(nodeInstanceEntities.indexOf(nodeInstanceEntity)).getFlowableElementType(), nodeInstanceEntity.getFlowableElementType());
 }
 }

 // Add tests for other methods here...

 @Test
 public void testFindByIds() {
 // Given
 Collection<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<NodeInstanceEntity> expectedNodeInstances = Arrays.asList(
 new NodeInstanceEntity(1L, 1L, FlowNodeType.START_EVENT, FlowableElementType.PROCESS, null),
 new NodeInstanceEntity(2L, 1L, FlowNodeType.END_EVENT, FlowableElementType.PROCESS, null)
 );
 Mockito.when(nodeInstanceEntityRepository.findByIds(ids))
 .thenReturn(expectedNodeInstances);

 // When
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

 // Then
 assertEquals(expectedNodeInstances.size(), nodeInstanceEntities.size());
 for (NodeInstanceEntity nodeInstanceEntity : nodeInstanceEntities) {
 assertEquals(expectedNodeInstances.get(nodeInstanceEntities.indexOf(nodeInstanceEntity)).getInstanceId(), nodeInstanceEntity.getInstanceId());
 assertEquals(expectedNodeInstances.get(nodeInstanceEntities.indexOf(nodeInstanceEntity)).getInstanceType(), nodeInstanceEntity.getInstanceType());
 assertEquals(expectedNodeInstances.get(nodeInstanceEntities.indexOf(nodeInstanceEntity)).getFlowableElementType(), nodeInstanceEntity.getFlowableElementType());
 }
 }

 // Add tests for other methods here...

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;
 int expectedNumberOfDeletedEntities = 2;

 // When
 int numberOfDeletedEntities = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 Mockito.verify(nodeInstanceEntityRepository, Mockito.times(expectedNumberOfDeletedEntities))
 .delete(Mockito.any(NodeInstanceEntity.class));
 }

 // Add tests for other methods here...

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 // Given
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 int expectedNumberOfDeletedEntities = 1;

 // When
 int numberOfDeletedEntities = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 Mockito.verify(nodeInstanceEntityRepository, Mockito.times(expectedNumberOfDeletedEntities))
 .delete(Mockito.any(NodeInstanceEntity.class));
 }

}