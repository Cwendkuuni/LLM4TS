
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

    @Test
    public void testGetCandidates() {
        assertNotNull(approvalPermissionService.getCandidates(1L, true));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        assertNotNull(approvalPermissionService.getInstanceId2CandidateRoleIds(Collections.singletonList(1L)));
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        assertNotNull(approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Collections.singletonList(1L)));
    }

    @Test
    public void testGetCandidateUserIds() {
        assertNotNull(approvalPermissionService.getCandidateUserIds(Collections.singletonList(1L)));
    }

    @Test
    public void testGetCandidateRoles() {
        assertNotNull(approvalPermissionService.getCandidateRoles(1L, true));
    }

    @Test
    public void testIsApprovable() {
        assertFalse(approvalPermissionService.isApprovable(1L));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        assertNotNull(approvalPermissionService.getApprovableApprovalInstances());
    }

    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        assertNotNull(approvalPermissionService.getApprovableApprovalInstances(1L));
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(1L, "resourceRoleIdentifier");
    }

    @Test
    public void testListApprovableExternalInstances() {
        assertNotNull(approvalPermissionService.listApprovableExternalInstances());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        assertNotNull(approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singletonList(1L)));
    }
}
