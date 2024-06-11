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
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepositoryMock;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepositoryMockFactory;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepositoryMockImpl;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.RoleRepositoryMock;
import com.oceanbase.odc.metadb.iam.RoleRepositoryMockFactory;
import com.oceanbase.odc.metadb.iam.RoleRepositoryMockImpl;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserEntityMockFactory;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRepositoryMock;
import com.oceanbase.odc.metadb.iam.UserRepositoryMockFactory;
import com.oceanbase.odc.metadb.iam.UserRepositoryMockImpl;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntityMockFactory;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.UserRoleRepositoryMock;
import com.oceanbase.odc.metadb.iam.UserRoleRepositoryMockFactory;
import com.oceanbase.odc.metadb.iam.UserRoleRepositoryMockImpl;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepositoryMock;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepositoryMockFactory;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepositoryMockImpl;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.ApprovalPermissionServiceMockFactory;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMock;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeMockFactory;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

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
    @SkipAuthorize
    private ApprovalPermissionService approvalPermissionService;

    private UserTaskInstanceCandidateEntity testCandidateEntity;
    private UserTaskInstanceCandidateEntity testCandidateEntity2;

    private UserTaskInstanceEntity testUserTaskInstanceEntity;
    private UserTaskInstanceEntity testUserTaskInstanceEntity2;

    private UserEntity testUserEntity;
    private UserEntity testUserEntity2;

    private Role testRole;

    private UserRoleEntity testUserRoleEntity;
    private UserRoleEntity testUserRoleEntity2;

    private UserResourceRoleEntity testUserResourceRoleEntity;
    private UserResourceRoleEntity testUserResourceRoleEntity2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testCandidateEntity = mock(UserTaskInstanceCandidateEntity.class);
        testCandidateEntity2 = mock(UserTaskInstanceCandidateEntity.class);
        when(testCandidateEntity.getApprovalInstanceId()).thenReturn(1L);
        when(testCandidateEntity.getRoleId()).thenReturn(1L);
        when(testCandidateEntity.getUserId()).thenReturn(1L);
        when(testCandidateEntity.getResourceRoleIdentifier()).thenReturn("testRoleIdentifier");

        testCandidateEntity2.getApprovalInstanceId();
        when(testCandidateEntity2.getRoleId()).thenReturn(2L);
        when(testCandidateEntity2.getUserId()).thenReturn(2L);

        testUserTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
        testUserTaskInstanceEntity2 = mock(UserTaskInstanceEntity.class);
        when(testUserTaskInstanceEntity.getId()).thenReturn(1L);
        when(testUserTaskInstanceEntity.getFlowInstanceId()).thenReturn(1L);
        when(testUserTaskInstanceEntity.getStatus()).thenReturn(FlowNodeStatus.EXECUTING);

        testUserTaskInstanceEntity2.getId();
        when(testUserTaskInstanceEntity2.getFlowInstanceId()).thenReturn(2L);
        when(testUserTaskInstanceEntity2.getStatus()).thenReturn(FlowNodeStatus.EXECUTING);

        testUserEntity = UserEntityMockFactory.newUserEntity(1L, "testUserName1");
        testUserEntity2 = UserEntityMockFactory.newUserEntity(2L, "testUserName2");

        testRole = RoleMockFactory.newRole(1L, "testRoleName");

        testUserRoleEntity = UserRoleEntityMockFactory.newUserRoleEntity(1L, 1L, "testRoleName1");
        testUserRoleEntity2 = UserRoleEntityMockFactory.newUserRoleEntity(2L, 1L, "testRoleName2");

        testUserResourceRoleEntity = UserResourceRoleEntityMockFactory.newUserResourceRoleEntity(1L,
                "testResourceIdentifier", "testRoleName");
        testUserResourceRoleEntity2 = UserResourceRoleEntityMockFactory.newUserResourceRoleEntity(2L,
                "testResourceIdentifier2", "testRoleName");

        AuthenticationFacadeMock authenticationFacadeMock = AuthenticationFacadeMockFactory.newAuthenticationFacadeMock(
                1L);
        when(authenticationFacade.currentUserId()).thenReturn(1L);

        when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>(Collections.singleton(1L)));
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(
                new HashSet<>(Collections.singleton("testResourceIdentifier")));
        when(userRepository.findByUserIds(new HashSet<>(Collections.singleton(1L)))).thenReturn(
                new HashSet<>(Collections.singleton(testUserEntity)));
        when(userRepository.findByUserIds(new HashSet<>(Collections.singleton(2L)))).thenReturn(
                new HashSet<>(Collections.singleton(testUserEntity2)));
        when(roleRepository.findByRoleIdsAndEnabled(new HashSet<>(Collections.singleton(1L)), true)).thenReturn(
                new HashSet<>(Collections.singleton(testRole)));

        UserTaskInstanceCandidateRepositoryMock userTaskCandidateRepositoryMock =
                UserTaskInstanceCandidateRepositoryMockFactory.newUserTaskInstanceCandidateRepositoryMock(
                        new HashSet<>(Collections.singleton(testCandidateEntity)));
        when(userTaskCandidateRepository).thenReturn(userTaskCandidateRepositoryMock);

        UserTaskInstanceCandidateRepositoryMockImpl userTaskInstanceCandidateRepositoryMockImpl =
                (UserTaskInstanceCandidateRepositoryMockImpl) userTaskCandidateRepositoryMock;
        when(userTaskInstanceCandidateRepositoryMockImpl.save(any(UserTaskInstanceCandidateEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userTaskInstanceRepository.findByApprovalInstanceIds(any(Collection.class)))
                .thenReturn(new ArrayList<>(Arrays.asList(testUserTaskInstanceEntity, testUserTaskInstanceEntity2)));

        UserTaskInstanceRepositoryMock userTaskInstanceRepositoryMock =
                UserTaskInstanceRepositoryMockFactory.newUserTaskInstanceRepositoryMock(
                        new ArrayList<>(Arrays.asList(testUserTaskInstanceEntity, testUserTaskInstanceEntity2)));
        when(userTaskInstanceRepository).thenReturn(userTaskInstanceRepositoryMock);

        when(roleRepository.findByRoleIdsAndEnabled(any(Collection.class), anyBoolean())).thenAnswer(
                invocation -> invocation.getArgument(0).stream()
                        .map(r -> RoleMockFactory.newRole(r.getId(), "testRoleName"))
                        .collect(Collectors.toList()));

        RoleRepositoryMockImpl roleRepositoryMockImpl = (RoleRepositoryMockImpl) roleRepository;
        when(roleRepositoryMockImpl.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(userRoleRepository.findByRoleIdIn(new HashSet<>(Collections.singleton(1L)))).thenReturn(
                new HashSet<>(Collections.singleton(testUserRoleEntity)));

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdentifiersIn(
                any(Collection.class), any(Collection.class)))
                .thenReturn(new HashSet<>(Arrays.asList(testUserResourceRoleEntity, testUserResourceRoleEntity2)));

        ApprovalPermissionService approvalPermissionServiceMock =
                ApprovalPermissionServiceMockFactory.newApprovalPermissionServiceMock(approvalPermissionService);
        when(approvalPermissionService).thenReturn(approvalPermissionServiceMock);
    }

    @Test
    public void testGetCandidates() {
        long approvalInstanceId = 1L;
        Boolean enabled = true;

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
        assertEquals(1, result.size());
        assertEquals(testUserEntity, result.iterator().next());

        assertNull(approvalPermissionService.getCandidates(approvalInstanceId, null));
        assertNull(approvalPermissionService.getCandidates(approvalInstanceId, false));
    }

    @Test
    public void testGetStringByIdence() {
        String result = approvalPermissionService.stripLeadingAndTrailingQuotes("foo");
        assertEquals("foo", result);

        String result2 = approvalPermissionService.stripLeadingAndTrailingQuotes("\"foo\"");
        assertEquals("foo", result2);

        String result3 = approvalPermissionService.stripLeadingAndTrailingQuotes("\"foo\" bar");
        assertEquals("foo bar", result3);

        String result4 = approvalPermissionService.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\"");
        assertEquals("foo and bar", result4);

        String result5 = approvalPermissionService.stripLeadingAndTrailingQuotes("");
        assertEquals("", result5);
    }

    @Test
    public void testStripLeadingHyphens() {
        String result = approvalPermissionService.stripLeadingHyphens("-f");
        assertEquals("f", result);

        String result2 = approvalPermissionService.stripLeadingHyphens("--foo");
        assertEquals("foo", result2);

        String result3 = approvalPermissionService.stripLeadingHyphens("---foo");
        assertEquals("foo", result3);

        String result4 = approvalPermissionService.stripLeadingHyphens(null);
        assertNull(result4);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>(Arrays.asList(1L, 2L));
        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>(Arrays.asList(1L, 2L));
        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                approvalInstanceIds);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>(Arrays.asList(1L, 2L));
        Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
        assertEquals(1, result.size());
        assertEquals(testRole, result.get(0));

        assertNull(approvalPermissionService.getCandidateRoles(approvalInstanceId, null));
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));

        assertFalse(approvalPermissionService.isApprovable(2L));
    }

    @Test
    public void testIsApprovableWithAuthenticationFacade() {
        Long approvalInstanceId = 1L;
        Long currentUserId = 1L;

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));

        when(authenticationFacade.currentUserId()).thenReturn(2L);

        assertFalse(approvalPermissionService.isApprovable(approvalInstanceId));
    }

    @Test
    public void testListApprovableExternalInstances() {
        Collection<Long> flowInstanceIds = new ArrayList<>(Arrays.asList(1L, 2L));
        Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertEquals(2, result.size());
    }
}