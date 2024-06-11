import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class ApprovalPermissionServiceTest {

    @Mock
    private UserService userServiceMock;
    @Mock
    private ResourceRoleService resourceRoleServiceMock;
    @Mock
    private AuthenticationFacade authenticationFacadeMock;
    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepositoryMock;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private RoleRepository roleRepositoryMock;
    @Mock
    private UserRoleRepository userRoleRepositoryMock;
    @Mock
    private UserResourceRoleRepository userResourceRoleRepositoryMock;

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        entities.add(entity1);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);
        entities.add(entity2);

        when(userServiceMock.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
        when(userServiceMock.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());
        when(userTaskCandidateRepositoryMock.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        Set<User> expectedCandidates = new HashSet<>();
        User user1 = new User(1L, "Test User 1");
        expectedCandidates.add(user1);
        User user2 = new User(2L, "Test User 2");
        expectedCandidates.add(user2);
        Assert.assertEquals(expectedCandidates, candidates);
    }

    @Test
    public void testGetCandidatesWithEmptyEntitiesList() {
        // Given
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        when(userServiceMock.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
        when(userServiceMock.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());
        when(userTaskCandidateRepositoryMock.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.emptyList());

        // When
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        // Then
        Assert.assertEquals(Collections.emptySet(), candidates);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Given
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);

        List<UserTaskInstanceCandidateEntity> entities1 = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity11 = new UserTaskInstanceCandidateEntity();
        entity11.setApprovalInstanceId(1L);
        entity11.setRoleId(1L);
        entities1.add(entity11);

        List<UserTaskInstanceCandidateEntity> entities2 = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity21 = new UserTaskInstanceCandidateEntity();
        entity21.setApprovalInstanceId(1L);
        entity21.setRoleId(2L);
        entities2.add(entity21);

        List<UserTaskInstanceCandidateEntity> entities3 = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity31 = new UserTaskInstanceCandidateEntity();
        entity31.setApprovalInstanceId(2L);
        entity31.setRoleId(1L);
        entities3.add(entity31);

        when(userTaskCandidateRepositoryMock.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(
                List.of(entities1, entities2, entities3));

        // When
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        // Then
        Map<Long, Set<Long>> expectedResult = new HashMap<>();
        expectedResult.put(1L, new HashSet<>(List.of(1L, 2L)));
        expectedResult.put(2L, new HashSet<>(List.of(1L)));
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testGetCandidateUserIds() {
        // Given
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);
        approvalInstanceIds.add(2L);

        // When
        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testIsApprovable() {
        // Given
        Long approvalInstanceId = 1L;
        Set<Long> ids = new HashSet<>();
        ids.add(approvalInstanceId);

        when(authenticationFacadeMock.currentUserId()).thenReturn(1L);
        when(userServiceMock.getUserRoleIds(1L)).thenReturn(Collections.emptySet());
        when(userServiceMock.getUserResourceRoleIdentifiers(1L)).thenReturn(Collections.emptySet());
        when(userTaskInstanceRepositoryMock.findByCandidateUserId(1L)).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(1L, ids)).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Collections.emptyList())).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Collections.singletonList("test"))).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Collections.singletonList("test2"))).thenReturn(new ArrayList<>());

        // When
        boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void testIsApprovableWithApprovalInstanceIdInApprovableInstances() {
        // Given
        Long approvalInstanceId = 1L;
        Set<Long> ids = new HashSet<>();
        ids.add(approvalInstanceId);

        when(authenticationFacadeMock.currentUserId()).thenReturn(1L);
        when(userServiceMock.getUserRoleIds(1L)).thenReturn(Collections.emptySet());
        when(userServiceMock.getUserResourceRoleIdentifiers(1L)).thenReturn(Collections.emptySet());
        when(userTaskInstanceRepositoryMock.findByCandidateUserId(1L)).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrRoleIds(1L, ids)).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Collections.singletonList("test"))).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepositoryMock.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Collections.singletonList("test2"))).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepositoryMock.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(List.of(new UserTaskInstanceEntity()));

        // When
        boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

        // Then
        Assert.assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void testSetCandidateResourceRoleWithNullApprovalInstanceId() {
        // Given
        String resourceRoleIdentifier = "test";

        // When
        approvalPermissionService.setCandidateResourceRole(null, resourceRoleIdentifier);

        // Then
        // NullPointerException is expected to be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCandidateResourceRoleWithNullResourceRoleIdentifier() {
        // Given
        Long approvalInstanceId = 1L;

        // When
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, null);

        // Then
        // IllegalArgumentException is expected to be thrown
    }
}