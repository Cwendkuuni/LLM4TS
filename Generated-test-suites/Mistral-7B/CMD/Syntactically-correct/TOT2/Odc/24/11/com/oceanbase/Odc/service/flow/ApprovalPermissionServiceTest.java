import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataSourceTransactionManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ALL)
@DataSourceTransactionManager
@ActiveProfiles("test")
@SpringJUnitConfig(classes = ApprovalPermissionService.class)
public class ApprovalPermissionServiceTest {

 @Autowired
 private ApprovalPermissionService approvalPermissionService;
 @Autowired
 private UserService userService;
 @Autowired
 private RoleRepository roleRepository;
 @Autowired
 private UserRepository userRepository;
 @Autowired
 private UserRoleRepository userRoleRepository;
 @Autowired
 private UserResourceRoleRepository userResourceRoleRepository;
 @Autowired
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;
 @Autowired
 private ResourceRoleService resourceRoleService;
 @Autowired
 private AuthenticationFacade authenticationFacade;

 @BeforeEach
 void setup() {
 userService.init();
 }

 @Test
 void testGetCandidates_ValidScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity()
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertEquals(3, candidates.size());
 }

 @Test
 void testGetCandidates_EmptyScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Collections.emptyList();
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertTrue(candidates.isEmpty());
 }

 @Test
 void testGetCandidates_RoleScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity(1L, 1L, null)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Role role = new Role();
 role.setId(1L);
 role.setName("TEST_ROLE");
 when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), true)).thenReturn(Collections.singletonList(role));
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertEquals(1, candidates.size());
 }

 @Test
 void testGetCandidates_UserScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(null, null, 1L),
 new UserTaskInstanceCandidateEntity(null, null, 2L),
 new UserTaskInstanceCandidateEntity(null, null, 3L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 User user = new User();
 user.setId(1L);
 user.setName("TEST_USER");
 when(userRepository.findByUserIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true))
 .thenReturn(Arrays.asList(user, null, null));
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertEquals(1, candidates.size());
 }

 @Test
 void testGetCandidates_MixedScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity(1L, 1L, null),
 new UserTaskInstanceCandidateEntity(null, null, 3L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Role role = new Role();
 role.setId(1L);
 role.setName("TEST_ROLE");
 when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), true)).thenReturn(Collections.singletonList(role));
 User user = new User();
 user.setId(1L);
 user.setName("TEST_USER");
 when(userRepository.findByUserIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true))
 .thenReturn(Arrays.asList(user, null, null));
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertEquals(2, candidates.size());
 }

 @Test
 void testGetCandidates_DisabledScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity(1L, 1L, null),
 new UserTaskInstanceCandidateEntity(null, null, 3L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Role role = new Role();
 role.setId(1L);
 role.setName("TEST_ROLE");
 when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), false)).thenReturn(Collections.singletonList(role));
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
 assertTrue(candidates.isEmpty());
 }

 @Test
 void testGetCandidates_EmptyRoleScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity(1L, 1L, null),
 new UserTaskInstanceCandidateEntity(null, null, 3L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), true)).thenReturn(Collections.emptyList());
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertTrue(candidates.isEmpty());
 }

 @Test
 void testGetCandidates_EmptyUserScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity(1L, 1L, null),
 new UserTaskInstanceCandidateEntity(null, null, 3L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 when(userRepository.findByUserIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true)).thenReturn(Collections.emptyList());
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertTrue(candidates.isEmpty());
 }

 @Test
 void testGetCandidates_WholeScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity(1L, 1L, null),
 new UserTaskInstanceCandidateEntity(null, null, 3L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 Role role = new Role();
 role.setId(1L);
 role.setName("TEST_ROLE");
 when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L), true))
 .thenReturn(Arrays.asList(role));
 User user = new User();
 user.setId(1L);
 user.setName("TEST_USER");
 when(userRepository.findByUserIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true))
 .thenReturn(Arrays.asList(user, null, null));
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Arrays.asList(1L), Collections.singletonList("TEST_ROLE")))
 .thenReturn(Collections.singletonList(new UserResourceRoleEntity()));
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertEquals(1, candidates.size());
 }

 @ParameterizedTest
 @MethodSource("getInvalidScenarios")
 void testGetCandidates_InvalidScenarios(Long approvalInstanceId, boolean enabled) {
 List<UserTaskInstanceCandidateEntity> entities = Collections.emptyList();
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(entities);
 assertThrows(IllegalArgumentException.class, () -> approvalPermissionService.getCandidates(approvalInstanceId, enabled));
 }

 private static List<Object[]> getInvalidScenarios() {
 return Arrays.asList(new Object[]{-1L, true}, new Object[]{null, true}, new Object[]{1L, null});
 }

 @Test
 void testGetInstanceId2CandidateRoleIds_ValidScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity(1L, null, null),
 new UserTaskInstanceCandidateEntity(null, 1L, null),
 new UserTaskInstanceCandidateEntity(null, null, 2L)
 );
 when(userTaskCandidateRepository.findByApprovalInstanceIds(Arrays.asList(approvalInstanceId)))
 .thenReturn(entities);
 Map<Long, Set<Long>> actual = approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(approvalInstanceId));
 Map<Long, Set<Long>> expected = new HashMap<>();
 expected.put(approvalInstanceId, new HashSet<>(Arrays.asList(1L, 2L)));
 assertEquals(expected, actual);
 }

 @Test
 void testGetInstanceId2CandidateResourceRoleIdentifierIds_ValidScenario() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
 new UserTaskInstanceCandidateEntity(),
 new UserTaskInstanceCandidateEntity(1L, null, null),
 new UserTaskInstanceCandidateEntity(null, 1L, "TEST_ROLE_1"),
 new UserTaskInstanceCandidateEntity(null, null, 2L, "TEST_ROLE_2")
 );
 when(userTaskCandidateRepository.findByApprovalInstanceIds(Arrays.asList(approvalInstanceId)))
 .thenReturn(entities);
 Map<Long, Set<String>> actual = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
 Arrays.asList(approvalInstanceId));
 Map<Long, Set<String>> expected = new HashMap<>();
 expected.put(approvalInstanceId, new HashSet<>(Arrays.asList("TEST_ROLE_1", "TEST_ROLE_2")));
 assertEquals(expected, actual);
 }

 // Add more test cases for other methods here

 @AfterEach
 void teardown() {
 authenticationFacade.logout();
 }

}