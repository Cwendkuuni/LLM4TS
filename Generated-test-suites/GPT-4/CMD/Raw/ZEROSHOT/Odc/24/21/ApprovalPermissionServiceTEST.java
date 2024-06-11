
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService permissionService;

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
    public void setUp() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setUserId(1L);

        when(userTaskCandidateRepository.findByApprovalInstanceId(1L))
                .thenReturn(Collections.singletonList(entity));
        when(userRepository.findByUserIdsAndEnabled(new HashSet<>(Arrays.asList(1L)), true))
                .thenReturn(Collections.singletonList(userEntity));
    }

    @Test
    public void getCandidatesTest() {
        Set<User> candidates = permissionService.getCandidates(1L, true);

        assertEquals(1, candidates.size());
        assertTrue(candidates.stream().map(User::getId).anyMatch(id -> id.equals(1L)));
    }
    
    // Similar way, write test cases for all other methods
}
