package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
        Boolean enabled = true;

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.emptyList());

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertNotNull(candidates);
        assertTrue(candidates.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(Collections.emptyList());

        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertNotNull(instanceId2CandidateRoleIds);
        assertTrue(instanceId2CandidateRoleIds.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(Collections.emptyList());

        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

        assertNotNull(instanceId2CandidateResourceRoleIdentifierIds);
        assertTrue(instanceId2CandidateResourceRoleIdentifierIds.isEmpty());
    }

    @Test
    public void testGetCandidateUserIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(Collections.emptyList());

        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        assertNotNull(candidateUserIds);
        assertTrue(candidateUserIds.isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.emptyList());

        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        assertNotNull(candidateRoles);
        assertTrue(candidateRoles.isEmpty());
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new HashSet<>())).thenReturn(Collections.emptyList());

        boolean isApprovable = approvalPermissionService.isApprovable(approvalInstanceId);

        assertFalse(isApprovable);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>());
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(new HashSet<>());
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(Collections.emptyList());

        List<UserTaskInstanceEntity> approvableApprovalInstances = approvalPermissionService.getApprovableApprovalInstances();

        assertNotNull(approvableApprovalInstances);
        assertTrue(approvableApprovalInstances.isEmpty());
    }

    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        long userId = 1L;

        when(userService.getUserRoleIds(userId)).thenReturn(new HashSet<>());
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId)).thenReturn(new HashSet<>());
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(Collections.emptyList());

        List<UserTaskInstanceEntity> approvableApprovalInstances = approvalPermissionService.getApprovableApprovalInstances(userId);

        assertNotNull(approvableApprovalInstances);
        assertTrue(approvableApprovalInstances.isEmpty());
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "Role1";

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
    }

    @Test
    public void testListApprovableExternalInstances() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(Collections.emptyList());

        List<UserTaskInstanceEntity> approvableExternalInstances = approvalPermissionService.listApprovableExternalInstances();

        assertNotNull(approvableExternalInstances);
        assertTrue(approvableExternalInstances.isEmpty());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        List<Long> flowInstanceIds = Arrays.asList(1L, 2L);

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.EXECUTING)).thenReturn(Collections.emptyList());

        Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

        assertNotNull(candidatesByFlowInstanceIds);
        assertTrue(candidatesByFlowInstanceIds.isEmpty());
    }

}