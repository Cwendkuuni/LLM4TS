import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates_EnabledTrue_ShouldReturnUserEntities() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, "USER", null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, "ROLE", 2L));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        Set<UserEntity> expectedUsers = new HashSet<>();
        UserEntity user1 = new UserEntity(1L, "User1");
        UserEntity user2 = new UserEntity(2L, "User2");
        expectedUsers.add(user1);
        expectedUsers.add(user2);

        Set<UserEntity> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void testGetCandidates_EnabledFalse_ShouldReturnRoleEntities() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, "ROLE", 2L),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, "USER", null));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        Set<Role> expectedRoles = new HashSet<>();
        Role role1 = new Role(2L, "Role1");
        expectedRoles.add(role1);

        Set<Role> actualRoles = approvalPermissionService.getCandidates(approvalInstanceId, false).stream()
                .map(ApprovalPermissionService::new).collect(Collectors.toSet());

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testGetCandidates_EnabledNull_ShouldReturnEnabledUsersAndRoles() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L, "USER", null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, "ROLE", 2L),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 3L, "USER", null),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, "ROLE", 4L));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        Set<UserEntity> users = new HashSet<>();
        UserEntity user1 = new UserEntity(1L, "User1");
        UserEntity user3 = new UserEntity(3L, "User3");
        users.add(user1);
        users.add(user3);

        Set<Role> roles = new HashSet<>();
        Role role2 = new Role(2L, "Role2");
        Role role4 = new Role(4L, "Role4");
        roles.add(role2);
        roles.add(role4);

        Set<UserEntity> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Set<Role> actualRoles = approvalPermissionService.getCandidates(approvalInstanceId, null).stream()
                .map(ApprovalPermissionService::new).filter(approvalPermissionService -> approvalPermissionService
                        .isRole())
                .map(ApprovalPermissionService::getRole).collect(Collectors.toSet());

        assertEquals(users, actualUsers);
        assertEquals(roles, actualRoles);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds_ShouldReturnMapOfApprovalInstanceIdsToRoleIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, null, "ROLE", 2L),
                new UserTaskInstanceCandidateEntity(2L, 3L, "USER", null),
                new UserTaskInstanceCandidateEntity(3L, null, "ROLE", 4L),
                new UserTaskInstanceCandidateEntity(1L, 5L, "ROLE", 6L),
                new UserTaskInstanceCandidateEntity(2L, null, "ROLE", 7L));
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        Map<Long, Set<Long>> expectedMap = new HashMap<>();
        expectedMap.put(1L, new HashSet<>(Arrays.asList(2L, 6L)));
        expectedMap.put(2L, new HashSet<>(Arrays.asList(3L)));
        expectedMap.put(3L, new HashSet<>(Arrays.asList(4L)));

        Map<Long, Set<Long>> actualMap = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds_ShouldReturnMapOfApprovalInstanceIdsToResourceRoleIdentifierIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, null, "ROLE", 2L, "resourceRoleIdentifier1"),
                new UserTaskInstanceCandidateEntity(2L, 3L, "USER", null, "resourceRoleIdentifier2"),
                new UserTaskInstanceCandidateEntity(3L, null, "ROLE", 4L, "resourceRoleIdentifier3"),
                new UserTaskInstanceCandidateEntity(1L, 5L, "ROLE", 6L, "resourceRoleIdentifier4"),
                new UserTaskInstanceCandidateEntity(2L, null, "ROLE", 7L, "resourceRoleIdentifier5"));
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        Map<Long, Set<String>> expectedMap = new HashMap<>();
        expectedMap.put(1L, new HashSet<>(Arrays.asList("resourceRoleIdentifier1", "resourceRoleIdentifier4")));
        expectedMap.put(2L, new HashSet<>(Arrays.asList("resourceRoleIdentifier2")));
        expectedMap.put(3L, new HashSet<>(Arrays.asList("resourceRoleIdentifier3")));

        Map<Long, Set<String>> actualMap = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetCandidateUserIds_ShouldReturnSetOfUserIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L, 3L);
        Set<Long> expectedUserIds = new HashSet<>(Arrays.asList(1L, 3L, 5L));

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenCallRealMethod();
        Set<Long> actualUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        assertEquals(expectedUserIds, actualUserIds);
    }

    @Test
    public void testGetCandidateRoles_ShouldReturnListOfRoles() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, "ROLE1", 2L),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, "ROLE2", 3L),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, "ROLE3", 4L));
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        List<Role> expectedRoles = Arrays.asList(
                new Role(2L, "ROLE1"),
                new Role(3L, "ROLE2"),
                new Role(4L, "ROLE3"));

        List<Role> actualRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, true).stream()
                .map(ApprovalPermissionService::new).map(ApprovalPermissionService::getRole).collect(Collectors.toList());

        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testIsApprovable_ShouldReturnTrueIfApprovalInstanceIdIsInApprovedApprovalInstances() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceEntity> approvedApprovalInstances = Arrays.asList(
                new UserTaskInstanceEntity(approvalInstanceId, "ApprovalInstance1", FlowNodeStatus.EXECUTED));
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTED)).thenReturn(approvedApprovalInstances);

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testIsApprovable_ShouldReturnFalseIfApprovalInstanceIdIsNotInApprovedApprovalInstances() {
        Long approvalInstanceId = 2L;
        List<UserTaskInstanceEntity> approvedApprovalInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, "ApprovalInstance1", FlowNodeStatus.EXECUTED));
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTED)).thenReturn(approvedApprovalInstances);

        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }
}