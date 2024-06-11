package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Collections;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks 
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationFacade authenticationFacade;

   
    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Set<User> mockUsers = new HashSet<>();

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
        when(approvalPermissionService.getCandidates(approvalInstanceId, true)).thenReturn(mockUsers);
        
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(mockUsers, result);
        verify(userService).getCurrentUserRoleIds();
    }
    
    
     @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Long approvalInstanceId = 1L;

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(approvalInstanceId));
        assertNotNull(result);
        verify(userService).getCurrentUserRoleIds();
    }

    @Test
    public void testGetCandidateUserIds() {
        Long approvalInstanceId = 1L;

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.EMPTY_SET);
        Set<Long> result = approvalPermissionService.getCandidateUserIds(Arrays.asList(approvalInstanceId));
        assertNotNull(result);
        verify(userService).getCurrentUserRoleIds();
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        List<Role> mockRoles = Arrays.asList(new Role());

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.EMPTY_SET);
        when(approvalPermissionService.getCandidateRoles(approvalInstanceId, true)).thenReturn(mockRoles);
        
        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        assertEquals(mockRoles, result);
        verify(userService).getCurrentUserRoleIds();
    }

    @Test
    public void testGetApprovableApprovalInstancesById() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> mockEntities = Arrays.asList(new UserTaskInstanceEntity());

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.EMPTY_SET);
        when(approvalPermissionService.getApprovableApprovalInstances(userId)).thenReturn(mockEntities);

        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);
        assertEquals(mockEntities, result);
        verify(userService).getCurrentUserRoleIds();
    }

    @Test
    public void testSetCandidateResourceRole() { 
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "role-1";

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        verify(userTaskCandidateRepository).save( Mockito.any(UserTaskInstanceCandidateEntity.class) );
    }

}