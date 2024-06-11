import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.exception.FlowException;

public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @org.junit.jupiter.api.BeforeEach
 public void setup() {
 userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);
 }

 // Test for findByStatus method
 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedUserTaskInstances);
 List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
 }

 // Test for findByCandidateUserIdOrRoleIds method with valid parameters
 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedUserTaskInstances);
 List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
 }

 // Test for findByCandidateUserIdOrResourceRoleIdentifier method with valid parameters
 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
 List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers)).thenReturn(expectedUserTaskInstances);
 List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
 }

 // Test for findByResourceRoleIdentifierIn method with valid parameters
 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");
 List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier)).thenReturn(expectedUserTaskInstances);
 List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
 Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
 }

 // Test for findByRoleId method with valid parameter
 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(expectedUserTaskInstances);
 List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByRoleId(roleId);
 Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
 }

 // Test for findByCandidateUserId method with valid parameter
 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(expectedUserTaskInstances);
 List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserId(userId);
 Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
 }

 // Test for update method with valid parameters
 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
 when(userTaskInstanceRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
 userTaskInstanceRepository.update(updatedEntity);
 verify(userTaskInstanceRepository).update(updatedEntity);
 }

 // Test for updateUserTaskIdById method with valid parameters
 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "userTaskId";
 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(new UserTaskInstanceEntity()));
 userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 verify(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);
 }

 // Test for updateStatusById method with valid parameters
 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(new UserTaskInstanceEntity()));
 userTaskInstanceRepository.updateStatusById(id, status);
 verify(userTaskInstanceRepository).updateStatusById(id, status);
 }

 // Test for deleteByFlowInstanceId method with valid parameter
 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 verify(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
 }

 // Test for findByInstanceTypeAndActivityId method with valid parameters
 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> expectedUserTaskInstance = Optional.of(new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
 .thenReturn(expectedUserTaskInstance);
 Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assertions.assertEquals(expectedUserTaskInstance.get(), actualUserTaskInstance.get());
 }

 // Test for findByInstanceTypeAndName method with valid parameters
 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> expectedUserTaskInstance = Optional.of(new UserTaskInstanceEntity());
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
 .thenReturn(expectedUserTaskInstance);
 Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assertions.assertEquals(expectedUserTaskInstance.get(), actualUserTaskInstance.get());
 }

 // Test for findConfirmById method with valid parameter
 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean expectedConfirm = true;
 Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(expectedConfirm);
 Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);
 Assertions.assertEquals(expectedConfirm, actualConfirm);
 }

 // Test for findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid parameters
 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 List<UserTaskInstanceEntity> expectedUserTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
 Pageable pageable = PageRequest.of(0, 10);
 Page<UserTaskInstanceEntity> expectedPage = new PageImpl<>(expectedUserTaskInstances, pageable, 1);
 Mockito.when(userTaskInstanceRepository.findAll(pageable)).thenReturn(expectedPage);
 List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
 }

 // Test for update method with invalid parameter - entity not found
 @Test
 public void testUpdateEntityNotFound() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
 when(userTaskInstanceRepository.findById(entity.getId())).thenReturn(Optional.empty());
 Executable updateCall = () -> userTaskInstanceRepository.update(updatedEntity);
 Assertions.assertThrows(FlowException.class, updateCall);
 }

 // Test for updateUserTaskIdById method with invalid parameter - entity not found
 @Test
 public void testUpdateUserTaskIdByIdEntityNotFound() {
 Long id = 1L;
 String userTaskId = "userTaskId";
 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.empty());
 Executable updateCall = () -> userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 Assertions.assertThrows(FlowException.class, updateCall);
 }

 // Test for updateStatusById method with invalid parameter - entity not found
 @Test
 public void testUpdateStatusByIdEntityNotFound() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.empty());
 Executable updateCall = () -> userTaskInstanceRepository.updateStatusById(id, status);
 Assertions.assertThrows(FlowException.class, updateCall);
 }

 // Test for deleteByFlowInstanceId method with invalid parameter - empty flowInstanceId
 @Test
 public void testDeleteByFlowInstanceIdEmptyFlowInstanceId() {
 Long instanceId = null;
 userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 verifyNoInteractions(userTaskInstanceRepository);
 }

 // Test for findByInstanceTypeAndActivityId method with invalid parameters - null instanceType or activityId
 @Test
 public void testFindByInstanceTypeAndActivityIdInvalidParameters() {
 FlowNodeType instanceType = null;
 String activityId = null;
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> expectedUserTaskInstance = Optional.empty();
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
 .thenReturn(expectedUserTaskInstance);
 Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assertions.assertFalse(actualUserTaskInstance.isPresent());
 }

 // Test for findByInstanceTypeAndName method with invalid parameters - null instanceType or name
 @Test
 public void testFindByInstanceTypeAndNameInvalidParameters() {
 FlowNodeType instanceType = null;
 String name = null;
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> expectedUserTaskInstance = Optional.empty();
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
 .thenReturn(expectedUserTaskInstance);
 Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assertions.assertFalse(actualUserTaskInstance.isPresent());
 }

}