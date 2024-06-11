import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class ApprovalPermissionServiceTest {

 private static final long APPROVAL_INSTANCE_ID = 1L;
 private static final long APPROVAL_INSTANCE_ID_2 = 2L;
 private static final long ROLE_ID = 1L;
 private static final long USER_ID = 1L;
 private static final String RESOURCE_ROLE_IDENTIFIER = "test";
 private static final String RESOURCE_ROLE_IDENTIFIER_2 = "test2";
 private ApprovalPermissionService approvalPermissionService;
 private AuthenticationFacade authenticationFacade;
 private UserResourceRoleRepository userResourceRoleRepository;
 private UserRoleRepository userRoleRepository;
 private UserRepository userRepository;
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
 private UserTaskInstanceRepository userTaskInstanceRepository;
 private RoleRepository roleRepository;
 private ResourceRoleService resourceRoleService;
 private UserService userService;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 approvalPermissionService = new ApprovalPermissionService();
 authenticationFacade = mock(AuthenticationFacade.class);
 userResourceRoleRepository = mock(UserResourceRoleRepository.class);
 userRoleRepository = mock(UserRoleRepository.class);
 userRepository = mock(UserRepository.class);
 userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
 userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
 roleRepository = mock(RoleRepository.class);
 resourceRoleService = mock(ResourceRoleService.class);
 userService = mock(UserService.class);
 when(authenticationFacade.currentUserId()).thenReturn(USER_ID);
 when(userRepository.findByUserIds(CollectionUtils.singletonList(USER_ID))).thenReturn(Collections.singletonList(new UserEntity()));
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Collections.singletonList(1L),
 Collection.singletonList(RESOURCE_ROLE_IDENTIFIER)))
 .thenReturn(Collections.singletonList(new UserResourceRoleEntity()));
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Collection.singletonList(1L),
 Collection.singletonList(RESOURCE_ROLE_IDENTIFIER_2)))
 .thenReturn(Collections.emptyList());
 when(userRoleRepository.findByRoleIdsAndEnabled(Collections.singletonList(ROLE_ID), Boolean.TRUE))
 .thenReturn(Collections.singletonList(new UserRoleEntity()));
 when(userRepository.findByRoleIds(Collections.singletonList(ROLE_ID)))
 .thenReturn(Collections.singletonList(new UserEntity()));
 when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singletonList(ROLE_ID));
 when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.singletonList(RESOURCE_ROLE_IDENTIFIER));
 when(userService.getUserRoleIds(USER_ID)).thenReturn(Collections.singletonList(ROLE_ID));
 when(userService.getUserResourceRoleIdentifiers(USER_ID))
 .thenReturn(Collections.singletonList(RESOURCE_ROLE_IDENTIFIER));
 when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(ROLE_ID), Boolean.TRUE))
 .thenReturn(Collections.singletonList(new Role()));
 when(resourceRoleService.getResourceRoleIdentifiersByUserId(USER_ID))
 .thenReturn(Collections.singletonList(RESOURCE_ROLE_IDENTIFIER));
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
 .thenReturn(Collections.singletonList(new UserTaskInstanceEntity()));
 when(userTaskInstanceRepository.findByFlowInstanceIdAndStatus(APPROVAL_INSTANCE_ID, FlowNodeStatus.EXECUTING))
 .thenReturn(null);
 when(userTaskInstanceRepository.findByFlowInstanceIdAndStatus(APPROVAL_INSTANCE_ID_2, FlowNodeStatus.EXECUTING))
 .thenReturn(new UserTaskInstanceEntity());
 when(userTaskInstanceRepository.findByResourceRoleIdentifier(RESOURCE_ROLE_IDENTIFIER))
 .thenReturn(Collections.emptyList());
 when(userTaskInstanceRepository.findByResourceRoleIdentifier(RESOURCE_ROLE_IDENTIFIER_2))
 .thenReturn(Collections.singletonList(new UserTaskInstanceEntity()));
 when(userTaskCandidateRepository.findByApprovalInstanceId(APPROVAL_INSTANCE_ID))
 .thenReturn(new ArrayList<>());
 when(userTaskCandidateRepository.findByApprovalInstanceId(APPROVAL_INSTANCE_ID_2))
 .thenReturn(new ArrayList<>());
 when(userTaskCandidateRepository.save(any(UserTaskInstanceCandidateEntity.class)))
 .thenAnswer(invocation -> invocation.getArgument(0));
 }

 @Test
 public void testGetCandidates() {
 // Test with null enabled
 Set<User> candidates = approvalPermissionService.getCandidates(APPROVAL_INSTANCE_ID, null);
 assertTrue(candidates.isEmpty());

 // Test with false enabled
 Set<User> candidatesFalse = approvalPermissionService.getCandidates(APPROVAL_INSTANCE_ID, false);
 assertTrue(candidatesFalse.isEmpty());

 // Test with true enabled
 Set<User> candidatesTrue = approvalPermissionService.getCandidates(APPROVAL_INSTANCE_ID, true);
 assertTrue(candidatesTrue.isEmpty());
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(
 Collection.singletonList(APPROVAL_INSTANCE_ID));
 assertTrue(instanceId2CandidateRoleIds.isEmpty());
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
 Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
 Collection.singletonList(APPROVAL_INSTANCE_ID));
 assertTrue(instanceId2CandidateResourceRoleIdentifierIds.isEmpty());
 }

 @Test
 public void testGetCandidateUserIds() {
 Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(
 Collection.singletonList(APPROVAL_INSTANCE_ID));
 assertTrue(candidateUserIds.isEmpty());
 }

 @Test
 public void testGetCandidateRoles() {
 List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(APPROVAL_INSTANCE_ID, Boolean.TRUE);
 assertTrue(candidateRoles.isEmpty());
 }

 @Test
 public void testIsApprovable() {
 assertFalse(approvalPermissionService.isApprovable(APPROVAL_INSTANCE_ID));
 }

 @Test
 public void testIsApprovableWithUserId() {
 ApprovalPermissionService approvalPermissionServiceWithUserId = new ApprovalPermissionService();
 approvalPermissionServiceWithUserId.setUserService(userService);
 assertTrue(approvalPermissionServiceWithUserId.isApprovable(APPROVAL_INSTANCE_ID));
 }

 @Test
 public void testGetApprovableApprovalInstances() {
 List<UserTaskInstanceEntity> approvableApprovalInstances = approvalPermissionService.getApprovableApprovalInstances();
 assertTrue(approvableApprovalInstances.isEmpty());
 }

 @Test
 public void testGetApprovableApprovalInstancesWithUserId() {
 ApprovalPermissionService approvalPermissionServiceWithUserId = new ApprovalPermissionService();
 approvalPermissionServiceWithUserId.setUserService(userService);
 List<UserTaskInstanceEntity> approvableApprovalInstances = approvalPermissionServiceWithUserId
 .getApprovableApprovalInstances();
 assertTrue(approvableApprovalInstances.isEmpty());
 }

 @Test
 public void testSetCandidateResourceRole() {
 approvalPermissionService.setCandidateResourceRole(APPROVAL_INSTANCE_ID, RESOURCE_ROLE_IDENTIFIER);
 UserTaskInstanceCandidateEntity candidateEntity = approvalPermissionService.getUserTaskInstanceCandidateEntityByApprovalInstanceIdAndResourceRoleIdentifier(APPROVAL_INSTANCE_ID, RESOURCE_ROLE_IDENTIFIER);
 assertNotNull(candidateEntity);
 }

 @Test(expected = NullPointerException.class)
 public void testSetCandidateResourceRoleWithNullApprovalInstanceId() {
 approvalPermissionService.setCandidateResourceRole(null, RESOURCE_ROLE_IDENTIFIER);
 }

 @Test(expected = NullPointerException.class)
 public void testSetCandidateResourceRoleWithNullResourceRoleIdentifier() {
 approvalPermissionService.setCandidateResourceRole(APPROVAL_INSTANCE_ID, null);
 }

 @Test
 public void testListApprovableExternalInstances() {
 List<UserTaskInstanceEntity> listApprovableExternalInstances = approvalPermissionService.listApprovableExternalInstances();
 assertTrue(listApprovableExternalInstances.isEmpty());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(
 Collections.singletonList(APPROVAL_INSTANCE_ID));
 assertTrue(candidatesByFlowInstanceIds.isEmpty());
 }
}