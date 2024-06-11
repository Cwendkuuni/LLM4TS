import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

class ApprovalPermissionServiceTest {

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
 @Mock
 private ResourceRoleService resourceRoleService;
 @Mock
 private AuthenticationFacade authenticationFacade;

 @Autowired
 @InjectMocks
 private ApprovalPermissionService approvalPermissionService;

 @BeforeEach
 void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 void testGetCandidates_EnabledTrue_ReturnsUsersAndRoles() {
 // Given
 Long approvalInstanceId = 1L;
 Set<User> expectedUsers = Set.of(new User(1L, "user1"), new User(2L, "user2"));
 Set<Role> expectedRoles = Set.of(new Role(1L, "role1"), new Role(2L, "role2"));
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(Arrays.asList(
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 2L),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 3L),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 4L),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null)
 ));
 when(userRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true)).thenReturn(List.of(new User(1L, "user1")));
 when(userRepository.findByRoleIdsAndEnabled(Collections.singletonList(2L), true)).thenReturn(List.of(new User(2L, "user2")));
 when(roleRepository.findByRoleIds(Arrays.asList(1L, 2L))).thenReturn(List.of(new Role(1L, "role1"), new Role(2L, "role2")));

 // When
 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);

 // Then
 Assertions.assertEquals(expectedUsers, actualUsers);
 }

 @Test
 void testGetCandidates_EnabledFalse_ReturnsRolesOnly() {
 // Given
 Long approvalInstanceId = 1L;
 Set<Role> expectedRoles = Set.of(new Role(1L, "role1"), new Role(2L, "role2"));
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(Arrays.asList(
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 3L),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 4L)
 ));
 when(roleRepository.findByRoleIds(Arrays.asList(3L, 4L))).thenReturn(List.of(new Role(3L, "role3"), new Role(4L, "role4")));

 // When
 Set<Role> actualRoles = approvalPermissionService.getCandidates(approvalInstanceId, false);

 // Then
 Assertions.assertEquals(expectedRoles, actualRoles);
 }

 @Test
 void testGetCandidates_EnabledNull_ReturnsAllUsersAndRoles() {
 // Given
 Long approvalInstanceId = 1L;
 Set<User> expectedUsers = Set.of(new User(1L, "user1"), new User(2L, "user2"));
 Set<Role> expectedRoles = Set.of(new Role(1L, "role1"), new Role(2L, "role2"));
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(Arrays.asList(
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 2L),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 3L),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, 4L),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null),
 new UserTaskInstanceCandidateEntity(approvalInstanceId, null)
 ));
 when(userRepository.findByRoleIdsAndEnabled(Collections.emptySet(), null)).thenReturn(List.ofAll(
 List.of(new User(1L, "user1")),
 List.of(new User(2L, "user2"))
 ));
 when(roleRepository.findByRoleIds(Collections.emptySet()))
 .thenReturn(List.ofAll(
 List.of(new Role(1L, "role1")),
 List.of(new Role(2L, "role2"))
 ));

 // When
 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, null);
 Set<Role> actualRoles = approvalPermissionService.getCandidates(approvalInstanceId, null);

 // Then
 Assertions.assertEquals(expectedUsers, actualUsers);
 Assertions.assertEquals(expectedRoles, actualRoles);
 }

 @Test
 void testGetCandidates_EmptyApprovalInstanceIds_ReturnsEmptySet() {
 // Given
 Set<User> expectedUsers = Collections.emptySet();
 Set<Role> expectedRoles = Collections.emptySet();
 when(userTaskCandidateRepository.findByApprovalInstanceIds(Collections.emptySet()))
 .thenReturn(Collections.emptyList());
 when(userRepository.findByRoleIdsAndEnabled(Collections.emptySet(), true))
 .thenReturn(Collections.emptyList());
 when(roleRepository.findByRoleIds(Collections.emptySet()))
 .thenReturn(Collections.emptyList());

 // When
 Set<User> actualUsers = approvalPermissionService.getCandidates(Collections.emptySet(), true);
 Set<Role> actualRoles = approvalPermissionService.getCandidates(Collections.emptySet(), true);

 // Then
 Assertions.assertEquals(expectedUsers, actualUsers);
 Assertions.assertEquals(expectedRoles, actualRoles);
 }

 // ... Add more test cases for other methods in the class

}