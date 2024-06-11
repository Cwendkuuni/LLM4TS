package com.oceanbase.odc.service.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
import com.oceanbase.odc.metadb.iam.Role;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.User;
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
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Set<UserEntity> expectedCandidates = new HashSet<>();
        UserEntity userEntity = new UserEntity();
        expectedCandidates.add(userEntity);

        when(userService.getCurrentUserRoleIds()).thenReturn(new ArrayList<>());
        when(userRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(Collections.singletonList(userEntity));

        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expectedCandidates.size(), actualCandidates.size());
        for (User candidate : actualCandidates) {
            assertSame(userEntity, candidate);
        }
    }

    @Test
    public void testGetCandidates_rolesEnabled() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        Set<Role> expectedRoles = new HashSet<>();
        Role role = new Role();
        expectedRoles.add(role);

        when(userService.getCurrentUserRoleIds())
                .thenReturn(new ArrayList<>(Collections.singletonList(1L)));
        when(roleRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(Collections.singletonList(role));

        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expectedRoles.size(), actualCandidates.size());
        for (User user : actualCandidates) {
            assertTrue(user instanceof Role);
            assertEquals(role, user);
        }
    }

    @Test
    public void testGetCandidates_rolesDisabled() {
        Long approvalInstanceId = 1L;
        Boolean enabled = false;
        Set<Role> expectedRoles = new HashSet<>();
        Role role = new Role();
        expectedRoles.add(role);

        when(userService.getCurrentUserRoleIds())
                .thenReturn(new ArrayList<>(Collections.singletonList(1L)));
        when(roleRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(Collections.singletonList(role));

        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertNull(actualCandidates);
    }

    @Test
    public void testGetCandidates_noUserOrRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = null;

        when(userService.getCurrentUserRoleIds()).thenReturn(new ArrayList<>());
        when(roleRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean())).thenReturn(new ArrayList<>());
        when(userRepository.findByUserIdsAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(Collections.emptyList());

        Set<User> actualCandidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertNull(actualCandidates);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        Map<Long, Set<Long>> expectedMap = new HashMap<>();
        Set<Long> roleIds = new HashSet<>();
        roleIds.add(1L);
        expectedMap.put(1L, roleIds);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setApprovalInstanceId(1L);
        entity.setRoleId(1L);
        entities.add(entity);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);

        Map<Long, Set<Long>> actualMap = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertEquals(expectedMap.size(), actualMap.size());
        for (Map.Entry<Long, Set<Long>> entry : expectedMap.entrySet()) {
            assertEquals(entry.getValue(), actualMap.get(entry.getKey()));
        }
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        Map<Long, Set<String>> expectedMap = new HashMap<>();
        Set<String> resourceRoleIdentifiers = new LinkedList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier1");
        expectedMap.put(1L, resourceRoleIdentifiers);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setApprovalInstanceId(1L);
        entity.setResourceRoleIdentifier("resourceRoleIdentifier1");
        entities.add(entity);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(entities);

        Map<Long, Set<String>> actualMap = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                approvalInstanceIds);

        assertEquals(expectedMap.size(), actualMap.size());
        for (Map.Entry<Long, Set<String>> entry : expectedMap.entrySet()) {
            assertNotNull(entry.getValue());
            assertEquals(entry.getValue(), actualMap.get(entry.getKey()));
        }
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalInstanceIds.add(1L);

        Set<Long> expectedUserIds = new HashSet<>();
        expectedUserIds.add(1L);

        when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds)).thenCallRealMethod();
        List<UserTaskInstanceCandidateEntity> entities = userTaskCandidateRepository
                .findByApprovalInstanceIds(approvalInstanceIds);

        Set<Long> roleIds = new HashSet<>();
        roleIds.add(1L);
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUserId(1L);
        userRoleEntity.setRoleId(1L);
        when(userRoleRepository.findByRoleIdIn(anySet())).thenReturn(new ArrayList<>(Collections.singletonList(userRoleEntity)));

        Set<String> resourceRoleIdentifiers = new HashSet<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier1");
        UserResourceRoleEntity userResourceRoleEntity = new UserResourceRoleEntity();
        userResourceRoleEntity.setUserId(1L);
        userResourceRoleEntity.setResourceId(1L);
        userResourceRoleEntity.setResourceRoleIdentifier("resourceRoleIdentifier1");
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(anySet(), anySet()))
                .thenReturn(new ArrayList<>());

        Set<Long> actualUserIds = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

        assertEquals(expectedUserIds.size(), actualUserIds.size());
        for (Long userId : expectedUserIds) {
            assertTrue(actualUserIds.contains(userId));
        }
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setRoleId(1L);
        entities.add(entity1);

        List<Role> expectedRoles = new ArrayList<>();
        Role role = new Role();
        role.setId(1L);
        expectedRoles.add(role);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);
        when(roleRepository.findByRoleIdsAndEnabled(anyLong(), anyBoolean()))
                .thenReturn(Collections.singletonList(role));

        List<Role> actualRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

        assertEquals(expectedRoles.size(), actualRoles.size());
        for (Role role : actualRoles) {
            assertEquals(role, expectedRoles.get(0));
        }
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        Set<Long> approvableApprovalInstances = new HashSet<>();
        approvableApprovalInstances.add(1L);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)
                .stream().map(UserTaskInstanceEntity::getId)
                .collect(Collectors.toSet())).thenReturn(approvableApprovalInstances);

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testIsApprovable_notApprovable() {
        Long approvalInstanceId = 1L;
        Set<Long> approvableApprovalInstances = new HashSet<>();

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)
                .stream().map(UserTaskInstanceEntity::getId)
                .collect(Collectors.toSet()))
                .thenReturn(Collections.emptySet());

        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    // Add test cases for other methods like getApprovableApprovalInstances, setCandidateResourceRole, etc.

}