import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertNotNull(userTaskInstanceEntities);
 Assertions.assertFalse(userTaskInstanceEntities.isEmpty());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 Assertions.assertNotNull(userTaskInstanceEntities);
 // assert size > 0, assuming there are records in the database that match the criteria
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 Assertions.assertNotNull(userTaskInstanceEntities);
 // assert size > 0, assuming there are records in the database that match the criteria
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifier = Arrays.asList("role1");
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
 Assertions.assertNotNull(userTaskInstanceEntities);
 // assert size > 0, assuming there are records in the database that match the criteria
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
 Assertions.assertNotNull(userTaskInstanceEntities);
 // assert size > 0, assuming there are records in the database that match the criteria
 }

 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
 Assertions.assertNotNull(userTaskInstanceEntities);
 // assert size > 0, assuming there are records in the database that match the criteria
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 // set necessary fields
 UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
 // set updated fields

 Executable updateMethod = () -> userTaskInstanceRepository.update(entity);
 int rowsAffected = updateMethod.execute();
 Assertions.assertGreaterEq(rowsAffected, 0);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "userTaskId";

 Executable updateMethod = () -> userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 int rowsAffected = updateMethod.execute();
 Assertions.assertGreaterEq(rowsAffected, 0);
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.REJECTED;

 Executable updateMethod = () -> userTaskInstanceRepository.updateStatusById(id, status);
 int rowsAffected = updateMethod.execute();
 Assertions.assertGreaterEq(rowsAffected, 0);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;

 Executable deleteMethod = () -> userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 int rowsAffected = deleteMethod.execute();
 Assertions.assertGreaterEq(rowsAffected, 0);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.TASK;
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assertions.assertNotNull(userTaskInstanceEntityOptional);
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.TASK;
 String name = "name";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assertions.assertNotNull(userTaskInstanceEntityOptional);
 Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;

 Optional<Boolean> confirmOptional = userTaskInstanceRepository.findConfirmById(id);
 Assertions.assertNotNull(confirmOptional);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 Assertions.assertNotNull(userTaskInstanceEntities);
 // assert size > 0, assuming there are records in the database that match the criteria
 }

}