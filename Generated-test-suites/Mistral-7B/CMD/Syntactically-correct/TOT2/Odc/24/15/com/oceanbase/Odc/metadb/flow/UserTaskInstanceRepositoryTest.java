import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@JpaTest
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Autowired
 public void setUserTaskInstanceRepository(UserTaskInstanceRepository userTaskInstanceRepository) {
 this.userTaskInstanceRepository = userTaskInstanceRepository;
 }

 @Test
 public void testFindByStatus() {
 // Given
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(status);
 // Then
 Assertions.assertNotNull(userTaskInstanceEntities);
 Assertions.assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 List<FlowNodeStatus> statuses = Arrays.asList(FlowNodeStatus.APPROVED, FlowNodeStatus.REJECTED);
 List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, statuses, resourceRoleIdentifiers);
 // Then
 Assertions.assertNotNull(userTaskInstanceEntities);
 Assertions.assertEquals(2, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 // Given
 List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 // Then
 Assertions.assertNotNull(userTaskInstanceEntities);
 Assertions.assertEquals(2, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByRoleId() {
 // Given
 Long roleId = 1L;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
 // Then
 Assertions.assertNotNull(userTaskInstanceEntities);
 Assertions.assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 Long userId = 1L;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
 // Then
 Assertions.assertNotNull(userTaskInstanceEntities);
 Assertions.assertEquals(1, userTaskInstanceEntities.size());
 }

 @Test
 public void testUpdate() {
 // Given
 Long id = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(id);
 // When
 int rowsAffected = userTaskInstanceRepository.update(userTaskInstanceEntity);
 // Then
 Assertions.assertEquals(1, rowsAffected);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 Long id = 1L;
 String userTaskId = "userTaskId";
 // When
 int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 // Then
 Assertions.assertEquals(1, rowsAffected);
 }

 @Test
 public void testUpdateStatusById() {
 // Given
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 // When
 int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
 // Then
 Assertions.assertEquals(1, rowsAffected);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;
 // When
 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 // Then
 Assertions.assertEquals(1, rowsAffected);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 FlowNodeType instanceType = FlowNodeType.START;
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 // When
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 // Then
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 FlowNodeType instanceType = FlowNodeType.END;
 String name = "name";
 Long flowInstanceId = 1L;
 // When
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 // Then
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 // Given
 Long id = 1L;
 // When
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 // Then
 Assertions.assertTrue(confirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 List<Long> flowInstanceIds = Arrays.asList(1L, 2L, 3L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 // Then
 Assertions.assertEquals(2, userTaskInstanceEntities.size());
 Assertions.assertFalse(userTaskInstanceEntities.contains(null));
 }

 @Test
 public void testFindByNullStatus() {
 // Given
 // When, Then
 Assertions.assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.findByStatus(null));
 }

 @Test
 public void testFindByEmptyCandidateUserIdsOrRoleIds() {
 // Given
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, Collections.emptyList(), Collections.emptyList());
 // Then
 Assertions.assertEquals(0, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByEmptyResourceRoleIdentifier() {
 // Given
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.emptyList());
 // Then
 Assertions.assertEquals(0, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByNegativeRoleId() {
 // Given
 Long roleId = -1L;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
 // Then
 Assertions.assertEquals(0, userTaskInstanceEntities.size());
 }

 @Test
 public void testFindByNullFlowInstanceId() {
 // Given
 // When, Then
 Assertions.assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.deleteByFlowInstanceId(null));
 }

 @Test
 public void testFindByNullInstanceType() {
 // Given
 // When, Then
 Assertions.assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.findByInstanceTypeAndActivityId(null, "activityId", 1L));
 }

 @Test
 public void testFindByNullActivityId() {
 // Given
 // When, Then
 Assertions.assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, null, 1L));
 }

 @Test
 public void testFindByNullFlowInstanceIdAndStatus() {
 // Given
 // When, Then
 Assertions.assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(null, FlowNodeStatus.APPROVED));
 }
}