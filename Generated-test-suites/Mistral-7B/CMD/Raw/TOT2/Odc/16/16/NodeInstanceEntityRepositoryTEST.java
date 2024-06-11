package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.NodeInstanceEntity;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepository;

@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 // Test case 1
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.START_EVENT);
 entity.setFlowableElementType(FlowableElementType.USER_TASK);

 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(entity.getInstanceId(), entity.getInstanceType(), entity.getFlowableElementType());

 Assert.assertNotNull(result);
 Assert.assertEquals(1, result.size());
 Assert.assertEquals(entity, result.get(0));

 // Test case 2 - edge case: empty parameters
 List<NodeInstanceEntity> resultEmptyParams = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
 Assert.assertNull(resultEmptyParams);

 // Test case 3 - edge case: invalid parameter types
 List<Object> resultInvalidParamTypes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType("", null, new Object[]{});
 Assert.assertNull(resultInvalidParamTypes);
 }

 @Test
 public void testFindByFlowInstanceId() {
 Long flowInstanceId = 1L;
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

 Assert.assertNotNull(result);
 Assert.assertTrue(result.size() > 0);
 }

 @Test
 public void testFindByIds() {
 Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

 Assert.assertNotNull(result);
 Assert.assertTrue(result.size() == ids.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

 Assert.assertEquals(1, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.END_EVENT;
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

 Assert.assertEquals(1, result);
 }

 @Test
 public void testGenericFindAll() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();

 Assert.assertNotNull(result);
 Assert.assertTrue(result.size() > 0);
 }

 @Test(expected = EmptyResultDataAccessException.class)
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NonExistentID() {
 NodeInstanceEntityRepository repository = new NodeInstanceEntityRepositoryImpl();
 List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(10L, FlowNodeType.START_EVENT, FlowableElementType.USER_TASK);

 Assert.assertEmpty(result);
 }

 @Test(expected = EmptyResultDataAccessException.class)
 public void testFindByFlowInstanceId_NonExistentID() {
 NodeInstanceEntityRepository repository = new NodeInstanceEntityRepositoryImpl();
 List<NodeInstanceEntity> result = repository.findByFlowInstanceId(10L);

 Assert.assertEmpty(result);
 }

 @Test(expected = EmptyResultDataAccessException.class)
 public void testFindByIds_NonExistentIDs() {
 NodeInstanceEntityRepository repository = new NodeInstanceEntityRepositoryImpl();
 Collection<Long> ids = Arrays.asList(10L, 11L, 12L);
 List<NodeInstanceEntity> result = repository.findByIds(ids);

 Assert.assertEmpty(result);
 }

 @Test
 public void testDeleteByFlowInstanceId_NonExistentID() {
 NodeInstanceEntityRepository repository = new NodeInstanceEntityRepositoryImpl();
 int result = repository.deleteByFlowInstanceId(10L);

 Assert.assertEquals(0, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType_NonExistentID() {
 NodeInstanceEntityRepository repository = new NodeInstanceEntityRepositoryImpl();
 int result = repository.deleteByInstanceIdAndInstanceType(10L, FlowNodeType.START_EVENT);

 Assert.assertEquals(0, result);
 }
}