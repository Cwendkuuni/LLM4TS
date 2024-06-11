import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.Collectors;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoJUnitRunner;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.AuthenticationFacade;
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
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

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

 @BeforeEach
 public void setup() {
 // Initialize mocks
 }

 @Test
 public void testGetCandidates_enabled_true() {
 // Given
 long approvalInstanceId = 1L;
 // When
 Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, true);
 // Then
 verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
 // ...other assertions
 }

 @Test
 public void testGetCandidates_enabled_false() {
 // Given
 long approvalInstanceId = 1L;
 // When
 Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, false);
 // Then
 verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
 // ...other assertions
 }

 @Test
 public void testGetCandidates_null_enabled() {
 // Given
 long approvalInstanceId = 1L;
 // When
 Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, null);
 // Then
 verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
 // ...other assertions
 }

 // Add more test cases for other methods in the class
}