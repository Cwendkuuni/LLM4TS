package com.oceanbase.odc.service.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService service = new ApprovalPermissionService();

    @Test
    public void testGetCandidates() {
        Set<User> users = service.getCandidates(1L, true);
        assertEquals(2, users.size());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);
        approvalInstanceIds.add(3L);
        approvalInstanceIds.add(4L);

        assertEquals(4, service.getInstanceId2CandidateRoleIds(approvalInstanceIds).size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);
        
        assertEquals(2, service.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds).size());
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);
        
        assertEquals(3, service.getCandidateUserIds(approvalInstanceIds).size());
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        assertEquals(5, service.getApprovableApprovalInstances().size());
    }

    @Test
    public void testIsApprovable() {
        assertTrue(service.isApprovable(1L));
        assertFalse(service.isApprovable(10L));
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> roles = service.getCandidateRoles(1L, true);
        assertEquals(2, roles.size());
    }

    @Test
    public void testGetApprovableApprovalInstancesByUserId() {
        assertEquals(3, service.getApprovableApprovalInstances(1L).size());
    }

    @Test
    public void testListApprovableExternalInstances() {
        assertEquals(2, service.listApprovableExternalInstances().size());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Set<Long> flowInstanceIds = new HashSet<>();
        flowInstanceIds.add(1L);
        flowInstanceIds.add(2L);
        
        assertEquals(2, service.getCandidatesByFlowInstanceIds(flowInstanceIds).size());
    }
}