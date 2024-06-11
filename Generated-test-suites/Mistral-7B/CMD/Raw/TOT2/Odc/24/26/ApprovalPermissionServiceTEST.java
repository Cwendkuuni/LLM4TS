import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.UserRole;
import com.oceanbase.odc.service.iam.resourcerole.UserResourceRole;
import com.oceanbase.odc.service.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.service.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.iam.UserRepository;
import com.oceanbase.odc.service.iam.RoleRepository;
import com.oceanbase.odc.service.iam.UserRoleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ApprovalPermissionServiceTest {

 private ApprovalPermissionService approvalPermissionService;
 private UserTaskInstanceRepository userTaskInstanceRepository;
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
 private UserService userService;
 private RoleRepository roleRepository;
 private UserRepository userRepository;
 private UserRoleRepository userRoleRepository;
 private UserResourceRoleRepository userResourceRoleRepository;
 private ResourceRoleService resourceRoleService;
 private AuthenticationFacade authenticationFacade;

 @SuppressWarnings("unchecked")
 @BeforeEach
 public void setup() {
 approvalPermissionService = new ApprovalPermissionService();
 userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);
 userTaskCandidateRepository = Mockito.mock(UserTaskInstanceCandidateRepository.class);
 userService = Mockito.mock(UserService.class);
 roleRepository = Mockito.mock(RoleRepository.class);
 userRepository = Mockito.mock(UserRepository.class);
 userRoleRepository = Mockito.mock(UserRoleRepository.class);
 userResourceRoleRepository = Mockito.mock(UserResourceRoleRepository.class);
 resourceRoleService = Mockito.mock(ResourceRoleService.class);
 authenticationFacade = Mockito.mock(AuthenticationFacade.class);

 OngoingStubbing<UserTaskInstanceRepository> userTaskInstanceRepositoryStub = Mockito.doReturn(userTaskInstanceRepository);
 approvalPermissionService = new ApprovalPermissionService() {
 {
 this.userTaskInstanceRepository = userTaskInstanceRepositoryStub.get();
 this.userTaskCandidateRepository = userTaskCandidateRepository;
 this.userService = userService;
 this.roleRepository = roleRepository;
 this.userRepository = userRepository;
 this.userRoleRepository = userRoleRepository;
 this.userResourceRoleRepository = userResourceRoleRepository;
 this.resourceRoleService = resourceRoleService;
 this.authenticationFacade = authenticationFacade;
 }
 };
 }

 @Test
 public void testGetCandidates() {
 // Test with null approvalInstanceId and enabled set to null
 Long approvalInstanceId = null;
 Boolean enabled = null;
 Set<User> expectedUsers = Collections.emptySet();
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(Collections.emptyList());
 Set<User> resultUsers = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Assertions.assertEquals(expectedUsers, resultUsers);

 // Test with an empty list of UserTaskInstanceCandidateEntities
 approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Collections.emptyList();
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Set<User> expectedUsers2 = Collections.emptySet();
 Set<User> resultUsers2 = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Assertions.assertEquals(expectedUsers2, resultUsers2);

 // Test with a list of UserTaskInstanceCandidateEntities containing only user candidates
 User user = new User();
 user.setId(1L);
 entities = Arrays.asList(new UserTaskInstanceCandidateEntity(), new UserTaskInstanceCandidateEntity().setUserId(user.getId()));
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Set<User> expectedUsers3 = new HashSet<>(Arrays.asList(user));
 Set<User> resultUsers3 = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Assertions.assertEquals(expectedUsers3, resultUsers3);

 // Test with a list of UserTaskInstanceCandidateEntities containing only role candidates
 UserRole role = new UserRole();
 role.setId(1L);
 UserRoleEntity userRoleEntity = new UserRoleEntity();
 userRoleEntity.setId(1L);
 userRoleEntity.setRole(role);
 Set<Long> roleIds = new HashSet<>(Collections.singleton(role.getId()));
 List<UserTaskInstanceCandidateEntity> entities2 = Arrays.asList(new UserTaskInstanceCandidateEntity().setRoleId(roleIds),
 new UserTaskInstanceCandidateEntity());
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities2);
 Set<User> expectedUsers4 = new HashSet<>();
 UserEntity userEntity = new UserEntity();
 userEntity.setId(1L);
 userEntity.setRoleIds(roleIds);
 expectedUsers4.add(new User(userEntity));
 Set<User> resultUsers4 = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Assertions.assertEquals(expectedUsers4, resultUsers4);

 // Test with a list of UserTaskInstanceCandidateEntities containing both user and role candidates
 entities2.get(1).setUserId(user.getId());
 expectedUsers4.add(user);
 resultUsers4 = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Assertions.assertEquals(expectedUsers4, resultUsers4);

 // Test with enabled set to false
 enabled = false;
 expectedUsers2 = new HashSet<>();
 Mockito.when(userRepository.findByRoleIdsAndEnabled(roleIds, false))
 .thenReturn(new ArrayList<>());
 resultUsers2 = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Assertions.assertEquals(expectedUsers2, resultUsers2);

 // Test with enabled set to true
 resultUsers2 = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 Assertions.assertEquals(expectedUsers4, resultUsers2);
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 // Test with an empty list of approval instance ids
 Set<Long> approvalInstanceIds = Collections.emptySet();
 Map<Long, Set<Long>> expectedResult = new HashMap<>();
 Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
 Assertions.assertEquals(expectedResult, result);

 // Test with a non-empty list of approval instance ids
 approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
 UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
 entity.setApprovalInstanceId(1L);
 entity.setRoleId(1L);
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(entity, new UserTaskInstanceCandidateEntity().setApprovalInstanceId(2L)
 .setRoleId(2L));
 Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(entities);
 Set<Long> roleIds1 = new HashSet<>(Arrays.asList(1L));
 Set<Long> roleIds2 = new HashSet<>(Arrays.asList(2L));
 Map<Long, Set<Long>> expectedResult2 = new HashMap<>();
 expectedResult2.put(1L, roleIds1);
 expectedResult2.put(2L, roleIds2);
 Map<Long, Set<Long>> result2 = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
 Assertions.assertEquals(expectedResult2, result2);
 }

 // Test other methods here with similar test cases
}