package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.AuthenticationFacade;
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
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

@SuppressWarnings("unchecked")
public class ApprovalPermissionServiceTest {

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
    @Mock
    private ResourceRoleService resourceRoleService;
    @Mock
    private UserService userService;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    private Long approvalInstanceId = 1L;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        reset(authenticationFacade, userTaskCandidateRepository, userTaskInstanceRepository, userRepository,
                roleRepository, userRoleRepository, userResourceRoleRepository, resourceRoleService, userService);
    }

    @Test
    public void testGetCandidates() {
        // Given
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, 2L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

        // Then
        Set<User> expectedCandidates = new HashSet<>();
        User user1 = new User(1L, "user1");
        User user2 = new User(2L, "user2");
        expectedCandidates.add(user1);
        expectedCandidates.add(user2);
        assertEquals(expectedCandidates, candidates);
        verify(userRepository).findByRoleIdsAndEnabled(Collections.singletonList(2L), true);
    }

    @Test
    public void testGetCandidates_nullEnabled() {
        // Given
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, 2L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);

        // Then
        Set<User> expectedCandidates = new HashSet<>();
        User user1 = new User(1L, "user1");
        User user2 = new User(2L, "user2");
        expectedCandidates.add(user1);
        expectedCandidates.add(user2);
        assertEquals(expectedCandidates, candidates);
        verify(userRepository, never()).findByRoleIdsAndEnabled(anyList(), anyBoolean());
    }

    @Test
    public void testGetCandidates_emptyEntities() {
        // Given
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Collections.emptyList());

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

        // Then
        assertEquals(Collections.emptySet(), candidates);
    }

    @Test
    public void testGetCandidates_noUserOrRole() {
        // Given
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(new UserTaskInstanceCandidateEntity(approvalInstanceId, null, null));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

        // Then
        assertEquals(Collections.emptySet(), candidates);
    }

    @Test
    public void testGetCandidates_emptyRoleIds() {
        // Given
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, 2L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);

        // Then
        Set<User> expectedCandidates = new HashSet<>();
        User user1 = new User(1L, "user1");
        expectedCandidates.add(user1);
        assertEquals(expectedCandidates, candidates);
        verify(userRepository, never()).findByRoleIdsAndEnabled(anyList(), anyBoolean());
    }

    @Test
    public void testGetCandidates_emptyUserIds() {
        // Given
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, 2L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

        // Then
        Set<User> expectedCandidates = new HashSet<>();
        User user2 = new User(2L, "user2");
        expectedCandidates.add(user2);
        assertEquals(expectedCandidates, candidates);
        verify(userRepository, times(1)).findByRoleIdsAndEnabled(Collections.singletonList(2L), true);
    }

    @Test
    public void testGetCandidates_mixedUserAndRole() {
        // Given
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, 2L),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, 3L));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);

        // Then
        Set<User> expectedCandidates = new HashSet<>();
        User user1 = new User(1L, "user1");
        User user2 = new User(2L, "user2");
        User user3 = new User(3L, "user3");
        expectedCandidates.add(user1);
        expectedCandidates.add(user2);
        expectedCandidates.add(user3);
        assertEquals(expectedCandidates, candidates);
        verify(userRepository, times(1)).findByRoleIdsAndEnabled(Collections.singletonList(3L), true);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Given
        Set<Long> approvalInstanceIds = new HashSet<>(Arrays.asList(1L, 2L));
        List<UserTaskInstanceCandidateEntity> entities1 = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, 1L, null),
                new UserTaskInstanceCandidateEntity(1L, null, 3L),
                new UserTaskInstanceCandidateEntity(1L, null, 4L));
        List<UserTaskInstanceCandidateEntity> entities2 = Arrays.asList(
                new UserTaskInstanceCandidateEntity(2L, 2L, null),
                new UserTaskInstanceCandidateEntity(2L, null, 5L),
                new UserTaskInstanceCandidateEntity(2L, null, 6L));

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(Stream.of(entities1, entities2).flatMap(Collection::stream).collect(Collectors.toList()));

        // When
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        // Then
        Map<Long, Set<Long>> expectedResult = new HashMap<>();
        expectedResult.put(1L, new HashSet<>(Arrays.asList(3L, 4L)));
        expectedResult.put(2L, new HashSet<>(Arrays.asList(5L, 6L)));
        assertEquals(expectedResult, result);
    }

    // Add more test methods for getInstanceId2CandidateResourceRoleIdentifierIds(),
    // getCandidateUserIds(), getCandidateRoles(), isApprovable(),
    // getApprovableApprovalInstances(), getApprovableApprovalInstances(userId),
    // setCandidateResourceRole(), and listApprovableExternalInstances()
}