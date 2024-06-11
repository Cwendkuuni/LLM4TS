import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ApprovalPermissionService.class)
@ActiveProfiles("test")
public class ApprovalPermissionServiceTest {

    @MockBean
    private UserService userService;

    @MockBean
    private ResourceRoleService resourceRoleService;

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRoleRepository userRoleRepository;

    @MockBean
    private UserResourceRoleRepository userResourceRoleRepository;

    private ApprovalPermissionService approvalPermissionService;
    private ApprovalPermissionService spyApprovalPermissionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AutowedFieldUtils fields = new AutowedFieldUtils();
        approvalPermissionService = fields.getInjectedObject(ApprovalPermissionService.class);
        spyApprovalPermissionService = spy(approvalPermissionService);
    }

    @DisplayName("Test getCandidates() method with valid arguments")
    @ParameterizedTest(name = "{0} - getCandidates() with enabled={1}")
    @MethodSource("getCandidatesTestData")
    public void testGetCandidatesWithValidArguments(Long approvalInstanceId, boolean enabled) {
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singletonList(1L));
        when(userRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), enabled)).thenReturn(Arrays.asList(
                new User(1L, "user1", "user1@example.com", null, null, null)));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByRoleIdsAndEnabled(Collections.singletonList(1L), enabled);
    }

    @DisplayName("Test getCandidates() method with null enabled")
    @ParameterizedTest(name = "{0} - getCandidates() with enabled=null")
    @MethodSource("getCandidatesTestData")
    public void testGetCandidatesWithNullEnabled(Long approvalInstanceId) {
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptyList());
        when(roleRepository.findByRoleIds(Collections.emptyList())).thenReturn(Collections.emptyList());
        when(userRepository.findByUserIdsAndEnabled(Collections.emptyList(), null)).thenReturn(Arrays.asList(
                new User(1L, "user1", "user1@example.com", null, null, null)));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByUserIdsAndEnabled(Collections.emptyList(), null);
    }

    @DisplayName("Test getCandidates() method with empty approvalInstanceId")
    @Test
    public void testGetCandidatesWithEmptyApprovalInstanceId() {
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singletonList(1L));
        when(roleRepository.findByRoleIds(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(new Role(1L, "role1")));
        when(userRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true)).thenReturn(Arrays.asList(
                new User(1L, "user1", "user1@example.com", null, null, null)));
        when(userRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), false)).thenReturn(Collections.emptyList());

        Set<User> result = approvalPermissionService.getCandidates(null, true);
        assertEquals(1, result.size());
        verify(userRepository, times(2)).findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
        verifyNoInteractions(userRepository, roleRepository);
    }

    @DisplayName("Test isApprovable() method with valid approvalInstanceId")
    @Test
    public void testIsApprovableWithValidApprovalInstanceId() {
        long userId = 1L;
        Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
        Set<String> resourceRoleIdentifiers = new HashSet<>(Arrays.asList("role1", "role2"));
        UserTaskInstanceEntity approvalInstance1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity approvalInstance2 = new UserTaskInstanceEntity();
        approvalInstance1.setId(1L);
        approvalInstance1.setFlowInstanceId(1L);
        approvalInstance1.setStatus(FlowNodeStatus.EXECUTING);
        approvalInstance2.setId(2L);
        approvalInstance2.setFlowInstanceId(1L);
        approvalInstance2.setStatus(FlowNodeStatus.EXECUTING);
        approvalInstance2.setExternalApprovalId(1L);
        approvalInstance2.setExternalFlowInstanceId(2L);

        when(userService.getCurrentUserRoleIds()).thenReturn(roleIds);
        when(userService.getUserRoleIds(userId)).thenReturn(roleIds);
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId)).thenReturn(resourceRoleIdentifiers);

        List<UserTaskInstanceEntity> approvalInstances = Arrays.asList(approvalInstance1, approvalInstance2);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(approvalInstances);

        assertTrue(approvalPermissionService.isApprovable(1L));
        verify(userService, times(2)).getCurrentUserRoleIds();
        verify(resourceRoleService, times(1)).getResourceRoleIdentifiersByUserId(userId);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
    }

    @DisplayName("Test isApprovable() method with null approvalInstanceId")
    @Test
    public void testIsApprovableWithNullApprovalInstanceId() {
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptyList());
        when(userService.getUserRoleIds(1L)).thenReturn(Collections.emptyList());

        assertFalse(approvalPermissionService.isApprovable(null));
        verify(userService, times(2)).getUserRoleIds(1L);
        verifyNoInteractions(userService, userTaskInstanceRepository);
    }

    @DisplayName("Test getApprovableApprovalInstances() method")
    @Test
    public void testGetApprovableApprovalInstances() {
        long userId = 1L;
        Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
        Set<String> resourceRoleIdentifiers = new HashSet<>(Arrays.asList("role1", "role2"));
        UserTaskInstanceEntity approvalInstance1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity approvalInstance2 = new UserTaskInstanceEntity();
        approvalInstance1.setId(1L);
        approvalInstance1.setFlowInstanceId(1L);
        approvalInstance1.setStatus(FlowNodeStatus.EXECUTING);
        approvalInstance2.setId(2L);
        approvalInstance2.setFlowInstanceId(1L);
        approvalInstance2.setStatus(FlowNodeStatus.EXECUTING);
        approvalInstance2.setExternalApprovalId(1L);
        approvalInstance2.setExternalFlowInstanceId(2L);

        when(userService.getCurrentUserRoleIds()).thenReturn(roleIds);
        when(userService.getUserRoleIds(userId)).thenReturn(roleIds);
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId)).thenReturn(resourceRoleIdentifiers);

        List<UserTaskInstanceEntity> approvalInstances = Arrays.asList(approvalInstance1, approvalInstance2);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(approvalInstances);

        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertEquals(1, result.size());
        assertEquals(approvalInstance1.getId(), result.get(0).getId());
        verify(userService, times(2)).getCurrentUserRoleIds();
        verify(resourceRoleService, times(1)).getResourceRoleIdentifiersByUserId(userId);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);

        verify(spyApprovalPermissionService, times(1)).getApprovableApprovalInstances(userId);
    }

    @DisplayName("Test getApprovableApprovalInstances() method with userId")
    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
        Set<String> resourceRoleIdentifiers = new HashSet<>(Arrays.asList("role1", "role2"));
        UserTaskInstanceEntity approvalInstance1 = new UserTaskInstanceEntity();
        UserTaskInstanceEntity approvalInstance2 = new UserTaskInstanceEntity();
        approvalInstance1.setId(1L);
        approvalInstance1.setFlowInstanceId(1L);
        approvalInstance1.setStatus(FlowNodeStatus.EXECUTING);
        approvalInstance2.setId(2L);
        approvalInstance2.setFlowInstanceId(1L);
        approvalInstance2.setStatus(FlowNodeStatus.EXECUTING);
        approvalInstance2.setExternalApprovalId(1L);
        approvalInstance2.setExternalFlowInstanceId(2L);

        when(resourceRoleService.getResourceRoleIdentifiersByUserId(1L)).thenReturn(resourceRoleIdentifiers);

        List<UserTaskInstanceEntity> approvalInstances = Arrays.asList(approvalInstance1, approvalInstance2);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(approvalInstances);

        List<UserTaskInstanceEntity> result = spyApprovalPermissionService.getApprovableApprovalInstances(1L);
        assertEquals(1, result.size());
        assertEquals(approvalInstance1.getId(), result.get(0).getId());
        verify(resourceRoleService, times(1)).getResourceRoleIdentifiersByUserId(1L);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
    }

    @DisplayName("Test setCandidateResourceRole() method")
    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "role1";

        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setApprovalInstanceId(approvalInstanceId);
        candidateEntity.setResourceRoleIdentifier(resourceRoleIdentifier);

        doNothing().when(userTaskCandidateRepository).save(candidateEntity);

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        verify(userTaskCandidateRepository, times(1)).save(candidateEntity);
    }

    @DisplayName("Test listApprovableExternalInstances() method")
    @Test
    public void testListApprovableExternalInstances() {
        UserTaskInstanceEntity approvalInstance1 = new UserTaskInstanceEntity();
        approvalInstance1.setId(1L);
        approvalInstance1.setFlowInstanceId(1L);
        approvalInstance1.setStatus(FlowNodeStatus.EXECUTING);
        approvalInstance1.setExternalApprovalId(1L);
        approvalInstance1.setExternalFlowInstanceId(2L);

        UserTaskInstanceEntity approvalInstance2 = new UserTaskInstanceEntity();
        approvalInstance2.setId(2L);
        approvalInstance2.setFlowInstanceId(1L);
        approvalInstance2.setStatus(FlowNodeStatus.EXECUTING);
        approvalInstance2.setExternalApprovalId(null);
        approvalInstance2.setExternalFlowInstanceId(null);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(Arrays.asList(approvalInstance1,
                approvalInstance2));

        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertEquals(1, result.size());
        assertEquals(approvalInstance1.getId(), result.get(0).getId());
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
    }

    @DisplayName("Test getCandidatesByFlowInstanceIds() method")
    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        long flowInstanceId = 1L;
        Map<Long, Set<UserEntity>> result = Map.of(flowInstanceId,
                Set.of(new UserEntity(1L, "user1", "user1@example.com", null, null, null)));

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(flowInstanceId),
                FlowNodeStatus.EXECUTING)).thenReturn(Map.of(1L, 1L));
        when(userTaskCandidateRepository.findByApprovalInstanceIdInAndCandidateUserIdIn(List.of(1L),
                Arrays.asList(1L))).thenReturn(Collections.emptyList());

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(
                Collections.singletonList(1L), Arrays.asList("role1")))
                .thenReturn(Collections.singletonList(new UserResourceRoleEntity(1L, 1L, "role1")));

        Map<Long, Set<UserEntity>> candidates = approvalPermissionService.getCandidatesByFlowInstanceIds(
                Collections.singletonList(flowInstanceId));
        assertEquals(result, candidates);
        verify(userTaskInstanceRepository, times(1))
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(flowInstanceId),
                        FlowNodeStatus.EXECUTING);
        verify(userTaskCandidateRepository, times(1))
                .findByApprovalInstanceIdInAndCandidateUserIdIn(List.of(1L), Arrays.asList(1L));
        verify(userResourceRoleRepository, times(1))
                .findByResourceIdsAndResourceRoleIdentifiersIn(Collections.singletonList(1L), Arrays.asList("role1"));
    }

    private static Stream<Arguments> getCandidatesTestData() {
        return Stream.of(Arguments.arguments(1L, true), Arguments.arguments(1L, false),
                Arguments.arguments(null, true));
    }

}