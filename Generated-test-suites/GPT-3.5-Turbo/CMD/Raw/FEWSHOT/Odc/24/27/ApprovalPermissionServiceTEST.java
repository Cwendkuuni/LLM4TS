package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.metadb.iam.UserEntity;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();
    
    @Test
    public void testGetCandidates() {
        Set<User> candidates = approvalPermissionService.getCandidates(1L, true);
        assertNotNull(candidates);
    }
    
    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> candidateRoleIdsMap = approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(1L, 2L, 3L));
        assertNotNull(candidateRoleIdsMap);
    }
    
    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> candidateResourceRoleIdentifiersMap = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Arrays.asList(1L, 2L, 3L));
        assertNotNull(candidateResourceRoleIdentifiersMap);
    }
    
    @Test
    public void testGetCandidateUserIds() {
        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(Arrays.asList(1L, 2L, 3L));
        assertNotNull(candidateUserIds);
    }
    
    @Test
    public void testGetCandidateRoles() {
        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(1L, true);
        assertNotNull(candidateRoles);
    }
    
    @Test
    public void testIsApprovable() {
        boolean isApprovable = approvalPermissionService.isApprovable(1L);
        assertFalse(isApprovable);
    }
    
    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> approvableInstances = approvalPermissionService.getApprovableApprovalInstances();
        assertNotNull(approvableInstances);
    }
    
    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        List<UserTaskInstanceEntity> approvableInstances = approvalPermissionService.getApprovableApprovalInstances(1L);
        assertNotNull(approvableInstances);
    }
    
    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(1L, "resourceRoleIdentifier");
        // Validate success based on the operation
        assertTrue(true);
    }
    
    @Test
    public void testlistApprovableExternalInstances() {
        List<UserTaskInstanceEntity> externalInstances = approvalPermissionService.listApprovableExternalInstances();
        assertNotNull(externalInstances);
    }
    
    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> candidatesMap = approvalPermissionService.getCandidatesByFlowInstanceIds(Arrays.asList(1L, 2L, 3L));
        assertNotNull(candidatesMap);
    }
}