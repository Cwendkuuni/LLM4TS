package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedFieldUtils;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateTestDataBuilder;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateTestUtil;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntityTestDataBuilder;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntityTestUtil;
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.User;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserEntityTestDataBuilder;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.ApprovalPermissionServiceTestDataBuilder;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.RoleType;

public class ApprovalPermissionServiceTest {

    @Autowired
    private ApprovalPermissionService approvalPermissionService;
    @Autowired
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserResourceRoleRepository userResourceRoleRepository;
    @Autowired
    private ResourceRoleService resourceRoleService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    private Long userId;
    private Long approvalInstanceId;
    private UserTaskInstanceEntity approvalInstance;
    private UserTaskInstanceCandidateEntity candidate;
    private Role role;
    private UserEntity user;
    private UserResourceRoleEntity userResourceRole;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        AuthenticationFacade authenticationFacadeMock = Mockito.mock(AuthenticationFacade.class);
        Mockito.when(authenticationFacade.currentUserId()).thenReturn(userId);
        AutowiredAnnotatedFieldUtils.setFieldValue(approvalPermissionService, "authenticationFacade",
                authenticationFacadeMock);
        ApprovalPermissionServiceTestDataBuilder approvalPermissionServiceTestDataBuilder = new ApprovalPermissionServiceTestDataBuilder();
        approvalPermissionService = approvalPermissionServiceTestDataBuilder.build();
        user = new UserEntityTestDataBuilder().build();
        userRepository.save(user);
        role = new RoleTestDataBuilder().setType(RoleType.APPROVAL).build();
        roleRepository.save(role);
        userResourceRole = new UserResourceRoleEntity();
        userResourceRole.setUserId(user.getId());
        userResourceRole.setRoleId(role.getId());
        userResourceRoleRepository.save(userResourceRole);
        approvalInstance = new UserTaskInstanceEntityTestDataBuilder().build();
        userTaskInstanceRepository.save(approvalInstance);
        candidate = new UserTaskInstanceCandidateEntity();
        candidate.setApprovalInstanceId(approvalInstance.getId());
        candidate.setUserId(user.getId());
        candidate.setRoleId(role.getId());
        userTaskCandidateRepository.save(candidate);
    }

    @Test
    public void testGetCandidates() {
        // test getCandidates with enabled = null
        Set<User> candidates1 = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertTrue(candidates1.contains(UserEntityTestUtil.copyOf(user)));
        // test getCandidates with enabled = true
        Set<User> candidates2 = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertTrue(candidates2.contains(UserEntityTestUtil.copyOf(user)));
        // test getCandidates with enabled = false
        Set<User> candidates3 = approvalPermissionService.getCandidates(approvalInstanceId, false);
        assertFalse(candidates3.contains(UserEntityTestUtil.copyOf(user)));
        // test getCandidates with empty approvalInstanceId
        Set<User> candidates4 = approvalPermissionService.getCandidates(null, false);
        assertTrue(candidates4.isEmpty());
        // test getCandidates with empty enabled
        Set<User> candidates5 = approvalPermissionService.getCandidates(approvalInstanceId, null);
        assertTrue(candidates5.contains(UserEntityTestUtil.copyOf(user)));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(approvalInstance.getId());
        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        Set<Long> candidateRoleIds = instanceId2CandidateRoleIds.get(approvalInstanceId);
        assertTrue(candidateRoleIds.contains(role.getId()));
        // test getInstanceId2CandidateRoleIds with empty approvalInstanceIds
        Map<Long, Set<Long>> instanceId2CandidateRoleIds1 = approvalPermissionService.getInstanceId2CandidateRoleIds(new ArrayList<>());
        assertTrue(instanceId2CandidateRoleIds1.isEmpty());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(approvalInstance.getId());
        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService
                .getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        Set<String> candidateResourceRoleIdentifierIds = instanceId2CandidateResourceRoleIdentifierIds.get(approvalInstanceId);
        assertTrue(candidateResourceRoleIdentifierIds.contains(role.getIdentifier()));
        // test getInstanceId2CandidateResourceRoleIdentifierIds with empty approvalInstanceIds
        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds1 = approvalPermissionService
                .getInstanceId2CandidateResourceRoleIdentifierIds(new ArrayList<>());
        assertTrue(instanceId2CandidateResourceRoleIdentifierIds1.isEmpty());
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = Arrays.asList(approvalInstance.getId());
        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertTrue(candidateUserIds.contains(user.getId()));
        // test getCandidateUserIds with empty approvalInstanceIds
        Set<Long> candidateUserIds1 = approvalPermissionService.getCandidateUserIds(new ArrayList<>());
        assertTrue(candidateUserIds1.isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, false);
        assertTrue(candidateRoles.isEmpty());
        // test getCandidateRoles with enabled = true
        List<Role> candidateRoles1 = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        assertTrue(candidateRoles1.isEmpty());
        // test getCandidateRoles with empty approvalInstanceId
        List<Role> candidateRoles2 = approvalPermissionService.getCandidateRoles(null, false);
        assertTrue(candidateRoles2.isEmpty());
    }

    @Test
    public void testIsApprovable() {
        // test isApprovable with null approvalInstanceId
        assertFalse(approvalPermissionService.isApprovable(null));
        // test isApprovable with empty approvableApprovalInstances
        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
        // test isApprovable with approvableApprovalInstances containing approvalInstanceId
        ApprovalPermissionServiceTestDataBuilder approvalPermissionServiceTestDataBuilder = new ApprovalPermissionServiceTestDataBuilder();
        ApprovalPermissionService approvalPermissionService1 = approvalPermissionServiceTestDataBuilder.build();
        UserTaskInstanceEntity approvableApprovalInstance = new UserTaskInstanceEntityTestDataBuilder()
                .setId(approvalInstanceId).setStatus(FlowNodeStatus.APPROVABLE).build();
        approvalPermissionService1.userTaskInstanceRepository.save(approvableApprovalInstance);
        assertTrue(approvalPermissionService1.isApprovable(approvalInstanceId));
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> approvableExternalInstances = approvalPermissionService.listApprovableExternalInstances();
        assertTrue(approvableExternalInstances.isEmpty());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = Arrays.asList(approvalInstance.getFlowInstanceId());
        Map<Long, Set<UserEntity>> instanceId2Candidates = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        Set<UserEntity> candidates = instanceId2Candidates.get(approvalInstanceId);
        assertEquals(UserEntityTestUtil.copyOf(user), candidates.stream().findFirst().get());
        // test getCandidatesByFlowInstanceIds with empty flowInstanceIds
        Map<Long, Set<UserEntity>> instanceId2Candidates1 = approvalPermissionService
                .getCandidatesByFlowInstanceIds(new ArrayList<>());
        assertTrue(instanceId2Candidates1.isEmpty());
    }
}