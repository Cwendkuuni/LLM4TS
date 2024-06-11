import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.JpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

@JpaTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 /**
 * Test method for {@link UserTaskInstanceRepository#findByStatus(FlowNodeStatus)}
 */
 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertEquals(1, userTaskInstances.size());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByCandidateUserIdOrRoleIds(Long, Collection)}
 */
 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertEquals(2, userTaskInstances.size());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByResourceRoleIdentifier(Collection)}
 */
 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifiers = List.of("role1", "role2");
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertEquals(2, userTaskInstances.size());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByRoleId(Long)}
 */
 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(roleId);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertEquals(1, userTaskInstances.size());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByCandidateUserId(Long)}
 */
 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserId(userId);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertEquals(1, userTaskInstances.size());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#update(UserTaskInstanceEntity)}
 */
 @Test
 public void testUpdate() {
 Long id = 1L;
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(id);
 // Set other fields to be updated
 UserTaskInstanceEntity updatedEntity = userTaskInstanceRepository.save(entity);
 UserTaskInstanceEntity retrievedEntity = userTaskInstanceRepository.findById(id).orElseThrow();
 Assertions.assertNotNull(retrievedEntity);
 Assertions.assertEquals(updatedEntity, retrievedEntity);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#updateUserTaskIdById(Long, String)}
 */
 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "taskId1";
 int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 Assertions.assertEquals(1, rowsAffected);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#updateStatusById(Long, FlowNodeStatus)}
 */
 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.REJECTED;
 int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
 Assertions.assertEquals(1, rowsAffected);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#deleteByFlowInstanceId(Long)}
 */
 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 Assertions.assertEquals(1, rowsAffected);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndActivityId(FlowNodeType, String, Long)}
 */
 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activity1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndName(FlowNodeType, String, Long)}
 */
 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findConfirmById(Long)}
 */
 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 Assertions.assertTrue(confirm);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findApprovalInstanceIdByFlowInstanceIdAndStatus(Collection, FlowNodeStatus)}
 */
 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = List.of(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 Assertions.assertNotNull(userTaskInstances);
 Assertions.assertEquals(2, userTaskInstances.size());
 }
}