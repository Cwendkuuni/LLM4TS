
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.common.lang.Pair;

import java.util.*;

import static org.mockito.Mockito.*;

public class ApprovalPermissionServiceTest {

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
    public void setUp() {
        approvalPermissionService = new ApprovalPermissionService();
        approvalPermissionService.userService = userService;
        approvalPermissionService.resourceRoleService = resourceRoleService;
        approvalPermissionService.authenticationFacade = authenticationFacade;
        approvalPermissionService.userTaskCandidateRepository = userTaskCandidateRepository;
        approvalPermissionService.userTaskInstanceRepository = userTaskInstanceRepository;
        approvalPermissionService.userRepository = userRepository;
        approvalPermissionService.roleRepository = roleRepository;
        approvalPermissionService.userRoleRepository = userRoleRepository;
        approvalPermissionService.userResourceRoleRepository = userResourceRoleRepository;
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertNotNull(candidates);
        assertTrue(candidates.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(new ArrayList<>());

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(new ArrayList<>());

        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(1L, 2L);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(new ArrayList<>());
        when(userRoleRepository.findByRoleIdIn(any())).thenReturn(new ArrayList<>());
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(any())).thenReturn(new ArrayList<>());

        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());
        when(roleRepository.findByRoleIdsAndEnabled(any(), anyBoolean())).thenReturn(new ArrayList<>());

        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;

        when(approvalPermissionService.getApprovableApprovalInstances()).thenReturn(new ArrayList<>());

        boolean result = approvalPermissionService.isApprovable(approvalInstanceId);
        assertTrue(!result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>());
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(new HashSet<>());
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(any())).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepository.findByCandidateUserId(anyLong())).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), any())).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), any())).thenReturn(new ArrayList<>());

        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetApprovableApprovalInstancesByUserId() {
        long userId = 1L;

        when(userService.getUserRoleIds(userId)).thenReturn(new HashSet<>());
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId)).thenReturn(new HashSet<>());
        when(userTaskInstanceRepository.findByCandidateUserId(anyLong())).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), any())).thenReturn(new ArrayList<>());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), any())).thenReturn(new ArrayList<>());

        List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "ROLE";

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
    }

    @Test
    public void testListApprovableExternalInstances() {
        when(userTaskInstanceRepository.findByStatus(any())).thenReturn(new ArrayList<>());

        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(any(), any())).thenReturn(new ArrayList<>());
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(any())).thenReturn(new ArrayList<>());
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(any())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIds(any())).thenReturn(new ArrayList<>());

        Map<Long, Set<Long>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
// Include additional test cases if needed
