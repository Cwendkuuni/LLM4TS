package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
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
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        // Test with empty approvalInstanceId and enabled = null
        Long approvalInstanceId = null;
        Boolean enabled = null;
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertTrue(candidates.isEmpty());

        // Test with non-empty approvalInstanceId and enabled = true
        approvalInstanceId = 1L;
        enabled = true;
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(1L);
        entities.add(entity1);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        Set<User> expectedUsers = new HashSet<>();
        User user = new User();
        user.setId(1L);
        expectedUsers.add(user);
        assertEquals(expectedUsers, candidates);

        // Test with non-empty approvalInstanceId and enabled = false
        enabled = false;
        entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);
        entities.add(entity2);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        Role role = new Role();
        role.setId(1L);
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), false)).thenReturn(Collections.singletonList(role));
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(Collections.singletonList(role), candidates.stream().map(user -> user.getRoles().get(0)).collect(Collectors.toList()));

        // Test with non-empty approvalInstanceId and enabled = null
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, null);
        Set<User> users = userRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
        expectedUsers = new HashSet<>(users);
        expectedUsers.addAll(Collections.singletonList(user));
        assertEquals(expectedUsers, candidates);

        // Test with empty approvalInstanceId and enabled = false
        approvalInstanceId = null;
        enabled = false;
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertTrue(candidates.isEmpty());

        // Test with non-empty approvalInstanceId and empty roles and users
        approvalInstanceId = 2L;
        entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity3 = new UserTaskInstanceCandidateEntity();
        entity3.setApprovalInstanceId(approvalInstanceId);
        entity3.setRoleId(null);
        entity3.setUserId(null);
        entities.add(entity3);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertTrue(candidates.isEmpty());
    }

    @Test
    public void testGetCandidateRoles() {
        // Test with empty approvalInstanceId and enabled = null
        Long approvalInstanceId = null;
        Boolean enabled = null;
        List<Role> roles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertTrue(roles.isEmpty());

        // Test with non-empty approvalInstanceId and enabled = true
        approvalInstanceId = 1L;
        enabled = true;
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setRoleId(1L);
        entities.add(entity1);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        Role role = new Role();
        role.setId(1L);
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true)).thenReturn(Collections.singletonList(role));
        roles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertEquals(Collections.singletonList(role), roles);

        // Test with non-empty approvalInstanceId and enabled = false
        enabled = false;
        entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setRoleId(1L);
        entities.add(entity2);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), false)).thenReturn(Collections.emptyList());
        roles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertTrue(roles.isEmpty());

        // Test with non-empty approvalInstanceId and empty roles
        approvalInstanceId = 2L;
        entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity3 = new UserTaskInstanceCandidateEntity();
        entity3.setApprovalInstanceId(approvalInstanceId);
        entity3.setRoleId(null);
        entities.add(entity3);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);
        roles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertTrue(roles.isEmpty());
    }

    @Test
    public void testIsApprovable() {
        long userId = 1L;
        Long approvalInstanceId = 1L;
        Set<Long> roleIds = Collections.singletonList(1L);
        Set<String> resourceRoleIdentifiers = Collections.singletonList("roleIdentifier");
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        userTaskInstanceEntity1.setId(1L);
        userTaskInstanceEntity1.setFlowInstanceId(1L);
        userTaskInstanceEntity1.setCandidateUserId(userId);
        userTaskInstanceEntity1.setCandidateRoleIds(roleIds);
        userTaskInstanceEntity1.setResourceRoleIdentifier(resourceRoleIdentifiers.get(0));
        userTaskInstanceEntities.add(userTaskInstanceEntity1);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(userTaskInstanceEntities);
        when(userService.getCurrentUserRoleIds()).thenReturn(roleIds);
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId)).thenReturn(resourceRoleIdentifiers);

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));

        roleIds = Collections.emptySet();
        when(userService.getUserRoleIds(userId)).thenReturn(roleIds);
        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));

        resourceRoleIdentifiers = Collections.emptyList();
        when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId)).thenReturn(resourceRoleIdentifiers);
        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "resourceRoleIdentifier";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setApprovalInstanceId(approvalInstanceId);
        candidateEntity.setResourceRoleIdentifier(resourceRoleIdentifier);
        when(userTaskCandidateRepository.save(candidateEntity)).thenReturn(candidateEntity);
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
        verify(userTaskCandidateRepository).save(candidateEntity);
    }
}