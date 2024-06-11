import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidatesEmptyApprovalInstanceId() {
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
                .thenReturn(Collections.emptyList());
        Set<User> result = approvalPermissionService.getCandidates(null, null);
        Assertions.assertEquals(Collections.emptySet(), result);
    }

    @Test
    public void testGetCandidatesNullEnabled() {
        Long approvalInstanceId = 1L;
        Set<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity()).stream().peek(entity -> entity.setApprovalInstanceId(approvalInstanceId))
                .collect(Collectors.toList());
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Set<User> expected = new HashSet<>(Arrays.asList(new User(), new User()));
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testGetCandidatesEnabledTrue() {
        Long approvalInstanceId = 1L;
        Set<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity()).stream().peek(entity -> {
                    entity.setApprovalInstanceId(approvalInstanceId);
                    entity.setEnabled(true);
                }).collect(Collectors.toList());
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);
        Set<User> expected = new HashSet<>(Arrays.asList(new User(), new User()));
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testGetCandidatesEnabledFalse() {
        Long approvalInstanceId = 1L;
        Set<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity()).stream().peek(entity -> {
                    entity.setApprovalInstanceId(approvalInstanceId);
                    entity.setEnabled(false);
                }).collect(Collectors.toList());
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, false);
        Set<User> expected = Collections.emptySet();
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testGetCandidatesEnabledNull() {
        Long approvalInstanceId = 1L;
        Set<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(),
                new UserTaskInstanceCandidateEntity()).stream().peek(entity -> {
                    entity.setApprovalInstanceId(approvalInstanceId);
                }).collect(Collectors.toList());
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Set<User> expected = new HashSet<>(Arrays.asList(new User(), new User()));
        Assertions.assertEquals(expected, result);
    }

    // Add more test cases for other methods in the class such as getInstanceId2CandidateRoleIds, getInstanceId2CandidateResourceRoleIdentifierIds,
    // getCandidateUserIds, getCandidateRoles, isApprovable, getApprovableApprovalInstances, setCandidateResourceRole,
    // listApprovableExternalInstances, and getCandidatesByFlowInstanceIds.

    @Test
    public void testIsApprovableEmptyApprovalInstanceIds() {
        Set<Long> approvalInstanceIds = Collections.emptySet();
        List<Long> approvableApprovalInstances = approvalPermissionService.getApprovableApprovalInstances();
        Assertions.assertEquals(Collections.emptyList(), approvableApprovalInstances);
    }

    // Add more test cases for all other methods in the class

    @Test
    public void testGetApprovableApprovalInstances() {
        long userId = 1L;
        Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
        Set<String> resourceRoleIdentifiers = new HashSet<>(Arrays.asList("role1", "role2"));
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity()).stream().peek(entity -> {
                    entity.setCandidateUserId(userId);
                    entity.setRoleIds(roleIds);
                    entity.setResourceRoleIdentifier(resourceRoleIdentifiers.get(0));
                    entity.setStatus(FlowNodeStatus.EXECUTING);
                }).collect(Collectors.toList());

        when(userService.getCurrentUserRoleIds()).thenReturn(roleIds);
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(resourceRoleIdentifiers);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);
        List<UserTaskInstanceEntity> expected = userTaskInstanceEntities;
        Assertions.assertEquals(expected, result);
    }

    // Add more test cases for all other methods in the class

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "resourceRoleIdentifier";
        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setApprovalInstanceId(approvalInstanceId);
        candidateEntity.setResourceRoleIdentifier(resourceRoleIdentifier);

        when(userTaskCandidateRepository.save(candidateEntity)).thenReturn(candidateEntity);

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        // Verify that the save method was called with the correct argument
        Mockito.verify(userTaskCandidateRepository).save(candidateEntity);
    }

    // Add more test cases for all other methods in the class

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity()).stream().peek(entity -> {
                    entity.setStatus(FlowNodeStatus.EXECUTING);
                    entity.setExternalApprovalId(1L);
                    entity.setExternalFlowInstanceId(1L);
                }).collect(Collectors.toList());

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        List<UserTaskInstanceEntity> expected = userTaskInstanceEntities;
        Assertions.assertEquals(expected, result);
    }

    // Add more test cases for all other methods in the class

    @Test
    public void testGetCandidatesByFlowInstanceIdsEmptyFlowInstanceIds() {
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.emptySet());
        Assertions.assertEquals(Collections.emptyMap(), result);
    }

    // Add more test cases for all other methods in the class
}