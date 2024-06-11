import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Autowired
 public void setUserTaskInstanceRepository(UserTaskInstanceRepository userTaskInstanceRepository) {
 this.userTaskInstanceRepository = userTaskInstanceRepository;
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByStatus(FlowNodeStatus)}
 */
 @Test
 public void testFindByStatus() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Mockito.mock(List.class);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 Mockito.when(userTaskInstanceRepository.findByStatus(status)).thenReturn(expectedUserTaskInstanceEntities);

 // When & Then
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByStatus(status);
 Assert.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByCandidateUserIdOrRoleIds(Long, Collection)}
 */
 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Mockito.mock(List.class);
 Long userId = 1L;
 Collection<Long> roleIds = Mockito.mock(Collection.class);
 Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(expectedUserTaskInstanceEntities);

 // When & Then
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 Assert.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByResourceRoleIdentifierIn(Collection)}
 */
 @Test
 public void testFindByResourceRoleIdentifierIn() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Mockito.mock(List.class);
 Collection<String> resourceRoleIdentifiers = Mockito.mock(Collection.class);
 Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers)).thenReturn(expectedUserTaskInstanceEntities);

 // When & Then
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 Assert.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByRoleId(Long)}
 */
 @Test
 public void testFindByRoleId() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Mockito.mock(List.class);
 Long roleId = 1L;
 Mockito.when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(expectedUserTaskInstanceEntities);

 // When & Then
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
 Assert.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByCandidateUserId(Long)}
 */
 @Test
 public void testFindByCandidateUserId() {
 // Given
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Mockito.mock(List.class);
 Long userId = 1L;
 Mockito.when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(expectedUserTaskInstanceEntities);

 // When & Then
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
 Assert.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#update(UserTaskInstanceEntity)}
 */
 @Test
 public void testUpdate() {
 // Given
 UserTaskInstanceEntity entity = Mockito.mock(UserTaskInstanceEntity.class);
 UserTaskInstanceEntity updateEntity = Mockito.mock(UserTaskInstanceEntity.class);
 mockUpdateQueries(entity, updateEntity);

 // When
 int result = userTaskInstanceRepository.update(entity);

 // Then
 Assert.assertEquals(1, result);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#updateUserTaskIdById(Long, String)}
 */
 @Test
 public void testUpdateUserTaskIdById() {
 // Given
 Long id = 1L;
 String userTaskId = "userTaskId";
 int result = 1;
 mockUpdateQueries(null, null);
 Mockito.doReturn(result).when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

 // When
 int actualResult = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

 // Then
 Assert.assertEquals(result, actualResult);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#updateStatusById(Long, FlowNodeStatus)}
 */
 @Test
 public void testUpdateStatusById() {
 // Given
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 int result = 1;
 mockUpdateQueries(null, null);
 Mockito.doReturn(result).when(userTaskInstanceRepository).updateStatusById(id, status);

 // When
 int actualResult = userTaskInstanceRepository.updateStatusById(id, status);

 // Then
 Assert.assertEquals(result, actualResult);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#deleteByFlowInstanceId(Long)}
 */
 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;
 int result = 1;
 mockUpdateQueries(null, null);
 Mockito.doReturn(result).when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

 // When
 int actualResult = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

 // Then
 Assert.assertEquals(result, actualResult);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndActivityId(FlowNodeType, String, Long)}
 */
 @Test
 public void testFindByInstanceTypeAndActivityId() {
 // Given
 UserTaskInstanceEntity expectedUserTaskInstanceEntity = Mockito.mock(UserTaskInstanceEntity.class);
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
 .thenReturn(Optional.of(expectedUserTaskInstanceEntity));

 // When & Then
 Optional<UserTaskInstanceEntity> actualUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 Assert.assertEquals(expectedUserTaskInstanceEntity, actualUserTaskInstanceEntity.get());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findByInstanceTypeAndName(FlowNodeType, String, Long)}
 */
 @Test
 public void testFindByInstanceTypeAndName() {
 // Given
 UserTaskInstanceEntity expectedUserTaskInstanceEntity = Mockito.mock(UserTaskInstanceEntity.class);
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name";
 Long flowInstanceId = 1L;
 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
 .thenReturn(Optional.of(expectedUserTaskInstanceEntity));

 // When & Then
 Optional<UserTaskInstanceEntity> actualUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 Assert.assertEquals(expectedUserTaskInstanceEntity, actualUserTaskInstanceEntity.get());
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findConfirmById(Long)}
 */
 @Test
 public void testFindConfirmById() {
 // Given
 Long id = 1L;
 Boolean confirm = true;
 Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

 // When & Then
 Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);
 Assert.assertEquals(confirm, actualConfirm);
 }

 /**
 * Test method for {@link UserTaskInstanceRepository#findApprovalInstanceIdByFlowInstanceIdAndStatus(Collection, FlowNodeStatus)}
 */
 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 // Given
 Collection<Long> flowInstanceIds = Mockito.mock(Collection.class);
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Mockito.mock(List.class);
 Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status)).thenReturn(expectedUserTaskInstanceEntities);

 // When & Then
 List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 Assert.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
 }

 /**
 * Helper method to mock update queries
 * @param entity1 entity 1
 * @param entity2 entity 2
 */
 private void mockUpdateQueries(UserTaskInstanceEntity entity1, UserTaskInstanceEntity entity2) {
 Mockito.doNothing().when(userTaskInstanceRepository).update(entity1);
 Mockito.doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(Mockito.anyLong(), Mockito.anyString());
 Mockito.doNothing().when(userTaskInstanceRepository).updateStatusById(Mockito.anyLong(), Mockito.any(FlowNodeStatus.class));
 }

}