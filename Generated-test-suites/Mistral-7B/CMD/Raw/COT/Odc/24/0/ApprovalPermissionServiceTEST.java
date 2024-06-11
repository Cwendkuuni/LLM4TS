package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * ApprovalPermissionService tests
 */
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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates_Enabled() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> candidateEntities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, "role1"),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 2L)
        );
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(candidateEntities);

        Set<User> expectedUsers = Set.of(
                new User(1L, "user1", null, null, null, null),
                new User(2L, "user2", null, null, null, null)
        );
        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertEquals(expectedUsers, actualUsers);
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
    }

    @Test
    public void testGetCandidates_Disabled() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> candidateEntities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 1L),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, null, "role1"),
                new UserTaskInstanceCandidateEntity(approvalInstanceId, 2L)
        );
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(candidateEntities);

        Set<User> expectedUsers = Set.of(
                new User(1L, "user1", null, null, null, null)
        ); // only enabled users are returned when disabled is false
        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, false);

        assertEquals(expectedUsers, actualUsers);
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
    }

    @Test
    public void testGetCandidates_NoUsersOrRoles() {
        Long approvalInstanceId = 1L;
        List<UserTaskInstanceCandidateEntity> candidateEntities = Collections.emptyList();
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(candidateEntities);

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);

        assertTrue(actualUsers.isEmpty());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
    }

    @Test
    public void testGetCandidates_EmptyApprovalInstanceIds() {
        Set<Long> approvalInstanceIds = Collections.emptySet();
        Map<Long, Set<Long>> actualUsersByIdInstance = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertTrue(actualUsersByIdInstance.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds_MultipleInstances() {
        Set<Long> approvalInstanceIds = Set.of(1L, 2L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(
                new UserTaskInstanceCandidateEntity(1L, 1L, "role1"),
                new UserTaskInstanceCandidateEntity(1L, null, "role2"),
                new UserTaskInstanceCandidateEntity(2L, 3L),
                new UserTaskInstanceCandidateEntity(2L, null, "role3")
        );
        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(entities);

        Map<Long, Set<Long>> expectedUsersByIdInstance = Map.of(
                1L, Set.of(1L, 2L),
                2L, Set.of(3L)
        );
        Map<Long, Set<Long>> actualUsersByIdInstance = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertEquals(expectedUsersByIdInstance, actualUsersByIdInstance);
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
    }

    // Add remaining test methods for getInstanceId2CandidateResourceRoleIdentifierIds, getCandidateUserIds, getCandidateRoles,
    // isApprovable, getApprovableApprovalInstances, getApprovableApprovalInstances(userId), setCandidateResourceRole,
    // listApprovableExternalInstances, and getCandidatesByFlowInstanceIds.

}