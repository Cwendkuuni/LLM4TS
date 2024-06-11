package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {
    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void getCandidatesTest() {
        Set<User> candidates = approvalPermissionService.getCandidates(1L, true);
        Assert.assertNotNull(candidates);
    }

    @Test
    public void getInstanceId2CandidateRoleIdsTest() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        Assert.assertNotNull(result);
    }

    @Test
    public void getInstanceId2CandidateResourceRoleIdentifierIdsTest() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        Assert.assertNotNull(result);
    }

    @Test
    public void getCandidateUserIdsTest() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        Set<Long> users = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        Assert.assertNotNull(users);
    }

    @Test
    public void getCandidateRolesTest() {
        List<Role> roles = approvalPermissionService.getCandidateRoles(1L, true);
        Assert.assertNotNull(roles);
    }

    @Test
    public void isApprovableTest() {
        boolean result = approvalPermissionService.isApprovable(1L);
        Assert.assertNotNull(result);
    }

    @Test
    public void getApprovableApprovalInstancesTest() {
        List<UserTaskInstanceEntity> entities = approvalPermissionService.getApprovableApprovalInstances();
        Assert.assertNotNull(entities);
    }

    @Test
    public void getApprovableApprovalInstancesForUserTest() {
        List<UserTaskInstanceEntity> entities = approvalPermissionService.getApprovableApprovalInstances(1L);
        Assert.assertNotNull(entities);
    }

    @Test
    public void listApprovableExternalInstancesTest() {
        List<UserTaskInstanceEntity> entities = approvalPermissionService.listApprovableExternalInstances();
        Assert.assertNotNull(entities);
    }

    @Test
    public void getCandidatesByFlowInstanceIdsTest() {
        Set<Long> flowInstanceIds = new HashSet<>();
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        Assert.assertNotNull(result);
    }

}
