package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        UserEntity user = new UserEntity();
        user.setId(1L);
        UserEntity user2 = new UserEntity();
        user2.setId(2L);

        Role role = new Role();
        role.setId(1L);
        role.setName("role1");

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());

        UserResourceRoleEntity userResourceRole = new UserResourceRoleEntity();
        userResourceRole.setUserId(user.getId());
        userResourceRole.setResourceId(1L);
        userResourceRole.setResourceRoleIdentifier("resourceRoleIdentifier1");

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId);
        entity1.setUserId(user.getId());
        entity1.setRoleId(role.getId());
        entities.add(entity1);

        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId);
        entity2.setUserId(user2.getId());
        entities.add(entity2);

        UserTaskInstanceCandidateEntity entity3 = new UserTaskInstanceCandidateEntity();
        entity3.setApprovalInstanceId(approvalInstanceId);
        entity3.setRoleId(role.getId());

        when(userRepository.findByRoleIdsAndEnabled(role.getId(), enabled)).thenReturn(Collections.singletonList(user));
        when(userRepository.findByRoleIdsAndEnabled(role.getId(), false)).thenReturn(Collections.singletonList(user2));
        when(userRepository.findByUserIdsAndEnabled(Collections.singletonList(user.getId()), enabled)).thenReturn(
                Collections.singletonList(user));
        when(userRepository.findByUserIdsAndEnabled(Collections.singletonList(user2.getId()), enabled)).thenReturn(
                Collections.singletonList(user2));
        when(userRepository.findByUserIdsOrRoleIds(userIds -> userIds.contains(user.getId()) || userIds.contains(role.getId()),
                Collections.singletonList(user), Collections.singletonList(role))).thenReturn(Collections.singletonList(user));

        when(userRoleRepository.findByRoleIdIn(Collections.singletonList(role.getId()))).thenReturn(
                Collections.singletonList(userRole));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(Collections.singletonList(1L),
                Collections.singletonList("resourceRoleIdentifier1"))).thenReturn(Collections.singletonList(userResourceRole));

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(user));
        assertTrue(candidates.contains(user2));

        verify(userRepository, times(3)).findByRoleIdsAndEnabled(role.getId(), enabled);
        verify(userRepository, times(2)).findByRoleIdsAndEnabled(role.getId(), false);
        verify(userRepository, times(2)).findByUserIdsAndEnabled(Collections.singletonList(user.getId()), enabled);
        verify(userRepository, times(2)).findByUserIdsAndEnabled(Collections.singletonList(user2.getId()), enabled);
        verify(userRepository, times(1)).findByUserIdsOrRoleIds(userIds -> userIds.contains(user.getId()) || userIds.contains(role.getId()),
                Collections.singletonList(user), Collections.singletonList(role));
        verify(userRoleRepository, times(1)).findByRoleIdIn(Collections.singletonList(role.getId()));
        verify(userResourceRoleRepository, times(1)).findByResourceIdsAndResourceRoleIdentifiersIn(
                Collections.singletonList(1L), Collections.singletonList("resourceRoleIdentifier1"));
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(approvalInstanceId);
    }

    // Test other methods...

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Test with empty approvalInstanceIds

        // Test with non-empty approvalInstanceIds and corresponding entities

        // Test with non-empty approvalInstanceIds and no entities
    }

    // Test other methods...

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        // Test with empty approvalInstanceIds

        // Test with non-empty approvalInstanceIds and corresponding entities

        // Test with non-empty approvalInstanceIds and no entities
    }

    // Test other methods...

    @Test
    public void testGetCandidateUserIds() {
        // Test with empty approvalInstanceIds

        // Test with non-empty approvalInstanceIds and corresponding entities
    }

    // Test other methods...

    @Test
    public void testGetCandidateRoles() {
        // Test with empty approvalInstanceId and enabled false

        // Test with non-empty approvalInstanceId and enabled true

        // Test with non-empty approvalInstanceId and enabled false
    }

    // Test other methods...

    @Test
    public void testIsApprovable() {
        // Test with non-existent approvalInstanceId

        // Test with approvable approvalInstanceId

        // Test with non-approvable approvalInstanceId
    }

    // Test other methods...

    @Test
    public void testListApprovableApprovalInstances() {
        // Test with empty userService.getCurrentUserRoleIds()

        // Test with non-empty userService.getCurrentUserRoleIds() and empty userService.getCurrentUserResourceRoleIdentifiers()

        // Test with non-empty userService.getCurrentUserRoleIds() and non-empty userService.getCurrentUserResourceRoleIdentifiers()

        // Test with empty userService.getCurrentUserRoleIds() and non-empty userService.getCurrentUserResourceRoleIdentifiers()
    }

    // Test other methods...

    @Test
    public void testSetCandidateResourceRole() {
        // Test with null approvalInstanceId

        // Test with null resourceRoleIdentifier

        // Test with valid approvalInstanceId and resourceRoleIdentifier
    }

    // Test other methods...

    @Test
    public void testListApprovableExternalInstances() {
        // Test with empty userTaskInstanceRepository.findByStatus()

        // Test with non-empty userTaskInstanceRepository.findByStatus()
    }

    // Test other methods...

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        // Test with empty flowInstanceIds

        // Test with non-empty flowInstanceIds and executing approval instances

        // Test with non-empty flowInstanceIds and no executing approval instances
    }
}