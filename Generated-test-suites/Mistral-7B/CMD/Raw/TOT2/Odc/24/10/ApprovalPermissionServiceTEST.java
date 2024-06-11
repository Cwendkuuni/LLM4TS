package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Before;
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
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

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

    @Before
    public void setUp() {
        // Initialize mocks with necessary behavior
    }

    @After
    public void tearDown() {
        // Reset mocks to default behavior
        verifyNoMoreInteractions(userService, resourceRoleService, authenticationFacade,
                userTaskCandidateRepository, userTaskInstanceRepository, userRepository, roleRepository,
                userRoleRepository, userResourceRoleRepository);
    }

    @Test
    public void testGetCandidates_EmptyList() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Set<User> expectedCandidates = Collections.emptySet();

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());

        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expectedCandidates, actualCandidates);
    }

    @Test
    public void testGetCandidates_EnabledUsers() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        when(userRepository.findByRoleIdsAndEnabled(anyLongArray(), enabled)).thenReturn(Arrays.asList(user1));

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        when(userRepository.findByUserIdsAndEnabled(anyLongArray(), enabled)).thenReturn(Arrays.asList(user2));

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(user1.getId());
        entities.add(entity1);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);
        entities.add(entity2);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        Set<User> expectedCandidates = new HashSet<>();
        expectedCandidates.add(user1);
        expectedCandidates.add(user2);

        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expectedCandidates, actualCandidates);
    }

    @Test
    public void testGetCandidates_DisabledUsers() {
        Long approvalInstanceId = 1L;
        Boolean enabled = false;

        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        when(userRepository.findByRoleIdsAndEnabled(anyLongArray(), enabled)).thenReturn(Arrays.asList(user1));

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        when(userRepository.findByUserIdsAndEnabled(anyLongArray(), enabled)).thenReturn(Arrays.asList(user2));

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(user1.getId());
        entities.add(entity1);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);
        entities.add(entity2);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        Set<User> expectedCandidates = new HashSet<>();
        expectedCandidates.add(user1);
        expectedCandidates.add(user2);

        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertTrue(actualCandidates.isEmpty());
    }

    @Test
    public void testGetCandidates_BothUsersAndRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = null;

        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        when(userRepository.findByRoleIds(anyLongArray())).thenReturn(Collections.emptyList());
        when(userRepository.findByUserIds(anyLongArray())).thenReturn(Arrays.asList(user1));

        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        when(roleRepository.findByRoleIds(anyLongArray())).thenReturn(Arrays.asList(role1));

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(user1.getId());
        entities.add(entity1);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(role1.getId());
        entities.add(entity2);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        Set<User> expectedCandidates = new HashSet<>();
        expectedCandidates.add(user1);

        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expectedCandidates, actualCandidates);
    }

    // Add more test cases for other methods like getInstanceId2CandidateRoleIds, getInstanceId2CandidateResourceRoleIdentifierIds,
    // getCandidateUserIds, getCandidateRoles, isApprovable, listApprovableExternalInstances, and setCandidateResourceRole.

    // Test edge cases and exception handling scenarios for each method.

    // Combine all the individual test cases into a complete JUnit 4 test file.
}