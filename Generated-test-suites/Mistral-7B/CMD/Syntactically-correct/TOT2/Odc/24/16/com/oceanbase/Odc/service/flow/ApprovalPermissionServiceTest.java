import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ApprovalPermissionServiceTest {

 private ApprovalPermissionService approvalPermissionService;
 private UserRepository userRepository;
 private RoleRepository roleRepository;
 private UserRoleRepository userRoleRepository;
 private UserResourceRoleRepository userResourceRoleRepository;

 @Autowired
 public ApprovalPermissionServiceTest(ApprovalPermissionService approvalPermissionService, UserRepository userRepository,
 RoleRepository roleRepository, UserRoleRepository userRoleRepository, UserResourceRoleRepository userResourceRoleRepository) {
 this.approvalPermissionService = approvalPermissionService;
 this.userRepository = userRepository;
 this.roleRepository = roleRepository;
 this.userRoleRepository = userRoleRepository;
 this.userResourceRoleRepository = userResourceRoleRepository;
 }

 @BeforeEach
 public void setup() {
 // initialize test data
 User user1 = new User().setName("user1").setEmail("user1@example.com").setEnabled(true);
 Role role1 = new Role().setName("role1").setCode("ROLE_1");
 UserRoleEntity userRole1 = new UserRoleEntity(user1, role1);
 Role role2 = new Role().setName("role2").setCode("ROLE_2");
 UserRoleEntity userRole2 = new UserRoleEntity(user1, role2);
 UserResourceRoleEntity userResourceRole1 = new UserResourceRoleEntity(userRole1, "resource1", "identifier1");
 UserResourceRoleEntity userResourceRole2 = new UserResourceRoleEntity(userRole2, "resource2", "identifier2");
 userRoleRepository.saveAll(Arrays.asList(userRole1, userRole2));
 userResourceRoleRepository.saveAll(Arrays.asList(userResourceRole1, userResourceRole2));
 }

 @Test
 public void testGetCandidates_ValidArguments() {
 // given
 Long approvalInstanceId = 1L;
 // when
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
 // then
 Assertions.assertNotNull(candidates);
 Assertions.assertEquals(2, candidates.size());
 Assertions.assertTrue(candidates.contains(new User(user1)));
 }

 @Test
 public void testGetCandidates_EmptyResult() {
 // given
 Long approvalInstanceId = 3L;
 // when
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
 // then
 Assertions.assertNotNull(candidates);
 Assertions.assertEquals(0, candidates.size());
 }

 @Test
 public void testGetCandidates_EnabledUsers() {
 // given
 Long approvalInstanceId = 1L;
 // when
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 // then
 Assertions.assertNotNull(candidates);
 Assertions.assertEquals(1, candidates.size());
 Assertions.assertTrue(candidates.contains(new User(user1)));
 }

 @Test
 public void testGetCandidates_DisabledUsers() {
 // given
 Long approvalInstanceId = 1L;
 // when
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
 // then
 Assertions.assertNotNull(candidates);
 Assertions.assertEquals(0, candidates.size());
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds_ValidArguments() {
 // given
 Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
 // when
 Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
 // then
 Assertions.assertEquals(3, result.size());
 Assertions.assertFalse(result.isEmpty());
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds_EmptyResult() {
 // given
 Collection<Long> approvalInstanceIds = Collections.emptyList();
 // when
 Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
 // then
 Assertions.assertEquals(0, result.size());
 Assertions.assertTrue(result.isEmpty());
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds_ValidArguments() {
 // given
 Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
 // when
 Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
 // then
 Assertions.assertEquals(3, result.size());
 Assertions.assertFalse(result.isEmpty());
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds_EmptyResult() {
 // given
 Collection<Long> approvalInstanceIds = Collections.emptyList();
 // when
 Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
 // then
 Assertions.assertEquals(0, result.size());
 Assertions.assertTrue(result.isEmpty());
 }

 @Test
 public void testGetCandidateUserIds_ValidArguments() {
 // given
 Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
 // when
 Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
 // then
 Assertions.assertFalse(result.isEmpty());
 }

 @Test
 public void testGetCandidateUserIds_EmptyResult() {
 // given
 Collection<Long> approvalInstanceIds = Collections.emptyList();
 // when
 Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
 // then
 Assertions.assertTrue(result.isEmpty());
 }

 @Test
 public void testGetCandidateRoles_ValidArguments() {
 // given
 Long approvalInstanceId = 1L;
 Boolean enabled = null;
 // when
 List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
 // then
 Assertions.assertFalse(result.isEmpty());
 Assertions.assertEquals(2, result.size());
 }

 @Test
 public void testGetCandidateRoles_EmptyResult() {
 // given
 Long approvalInstanceId = 3L;
 Boolean enabled = null;
 // when
 List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
 // then
 Assertions.assertTrue(result.isEmpty());
 }

 @Test
 public void testIsApprovable_ValidInstanceId() {
 // given
 Long approvalInstanceId = 1L;
 // when
 boolean result = approvalPermissionService.isApprovable(approvalInstanceId);
 // then
 Assertions.assertTrue(result);
 }

 @Test
 public void testIsApprovable_InvalidInstanceId() {
 // given
 Long approvalInstanceId = 3L;
 // when
 boolean result = approvalPermissionService.isApprovable(approvalInstanceId);
 // then
 Assertions.assertFalse(result);
 }

 @Test
 public void testSetCandidateResourceRole_ValidArguments() {
 // given
 Long approvalInstanceId = 1L;
 String resourceRoleIdentifier = "identifier1";
 // when
 approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
 // then
 // verify
 Mockito.verify(userTaskCandidateRepository, Mockito.times(1)).save(Mockito.any(UserTaskInstanceCandidateEntity.class));
 }

 @Test
 public void testSetCandidateResourceRole_InvalidArguments() {
 // given
 Long approvalInstanceId = 3L;
 String resourceRoleIdentifier = null;
 // when, then
 Assertions.assertThrows(NullPointerException.class, () -> approvalPermissionService.setCandidateResourceRole(approvalInstanceId,
 resourceRoleIdentifier));
 }

 @Test
 public void testIsApprovableExternalInstances_ValidArguments() {
 // given
 // when
 List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
 // then
 Assertions.assertFalse(result.isEmpty());
 Assertions.assertEquals(2, result.size());
 }

 @Test
 public void testIsApprovableExternalInstances_EmptyList() {
 // given
 // when
 List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
 // then
 Assertions.assertTrue(result.isEmpty());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds_ValidArguments() {
 // given
 Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L, 3L);
 // when
 Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
 // then
 Assertions.assertFalse(result.isEmpty());
 Assertions.assertEquals(3, result.size());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds_EmptyList() {
 // given
 Collection<Long> flowInstanceIds = Collections.emptyList();
 // when
 Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
 // then
 Assertions.assertTrue(result.isEmpty());
 }

}