package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepositoryImpl;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.RoleRepositoryImpl;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserEntityImpl;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRepositoryImpl;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntityImpl;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepositoryImpl;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntityImpl;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepositoryImpl;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.ApprovalPermissionServiceImpl;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

@SuppressWarnings("unchecked")
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
    @InjectMocks
    private ApprovalPermissionServiceImpl approvalPermissionServiceImpl;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>(Arrays.asList(1L, 2L, 3L)));
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(new HashSet<>(Arrays.asList("r1", "r2")));
        when(userRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(Arrays.asList(new UserEntityImpl(1L, "u1", new HashSet<>(Arrays.asList(1L, 2L, 3L)), true),
                        new UserEntityImpl(4L, "u4", new HashSet<>(Collections.emptySet()), true)));
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(Arrays.asList(new UserEntityImpl(5L, "u5", new HashSet<>(Collections.singletonList(1L)), true),
                        new UserEntityImpl(6L, "u6", new HashSet<>(Collections.singletonList(2L)), true)));
        when(userRepository.findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean()))
                .thenReturn(Arrays.asList(new UserEntityImpl(5L, "u5", new HashSet<>(Arrays.asList(1L, 2L, 3L)), true),
                        new UserEntityImpl(6L, "u6", new HashSet<>(Arrays.asList(1L, 2L, 3L)), true),
                        new UserEntityImpl(7L, "u7", new HashSet<>(Collections.singletonList(3L)), true)));

        assertEquals(new HashSet<>(Arrays.asList(new UserImpl(1L, "u1"), new UserImpl(4L, "u4"), new UserImpl(5L, "u5"),
                new UserImpl(6L, "u6"), new UserImpl(7L, "u7"))),
                approvalPermissionService.getCandidates(1L, null));
        assertEquals(new HashSet<>(Arrays.asList(new UserImpl(4L, "u4"), new UserImpl(5L, "u5"), new UserImpl(6L, "u6"))),
                approvalPermissionService.getCandidates(1L, false));
        assertEquals(new HashSet<>(Arrays.asList(new UserImpl(5L, "u5"), new UserImpl(6L, "u6"), new UserImpl(7L, "u7"))),
                approvalPermissionService.getCandidates(1L, true));

        verify(userRepository, times(3)).findByUserIdsOrRoleIds(anyLong(), anyCollection(), anyBoolean());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Long approvalInstanceId1 = 1L;
        Long approvalInstanceId2 = 2L;
        when(userTaskCandidateRepository.findByApprovalInstanceIds(Arrays.asList(approvalInstanceId1, approvalInstanceId2)))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity(approvalInstanceId1, 1L),
                        new UserTaskInstanceCandidateEntity(approvalInstanceId1, 2L),
                        new UserTaskInstanceCandidateEntity(approvalInstanceId1, 3L),
                        new UserTaskInstanceCandidateEntity(approvalInstanceId2, 2L),
                        new UserTaskInstanceCandidateEntity(approvalInstanceId2, 3L)));

        Map<Long, Set<Long>> expectedResult = new HashMap<>();
        expectedResult.put(approvalInstanceId1, new HashSet<>(Arrays.asList(1L, 2L, 3L)));
        expectedResult.put(approvalInstanceId2, new HashSet<>(Arrays.asList(2L, 3L)));

        assertEquals(expectedResult, approvalPermissionServiceImpl.getInstanceId2CandidateRoleIds(
                Arrays.asList(approvalInstanceId1, approvalInstanceId2)));

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(Arrays.asList(approvalInstanceId1,
                approvalInstanceId2));
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Long approvalInstanceId1 = 1L;
        Long approvalInstanceId2 = 2L;
        when(userTaskCandidateRepository.findByApprovalInstanceIds(Arrays.asList(approvalInstanceId1, approvalInstanceId2)))
                .thenReturn(Arrays.asList(new UserTaskInstanceCandidateEntity(approvalInstanceId1, "r1"),
                        new UserTaskInstanceCandidateEntity(approvalInstanceId1, "r2"),
                        new UserTaskInstanceCandidateEntity(approvalInstanceId1, "r3"),
                        new UserTaskInstanceCandidateEntity(approvalInstanceId2, "r2"),
                        new UserTaskInstanceCandidateEntity(approvalInstanceId2, "r3")));

        Map<Long, Set<String>> expectedResult = new HashMap<>();
        expectedResult.put(approvalInstanceId1, new HashSet<>(Arrays.asList("r1", "r2", "r3")));
        expectedResult.put(approvalInstanceId2, new HashSet<>(Arrays.asList("r2", "r3")));

        assertEquals(expectedResult, approvalPermissionServiceImpl.getInstanceId2CandidateResourceRoleIdentifierIds(
                Arrays.asList(approvalInstanceId1, approvalInstanceId2)));

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(Arrays.asList(approvalInstanceId1,
                approvalInstanceId2));
    }

    @Test
    public void testGetCandidateUserIds() {
        Long approvalInstanceId = 1L;

        when(userRoleRepository.findByRoleIdIn(anyLong(), anyCollection())).thenReturn(Arrays.asList(
                new UserRoleEntityImpl(1L, 1L, new HashSet<>(Arrays.asList(1L, 2L, 3L))),
                new UserRoleEntityImpl(2L, 2L, new HashSet<>(Collections.singletonList(4L)))));

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anyCollection(), anyCollection()))
                .thenReturn(Arrays.asList(new UserResourceRoleEntityImpl(3L, 1L, 5L, new HashSet<>(Collections.singletonList(5L))),
                        new UserResourceRoleEntityImpl(4L, 2L, 6L, new HashSet<>(Collections.singletonList(6L)))));

        Set<User> expectedResult = Arrays.asList(new UserImpl(5L, "u5"), new UserImpl(6L, "u6"))
                .stream().map(user -> new User(user.getId(), user.getName(), Collections.emptySet(), user.isEnabled()))
                .collect(Collectors.toSet());

        assertEquals(expectedResult, approvalPermissionServiceImpl.getCandidateUserIds(Collections.singletonList(approvalInstanceId)));

        verify(userRoleRepository, times(1)).findByRoleIdIn(anyLong(), anyCollection());
        verify(userResourceRoleRepository, times(1)).findByResourceIdsAndResourceRoleIdsIn(anyCollection(), anyCollection());
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        when(roleRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(Arrays.asList(
                new RoleImpl(1L, "r1"), new RoleImpl(2L, "r2"), new RoleImpl(3L, "r3")));

        Set<User> users = Arrays.asList(new UserImpl(1L, "u1"), new UserImpl(2L, "u2"), new UserImpl(3L, "u3"))
                .stream().map(user -> new User(user.getId(), user.getName(), Collections.emptySet(), user.isEnabled()))
                .collect(Collectors.toSet());

        List<Role> expectedResult = Arrays.asList(new RoleImpl(1L, "r1"), new RoleImpl(2L, "r2"));

        assertEquals(expectedResult, approvalPermissionServiceImpl.getCandidateRoles(approvalInstanceId, enabled));

        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(anyLong(), anyBoolean());
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        Set<Long> approvableApprovalInstances = new HashSet<>(Arrays.asList(1L, 2L, 3L));

        when(userTaskInstanceRepository.findByStatus(any(FlowNodeStatus.class)))
                .thenReturn(Arrays.asList(new UserTaskInstanceEntityImpl(1L, approvalInstanceId, FlowNodeStatus.EXECUTING),
                        new UserTaskInstanceEntityImpl(2L, approvalInstanceId + 1, FlowNodeStatus.EXECUTED),
                        new UserTaskInstanceEntityImpl(3L, approvalInstanceId + 2, FlowNodeStatus.EXECUTING)));

        when(authenticationFacade.currentUserId()).thenReturn(approvalInstanceId);

        assertTrue(approvalPermissionServiceImpl.isApprovable(approvalInstanceId));

        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
        verify(authenticationFacade, times(1)).currentUserId();
    }

    @Test
    public void testIsApprovableWithUserId() {
        Long userId = 1L;
        Set<Long> approvableApprovalInstances = new HashSet<>(Arrays.asList(1L, 2L, 3L));

        when(userService.getUserRoleIds(userId)).thenReturn(Arrays.asList(1L, 2L, 3L));
        when(userRepository.findByUserIds(Arrays.asList(userId)))
                .thenReturn(Arrays.asList(new UserEntityImpl(userId, "user1", Collections.emptySet(), true)));

        when(userRoleRepository.findByRoleIdIn(anyLong(), anyCollection())).thenReturn(Arrays.asList(
                new UserRoleEntityImpl(1L, 1L, new HashSet<>(Arrays.asList(1L, 2L, 3L))),
                new UserRoleEntityImpl(2L, 2L, new HashSet<>(Collections.singletonList(4L)))));

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, Arrays.asList(1L, 2L, 3L)))
                .thenReturn(Arrays.asList(new UserTaskInstanceEntityImpl(1L, 1L, FlowNodeStatus.EXECUTING),
                        new UserTaskInstanceEntityImpl(2L, 2L, FlowNodeStatus.EXECUTED),
                        new UserTaskInstanceEntityImpl(3L, 3L, FlowNodeStatus.EXECUTING)));

        assertTrue(approvalPermissionServiceImpl.isApprovable(userId));

        verify(userRoleRepository, times(1)).findByRoleIdIn(anyLong(), anyCollection());
        verify(userRepository, times(1)).findByUserIds(Arrays.asList(userId));
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(userId, Arrays.asList(1L, 2L, 3L));
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "r1";

        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setApprovalInstanceId(approvalInstanceId);
        candidateEntity.setResourceRoleIdentifier(resourceRoleIdentifier);

        doNothing().when(userTaskCandidateRepository).save(candidateEntity);

        approvalPermissionServiceImpl.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        verify(userTaskCandidateRepository, times(1)).save(candidateEntity);
    }
}