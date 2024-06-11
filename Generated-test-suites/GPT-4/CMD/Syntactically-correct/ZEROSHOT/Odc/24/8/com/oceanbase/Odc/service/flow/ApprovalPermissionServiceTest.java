
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ApprovalPermissionServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetCandidates() throws Exception {
        when(userService.findByUserIdsAndEnabled(any(Set.class), any(Boolean.class)))
                .thenReturn(Arrays.asList(new UserEntity()));

        Set<User> result = approvalPermissionService.getCandidates(1L, true);
        assertEquals(new HashSet(Arrays.asList(new User())), result);
    }

    @Test
    public void testGetCandidateUserIds() throws Exception {
        Set<Long> result = approvalPermissionService.getCandidateUserIds(new HashSet(Arrays.asList("userId1")));
        assertEquals(new HashSet(Arrays.asList("userId1")), result);
    }

    @Test
    public void testGetCandidateRoles() throws Exception {
        when(permissionService.findByRoleIdsAndEnabled(any(Set.class), any(Boolean.class)))
                .thenReturn(Arrays.asList(new RoleEntity()));

        Set<Role> result = approvalPermissionService.getCandidateRoles(1L, true);
        assertEquals(new HashSet(Arrays.asList(new Role())), result);
    }

    @Test
    public void testIsApprovable() throws Exception {
        boolean result = approvalPermissionService.isApprovable(1L);
        assertEquals(true, result);
    }

    @Test
    public void testGetApprovableApprovalInstances() throws Exception {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertEquals(new ArrayList<UserTaskInstanceEntity>(), result);
    }


    @Test
    public void testListApprovableExternalInstances() throws Exception {
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertEquals(new ArrayList<UserTaskInstanceEntity>(), result);
    }
}
