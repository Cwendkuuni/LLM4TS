import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

 // Then
 assertEquals(1, actualUserTaskInstanceEntities.size());
 assertNotNull(actualUserTaskInstanceEntities.get(0));
 assertEquals(FlowNodeStatus.APPROVED, actualUserTaskInstanceEntities.get(0).getStatus());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setUserId(userId);
 userTaskInstanceEntity1.setStatus(FlowNodeStatus.APPROVED);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);

 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

 // Then
 assertEquals(1, actualUserTaskInstanceEntities.size());
 assertNotNull(actualUserTaskInstanceEntities.get(0));
 assertEquals(userId, actualUserTaskInstanceEntities.get(0).getUserId());
 }

 @Test
 public void testFindByResourceRoleIdentifier() {
 // Given
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("resourceRoleIdentifier1");
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setResourceRoleIdentifier("resourceRoleIdentifier1");
 userTaskInstanceEntity1.setStatus(FlowNodeStatus.APPROVED);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);

 when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

 // Then
 assertEquals(1, actualUserTaskInstanceEntities.size());
 assertNotNull(actualUserTaskInstanceEntities.get(0));
 assertEquals("resourceRoleIdentifier1", actualUserTaskInstanceEntities.get(0).getResourceRoleIdentifier());
 }

 @Test
 public void testFindByRoleId() {
 // Given
 Long roleId = 1L;
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setRoleId(roleId);
 userTaskInstanceEntity1.setStatus(FlowNodeStatus.APPROVED);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);

 when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);

 // Then
 assertEquals(1, actualUserTaskInstanceEntities.size());
 assertNotNull(actualUserTaskInstanceEntities.get(0));
 assertEquals(roleId, actualUserTaskInstanceEntities.get(0).getRoleId());
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 Long userId = 1L;
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setUserId(userId);
 userTaskInstanceEntity1.setStatus(FlowNodeStatus.APPROVED);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);

 when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);

 // Then
 assertEquals(1, actualUserTaskInstanceEntities.size());
 assertNotNull(actualUserTaskInstanceEntities.get(0));
 assertEquals(userId, actualUserTaskInstanceEntities.get(0).getUserId());
 }

 @Test
 public void testUpdate() {
 // Given
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 Long id = 1L;
 String userTaskId = "userTaskId";
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 String operatorId = "operatorId";
 String comment = "comment";
 Long expireIntervalSeconds = 1L;
 String externalFlowInstanceId = "externalFlowInstanceId";

 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(entity));

 // When
 int actualResult = userTaskInstanceRepository.update(entity);

 // Then
 assertEquals(1, actualResult);
 assertEquals(userTaskId, entity.getUserTaskId());
 assertEquals(status, entity.getStatus());
 assertEquals(operatorId, entity.getOperatorId());
 assertEquals(comment, entity.getComment());
 assertEquals(expireIntervalSeconds, entity.getExpireIntervalSeconds());
 assertEquals(externalFlowInstanceId, entity.getExternalFlowInstanceId());
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 Long id = 1L;
 String userTaskId = "userTaskId";

 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(new UserTaskInstanceEntity()));

 // When
 int actualResult = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

 // Then
 assertEquals(1, actualResult);
 assertEquals(userTaskId, userTaskInstanceRepository.findById(id).get().getUserTaskId());
 }

 @Test
 public void testUpdateStatusById() {
 // Given
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.of(new UserTaskInstanceEntity()));

 // When
 int actualResult = userTaskInstanceRepository.updateStatusById(id, status);

 // Then
 assertEquals(1, actualResult);
 assertEquals(status, userTaskInstanceRepository.findById(id).get().getStatus());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;
 int expectedResult = 1;

 when(userTaskInstanceRepository.findAll()).thenReturn(new ArrayList<>());

 // When
 int actualResult = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

 // Then
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity expectedUserTaskInstanceEntity = new UserTaskInstanceEntity();

 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId)).thenReturn(Optional.of(expectedUserTaskInstanceEntity));

 // When
 Optional<UserTaskInstanceEntity> actualUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

 // Then
 assertNotNull(actualUserTaskInstanceEntity);
 assertNotNull(actualUserTaskInstanceEntity.get());
 assertEquals(expectedUserTaskInstanceEntity, actualUserTaskInstanceEntity.get());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity expectedUserTaskInstanceEntity = new UserTaskInstanceEntity();

 when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId)).thenReturn(Optional.of(expectedUserTaskInstanceEntity));

 // When
 Optional<UserTaskInstanceEntity> actualUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

 // Then
 assertNotNull(actualUserTaskInstanceEntity);
 assertNotNull(actualUserTaskInstanceEntity.get());
 assertEquals(expectedUserTaskInstanceEntity, actualUserTaskInstanceEntity.get());
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
 assertEquals(expectedConfirm, actualConfirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 Collection<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setFlowInstanceId(1L);
 userTaskInstanceEntity1.setStatus(status);
 expectedUserTaskInstanceEntities.add(userTaskInstanceEntity1);

 when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status)).thenReturn(expectedUserTaskInstanceEntities);

 // When
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

 // Then
 assertEquals(1, actualUserTaskInstanceEntities.size());
 assertNotNull(actualUserTaskInstanceEntities.get(0));
 assertEquals(status, actualUserTaskInstanceEntities.get(0).getStatus());
 }
}