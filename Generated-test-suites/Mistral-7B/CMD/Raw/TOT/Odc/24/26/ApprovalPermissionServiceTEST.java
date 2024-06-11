import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.lang.Pair;
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
import com.oceanbase.odc.service.iam.UserEntity;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ApprovalPermissionServiceTest {

  @Mock private AuthenticationFacade authenticationFacade;
  @Mock private UserService userService;
  @Mock private ResourceRoleService resourceRoleService;
  @Mock private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
  @Mock private UserTaskInstanceRepository userTaskInstanceRepository;
  @Mock private UserRepository userRepository;
  @Mock private RoleRepository roleRepository;
  @Mock private UserRoleRepository userRoleRepository;
  @Mock private UserResourceRoleRepository userResourceRoleRepository;

  @InjectMocks private ApprovalPermissionService approvalPermissionService;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetCandidates_enabledTrue_returnUsersAndRoles() {
    // given
    long approvalInstanceId = 1L;
    List<Pair<String, Long>> usersAndRoles = Arrays.asList(new Pair<>("ROLE", 1L), new Pair("USER", 2L));
    Map<String, Set<Long>> candidates = new HashMap<>();
    candidates.putAll(usersAndRoles.stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue)));

    when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
            .thenReturn(new ArrayList<>(Collections.singletonList(new UserTaskInstanceCandidateEntity())))
            .thenReturn(new ArrayList<>());

    when(userRepository.findByRoleIdsAndEnabled(candidates.get(Role.ROLE), true)).thenReturn(
            Arrays.asList(new UserEntity()));
    when(userRepository.findByUserIdsAndEnabled(candidates.get(User.USER), true)).thenReturn(
            Arrays.asList(new UserEntity()));

    // when
    Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, true);

    // then
    assertEquals(2, users.size());
    verify(userRepository, times(1)).findByRoleIdsAndEnabled(candidates.get(Role.ROLE), true);
    verify(userRepository, times(1)).findByUserIdsAndEnabled(candidates.get(User.USER), true);
  }

  @Test
  public void testGetCandidates_enabledFalse_returnRoles() {
    // given
    long approvalInstanceId = 1L;
    List<Pair<String, Long>> usersAndRoles = Arrays.asList(new Pair("ROLE", 1L));
    Map<String, Set<Long>> candidates = new HashMap<>();
    candidates.putAll(usersAndRoles.stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue)));

    when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
            .thenReturn(new ArrayList<>(Collections.singletonList(new UserTaskInstanceCandidateEntity())))
            .thenReturn(new ArrayList<>());

    when(userRepository.findByRoleIdsAndEnabled(candidates.get(Role.ROLE), false)).thenReturn(
            Arrays.asList(new UserEntity()));

    // when
    Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, false);

    // then
    assertEquals(1, users.size());
    verify(userRepository, times(1)).findByRoleIdsAndEnabled(candidates.get(Role.ROLE), false);
  }

  @Test
  public void testGetCandidates_enabledNull_returnUsersAndRoles() {
    // given
    long approvalInstanceId = 1L;
    List<Pair<String, Long>> usersAndRoles = Arrays.asList(new Pair("ROLE", 1L), new Pair("USER", 2L));
    Map<String, Set<Long>> candidates = new HashMap<>();
    candidates.putAll(usersAndRoles.stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue)));

    when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
            .thenReturn(new ArrayList<>(Collections.singletonList(new UserTaskInstanceCandidateEntity())))
            .thenReturn(new ArrayList<>());

    when(userRepository.findByRoleIdsAndEnabled(candidates.get(Role.ROLE), null))
            .thenReturn(Arrays.asList(new UserEntity()));
    when(userRepository.findByUserIdsAndEnabled(candidates.get(User.USER), null))
            .thenReturn(Arrays.asList(new UserEntity()));

    // when
    Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, null);

    // then
    assertEquals(2, users.size());
    verify(userRepository, times(1)).findByRoleIdsAndEnabled(candidates.get(Role.ROLE), null);
    verify(userRepository, times(1)).findByUserIdsAndEnabled(candidates.get(User.USER), null);
  }

  @Test
  public void testGetCandidateRoles_returnRoles() {
    // given
    long approvalInstanceId = 1L;
    List<Pair<String, Long>> usersAndRoles = Arrays.asList(new Pair("ROLE", 1L));
    Map<String, Set<Long>> candidates = new HashMap<>();
    candidates.putAll(usersAndRoles.stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue)));

    List<Role> roles = Arrays.asList(new Role());

    when(roleRepository.findByRoleIdsAndEnabled(candidates.get(Role.ROLE), true)).thenReturn(roles);

    // when
    List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);

    // then
    assertEquals(roles, result);
    verify(roleRepository, times(1)).findByRoleIdsAndEnabled(candidates.get(Role.ROLE), true);
  }

  @Test
  public void testIsApprovable_returnTrue() {
    // given
    long approvalInstanceId = 1L;

    long userId = 1L;
    Set<Long> roleIds = new HashSet<>(Arrays.asList(1L));
    Set<String> resourceRoleIdentifiers = new HashSet<>(Arrays.asList("resourceRoleIdentifier1"));

    when(authenticationFacade.currentUserId()).thenReturn(userId);
    when(userService.getCurrentUserRoleIds()).thenReturn(roleIds);
    when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(resourceRoleIdentifiers);

    when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(
            Arrays.asList(new UserTaskInstanceEntity()));

    // when
    boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

    // then
    assertTrue(result);
  }

  @Test
  public void testIsApprovable_returnFalse() {
    // given
    long approvalInstanceId = 1L;

    long userId = 1L;
    Set<Long> roleIds = new HashSet<>();
    Set<String> resourceRoleIdentifiers = new HashSet<>();

    when(authenticationFacade.currentUserId()).thenReturn(userId);
    when(userService.getCurrentUserRoleIds()).thenReturn(roleIds);
    when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(resourceRoleIdentifiers);

    // when
    boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

    // then
    assertFalse(result);
  }

  @Test
  public void testGetApprovableApprovalInstances_returnApprovableInstances() {
    // given
    long userId = 1L;
    List<UserTaskInstanceEntity> approvableInstances = Arrays.asList(new UserTaskInstanceEntity());

    when(userService.getUserRoleIds(userId)).thenReturn(Collections.singleton(1L));
    when(userService.getUserResourceRoleIdentifiers(userId)).thenReturn(Collections.singleton("resourceRoleIdentifier"));
    when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, Arrays.asList(1L)))
            .thenReturn(approvableInstances);

    // when
    List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);

    // then
    assertEquals(approvableInstances, result);
  }

  @Test
  public void testGetApprovableApprovalInstances_returnEmptyList() {
    // given
    long userId = 1L;

    when(userService.getUserRoleIds(userId)).thenReturn(Collections.emptySet());
    when(userService.getUserResourceRoleIdentifiers(userId)).thenReturn(Collections.emptySet());

    // when
    List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);

    // then
    assertTrue(result.isEmpty());
  }

  @Test
  public void testSetCandidateResourceRole_saveCandidate() {
    // given
    long approvalInstanceId = 1L;
    String resourceRoleIdentifier = "resourceRoleIdentifier";

    // when
    approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

    // then
    verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
  }

  @Test
  public void testListApprovableExternalInstances_returnApprovableInstances() {
    // given
    List<UserTaskInstanceEntity> approvableInstances = Arrays.asList(new UserTaskInstanceEntity());

    when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(approvableInstances);

    // when
    List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();

    // then
    assertEquals(approvableInstances, result);
  }

  @Test
  public void testGetCandidatesByFlowInstanceIds_returnCandidates() {
    // given
    List<Long> flowInstanceIds = Arrays.asList(1L);
    Map<Long, Set<UserEntity>> instanceId2Candidates = new HashMap<>();
    instanceId2Candidates.put(1L, new HashSet<>(Arrays.asList(new UserEntity())));

    // when
    Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

    // then
    assertEquals(instanceId2Candidates, result);
  }

  @Test
  public void testGetCandidatesByFlowInstanceIds_returnEmptyMap() {
    // given
    List<Long> flowInstanceIds = Arrays.asList(1L);

    // when
    Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

    // then
    assertTrue(result.isEmpty());
  }
}