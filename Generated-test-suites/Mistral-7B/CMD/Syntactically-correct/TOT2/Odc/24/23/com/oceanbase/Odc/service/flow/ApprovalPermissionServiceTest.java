import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.annotations.MockBean;

import java.util.*;
import java.util.stream.Collectors;

@Component
@SkipAuthorize("odc internal usage")
public class ApprovalPermissionServiceTest {

 @Autowired
 private ApprovalPermissionService approvalPermissionService;
 @MockBean
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
 @MockBean
 private UserTaskInstanceRepository userTaskInstanceRepository;
 @MockBean
 private RoleRepository roleRepository;
 @MockBean
 private UserRepository userRepository;
 @MockBean
 private UserRoleRepository userRoleRepository;
 @MockBean
 private UserResourceRoleRepository userResourceRoleRepository;

 @BeforeEach
 void setup() {
 // Initialize mocks
 when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
 .thenReturn(Collections.emptyList());
 when(userTaskInstanceRepository.findByFlowInstanceIdAndStatus(anyLong(), eq(FlowNodeStatus.EXECUTING)))
 .thenReturn(Collections.emptyList());
 when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
 .thenReturn(Collections.emptyList());
 when(userRepository.findByUserIds(anySet()))
 .thenReturn(Collections.emptyList());
 when(userRoleRepository.findByRoleIdIn(anySet()))
 .thenReturn(Collections.emptyList());
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(anySet(), anySet()))
 .thenReturn(Collections.emptyList());
 }

 @Test
 void testGetCandidates_whenEnabledIsTrue_returnsUsersAndRoles() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity().setRoleId(1L),
 new UserTaskInstanceCandidateEntity().setUserId(2L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Set<User> expectedUsers = Set.of(new User(1L, "User1"), new User(2L, "User2"), new User(3L, "User3"));
 Set<Role> expectedRoles = Set.of(new Role(1L, "Role1"), new Role(2L, "Role2"));
 // When
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 // Then
 assertEquals(expectedUsers, candidates);
 // Verify
 verify(userRepository, times(1)).findByUserIds(expectedUsers.stream().map(User::getId).collect(Collectors.toSet()));
 }

 @Test
 void testGetCandidates_whenEnabledIsFalse_returnsRoles() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity().setRoleId(1L),
 new UserTaskInstanceCandidateEntity().setRoleId(2L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Set<Role> expectedRoles = Set.of(new Role(1L, "Role1"), new Role(2L, "Role2"));
 // When
 Set<Role> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
 // Then
 assertEquals(expectedRoles, candidates);
 }

 @Test
 void testGetCandidates_whenEnabledIsNull_returnsUsersAndRoles() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity().setRoleId(1L),
 new UserTaskInstanceCandidateEntity().setUserId(2L),
 new UserTaskInstanceCandidateEntity().setUserId(3L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Set<User> expectedUsers = Set.of(new User(2L, "User2"), new User(3L, "User3"));
 Set<Role> expectedRoles = Set.of(new Role(1L, "Role1"));
 // When
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
 // Then
 assertEquals(expectedUsers, candidates);
 verify(userRepository, times(1)).findByUserIds(expectedUsers.stream().map(User::getId).collect(Collectors.toSet()));
 }

 // Other test cases for getInstanceId2CandidateRoleIds(), getInstanceId2CandidateResourceRoleIdentifierIds(),
 // getInstanceId2CandidateUserIds(), getCandidateRoles(), isApprovable(),
 // getApprovableApprovalInstances(), getApprovableApprovalInstances(long userId), setCandidateResourceRole(),
 // listApprovableExternalInstances(), and getCandidatesByFlowInstanceIds() can be added here.

 @AfterEach
 void cleanup() {
 // Reset mocks
 reset(userTaskCandidateRepository, userTaskInstanceRepository, roleRepository, userRepository, userRoleRepository,
 userResourceRoleRepository);
 }
}