package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepositoryImpl;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntityMock;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.RoleRepositoryMock;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserEntityMock;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRepositoryMock;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepositoryMock;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepositoryMock;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.ApprovalPermissionServiceImpl;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.iam.model.RoleMock;
import com.oceanbase.odc.service.iam.model.UserMock;

@Component
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionServiceImpl approvalPermissionService;

    @Mock
    @Qualifier("userTaskCandidateRepository")
    private UserTaskInstanceCandidateRepository repository;

    @Mock
    @Qualifier("userTaskInstanceRepository")
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Mock
    @Qualifier("roleRepository")
    private RoleRepository roleRepository;

    @Mock
    @Qualifier("userRoleRepository")
    private UserRoleRepository userRoleRepository;

    @Mock
    @Qualifier("userResourceRoleRepository")
    private UserResourceRoleRepository userResourceRoleRepository;

    @Mock
    @Qualifier("authenticationFacade")
    private AuthenticationFacadeMock authenticationFacade;

    @Mock
    @Qualifier("resourceRoleService")
    private ResourceRoleService resourceRoleService;

    @Mock
    @Qualifier("userService")
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenAnswer(new Answer<List<UserTaskInstanceEntity>>() {
            @Override
            public List<UserTaskInstanceEntity> answer(final InvocationOnMock invocation) throws Throwable {
                return Arrays.asList(new UserTaskInstanceEntityMock(1), new UserTaskInstanceEntityMock(2));
            }
        });
        when(userRepository.findByUserIds(new HashSet<Long>(Arrays.asList(1, 2)))).thenReturn(
                Arrays.asList(new UserEntityMock(1), new UserEntityMock(2)));
        when(roleRepository.findByRoleIdsAndEnabled(new HashSet<Long>(Arrays.asList(1, 2)), true)).thenReturn(
                Arrays.asList(new RoleMock(1), new RoleMock(2)));
        when(userRoleRepository.findByRoleIdIn(new HashSet<Long>(Arrays.asList(1, 2)))).thenReturn(
                Arrays.asList(new UserRoleEntity(1, 1L, 1L)));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(new HashSet<Long>(
                Arrays.asList(1, 2)), Arrays.asList("role1", "role2"))).thenReturn(
                Arrays.asList(new UserResourceRoleEntity(1, 1L, 1L, "role1"), new UserResourceRoleEntity(2, 1L, 2L,
                        "role2")));
    }

    @Test
    public void testGetCandidates() {
        long approvalInstanceId = 1;
        boolean enabled = true;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        List<User> expectedCandidates = Arrays.asList(new UserMock(1, "user1"), new UserMock(2, "user2"));
        assertEquals(expectedCandidates, candidates);

        // Test when no candidates found
        approvalInstanceId = 3;
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertTrue(candidates.isEmpty());

        // Test when only roles are candidates
        approvalInstanceId = 4;
        enabled = false;
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        List<Role> expectedRoles = Arrays.asList(new RoleMock(1), new RoleMock(2));
        assertEquals(expectedRoles, candidates.stream().map(user -> (Role) user).collect(Collectors.toList()));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1, 2);
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        Map<Long, Set<Long>> expectedResult = new HashMap<>();
        expectedResult.put(1L, new HashSet<>(Arrays.asList(1, 2)));
        expectedResult.put(2L, new HashSet<>(Arrays.asList(3, 4)));
        assertEquals(expectedResult, result);

        // Test when no approval instance ids provided
        approvalInstanceIds = Collections.emptyList();
        result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1, 2);
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                approvalInstanceIds);
        Map<Long, Set<String>> expectedResult = new HashMap<>();
        expectedResult.put(1L, new HashSet<>(Arrays.asList("role1", "role2")));
        expectedResult.put(2L, new HashSet<>(Arrays.asList("role3", "role4")));
        assertEquals(expectedResult, result);

        // Test when no approval instance ids provided
        approvalInstanceIds = Collections.emptyList();
        result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidateUserIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1, 2);
        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        Set<Long> expectedResult = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        assertEquals(expectedResult, result);

        // Test when no approval instance ids provided
        approvalInstanceIds = Collections.emptyList();
        result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        long approvalInstanceId = 1;
        boolean enabled = true;
        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        List<Role> expectedResult = Arrays.asList(new RoleMock(1), new RoleMock(2));
        assertEquals(expectedResult, result);

        // Test when no candidates found
        approvalInstanceId = 3;
        result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertTrue(result.isEmpty());

        // Test when only roles are candidates
        approvalInstanceId = 4;
        enabled = false;
        result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        List<Role> expectedRoles = Arrays.asList(new RoleMock(1), new RoleMock(2));
        assertEquals(expectedRoles, result);
    }

    @Test
    public void testIsApprovable() {
        long approvalInstanceId = 1;
        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));

        // Test when approval instance id not approvable
        approvalInstanceId = 3;
        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> expectedResult = Arrays.asList(new UserTaskInstanceEntityMock(1),
                new UserTaskInstanceEntityMock(2));
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertEquals(expectedResult, result);

        // Test when no approvable approval instances found
        when(authenticationFacade.currentUserId()).thenReturn(3L);
        result = approvalPermissionService.getApprovableApprovalInstances();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        List<UserTaskInstanceEntity> expectedResult = Arrays.asList(new UserTaskInstanceEntityMock(1),
                new UserTaskInstanceEntityMock(2));
        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(1);
        assertEquals(expectedResult, result);

        // Test when no approvable approval instances found for given user id
        when(authenticationFacade.currentUserId()).thenReturn(3L);
        result = approvalPermissionService.getApprovableApprovalInstances(1);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSetCandidateResourceRole() {
        long approvalInstanceId = 1;
        String resourceRoleIdentifier = "role1";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
        verify(repository).save(argThat(entity -> entity.getApprovalInstanceId() == approvalInstanceId
                && entity.getResourceRoleIdentifier().equals(resourceRoleIdentifier)));
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> expectedResult = Arrays.asList(new UserTaskInstanceEntityMock(1),
                new UserTaskInstanceEntityMock(2));
        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertEquals(expectedResult, result);

        // Test when no approvable external instances found
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(Collections.emptyList());
        result = approvalPermissionService.listApprovableExternalInstances();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        List<Long> flowInstanceIds = Arrays.asList(1, 2);
        Map<Long, Set<UserEntity>> expectedResult = new HashMap<>();
        expectedResult.put(1L, new HashSet<>(Arrays.asList(new UserEntityMock(1), new UserEntityMock(2))));
        expectedResult.put(2L, new HashSet<>(Arrays.asList(new UserEntityMock(3), new UserEntityMock(4))));
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertEquals(expectedResult, result);

        // Test when no flow instance ids provided
        flowInstanceIds = Collections.emptyList();
        result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertTrue(result.isEmpty());
    }
}