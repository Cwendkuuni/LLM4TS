package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

/**
 * Test class for ApprovalPermissionService
 */
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method getCandidates
     */
    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = Boolean.TRUE;
        Set<User> expectedCandidates = new HashSet<>();
        // Given
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);
        UserRoleEntity userRole1 = new UserRoleEntity();
        userRole1.setUserId(user1.getId());
        userRole1.setRoleId(role1.getId());
        UserRoleEntity userRole2 = new UserRoleEntity();
        userRole2.setUserId(user2.getId());
        userRole2.setRoleId(role2.getId());
        UserResourceRoleEntity userResourceRole1 = new UserResourceRoleEntity();
        userResourceRole1.setUserId(user1.getId());
        userResourceRole1.setResourceId(1L);
        userResourceRole1.setResourceRoleIdentifier("roleKey1:1");
        UserResourceRoleEntity userResourceRole2 = new UserResourceRoleEntity();
        userResourceRole2.setUserId(user2.getId());
        userResourceRole2.setResourceId(2L);
        userResourceRole2.setResourceRoleIdentifier("roleKey2:2");

        // When
        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertEquals(expectedCandidates, actualCandidates);

        // Add test cases with different user sets
    }

    /**
     * Test method getInstanceId2CandidateRoleIds
     */
    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);

        // Given - Assume there are UserTaskInstanceCandidateEntities for the given approvalInstanceIds

        // When
        Map<Long, Set<Long>> actualCandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        // Then
        // Add test cases with different role mappings
    }

    /**
     * Test method getInstanceId2CandidateResourceRoleIdentifierIds
     */
    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        // Given - Assume there are UserTaskInstanceCandidateEntities for the given approvalInstanceIds

        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);

        // When
        Map<Long, Set<String>> actualCandidateResourceRoleIdentifierIds = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

        // Then
        // Add test cases with different resource role identifiers
    }

    /**
     * Test method getCandidateUserIds
     */
    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);

        // Given - Assume there are UserTaskInstanceCandidateEntities for the given approvalInstanceIds

        // When
        Set<Long> actualCandidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        // Then
        // Add test cases with different user sets
    }

    /**
     * Test method getCandidateRoles
     */
    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = Boolean.TRUE;

        // Given - Assume there are UserTaskInstanceCandidateEntities for the given approvalInstanceId

        // When
        List<Role> actualCandidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        // Then
        // Add test cases with different role sets
    }

    /**
     * Test method isApprovable
     */
    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;

        // Given - Assume there are approvable approval instances

        // When
        boolean actualIsApprovable = approvalPermissionService.isApprovable(approvalInstanceId);

        // Then
        assertTrue(actualIsApprovable);

        // Add test case with an unapprovable approval instance
    }

    /**
     * Test method getApprovableApprovalInstances
     */
    @Test
    public void testGetApprovableApprovalInstances() {
        // Given - Assume there are approvable approval instances

        // When
        List<UserTaskInstanceEntity> actualApprovableApprovalInstances = approvalPermissionService.getApprovableApprovalInstances();

        // Then
        // Add test cases with different approval instance sets
    }

    /**
     * Test method getApprovableApprovalInstances
     * (Test with userId instead of current user)
     */
    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        long userId = 1L;

        // Given - Assume there are approvable approval instances for the given userId

        // When
        List<UserTaskInstanceEntity> actualApprovableApprovalInstances = approvalPermissionService.getApprovableApprovalInstances(userId);

        // Then
        // Add test cases with different approval instance sets
    }

    /**
     * Test method setCandidateResourceRole
     */
    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "resourceRoleIdentifier";

        // Given

        // When
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        // Then
        // Add test cases checking if the UserTaskInstanceCandidateEntity was saved correctly
    }

    /**
     * Test method listApprovableExternalInstances
     */
    @Test
    public void testListApprovableExternalInstances() {
        // Given - Assume there are approvable external instances

        // When
        List<UserTaskInstanceEntity> actualApprovableExternalInstances = approvalPermissionService.listApprovableExternalInstances();

        // Then
        // Add test cases with different external instance sets
    }

    /**
     * Test method getCandidatesByFlowInstanceIds
     */
    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        flowInstanceIds.add(2L);

        // Given - Assume there are executing approval instances with the given flowInstanceIds

        // When
        Map<Long, Set<UserEntity>> actualCandidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

        // Then
        // Add test cases with different user sets for each flowInstanceId
    }
}