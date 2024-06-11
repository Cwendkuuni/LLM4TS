import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
 assertNotNull(userTaskInstanceEntities);
 // Edge case: Empty list
 assertEmpty(userTaskInstanceRepository.findByStatus(null));
 // Exception handling: NullPointerException
 assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.findByStatus(null));
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 // Then
 assertNotNull(userTaskInstanceEntities);
 // Edge case: Empty list
 assertEmpty(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, null));
 // Edge case: No matching entities
 assertEmpty(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, List.of(3L, 4L)));
 // Exception handling: NullPointerException
 assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, null));
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 // Given
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = List.of("admin", "developer");
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 // Then
 assertNotNull(userTaskInstanceEntities);
 // Edge case: Empty list
 assertEmpty(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, null));
 // Edge case: No matching entities
 assertEmpty(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, List.of("editor", "tester")));
 // Exception handling: NullPointerException
 assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, null));
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 // Given
 Collection<String> resourceRoleIdentifier = List.of("admin", "developer");
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
 // Then
 assertNotNull(userTaskInstanceEntities);
 // Edge case: Empty list
 assertEmpty(userTaskInstanceRepository.findByResourceRoleIdentifierIn(null));
 // Edge case: No matching entities
 assertEmpty(userTaskInstanceRepository.findByResourceRoleIdentifierIn(List.of("editor", "tester")));
 // Exception handling: NullPointerException
 assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.findByResourceRoleIdentifierIn(null));
 }

 @Test
 public void testFindByRoleId() {
 // Given
 Long roleId = 1L;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
 // Then
 assertNotNull(userTaskInstanceEntities);
 // Edge case: Empty list
 assertEmpty(userTaskInstanceRepository.findByRoleId(null));
 // Edge case: No matching entities
 assertEmpty(userTaskInstanceRepository.findByRoleId(2L));
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 Long userId = 1L;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
 // Then
 assertNotNull(userTaskInstanceEntities);
 // Edge case: Empty list
 assertEmpty(userTaskInstanceRepository.findByCandidateUserId(null));
 // Edge case: No matching entities
 assertEmpty(userTaskInstanceRepository.findByCandidateUserId(2L));
 }

 @Test
 public void testUpdate() {
 // Given
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 // When
 int rowsAffected = userTaskInstanceRepository.update(entity);
 // Then
 assertEquals(1, rowsAffected);
 // Edge case: Null entity
 assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.update(null));
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 Long id = 1L;
 String userTaskId = "test";
 // When
 int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 // Then
 assertEquals(1, rowsAffected);
 // Edge case: Null id or userTaskId
 assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.updateUserTaskIdById(null, null));
 }

 @Test
 public void testUpdateStatusById() {
 // Given
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 // When
 int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
 // Then
 assertEquals(1, rowsAffected);
 // Edge case: Null id or status
 assertThrows(NullPointerException.class, () -> userTaskInstanceRepository.updateStatusById(null, null));
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;
 // When
 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 // Then
 assertEquals(1, rowsAffected);
 // Edge case: Negative id
 assertThrows(IllegalArgumentException.class, () -> userTaskInstanceRepository.deleteByFlowInstanceId(-1L));
 // Edge case: Zero id
 assertThrows(IllegalArgumentException.class, () -> userTaskInstanceRepository.deleteByFlowInstanceId(0L));
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "test";
 Long flowInstanceId = 1L;
 // When
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 // Then
 assertTrue(userTaskInstanceEntityOptional.isPresent());
 // Edge case: Null instanceType, activityId or flowInstanceId
 assertFalse(userTaskInstanceRepository.findByInstanceTypeAndActivityId(null, null, null).isPresent());
 // Edge case: No matching entity
 assertFalse(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.DECISION, "test", 2L).isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "test";
 Long flowInstanceId = 1L;
 // When
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 // Then
 assertTrue(userTaskInstanceEntityOptional.isPresent());
 // Edge case: Null instanceType, name or flowInstanceId
 assertFalse(userTaskInstanceRepository.findByInstanceTypeAndName(null, null, null).isPresent());
 // Edge case: No matching entity
 assertFalse(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.DECISION, "test", 2L).isPresent());
 }

 @Test
 public void testFindConfirmById() {
 // Given
 Long id = 1L;
 // When
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 // Then
 assertNull(confirm);
 // Edge case: Null id
 assertNull(userTaskInstanceRepository.findConfirmById(null));
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 Collection<Long> flowInstanceIds = List.of(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 // Then
 assertNotNull(userTaskInstanceEntities);
 // Edge case: Empty flowInstanceIds
 assertEmpty(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(null, status));
 // Edge case: Empty status
 assertEmpty(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, null));
 // Edge case: No matching entities
 assertEmpty(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(List.of(3L, 4L), FlowNodeStatus.REJECTED));
 }

}