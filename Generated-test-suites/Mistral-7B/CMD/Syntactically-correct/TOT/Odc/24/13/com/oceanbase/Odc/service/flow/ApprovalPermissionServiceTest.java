package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;
import org.springframework.stereotype.Service;

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

@Service
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

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        AuthenticationFacade authenticationFacadeMock = mock(AuthenticationFacade.class);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "authenticationFacade",
                authenticationFacadeMock);
    }

    @Test
    public void testGetCandidates_Normal() {
        Long approvalInstanceId = 1L;
        Set<UserEntity> expectedUsers = new HashSet<>();
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        when(userRepository.findByRoleIdsAndEnabled(Mockito.anySet(), Mockito.anyBoolean()))
                .thenReturn(expectedUsers);

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertEquals(expectedUsers.size(), result.size());
        for (User user : result) {
            assertTrue(expectedUsers.contains(user.getUserEntity()));
        }

        verify(userRepository, times(1)).findByRoleIdsAndEnabled(anySet(), anyBoolean());
    }

    @Test
    public void testGetCandidates_NoUsersOrRoles() {
        Long approvalInstanceId = 1L;
        when(userRepository.findByRoleIdsAndEnabled(Mockito.anySet(), Mockito.anyBoolean()))
                .thenReturn(Collections.emptySet());

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByRoleIdsAndEnabled(anySet(), anyBoolean());
    }

    @Test
    public void testGetCandidates_OnlyRoles() {
        Long approvalInstanceId = 1L;
        Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
        Set<UserEntity> expectedUsers = new HashSet<>();
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        when(roleRepository.findByRoleIds(Mockito.anySet()))
                .thenReturn(expectedUsers.stream().map(UserEntity::getRole).collect(Collectors.toSet()));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertEquals(expectedUsers.size(), result.size());
        for (User user : result) {
            assertTrue(expectedUsers.contains(user.getUserEntity()));
        }

        verify(roleRepository, times(1)).findByRoleIds(anySet());
    }

    @Test
    public void testGetCandidates_OnlyUsers() {
        Long approvalInstanceId = 1L;
        Set<UserEntity> expectedUsers = new HashSet<>();
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        when(userRepository.findByUserIdsAndEnabled(Mockito.anySet(), Mockito.anyBoolean()))
                .thenReturn(expectedUsers);

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertEquals(expectedUsers.size(), result.size());
        for (User user : result) {
            assertTrue(expectedUsers.contains(user.getUserEntity()));
        }

        verify(userRepository, times(1)).findByUserIdsAndEnabled(anySet(), anyBoolean());
    }

    @Test
    public void testGetCandidates_BothUsersAndRoles() {
        Long approvalInstanceId = 1L;
        Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
        Set<Long> userIds = new HashSet<>(Arrays.asList(3L, 4L));
        Set<UserEntity> expectedUsers = new HashSet<>();
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        when(roleRepository.findByRoleIds(Mockito.anySet()))
                .thenReturn(expectedUsers.stream().map(UserEntity::getRole).collect(Collectors.toSet()));
        when(userRepository.findByUserIds(Mockito.anySet()))
                .thenReturn(expectedUsers);

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertEquals(expectedUsers.size(), result.size());
        for (User user : result) {
            assertTrue(expectedUsers.contains(user.getUserEntity()));
        }

        verify(roleRepository, times(1)).findByRoleIds(anySet());
        verify(userRepository, times(1)).findByUserIds(anySet());
    }

    @Test
    public void testGetCandidates_FalseEnabled() {
        Long approvalInstanceId = 1L;
        Set<UserEntity> expectedUsers = new HashSet<>();
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        expectedUsers.add(user1);
        expectedUsers.add(user2);
        when(userRepository.findByRoleIdsAndEnabled(Mockito.anySet(), false))
                .thenReturn(expectedUsers);

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, false);

        assertEquals(expectedUsers.size(), result.size());
        for (User user : result) {
            assertTrue(expectedUsers.contains(user.getUserEntity()));
        }

        verify(userRepository, times(1)).findByRoleIdsAndEnabled(anySet(), false);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds_Normal() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
        Map<Long, Set<Long>> expectedResult = new HashMap<>();
        Set<Long> roleIds1 = new HashSet<>(Arrays.asList(1L, 2L));
        Set<Long> roleIds2 = new HashSet<>(Arrays.asList(3L, 4L));
        expectedResult.put(1L, roleIds1);
        expectedResult.put(2L, roleIds2);
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity(), new UserTaskInstanceCandidateEntity()));

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertEquals(expectedResult, result);

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds_Empty() {
        List<Long> approvalInstanceIds = new ArrayList<>();

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertTrue(result.isEmpty());

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
    }

    // Other test methods for getInstanceId2CandidateResourceRoleIdentifierIds, isApprovable,
    // listApprovableExternalInstances, getCandidatesByFlowInstanceIds and their respective methods

    @Test
    public void testSetCandidateResourceRole_Normal() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "roleIdentifier";

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
    }
}