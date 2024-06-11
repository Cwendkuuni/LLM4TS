import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertFalse(userTaskInstances.isEmpty());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, null);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertFalse(userTaskInstances.isEmpty());

 List<String> resourceRoleIdentifiers = List.of("role1", "role2");
 userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertFalse(userTaskInstances.isEmpty());
 }

 @ParameterizedTest
 @ValueSource(strings = { "role1", "role2" })
 public void testFindByResourceRoleIdentifierIn(String resourceRoleIdentifier) {
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(List.of(resourceRoleIdentifier));
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertFalse(userTaskInstances.isEmpty());
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(roleId);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertFalse(userTaskInstances.isEmpty());
 }

 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserId(userId);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertFalse(userTaskInstances.isEmpty());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setUserTaskId("userTask1");
 entity.setStatus(FlowNodeStatus.APPROVED);
 entity.setApproved(true);
 entity.setOperatorId(1L);
 entity.setComment("test comment");
 entity.setExpireIntervalSeconds(300);
 entity.setExternalFlowInstanceId("externalFlowInstance1");

 int updatedRows = userTaskInstanceRepository.update(entity);
 Assertions.assertEquals(1, updatedRows);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "userTask2";

 int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 Assertions.assertEquals(1, updatedRows);
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.REJECTED;

 int updatedRows = userTaskInstanceRepository.updateStatusById(id, status);
 Assertions.assertEquals(1, updatedRows);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 Assertions.assertEquals(1, deletedRows);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activity1";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name1";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Assertions.assertTrue(userTaskInstanceRepository.findConfirmById(id));
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<Long> flowInstanceIds = List.of(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertFalse(userTaskInstances.isEmpty());
 }

}