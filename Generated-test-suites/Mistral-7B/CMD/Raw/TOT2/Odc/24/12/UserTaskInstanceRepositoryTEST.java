import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@JpaTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setStatus(FlowNodeStatus.APPROVED);
 userTaskInstanceRepository.save(entity);

 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertEquals(1, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 String roleIdentifier = "admin";

 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId,
 Collection.of(roleIdentifier));
 Assertions.assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindByResourceRoleIdentifier() {
 String resourceRoleIdentifier = "admin";

 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
 Collection.of(resourceRoleIdentifier));
 Assertions.assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;

 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(roleId);
 Assertions.assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;

 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(userId);
 Assertions.assertFalse(entities.isEmpty());
 }

 @Test
 @Transactional
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setStatus(FlowNodeStatus.PENDING);
 userTaskInstanceRepository.save(entity);
 Long id = entity.getId();

 UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
 updatedEntity.setId(id);
 updatedEntity.setUserTaskId("testUserTaskId");
 updatedEntity.setStatus(FlowNodeStatus.APPROVED);
 updatedEntity.setApproved(true);
 updatedEntity.setOperatorId(1L);
 updatedEntity.setComment("test comment");
 updatedEntity.setExpireIntervalSeconds(3600);
 updatedEntity.setExternalFlowInstanceId("testExternalFlowInstanceId");

 int result = userTaskInstanceRepository.update(updatedEntity);
 Assertions.assertEquals(1, result);
 }

 @Test
 @Transactional
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "testUserTaskId";

 int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 Assertions.assertEquals(1, result);
 }

 @Test
 @Transactional
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.PENDING;

 int result = userTaskInstanceRepository.updateStatusById(id, status);
 Assertions.assertEquals(1, result);
 }

 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;

 int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 Assertions.assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Long flowInstanceId = 1L;
 String activityId = "activityId";
 FlowNodeType instanceType = FlowNodeType.USER_TASK;

 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
 activityId, flowInstanceId);
 Assertions.assertFalse(entity.isEmpty());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Long flowInstanceId = 1L;
 String name = "name";
 FlowNodeType instanceType = FlowNodeType.USER_TASK;

 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
 flowInstanceId);
 Assertions.assertFalse(entity.isEmpty());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;

 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 Assertions.assertFalse(confirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Long flowInstanceId = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
 Collection.of(flowInstanceId), status);
 Assertions.assertFalse(entities.isEmpty());
 }

 @Test(expected = RuntimeException.class)
 public void testFindByIdWithInvalidId() {
 Long id = -1L;

 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findById(id);
 Assertions.assertFalse(entity.isPresent());
 }

 @Test(expected = RuntimeException.class)
 public void testUpdateWithNullEntity() {
 UserTaskInstanceEntity entity = null;

 int result = userTaskInstanceRepository.update(entity);
 Assertions.assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.update(entity));
 }

 @Test(expected = RuntimeException.class)
 public void testUpdateWithInvalidId() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 Long id = -1L;
 entity.setId(id);

 int result = userTaskInstanceRepository.update(entity);
 Assertions.assertThrows(RuntimeException.class, () -> userTaskInstanceRepository.update(entity));
 }
}