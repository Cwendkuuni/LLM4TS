import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.AuthenticationFacade;
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
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

public class ApprovalPermissionServiceTest {

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
 @Mock
 private AuthenticationFacade authenticationFacade;
 @Mock
 private UserService userService;
 @Mock
 private ResourceRoleService resourceRoleService;

 @InjectMocks
 private ApprovalPermissionService approvalPermissionService;

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule();

 @Before
 public void setup() {
 // Initialize mocks
 when(userRepository.findByUserIds(anySet()))
 .then(invocation -> {
 Collection<UserEntity> users = new ArrayList<>();
 UserEntity user1 = new UserEntity();
 user1.setId(1L);
 users.add(user1);
 return users;
 });
 when(userRoleRepository.findByRoleIds(anySet()))
 .then(invocation -> {
 Collection<UserRoleEntity> userRoles = new ArrayList<>();
 UserRoleEntity userRole1 = new UserRoleEntity();
 userRole1.setId(1L);
 userRole1.setUserId(1L);
 userRoles.add(userRole1);
 return userRoles;
 });
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
 .then(invocation -> {
 Collection<UserResourceRoleEntity> userResourceRoles = new ArrayList<>();
 UserResourceRoleEntity userResourceRole1 = new UserResourceRoleEntity();
 userResourceRole1.setId(1L);
 userResourceRole1.setUserId(1L);
 userResourceRoles.add(userResourceRole1);
 return userResourceRoles;
 });
 }

 @Test
 public void testGetCandidates() {
 // Test with null enabled
 Long approvalInstanceId = 1L;
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
 assertEquals(0, candidates.size());
 // Test with true enabled
 candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
 assertEquals(1, candidates.size());
 // Test with false enabled
 candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
 assertEquals(0, candidates.size());
 }

 // Add more test cases for other methods here

}