import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class ApprovalPermissionServiceTest {

 @MockBean
 private UserService userService;
 @MockBean
 private ResourceRoleService resourceRoleService;
 @MockBean
 private AuthenticationFacade authenticationFacade;
 @MockBean
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
 @MockBean
 private UserTaskInstanceRepository userTaskInstanceRepository;
 @MockBean
 private UserRepository userRepository;
 @MockBean
 private RoleRepository roleRepository;
 @MockBean
 private UserRoleRepository userRoleRepository;
 @MockBean
 private UserResourceRoleRepository userResourceRoleRepository;

 @Autowired
 @InjectMocks
 private ApprovalPermissionService approvalPermissionService;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetCandidates() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity());
 Set<User> expectedUsers = new HashSet<>(Arrays.asList(new User(), new User()));
 Map<String, Set<Long>> candidates = new HashMap<>();
 candidates.put("ROLE", new HashSet<>(Arrays.asList(1L, 2L)));
 candidates.put("USER", new HashSet<>(Arrays.asList(3L, 4L)));
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
 when(roleRepository.findByRoleIdsAndEnabled((Set<Long>) anyObject(), anyBoolean())).thenReturn(Collections.singletonList(new Role()));
 when(userRepository.findByRoleIdsAndEnabled((Set<Long>) anyObject(), anyBoolean())).thenReturn(Arrays.asList(new User(), new User()));
 when(userRepository.findByUserIdsAndEnabled((Set<Long>) anyObject(), anyBoolean())).thenReturn(Collections.singletonList(new User()));

 // When
 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);

 // Then
 assertEquals(expectedUsers, actualUsers);
 }

 @Test
 public void testGetCandidatesWithEmptyResult() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Collections.emptyList();

 // When
 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);

 // Then
 assertEquals(Collections.emptySet(), actualUsers);
 }

 @Test
 public void testGetCandidatesWithOnlyRoles() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity());

 // When
 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, null);

 // Then
 assertTrue(actualUsers.isEmpty());
 List<Role> roles = Arrays.asList(new Role(), new Role());
 List<Role> actualRoles = approvalPermissionService.getCandidates(approvalInstanceId, false).stream()
 .map(User::new).collect(Collectors.toList());
 assertEquals(roles, actualRoles);
 }

 @Test
 public void testGetCandidatesWithOnlyUsers() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity());
 Set<User> expectedUsers = new HashSet<>(Arrays.asList(new User(), new User()));
 when(userRepository.findByUserIdsAndEnabled((Set<Long>) anyObject(), anyBoolean())).thenReturn(expectedUsers);

 // When
 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);

 // Then
 assertEquals(expectedUsers, actualUsers);
 }

 @Test
 public void testGetCandidatesWithUsersAndRoles() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity());
 Set<User> expectedUsers = new HashSet<>(Arrays.asList(new User(), new User()));
 Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
 Set<Long> userIds = new HashSet<>(Arrays.asList(3L, 4L));
 Map<String, Set<Long>> candidates = new HashMap<>();
 candidates.put("ROLE", new HashSet<>(roleIds));
 candidates.put("USER", new HashSet<>(userIds));
 when(userRepository.findByUserIdsAndEnabled((Set<Long>) anyObject(), anyBoolean())).thenReturn(expectedUsers);
 when(roleRepository.findByRoleIdsAndEnabled((Set<Long>) anyObject(), anyBoolean())).thenReturn(Collections.emptyList());

 // When
 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);

 // Then
 assertEquals(expectedUsers, actualUsers);
 Set<Role> roles = Collections.emptySet();
 Set<User> actualRolesUsers = approvalPermissionService.getCandidates(approvalInstanceId, false).stream()
 .filter(user -> user instanceof Role).map(User::new).collect(Collectors.toSet());
 assertEquals(roles, actualRolesUsers);
 Set<User> actualUsersUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertEquals(expectedUsers, actualUsersUsers);
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 // Given
 Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity());
 Map<Long, Set<Long>> expectedMap = new HashMap<>();
 expectedMap.put(1L, new HashSet<>(Arrays.asList(1L, 2L)));
 expectedMap.put(2L, new HashSet<>(Arrays.asList(3L, 4L)));
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

 // When
 Map<Long, Set<Long>> actualMap = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

 // Then
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
 // Given
 Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity());
 Map<Long, Set<String>> expectedMap = new HashMap<>();
 expectedMap.put(1L, new HashSet<>(Arrays.asList("role1", "role2")));
 expectedMap.put(2L, new HashSet<>(Arrays.asList("role3", "role4")));
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

 // When
 Map<Long, Set<String>> actualMap = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

 // Then
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIdsWithEmptyResult() {
 // Given
 Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
 List<UserTaskInstanceCandidateEntity> entities = Collections.emptyList();

 // When
 Map<Long, Set<String>> actualMap = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

 // Then
 assertEquals(Collections.emptyMap(), actualMap);
 }

 @Test
 public void testGetCandidateUserIds() {
 // Given
 Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity());
 Set<Long> expectedUsersIds = new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L));
 Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
 Set<String> resourceRoleIdentifiers = new HashSet<>(Arrays.asList("role1", "role2"));
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);
 when(userRoleRepository.findByRoleIdIn(roleIds)).thenReturn(Collections.emptyList());
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(resourceRoleIdentifiers,
 new HashSet<>(Arrays.asList("resource1", "resource2")))).thenReturn(new ArrayList<>());

 // When
 Set<Long> actualUsersIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

 // Then
 assertEquals(expectedUsersIds, actualUsersIds);
 }

 @Test
 public void testGetCandidateRoles() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity());
 List<Role> expectedRoles = Arrays.asList(new Role(), new Role());
 Map<String, Set<Long>> candidates = new HashMap<>();
 candidates.put("ROLE", new HashSet<>(Arrays.asList(1L, 2L)));
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
 when(roleRepository.findByRoleIdsAndEnabled((Set<Long>) anyObject(), anyBoolean())).thenReturn(expectedRoles);

 // When
 List<Role> actualRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);

 // Then
 assertEquals(expectedRoles, actualRoles);
 }

 @Test
 public void testIsApprovable() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceEntity> approvableInstances = Arrays.asList(new UserTaskInstanceEntity(),
 new UserTaskInstanceEntity());
 Set<Long> ids = new HashSet<>(Arrays.asList(approvalInstanceId, 1L));
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(approvableInstances);

 // When
 boolean actual = approvalPermissionService.isApprovable(approvalInstanceId);

 // Then
 assertTrue(actual);
 }

 @Test
 public void testIsApprovableWithNotApprovableInstance() {
 // Given
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceEntity> approvableInstances = Collections.emptyList();
 Set<Long> ids = new HashSet<>(Arrays.asList(approvalInstanceId, 1L));
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(approvableInstances);

 // When
 boolean actual = approvalPermissionService.isApprovable(approvalInstanceId);

 // Then
 assertFalse(actual);
 }

 @Test
 public void testSetCandidateResourceRole() {
 // Given
 Long approvalInstanceId = 1L;
 String resourceRoleIdentifier = "role1";

 // When
 approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

 // Then
 // Verify that the method call to userTaskCandidateRepository is made with the correct arguments
 verify(userTaskCandidateRepository).save(new UserTaskInstanceCandidateEntity());
 }
}