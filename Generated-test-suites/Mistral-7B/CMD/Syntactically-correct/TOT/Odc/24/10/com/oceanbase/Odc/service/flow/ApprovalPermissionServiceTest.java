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
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.Model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserRepository;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class ApprovalPermissionServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;
    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    private AuthenticationFacade authenticationFacade;
    @Mock
    private ResourceRoleService resourceRoleService;

    @Autowired
    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @BeforeAll
    public static void initMocks() {
        MockitoAnnotations.initMocks(ApprovalPermissionServiceTest.class);
    }

    @Test
    public void testGetCandidatesEmpty() {
        Long approvalInstanceId = 1L;
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.emptyList());
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Assertions.assertEmpty(candidates);
    }

    @Test
    public void testGetCandidatesWithUsers() {
        Long approvalInstanceId = 1L;
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        User user3 = new User();
        user3.setId(3L);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity());
        entities.add(new UserTaskInstanceCandidateEntity().setUserId(user1.getId()));
        entities.add(new UserTaskInstanceCandidateEntity().setUserId(user2.getId()));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Assertions.assertEquals(3, candidates.size());
        Assertions.assertTrue(candidates.contains(user1));
        Assertions.assertTrue(candidates.contains(user2));
        Assertions.assertFalse(candidates.contains(user3));
    }

    @Test
    public void testGetCandidatesWithRoles() {
        Long approvalInstanceId = 1L;
        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity().setRoleId(role1.getId()));
        entities.add(new UserTaskInstanceCandidateEntity().setRoleId(role2.getId()));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Assertions.assertEquals(0, candidates.size());

        when(userRepository.findByRoleIdsAndEnabled(role1.getIds(), true)).thenReturn(Arrays.asList(user1));
        when(userRepository.findByRoleIdsAndEnabled(role2.getIds(), true)).thenReturn(Arrays.asList(user2));
        candidates = approvalPermissionService.getCandidances(approvalInstanceId, true);
        Assertions.assertEquals(2, candidates.size());
        Assertions.assertTrue(candidates.contains(user1));
        Assertions.assertTrue(candidates.contains(user2));
    }

    @Test
    public void testGetCandidatesWithUsersAndRoles() {
        Long approvalInstanceId = 1L;
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Role role1 = new Role();
        role1.setId(1L);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity().setUserId(user1.getId()));
        entities.add(new UserTaskInstanceCandidateEntity().setRoleId(role1.getId()));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Assertions.assertEquals(1, candidates.size());
        Assertions.assertTrue(candidates.contains(user1));

        when(userRepository.findByRoleIdsAndEnabled(role1.getIds(), true)).thenReturn(Arrays.asList(user2));
        candidates = approvalPermissionService.getCandidances(approvalInstanceId, true);
        Assertions.assertEquals(2, candidates.size());
        Assertions.assertTrue(candidates.contains(user1));
        Assertions.assertTrue(candidates.contains(user2));
    }

    @Test
    public void testGetCandidateUserIdsEmpty() {
        Long approvalInstanceId = 1L;
        when(userTaskCandidateRepository.findByApprovalInstanceIds(Collections.singletonList(approvalInstanceId)))
                .thenReturn(Collections.emptyList());
        Set<Long> userIds = approvalPermissionService.getCandidateUserIds(Collections.singletonList(approvalInstanceId));
        Assertions.assertEquals(0, userIds.size());
    }

    @Test
    public void testGetCandidateUserIdsWithUsers() {
        Long approvalInstanceId = 1L;
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity().setUserId(user1.getId()));
        entities.add(new UserTaskInstanceCandidateEntity().setUserId(user2.getId()));

        when(userTaskCandidateRepository.findByApprovalInstanceIds(Collections.singletonList(approvalInstanceId)))
                .thenReturn(entities);
        Set<Long> userIds = approvalPermissionService.getCandidateUserIds(Collections.singletonList(approvalInstanceId));
        Assertions.assertEquals(2, userIds.size());
        Assertions.assertTrue(userIds.contains(user1.getId()));
        Assertions.assertTrue(userIds.contains(user2.getId()));
    }

    @Test
    public void testGetCandidateRolesEmpty() {
        Long approvalInstanceId = 1L;
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Collections.emptyList());
        List<Role> roles = approvalPermissionService.getCandidateRoles(approvalInstanceId, null);
        Assertions.assertEquals(0, roles.size());
    }

    @Test
    public void testGetCandidateRolesWithRoles() {
        Long approvalInstanceId = 1L;
        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity().setRoleId(role1.getId()));
        entities.add(new UserTaskInstanceCandidateEntity().setRoleId(role2.getId()));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        List<Role> roles = approvalPermissionService.getCandidateRoles(approvalInstanceId, null);
        Assertions.assertEquals(2, roles.size());
        Assertions.assertTrue(roles.contains(role1));
        Assertions.assertTrue(roles.contains(role2));
    }

    @Test
    public void testIsApprovableWithApprovableInstance() {
        Long approvableInstanceId = 1L;
        when(userTaskInstanceRepository.findById(approvableInstanceId)).thenReturn(Optional.of(new UserTaskInstanceEntity()));
        boolean result = approvalPermissionService.isApprovable(approvableInstanceId);
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsApprovableWithNonApprovableInstance() {
        Long nonApprovableInstanceId = 1L;
        when(userTaskInstanceRepository.findById(nonApprovableInstanceId))
                .thenReturn(Optional.of(new UserTaskInstanceEntity().setStatus(FlowNodeStatus.FAILED)));
        boolean result = approvalPermissionService.isApprovable(nonApprovableInstanceId);
        Assertions.assertFalse(result);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIdsEmpty() {
        List<Long> flowInstanceIds = Collections.emptyList();
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIdsWithUsers() {
        Long flowInstanceId1 = 1L;
        Long flowInstanceId2 = 2L;
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Map<Long, UserEntity> userMap = new HashMap<>();
        userMap.put(user1.getId(), user1);
        userMap.put(user2.getId(), user2);

        List<Long> flowInstanceIds = Arrays.asList(flowInstanceId1, flowInstanceId2);
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.EXECUTING))
                .thenReturn(Arrays.asList(approvalInstanceId1, approvalInstanceId2));
        when(userRepository.findByUserIds(Arrays.asList(user1.getId(), user2.getId())))
                .thenReturn(Arrays.asList(user1, user2));

        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(Collections.singletonList(user1), result.get(approvalInstanceId1));
        Assertions.assertEquals(Collections.singletonList(user2), result.get(approvalInstanceId2));
    }
}