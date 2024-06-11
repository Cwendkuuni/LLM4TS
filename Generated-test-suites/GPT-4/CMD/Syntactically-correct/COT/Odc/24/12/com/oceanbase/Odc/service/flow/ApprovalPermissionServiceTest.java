package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ResourceRoleService resourceRoleService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void testGetCandidates() {
        Set<User> result = approvalPermissionService.getCandidates(1l, true);
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(new HashSet<>(Arrays.asList(1l)));
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(new HashSet<>(Arrays.asList(1l)));
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> result = approvalPermissionService.getCandidateUserIds(new HashSet<>(Arrays.asList(1l)));
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> result = approvalPermissionService.getCandidateRoles(1l, true);
        assertNotNull(result);
    }

    @Test
    public void testIsApprovable() {
        boolean result = approvalPermissionService.isApprovable(1l);
        assertFalse(result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertNotNull(result);
    }

    @Test
    public void testGetApprovableApprovalInstancesById() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(1l);
        assertNotNull(result);
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertNotNull(result);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(new HashSet<>(Arrays.asList(1l)));
        assertNotNull(result);
    }
}
