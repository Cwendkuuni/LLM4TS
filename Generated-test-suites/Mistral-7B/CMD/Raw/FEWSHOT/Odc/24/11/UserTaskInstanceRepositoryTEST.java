package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.data.domain.ExampleMatchResult;
import org.springframework.data.domain.ExampleMatcher;

public class UserTaskInstanceRepositoryTest {

 @Test
 public void testFindByStatus() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation of UserTaskInstanceRepository

 List<UserTaskInstanceEntity> userTaskInstances = repository.findByStatus(FlowNodeStatus.APPROVED);
 assertEquals(0, userTaskInstances.size());

 userTaskInstances = repository.findByStatus(FlowNodeStatus.REJECTED);
 assertEquals(0, userTaskInstances.size());

 userTaskInstances = repository.findByStatus(FlowNodeStatus.PENDING);
 assertEquals(1, userTaskInstances.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();

 List<UserTaskInstanceEntity> userTaskInstances = repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
 assertEquals(0, userTaskInstances.size());

 userTaskInstances = repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));
 assertEquals(1, userTaskInstances.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();

 List<UserTaskInstanceEntity> userTaskInstances = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
 assertEquals(0, userTaskInstances.size());

 userTaskInstances = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1"));
 assertEquals(1, userTaskInstances.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();

 List<UserTaskInstanceEntity> userTaskInstances = repository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
 assertEquals(0, userTaskInstances.size());

 userTaskInstances = repository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));
 assertEquals(1, userTaskInstances.size());
 }

 @Test
 public void testFindByRoleId() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();

 List<UserTaskInstanceEntity> userTaskInstances = repository.findByRoleId(1L);
 assertEquals(0, userTaskInstances.size());

 userTaskInstances = repository.findByRoleId(2L);
 assertEquals(1, userTaskInstances.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();

 List<UserTaskInstanceEntity> userTaskInstances = repository.findByCandidateUserId(1L);
 assertEquals(0, userTaskInstances.size());

 userTaskInstances = repository.findByCandidateUserId(2L);
 assertEquals(1, userTaskInstances.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();

 // Set up userTaskInstance
 userTaskInstance.setId(1L);
 userTaskInstance.setUserTaskId("task1");
 userTaskInstance.setStatus(FlowNodeStatus.PENDING);
 userTaskInstance.setApproved(false);
 userTaskInstance.setOperatorId(1L);
 userTaskInstance.setComment("Comment1");
 userTaskInstance.setExpireIntervalSeconds(3600);
 userTaskInstance.setExternalFlowInstanceId("instance1");

 // Test update with UserTaskInstanceEntity
 UserTaskInstanceEntity updatedUserTaskInstance = repository.update(userTaskInstance);
 assertNotNull(updatedUserTaskInstance);
 assertEquals("task1", updatedUserTaskInstance.getUserTaskId());
 assertEquals(FlowNodeStatus.PENDING, updatedUserTaskInstance.getStatus());
 assertFalse(updatedUserTaskInstance.isApproved());
 assertEquals(1L, updatedUserTaskInstance.getOperatorId());
 assertEquals("Comment1", updatedUserTaskInstance.getComment());
 assertEquals(3600, updatedUserTaskInstance.getExpireIntervalSeconds());
 assertEquals("instance1", updatedUserTaskInstance.getExternalFlowInstanceId());

 // Test update with UserTaskInstanceEntity and null id
 UserTaskInstanceEntity nullIdUserTaskInstance = new UserTaskInstanceEntity();
 nullIdUserTaskInstance.setId(null);
 nullIdUserTaskInstance.setUserTaskId("task2");
 nullIdUserTaskInstance.setStatus(FlowNodeStatus.APPROVED);
 nullIdUserTaskInstance.setApproved(true);
 nullIdUserTaskInstance.setOperatorId(2L);
 nullIdUserTaskInstance.setComment("Comment2");
 nullIdUserTaskInstance.setExpireIntervalSeconds(7200);
 nullIdUserTaskInstance.setExternalFlowInstanceId("instance2");

 int updateCount = repository.update(nullIdUserTaskInstance);
 assertEquals(0, updateCount);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();
 Long id = 1L;
 String userTaskId = "task1";

 int updateCount = repository.updateUserTaskIdById(id, userTaskId);
 assertEquals(1, updateCount);

 UserTaskInstanceEntity userTaskInstance = repository.findByStatus(FlowNodeStatus.APPROVED).get(0);
 assertEquals("task1", userTaskInstance.getUserTaskId());
 }

 @Test
 public void testUpdateStatusById() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.REJECTED;

 int updateCount = repository.updateStatusById(id, status);
 assertEquals(1, updateCount);

 UserTaskInstanceEntity userTaskInstance = repository.findByStatus(FlowNodeStatus.REJECTED).get(0);
 assertEquals(status, userTaskInstance.getStatus());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();
 Long instanceId = 1L;

 int deleteCount = repository.deleteByFlowInstanceId(instanceId);
 assertEquals(1, deleteCount);

 List<UserTaskInstanceEntity> userTaskInstances = repository.findByFlowInstanceId(instanceId);
 assertEquals(0, userTaskInstances.size());
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activity1";

 Optional<UserTaskInstanceEntity> userTaskInstanceOptional = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 assertFalse(userTaskInstanceOptional.isPresent());

 UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
 userTaskInstance.setFlowInstanceId(flowInstanceId);
 userTaskInstance.setFlowNodeType(instanceType);
 userTaskInstance.setActivityId(activityId);
 repository.save(userTaskInstance);

 userTaskInstanceOptional = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 assertTrue(userTaskInstanceOptional.isPresent());
 assertEquals(userTaskInstance.getId(), userTaskInstanceOptional.get().getId());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name1";

 Optional<UserTaskInstanceEntity> userTaskInstanceOptional = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 assertFalse(userTaskInstanceOptional.isPresent());

 UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
 userTaskInstance.setFlowInstanceId(flowInstanceId);
 userTaskInstance.setFlowNodeType(instanceType);
 userTaskInstance.setName(name);
 repository.save(userTaskInstance);

 userTaskInstanceOptional = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 assertTrue(userTaskInstanceOptional.isPresent());
 assertEquals(userTaskInstance.getId(), userTaskInstanceOptional.get().getId());
 }

 @Test
 public void testFindConfirmById() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();
 Long id = 1L;

 Boolean confirm = repository.findConfirmById(id);
 assertFalse(confirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepositoryImpl();
 Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L, 3L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 List<UserTaskInstanceEntity> userTaskInstances = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 assertEquals(0, userTaskInstances.size());

 UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
 userTaskInstance1.setFlowInstanceId(1L);
 userTaskInstance1.setStatus(status);
 repository.save(userTaskInstance1);

 UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
 userTaskInstance2.setFlowInstanceId(2L);
 userTaskInstance2.setStatus(status);
 repository.save(userTaskInstance2);

 List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(userTaskInstance1, userTaskInstance2);
 ExampleMatcher matcher = ExampleMatcher.matchingAllProperties().withIgnoreCase();
 ExampleMatchResult<UserTaskInstanceEntity> matchResult = new UserTaskInstanceEntity().matching(expectedUserTaskInstances, matcher);
 List<UserTaskInstanceEntity> userTaskInstancesFromDB = repository.findAll(matchResult.getExample()).getContent();
 assertEquals(expectedUserTaskInstances, userTaskInstancesFromDB);
 }
}