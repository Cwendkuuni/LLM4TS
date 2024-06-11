package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@ActiveProfiles("test")
public class ApprovalPermissionServiceTest {

    @Autowired
    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void testGetCandidates() {
        Set<User> candidates = approvalPermissionService.getCandidates(1234567890L, true);
        // Add your assertions here
    }

    @Test
    public void testIsInstanceApproved() {
        boolean isApproved = approvalPermissionService.isApprovable(1234567890L);
        // Add your assertions here
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(1234567890L, 1234567891L, 1234567892L));
        // Add your assertions here
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Arrays.asList(1234567890L, 1234567891L, 1234567892L));
        // Add your assertions here
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> ids = approvalPermissionService.getCandidateUserIds(Arrays.asList(1234567890L, 1234567891L, 1234567892L));
        // Add your assertions here
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> roles = approvalPermissionService.getCandidateRoles(1234567890L, true);
        // Add your assertions here
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> instances = approvalPermissionService.getApprovableApprovalInstances();
        // Add your assertions here
    }

    @Test
    public void testGetApprovableApprovalInstances2() {
        List<UserTaskInstanceEntity> instances = approvalPermissionService.getApprovableApprovalInstances(123L);
        // Add your assertions here
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> instances = approvalPermissionService.listApprovableExternalInstances();
        // Add your assertions here
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> map = approvalPermissionService.getCandidatesByFlowInstanceIds(Arrays.asList(1234567890L, 1234567891L, 1234567892L));
        // Add your assertions here
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(1234567890L, "testResource");
        // Check the database for persist or use a mock to verify method calls, since this method has no return value.
    }
}
