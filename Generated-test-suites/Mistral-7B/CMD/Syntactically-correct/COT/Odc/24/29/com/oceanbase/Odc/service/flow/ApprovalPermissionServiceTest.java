package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class ApprovalPermissionServiceTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserService userService;

    @Mock
    private ResourceRoleService resourceRoleService;

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

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        // Test with valid input
        Long approvalInstanceId = 1L;
        Set<Boolean> enabledSet = new HashSet<>(Arrays.asList(true, false, null));
        for (Boolean enabled : enabledSet) {
            Set<User> expectedUsers = new HashSet<>();
            expectedUsers.add(new User(1L, "user1"));
            expectedUsers.add(new User(2L, "user2"));
            when(userRepository.findByRoleIdsAndEnabled(anySet(), eq(enabled)))
                    .thenReturn(Arrays.asList(new User(3L, "roleUser1"), new User(4L, "roleUser2")));
            when(roleRepository.findByRoleIdsAndEnabled(new HashSet<>(Arrays.asList(1L, 2L)), eq(enabled)))
                    .thenReturn(Arrays.asList(new Role(5L, "role1")));
            Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
            assertEquals(expectedUsers, actualUsers);
        }

        // Test with empty input
        Set<User> emptySet = Collections.emptySet();
        when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(emptySet, actualUsers);

        // Test with null input
        when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
                .thenThrow(new NullPointerException());
        try {
            approvalPermissionService.getCandidates(approvalInstanceId, null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Add other test methods for other public methods in the ApprovalPermissionService class
}