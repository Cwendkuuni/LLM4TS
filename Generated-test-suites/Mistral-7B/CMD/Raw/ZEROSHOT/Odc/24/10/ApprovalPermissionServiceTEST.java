package com.oceanbase.odc.service.flow.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.MockAuthenticationFacade;
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

@ExtendWith(MockitoExtension.class)
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

    private MockAuthenticationFacade authenticationFacademock;

    @BeforeEach
    public void setUp() {
        authenticationFacademock = new MockAuthenticationFacade(1L);
        approvalPermissionService.setAuthenticationFacade(authenticationFacademock);
    }

    @Test
    public void testGetCandidates() {
        // Given
        long approvalInstanceId = 1L;
        boolean enabled = true;

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertNotNull(candidates);
        // Add more assertions as needed
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Given
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        // When
        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        // Then
        assertNotNull(instanceId2CandidateRoleIds);
        // Add more assertions as needed
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        // Given
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        // When
        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService
                .getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

        // Then
        assertNotNull(instanceId2CandidateResourceRoleIdentifierIds);
        // Add more assertions as needed
    }

    @Test
    public void testGetCandidateUserIds() {
        // Given
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        // When
        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        // Then
        assertNotNull(candidateUserIds);
        // Add more assertions as needed
    }

    @Test
    public void testGetCandidateRoles() {
        // Given
        Long approvalInstanceId = 1L;
        boolean enabled = true;

        // When
        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        // Then
        assertNotNull(candidateRoles);
        // Add more assertions as needed
    }

    @Test
    public void testIsApprovable() {
        // Given
        long approvalInstanceId = 1L;

        // When
        boolean isApprovable = approvalPermissionService.isApprovable(approvalInstanceId);

        // Then
        assertFalse(isApprovable);
        // Add more assertions as needed
    }

    @Test
    public void testIsApprovableWithUserId() {
        // Given
        long userId = 1L;
        long approvalInstanceId = 1L;

        // When
        boolean isApprovable = approvalPermissionService.isApprovable(approvalInstanceId, userId);

        // Then
        assertFalse(isApprovable);
        // Add more assertions as needed
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        // Given

        // When
        List<UserTaskInstanceEntity> approvableApprovalInstances = approvalPermissionService
                .getApprovableApprovalInstances();

        // Then
        assertNotNull(approvableApprovalInstances);
        // Add more assertions as needed
    }

    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        // Given
        long userId = 1L;

        // When
        List<UserTaskInstanceEntity> approvableApprovalInstances = approvalPermissionService
                .getApprovableApprovalInstances(userId);

        // Then
        assertNotNull(approvableApprovalInstances);
        // Add more assertions as needed
    }

    @Test
    public void testSetCandidateResourceRole() {
        // Given
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "testRoleIdentifier";

        // When
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        // Then
        // Add appropriate assertions here
    }

    @Test
    public void testListApprovableExternalInstances() {
        // Given

        // When
        List<UserTaskInstanceEntity> approvableExternalInstances = approvalPermissionService
                .listApprovableExternalInstances();

        // Then
        assertNotNull(approvableExternalInstances);
        // Add more assertions as needed
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        // Given
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);

        // When
        Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService
                .getCandidatesByFlowInstanceIds(flowInstanceIds);

        // Then
        assertNotNull(candidatesByFlowInstanceIds);
        // Add more assertions as needed
    }
}