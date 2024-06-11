package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Set<UserEntity> expectedUsers = new HashSet<>();
        UserEntity userEntity1 = mock(UserEntity.class);
        UserEntity userEntity2 = mock(UserEntity.class);
        expectedUsers.add(userEntity1);
        expectedUsers.add(userEntity2);

        when(userRepository.findByUserIds(expectedUsers)).thenReturn(new ArrayList<>(expectedUsers));

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity()));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity()));

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(expectedUsers.size(), actualUsers.size());
        for (User user : actualUsers) {
            assertEquals(userEntity1.getId(), user.getId());
            assertEquals(userEntity2.getId(), user.getId());
        }
    }

    @Test
    public void testGetCandidates_withUserAndRole() {
        Long approvalInstanceId = 1L;
        Set<UserEntity> users = new HashSet<>();
        UserEntity userEntity1 = mock(UserEntity.class);
        UserEntity userEntity2 = mock(UserEntity.class);
        users.add(userEntity1);

        Set<Role> roles = new HashSet<>();
        Role role1 = mock(Role.class);
        roles.add(role1);

        when(userRepository.findByUserIds(users)).thenReturn(new ArrayList<>(users));
        when(roleRepository.findByRoleIdsAndEnabled(roles, true)).thenReturn(new ArrayList<>(roles));

        when(userService.getCurrentUserRoleIds())
                .thenReturn(new HashSet<>(Arrays.asList(role1.getId().longValue())));
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity()));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity()));

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(expectedUsers.size(), actualUsers.size());
        for (User user : actualUsers) {
            assertEquals(userEntity1.getId(), user.getId());
            assertEquals(userEntity2.getId(), user.getId());
        }
    }

    @Test
    public void testGetCandidates_withOnlyRole() {
        Long approvalInstanceId = 1L;
        Set<Role> roles = new HashSet<>();
        Role role1 = mock(Role.class);
        roles.add(role1);

        when(roleRepository.findByRoleIdsAndEnabled(roles, true)).thenReturn(new ArrayList<>(roles));

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Collections.emptyList());

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertNotNull(actualUsers);
    }

    @Test
    public void testGetCandidates_withOnlyUser() {
        Long approvalInstanceId = 1L;
        Set<UserEntity> users = new HashSet<>();
        UserEntity userEntity1 = mock(UserEntity.class);
        users.add(userEntity1);

        when(userRepository.findByUserIds(users)).thenReturn(new ArrayList<>(users));

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Collections.emptyList());

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(expectedUsers.size(), actualUsers.size());
        assertNotNull(actualUsers);
    }

    @Test
    public void testGetCandidates_empty() {
        Long approvalInstanceId = 1L;

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Collections.emptyList());

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(0, actualUsers.size());
        assertNotNull(actualUsers);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);

        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity(), new UserTaskInstanceCandidateEntity());

        Map<Long, Set<Long>> expectedMap = new HashMap<>();
        expectedMap.put(1L, new HashSet<>(Arrays.asList(1L, 2L)));
        expectedMap.put(2L, new HashSet<>(Arrays.asList(3L)));

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);

        Map<Long, Set<Long>> actualMap = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetCandidateUserIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);

        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity(), new UserTaskInstanceCandidateEntity());

        Set<Long> userIds = new HashSet<>(Arrays.asList(1L, 2L));
        when(userRoleRepository.findByRoleIdIn(new HashSet<>(Arrays.asList(1L, 2L)))).thenReturn(Collections.emptyList());
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(
                Arrays.asList(1L, 2L), Arrays.asList("role1", "role2"))).thenReturn(Collections.emptyList());

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);

        Set<Long> actualUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertEquals(userIds, actualUserIds);
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));

        List<Role> expectedRoles = Arrays.asList(mock(Role.class), mock(Role.class));

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(expectedRoles);

        List<Role> actualRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        Long userId = 1L;

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(mock(UserTaskInstanceEntity.class),
                mock(UserTaskInstanceEntity.class));

        Set<Long> approvableApprovalInstancesIds = new HashSet<>(Arrays.asList(approvalInstanceId));
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(userTaskInstanceEntities);
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(Collections.singletonList(
                userTaskInstanceEntities.get(0)));
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, approvableApprovalInstancesIds))
                .thenReturn(Collections.singletonList(userTaskInstanceEntities.get(1)));

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testIsApprovable_false() {
        Long approvalInstanceId = 1L;
        Long userId = 1L;

        Set<Long> approvableApprovalInstancesIds = new HashSet<>();
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(Collections.emptyList());
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(Collections.emptyList());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, approvableApprovalInstancesIds))
                .thenReturn(Collections.emptyList());

        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testIsApprovable_withoutUser() {
        Long approvalInstanceId = 1L;

        Set<Long> approvableApprovalInstancesIds = new HashSet<>();
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(Collections.emptyList());
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.emptySet()))
                .thenReturn(Collections.singletonList(new UserTaskInstanceEntity()));

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
    }

}