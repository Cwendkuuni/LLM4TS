package com.oceanbase.odc.service.flow;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService = new ApprovalPermissionService();

    @Test
    public void testGetCandidates() {
        // Test case 1: empty approvalInstanceId -> expect empty set
        assertEquals(Collections.emptySet(), approvalPermissionService.getCandidates(null, null));

        // Test case 2: valid approvalInstanceId with enabled = true -> expect set of User objects
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        assertNotNull(approvalPermissionService.getCandidates(approvalInstanceId, enabled));

        // Test case 3: valid approvalInstanceId with enabled = false -> expect set of User objects
        enabled = false;
        assertNotNull(approvalPermissionService.getCandidates(approvalInstanceId, enabled));

        // Test case 4: valid approvalInstanceId with enabled = null -> expect set of User objects
        enabled = null;
        assertNotNull(approvalPermissionService.getCandidates(approvalInstanceId, enabled));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Test case 1: empty approvalInstanceIds -> expect empty map
        Collection<Long> approvalInstanceIds = Collections.emptyList();
        assertEquals(Collections.emptyMap(), approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds));

        // Test case 2: valid approvalInstanceIds -> expect map with instance id and set of role ids
        approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
        assertNotNull(approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds));
    }

    // Add more test cases for other methods in the ApprovalPermissionService class

}