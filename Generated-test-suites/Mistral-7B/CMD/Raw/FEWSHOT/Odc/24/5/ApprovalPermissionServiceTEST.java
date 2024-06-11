package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

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

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Test
    public void testGetCandidates() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        User user1 = new User();
        User user2 = new User();
        Role role1 = new Role();
        Role role2 = new Role();
        UserRoleEntity userRoleEntity1 = new UserRoleEntity();
        UserRoleEntity userRoleEntity2 = new UserRoleEntity();
        UserResourceRoleEntity userResourceRoleEntity1 = new UserResourceRoleEntity();
        UserResourceRoleEntity userResourceRoleEntity2 = new UserResourceRoleEntity();

        when(userService.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(Collections.singletonList(role1));
        when(userRepository.findByUserIdsAndEnabled(anyCollection(), anyBoolean())).thenReturn(
                Collections.singletonList(user1));
        when(roleRepository.findByRoleIdsAndEnabled(anyCollection(), anyBoolean())).thenReturn(
                Collections.singletonList(role2));
        when(userRoleRepository.findByRoleIdIn(anyCollection())).thenReturn(Arrays.asList(userRoleEntity1, userRoleEntity2));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anyCollection(), anyCollection()))
                .thenReturn(Arrays.asList(userResourceRoleEntity1, userResourceRoleEntity2));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(
                Arrays.asList(
                        new UserTaskInstanceCandidateEntity(),
                        new UserTaskInstanceCandidateEntity()));

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(user1));
        assertTrue(candidates.contains(user2));

        // Clean Up
        verify(userService, times(1)).findByRoleIdsAndEnabled(anyLong(), anyBoolean());
        verify(userRepository, times(1)).findByUserIdsAndEnabled(anyCollection(), anyBoolean());
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(anyCollection(), anyBoolean());
        verify(userRoleRepository, times(1)).findByRoleIdIn(anyCollection());
        verify(userResourceRoleRepository, times(1)).findByResourceIdsAndResourceRoleIdsIn(anyCollection(), anyCollection());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
    }

    @Test
    public void testGetCandidates_whenUserIdsAndRoleIdsAreEmpty() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = null;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(
                Arrays.asList(entity1, entity2));

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertTrue(candidates.isEmpty());

        // Clean Up
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
    }

    @Test
    public void testGetCandidates_whenOnlyRoleIdsArePresent() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = null;
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.singletonList(entity));

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertTrue(candidates.isEmpty());

        // Clean Up
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
    }

    @Test
    public void testGetCandidates_whenOnlyUserIdsArePresent() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = null;
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.singletonList(entity));

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        assertTrue(candidates.isEmpty());

        // Clean Up
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
    }

    // Add test cases for other methods: getInstanceId2CandidateRoleIds(), getInstanceId2CandidateResourceRoleIdentifierIds(),
    // getCandidateUserIds(), getCandidateRoles(), isApprovable(), getApprovableApprovalInstances(),
    // setCandidateResourceRole(), listApprovableExternalInstances(), getCandidatesByFlowInstanceIds()
}