package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

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

    @Test
    public void testGetCandidates() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertNotNull(candidates);
    }

    @Test
    public void testGetCandidateUserIds() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        assertNotNull(candidateUserIds);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertNotNull(instanceId2CandidateRoleIds);
    }

    @Test
    public void testGetCandidateRoles() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        assertNotNull(candidateRoles);
    }

    @Test
    public void testIsApprovable() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        Long approvalInstanceId = 1L;

        boolean isApprovable = approvalPermissionService.isApprovable(approvalInstanceId);

        assertTrue(isApprovable);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        List<UserTaskInstanceEntity> approvableInstances = approvalPermissionService.getApprovableApprovalInstances();

        assertNotNull(approvableInstances);
    }

    @Test
    public void testGetApprovableApprovalInstancesByUserId() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        long userId = 1L;

        List<UserTaskInstanceEntity> approvableInstances = approvalPermissionService.getApprovableApprovalInstances(userId);

        assertNotNull(approvableInstances);
    }

    @Test
    public void testSetCandidateResourceRole() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "ROLE1";

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
    }

    @Test
    public void testListApprovableExternalInstances() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        List<UserTaskInstanceEntity> approvableExternalInstances = approvalPermissionService.listApprovableExternalInstances();

        assertNotNull(approvableExternalInstances);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);

        Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

        assertNotNull(candidatesByFlowInstanceIds);
    }
}