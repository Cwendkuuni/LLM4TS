import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Before
 public void setup() {
 userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testFindByStatus() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setStatus(FlowNodeStatus.APPROVED);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);
 UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
 userTaskInstanceEntity2.setStatus(FlowNodeStatus.WAITING);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity2);

 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

 // Then
 assertThat(actualUserTaskInstanceEntities).isEqualTo(expectedUserTaskInstanceEntities);
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);

 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setUserId(userId);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);
 UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
 userTaskInstanceEntity2.setRoleId(1L);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity2);

 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

 // Then
 assertThat(actualUserTaskInstanceEntities).isEqualTo(expectedUserTaskInstanceEntities);
 }

 @Test
 public void testFindByResourceRoleIdentifier() {
 // Given
 String resourceRoleIdentifier = "role1";
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 resourceRoleIdentifiers.add("role2");

 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setResourceRoleIdentifier(resourceRoleIdentifier);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);

 when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

 // Then
 assertThat(actualUserTaskInstanceEntities).isEqualTo(expectedUserTaskInstanceEntities);
 }

 @Test
 public void testFindByRoleId() {
 // Given
 Long roleId = 1L;

 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setRoleId(roleId);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);

 when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);

 // Then
 assertThat(actualUserTaskInstanceEntities).isEqualTo(expectedUserTaskInstanceEntities);
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 Long userId = 1L;

 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setUserId(userId);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);

 when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);

 // Then
 assertThat(actualUserTaskInstanceEntities).isEqualTo(expectedUserTaskInstanceEntities);
 }

 @Test
 public void testUpdate() {
 // Given
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(entity));

 // When
 int actual = userTaskInstanceRepository.update(entity);

 // Then
 assertThat(actual).isOne();
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 Long id = 1L;
 String userTaskId = "userTask1";

 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(new UserTaskInstanceEntity()));

 // When
 int actual = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

 // Then
 assertThat(actual).isOne();
 }

 @Test
 public void testUpdateStatusById() {
 // Given
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(new UserTaskInstanceEntity()));

 // When
 int actual = userTaskInstanceRepository.updateStatusById(id, status);

 // Then
 assertThat(actual).isOne();
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;

 // When
 int actual = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

 // Then
 assertThat(actual).isGreaterThan(0);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.USER_TASK;
 String activityId = "activity1";

 UserTaskInstanceEntity expectedUserTaskInstanceEntity = new UserTaskInstanceEntity();

 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId)).thenReturn(Optional.of(expectedUserTaskInstanceEntity));

 // When
 Optional<UserTaskInstanceEntity> actualOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

 // Then
 assertThat(actualOptional.isPresent()).isTrue();
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 Long flowInstanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.USER_TASK;
 String name = "name1";

 UserTaskInstanceEntity expectedUserTaskInstanceEntity = new UserTaskInstanceEntity();

 when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId)).thenReturn(Optional.of(expectedUserTaskInstanceEntity));

 // When
 Optional<UserTaskInstanceEntity> actualOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

 // Then
 assertThat(actualOptional.isPresent()).isTrue();
 }

 @Test
 public void testFindConfirmById() {
 // Given
 Long id = 1L;

 Boolean expectedConfirm = true;

 when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(expectedConfirm);

 // When
 Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);

 // Then
 assertThat(actualConfirm).isEqualTo(expectedConfirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 Collection<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 flowInstanceIds.add(2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setId(1L);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);
 UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
 userTaskInstanceEntity2.setId(2L);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity2);

 when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

 // Then
 assertThat(actualUserTaskInstanceEntities).isEqualTo(expectedUserTaskInstanceEntities);
 }
}