import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setUp() {
        approvalPermissionService = new ApprovalPermissionService();
    }

    @Test
    public void testGetCandidates() {
        Set<User> expectedCandidates = new HashSet<>(Arrays.asList(new User(1L, "User1"), new User(2L, "User2")));
        Set<User> actualCandidates = approvalPermissionService.getCandidates(1L, true);
        assertEquals(expectedCandidates, actualCandidates);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Set<Long> approvalInstanceIds = new HashSet<>(Collections.singletonList(1L));
        Long expectedRoleId = 100L;
        Set<Long> actualRoleIds = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds).get(1L);
        assertEquals(expectedRoleId, actualRoleIds.iterator().next());
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> expectedUserIds = new HashSet<>(Collections.singletonList(1L));
        Set<Long> actualUserIds = approvalPermissionService.getCandidateUserIds(Collections.singletonList(1L));
        assertEquals(expectedUserIds, actualUserIds);
    }

    @Test
    public void testIsApprovable() {
        assertTrue(approvalPermissionService.isApprovable(1L));
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> expectedRoles = Arrays.asList(new Role(1L, "Role1"), new Role(2L, "Role2"));
        List<Role> actualRoles = approvalPermissionService.getCandidateRoles(1L, true);
        assertEquals(expectedRoles, actualRoles);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> expectedInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, "Instance1"), new UserTaskInstanceEntity(2L, "Instance2")
        );
        List<UserTaskInstanceEntity> actualInstances = approvalPermissionService.getApprovableApprovalInstances();
        assertEquals(expectedInstances, actualInstances);
    }

    @Test
    public void testGetApprovableApprovalInstancesWithUserId() {
        List<UserTaskInstanceEntity> expectedInstances = Collections.singletonList(new UserTaskInstanceEntity(1L, "Instance1"));
        List<UserTaskInstanceEntity> actualInstances = approvalPermissionService.getApprovableApprovalInstances(1L);
        assertEquals(expectedInstances, actualInstances);
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> expectedInstances = Arrays.asList(
                new UserTaskInstanceEntity(1L, "External1"), new UserTaskInstanceEntity(2L, "External2")
        );
        List<UserTaskInstanceEntity> actualInstances = approvalPermissionService.listApprovableExternalInstances();
        assertEquals(expectedInstances, actualInstances);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Set<UserEntity> expectedUsers = new HashSet<>(Collections.singletonList(new UserEntity(1L, "User1")));
        Set<UserEntity> actualUsers = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singletonList(1L)).get(1L);
        assertEquals(expectedUsers, actualUsers);
    }
}