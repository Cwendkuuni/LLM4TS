import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;

public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        userTaskInstances.add(new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.OPEN));
        userTaskInstances.add(new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.IN_PROGRESS));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.OPEN);
        assertNotNull(result);
        assertEquals(userTaskInstances.size(), result.size());
        assertTrue(result.containsAll(userTaskInstances));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertNotNull(result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertNotNull(result);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertNotNull(result);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.OPEN);

        int result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "task1";

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.OPEN;

        int result = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "activity1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "task1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.OPEN;

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertNotNull(result);
    }

}