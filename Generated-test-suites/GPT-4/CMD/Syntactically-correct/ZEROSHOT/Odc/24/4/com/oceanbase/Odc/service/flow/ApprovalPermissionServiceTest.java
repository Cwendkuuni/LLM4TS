package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    ApprovalPermissionService approvalPermissionService;

    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserService userService;

    @Mock
    AuthenticationFacade authenticationFacade;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetCandidates() {
        Set<User> result = approvalPermissionService.getCandidates(123L, Boolean.TRUE);
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(Collections.singleton(123L));
        assertNotNull(result);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Collections.singleton(123L));
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> result = approvalPermissionService.getCandidateUserIds(Collections.singleton(123L));
        assertNotNull(result);
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> result = approvalPermissionService.getCandidateRoles(123L, Boolean.TRUE);
        assertNotNull(result);
    }

    @Test
    public void testIsApprovable() {
        boolean result = approvalPermissionService.isApprovable(123L);
        assertFalse(result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertNotNull(result);
    }

    @Test
    public void testGetApprovableApprovalInstancesBasedOnUserId() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(123L);
        assertNotNull(result);
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(123L, "Identifier");
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertNotNull(result);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singleton(123L));
        assertNotNull(result);
    }
}
