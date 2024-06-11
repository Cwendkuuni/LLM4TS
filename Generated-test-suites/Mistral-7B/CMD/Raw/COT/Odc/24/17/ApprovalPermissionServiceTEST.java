import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
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
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
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
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class ApprovalPermissionServiceTest {

 private ApprovalPermissionService approvalPermissionService;
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

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 approvalPermissionService = new ApprovalPermissionService();
 approvalPermissionService.setUserService(userService);
 approvalPermissionService.setResourceRoleService(resourceRoleService);
 approvalPermissionService.setAuthenticationFacade(authenticationFacade);
 approvalPermissionService.setUserTaskCandidateRepository(userTaskCandidateRepository);
 approvalPermissionService.setUserTaskInstanceRepository(userTaskInstanceRepository);
 approvalPermissionService.setUserRepository(userRepository);
 approvalPermissionService.setRoleRepository(roleRepository);
 approvalPermissionService.setUserRoleRepository(userRoleRepository);
 approvalPermissionService.setUserResourceRoleRepository(userResourceRoleRepository);
 }

 @Test
 public void testGetCandidates_enabledTrue() {
 Long approvalInstanceId = 1L;
 // given
 UserTaskInstanceCandidateEntity candidateEntity1 = new UserTaskInstanceCandidateEntity();
 candidateEntity1.setApprovalInstanceId(approvalInstanceId);
 candidateEntity1.setRoleId(1L);
 UserTaskInstanceCandidateEntity candidateEntity2 = new UserTaskInstanceCandidateEntity();
 candidateEntity2.setApprovalInstanceId(approvalInstanceId);
 candidateEntity2.setUserId(1L);
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(candidateEntity1, candidateEntity2);
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

 // when
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

 // then
 assertEquals(2, candidates.size());
 verify(userRepository).findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
 }

 @Test
 public void testGetCandidates_enabledFalse() {
 Long approvalInstanceId = 1L;
 // given
 UserTaskInstanceCandidateEntity candidateEntity1 = new UserTaskInstanceCandidateEntity();
 candidateEntity1.setApprovalInstanceId(approvalInstanceId);
 candidateEntity1.setRoleId(1L);
 UserTaskInstanceCandidateEntity candidateEntity2 = new UserTaskInstanceCandidateEntity();
 candidateEntity2.setApprovalInstanceId(approvalInstanceId);
 candidateEntity2.setUserId(1L);
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(candidateEntity1, candidateEntity2);
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

 // when
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);

 // then
 assertEquals(1, candidates.size());
 verify(userRepository, never()).findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
 }

 @Test
 public void testGetCandidates_enabledNull() {
 Long approvalInstanceId = 1L;
 // given
 UserTaskInstanceCandidateEntity candidateEntity1 = new UserTaskInstanceCandidateEntity();
 candidateEntity1.setApprovalInstanceId(approvalInstanceId);
 candidateEntity1.setRoleId(1L);
 UserTaskInstanceCandidateEntity candidateEntity2 = new UserTaskInstanceCandidateEntity();
 candidateEntity2.setApprovalInstanceId(approvalInstanceId);
 candidateEntity2.setUserId(1L);
 List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(candidateEntity1, candidateEntity2);
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

 // when
 Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);

 // then
 assertEquals(2, candidates.size());
 verify(userRepository, atLeastOnce()).findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
 verify(userRepository, atLeastOnce()).findByRoleIdsAndEnabled(Collections.singletonList(1L), false);
 }

 // ... (add test cases for other methods)

}