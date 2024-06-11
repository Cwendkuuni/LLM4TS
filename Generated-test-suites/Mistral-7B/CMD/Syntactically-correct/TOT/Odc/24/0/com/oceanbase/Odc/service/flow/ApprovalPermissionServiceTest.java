package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
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

    @Mock
    private ResourceRoleService resourceRoleService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates_enabled_true() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, 2L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        when(userRepository.findByRoleIdsAndEnabled(eq(Collections.singletonList(2L)), eq(true)))
                .thenReturn(Arrays.asList(new UserEntity(2L, "user2", "password2")));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(new UserEntity(2L, "user2", "password2")));

        verify(userRepository, times(1))
                .findByRoleIdsAndEnabled(eq(Collections.singletonList(2L)), eq(true));
    }

    @Test
    public void testGetCandidates_enabled_false() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, 2L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        when(userRepository.findByRoleIdsAndEnabled(eq(Collections.singletonList(2L)), eq(false)))
                .thenReturn(Collections.emptyList());

        when(userRepository.findByUserIdsAndEnabled(eq(Collections.singletonList(1L)), eq(true)))
                .thenReturn(Arrays.asList(new UserEntity(1L, "user1", "password1")));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(new UserEntity(1L, "user1", "password1")));

        verify(userRepository, times(2))
                .findByRoleIdsAndEnabled(eq(Collections.singletonList(2L)), anyBoolean());
    }

    @Test
    public void testGetCandidates_null_enabled() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, 2L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        when(userRepository.findByRoleIdsAndEnabled(anyList(), eq(null)))
                .thenReturn(Collections.emptyList());

        when(userRepository.findByUserIdsAndEnabled(eq(Collections.singletonList(1L)), eq(true)))
                .thenReturn(Arrays.asList(new UserEntity(1L, "user1", "password1")));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(1, candidates.size());
        assertTrue(candidates.contains(new UserEntity(1L, "user1", "password1")));

        verify(userRepository, times(3))
                .findByRoleIdsAndEnabled(anyList(), anyBoolean());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds_empty_approval_instance_ids() {
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(Collections.emptySet());
        assertEquals(0, result.size());
    }

    // Add other test cases for other methods here
}