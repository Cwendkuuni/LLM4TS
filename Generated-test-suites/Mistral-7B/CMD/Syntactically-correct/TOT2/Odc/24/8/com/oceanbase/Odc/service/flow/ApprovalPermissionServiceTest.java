import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Test
    void testGetCandidates_EmptyApprovalInstanceId_ReturnsEmptySet() {
        // Given
        Long approvalInstanceId = null;
        Boolean enabled = null;
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.emptyList());

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        Assertions.assertEquals(Collections.emptySet(), candidates);
    }

    @Test
    void testGetCandidates_EnabledIsFalse_ReturnsUsersWithDisabledRoles() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = false;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, approvalInstanceId, 1L, null),
                new UserTaskInstanceCandidateEntity(1L, approvalInstanceId, null, 2L),
                new UserTaskInstanceCandidateEntity(1L, approvalInstanceId, null, 3L),
                new UserTaskInstanceCandidateEntity(1L, approvalInstanceId, 4L, null));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        Set<Long> roleIds = new HashSet<>(Arrays.asList(2L, 3L));
        Set<UserEntity> usersWithDisabledRoles = Arrays.asList(
                new UserEntity(1L, "User1", "user1@example.com", null),
                new UserEntity(2L, "User2", "user2@example.com", null),
                new UserEntity(3L, "User3", "user3@example.com", null),
                new UserEntity(4L, "User4", "user4@example.com", null)).stream()
                .filter(user -> CollectionUtils.isNotEmpty(user.getRoleIds()) && CollectionUtils.disjoint(user.getRoleIds(), roleIds))
                .collect(Collectors.toSet());

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        when(userRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(Arrays.asList(new UserEntity(5L, "User5", "user5@example.com", null)));
        Set<User> expectedCandidates = new HashSet<>(Arrays.asList(new UserEntity(5L, "User5", "user5@example.com", null), usersWithDisabledRoles.toArray(new UserEntity[0])));
        Assertions.assertEquals(expectedCandidates, candidates);
    }

    @Test
    void testGetCandidates_EnabledIsNull_ReturnsUsersWithEnabledRoles() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = null;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, approvalInstanceId, 1L, null),
                new UserTaskInstanceCandidateEntity(1L, approvalInstanceId, null, 2L),
                new UserTaskInstanceCandidateEntity(1L, approvalInstanceId, null, 3L),
                new UserTaskInstanceCandidateEntity(1L, approvalInstanceId, 4L, null));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        Set<UserEntity> usersWithEnabledRoles = Arrays.asList(
                new UserEntity(1L, "User1", "user1@example.com", null),
                new UserEntity(2L, "User2", "user2@example.com", null),
                new UserEntity(3L, "User3", "user3@example.com", null),
                new UserEntity(4L, "User4", "user4@example.com", null)).stream()
                .filter(user -> CollectionUtils.isNotEmpty(user.getRoleIds()) && CollectionUtils.isNotEmpty(user.getRoleIds()))
                .collect(Collectors.toSet());

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        Set<User> expectedCandidates = usersWithEnabledRoles;
        Assertions.assertEquals(expectedCandidates, candidates);
    }

    // Test other methods such as getInstanceId2CandidateRoleIds, getInstanceId2CandidateResourceRoleIdentifierIds,
    // getCandidateUserIds, getCandidateRoles, isApprovable, setCandidateResourceRole, listApprovableExternalInstances and
    // getCandidatesByFlowInstanceIds here.

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}