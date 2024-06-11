package com.oceanbase.odc.service.flow;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.UserService;

import java.util.*;

public class ApprovalPermissionServiceTest {

    @Autowired
    ApprovalPermissionService approvalPermissionService;

    @Mock
    UserService userService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates(){
        Set<User> users = approvalPermissionService.getCandidates(1L, true);
        Assert.assertNotNull(users);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds(){
        List<Long> approvalInstanceIds = Arrays.asList(1L,2L,3L);
        Map<Long, Set<Long>> roleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        Assert.assertNotNull(roleIds);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds(){
        List<Long> approvalInstanceIds = Arrays.asList(1L,2L,3L);
        Map<Long, Set<String>> identifierIds = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        Assert.assertNotNull(identifierIds);
    }

    @Test
    public void testGetCandidateUserIds(){
        List<Long> approvalInstanceIds = Arrays.asList(1L,2L,3L);
        Set<Long> userIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        Assert.assertNotNull(userIds);
    }

    @Test
    public void testGetCandidateRoles(){
        List<Role> roles = approvalPermissionService.getCandidateRoles(1L,true);
        Assert.assertNotNull(roles);
    }

    @Test
    public void testIsApprovable(){
        boolean approvable = approvalPermissionService.isApprovable(1L);
        Assert.assertNotNull(approvable);
    }

    @Test
    public void testGetApprovableApprovalInstances(){
        List<UserTaskInstanceCandidateEntity> entities = approvalPermissionService.getApprovableApprovalInstances();
        Assert.assertNotNull(entities);
    }

    @Test
    public void testSetCandidateResourceRole(){
        approvalPermissionService.setCandidateResourceRole(1L, "test");
        Mockito.verify(userTaskCandidateRepository, Mockito.times(1)).save(Mockito.any(UserTaskInstanceCandidateEntity.class));
    }

    @Test
    public void testListApprovableExternalInstances(){
        List<UserTaskInstanceCandidateEntity> entities = approvalPermissionService.listApprovableExternalInstances();
        Assert.assertNotNull(entities);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds(){
         List<Long> flowInstanceIds = Arrays.asList(1L,2L,3L);
         Map<Long, Set<UserEntity>> usersMap = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
         Assert.assertNotNull(usersMap);
    }
    
}