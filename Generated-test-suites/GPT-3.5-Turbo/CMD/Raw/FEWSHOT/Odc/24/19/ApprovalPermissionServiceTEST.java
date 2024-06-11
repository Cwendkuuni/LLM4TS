package org.apache.commons.cli;

import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setUp() {
        approvalPermissionService = new ApprovalPermissionService();
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        Assert.assertNotNull(candidates);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);

        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        Assert.assertNotNull(instanceId2CandidateRoleIds);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);

        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        Assert.assertNotNull(instanceId2CandidateResourceRoleIdentifierIds);
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);

        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        Assert.assertNotNull(candidateUserIds);
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        Assert.assertNotNull(candidateRoles);
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;

        boolean isApprovable = approvalPermissionService.isApprovable(approvalInstanceId);
        Assert.assertTrue(isApprovable);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> approvableInstances = approvalPermissionService.getApprovableApprovalInstances();
        Assert.assertNotNull(approvableInstances);
    }

    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> approvableInstances = approvalPermissionService.getApprovableApprovalInstances(userId);
        Assert.assertNotNull(approvableInstances);
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "ROLE";

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> externalInstances = approvalPermissionService.listApprovableExternalInstances();
        Assert.assertNotNull(externalInstances);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L, 3L);

        Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        Assert.assertNotNull(candidatesByFlowInstanceIds);
    }
}