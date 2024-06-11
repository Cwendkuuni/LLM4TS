import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @BeforeEach
 public void setup() {
 userTaskInstanceRepository = new UserTaskInstanceRepositoryImpl(); // Assuming UserTaskInstanceRepositoryImpl is the implementation class
 }

 @Test
 public void testFindByStatus() {
 // Test with valid status
 List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setStatus(FlowNodeStatus.APPROVED);
 expectedResult.add(userTaskInstanceEntity1);

 List<UserTaskInstanceEntity> actualResult = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 Assertions.assertEquals(expectedResult, actualResult);

 // Test with null status
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(null);
 Assertions.assertTrue(result.isEmpty());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 // Test with valid userId and roleIds
 List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setUserId(1L);
 expectedResult.add(userTaskInstanceEntity1);

 UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
 userTaskInstanceEntity2.setRoleId(1L);
 expectedResult.add(userTaskInstanceEntity2);

 List<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);

 List<UserTaskInstanceEntity> actualResult = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
 Assertions.assertEquals(expectedResult, actualResult);

 // Test with null userId and valid roleIds
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, roleIds);
 Assertions.assertEquals(expectedResult, result);

 // Test with valid userId and null roleIds
 List<UserTaskInstanceEntity> result1 = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
 Assertions.assertTrue(result1.isEmpty());

 // Test with null userId and null roleIds
 List<UserTaskInstanceEntity> result2 = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, null);
 Assertions.assertTrue(result2.isEmpty());
 }

 @Test
 public void testFindByResourceRoleIdentifier() {
 // Test with valid resourceRoleIdentifier
 List<UserTaskInstanceEntity> expectedResult = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setResourceRoleIdentifier("TestRoleIdentifier");
 expectedResult.add(userTaskInstanceEntity1);

 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("TestRoleIdentifier");

 List<UserTaskInstanceEntity> actualResult = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 Assertions.assertEquals(expectedResult, actualResult);

 // Test with null resourceRoleIdentifier
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(null);
 Assertions.assertTrue(result.isEmpty());
 }

 // Add more test cases for the other methods in UserTaskInstanceRepository
 // ...
}