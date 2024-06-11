import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @MockBean
 private UserTaskInstanceEntity userTaskInstanceEntity;

 @Test
 public void testFindByStatus() {
 // Given
 Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.any(FlowNodeStatus.class)))
 .thenReturn(List.of(userTaskInstanceEntity));

 // When & Then
 Assertions.assertEquals(List.of(userTaskInstanceEntity), userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED));
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 List<Long> roleIds = List.of(1L, 2L);
 Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds))
 .thenReturn(List.of(userTaskInstanceEntity));

 // When & Then
 Assertions.assertEquals(List.of(userTaskInstanceEntity), userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds));
 }

 @Test
 public void testFindByResourceRoleIdentifier() {
 // Given
 List<String> resourceRoleIdentifiers = List.of("role1", "role2");
 Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers))
 .thenReturn(List.of(userTaskInstanceEntity));

 // When & Then
 Assertions.assertEquals(List.of(userTaskInstanceEntity), userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers));
 }

 @Test
 public void testFindByRoleId() {
 // Given
 Long roleId = 1L;
 Mockito.when(userTaskInstanceRepository.findByRoleId(roleId))
 .thenReturn(List.of(userTaskInstanceEntity));

 // When & Then
 Assertions.assertEquals(List.of(userTaskInstanceEntity), userTaskInstanceRepository.findByRoleId(roleId));
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 Long userId = 1L;
 Mockito.when(userTaskInstanceRepository.findByCandidateUserId(userId))
 .thenReturn(List.of(userTaskInstanceEntity));

 // When & Then
 Assertions.assertEquals(List.of(userTaskInstanceEntity), userTaskInstanceRepository.findByCandidateUserId(userId));
 }

 @Test
 public void testUpdate() {
 // Given
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 Mockito.when(userTaskInstanceRepository.save(entity))
 .thenReturn(entity);

 // When
 UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
 updatedEntity.setId(1L);
 updatedEntity.setUserTaskId("new_user_task_id");
 updatedEntity.setStatus(FlowNodeStatus.APPROVED);
 updatedEntity.setApproved(true);
 updatedEntity.setOperatorId(1L);
 updatedEntity.setComment("test comment");
 updatedEntity.setExpireIntervalSeconds(10L);
 updatedEntity.setExternalFlowInstanceId("new_external_flow_instance_id");
 UserTaskInstanceEntity result = userTaskInstanceRepository.save(updatedEntity);

 // Then
 Assertions.assertEquals(updatedEntity, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 Long id = 1L;
 String userTaskId = "new_user_task_id";

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
 int expectedRowsAffected = 1;

 // When
 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

 // Then
 Assertions.assertEquals(expectedRowsAffected, rowsAffected);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.ACTIVITY;
 String activityId = "activity_id";
 UserTaskInstanceEntity expectedResult = new UserTaskInstanceEntity();
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
 .thenReturn(Optional.of(expectedResult));

 // When & Then
 Assertions.assertEquals(expectedResult, userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId).get());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.GATEWAY;
 String name = "gateway_name";
 UserTaskInstanceEntity expectedResult = new UserTaskInstanceEntity();
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
 .thenReturn(Optional.of(expectedResult));

 // When & Then
 Assertions.assertEquals(expectedResult, userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId).get());
 }

 @Test
 public void testFindConfirmById() {
 // Given
 Long id = 1L;
 boolean expectedResult = true;
 Mockito.when(userTaskInstanceRepository.findConfirmById(id))
 .thenReturn(expectedResult);

 // When & Then
 Assertions.assertEquals(expectedResult, userTaskInstanceRepository.findConfirmById(id));
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 Collection<Long> flowInstanceIds = List.of(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 List<UserTaskInstanceEntity> expectedResult = List.of(userTaskInstanceEntity, userTaskInstanceEntity);
 Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status))
 .thenReturn(expectedResult);

 // When & Then
 Assertions.assertEquals(expectedResult, userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status));
 }
}