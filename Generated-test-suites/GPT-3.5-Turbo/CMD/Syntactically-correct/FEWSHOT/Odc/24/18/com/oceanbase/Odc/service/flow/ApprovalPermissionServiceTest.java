package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.service.iam.resourcerole.UserResourceRoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserRepository userRepository;

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
    private UserResourceRoleRepository userResourceRoleRepository;

    @Test
    public void testGetCandidates() {
        // Test case for getting candidates by approvalInstanceId
        Long approvalInstanceId = 123L;
        Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Test case for getting instanceId to candidate role Ids mapping
        Collection<Long> approvalInstanceIds = new HashSet<>();
        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertNotNull(instanceId2CandidateRoleIds);
        assertEquals(0, instanceId2CandidateRoleIds.size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        // Test case for getting instanceId to candidate resource role identifier Ids mapping
        Collection<Long> approvalInstanceIds = new HashSet<>();
        Map<Long, Set<String>> instanceId2CandidateResourceRoleIds = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        assertNotNull(instanceId2CandidateResourceRoleIds);
        assertEquals(0, instanceId2CandidateResourceRoleIds.size());
    }

    @Test
    public void testGetCandidateUserIds() {
        // Test case for getting candidate user Ids
        Collection<Long> approvalInstanceIds = new HashSet<>();
        Set<Long> userIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertNotNull(userIds);
        assertEquals(0, userIds.size());
    }

    @Test
    public void testGetCandidateRoles() {
        // Test case for getting candidate roles
        Long approvalInstanceId = 123L;
        List<Role> roles = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        assertNotNull(roles);
        assertEquals(0, roles.size());
    }

    @Test
    public void testIsApprovable() {
        // Test case for checking if approval is approvable
        Long approvalInstanceId = 123L;
        boolean isApprovable = approvalPermissionService.isApprovable(approvalInstanceId);
        assertFalse(isApprovable);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        // Test case for getting approvable approval instances
        List<UserTaskInstanceEntity> instances = approvalPermissionService.getApprovableApprovalInstances();
        assertNotNull(instances);
        assertEquals(0, instances.size());
    }

    @Test
    public void testGetApprovableApprovalInstancesUserId() {
        // Test case for getting approvable approval instances by user Id
        long userId = 123L;
        List<UserTaskInstanceEntity> instances = approvalPermissionService.getApprovableApprovalInstances(userId);
        assertNotNull(instances);
        assertEquals(0, instances.size());
    }

    @Test
    public void testSetCandidateResourceRole() {
        // Test case for setting candidate resource role
        Long approvalInstanceId = 123L;
        String resourceRoleIdentifier = "ROLE1";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
    }

    @Test
    public void testListApprovableExternalInstances() {
        // Test case for listing approvable external instances
        List<UserTaskInstanceEntity> instances = approvalPermissionService.listApprovableExternalInstances();
        assertNotNull(instances);
        assertEquals(0, instances.size());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        // Test case for getting candidates by flow instance Ids
        Collection<Long> flowInstanceIds = new HashSet<>();
        Map<Long, Set<UserEntity>> instanceId2Candidates = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertNotNull(instanceId2Candidates);
        assertEquals(0, instanceId2Candidates.size());
    }

}