package com.oceanbase.odc.metadb.flow.test;

 import static org.junit.Assert.*;

 import java.util.Arrays;
 import java.util.Collections;
 import java.util.List;

 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.test.context.ContextConfiguration;
 import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

 import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
 import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
 import com.oceanbase.odc.metadb.flow.model.FlowableElementType;
 import com.oceanbase.odc.service.flow.model.FlowNode;
 import com.oceanbase.odc.service.flow.model.FlowInstance;

 @RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration(locations = { "classpath:application- context.xml" })
 public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 // Given
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.TASK;
 FlowableElementType flowableElementType = FlowableElementType.ACTIVITI_SEQUENCE_FLOW;
 List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, instanceId, instanceType, flowableElementType, null, null, null, null, null));
 // When
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 // Then
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithInvalidTypes() {
 // Given
 Long instanceId = 1L;
 FlowNodeType invalidInstanceType = null;
 FlowableElementType invalidFlowableElementType = null;
 // When & Then
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, invalidInstanceType, invalidFlowableElementType);
 }

 @Test
 public void testFindByFlowInstanceId() {
 // Given
 Long flowInstanceId = 1L;
 List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, null, null, null, new FlowInstance(1L, null, null, null, null), null, null, null, null),
 new NodeInstanceEntity(2L, null, null, null, new FlowInstance(1L, null, null, null, null), null, null, null, null));
 // When
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
 // Then
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByFlowInstanceIdWithNullParameter() {
 // When & Then
 nodeInstanceEntityRepository.findByFlowInstanceId(null);
 }

 @Test
 public void testFindByIds() {
 // Given
 Collection<Long> ids = Arrays.asList(1L, 2L);
 List<NodeInstanceEntity> expectedResult = Arrays.asList(new NodeInstanceEntity(1L, null, null, null, null, null, null, null, null),
 new NodeInstanceEntity(2L, null, null, null, null, null, null, null, null));
 // When
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
 // Then
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByIdsWithEmptyCollection() {
 // When & Then
 nodeInstanceEntityRepository.findByIds(Collections.emptyList());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;
 int expectedNumberOfDeletedRows = 2; // Assuming there are 2 nodes with given instanceId in the database
 // When
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 // Then
 assertEquals(expectedNumberOfDeletedRows, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByFlowInstanceIdWithNullParameter() {
 // When & Then
 nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 // Given
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.TASK;
 int expectedNumberOfDeletedRows = 2; // Assuming there are 2 nodes with given instanceId and instanceType in the database
 // When
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 // Then
 assertEquals(expectedNumberOfDeletedRows, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByInstanceIdAndInstanceTypeWithInvalidTypes() {
 // Given
 Long instanceId = 1L;
 FlowNodeType invalidInstanceType = null;
 // When & Then
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, invalidInstanceType);
 }

 }