package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.oceanbase.odc.service.iam.model.Role;

@SuppressWarnings("unchecked")
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

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates_enabled() {
        Long approvalInstanceId = 1L;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);

        when(userService.getCurrentUserRoleIds())
                .thenReturn(Collections.singleton(1L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(List.of(entity1, entity2));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertFalse(candidates.isEmpty());
        assertTrue(candidates.contains(new User(1L, "Test User")));
        assertTrue(candidates.contains(new User(1L, "Test User")));

        verify(userRepository, times(1)).findByRoleIdsAndEnabled(Collections.singleton(1L), true);
    }

    @Test
    public void testGetCandidates_disabled() {
        Long approvalInstanceId = 1L;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);

        when(userService.getCurrentUserRoleIds())
                .thenReturn(Collections.singleton(1L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(List.of(entity1, entity2));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);

        assertFalse(candidates.isEmpty());
        assertTrue(candidates.contains(new User(1L, "Test User")));
        assertTrue(candidates.contains(new User(1L, "Test User")));

        verify(userRepository, times(1)).findByRoleIdsAndEnabled(Collections.singleton(1L), false);
    }

    @Test
    public void testGetCandidates_empty() {
        Long approvalInstanceId = 1L;

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Collections.emptyList());

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);

        assertTrue(candidates.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds_empty() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertEquals(result.size(), 0);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds_notEmpty() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceIds.get(0));
        entity1.setRoleId(1L);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceIds.get(0));
        entity2.setRoleId(2L);

        List<UserTaskInstanceCandidateEntity> entities = List.of(entity1, entity2);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertEquals(result.size(), 1);
        assertEquals(result.get(approvalInstanceIds.get(0)).size(), 2);
        assertTrue(result.get(approvalInstanceIds.get(0)).contains(1L));
        assertTrue(result.get(approvalInstanceIds.get(0)).contains(2L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceId2CandidateRoleIds_nullApprovalInstanceIds() {
        approvalPermissionService.getInstanceId2CandidateRoleIds(null);
    }

    @Test
    public void testGetCandidateUserIds_empty() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();

        Set<String> resourceRoleIdentifiers = new HashSet<>();
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(Collections.emptyList());

        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidateUserIds_singleUser() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceIds.get(0));
        entity1.setUserId(1L);

        List<UserTaskInstanceCandidateEntity> entities = List.of(entity1);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);

        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        assertEquals(result.size(), 1);
        assertEquals(result, Set.of(1L));
    }

    @Test
    public void testGetCandidateUserIds_multipleUsers() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);

        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceIds.get(0));
        entity1.setUserId(1L);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceIds.get(1));
        entity2.setUserId(2L);

        List<UserTaskInstanceCandidateEntity> entities = List.of(entity1, entity2);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);

        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        assertEquals(result.size(), 2);
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
    }

    @Test
    public void testGetCandidateRoles_empty() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidateRoles_singleRole() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setApprovalInstanceId(approvalInstanceId);
        entity.setRoleId(1L);

        List<UserTaskInstanceCandidateEntity> entities = List.of(entity);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), enabled))
                .thenReturn(List.of(new Role(1L, "Test Role")));

        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        assertEquals(result.size(), 1);
        assertEquals(result, List.of(new Role(1L, "Test Role")));
    }

    @Test
    public void testGetCandidateRoles_multipleRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setRoleId(1L);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(2L);

        List<UserTaskInstanceCandidateEntity> entities = List.of(entity1, entity2);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        when(roleRepository.findByRoleIdsAndEnabled(new HashSet<>(Arrays.asList(1L, 2L)), enabled))
                .thenReturn(List.of(new Role(1L, "Test Role 1"), new Role(2L, "Test Role 2")));

        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        assertEquals(result.size(), 2);
        assertEquals(result, List.of(new Role(1L, "Test Role 1"), new Role(2L, "Test Role 2")));
    }

    @Test
    public void testIsApprovable_approvableInstance() {
        Long approvalInstanceId = 1L;
        Long userId = 1L;
        Set<Long> roleIds = new HashSet<>();
        Set<String> resourceRoleIdentifiers = new HashSet<>();

        when(authenticationFacade.currentUserId()).thenReturn(userId);
        when(userService.getUserRoleIds(userId)).thenReturn(roleIds);
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId))
                .thenReturn(resourceRoleIdentifiers);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)
                .stream()
                .filter(entity -> entity.getId().equals(approvalInstanceId))
                .findFirst())
                .thenReturn(new UserTaskInstanceEntity());

        boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

        assertTrue(result);
    }

    @Test
    public void testIsApprovable_nonApprovableInstance() {
        Long approvalInstanceId = 1L;
        Long userId = 1L;
        Set<Long> roleIds = new HashSet<>();
        Set<String> resourceRoleIdentifiers = new HashSet<>();

        when(authenticationFacade.currentUserId()).thenReturn(userId);
        when(userService.getUserRoleIds(userId)).thenReturn(roleIds);
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId))
                .thenReturn(resourceRoleIdentifiers);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)
                .stream()
                .filter(entity -> entity.getId().equals(approvalInstanceId))
                .findFirst())
                .thenReturn(null);

        boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

        assertFalse(result);
    }

    @Test
    public void testSetCandidateResourceRole_success() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "Test Role Identifier";

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCandidateResourceRole_nullApprovalInstanceId() {
        approvalPermissionService.setCandidateResourceRole(null, "Test Role Identifier");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCandidateResourceRole_nullResourceRoleIdentifier() {
        Long approvalInstanceId = 1L;

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, null);
    }
}