package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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

public class ApprovalPermissionServiceTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserService userService;

    @Mock
    private ResourceRoleService resourceRoleService;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates_enabledIsNull_returnEmptySet() {
        Long approvalInstanceId = 1L;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertTrue(candidates.isEmpty());
    }

    @Test
    public void testGetCandidates_returnCorrectUsers() {
        Long approvalInstanceId = 1L;
        UserEntity user1 = mock(UserEntity.class);
        UserEntity user2 = mock(UserEntity.class);
        RoleEntity role1 = mock(RoleEntity.class);
        RoleEntity role2 = mock(RoleEntity.class);
        UserRoleEntity userRole1 = mock(UserRoleEntity.class);
        UserRoleEntity userRole2 = mock(UserRoleEntity.class);
        UserResourceRoleEntity userResourceRole1 = mock(UserResourceRoleEntity.class);
        UserResourceRoleEntity userResourceRole2 = mock(UserResourceRoleEntity.class);

        when(userRepository.findByRoleIdsAndEnabled(anyCollection(), anyBoolean())).thenReturn(Arrays.asList(user1, user2));

        when(roleRepository.findByRoleIds(anyCollection())).thenReturn(Arrays.asList(role1, role2));
        when(userRoleRepository.findByRoleIdIn(anyCollection())).thenReturn(Arrays.asList(userRole1, userRole2));
        when(userRole1.getUserId()).thenReturn(1L);
        when(userRole2.getUserId()).thenReturn(2L);
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(anyCollection(), anyCollection()))
                .thenReturn(Arrays.asList(userResourceRole1, userResourceRole2));
        when(userResourceRole1.getUserId()).thenReturn(1L);
        when(userResourceRole2.getUserId()).thenReturn(3L);

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        Set<Long> expectedUsersIds = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        assertTrue(candidates.size() == expectedUsersIds.size());
        assertTrue(candidates.containsAll(Arrays.asList(new User(user1), new User(user2), new User(userRepository.findById(3L).get()))));
    }

    @Test
    public void testGetCandidates_returnCorrectRoles() {
        Long approvalInstanceId = 1L;
        RoleEntity role1 = mock(RoleEntity.class);
        RoleEntity role2 = mock(RoleEntity.class);

        when(roleRepository.findByRoleIdsAndEnabled(anyCollection(), anyBoolean())).thenReturn(Arrays.asList(role1, role2));

        List<Role> candidates = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        assertTrue(candidates.size() == 2);
        assertTrue(candidates.containsAll(Arrays.asList(new Role(role1), new Role(role2))));
    }

    @Test
    public void testIsApprovable_approvalInstanceIdExists_returnTrue() {
        Long approvalInstanceId = 1L;
        Long userId = 1L;
        Set<Long> userIds = new HashSet<>(Arrays.asList(userId));
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(userTaskInstanceEntities);
        when(userService.getUserRoleIds(userId)).thenReturn(userIds);

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testIsApprovable_approvalInstanceIdNotExists_returnFalse() {
        Long approvalInstanceId = 1L;
        Set<Long> userIds = new HashSet<>(Arrays.asList(1L));
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(userTaskInstanceEntities);
        when(userService.getUserRoleIds(1L)).thenReturn(userIds);

        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test(expected = NullPointerException.class)
    public void testGetCandidates_approvalInstanceIdNull_throwException() {
        approvalPermissionService.getCandidates(null, true);
    }

    @Test(expected = NullPointerException.class)
    public void testIsApprovable_approvalInstanceIdNull_throwException() {
        approvalPermissionService.isApprovable(null);
    }

    @Test
    public void testListApprovableExternalInstances_returnCorrectInstances() {
        UserTaskInstanceEntity instance1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity instance2 = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(Arrays.asList(instance1, instance2));

        List<UserTaskInstanceEntity> instances = approvalPermissionService.listApprovableExternalInstances();
        assertTrue(instances.size() == 2);
        assertTrue(instances.containsAll(Arrays.asList(instance1, instance2)));
    }
}