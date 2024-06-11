import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private UserResourceRoleRepository userResourceRoleRepository;

    @Before
    public void setup() {
        approvalPermissionService = new ApprovalPermissionService();
        userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRoleRepository = mock(UserRoleRepository.class);
        userResourceRoleRepository = mock(UserResourceRoleRepository.class);

        approvalPermissionService.setUserTaskCandidateRepository(userTaskCandidateRepository);
        approvalPermissionService.setUserTaskInstanceRepository(userTaskInstanceRepository);
        approvalPermissionService.setUserRepository(userRepository);
        approvalPermissionService.setRoleRepository(roleRepository);
        approvalPermissionService.setUserRoleRepository(userRoleRepository);
        approvalPermissionService.setUserResourceRoleRepository(userResourceRoleRepository);
    }

    @Test
    public void testGetCandidates() {
        // Mock data
        Long approvalInstanceId = 1L;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity(1L, null, 1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity(2L, 2L, null);
        UserTaskInstanceCandidateEntity entity3 = new UserTaskInstanceCandidateEntity(3L, 3L, 3L);
        List<UserTaskInstanceCandidateEntity> entities = Arrays.asList(entity1, entity2, entity3);

        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(entities);

        // Test
        Set<User> candidates = approvalPermissionService.getCandidates(approvalInstanceId, true);
        assertEquals(2, candidates.size());
    }

    @Test
    public void testIsApprovable() {
        // Mock data
        Long approvalInstanceId = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity(1L, FlowNodeStatus.EXECUTING);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Collections.singletonList(entity);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(userTaskInstanceEntities);

        // Test
        boolean isApprovable = approvalPermissionService.isApprovable(approvalInstanceId);
        assertTrue(isApprovable);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        // Mock data
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity(1L, FlowNodeStatus.EXECUTING);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Collections.singletonList(entity);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(userTaskInstanceEntities);

        // Test
        List<UserTaskInstanceEntity> approvableInstances = approvalPermissionService.getApprovableApprovalInstances();
        assertEquals(1, approvableInstances.size());
    }

    @Test
    public void testListApprovableExternalInstances() {
        // Mock data
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity(1L, FlowNodeStatus.EXECUTING, "id1", "flow1");
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity(2L, FlowNodeStatus.EXECUTING, "id2", "flow2");
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(entity1, entity2);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(userTaskInstanceEntities);

        // Test
        List<UserTaskInstanceEntity> externalInstances = approvalPermissionService.listApprovableExternalInstances();
        assertEquals(2, externalInstances.size());
    }

    // Add more test methods for other functions

}