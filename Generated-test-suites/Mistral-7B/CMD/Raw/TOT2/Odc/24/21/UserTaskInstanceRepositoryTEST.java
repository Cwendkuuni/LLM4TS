package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepositoryTest;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringJUnitConfig(classes = UserTaskInstanceRepositoryTest.class)
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Autowired
 public void setUserTaskInstanceRepository(UserTaskInstanceRepository userTaskInstanceRepository) {
 this.userTaskInstanceRepository = userTaskInstanceRepository;
 }

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 assertEquals(2, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 assertEquals(2, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifier = Arrays.asList("role1");
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
 assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
 assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
 assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setUserTaskId("task1");
 userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
 userTaskInstanceEntity.setApproved(true);
 userTaskInstanceEntity.setOperatorId(1L);
 userTaskInstanceEntity.setComment("comment1");
 userTaskInstanceEntity.setExpireIntervalSeconds(10L);
 userTaskInstanceEntity.setExternalFlowInstanceId(1L);

 UserTaskInstanceEntity updatedUserTaskInstanceEntity = new UserTaskInstanceEntity();
 updatedUserTaskInstanceEntity.setId(1L);
 updatedUserTaskInstanceEntity.setUserTaskId("task2");
 updatedUserTaskInstanceEntity.setStatus(FlowNodeStatus.REJECTED);
 updatedUserTaskInstanceEntity.setApproved(false);
 updatedUserTaskInstanceEntity.setOperatorId(2L);
 updatedUserTaskInstanceEntity.setComment("comment2");
 updatedUserTaskInstanceEntity.setExpireIntervalSeconds(20L);
 updatedUserTaskInstanceEntity.setExternalFlowInstanceId(2L);

 int result = userTaskInstanceRepository.update(updatedUserTaskInstanceEntity);
 assertEquals(1, result);

 UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(1L).get();
 assertEquals("task2", entity.getUserTaskId());
 assertEquals(FlowNodeStatus.REJECTED, entity.getStatus());
 assertEquals(false, entity.isApproved());
 assertEquals(2L, entity.getOperatorId());
 assertEquals("comment2", entity.getComment());
 assertEquals(20, entity.getExpireIntervalSeconds());
 assertEquals(2L, entity.getExternalFlowInstanceId());
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "task3";

 int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 assertEquals(1, result);

 UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(id).get();
 assertEquals("task3", entity.getUserTaskId());
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.REJECTED;

 int result = userTaskInstanceRepository.updateStatusById(id, status);
 assertEquals(1, result);

 UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(id).get();
 assertEquals(status, entity.getStatus());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 assertEquals(1, result);

 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findAll();
 assertEquals(0, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.START;
 String activityId = "activity1";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 assertEquals(true, userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.START;
 String name = "start";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 assertEquals(true, userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 assertEquals(true, confirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindEmptyByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = Collections.emptyList();
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 assertEquals(0, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByInvalidStatus() {
 Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.REJECTED;

 Assertions.assertThrows(RuntimeException.class, () -> {
 userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 });
 }
}