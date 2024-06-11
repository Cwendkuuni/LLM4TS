package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setUp() {
        approvalPermissionService = new ApprovalPermissionService();
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(0, candidates.size());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = Collections.singletonList(1L);
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = Collections.singletonList(1L);
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = Collections.singletonList(1L);
        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertEquals(0, candidateUserIds.size());
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        assertEquals(0, candidateRoles.size());
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        boolean result = approvalPermissionService.isApprovable(approvalInstanceId);
        assertTrue(!result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> approvableInstances = approvalPermissionService.getApprovableApprovalInstances();
        assertEquals(0, approvableInstances.size());
    }

    @Test
    public void testGetApprovableApprovalInstancesForUser() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> approvableInstances = approvalPermissionService.getApprovableApprovalInstances(userId);
        assertEquals(0, approvableInstances.size());
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "testRole";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
        // No Assertion as this is a void method
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> approvableExternalInstances = approvalPermissionService.listApprovableExternalInstances();
        assertEquals(0, approvableExternalInstances.size());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = Collections.singletonList(1L);
        Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertEquals(0, candidatesByFlowInstanceIds.size());
    }
}