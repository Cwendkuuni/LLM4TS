package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

    private Long approvalInstanceId = 1L;
    private Long userId = 1L;
    private Long roleId = 1L;
    private Long flowInstanceId = 1L;

    @Before
    public void setup() {
        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>(Arrays.asList(roleId)));
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());
        when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(Collections.emptyList());
        when(userRepository.findByUserIds(anySet())).thenReturn(Collections.emptyList());
        when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(Collections.emptyList());
        when(userRepository.findByUserIdsOrRoleIds(anySet(), anySet(), anyBoolean())).thenReturn(Collections.emptyList());
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet())).thenReturn(
                new ArrayList<>());
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.emptyList());
        when(userTaskInstanceRepository.findByFlowInstanceIdAndStatus(flowInstanceId, FlowNodeStatus.EXECUTING))
                .thenReturn(new UserTaskInstanceEntity());
    }

    @Test
    public void testGetCandidates_userIdEnabled_returnsUsers() {
        // given: user has the role and the role has a candidate user
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        when(userRoleRepository.findOne(roleId)).thenReturn(userRole);
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(userRepository.findOne(userId)).thenReturn(user);

        // when: call getCandidates method with userId and enabled = true
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, true);

        // then: result should contain the user
        assertEquals(1, result.size());
        assertTrue(result.contains(new User(user)));
    }

    @Test
    public void testGetCandidates_userIdDisabled_returnsEmptySet() {
        // given: user has the role and the role has a candidate user
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        when(userRoleRepository.findOne(roleId)).thenReturn(userRole);
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(userRepository.findOne(userId)).thenReturn(user);

        // when: call getCandidates method with userId and enabled = false
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, false);

        // then: result should be an empty set
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidates_roleEnabled_returnsUsers() {
        // given: role has candidate users
        when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
                .thenReturn(Arrays.asList(new Role("role1", new HashSet<>(Arrays.asList(userId, 2L)))));

        // when: call getCandidates method with approvalInstanceId and enabled = null
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, null);

        // then: result should contain the users
        assertEquals(2, result.size());
        assertTrue(result.contains(new User(userRepository.findOne(userId))));
        assertTrue(result.contains(new User(userRepository.findOne(2L))));
    }

    @Test
    public void testGetCandidates_roleDisabled_returnsEmptySet() {
        // given: role has candidate users
        when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
                .thenReturn(Arrays.asList(new Role("role1", new HashSet<>(Arrays.asList(userId, 2L)))));

        // when: call getCandidates method with approvalInstanceId and enabled = false
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, false);

        // then: result should be an empty set
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCandidates_bothUsersAndRoles_returnsUsersAndRoles() {
        // given: user has the role and the role has a candidate user, and there are also candidate users directly assigned to the approval instance
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        when(userRoleRepository.findOne(roleId)).thenReturn(userRole);
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(userRepository.findOne(userId)).thenReturn(user);

        UserTaskInstanceCandidateEntity userTaskCandidate1 = new UserTaskInstanceCandidateEntity();
        userTaskCandidate1.setApprovalInstanceId(approvalInstanceId);
        userTaskCandidate1.setUserId(2L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(
                new ArrayList<>(Arrays.asList(userTaskCandidate1)));

        // when: call getCandidates method with approvalInstanceId and enabled = null
        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, null);

        // then: result should contain the user and the directly assigned user
        assertEquals(2, result.size());
        assertTrue(result.contains(new User(user)));
        assertTrue(result.contains(new User(userRepository.findOne(2L))));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds_multipleApprovalInstances_returnsMap() {
        // given: multiple approval instances have different roles
        when(userTaskInstanceRepository.findByFlowInstanceIdAndStatus(flowInstanceId, FlowNodeStatus.EXECUTING))
                .thenReturn(new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByFlowInstanceIdAndStatus(anotherFlowInstanceId, FlowNodeStatus.EXECUTING))
                .thenReturn(new UserTaskInstanceEntity());
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(
                Arrays.asList(flowInstanceId, anotherFlowInstanceId));

        // then: result should contain the correct maps for each approval instance
        assertEquals(2, result.size());
        Map<Long, Set<Long>> expectedResult1 = new HashMap<>();
        Set<Long> roleIds1 = new HashSet<>();
        roleIds1.add(roleId);
        expectedResult1.put(flowInstanceId, roleIds1);
        Map<Long, Set<Long>> expectedResult2 = new HashMap<>();
        Set<Long> roleIds2 = new HashSet<>();
        roleIds2.add(anotherRoleId);
        expectedResult2.put(anotherFlowInstanceId, roleIds2);
        assertEquals(expectedResult1, result.get(flowInstanceId));
        assertEquals(expectedResult2, result.get(anotherFlowInstanceId));
    }

    // Add more test cases for other methods here

    @Test
    public void testSetCandidateResourceRole_setsCandidateResourceRole() {
        // given: approval instance exists
        UserTaskInstanceEntity approvalInstance = new UserTaskInstanceEntity();
        approvalInstance.setId(approvalInstanceId);
        when(userTaskInstanceRepository.findOne(approvalInstanceId)).thenReturn(approvalInstance);

        // when: call setCandidateResourceRole method
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, "resourceRoleIdentifier");

        // then: the approval instance should have the resource role identifier added
        verify(userTaskCandidateRepository).save(any(UserTaskInstanceCandidateEntity.class));
    }
}