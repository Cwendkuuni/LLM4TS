package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade; 
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    ApprovalPermissionService approvalPermissionService;

    @Mock
    UserService userService;

    @Mock
    ResourceRoleService resourceRoleService;

    @Mock
    AuthenticationFacade authenticationFacade;

    @Mock
    UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    UserRoleRepository userRoleRepository;

    @Mock
    UserResourceRoleRepository userResourceRoleRepository;
    
    @Test
    public void testGetCandidates() {
        Set<User> expected = Collections.emptySet();
        when(userTaskCandidateRepository.findByApprovalInstanceId(1L)).thenReturn(Collections.emptyList());
        Set<User> result = approvalPermissionService.getCandidates(1L, true);
        assertEquals(expected, result);
        assertFalse(result.contains(new User()));
    }
    
    // Write more test cases here for all methods in ApprovalPermissionService
    
}
