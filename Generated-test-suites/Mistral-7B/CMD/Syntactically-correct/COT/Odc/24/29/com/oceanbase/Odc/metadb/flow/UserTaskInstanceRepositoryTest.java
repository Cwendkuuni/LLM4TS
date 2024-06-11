package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

 @InjectMocks
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
 userTaskInstance1.setStatus(FlowNodeStatus.APPROVED);
 UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
 userTaskInstance2.setStatus(FlowNodeStatus.REJECTED);

 List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
 expectedResult.add(userTaskInstance1);

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);

 List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
 expectedResult.add(new UserTaskInstanceEntity());
 expectedResult.add(new UserTaskInstanceEntity());

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 assertEquals(expectedResult.size(), result.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByCandidateUserIdOrRoleIdsWithNullRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = null;

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 resourceRoleIdentifiers.add("role2");

 List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
 expectedResult.add(new UserTaskInstanceEntity());
 expectedResult.add(new UserTaskInstanceEntity());

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
 resourceRoleIdentifiers);
 assertEquals(expectedResult.size(), result.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByCandidateUserIdOrResourceRoleIdentifierWithNullUserId() {
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 resourceRoleIdentifiers.add("role2");

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null,
 resourceRoleIdentifiers);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;

 List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
 expectedResult.add(new UserTaskInstanceEntity());
 expectedResult.add(new UserTaskInstanceEntity());

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
 assertEquals(expectedResult.size(), result.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByRoleIdWithNullRoleId() {
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;

 List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
 expectedResult.add(new UserTaskInstanceEntity());
 expectedResult.add(new UserTaskInstanceEntity());

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
 assertEquals(expectedResult.size(), result.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByCandidateUserIdWithNullUserId() {
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 Long id = 1L;
 Long userTaskId = 2L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 String operatorId = "operator";
 String comment = "comment";
 Long expireIntervalSeconds = 10L;
 String externalFlowInstanceId = "externalFlowInstanceId";

 entity.setId(id);
 entity.setUserTaskId(null);
 entity.setStatus(FlowNodeStatus.PENDING);
 entity.setOperatorId(null);
 entity.setComment(null);
 entity.setExpireIntervalSeconds(null);
 entity.setExternalFlowInstanceId(null);

 UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
 updatedEntity.setId(id);
 updatedEntity.setUserTaskId(userTaskId);
 updatedEntity.setStatus(status);
 updatedEntity.setOperatorId(operatorId);
 updatedEntity.setComment(comment);
 updatedEntity.setExpireIntervalSeconds(expireIntervalSeconds);
 updatedEntity.setExternalFlowInstanceId(externalFlowInstanceId);

 int result = userTaskInstanceRepository.update(updatedEntity);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithNullEntity() {
 int result = userTaskInstanceRepository.update(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "userTaskId";

 int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateUserTaskIdByIdWithNullId() {
 String userTaskId = "userTaskId";

 int result = userTaskInstanceRepository.updateUserTaskIdById(null, userTaskId);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 int result = userTaskInstanceRepository.updateStatusById(id, status);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateStatusByIdWithNullId() {
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 int result = userTaskInstanceRepository.updateStatusById(null, status);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;

 int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByFlowInstanceIdWithNullInstanceId() {
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.TASK;
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
 activityId, flowInstanceId);
 assertFalse(result.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.TASK;
 String name = "name";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
 flowInstanceId);
 assertFalse(result.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;

 Boolean result = userTaskInstanceRepository.findConfirmById(id);
 assertFalse(result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 flowInstanceIds.add(2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
 status);
 assertTrue(result.isEmpty());
 }

}