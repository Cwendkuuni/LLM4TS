package com.oceanbase.odc.service.flow;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;

@SpringBootTest
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
 private ApprovalPermissionService approvalPermissionService;

 @Test
 public void testGetCandidates_EmptyInput_ReturnsEmptySet() {
 Long approvalInstanceId = 1L;
 Boolean enabled = Boolean.TRUE;
 Set<User> expected = Collections.emptySet();
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());
 Set<User> actual = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetCandidates_NormalInput_ReturnsExpectedUsers() {
 Long approvalInstanceId = 1L;
 Boolean enabled = Boolean.TRUE;
 Set<UserTaskInstanceCandidateEntity> entities = new HashSet<>();
 UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
 entity1.setApprovalInstanceId(approvalInstanceId);
 entity1.setUserId(1L);
 entities.add(entity1);
 UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
 entity2.setApprovalInstanceId(approvalInstanceId);
 entity2.setRoleId(1L);
 entities.add(entity2);
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
 Set<User> expected = Set.of(new User(1L), new Role(1L, "roleName"));
 Set<User> actual = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds_EmptyInput_ReturnsEmptyMap() {
 List<Long> approvalInstanceIds = new ArrayList<>();
 Map<Long, Set<Long>> expected = new HashMap<>();
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(new ArrayList<>());
 Map<Long, Set<Long>> actual = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds_NormalInput_ReturnsExpectedMap() {
 List<Long> approvalInstanceIds = List.of(1L, 2L);
 List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
 UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
 entity1.setApprovalInstanceId(1L);
 entity1.setRoleId(1L);
 entities.add(entity1);
 UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
 entity2.setApprovalInstanceId(2L);
 entity2.setRoleId(2L);
 entities.add(entity2);
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);
 Map<Long, Set<Long>> expected = new HashMap<>();
 expected.put(1L, Set.of(1L));
 expected.put(2L, Set.of(2L));
 Map<Long, Set<Long>> actual = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds_NormalInput_ReturnsExpectedMap() {
 List<Long> approvalInstanceIds = List.of(1L, 2L);
 List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
 UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
 entity1.setApprovalInstanceId(1L);
 entity1.setResourceRoleIdentifier("identifier1");
 entities.add(entity1);
 UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
 entity2.setApprovalInstanceId(2L);
 entity2.setResourceRoleIdentifier("identifier2");
 entities.add(entity2);
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);
 Map<Long, Set<String>> expected = new HashMap<>();
 expected.put(1L, Set.of("identifier1"));
 expected.put(2L, Set.of("identifier2"));
 Map<Long, Set<String>> actual = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetCandidateUserIds_EmptyInput_ReturnsEmptySet() {
 List<Long> approvalInstanceIds = new ArrayList<>();
 Set<Long> expected = Collections.emptySet();
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(new ArrayList<>());
 Set<Long> actual = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetCandidateUserIds_NormalInput_ReturnsExpectedUsersIds() {
 List<Long> approvalInstanceIds = List.of(1L, 2L);
 List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
 UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
 entity1.setApprovalInstanceId(1L);
 entity1.setUserId(1L);
 entities.add(entity1);
 UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
 entity2.setApprovalInstanceId(2L);
 entity2.setUserId(2L);
 entities.add(entity2);
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);
 Set<Long> expected = Set.of(1L, 2L);
 Set<Long> actual = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetCandidateRoles_EmptyInput_ReturnsEmptyList() {
 Long approvalInstanceId = 1L;
 Boolean enabled = Boolean.TRUE;
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());
 List<Role> expected = Collections.emptyList();
 List<Role> actual = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetCandidateRoles_NormalInput_ReturnsExpectedRoles() {
 Long approvalInstanceId = 1L;
 Boolean enabled = Boolean.TRUE;
 List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
 UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
 entity1.setApprovalInstanceId(approvalInstanceId);
 entity1.setRoleId(1L);
 UserRoleEntity roleEntity = new UserRoleEntity();
 roleEntity.setRoleId(1L);
 roleEntity.setRoleName("roleName1");
 entities.add(entity1);
 UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
 entity2.setApprovalInstanceId(approvalInstanceId);
 entity2.setRoleId(2L);
 UserRoleEntity roleEntity2 = new UserRoleEntity();
 roleEntity2.setRoleId(2L);
 roleEntity2.setRoleName("roleName2");
 entities.add(entity2);
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
 when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), enabled)).thenReturn(List.of(new Role(1L, "roleName1")));
 when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(2L), enabled)).thenReturn(List.of(new Role(2L, "roleName2")));
 List<Role> expected = List.of(new Role(1L, "roleName1"), new Role(2L, "roleName2"));
 List<Role> actual = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testIsApprovable_EmptyInput_ReturnsFalse() {
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(new ArrayList<>());
 Assertions.assertFalse(approvalPermissionService.isApprovable(1L));
 }

 @Test
 public void testIsApprovable_NormalInput_ReturnsTrue() {
 Long approvalInstanceId = 1L;
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(approvalInstanceId);
 entity.setStatus(FlowNodeStatus.EXECUTING);
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(List.of(entity));
 Assertions.assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
 }

 @Test
 public void testIsApprovable_NonExecutingInput_ReturnsFalse() {
 Long approvalInstanceId = 1L;
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(approvalInstanceId);
 entity.setStatus(FlowNodeStatus.COMPLETED);
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(new ArrayList<>());
 Assertions.assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
 }

 @Test
 public void testSetCandidateResourceRole_NormalInput_DoesNotThrowException() {
 Long approvalInstanceId = 1L;
 String resourceRoleIdentifier = "identifier";
 approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
 Mockito.verify(userTaskCandidateRepository).save(Mockito.any(UserTaskInstanceCandidateEntity.class));
 }

 @Test(expected = RuntimeException.class)
 public void testSetCandidateResourceRole_NullInput_ThrowsException() {
 Long approvalInstanceId = null;
 String resourceRoleIdentifier = "identifier";
 approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
 }

 @Test(expected = RuntimeException.class)
 public void testSetCandidateResourceRole_EmptyInput_ThrowsException() {
 Long approvalInstanceId = 1L;
 String resourceRoleIdentifier = "";
 approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
 }

 @Test
 public void testListApprovableExternalInstances_EmptyInput_ReturnsEmptyList() {
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(new ArrayList<>());
 List<UserTaskInstanceEntity> expected = new ArrayList<>();
 List<UserTaskInstanceEntity> actual = approvalPermissionService.listApprovableExternalInstances();
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testListApprovableExternalInstances_NormalInput_ReturnsExpectedList() {
 List<UserTaskInstanceEntity> expected = List.of(new UserTaskInstanceEntity(1L, 1L, null, null, null, null,
 FlowNodeStatus.EXECUTING, null, null, null, null, null, null, null, null, null, null, null, null, null),
 new UserTaskInstanceEntity(2L, 2L, null, null, null, null, FlowNodeStatus.EXECUTING, null, null, null, null,
 null, null, null, null, null, null, null, null, null, null));
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(expected);
 List<UserTaskInstanceEntity> actual = approvalPermissionService.listApprovableExternalInstances();
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds_EmptyInput_ReturnsEmptyMap() {
 List<Long> flowInstanceIds = new ArrayList<>();
 Map<Long, Set<UserEntity>> expected = new HashMap<>();
 Map<Long, Set<UserEntity>> actual = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds_NormalInput_ReturnsExpectedMap() {
 List<Long> flowInstanceIds = List.of(1L, 2L);
 Map<Long, Set<UserEntity>> expected = Map.of(1L, Set.of(new UserEntity(1L, "userName1", null, null, false),
 new UserEntity(2L, "userName2", null, null, false)),
 2L, Set.of(new UserEntity(3L, "userName3", null, null, false), new UserEntity(4L, "userName4", null, null, false)));
 List<UserTaskInstanceEntity> approvalInstances = new ArrayList<>();
 UserTaskInstanceEntity approvalInstance1 = new UserTaskInstanceEntity();
 approvalInstance1.setFlowInstanceId(1L);
 approvalInstance1.setStatus(FlowNodeStatus.EXECUTING);
 approvalInstances.add(approvalInstance1);
 UserTaskInstanceEntity approvalInstance2 = new UserTaskInstanceEntity();
 approvalInstance2.setFlowInstanceId(2L);
 approvalInstance2.setStatus(FlowNodeStatus.EXECUTING);
 approvalInstances.add(approvalInstance2);
 when(userTaskInstanceRepository.findByFlowInstanceIdsAndStatus(flowInstanceIds, FlowNodeStatus.EXECUTING))
 .thenReturn(approvalInstances);
 when(userRepository.findByUsersIds(List.of(1L, 2L, 3L, 4L))).thenReturn(Map.of(1L, new UserEntity(1L, "userName1", null, null, false),
 2L, new UserEntity(2L, "userName2", null, null, false), 3L, new UserEntity(3L, "userName3", null, null, false),
 4L, new UserEntity(4L, "userName4", null, null, false)));
 Map<Long, Set<UserEntity>> actual = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
 Assertions.assertEquals(expected, actual);
 }
}