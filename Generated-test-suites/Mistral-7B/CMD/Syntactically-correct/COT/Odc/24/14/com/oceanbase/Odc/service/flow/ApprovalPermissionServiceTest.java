import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
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

 @InjectMocks
 private ApprovalPermissionService approvalPermissionService;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetCandidates() {
 // Provide test data for getCandidates method
 Long approvalInstanceId = 1L;
 Set<User> expectedUsers = new HashSet<>();
 User user1 = new User();
 User user2 = new User();
 expectedUsers.add(user1);
 expectedUsers.add(user2);

 when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
 .thenReturn(new HashSet<>(Arrays.asList(user1, user2)));

 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertEquals(expectedUsers, actualUsers);

 verify(userRepository, times(1)).findByRoleIdsAndEnabled(anySet(), anyBoolean());
 }

 @Test
 public void testGetCandidates_DisabledUsers() {
 // Provide test data for getCandidates method with disabled users
 Set<User> expectedUsers = new HashSet<>();
 User user1 = new User();
 User user2 = new User();
 expectedUsers.add(user1);
 expectedUsers.add(user2);

 when(userRepository.findByRoleIdsAndEnabled(anySet(), false))
 .thenReturn(new HashSet<>(Arrays.asList(user1)));

 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, false);
 assertEquals(expectedUsers, actualUsers);

 verify(userRepository, times(1)).findByRoleIdsAndEnabled(anySet(), false);
 }

 @Test
 public void testGetCandidates_NoUsersOrRoles() {
 // Provide test data for getCandidates method with no users or roles
 Set<User> expectedUsers = Collections.emptySet();

 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(new ArrayList<>());

 Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, null);
 assertEquals(expectedUsers, actualUsers);

 verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 // Provide test data for getInstanceId2CandidateRoleIds method
 Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
 Map<Long, Set<Long>> expectedMap = new HashMap<>();
 Set<Long> roleIds1 = new HashSet<>(Arrays.asList(1L, 2L, 3L));
 Set<Long> roleIds2 = new HashSet<>(Arrays.asList(4L, 5L));
 expectedMap.put(1L, roleIds1);
 expectedMap.put(2L, roleIds2);

 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(new ArrayList<>(Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity())));

 when(userTaskCandidateRepository.findByApprovalInstanceId(1L))
 .thenReturn(new UserTaskInstanceCandidateEntity());
 when(userTaskCandidateRepository.findByApprovalInstanceId(2L))
 .thenReturn(new UserTaskInstanceCandidateEntity());
 when(userRoleRepository.findByRoleIdIn(anySet()))
 .thenReturn(new HashSet<>(Arrays.asList(new UserRoleEntity(), new UserRoleEntity())));

 Map<Long, Set<Long>> actualMap = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
 assertEquals(expectedMap, actualMap);

 verify(userTaskCandidateRepository, times(2)).findByApprovalInstanceId(1L);
 verify(userTaskCandidateRepository, times(2)).findByApprovalInstanceId(2L);
 verify(userRoleRepository, times(2)).findByRoleIdIn(anySet());
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
 // Provide test data for getInstanceId2CandidateResourceRoleIdentifierIds method
 Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
 Map<Long, Set<String>> expectedMap = new HashMap<>();
 Set<String> resourceRoleIdentifierIds1 = new HashSet<>(Arrays.asList("role1", "role2"));
 Set<String> resourceRoleIdentifierIds2 = new HashSet<>(Arrays.asList("role3", "role4"));
 expectedMap.put(1L, resourceRoleIdentifierIds1);
 expectedMap.put(2L, resourceRoleIdentifierIds2);

 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(new ArrayList<>(Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity())));

 when(userTaskCandidateRepository.findByApprovalInstanceId(1L))
 .thenReturn(new UserTaskInstanceCandidateEntity());
 when(userTaskCandidateRepository.findByApprovalInstanceId(2L))
 .thenReturn(new UserTaskInstanceCandidateEntity());
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(anySet(), anySet()))
 .thenReturn(new ArrayList<>(Arrays.asList(new UserResourceRoleEntity(), new UserResourceRoleEntity())));

 Map<Long, Set<String>> actualMap = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
 assertEquals(expectedMap, actualMap);

 verify(userTaskCandidateRepository, times(2)).findByApprovalInstanceId(1L);
 verify(userTaskCandidateRepository, times(2)).findByApprovalInstanceId(2L);
 verify(userResourceRoleRepository, times(2)).findByResourceIdsAndResourceRoleIdentifiersIn(anySet(), anySet());
 }

 @Test
 public void testGetCandidateUserIds() {
 // Provide test data for getCandidateUserIds method
 Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
 Set<Long> expectedUserIds = new HashSet<>(Arrays.asList(1L, 2L, 3L));

 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(new ArrayList<>(Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity())));
 when(userRoleRepository.findByRoleIdIn(anySet()))
 .thenReturn(new HashSet<>(Arrays.asList(new UserRoleEntity(), new UserRoleEntity())));
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(anySet(), anySet()))
 .thenReturn(new ArrayList<>(Arrays.asList(new UserResourceRoleEntity(), new UserResourceRoleEntity())));

 Set<Long> actualUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
 assertEquals(expectedUserIds, actualUserIds);

 verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
 verify(userRoleRepository, times(2)).findByRoleIdIn(anySet());
 verify(userResourceRoleRepository, times(1)).findByResourceIdsAndResourceRoleIdentifiersIn(anySet(), anySet());
 }

 @Test
 public void testGetCandidateRoles() {
 // Provide test data for getCandidateRoles method
 Long approvalInstanceId = 1L;
 List<Role> expectedRoles = new ArrayList<>();
 Role role1 = new Role();
 Role role2 = new Role();
 expectedRoles.add(role1);
 expectedRoles.add(role2);

 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(new UserTaskInstanceCandidateEntity());
 when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
 .thenReturn(new ArrayList<>(Arrays.asList(role1, role2)));

 List<Role> actualRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
 assertEquals(expectedRoles, actualRoles);

 verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
 verify(roleRepository, times(1)).findByRoleIdsAndEnabled(anySet(), anyBoolean());
 }

 @Test(expected = NullPointerException.class)
 public void testIsApprovable_NullApprovalInstanceId() {
 approvalPermissionService.isApprovable(null);
 }

 @Test
 public void testIsApprovable_ApprovalInstanceIdInList() {
 long approvalInstanceId = 1L;
 Set<Long> approvableApprovalInstances = new HashSet<>(Arrays.asList(1L, 2L));

 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
 .thenReturn(new ArrayList<>(Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity())));

 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(approvableApprovalInstances.stream()
 .filter(id -> id.equals(approvalInstanceId)).findFirst().orElse(null));

 assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));

 verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
 }

 @Test
 public void testIsApprovable_ApprovalInstanceIdNotInList() {
 long approvalInstanceId = 1L;
 Set<Long> approvableApprovalInstances = new HashSet<>(Arrays.asList(2L, 3L));

 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
 .thenReturn(new ArrayList<>(Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity())));

 assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));

 verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
 }

 @Test
 public void testListApprovableExternalInstances() {
 // Provide test data for listApprovableExternalInstances method
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
 .thenReturn(new ArrayList<>(Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity())));

 List<UserTaskInstanceEntity> actualInstances = approvalPermissionService.listApprovableExternalInstances();
 assertEquals(2, actualInstances.size());

 verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 // Provide test data for getCandidatesByFlowInstanceIds method
 Set<Long> flowInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
 Map<Long, Set<UserEntity>> expectedMap = new HashMap<>();
 UserEntity user1 = new UserEntity();
 UserEntity user2 = new UserEntity();
 expectedMap.put(1L, new HashSet<>(Arrays.asList(user1, user2)));
 expectedMap.put(2L, new HashSet<>());

 when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.EXECUTING))
 .thenReturn(new HashMap<>(Arrays.asList(
 1L, new LongWrapper(1),
 2L, new LongWrapper(2))));

 when(userRepository.findByUserIds(anySet()))
 .thenReturn(new ArrayList<>(Arrays.asList(user1, user2)));

 Map<Long, Set<UserEntity>> actualMap = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
 assertEquals(expectedMap, actualMap);

 verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.EXECUTING);
 verify(userRepository, times(1)).findByUserIds(anySet());
 }
}

class LongWrapper {
 Long id;

 public LongWrapper(Long id) {
 this.id = id;
 }
}