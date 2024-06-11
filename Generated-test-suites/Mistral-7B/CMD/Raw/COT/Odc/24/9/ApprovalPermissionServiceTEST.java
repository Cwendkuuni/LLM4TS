package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    public void testGetCandidates() throws Exception {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Set<UserEntity> expectedUsers = Set.of(new UserEntity());
        Mockito.when(userRepository.findByRoleIdsAndEnabled(Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(Collections.singletonList(new UserEntity())).thenReturn(Collections.emptyList());
        Mockito.when(userRepository.findByUserIdsAndEnabled(Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(Collections.singletonList(new UserEntity())).thenReturn(Collections.emptyList());
        Mockito.when(userRepository.findByUserIdsOrRoleIds(Mockito.anySet(), Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(Collections.singletonList(new UserEntity()));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(expectedUsers, result.stream().map(user -> new User(user)).collect(Collectors.toSet()));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() throws Exception {
        Collection<Long> approvalInstanceIds = Set.of(1L, 2L);
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(List.of(new UserTaskInstanceCandidateEntity()));

        Map<Long, Set<Long>> expectedResult = new HashMap<>();
        expectedResult.put(1L, Set.of(1L));
        expectedResult.put(2L, Set.of(2L, 3L));

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() throws Exception {
        Collection<Long> approvalInstanceIds = Set.of(1L, 2L);
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(List.of(new UserTaskInstanceCandidateEntity()));

        Map<Long, Set<String>> expectedResult = new HashMap<>();
        expectedResult.put(1L, Set.of("resourceRoleIdentifier1"));
        expectedResult.put(2L, Set.of("resourceRoleIdentifier2", "resourceRoleIdentifier3"));

        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetCandidateUserIds() throws Exception {
        Collection<Long> approvalInstanceIds = Set.of(1L, 2L);

        UserRoleEntity userRoleEntity1 = new UserRoleEntity();
        userRoleEntity1.setUserId(1L);
        UserRoleEntity userRoleEntity2 = new UserRoleEntity();
        userRoleEntity2.setUserId(2L);

        UserResourceRoleEntity userResourceRoleEntity1 = new UserResourceRoleEntity();
        userResourceRoleEntity1.setResourceId(1L);
        userResourceRoleEntity1.setResourceRoleIdentifier("resourceRoleIdentifier1");
        UserResourceRoleEntity userResourceRoleEntity2 = new UserResourceRoleEntity();
        userResourceRoleEntity2.setResourceId(2L);
        userResourceRoleEntity2.setResourceRoleIdentifier("resourceRoleIdentifier2");

        Mockito.when(userRoleRepository.findByRoleIdIn(Mockito.anySet()))
                .thenReturn(List.of(userRoleEntity1, userRoleEntity2));
        Mockito.when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(
                Mockito.anySet(), Mockito.anySet()))
                .thenReturn(List.of(userResourceRoleEntity1, userResourceRoleEntity2));

        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        Set<Long> expectedResult = Set.of(1L, 2L);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetCandidateRoles() throws Exception {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Role role1 = new Role();
        Role role2 = new Role();

        Mockito.when(roleRepository.findByRoleIdsAndEnabled(Mockito.anySet(), Mockito.eq(enabled)))
                .thenReturn(List.of(role1, role2));

        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertEquals(List.of(role1, role2), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsApprovableWithNullApprovalInstanceId() throws Exception {
        approvalPermissionService.isApprovable(null);
    }

    @Test
    public void testIsApprovable() throws Exception {
        Long approvalInstanceId = 1L;
        Set<Long> expectedIds = Set.of(1L, 2L);
        Set<Long> ids = Set.of(approvalInstanceId);

        Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.eq(FlowNodeStatus.EXECUTING)))
                .thenReturn(List.of(new UserTaskInstanceEntity()));
        Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.eq(FlowNodeStatus.EXECUTING)))
                .thenReturn(List.of(new UserTaskInstanceEntity()));
        Mockito.when(userTaskInstanceRepository.findById(approvalInstanceId))
                .thenReturn(Optional.of(new UserTaskInstanceEntity())).thenReturn(Optional.empty());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(Objects.requireNonNull(authenticationFacade.currentUserId())))
                .thenReturn(List.of(new UserTaskInstanceEntity()));
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Objects.requireNonNull(authenticationFacade.currentUserId()),
                Mockito.anySet()))
                .thenReturn(List.of(new UserTaskInstanceEntity()));

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testGetApprovableApprovalInstances() throws Exception {
        long userId = 1L;
        Set<Long> roleIds = Set.of(1L, 2L);
        Set<String> resourceRoleIdentifiers = Set.of("resourceRoleIdentifier1", "resourceRoleIdentifier2");
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity();

        Mockito.when(authenticationFacade.currentUserId()).thenReturn(userId);
        Mockito.when(userService.getCurrentUserRoleIds()).thenReturn(roleIds);
        Mockito.when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId)).thenReturn(resourceRoleIdentifiers);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(List.of(userTaskInstanceEntity1));
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
                .thenReturn(List.of(userTaskInstanceEntity2, userTaskInstanceEntity3));

        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);
        List<UserTaskInstanceEntity> expectedResult = List.of(userTaskInstanceEntity1, userTaskInstanceEntity2, userTaskInstanceEntity3);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSetCandidateResourceRole() throws Exception {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "resourceRoleIdentifier1";

        UserTaskInstanceCandidateEntity candidateEntity = Mockito.spy(new UserTaskInstanceCandidateEntity());
        candidateEntity.setApprovalInstanceId(approvalInstanceId);
        candidateEntity.setResourceRoleIdentifier(resourceRoleIdentifier);

        Mockito.when(userTaskCandidateRepository.save(candidateEntity))
                .thenReturn(candidateEntity);

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
        assertEquals(resourceRoleIdentifier, candidateEntity.getResourceRoleIdentifier());
    }

    @Test
    public void testListApprovableExternalInstances() throws Exception {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();

        Mockito.when(userTaskInstanceRepository.findByStatus(Mockito.eq(FlowNodeStatus.EXECUTING)))
                .thenReturn(List.of(userTaskInstanceEntity1, userTaskInstanceEntity2));

        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertEquals(List.of(userTaskInstanceEntity1, userTaskInstanceEntity2), result);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() throws Exception {
        Collection<Long> flowInstanceIds = Set.of(1L, 2L);
        UserEntity userEntity1 = new UserEntity();
        UserEntity userEntity2 = new UserEntity();

        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, Mockito.eq(FlowNodeStatus.EXECUTING)))
                .thenReturn(Map.of(1L, 1L, 2L, 2L));

        Map<Long, Set<UserEntity>> expectedResult = new HashMap<>();
        expectedResult.put(1L, Set.of(userEntity1));
        expectedResult.put(2L, Set.of(userEntity2));

        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertEquals(expectedResult, result);
    }
}