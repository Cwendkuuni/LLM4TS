package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

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

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    @Before
    public void setup() {
        Mockito.reset(
                userService, 
                resourceRoleService, 
                authenticationFacade, 
                userTaskCandidateRepository,
                userTaskInstanceRepository, 
                userRepository, 
                roleRepository, 
                userRoleRepository, 
                userResourceRoleRepository
        );
    }

    @Test
    public void testGetCandidates() {
        //Configure mocks
        when(userTaskCandidateRepository.findByApprovalInstanceId(any(Long.class))).thenReturn(new ArrayList<>());
        when(userRepository.findByRoleIdsAndEnabled(any(), any())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(any(), any())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsOrRoleIds(any(), any(), any())).thenReturn(new ArrayList<>());
        
        //Call method and verify
        Set<User> result = approvalPermissionService.getCandidates(1L, true);
        assertTrue(result.isEmpty());
    }

    //... Repeated for every method

}
