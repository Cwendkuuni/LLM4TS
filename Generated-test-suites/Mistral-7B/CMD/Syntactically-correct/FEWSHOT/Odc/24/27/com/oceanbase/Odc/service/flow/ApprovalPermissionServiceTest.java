package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

public class ApprovalPermissionServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private ResourceRoleService resourceRoleService;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidatesEmpty() {
        Long approvalInstanceId = 1L;
        Boolean enabled = null;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertTrue(candidates.isEmpty());
    }

    @Test
    public void testGetCandidatesUserIds() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        when(userRepository.findByUserIdsAndEnabled(Collections.singletonList(1L), enabled)).thenReturn(Arrays.asList(
                new User(1L, "user1", "password1")));
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(1, candidates.size());
        User user = candidates.iterator().next();
        assertEquals("user1", user.getName());
    }

    @Test
    public void testGetCandidatesRoleIds() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), enabled)).thenReturn(Arrays.asList(
                new Role(1L, "role1")));
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(1, candidates.size());
        User user = candidates.iterator().next();
        assertEquals("role1", user.getName());
    }

    @Test
    public void testGetCandidatesBoth() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        when(userRepository.findByUserIdsAndEnabled(Collections.singletonList(1L), enabled)).thenReturn(Arrays.asList(
                new User(1L, "user1", "password1")));
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(2L), enabled)).thenReturn(Arrays.asList(
                new Role(2L, "role2")));
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(2, candidates.size());
        User user1 = candidates.iterator().next();
        assertEquals("user1", user1.getName());
        User user2 = candidates.iterator().next();
        assertEquals("role2", user2.getName());
    }

    @Test
    public void testGetCandidatesDisabled() {
        Long approvalInstanceId = 1L;
        Boolean enabled = false;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertTrue(candidates.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIdsEmpty() {
        Collection<Long> approvalInstanceIds = Collections.emptyList();
        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(
                approvalInstanceIds);
        assertTrue(instanceId2CandidateRoleIds.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIdsValid() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, 1L, null, null),
                new UserTaskInstanceCandidateEntity(1L, null, 2L, null),
                new UserTaskInstanceCandidateEntity(2L, null, null, 1L));
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);
        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(
                approvalInstanceIds);
        assertEquals(2, instanceId2CandidateRoleIds.size());
        Set<Long> roleIds1 = instanceId2CandidateRoleIds.get(1L);
        assertEquals(1, roleIds1.size());
        assertEquals(2L, roleIds1.iterator().next());
        Set<Long> roleIds2 = instanceId2CandidateRoleIds.get(2L);
        assertEquals(1, roleIds2.size());
        assertEquals(1L, roleIds2.iterator().next());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIdsEmpty() {
        Collection<Long> approvalInstanceIds = Collections.emptyList();
        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService
                .getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        assertTrue(instanceId2CandidateResourceRoleIdentifierIds.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIdsValid() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, null, null, "role1"),
                new UserTaskInstanceCandidateEntity(1L, null, null, "role2"),
                new UserTaskInstanceCandidateEntity(2L, null, null, "role3"));
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);
        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService
                .getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        assertEquals(3, instanceId2CandidateResourceRoleIdentifierIds.size());
        Set<String> resourceRoleIdentifierIds1 = instanceId2CandidateResourceRoleIdentifierIds.get(1L);
        assertEquals(2, resourceRoleIdentifierIds1.size());
        assertTrue(resourceRoleIdentifierIds1.contains("role1"));
        assertTrue(resourceRoleIdentifierIds1.contains("role2"));
        Set<String> resourceRoleIdentifierIds2 = instanceId2CandidateResourceRoleIdentifierIds.get(2L);
        assertEquals(1, resourceRoleIdentifierIds2.size());
        assertEquals("role3", resourceRoleIdentifierIds2.iterator().next());
    }

    @Test
    public void testGetCandidateUserIdsEmpty() {
        Collection<Long> approvalInstanceIds = Collections.emptyList();
        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertTrue(candidateUserIds.isEmpty());
    }

    @Test
    public void testGetCandidateUserIdsValid() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, 1L, 1L, null),
                new UserTaskInstanceCandidateEntity(1L, null, null, 1L),
                new UserTaskInstanceCandidateEntity(2L, 2L, 2L, null),
                new UserTaskInstanceCandidateEntity(2L, null, null, 2L),
                new UserTaskInstanceCandidateEntity(2L, null, null, 3L));
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);
        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertEquals(3, candidateUserIds.size());
        assertTrue(candidateUserIds.contains(1L));
        assertTrue(candidateUserIds.contains(2L));
        assertTrue(candidateUserIds.contains(3L));
    }

    @Test
    public void testGetCandidateRolesEmpty() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertTrue(candidateRoles.isEmpty());
    }

    @Test
    public void testGetCandidateRolesValid() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, 1L, null, null),
                new UserTaskInstanceCandidateEntity(1L, null, 1L, null));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertEquals(1, candidateRoles.size());
        Role role = candidateRoles.get(0);
        assertEquals("role1", role.getName());
    }

    @Test
    public void testIsApprovableEmpty() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceEntity> approvableApprovalInstances = Collections.emptyList();
        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testIsApprovableValid() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceEntity> approvableApprovalInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, 1L, "task1", FlowNodeStatus.CREATED, null, null, null));
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(approvableApprovalInstances);
        when(userTaskInstanceRepository.findById(approvalInstanceId)).thenReturn(Optional.of(new UserTaskInstanceEntity(
                1L, approvalInstanceId, "task1", FlowNodeStatus.CREATED, null, null, null)));
        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testIsApprovableInvalid() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceEntity> approvableApprovalInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, 1L, "task1", FlowNodeStatus.EXECUTING, null, null, null));
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(approvableApprovalInstances);
        when(userTaskInstanceRepository.findById(approvalInstanceId)).thenReturn(Optional.of(new UserTaskInstanceEntity(
                1L, approvalInstanceId, "task1", FlowNodeStatus.CREATED, null, null, null)));
        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "role1";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
        verify(userTaskCandidateRepository, times(1))
                .save(argThat(entity -> entity.getApprovalInstanceId() == approvalInstanceId
                        && entity.getResourceRoleIdentifier().equals(resourceRoleIdentifier)));
    }
}