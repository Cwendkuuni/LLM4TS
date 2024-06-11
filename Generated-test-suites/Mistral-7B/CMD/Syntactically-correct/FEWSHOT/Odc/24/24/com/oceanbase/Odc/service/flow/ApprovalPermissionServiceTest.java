package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApprovalPermissionServiceTest {

    @Autowired
    private ApprovalPermissionService approvalPermissionService;
    @Autowired
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserResourceRoleRepository userResourceRoleRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    private Long approvalInstanceId;

    @Before
    public void setup() {
        approvalPermissionService = new ApprovalPermissionService() {
            @Override
            @Autowired
            public void setUserService(UserService userService) {
                this.userService = userService;
            }

            @Override
            @Autowired
            public void setResourceRoleService(ResourceRoleService resourceRoleService) {
                this.resourceRoleService = resourceRoleService;
            }

            @Override
            @Autowired
            public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
                this.authenticationFacade = authenticationFacade;
            }

            @Override
            @Autowired
            public void setUserTaskCandidateRepository(UserTaskInstanceCandidateRepository userTaskCandidateRepository) {
                this.userTaskCandidateRepository = userTaskCandidateRepository;
            }

            @Override
            @Autowired
            public void setUserTaskInstanceRepository(UserTaskInstanceRepository userTaskInstanceRepository) {
                this.userTaskInstanceRepository = userTaskInstanceRepository;
            }

            @Override
            @Autowired
            public void setUserRepository(UserRepository userRepository) {
                this.userRepository = userRepository;
            }

            @Override
            @Autowired
            public void setRoleRepository(RoleRepository roleRepository) {
                this.roleRepository = roleRepository;
            }

            @Override
            @Autowired
            public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
                this.userRoleRepository = userRoleRepository;
            }

            @Override
            @Autowired
            public void setUserResourceRoleRepository(UserResourceRoleRepository userResourceRoleRepository) {
                this.userResourceRoleRepository = userResourceRoleRepository;
            }
        };
        Mockito.when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singletonList(1L));
        Mockito.when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.singletonList("role1"));
        Mockito.when(authenticationFacade.currentUserId()).thenReturn(approvalInstanceId);
        Mockito.when(userTaskCandidateRepository.save(Mockito.any(UserTaskInstanceCandidateEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(userRepository.findByUserIds(Mockito.anyList()))
                .thenAnswer(invocation -> Arrays.asList(new UserEntity(1L, "user1"), new UserEntity(2L, "user2")));
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(Mockito.anyList(), Mockito.anyBoolean()))
                .thenAnswer(invocation -> Arrays.asList(new Role(1L, "role1"), new Role(2L, "role2")));
        Mockito.when(userRoleRepository.findByRoleIdIn(Mockito.anyList()))
                .thenAnswer(invocation -> Arrays.asList(new UserRoleEntity(1L, 1L, 1L), new UserRoleEntity(2L, 1L, 2L)));
        Mockito.when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Mockito.anyList(),
                Mockito.anyList()))
                .thenAnswer(invocation -> Arrays.asList(new UserResourceRoleEntity(1L, 1L, "resource1", "role1"),
                        new UserResourceRoleEntity(1L, 2L, "resource2", "role1"),
                        new UserResourceRoleEntity(2L, 1L, "resource3", "role2")));
        userTaskCandidateRepository.deleteAll();
        userTaskInstanceRepository.deleteAll();
        approvalInstanceId = 1L;
    }

    @Test
    public void testGetCandidates() {
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        Assert.assertEquals(2, candidates.size());
        Set<User> candidates2 = approvalPermissionService.getCandidates(approvalInstanceId, false);
        Assert.assertEquals(1, candidates2.size());
        Set<User> candidates3 = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Assert.assertEquals(2, candidates3.size());

        Set<User> candidates4 = approvalPermissionService.getCandidates(null, true);
        Assert.assertTrue(candidates4.isEmpty());

        Set<User> candidates5 = approvalPermissionService.getCandidates(null, false);
        Assert.assertTrue(candidates5.isEmpty());

        Set<User> candidates6 = approvalPermissionService.getCandidates(null, null);
        Assert.assertTrue(candidates6.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        Set<Long> roleIds = new HashSet<>(List.of(1L, 2L));
        Assert.assertEquals(instanceId2CandidateRoleIds.size(), approvalInstanceIds.size());
        Assert.assertEquals(roleIds, instanceId2CandidateRoleIds.get(1L));
        Assert.assertEquals(roleIds, instanceId2CandidateRoleIds.get(2L));
        Assert.assertNull(instanceId2CandidateRoleIds.get(3L));
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds =
                approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        Set<String> resourceRoleIdentifiers = new HashSet<>(List.of("role1", "role2"));
        Assert.assertEquals(instanceId2CandidateResourceRoleIdentifierIds.size(), approvalInstanceIds.size());
        Assert.assertEquals(resourceRoleIdentifiers, instanceId2CandidateResourceRoleIdentifierIds.get(1L));
        Assert.assertEquals(resourceRoleIdentifiers, instanceId2CandidateResourceRoleIdentifierIds.get(2L));
        Assert.assertNull(instanceId2CandidateResourceRoleIdentifierIds.get(3L));
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        Set<Long> userIds = new HashSet<>(List.of(1L, 2L));
        Assert.assertEquals(candidateUserIds.size(), approvalInstanceIds.size());
        Assert.assertEquals(userIds, candidateUserIds);

        Set<Long> candidateUserIds2 = approvalPermissionService.getCandidateUserIds(Collections.emptyList());
        Assert.assertTrue(candidateUserIds2.isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        List<Role> roles = Arrays.asList(new Role(1L, "role1"), new Role(2L, "role2"));
        Assert.assertEquals(roles.size(), candidateRoles.size());
        Assert.assertEquals(roles.get(0), candidateRoles.get(0));
        Assert.assertEquals(roles.get(1), candidateRoles.get(1));

        List<Role> candidateRoles2 = approvalPermissionService.getCandidateRoles(approvalInstanceId, false);
        Assert.assertTrue(candidateRoles2.isEmpty());

        List<Role> candidateRoles3 = approvalPermissionService.getCandidateRoles(approvalInstanceId, null);
        Assert.assertEquals(roles.size(), candidateRoles3.size());
        Assert.assertEquals(roles.get(0), candidateRoles3.get(0));
        Assert.assertEquals(roles.get(1), candidateRoles3.get(1));

        List<Role> candidateRoles4 = approvalPermissionService.getCandidateRoles(null, true);
        Assert.assertTrue(candidateRoles4.isEmpty());

        List<Role> candidateRoles5 = approvalPermissionService.getCandidateRoles(null, false);
        Assert.assertTrue(candidateRoles5.isEmpty());

        List<Role> candidateRoles6 = approvalPermissionService.getCandidateRoles(null, null);
        Assert.assertEquals(roles.size(), candidateRoles6.size());
        Assert.assertEquals(roles.get(0), candidateRoles6.get(0));
        Assert.assertEquals(roles.get(1), candidateRoles6.get(1));
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        boolean isApprovable = approvalPermissionService.isApprovable(approvalInstanceId);
        Assert.assertTrue(isApprovable);

        isApprovable = approvalPermissionService.isApprovable(null);
        Assert.assertFalse(isApprovable);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> approvableApprovalInstances = approvalPermissionService.getApprovableApprovalInstances();
        Assert.assertEquals(3, approvableApprovalInstances.size());

        List<UserTaskInstanceEntity> approvableApprovalInstances2 = approvalPermissionService.getApprovableApprovalInstances(1L);
        Assert.assertEquals(1, approvableApprovalInstances2.size());
        Assert.assertEquals(approvalInstanceId, approvableApprovalInstances2.get(0).getId());

        List<UserTaskInstanceEntity> approvableApprovalInstances3 = approvalPermissionService.getApprovableApprovalInstances(2L);
        Assert.assertEquals(0, approvableApprovalInstances3.size());

        List<UserTaskInstanceEntity> approvableApprovalInstances4 = approvalPermissionService.getApprovableApprovalInstances(null);
        Assert.assertEquals(3, approvableApprovalInstances4.size());
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "role1";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        UserTaskInstanceCandidateEntity candidateEntity = userTaskCandidateRepository.findByApprovalInstanceIdAndResourceRoleIdentifier(
                approvalInstanceId, resourceRoleIdentifier);
        Assert.assertNotNull(candidateEntity);
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> approvableExternalInstances = approvalPermissionService.listApprovableExternalInstances();
        Assert.assertEquals(1, approvableExternalInstances.size());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L, 3L);
        Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        Set<UserEntity> userEntities1 = new HashSet<>(List.of(new UserEntity(1L, "user1"), new UserEntity(2L, "user2")));
        Set<UserEntity> userEntities2 = new HashSet<>(List.of(new UserEntity(3L, "user3"), new UserEntity(4L, "user4")));

        for (Map.Entry<Long, Set<UserEntity>> entry : candidatesByFlowInstanceIds.entrySet()) {
            Long flowInstanceId = entry.getKey();
            Set<UserEntity> candidates = entry.getValue();
            if (flowInstanceId == 1L) {
                Assert.assertEquals(userEntities1, candidates);
            } else if (flowInstanceId == 2L) {
                Assert.assertEquals(userEntities2, candidates);
            } else {
                Assert.assertTrue(candidates.isEmpty());
            }
        }

        Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds2 = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.emptyList());
        Assert.assertTrue(candidatesByFlowInstanceIds2.isEmpty());
    }
}