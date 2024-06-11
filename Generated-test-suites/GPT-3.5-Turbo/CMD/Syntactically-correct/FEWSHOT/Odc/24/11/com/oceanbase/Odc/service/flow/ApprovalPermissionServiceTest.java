
package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;
    private UserTaskInstanceRepository userTaskInstanceRepositoryMock;
    private UserService userServiceMock;
    private AuthenticationFacade authenticationFacadeMock;

    @Before
    public void setUp() {
        approvalPermissionService = new ApprovalPermissionService();
        userTaskInstanceRepositoryMock = mock(UserTaskInstanceRepository.class);
        userServiceMock = mock(UserService.class);
        authenticationFacadeMock = mock(AuthenticationFacade.class);

        approvalPermissionService.setUserTaskInstanceRepository(userTaskInstanceRepositoryMock);
        approvalPermissionService.setUserService(userServiceMock);
        approvalPermissionService.setAuthenticationFacade(authenticationFacadeMock);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Set<User> expectedUsers = new HashSet<>(Collections.singletonList(new User("testUser")));

        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setUserId(1L);

        when(userTaskInstanceRepositoryMock.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Collections.singletonList(candidateEntity));

        Set<User> actualUsers = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L);
        Map<Long, Set<Long>> expectedMap = Map.of(1L, new HashSet<>(Collections.singletonList(1L)),
                2L, new HashSet<>(Collections.singletonList(2L)));

        UserTaskInstanceCandidateEntity candidateEntity1 = new UserTaskInstanceCandidateEntity();
        candidateEntity1.setApprovalInstanceId(1L);
        candidateEntity1.setRoleId(1L);

        UserTaskInstanceCandidateEntity candidateEntity2 = new UserTaskInstanceCandidateEntity();
        candidateEntity2.setApprovalInstanceId(2L);
        candidateEntity2.setRoleId(2L);

        when(userTaskInstanceRepositoryMock.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(Arrays.asList(candidateEntity1, candidateEntity2));

        Map<Long, Set<Long>> actualMap = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        List<Long> approvalInstanceIds = Arrays.asList(1L, 2L);
        Map<Long, Set<String>> expectedMap = Map.of(1L, new HashSet<>(Collections.singletonList("resourceRole1")),
                2L, new HashSet<>(Collections.singletonList("resourceRole2")));

        UserTaskInstanceCandidateEntity candidateEntity1 = new UserTaskInstanceCandidateEntity();
        candidateEntity1.setApprovalInstanceId(1L);
        candidateEntity1.setResourceRoleIdentifier("resourceRole1");

        UserTaskInstanceCandidateEntity candidateEntity2 = new UserTaskInstanceCandidateEntity();
        candidateEntity2.setApprovalInstanceId(2L);
        candidateEntity2.setResourceRoleIdentifier("resourceRole2");

        when(userTaskInstanceRepositoryMock.findByApprovalInstanceIds(approvalInstanceIds))
                .thenReturn(Arrays.asList(candidateEntity1, candidateEntity2));

        Map<Long, Set<String>> actualMap = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        List<Role> expectedRoles = Collections.singletonList(new Role("testRole"));

        UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setRoleId(1L);

        when(userTaskInstanceRepositoryMock.findByApprovalInstanceId(approvalInstanceId))
                .thenReturn(Collections.singletonList(candidateEntity));

        List<Role> actualRoles = approvalPermissionService.getCandidateRoles(approvalInstanceId, true);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        Set<Long> approvableIds = new HashSet<>(Collections.singletonList(1L));

        when(userServiceMock.getApprovableApprovalInstances())
                .thenReturn(Collections.singletonList(new UserTaskInstanceEntity(1L)));

        assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
        assertFalse(approvalPermissionService.isApprovable(2L));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> expectedEntities = Collections.singletonList(new UserTaskInstanceEntity(1L));

        when(userServiceMock.getApprovableApprovalInstances())
                .thenReturn(Collections.singletonList(new UserTaskInstanceEntity(1L)));

        List<UserTaskInstanceEntity> actualEntities = approvalPermissionService.getApprovableApprovalInstances();
        assertEquals(expectedEntities, actualEntities);
    }
}
