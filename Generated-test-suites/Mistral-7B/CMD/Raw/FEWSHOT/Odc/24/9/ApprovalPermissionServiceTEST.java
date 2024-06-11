package com.oceanbase.odc.service.flow;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepositoryImpl;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepositoryTestDataBuilder;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.RoleRepositoryImpl;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserEntityTestDataBuilder;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRepositoryImpl;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntityTestDataBuilder;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntityTestDataBuilder;
import com.oceanbase.odc.metadb.iam.resourcerole.ResourceRoleService;
import com.oceanbase.odc.metadb.iam.resourcerole.ResourceRoleServiceImpl;
import com.oceanbase.odc.metadb.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeImpl;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.RoleTestDataBuilder;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.iam.model.UserTestDataBuilder;

import lombok.NonNull;

/**
 * Approval Permission Service Test
 *
 * @author yh263208
 */
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

    private UserTaskInstanceCandidateEntity testUserTaskInstanceCandidate;
    private UserTaskInstanceCandidateEntity testUserTaskInstanceCandidate2;
    private UserTaskInstanceEntity testUserTaskInstance;
    private UserTaskInstanceEntity testUserTaskInstance2;
    private UserEntity testUser;
    private Role role;
    private RoleTestDataBuilder roleTestDataBuilder;
    private UserTestDataBuilder userTestDataBuilder;
    private UserRoleEntity userRole;
    private UserEntityTestDataBuilder userEntityTestDataBuilder;
    private UserResourceRoleEntity userResourceRole;
    private UserResourceRoleEntityTestDataBuilder userResourceRoleTestDataBuilder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        roleTestDataBuilder = new RoleTestDataBuilder();
        userTestDataBuilder = new UserTestDataBuilder();
        userEntityTestDataBuilder = new UserEntityTestDataBuilder();
        userResourceRoleTestDataBuilder = new UserResourceRoleEntityTestDataBuilder();

        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());

        role = roleTestDataBuilder.build();
        testUser = userTestDataBuilder.build();
        userRole = userRoleTestDataBuilder.build(testUser, role);
        userResourceRole = userResourceRoleTestDataBuilder.build(testUser, role.getId().toString());

        testUserTaskInstance = new UserTaskInstanceEntity();
        testUserTaskInstance.setId(1L);
        testUserTaskInstance.setStatus(FlowNodeStatus.EXECUTING);

        testUserTaskInstanceCandidate = new UserTaskInstanceCandidateEntity();
        testUserTaskInstanceCandidate.setApprovalInstanceId(testUserTaskInstance.getId());
        testUserTaskInstanceCandidate.setRoleId(role.getId());

        testUserTaskInstance2 = new UserTaskInstanceEntity();
        testUserTaskInstance2.setId(2L);
        testUserTaskInstance2.setStatus(FlowNodeStatus.EXECUTING);

        testUserTaskInstanceCandidate2 = new UserTaskInstanceCandidateEntity();
        testUserTaskInstanceCandidate2.setApprovalInstanceId(testUserTaskInstance2.getId());
        testUserTaskInstanceCandidate2.setRoleId(role.getId());

        when(userRepository.findByRoleIdsAndEnabled(expected, enabled)).thenReturn(Collections.emptyList());
        when(userRepository.findByUserIdsAndEnabled(expected, enabled)).thenReturn(Collections.singletonList(testUser));
        when(userRepository.findByUserIdsOrRoleIds(expected, expected2)).thenReturn(Collections.singletonList(testUser));

        when(roleRepository.findByRoleIdsAndEnabled(expected, enabled)).thenReturn(Collections.singletonList(role));

        when(userRoleRepository.findByRoleIdIn(expected)).thenReturn(Collections.singletonList(userRole));

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(expected, expected2)).thenReturn(Collections.emptyList());

        when(userTaskInstanceRepository.findByStatus(expected)).thenReturn(Collections.singletonList(testUserTaskInstance));
        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(Collections.singletonList(testUserTaskInstance));
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, expected)).thenReturn(
                Arrays.asList(testUserTaskInstance, testUserTaskInstance2));
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, expected)).thenReturn(
                Arrays.asList(testUserTaskInstance, testUserTaskInstance2));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(
                Arrays.asList(testUserTaskInstanceCandidate, testUserTaskInstanceCandidate2));
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(
                Arrays.asList(testUserTaskInstanceCandidate, testUserTaskInstanceCandidate2));
    }

    @After
    public void tearDown() {
        reset(userService, resourceRoleService, authenticationFacade, userTaskCandidateRepository,
                userTaskInstanceRepository, userRepository, roleRepository, userRoleRepository,
                userResourceRoleRepository);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        verify(userRepository).findByRoleIdsAndEnabled(role.getId(), enabled);
        verifyZeroInteractions(userRepository, userRoleRepository, userResourceRoleRepository);
        verify(roleRepository).findByRoleIdsAndEnabled(role.getId(), enabled);
        assertNull(result);

        // Candidate is a user
        testUser.setId(1L);
        result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        verify(userRepository).findByUserIdsAndEnabled(Arrays.asList(testUser.getId()), enabled);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertEquals(testUser, result.iterator().next());

        // Candidate is a role
        role.setId(1L);
        result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        verify(roleRepository).findByRoleIdsAndEnabled(role.getId(), enabled);
        verifyZeroInteractions(userRepository, userRoleRepository, userResourceRoleRepository);
        assertEquals(role, result.iterator().next());

        // Candidate is a user and a role
        testUser.setId(2L);
        role.setId(2L);
        Set<User> result2 = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        verify(userRepository).findByUserIdsOrRoleIds(Arrays.asList(testUser.getId()), Collections.singleton(role.getId()));
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertEquals(testUser, result2.iterator().next());
        assertEquals(role, result2.iterator().next());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        verify(userTaskCandidateRepository).findByApprovalInstanceIds(approvalInstanceIds);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(1L));
        assertTrue(result.get(1L).size() > 0);
        assertTrue(result.get(2L).size() > 0);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

        verify(userTaskCandidateRepository).findByApprovalInstanceIds(approvalInstanceIds);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(1L));
        assertTrue(result.get(1L).size() > 0);
        assertTrue(result.get(2L).size() > 0);
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        verify(userTaskCandidateRepository).findByApprovalInstanceIds(approvalInstanceIds);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertEquals(2, result.size());
        assertTrue(result.contains(testUser.getId()));
        assertTrue(result.contains(testUser.getId() + 1));
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertNull(result);
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;

        boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.EXECUTING);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertFalse(result);

        testUserTaskInstance.setId(approvalInstanceId);
        result = approvalPermissionService.isApprovable(approvalInstanceId);
        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.EXECUTING);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertTrue(result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();

        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.EXECUTING);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetApprovableApprovalInstancesUserId() {
        long userId = 1L;

        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);

        verify(userTaskInstanceRepository).findByCandidateUserIdOrResourceRoleIdentifier(userId, null);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "resourceRoleIdentifier";

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        verify(userTaskCandidateRepository).save(any(UserTaskInstanceCandidateEntity.class));
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();

        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.EXECUTING);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Set<Long> flowInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));

        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

        verify(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
                FlowNodeStatus.EXECUTING);
        verifyZeroInteractions(userRepository, roleRepository, userRoleRepository, userResourceRoleRepository);
        assertEquals(0, result.size());
    }
}