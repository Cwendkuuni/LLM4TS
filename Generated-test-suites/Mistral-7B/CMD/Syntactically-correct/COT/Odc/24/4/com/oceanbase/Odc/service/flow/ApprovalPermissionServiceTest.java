package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.User;
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

 @Test
 public void testGetCandidates() {
 UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
 entity1.setApprovalInstanceId(1L);
 entity1.setRoleId(1L);
 UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
 entity2.setApprovalInstanceId(1L);
 entity2.setUserId(2L);
 List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
 entities.add(entity1);
 entities.add(entity2);
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(entities);

 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Set<User> expected = new HashSet<>();
 expected.add(new User(1L, "User1", "user1@example.com"));
 expected.add(new User(2L, "User2", "user2@example.com"));
 assertEquals(expected, result);
 }

 @Test
 public void testGetCandidates_empty() {
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(Collections.emptyList());

 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 assertEquals(Collections.emptySet(), result);
 }

 @Test
 public void testGetCandidates_role() {
 UserRoleEntity userRoleEntity = new UserRoleEntity();
 userRoleEntity.setRoleId(1L);
 userRoleEntity.setUserId(1L);
 Mockito.when(userRoleRepository.findByRoleId(1L)).thenReturn(userRoleEntity);

 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 User user = new User(1L, "User1", "user1@example.com");
 assertEquals(Collections.singleton(user), result);
 }

 @Test
 public void testGetCandidates_user() {
 UserEntity userEntity = new UserEntity();
 userEntity.setUserId(2L);
 Mockito.when(userRepository.findByUserId(2L)).thenReturn(userEntity);

 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 User user = new User(2L, "User2", "user2@example.com");
 assertEquals(Collections.singleton(user), result);
 }

 @Test
 public void testGetCandidates_multiple() {
 UserRoleEntity userRoleEntity1 = new UserRoleEntity();
 userRoleEntity1.setRoleId(1L);
 userRoleEntity1.setUserId(1L);
 UserRoleEntity userRoleEntity2 = new UserRoleEntity();
 userRoleEntity2.setRoleId(1L);
 userRoleEntity2.setUserId(3L);
 List<UserRoleEntity> userRoleEntities = new ArrayList<>();
 userRoleEntities.add(userRoleEntity1);
 userRoleEntities.add(userRoleEntity2);
 Mockito.when(userRoleRepository.findByRoleId(1L)).thenReturn(userRoleEntities.get(0));
 Mockito.when(userRepository.findByUserId(3L)).thenReturn(new UserEntity());

 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Set<User> expected = new HashSet<>();
 expected.add(new User(1L, "User1", "user1@example.com"));
 expected.add(new User(3L, "User3", "user3@example.com"));
 assertEquals(expected, result);
 }

 @Test
 public void testGetCandidates_disabled() {
 UserRoleEntity userRoleEntity = new UserRoleEntity();
 userRoleEntity.setRoleId(1L);
 userRoleEntity.setUserId(1L);
 Mockito.when(userRoleRepository.findByRoleId(1L)).thenReturn(userRoleEntity);

 Long approvalInstanceId = 1L;
 Boolean enabled = false;
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 assertEquals(Collections.emptySet(), result);
 }

 @Test
 public void testGetCandidates_null() {
 Long approvalInstanceId = 1L;
 Boolean enabled = null;
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 assertEquals(Collections.emptySet(), result);
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 Set<Long> approvalInstanceIds = new HashSet<>();
 approvalInstanceIds.add(1L);
 approvalInstanceIds.add(2L);
 List<UserTaskInstanceCandidateEntity> entities1 = new ArrayList<>();
 entities1.add(new UserTaskInstanceCandidateEntity());
 entities1.add(new UserTaskInstanceCandidateEntity());
 entities1.add(new UserTaskInstanceCandidateEntity());
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(entities1);

 Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
 Map<Long, Set<Long>> expected = new HashMap<>();
 expected.put(1L, new HashSet<>(Arrays.asList(1L, 2L, 3L)));
 expected.put(2L, new HashSet<>(Arrays.asList(3L, 4L, 5L)));
 assertEquals(expected, result);
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
 Set<Long> approvalInstanceIds = new HashSet<>();
 approvalInstanceIds.add(1L);
 approvalInstanceIds.add(2L);
 List<UserTaskInstanceCandidateEntity> entities1 = new ArrayList<>();
 entities1.add(new UserTaskInstanceCandidateEntity());
 entities1.add(new UserTaskInstanceCandidateEntity());
 entities1.add(new UserTaskInstanceCandidateEntity());
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(entities1);

 Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
 Map<Long, Set<String>> expected = new HashMap<>();
 expected.put(1L, new HashSet<>(Arrays.asList("role1", "role2", "role3")));
 expected.put(2L, new HashSet<>(Arrays.asList("role3", "role4", "role5")));
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCandidatesByFlowInstanceIds_empty() {
 approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.emptyList());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 Set<Long> flowInstanceIds = new HashSet<>();
 flowInstanceIds.add(1L);
 flowInstanceIds.add(2L);
 Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
 Map<Long, UserEntity> userEntityMap = new HashMap<>();
 userEntityMap.put(1L, new UserEntity());
 userEntityMap.put(2L, new UserEntity());
 Map<Long, Set<UserEntity>> expected = new HashMap<>();
 expected.put(1L, new HashSet<>(Arrays.asList(userEntityMap.get(1L))));
 expected.put(2L, new HashSet<>(Arrays.asList(userEntityMap.get(2L))));
 assertEquals(expected, result);
 }

 @Test
 public void testIsApprovable() {
 Long approvalInstanceId = 1L;
 Set<Long> approvableApprovalInstances = new HashSet<>();
 approvableApprovalInstances.add(approvalInstanceId);
 Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
 .thenReturn(new ArrayList<UserTaskInstanceEntity>());
 Mockito.when(userTaskInstanceRepository.findByStatusAndIdIn(FlowNodeStatus.EXECUTING, approvableApprovalInstances))
 .thenReturn(new ArrayList<>());
 Mockito.when(userService.getCurrentUserRoleIds())
 .thenReturn(new HashSet<Long>(Arrays.asList(1L, 2L)));
 Mockito.when(resourceRoleService.getResourceRoleIdentifiersByUserId(Mockito.anyLong()))
 .thenReturn(new ArrayList<String>());
 when(userService.getUserRoleIds(Mockito.anyLong()))
 .thenReturn(new HashSet<Long>(Arrays.asList(3L)));

 assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
 }

 @Test
 public void testIsApprovable_false() {
 Long approvalInstanceId = 1L;
 Set<Long> approvableApprovalInstances = new HashSet<>();
 approvableApprovalInstances.add(approvalInstanceId);
 Mockito.when(userTaskInstanceRepository.findByStatusAndIdIn(FlowNodeStatus.EXECUTING, approvableApprovalInstances))
 .thenReturn(new ArrayList<>());
 Mockito.when(userService.getCurrentUserRoleIds())
 .thenReturn(new HashSet<Long>(Arrays.asList(1L, 2L)));
 when(userService.getUserRoleIds(Mockito.anyLong()))
 .thenReturn(Collections.emptySet());

 assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
 }

 @Test
 public void testSetCandidateResourceRole() {
 Long approvalInstanceId = 1L;
 String resourceRoleIdentifier = "role1";
 approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
 UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
 candidateEntity.setApprovalInstanceId(approvalInstanceId);
 candidateEntity.setResourceRoleIdentifier(resourceRoleIdentifier);
 Mockito.verify(userTaskCandidateRepository).save(candidateEntity);
 }

}