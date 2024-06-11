import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

 @Mock
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @InjectMocks
 private UserTaskInstanceRepository testSubject;

 @Before
 public void setup() {
 // Initialize the mock repository
 UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
 entity1.setId(1L);
 entity1.setFlowInstanceId(1L);
 entity1.setUserTaskId("userTaskId1");
 entity1.setStatus(FlowNodeStatus.APPROVED);

 UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
 entity2.setId(2L);
 entity2.setFlowInstanceId(1L);
 entity2.setUserTaskId("userTaskId2");
 entity2.setStatus(FlowNodeStatus.WAITING);

 Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(List.of(entity1, entity2));
 Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(entity1));
 Mockito.when(userTaskInstanceRepository.findById(2L)).thenReturn(Optional.of(entity2));
 }

 @Test
 public void testFindByStatus() {
 // Given
 FlowNodeStatus status = FlowNodeStatus.WAITING;

 // When
 List<UserTaskInstanceEntity> result = testSubject.findByStatus(status);

 // Then
 Assert.assertEquals(1, result.size());
 Assert.assertEquals(entity2.getId(), result.get(0).getId());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);

 // When
 List<UserTaskInstanceEntity> result = testSubject.findByCandidateUserIdOrRoleIds(userId, roleIds);

 // Then
 Assert.assertEquals(1, result.size());
 Assert.assertEquals(entity1.getId(), result.get(0).getId());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 // Given
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = List.of("roleIdentifier1", "roleIdentifier2");

 // When
 List<UserTaskInstanceEntity> result = testSubject.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

 // Then
 Assert.assertEquals(1, result.size());
 Assert.assertEquals(entity1.getId(), result.get(0).getId());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 // Given
 Collection<String> resourceRoleIdentifier = List.of("roleIdentifier1");

 // When
 List<UserTaskInstanceEntity> result = testSubject.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

 // Then
 Assert.assertEquals(1, result.size());
 Assert.assertEquals(entity1.getId(), result.get(0).getId());
 }

 @Test
 public void testFindByRoleId() {
 // Given
 Long roleId = 1L;

 // When
 List<UserTaskInstanceEntity> result = testSubject.findByRoleId(roleId);

 // Then
 Assert.assertEquals(1, result.size());
 Assert.assertEquals(entity1.getId(), result.get(0).getId());
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 Long userId = 1L;

 // When
 List<UserTaskInstanceEntity> result = testSubject.findByCandidateUserId(userId);

 // Then
 Assert.assertEquals(1, result.size());
 Assert.assertEquals(entity1.getId(), result.get(0).getId());
 }

 @Test
 public void testUpdate() {
 // Given
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setFlowInstanceId(1L);
 entity.setUserTaskId("userTaskId1");
 entity.setStatus(FlowNodeStatus.WAITING);

 String newUserTaskId = "newUserTaskId1";
 FlowNodeStatus newStatus = FlowNodeStatus.APPROVED;
 Long operatorId = 1L;
 String comment = "new comment";
 Long expireIntervalSeconds = 10L;
 String externalFlowInstanceId = "newExternalFlowInstanceId";

 // When
 int result = testSubject.update(entity);

 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).update(entity);
 Assert.assertEquals(1, result);

 // Given
 UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
 updatedEntity.setId(1L);
 updatedEntity.setUserTaskId(newUserTaskId);
 updatedEntity.setStatus(newStatus);
 updatedEntity.setOperatorId(operatorId);
 updatedEntity.setComment(comment);
 updatedEntity.setExpireIntervalSeconds(expireIntervalSeconds);
 updatedEntity.setExternalFlowInstanceId(externalFlowInstanceId);

 // When
 int result2 = testSubject.update(updatedEntity);

 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(2)).update(entity);
 Assert.assertEquals(1, result2);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 Long id = 1L;
 String userTaskId = "userTaskId1";

 // When
 int result = testSubject.updateUserTaskIdById(id, userTaskId);

 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).updateUserTaskIdById(id, userTaskId);
 Assert.assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 // Given
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 // When
 int result = testSubject.updateStatusById(id, status);

 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).updateStatusById(id, status);
 Assert.assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;

 // When
 int result = testSubject.deleteByFlowInstanceId(instanceId);

 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).deleteByFlowInstanceId(instanceId);
 Assert.assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activityId1";
 Long flowInstanceId = 1L;

 // When
 Optional<UserTaskInstanceEntity> result = testSubject.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assert.assertTrue(result.isPresent());
 Assert.assertEquals(entity1.getId(), result.get().getId());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name1";
 Long flowInstanceId = 1L;

 // When
 Optional<UserTaskInstanceEntity> result = testSubject.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assert.assertTrue(result.isPresent());
 Assert.assertEquals(entity1.getId(), result.get().getId());
 }

 @Test
 public void testFindConfirmById() {
 // Given
 Long id = 1L;

 // When
 Boolean result = testSubject.findConfirmById(id);

 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findConfirmById(id);
 Assert.assertEquals(true, result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 Collection<Long> flowInstanceIds = List.of(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 // When
 List<UserTaskInstanceEntity> result = testSubject.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 Assert.assertEquals(2, result.size());
 }
}