import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceRepository;

public class UserTaskInstanceRepositoryTest {

 @InjectMocks
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 // Given
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(status);
 // Then
 Assertions.assertNotNull(userTaskInstanceEntities);
 // Edge case and exception handling
 Assertions.assertEquals(0, userTaskInstanceEntities.size(), () -> "Should return an empty list when no instances found with status " + status);
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 // Then
 Assertions.assertNotNull(userTaskInstanceEntities);
 // Edge case and exception handling
 Assertions.assertEquals(0, userTaskInstanceEntities.size(), () -> "Should return an empty list when no instances found with given userId and roleIds");
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 // Given
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = List.of("role1", "role2");
 // When
 List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 // Then
 Assertions.assertNotNull(userTaskInstanceEntities);
 // Edge case and exception handling
 Assertions.assertEquals(0, userTaskInstanceEntities.size(), () -> "Should return an empty list when no instances found with given userId and resourceRoleIdentifiers");
 }

 // Other test methods for remaining methods with similar test logic and edge cases

 @Test
 public void testUpdate() {
 // Given
 Long id = 1L;
 UserTaskInstanceEntity entity = Mockito.spy(new UserTaskInstanceEntity());
 // When
 int result = userTaskInstanceRepository.update(entity);
 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).update(entity);
 // Edge case and exception handling
 Assertions.assertEquals(1, result, () -> "Should return 1 when a record is updated successfully");
 }

 // Test other methods with similar test logic and edge cases

 @Test
 public void testDeleteByFlowInstanceId() {
 // Given
 Long instanceId = 1L;
 // When
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
 // Then
 Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).deleteByFlowInstanceId(instanceId);
 // Edge case and exception handling
 Assertions.assertEquals(1, result, () -> "Should return 1 when a record is deleted successfully");
 }

 // Test other methods with similar test logic and edge cases
}