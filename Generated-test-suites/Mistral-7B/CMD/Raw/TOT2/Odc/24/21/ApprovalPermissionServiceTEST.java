import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Collectors;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ApprovalPermissionService Test")
public class ApprovalPermissionServiceTest {

 @Autowired
 private ApprovalPermissionService approvalPermissionService;
 @Autowired
 private UserService userService;
 @Autowired
 private ResourceRoleService resourceRoleService;
 @Autowired
 private AuthenticationFacade authenticationFacade;
 @Autowired
 private UserResourceRoleRepository userResourceRoleRepository;

 @BeforeAll
 static void setup() {
 // Initialize test users and roles
 User user1 = new User("user1", "password1");
 User user2 = new User("user2", "password2");
 Role role1 = new Role("role1");
 Role role2 = new Role("role2");
 UserRole userRole1 = new UserRole(user1, role1);
 UserRole userRole2 = new UserRole(user2, role1);
 UserRole userRole3 = new UserRole(user1, role2);
 userService.saveUser(user1);
 userService.saveUser(user2);
 roleRepository.saveRole(role1);
 roleRepository.saveRole(role2);
 userRoleRepository.saveUserRole(userRole1);
 userRoleRepository.saveUserRole(userRole2);
 userRoleRepository.saveUserRole(userRole3);

 // Initialize test resource roles
 UserResourceRole userResourceRole1 = new UserResourceRole();
 userResourceRole1.setResourceId(1L);
 userResourceRole1.setResourceRoleIdentifier("resourceRole1");
 userResourceRoleRepository.save(userResourceRole1);

 UserResourceRole userResourceRole2 = new UserResourceRole();
 userResourceRole2.setResourceId(2L);
 userResourceRole2.setResourceRoleIdentifier("resourceRole2");
 userResourceRoleRepository.save(userResourceRole2);
 }

 @Test
 void testGetCandidates_EnabledTrue() {
 Long approvalInstanceId = 1L;
 Set<User> expectedCandidates = Set.of(user1, user2);
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertEquals(expectedCandidates, candidates);
 }

 @Test
 void testGetCandidates_EnabledFalse() {
 Long approvalInstanceId = 1L;
 Set<User> expectedCandidates = Set.of();
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
 assertEquals(expectedCandidates, candidates);
 }

 // Add more test cases here for other methods in ApprovalPermissionService, including edge cases and error scenarios.

 @AfterAll
 static void cleanup() {
 // Cleanup test data
 userService.deleteUser(user1.getId());
 userService.deleteUser(user2.getId());
 roleRepository.deleteRole(role1.getId());
 roleRepository.deleteRole(role2.getId());
 userRoleRepository.deleteUserRole(userRole1);
 userRoleRepository.deleteUserRole(userRole2);
 userRoleRepository.deleteUserRole(userRole3);
 userResourceRoleRepository.deleteAll();
 }
}