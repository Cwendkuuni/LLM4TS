import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 /**
 * Test method for {@link UserTaskInstanceRepository#findByStatus(FlowNodeStatus)}
 */
 @Test
 public void testFindByStatus() {
 UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
 // set the expected values for the entity
 // ...
 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 // check if the returned list contains the expected entity
 Assert.assertTrue("Expected entity not found in the list", actual.contains(expected));
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByCandidateUserIdOrRoleIds(Long, Collection)}
 */
 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // given
 Long userId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);

 // when
 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

 // then
 // check the size of the returned list
 Assert.assertEquals("Size of the list should be greater than zero", actual.size(), greaterThan(0));

 // check if each entity in the list has the given userId or roleIds
 for (UserTaskInstanceEntity entity : actual) {
 if (entity.getCandidateUserId() != null && entity.getCandidateUserId().equals(userId)) {
 // entity has the given userId
 } else if (entity.getRoleIds() != null && roleIds.containsAll(entity.getRoleIds())) {
 // entity has one of the given roleIds
 } else {
 throw new AssertionError("Unexpected UserTaskInstanceEntity found in the list");
 }
 }
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByResourceRoleIdentifier(Collection)}
 */
 @Test
 public void testFindByResourceRoleIdentifier() {
 // given
 List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

 // when
 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

 // then
 // check the size of the returned list
 Assert.assertEquals("Size of the list should be greater than zero", actual.size(), greaterThan(0));

 // check if each entity in the list has one of the given resourceRoleIdentifiers
 for (UserTaskInstanceEntity entity : actual) {
 if (resourceRoleIdentifiers.contains(entity.getResourceRoleIdentifier())) {
 // entity has one of the given resourceRoleIdentifiers
 } else {
 throw new AssertionError("Unexpected UserTaskInstanceEntity found in the list");
 }
 }
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByRoleId(Long)}
 */
 @Test
 public void testFindByRoleId() {
 // given
 Long roleId = 1L;

 // when
 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(roleId);

 // then
 // check the size of the returned list
 Assert.assertEquals("Size of the list should be greater than zero", actual.size(), greaterThan(0));

 // check if each entity in the list has the given roleId
 for (UserTaskInstanceEntity entity : actual) {
 if (entity.getRoleId().equals(roleId)) {
 // entity has the given roleId
 } else {
 throw new AssertionError("Unexpected UserTaskInstanceEntity found in the list");
 }
 }
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByCandidateUserId(Long)}
 */
 @Test
 public void testFindByCandidateUserId() {
 // given
 Long userId = 1L;

 // when
 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(userId);

 // then
 // check the size of the returned list
 Assert.assertEquals("Size of the list should be greater than zero", actual.size(), greaterThan(0));

 // check if each entity in the list has the given userId
 for (UserTaskInstanceEntity entity : actual) {
 if (entity.getCandidateUserId().equals(userId)) {
 // entity has the given userId
 } else {
 throw new AssertionError("Unexpected UserTaskInstanceEntity found in the list");
 }
 }
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#update(UserTaskInstanceEntity)}
 */
 @Test
 public void testUpdate() {
 // given
 UserTaskInstanceEntity entityToUpdate = new UserTaskInstanceEntity();
 // set the values for the entity to be updated
 // ...

 // when
 int rowsAffected = userTaskInstanceRepository.update(entityToUpdate);

 // then
 // check if the number of rows affected is greater than zero
 Assert.assertEquals("Number of rows affected should be greater than zero", rowsAffected, greaterThan(0));
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#updateUserTaskIdById(Long, String)}
 */
 @Test
 public void testUpdateUserTaskIdById() {
 // given
 Long id = 1L;
 String userTaskId = "userTask1";

 // when
 int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

 // then
 // check if the number of rows affected is greater than zero
 Assert.assertEquals("Number of rows affected should be greater than zero", rowsAffected, greaterThan(0));

 // check if the updated UserTaskInstanceEntity has the given userTaskId
 UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).get();
 Assert.assertEquals("Updated UserTaskInstanceEntity should have the given userTaskId", userTaskId, actual.getUserTaskId());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#updateStatusById(Long, FlowNodeStatus)}
 */
 @Test
 public void testUpdateStatusById() {
 // given
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 // when
 int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);

 // then
 // check if the number of rows affected is greater than zero
 Assert.assertEquals("Number of rows affected should be greater than zero", rowsAffected, greaterThan(0));

 // check if the updated UserTaskInstanceEntity has the given status
 UserTaskInstanceEntity actual = userTaskInstanceRepository.findById(id).get();
 Assert.assertEquals("Updated UserTaskInstanceEntity should have the given status", status.name(), actual.getStatus().name());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#deleteByFlowInstanceId(Long)}
 */
 @Test
 public void testDeleteByFlowInstanceId() {
 // given
 Long instanceId = 1L;

 // when
 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

 // then
 // check if the number of rows affected is greater than zero
 Assert.assertEquals("Number of rows affected should be greater than zero", rowsAffected, greaterThan(0));

 // check if there's no UserTaskInstanceEntity with the given flowInstanceId
 Assert.assertNull("No UserTaskInstanceEntity should be found with the given flowInstanceId",
 userTaskInstanceRepository.findByFlowInstanceId(instanceId));
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndActivityId(FlowNodeType, String, Long)}
 */
 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // given
 FlowNodeType instanceType = FlowNodeType.START;
 String activityId = "activity1";
 Long flowInstanceId = 1L;

 // when
 Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

 // then
 // check if the optional is present
 Assert.assertTrue("Optional should be present", actual.isPresent());

 // check if the UserTaskInstanceEntity has the given instanceType, activityId, and flowInstanceId
 UserTaskInstanceEntity userTaskInstanceEntity = actual.get();
 Assert.assertEquals("UserTaskInstanceEntity should have the given instanceType", instanceType.name(), userTaskInstanceEntity.getInstanceType().name());
 Assert.assertEquals("UserTaskInstanceEntity should have the given activityId", activityId, userTaskInstanceEntity.getActivityId());
 Assert.assertEquals("UserTaskInstanceEntity should have the given flowInstanceId", flowInstanceId, userTaskInstanceEntity.getFlowInstanceId());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndName(FlowNodeType, String, Long)}
 */
 @Test
 public void testFindByInstanceTypeAndName() {
 // given
 FlowNodeType instanceType = FlowNodeType.END;
 String name = "endNode";
 Long flowInstanceId = 1L;

 // when
 Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

 // then
 // check if the optional is present
 Assert.assertTrue("Optional should be present", actual.isPresent());

 // check if the UserTaskInstanceEntity has the given instanceType, name, and flowInstanceId
 UserTaskInstanceEntity userTaskInstanceEntity = actual.get();
 Assert.assertEquals("UserTaskInstanceEntity should have the given instanceType", instanceType.name(), userTaskInstanceEntity.getInstanceType().name());
 Assert.assertEquals("UserTaskInstanceEntity should have the given name", name, userTaskInstanceEntity.getName());
 Assert.assertEquals("UserTaskInstanceEntity should have the given flowInstanceId", flowInstanceId, userTaskInstanceEntity.getFlowInstanceId());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findConfirmById(Long)}
 */
 @Test
 public void testFindConfirmById() {
 // given
 Long id = 1L;

 // when
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);

 // then
 // check if the confirm variable is true or false based on the database value
 // ...
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findApprovalInstanceIdByFlowInstanceIdAndStatus(Collection, FlowNodeStatus)}
 */
 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // given
 Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 // when
 List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

 // then
 // check the size of the returned list
 Assert.assertEquals("Size of the list should be equal to the number of flowInstanceIds", actual.size(), flowInstanceIds.size());

 // check if each UserTaskInstanceEntity in the list has the given status
 for (UserTaskInstanceEntity entity : actual) {
 Assert.assertEquals("UserTaskInstanceEntity should have the given status", status.name(), entity.getStatus().name());
 }
 }
}