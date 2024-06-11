package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.AuthenticationFacade;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.*;
import java.util.stream.Collectors;

@SpringJUnitConfig(classes = ApprovalPermissionService.class)
@Configuration
public class ApprovalPermissionServiceTest {

    @Autowired
    private ApprovalPermissionService approvalPermissionService;

    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private UserService userService;

    @MockBean
    private ResourceRoleService resourceRoleService;

    @MockBean
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @MockBean
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRoleRepository userRoleRepository;

    @MockBean
    private UserResourceRoleRepository userResourceRoleRepository;

    private Set<UserEntity> users;
    private Set<Role> roles;
    private Set<UserResourceRoleEntity> userResourceRoles;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        users = new HashSet<>();
        roles = new HashSet<>();
        userResourceRoles = new HashSet<>();
    }

    @After
    public void cleanup() {
        verificationComplete(authenticationFacade);
        verificationComplete(userService);
        verificationComplete(resourceRoleService);
        verificationComplete(userTaskCandidateRepository);
        verificationComplete(userTaskInstanceRepository);
        verificationComplete(userRepository);
        verificationComplete(roleRepository);
        verificationComplete(userRoleRepository);
        verificationComplete(userResourceRoleRepository);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        UserEntity user2 = new UserEntity();
        user2.setId(2L);

        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);

        UserRoleEntity userRole1 = new UserRoleEntity();
        userRole1.setUserId(user1.getId());
        userRole1.setRoleId(role1.getId());

        UserRoleEntity userRole2 = new UserRoleEntity();
        userRole2.setUserId(user2.getId());
        userRole2.setRoleId(role1.getId());

        UserResourceRoleEntity userResourceRole1 = new UserResourceRoleEntity();
        userResourceRole1.setUserId(user1.getId());
        userResourceRole1.setResourceRoleIdentifier("resourceRole1");

        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(1L), enabled)).thenReturn(Collections.singleton(user1));
        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(2L), enabled)).thenReturn(Collections.singleton(user2));
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), enabled)).thenReturn(Collections.singleton(role1));
        when(userRoleRepository.findByRoleId(1L)).thenReturn(Collections.singleton(userRole1));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(
                Collections.singleton(1L), Arrays.asList("resourceRole1"))).thenReturn(Collections.singletonList(userResourceRole1));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(3, candidates.size());
        assertTrue(candidates.contains(user1));
        assertTrue(candidates.contains(user2));
        assertFalse(candidates.contains(new UserEntity()));

        verification(userRepository).findByUserIdsAndEnabled(Collections.singleton(1L), enabled);
        verification(userRepository).findByUserIdsAndEnabled(Collections.singleton(2L), enabled);
        verification(roleRepository).findByRoleIdsAndEnabled(Collections.singleton(1L), enabled);
        verification(userRoleRepository).findByRoleId(1L);
        verification(userResourceRoleRepository).findByResourceIdsAndResourceRoleIdentifiersIn(
                Collections.singleton(1L), Arrays.asList("resourceRole1"));
    }

    @Test
    public void testGetCandidatesEmpty() {
        Long approvalInstanceId = 1L;
        Boolean enabled = null;

        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), enabled)).thenReturn(Collections.emptySet());
        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(1L), enabled)).thenReturn(Collections.emptySet());

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(0, candidates.size());

        verificationNoInteractions(userRepository);
        verificationNoInteractions(roleRepository);
    }

    @Test
    public void testGetCandidatesDisabled() {
        Long approvalInstanceId = 1L;
        Boolean enabled = false;

        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        UserEntity user2 = new UserEntity();
        user2.setId(2L);

        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);

        UserRoleEntity userRole1 = new UserRoleEntity();
        userRole1.setUserId(user1.getId());
        userRole1.setRoleId(role1.getId());

        UserRoleEntity userRole2 = new UserRoleEntity();
        userRole2.setUserId(user2.getId());
        userRole2.setRoleId(role1.getId());

        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(1L), enabled)).thenReturn(Collections.singleton(user1));
        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(2L), enabled)).thenReturn(Collections.singleton(user2));
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), enabled)).thenReturn(Collections.singleton(role1));
        when(userRoleRepository.findByRoleId(1L)).thenReturn(Collections.singleton(userRole1));
        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(1L), false)).thenReturn(Collections.singleton(user1));
        when(userRepository.findByUserIdsAndEnabled(Collections.singleton(2L), false)).thenReturn(Collections.singleton(user2));

        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(3, candidates.size());
        assertTrue(candidates.contains(user1));
        assertTrue(candidates.contains(user2));
        assertFalse(candidates.contains(new UserEntity()));

        verification(userRepository).findByUserIdsAndEnabled(Collections.singleton(1L), enabled);
        verification(userRepository).findByUserIdsAndEnabled(Collections.singleton(2L), enabled);
        verification(roleRepository).findByRoleIdsAndEnabled(Collections.singleton(1L), enabled);
        verification(userRoleRepository).findByRoleId(1L);
        verification(userRepository).findByUserIdsAndEnabled(Collections.singleton(1L), false);
        verification(userRepository).findByUserIdsAndEnabled(Collections.singleton(2L), false);
    }
}