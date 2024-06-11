import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.Test;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalService;
    private UserTaskInstanceCandidateRepository mockUserTaskInstanceCandidateRepository;
    private UserRepository mockUserRepository;

    public void setUp() {
        approvalService = new ApprovalPermissionService();

        // Mocking dependencies
        mockUserTaskInstanceCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
        mockUserRepository = mock(UserRepository.class);

        approvalService.setUserTaskCandidateRepository(mockUserTaskInstanceCandidateRepository);
        approvalService.setUserRepository(mockUserRepository);
    }

    @Test
    public void testGetCandidates() {
        // Prepare test data
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        // Mocking behavior
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity(1L, 1L, null));
        entities.add(new UserTaskInstanceCandidateEntity(2L, null, 2L));

        when(mockUserTaskInstanceCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
            .thenReturn(entities);

        // Call the method to test
        Set<User> result = approvalService.getCandidates(approvalInstanceId, enabled);

        // Verification
        assertEquals(1, result.size());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        // Prepare test data
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(1L);

        // Mocking behavior
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity(1L, 1L, 1L));

        when(mockUserTaskInstanceCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
            .thenReturn(entities);

        // Call the method to test
        Map<Long, Set<Long>> result = approvalService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        // Verification
        assertEquals(1, result.size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        // Prepare test data
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(1L);

        // Mocking behavior
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity(1L, null, null, "identifier"));

        when(mockUserTaskInstanceCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
            .thenReturn(entities);

        // Call the method to test
        Map<Long, Set<String>> result = approvalService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

        // Verification
        assertEquals(1, result.size());
    }

    @Test
    public void testGetCandidateUserIds() {
        // Prepare test data
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(1L);

        // Mocking behavior
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity(1L, 1L, 1L, "identifier"));

        when(mockUserTaskInstanceCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
            .thenReturn(entities);

        // Call the method to test
        Set<Long> result = approvalService.getCandidateUserIds(approvalInstanceIds);

        // Verification
        assertEquals(1, result.size());
    }

    @Test
    public void testGetCandidateRoles() {
        // Prepare test data
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        // Mocking behavior
        List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceCandidateEntity(1L, null, 1L));

        when(mockUserTaskInstanceCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
            .thenReturn(entities);

        // Call the method to test
        List<Role> result = approvalService.getCandidateRoles(approvalInstanceId, enabled);

        // Verification
        assertEquals(1, result.size());
    }

    @Test
    public void testIsApprovable() {
        // Prepare test data
        Long approvalInstanceId = 1L;

        // Mocking behavior
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(new UserTaskInstanceEntity());

        when(mockUserTaskInstanceCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
            .thenReturn(userTaskInstanceEntities);

        // Call the method to test
        boolean result = approvalService.isApprovable(approvalInstanceId);

        // Verification
        assertEquals(true, result);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        // Mocking behavior
        when(mockUserRepository.currentUserId()).thenReturn(1L);

        // Call the method to test
        List<UserTaskInstanceEntity> result = approvalService.getApprovableApprovalInstances();

        // Verification
        assertEquals(0, result.size());
    }

}