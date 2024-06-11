import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.service.iam.model.UserRole;
import com.oceanbase.odc.service.iam.model.UserResourceRole;

public class ApprovalPermissionServiceTest {

 @Mock
 private AuthenticationFacade authenticationFacade;
 @Mock
 private UserService userService;
 @Mock
 private ResourceRoleService resourceRoleService;
 @Mock
 private UserTaskInstanceRepository userTaskInstanceRepository;
 @Mock
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
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

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetCandidates() {
 // Given
 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 when(userService.getCurrentUserRoleIds())
 .thenReturn(new HashSet<>(Arrays.asList(1L, 2L)));
 when(resourceRoleService.getResourceRoleIdentifiersByUserId(anyLong()))
 .thenReturn(new HashSet<>(Arrays.asList("role1:id1", "role2:id2")));
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity()));
 when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
 .thenReturn(Arrays.asList(new UserEntity()).stream().map(User::new).collect(Collectors.toList()));
 when(userRepository.findByUserIdsAndEnabled(anySet(), anyBoolean()))
 .thenReturn(Arrays.asList(new UserEntity()).stream().map(User::new).collect(Collectors.toList()));
 when(userRepository.findByUserIdsOrRoleIds(anySet(), anySet(), anyBoolean()))
 .thenReturn(Arrays.asList(new UserEntity()).stream().map(User::new).collect(Collectors.toList()));

 // When
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

 // Then
 assertEquals(1, candidates.size());
 // Add more assertions based on the actual implementation and expected results
 }

 // ... other test methods for all the methods in ApprovalPermissionService

}