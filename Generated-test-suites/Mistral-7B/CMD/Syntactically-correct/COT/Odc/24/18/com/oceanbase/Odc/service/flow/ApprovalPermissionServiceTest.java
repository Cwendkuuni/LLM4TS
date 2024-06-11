package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@RunWith(MockitoJUnitRunner.class)
@Component
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

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Set<User> expectedCandidates = new HashSet<>();
        User user = new User();
        expectedCandidates.add(user);

        when(userRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(Arrays.asList(user));

        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expectedCandidates, actualCandidates);

        verify(userRepository, times(1)).findByRoleIdsAndEnabled(anyLong(), anyBoolean());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        Map<Long, Set<Long>> expectedInstanceId2CandidateRoleIds = new HashMap<>();
        expectedInstanceId2CandidateRoleIds.put(1L, new HashSet<>(Arrays.asList(1L, 2L)));

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(new ArrayList<>());

        Map<Long, Set<Long>> actualInstanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertEquals(expectedInstanceId2CandidateRoleIds, actualInstanceId2CandidateRoleIds);

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        Map<Long, Set<String>> expectedInstanceId2CandidateResourceRoleIdentifierIds = new HashMap<>();
        expectedInstanceId2CandidateResourceRoleIdentifierIds.put(1L, new HashSet<>(Arrays.asList("role1", "role2")));

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenReturn(new ArrayList<>());

        Map<Long, Set<String>> actualInstanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

        assertEquals(expectedInstanceId2CandidateResourceRoleIdentifierIds, actualInstanceId2CandidateResourceRoleIdentifierIds);

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(approvalInstanceIds);
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        Set<Long> expectedCandidateUserIds = new HashSet<>(Arrays.asList(1L, 2L));

        when(userRoleRepository.findByRoleIdIn(any()))
                .thenReturn(Arrays.asList(new UserRoleEntity()));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(any(), any()))
                .thenReturn(Arrays.asList(new UserResourceRoleEntity()));

        Set<Long> actualCandidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        assertEquals(expectedCandidateUserIds, actualCandidateUserIds);

        verify(userRoleRepository, times(1)).findByRoleIdIn(any());
        verify(userResourceRoleRepository, times(1)).findByResourceIdsAndResourceRoleIdsIn(any(), any());
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<Role> expectedRoles = new ArrayList<>();
        Role role = new Role();
        expectedRoles.add(role);

        when(roleRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(Arrays.asList(role));

        List<Role> actualRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        assertEquals(expectedRoles, actualRoles);

        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(anyLong(), anyBoolean());
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        Set<Long> approvableApprovalInstances = new HashSet<>(Arrays.asList(1L, 2L));

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(Arrays.asList(new UserTaskInstanceEntity()));

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));

        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        long userId = 1L;
        Set<Long> roleIds = new HashSet<>(Arrays.asList(1L, 2L));
        Set<String> resourceRoleIdentifiers = new HashSet<>(Arrays.asList("role1", "role2"));

        List<UserTaskInstanceEntity> expectedApprovableApprovalInstances = Arrays.asList(new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());

        when(userService.getCurrentUserRoleIds()).thenReturn(roleIds);
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(resourceRoleIdentifiers);
        when(userTaskInstanceRepository.findByCandidateUserId(userId))
                .thenReturn(Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity()));
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
                .thenReturn(new ArrayList<>());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds))
                .thenReturn(expectedApprovableApprovalInstances);

        List<UserTaskInstanceEntity> actualApprovableApprovalInstances = approvalPermissionService.getApprovableApprovalInstances(userId);

        assertEquals(expectedApprovableApprovalInstances, actualApprovableApprovalInstances);

        verify(userService, times(1)).getCurrentUserRoleIds();
        verify(userService, times(1)).getCurrentUserResourceRoleIdentifiers();
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserId(userId);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "role1";

        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();

        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

        verify(userTaskCandidateRepository, times(1)).save(candidateEntity);
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> expectedApprovableExternalInstances = Arrays.asList(new UserTaskInstanceEntity(),
                new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(expectedApprovableExternalInstances);

        List<UserTaskInstanceEntity> actualApprovableExternalInstances = approvalPermissionService.listApprovableExternalInstances();

        assertEquals(expectedApprovableExternalInstances, actualApprovableExternalInstances);

        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);

        Map<Long, Set<UserEntity>> expectedInstanceId2Candidates = new HashMap<>();
        UserEntity user = new UserEntity();
        expectedInstanceId2Candidates.put(1L, new HashSet<>(Arrays.asList(user)));

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
                FlowNodeStatus.EXECUTING))
                .thenReturn(new HashMap<>());
        when(userTaskInstanceRepository.findByFlowInstanceIdIn(flowInstanceIds))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));
        when(userRepository.findByUserIds(Arrays.asList(user.getId())))
                .thenReturn(Arrays.asList(user));

        Map<Long, Set<UserEntity>> actualInstanceId2Candidates = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

        assertEquals(expectedInstanceId2Candidates, actualInstanceId2Candidates);

        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
                FlowNodeStatus.EXECUTING);
        verify(userTaskInstanceRepository, times(1)).findByFlowInstanceIdIn(flowInstanceIds);
        verify(userRepository, times(1)).findByUserIds(Arrays.asList(user.getId()));
    }
}