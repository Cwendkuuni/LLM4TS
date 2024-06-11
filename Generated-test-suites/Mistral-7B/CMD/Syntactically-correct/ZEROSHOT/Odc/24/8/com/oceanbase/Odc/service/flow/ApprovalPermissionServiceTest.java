import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.AuthenticationFacade;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.Role;
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
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeImpl;

@RunWith(MockitoJUnitRunner.class)
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

 private static final long APPROVAL_INSTANCE_ID = 1L;
 private static final long FLOW_INSTANCE_ID = 1L;
 private static final Long USER_ID = 1L;
 private static final String ROLE_KEY = "ROLE";
 private static final String USER_KEY = "USER";
 private static final String ROLE_IDENTIFIER = "roleIdentifier";
 private static final String RESOURCE_ROLE_IDENTIFIER = "resourceRoleIdentifier";
 private static final List<Long> APPROVAL_INSTANCE_IDS = Arrays.asList(APPROVAL_INSTANCE_ID);
 private static final List<Role> ROLES = Arrays.asList(new Role());
 private static final Set<String> RESOURCE_ROLE_IDENTIFIERS = new HashSet<>(Arrays.asList(RESOURCE_ROLE_IDENTIFIER));
 private static final Set<UserEntity> USERS = new HashSet<>();
 private static final Map<Long, UserEntity> USERS_MAP = new HashMap<>();
 private static final Map<Long, Set<UserEntity>> INSTANCE_ID_USERS = new HashMap<>();

 @Before
 public void setup() {
 UserEntity userEntity = new UserEntity();
 userEntity.setId(USER_ID);
 USERS.add(userEntity);
 USERS_MAP.put(USER_ID, userEntity);
 }

 @Test
 public void testGetCandidates() {
 // test case 1: enabled is null
 Set<User> candidates = approvalPermissionService.getCandidates(APPROVAL_INSTANCE_ID, null);
 assertEquals(0, candidates.size());

 // test case 2: enabled is false
 Set<User> candidatesFalse = approvalPermissionService.getCandidates(APPROVAL_INSTANCE_ID, false);
 assertEquals(0, candidatesFalse.size());

 // test case 3: enabled is true
 Set<User> candidatesTrue = approvalPermissionService.getCandidates(APPROVAL_INSTANCE_ID, true);
 List<UserTaskInstanceCandidateEntity> entities = Mockito.mock(List.class);
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(APPROVAL_INSTANCE_ID)).thenReturn(entities);
 Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
 Set<Long> userIds = new HashSet<>(Arrays.asList(USER_ID));
 Mockito.when(entities.stream()).thenReturn(entities.stream());
 Mockito.when(entities.flatMap(entity -> {
 List<Pair<String, Long>> list = new LinkedList<>();
 if (entity.getRoleId() != null) {
 list.add(new Pair<>(ROLE_KEY, entity.getRoleId()));
 }
 if (entity.getUserId() != null) {
 list.add(new Pair<>(USER_KEY, entity.getUserId()));
 }
 return list.stream();
 })).thenReturn(Mockito.mock(Stream.class));
 Mockito.when(list.stream().collect(Collectors.toMap(pair -> pair.left, pair -> new HashSet<>(Collections.singleton(pair.right)),
 (s1, s2) -> {
 s1.addAll(s2);
 return s1;
 }))).thenReturn(new HashMap<>());
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(ROLES);
 Mockito.when(userRepository.findByUserIdsAndEnabled(userIds, true)).thenReturn(USERS);
 Mockito.when(userRepository.findByUserIdsOrRoleIds(userIds, roleIds, true)).thenReturn(USERS);
 candidatesTrue = approvalPermissionService.getCandidates(APPROVAL_INSTANCE_ID, true);
 assertEquals(1, candidatesTrue.size());
 assertNotNull(candidatesTrue.iterator().next());

 // test case 4: enabled is true and there are no users or roles
 Set<User> candidatesEmpty = approvalPermissionService.getCandidates(APPROVAL_INSTANCE_ID, true);
 assertEquals(0, candidatesEmpty.size());
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(APPROVAL_INSTANCE_IDS);
 assertEquals(0, instanceId2CandidateRoleIds.size());
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
 Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(APPROVAL_INSTANCE_IDS);
 assertEquals(0, instanceId2CandidateResourceRoleIdentifierIds.size());
 }

 @Test
 public void testGetCandidateUserIds() {
 Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(APPROVAL_INSTANCE_IDS);
 assertEquals(0, candidateUserIds.size());
 }

 @Test
 public void testGetCandidateRoles() {
 List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(APPROVAL_INSTANCE_ID, true);
 assertEquals(0, candidateRoles.size());
 }

 @Test
 public void testIsApprovable() {
 assertFalse(approvalPermissionService.isApprovable(null));
 assertTrue(approvalPermissionService.isApprovable(APPROVAL_INSTANCE_ID));
 }

 @Test
 public void testGetApprovableApprovalInstances() {
 List<UserTaskInstanceEntity> approvableApprovalInstances = approvalPermissionService.getApprovableApprovalInstances();
 assertEquals(0, approvableApprovalInstances.size());
 }

 @Test
 public void testSetCandidateResourceRole() {
 approvalPermissionService.setCandidateResourceRole(APPROVAL_INSTANCE_ID, RESOURCE_ROLE_IDENTIFIER);
 }

 @Test
 public void testListApprovableExternalInstances() {
 List<UserTaskInstanceEntity> approvableExternalInstances = approvalPermissionService.listApprovableExternalInstances();
 assertEquals(0, approvableExternalInstances.size());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(APPROVAL_INSTANCE_IDS);
 assertEquals(0, candidatesByFlowInstanceIds.size());
 }
}