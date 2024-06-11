import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ApprovalPermissionServiceTest {

 private ApprovalPermissionService approvalPermissionService;
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
 private UserTaskInstanceRepository userTaskInstanceRepository;
 private UserRepository userRepository;
 private RoleRepository roleRepository;
 private UserRoleRepository userRoleRepository;
 private UserResourceRoleRepository userResourceRoleRepository;
 private UserService userService;
 private ResourceRoleService resourceRoleService;
 private AuthenticationFacade authenticationFacade;

 @Autowired
 public void setUpMocks(ApprovalPermissionService approvalPermissionService, UserTaskInstanceCandidateRepository userTaskCandidateRepository,
                       UserTaskInstanceRepository userTaskInstanceRepository, UserRepository userRepository, RoleRepository roleRepository,
                       UserRoleRepository userRoleRepository, UserResourceRoleRepository userResourceRoleRepository,
                       UserService userService, ResourceRoleService resourceRoleService, AuthenticationFacade authenticationFacade) {
 this.approvalPermissionService = approvalPermissionService;
 this.userTaskCandidateRepository = userTaskCandidateRepository;
 this.userTaskInstanceRepository = userTaskInstanceRepository;
 this.userRepository = userRepository;
 this.roleRepository = roleRepository;
 this.userRoleRepository = userRoleRepository;
 this.userResourceRoleRepository = userResourceRoleRepository;
 this.userService = userService;
 this.resourceRoleService = resourceRoleService;
 this.authenticationFacade = authenticationFacade;
 }

 @BeforeEach
 public void setUp() {
 // Initialize mocks
 Mockito.when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
 .thenReturn(Collections.singletonList(new UserEntity()));
 Mockito.when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
 .thenReturn(Collections.singletonList(new Role()));
 Mockito.when(userService.getCurrentUserRoleIds())
 .thenReturn(Collections.singleton(1L));
 Mockito.when(resourceRoleService.getResourceRoleIdentifiersByUserId(anyLong()))
 .thenReturn(Collections.singletonList("roleIdentifier1"));
 Mockito.when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anyList()))
 .thenReturn(Collections.singletonList(new UserResourceRoleEntity()));

 // Initialize test data
 userTaskCandidateRepository.save(new UserTaskInstanceCandidateEntity());
 userTaskCandidateRepository.save(new UserTaskInstanceCandidateEntity());
 userTaskInstanceRepository.save(new UserTaskInstanceEntity());
 userTaskInstanceRepository.save(new UserTaskInstanceEntity());
 userRepository.save(new UserEntity());
 roleRepository.save(new Role());
 userRoleRepository.save(new UserRoleEntity());
 userResourceRoleRepository.save(new UserResourceRoleEntity());
 }

 @Test
 public void testGetCandidates() {
 Long approvalInstanceId = 1L;
 Boolean enabled = true;

 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

 assertTrue(candidates.isEmpty() || candidates.contains(new UserEntity()));
 }

 @Test
 public void testGetCandidates_EmptyList() {
 Long approvalInstanceId = 1L;
 Boolean enabled = null;

 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

 assertTrue(candidates.isEmpty());
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 Set<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

 Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

 assertEquals(2, instanceId2CandidateRoleIds.size());
 assertEquals(1, instanceId2CandidateRoleIds.get(1L).size());
 assertEquals(1, instanceId2CandidateRoleIds.get(2L).size());
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds_EmptyList() {
 Set<Long> approvalInstanceIds = Collections.emptySet();

 Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

 assertTrue(instanceId2CandidateRoleIds.isEmpty());
 }

 @Test
 public void testGetCandidateUserIds() {
 Set<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

 Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

 assertEquals(2, candidateUserIds.size());
 }

 @Test
 public void testGetCandidateUserIds_EmptyList() {
 Set<Long> approvalInstanceIds = Collections.emptySet();

 Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

 assertTrue(candidateUserIds.isEmpty());
 }

 @Test
 public void testGetCandidateRoles() {
 Long approvalInstanceId = 1L;
 Boolean enabled = true;

 List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

 assertTrue(candidateRoles.isEmpty() || candidateRoles.contains(new Role()));
 }

 @Test
 public void testIsApprovable() {
 Long approvalInstanceId = 1L;

 assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
 }

 @Test
 public void testIsApprovable_False() {
 Long approvalInstanceId = 2L;
 Set<Long> approvableApprovalInstances = new HashSet<>(Arrays.asList(1L));

 ReflectionTestUtils.setFieldValue(approvalPermissionService, "approvableApprovalInstances", approvableApprovalInstances);

 assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
 }

 @Test
 public void testListApprovableExternalInstances() {
 List<UserTaskInstanceEntity> approvableExternalInstances = approvalPermissionService.listApprovableExternalInstances();

 assertFalse(approvableExternalInstances.isEmpty());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 Set<Long> flowInstanceIds = Arrays.asList(1L, 2L);

 Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

 assertEquals(2, candidatesByFlowInstanceIds.size());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds_EmptyList() {
 Set<Long> flowInstanceIds = Collections.emptySet();

 Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

 assertTrue(candidatesByFlowInstanceIds.isEmpty());
 }

}