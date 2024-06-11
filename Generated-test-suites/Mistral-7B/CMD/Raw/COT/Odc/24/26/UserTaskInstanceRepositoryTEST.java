import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

@SpringBootTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Autowired
 public void setUserTaskInstanceRepository(UserTaskInstanceRepository userTaskInstanceRepository) {
 this.userTaskInstanceRepository = userTaskInstanceRepository;
 }

 @Test
 public void testFindByStatus() {
 // Given
 FlowNodeStatus status = FlowNodeStatus.APPROVED;

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(status);

 // Then
 Assertions.assertNotNull(result);
 // Add assertions based on the expected behavior of the method
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Given
 Long userId = 1L;
 Collection<Long> roleIds = List.of(1L, 2L);

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

 // Then
 Assertions.assertNotNull(result);
 // Add assertions based on the expected behavior of the method
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 // Given
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = List.of("role1", "role2");

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

 // Then
 Assertions.assertNotNull(result);
 // Add assertions based on the expected behavior of the method
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 // Given
 Collection<String> resourceRoleIdentifier = List.of("role1", "role2");

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

 // Then
 Assertions.assertNotNull(result);
 // Add assertions based on the expected behavior of the method
 }

 @Test
 public void testFindByRoleId() {
 // Given
 Long roleId = 1L;

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);

 // Then
 Assertions.assertNotNull(result);
 // Add assertions based on the expected behavior of the method
 }

 @Test
 public void testFindByCandidateUserId() {
 // Given
 Long userId = 1L;

 // When
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);

 // Then
 Assertions.assertNotNull(result);
 // Add assertions based on the expected behavior of the method
 }

 @ParameterizedTest
 @ValueSource(longs = {0L, Long.MAX_VALUE})
 public void testUpdate(Long id) {
 // Given
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(id);
 // Add other fields and set values based on the expected behavior of the method

 // When
 int result = userTaskInstanceRepository.update(entity);

 // Then
 Assertions.assertEquals(1, result);
 // Add assertions based on the expected behavior of the method
 }

 @ParameterizedTest
 @ValueSource(strings = {"invalid_user_task_id"})
 public void testUpdateWithInvalidUserTaskId(@Param("id") Long id, @Param("userTaskId") String userTaskId) {
 // Given
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(id);
 // Add other fields and set values based on the expected behavior of the method

 // When & Then
 Assertions.assertThrows(RuntimeException.class, () -> userTaskInstanceRepository.update(entity));
 }

 // Add test cases for other methods with appropriate input data and expected outcomes

}