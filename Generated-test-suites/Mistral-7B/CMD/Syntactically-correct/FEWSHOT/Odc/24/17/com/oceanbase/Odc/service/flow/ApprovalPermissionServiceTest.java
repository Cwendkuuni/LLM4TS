package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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

@SpringJUnitConfig(classes = { ApprovalPermissionService.class })
@ContextConfiguration(classes = { ApprovalPermissionService.class })
public class ApprovalPermissionServiceTest {

    @Component
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

    private Long approvalInstanceId;
    private User user;
    private Role role;
    private UserTaskInstanceEntity userTaskInstance;
    private UserTaskInstanceCandidateEntity userTaskInstanceCandidate;
    private UserTaskInstanceCandidateEntity userTaskInstanceCandidateRole;

    @Before
    public void setup() {
        user = new User().setId(1L);
        role = new Role().setId(1L);
        userTaskInstance = new UserTaskInstanceEntity().setId(1L);
        userTaskInstanceCandidate = new UserTaskInstanceCandidateEntity().setApprovalInstanceId(approvalInstanceId)
                .setRoleId(role.getId()).setUserId(user.getId());
        userTaskInstanceCandidateRole = new UserTaskInstanceCandidateEntity().setApprovalInstanceId(approvalInstanceId)
                .setRoleId(role.getId()).setResourceRoleIdentifier("roleIdentifier");
    }

    @Test
    public void testGetCandidates() {
        approximationOfGetCandidates(false, user, null);
        approximationOfGetCandidates(true, user, null);
        approximationOfGetCandidates(false, null, role);
        approximationOfGetCandidates(true, null, role);
        approximationOfGetCandidates(false, user, role);
        approximationOfGetCandidates(true, user, role);

        // Test with multiple users and roles
        User user2 = new User().setId(2L);
        Role role2 = new Role().setId(2L);

        Long approvalInstanceId2 = 2L;

        userTaskInstanceCandidate.setApprovalInstanceId(approvalInstanceId2);
        userTaskInstanceCandidateRole.setApprovalInstanceId(approvalInstanceId2);

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(userTaskInstanceCandidate);
        entities.add(userTaskInstanceCandidateRole);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(entities);

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, false);
        assertTrue(candidates.size() > 0);
        assertTrue(candidates.contains(user));
        assertTrue(candidates.contains(new User().setId(user2.getId())));

        candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertTrue(candidates.size() > 0);
        assertTrue(candidates.contains(user));
        assertFalse(candidates.contains(new User().setId(user2.getId())));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        approvalPermissionService.getInstanceId2CandidateRoleIds(Collections.singletonList(approvalInstanceId));
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Collections.singletonList(approvalInstanceId));
    }

    @Test
    public void testGetCandidateUserIds() {
        approvalPermissionService.getCandidateUserIds(Collections.singletonList(approvalInstanceId));
    }

    @Test
    public void testGetCandidateRoles() {
        approvalPermissionService.getCandidateRoles(approvalInstanceId, false);
        approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
    }

    @Test
    public void testIsApprovable() {
        when(authenticationFacade.currentUserId()).thenReturn(user.getId());

        approvalPermissionService.isApprovable(approvalInstanceId);

        when(authenticationFacade.currentUserId()).thenReturn(null);

        approvalPermissionService.isApprovable(approvalInstanceId);
    }

    @Test
    public void testIsApprovableWithUserId() {
        appointmentOfIsApprovableWithUserId(user.getId());
        appointmentOfIsApprovableWithUserId(null);
    }

    @Test
    public void testListApprovableExternalInstances() {
        approvalPermissionService.listApprovableExternalInstances();
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singletonList(1L));
    }

    private void approximationOfGetCandidates(Boolean enabled, User user, Role role) {
        when(authenticationFacade.currentUserId()).thenReturn(user == null ? null : user.getId());
        when(userService.getCurrentUserRoleIds()).thenReturn(CollectionUtils.isNotEmpty(role) ? Collections.singletonList(role.getId())
                : Collections.emptyList());

        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();

        if (user != null) {
            entities.add(userTaskInstanceCandidate);
        }

        if (role != null) {
            entities.add(userTaskInstanceCandidateRole);
        }

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        if (user == null) {
            assertNull(candidates);
        } else {
            assertTrue(candidates.size() > 0);
            assertTrue(candidates.contains(user));
        }
    }

    private void appointmentOfIsApprovableWithUserId(Long userId) {
        when(authenticationFacade.currentUserId()).thenReturn(userId);
        approvalPermissionService.isApprovable(approvalInstanceId);
        when(authenticationFacade.currentUserId()).thenReturn(null);

        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(userTaskInstance);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(userTaskInstances);

        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }
}