import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@JpaTest
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertEquals(1, userTaskInstances.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertTrue(userTaskInstances.size() > 0);
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = List.of("role1", "role2");
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertTrue(userTaskInstances.size() > 0);
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifier = List.of("role1");
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertTrue(userTaskInstances.size() > 0);
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(roleId);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertTrue(userTaskInstances.size() > 0);
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setUserTaskId("task1");
 entity.setStatus(FlowNodeStatus.APPROVED);
 entity.setApproved(true);
 entity.setOperatorId(1L);
 entity.setComment("Comment");
 entity.setExpireIntervalSeconds(10);
 entity.setExternalFlowInstanceId(1L);

 int result = userTaskInstanceRepository.update(entity);
 Assertions.assertEquals(1, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "task2";

 int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 Assertions.assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.REJECTED;

 int result = userTaskInstanceRepository.updateStatusById(id, status);
 Assertions.assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 Assertions.assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.START;
 String activityId = "activity1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.END;
 String name = "End";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 Assertions.assertFalse(confirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = List.of(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertTrue(userTaskInstances.size() > 0);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFindByInvalidStatus() {
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(null);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFindByCandidateUserIdOrNullRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = null;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFindByCandidateUserIdOrNullResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = null;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFindByResourceRoleIdentifierNull() {
 Collection<String> resourceRoleIdentifier = null;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testUpdateNullEntity() {
 UserTaskInstanceEntity entity = null;
 int result = userTaskInstanceRepository.update(entity);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testUpdateUserTaskIdByIdNullId() {
 Long id = null;
 String userTaskId = "task2";
 int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testUpdateStatusByIdNullId() {
 Long id = 1L;
 FlowNodeStatus status = null;
 int result = userTaskInstanceRepository.updateStatusById(id, status);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testDeleteByFlowInstanceIdNullId() {
 Long instanceId = null;
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFindByInstanceTypeAndActivityIdNullInstanceType() {
 FlowNodeType instanceType = null;
 String activityId = "activity1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFindByInstanceTypeAndNameNullInstanceType() {
 FlowNodeType instanceType = null;
 String name = "End";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 }
}