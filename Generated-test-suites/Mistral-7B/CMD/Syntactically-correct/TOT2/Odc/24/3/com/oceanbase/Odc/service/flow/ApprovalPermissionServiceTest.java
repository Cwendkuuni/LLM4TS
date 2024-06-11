import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApprovalPermissionServiceTest {

 @InjectMocks
 private ApprovalPermissionService approvalPermissionService;
 @Mock
 private UserService userService;
 @Mock
 private ResourceRoleService resourceRoleService;
 @Mock
 private AuthenticationFacade authenticationFacade;
 @Mock
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
 @Mock
 private UserTaskInstanceRepository userTaskInstanceRepository;
 @Mock
 private UserRepository userRepository;
 @Mock
 private RoleRepository roleRepository;
 @Mock
 private UserRoleRepository userRoleRepository;
 @Mock
 private UserResourceRoleRepository userResourceRoleRepository;

 @BeforeAll
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetCandidates() {
 // Given
 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
 candidateEntity.setApprovalInstanceId(approvalInstanceId);

 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(candidateEntity);
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);

 Role role = new Role();
 role.setId(1L);
 role.setName("role1");

 User user = new User();
 user.setId(1L);
 user.setName("user1");
 user.setEnabled(true);

 UserEntity userEntity = new UserEntity();
 userEntity.setUser(user);

 Set<UserEntity> candidates = new HashSet<>();
 candidates.add(userEntity);

 when(userRepository.findByRoleIdsAndEnabled(Collections.singletonList(role.getId()), enabled))
 .thenReturn(Collections.singletonList(userEntity));

 when(userService.getCurrentUserRoleIds())
 .thenReturn(Collections.singletonList(role.getId()));

 // When
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

 // Then
 assertEquals(1, result.size());
 assertEquals(user, result.iterator().next());
 verify(userRepository, times(1)).findByRoleIdsAndEnabled(eq(Collections.singletonList(role.getId())), eq(enabled));
 }

 @Test
 public void testGetCandidates_empty() {
 // Given
 Long approvalInstanceId = 1L;
 Boolean enabled = null;

 List<UserTaskInstanceCandidateEntity> entities = Collections.emptyList();
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);

 // When
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

 // Then
 assertTrue(result.isEmpty());
 verify(userRepository, never()).findByRoleIdsAndEnabled(any(), any());
 }

 // Add test cases for other methods:
 // getInstanceId2CandidateRoleIds, getInstanceId2CandidateResourceRoleIdentifierIds,
 // getCandidateUserIds, getCandidateRoles, isApprovable, getApprovableApprovalInstances,
 // setCandidateResourceRole, listApprovableExternalInstances, getCandidatesByFlowInstanceIds.
}