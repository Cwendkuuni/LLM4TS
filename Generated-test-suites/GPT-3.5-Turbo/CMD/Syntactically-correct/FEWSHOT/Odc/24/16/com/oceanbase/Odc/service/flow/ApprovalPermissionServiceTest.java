
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void testGetCandidates() {
        Set<User> users = approvalPermissionService.getCandidates(1L, true);
        assertEquals(Collections.emptySet(), users);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> map = approvalPermissionService.getInstanceId2CandidateRoleIds(Collections.singletonList(1L));
        assertNotNull(map);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> map = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Collections.singletonList(1L));
        assertNotNull(map);
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> userIds = approvalPermissionService.getCandidateUserIds(Collections.singletonList(1L));
        assertNotNull(userIds);
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> roles = approvalPermissionService.getCandidateRoles(1L, true);
        assertEquals(Collections.emptyList(), roles);
    }

    @Test
    public void testIsApprovable() {
        boolean approvable = approvalPermissionService.isApprovable(1L);
        assertFalse(approvable);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> instances = approvalPermissionService.getApprovableApprovalInstances();
        assertNotNull(instances);
    }

    @Test
    public void testGetApprovableApprovalInstancesForUserId() {
        List<UserTaskInstanceEntity> instances = approvalPermissionService.getApprovableApprovalInstances(1L);
        assertNotNull(instances);
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(1L, "resourceRoleIdentifier");
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> instances = approvalPermissionService.listApprovableExternalInstances();
        assertNotNull(instances);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> map = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singletonList(1L));
        assertNotNull(map);
    }
}
