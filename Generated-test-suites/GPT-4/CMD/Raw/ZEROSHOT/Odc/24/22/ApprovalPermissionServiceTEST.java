
package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ApprovalPermissionServiceTest {
    
    @InjectMocks
    ApprovalPermissionService approvalPermissionService;
    
    @Mock
    UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
    
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setUserId(1L);
        entity.setRoleId(2L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(Collections.singletonList(entity));

        Set<User> users = approvalPermissionService.getCandidates(1L, true);

        Assert.assertFalse(users.isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setRoleId(2L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(Collections.singletonList(entity));

        Set<Role> roles = approvalPermissionService.getCandidateRoles(1L, true);

        Assert.assertFalse(roles.isEmpty());
    }

    @Test
    public void testIsApprovable() {
        ApprovalPermissionService spyService = spy(approvalPermissionService);        
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        when(spyService.getApprovableApprovalInstances()).thenReturn(Collections.singletonList(entity));

        boolean result = spyService.isApprovable(1L);

        Assert.assertTrue(result);
    }

    @Test
    public void testSetCandidateResourceRole() {
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setApprovalInstanceId(1L);
        entity.setResourceRoleIdentifier("role");

        approvalPermissionService.setCandidateResourceRole(1L, "role");

        verify(userTaskCandidateRepository, times(1)).save(entity);
    }
}
