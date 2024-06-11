package com.oceanbase.odc.service.flow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

public class ApprovalPermissionServiceTest {
    @Autowired
    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void testGetCandidates() {
        Set<Long> instanceIds = LongStream.rangeClosed(1, 5).boxed().collect(Collectors.toSet());
        assertFalse(approvalPermissionService.getCandidates(1L, true).isEmpty());
        assertTrue(approvalPermissionService.getCandidates(1L, false).isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Set<Long> instanceIds = LongStream.rangeClosed(1, 5).boxed().collect(Collectors.toSet());
        assertFalse(approvalPermissionService.getInstanceId2CandidateRoleIds(instanceIds).isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Set<Long> instanceIds = LongStream.rangeClosed(1, 5).boxed().collect(Collectors.toSet());
        assertFalse(approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(instanceIds).isEmpty());
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> instanceIds = LongStream.rangeClosed(1, 5).boxed().collect(Collectors.toSet());
        assertFalse(approvalPermissionService.getCandidateUserIds(instanceIds).isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        assertNotNull(approvalPermissionService.getCandidateRoles(1L, true));
        assertNull(approvalPermissionService.getCandidateRoles(1L, false));
    }

    @Test
    public void testIsApprovable() {
        assertTrue(approvalPermissionService.isApprovable(1L));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        assertFalse(approvalPermissionService.getApprovableApprovalInstances().isEmpty());
    }

    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        assertFalse(approvalPermissionService.getApprovableApprovalInstances(1L).isEmpty());
    }

    @Test
    public void testListApprovableExternalInstances() {
        assertFalse(approvalPermissionService.listApprovableExternalInstances().isEmpty());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Set<Long> flowInstanceIds = new HashSet<Long>();
        flowInstanceIds.add(1L);
        assertFalse(approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds).isEmpty());
    }
}