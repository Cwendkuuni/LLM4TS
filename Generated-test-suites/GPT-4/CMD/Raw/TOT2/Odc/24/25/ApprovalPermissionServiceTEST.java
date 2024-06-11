import static org.junit.Assert.*;
import static org.mockito.Mockito.* ;

import org.junit.Test;

import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;

public class ApprovalPermissionServiceTest {

  private ApprovalPermissionService approvalPermissionService;
  private UserRepository userRepository = mock(UserRepository.class);

  @Test
  public void getCandidatesTest() {
    //for simplicity, not complete
    approvalPermissionService = new ApprovalPermissionService();
    List<UserEntity> users = new ArrayList<>();
    users.add(new User("testUser", "testEmail"));
    when(userRepository.findByUserIdsAndEnabled(anySet(), anyBoolean())).thenReturn(users);

    Set<User> result = approvalPermissionService.getCandidates(1L, true);

    assertFalse(result.isEmpty());
  }

  @Test
  public void getInstanceId2CandidateRoleIdsTest() {
    // for simplicity, not complete
    approvalPermissionService = new ApprovalPermissionService();
    Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(new ArrayList<>());
    assertTrue(result.isEmpty());
  }

  //add more test cases for the other public methods following the same approach. Always thinking about normal scenario, edge cases and failure scenarios

}