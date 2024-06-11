import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.role.RoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.UserRoleEntity;
import com.oceanbase.odc.service.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.service.iam.resourcerole.UserResourceRoleRepository;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private UserResourceRoleRepository userResourceRoleRepository;
    private ResourceRoleService resourceRoleService;
    private UserService userService;
    private AuthenticationFacade authenticationFacade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        approvalPermissionService = new ApprovalPermissionService();
        userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRoleRepository = mock(UserRoleRepository.class);
        userResourceRoleRepository = mock(UserResourceRoleRepository.class);
        resourceRoleService = mock(ResourceRoleService.class);
        userService = mock(UserService.class);
        authenticationFacade = mock(AuthenticationFacade.class);
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenAnswer(new Answer<List<UserTaskInstanceCandidateEntity>>() {
            @Override
            public List<UserTaskInstanceCandidateEntity> answer(InvocationOnMock invocation) throws Throwable {
                Long approvalInstanceId = (Long) invocation.getArgument(0);
                if (approvalInstanceId == null) {
                    return Collections.emptyList();
                }
                List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
                UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
                entity1.setApprovalInstanceId(approvalInstanceId);
                entity1.setRoleId(1L);
                entities.add(entity1);
                UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
                entity2.setApprovalInstanceId(approvalInstanceId);
                entity2.setUserId(1L);
                entities.add(entity2);
                return entities;
            }
        });
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIds(anyCollection())).thenAnswer(new Answer<List<UserEntity>>() {
            @Override
            public List<UserEntity> answer(InvocationOnMock invocation) throws Throwable {
                Collection<Long> userIds = (Collection<Long>) invocation.getArgument(0);
                List<UserEntity> users = new ArrayList<>();
                for (Long userId : userIds) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setId(userId);
                    userEntity.setName("User_" + userId);
                    users.add(userEntity);
                }
                return users;
            }
        });
        when(roleRepository.findByRoleIdsAndEnabled(anyCollection(), anyBoolean())).thenAnswer(new Answer<List<Role>>() {
            @Override
            public List<Role> answer(InvocationOnMock invocation) throws Throwable {
                Collection<Long> roleIds = (Collection<Long>) invocation.getArgument(0);
                Boolean enabled = (Boolean) invocation.getArgument(1);
                List<Role> roles = new ArrayList<>();
                for (Long roleId : roleIds) {
                    Role role = new Role();
                    role.setId(roleId);
                    role.setName("Role_" + roleId);
                    roles.add(role);
                }
                return roles;
            }
        });
        when(userRoleRepository.findByUserIds(anyCollection())).thenReturn(new ArrayList<>());
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(anyCollection(), anyCollection()))
                .thenReturn(new ArrayList<>());
        when(userResourceRoleRepository.findByResourceIds(anyLong())).thenAnswer(new Answer<List<UserResourceRoleEntity>>() {
            @Override
            public List<UserResourceRoleEntity> answer(InvocationOnMock invocation) throws Throwable {
                Long resourceId = (Long) invocation.getArgument(0);
                List<UserResourceRoleEntity> userResourceRoles = new ArrayList<>();
                UserResourceRoleEntity userResourceRole = new UserResourceRoleEntity();
                userResourceRole.setUserId(1L);
                userResourceRole.setResourceRoleIdentifier("resourceRoleIdentifier1");
                userResourceRole.setResourceId(resourceId);
                userResourceRoles.add(userResourceRole);
                return userResourceRoles;
            }
        });
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(anyLong())).thenAnswer(new Answer<Set<String>>() {
            @Override
            public Set<String> answer(InvocationOnMock invocation) throws Throwable {
                Long userId = (Long) invocation.getArgument(0);
                Set<String> resourceRoleIdentifiers = new HashSet<>();
                resourceRoleIdentifiers.add("resourceRoleIdentifier1");
                resourceRoleIdentifiers.add("resourceRoleIdentifier2");
                return resourceRoleIdentifiers;
            }
        });
        when(userService.getCurrentUserRoleIds()).thenReturn(new ArrayList<>());
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(new ArrayList<>());
        when(authenticationFacade.currentUserId()).thenReturn(1L);
    }

    @Test
    public void testGetCandidates() {
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singletonList(1L));
        approvalPermissionService.setCandidateResourceRole("1", "resourceRoleIdentifier1");
        Set<User> candidates = approvalPermissionService.getCandidates(1L, null);
        assertEquals(2, candidates.size());
        User user1 = candidates.stream().filter(u -> u.getId() == 1L).findFirst().get();
        assertNotNull(user1);
        User user2 = candidates.stream().filter(u -> u.getId() != 1L).findFirst().get();
        assertNotNull(user2);
    }

    @Test
    public void testGetCandidates_Enabled() {
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singletonList(1L));
        approvalPermissionService.setCandidateResourceRole("1", "resourceRoleIdentifier1");
        Set<User> candidates = approvalPermissionService.getCandidates(1L, true);
        assertEquals(1, candidates.size());
        User user = candidates.iterator().next();
        assertEquals(1L, user.getId());
    }

    @Test
    public void testGetCandidates_Disabled() {
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singletonList(1L));
        approvalPermissionService.setCandidateResourceRole("1", "resourceRoleIdentifier1");
        Set<User> candidates = approvalPermissionService.getCandidates(1L, false);
        assertEquals(1, candidates.size());
        User user = candidates.iterator().next();
        assertEquals(1L, user.getId());
    }

    @Test
    public void testGetCandidates_NoUserOrRole() {
        approvalPermissionService.getCandidates(1L, null);
        verify(userTaskCandidateRepository, never()).findByApprovalInstanceId(1L);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(1L, 2L));
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(Arrays.asList(1L, 2L));
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Arrays.asList(1L, 2L));
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(Arrays.asList(1L, 2L));
    }

    @Test
    public void testGetCandidateUserIds() {
        approvalPermissionService.getCandidateUserIds(Arrays.asList(1L, 2L));
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(Arrays.asList(1L, 2L));
    }

    @Test
    public void testGetCandidateRoles() {
        approvalPermissionService.getCandidateRoles(1L, null);
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(1L);
    }

    @Test
    public void testIsApprovable() {
        approvalPermissionService.isApprovable(1L);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
        verify(userTaskInstanceRepository, times(1)).findById(1L);
    }

    @Test
    public void testIsApprovable_NotExecuting() {
        when(userTaskInstanceRepository.findById(1L)).thenReturn(null);
        approvalPermissionService.isApprovable(1L);
        verify(userTaskInstanceRepository, times(1)).findById(1L);
        verify(userTaskInstanceRepository, never()).findByStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> approvalInstances = approvalPermissionService.getApprovableApprovalInstances();
        assertEquals(0, approvalInstances.size());
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testGetApprovableApprovalInstances_User() {
        approvalPermissionService.getApprovableApprovalInstances(1L);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserId(1L);
    }

    @Test
    public void testGetApprovableApprovalInstances_Role() {
        when(userService.getUserRoleIds(1L)).thenReturn(Arrays.asList(1L));
        approvalPermissionService.getApprovableApprovalInstances(1L);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));
    }

    @Test
    public void testGetApprovableApprovalInstances_ResourceRole() {
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Arrays.asList("resourceRoleIdentifier1"));
        approvalPermissionService.getApprovableApprovalInstances();
        verify(userTaskInstanceRepository, times(1)).findByResourceRoleIdentifier("resourceRoleIdentifier1");
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(1L, "resourceRoleIdentifier1");
        verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
    }

    @Test
    public void testListApprovableExternalInstances() {
        approvalPermissionService.listApprovableExternalInstances();
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        approvalPermissionService.getCandidatesByFlowInstanceIds(Arrays.asList(1L, 2L));
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L),
                FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds_Empty() {
        approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.emptyList());
        verify(userTaskInstanceRepository, never()).findApprovalInstanceIdByFlowInstanceIdAndStatus(anyCollection(),
                any(FlowNodeStatus.class));
    }

}