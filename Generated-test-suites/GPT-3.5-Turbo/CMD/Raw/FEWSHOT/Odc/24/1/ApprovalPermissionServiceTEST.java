package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ApprovalPermissionServiceTest {

    @Test
    public void testGetCandidates() {
        // Test for approval instance with enabled users
        assertEquals(2, service.getCandidates(1L, true).size());
        // Test for approval instance with disabled users
        assertEquals(1, service.getCandidates(2L, false).size());
        // Test for approval instance with both enabled and disabled users
        assertEquals(3, service.getCandidates(3L, null).size());
        // Test for approval instance with no candidates
        assertEquals(0, service.getCandidates(4L, true).size());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Test for collection with approval instance IDs
        assertEquals(2, service.getInstanceId2CandidateRoleIds(Collections.singletonList(1L)).size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        // Test for collection with approval instance IDs
        assertEquals(1, service.getInstanceId2CandidateResourceRoleIdentifierIds(Collections.singletonList(1L)).size());
    }

    @Test
    public void testGetCandidateUserIds() {
        // Test for collection with approval instance IDs
        assertEquals(2, service.getCandidateUserIds(Collections.singletonList(1L)).size());
    }

    @Test
    public void testGetCandidateRoles() {
        // Test for approval instance with enabled roles
        assertEquals(2, service.getCandidateRoles(1L, true).size());
        // Test for approval instance with disabled roles
        assertEquals(1, service.getCandidateRoles(2L, false).size());
    }

    @Test
    public void testIsApprovable() {
        // Test for an approvable approval instance
        assertEquals(true, service.isApprovable(1L));
        // Test for a non-approvable approval instance
        assertEquals(false, service.isApprovable(2L));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        // Test for valid current user
        assertNotNull(service.getApprovableApprovalInstances());
    }

    @Test
    public void testGetApprovableApprovalInstancesForUser() {
        // Test for specific user
        assertNotNull(service.getApprovableApprovalInstances(1L));
    }

    @Test
    public void testSetCandidateResourceRole() {
        // Test for setting resource role for an approval instance
        service.setCandidateResourceRole(1L, "role_identifier");
    }

    @Test
    public void testListApprovableExternalInstances() {
        // Test for listing approvable external instances
        assertNotNull(service.listApprovableExternalInstances());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        // Test for collection with flow instance IDs
        assertNotNull(service.getCandidatesByFlowInstanceIds(Collections.singletonList(1L)));
    }
}