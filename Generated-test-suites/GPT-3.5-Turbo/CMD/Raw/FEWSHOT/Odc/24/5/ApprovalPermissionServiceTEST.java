package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ApprovalPermissionServiceTest {

    @Test
    public void testGetCandidates() {
        // Test for empty approvalInstanceId
        assertTrue(ApprovalPermissionService.getCandidates(null, true).isEmpty());
        
        // Test for valid approvalInstanceId with enabled=true
        assertFalse(ApprovalPermissionService.getCandidates(1001L, true).isEmpty());
        
        // Test for valid approvalInstanceId with enabled=false
        assertFalse(ApprovalPermissionService.getCandidates(1002L, false).isEmpty());
        
        // Test for valid approvalInstanceId with enabled=null
        assertFalse(ApprovalPermissionService.getCandidates(1003L, null).isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Test for empty approvalInstanceIds
        assertEquals(0, ApprovalPermissionService.getInstanceId2CandidateRoleIds(Collections.emptyList()).size());
        
        // Test for valid approvalInstanceIds
        assertEquals(1, ApprovalPermissionService.getInstanceId2CandidateRoleIds(Collections.singletonList(2001L)).size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        // Test for empty approvalInstanceIds
        assertEquals(0, ApprovalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Collections.emptyList()).size());
        
        // Test for valid approvalInstanceIds
        assertEquals(1, ApprovalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Collections.singletonList(2001L)).size());
    }

    @Test
    public void testGetCandidateUserIds() {
        // Test for empty approvalInstanceIds
        assertTrue(ApprovalPermissionService.getCandidateUserIds(Collections.emptyList()).isEmpty());
        
        // Test for valid approvalInstanceIds
        assertFalse(ApprovalPermissionService.getCandidateUserIds(Collections.singletonList(2001L)).isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        // Test for empty approvalInstanceId
        assertTrue(ApprovalPermissionService.getCandidateRoles(null, true).isEmpty());
        
        // Test for valid approvalInstanceId with enabled=true
        assertFalse(ApprovalPermissionService.getCandidateRoles(1001L, true).isEmpty());
        
        // Test for valid approvalInstanceId with enabled=false
        assertFalse(ApprovalPermissionService.getCandidateRoles(1002L, false).isEmpty());
    }

    @Test
    public void testIsApprovable() {
        // Test for invalid approvalInstanceId
        assertFalse(ApprovalPermissionService.isApprovable(null));
        
        // Test for valid approvalInstanceId
        assertTrue(ApprovalPermissionService.isApprovable(3001L));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        assertNotNull(ApprovalPermissionService.getApprovableApprovalInstances());
    }

    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        assertFalse(ApprovalPermissionService.getApprovableApprovalInstances(4001L).isEmpty());
    }

    @Test
    public void testSetCandidateResourceRole() {
        ApprovalPermissionService.setCandidateResourceRole(5001L, "resourceRoleIdentifier");
        // Verify if the method runs without exceptions
        assertTrue(true);
    }

    @Test
    public void testListApprovableExternalInstances() {
        assertNotNull(ApprovalPermissionService.listApprovableExternalInstances());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        // Test for empty flowInstanceIds
        assertEquals(0, ApprovalPermissionService.getCandidatesByFlowInstanceIds(Collections.emptyList()).size());
        
        // Test for valid flowInstanceIds
        assertEquals(1, ApprovalPermissionService.getCandidatesByFlowInstanceIds(Collections.singletonList(6001L)).size());
    }

}