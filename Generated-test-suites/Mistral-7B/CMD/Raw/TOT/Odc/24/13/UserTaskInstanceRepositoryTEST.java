import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        Assertions.assertFalse(userTaskInstances.isEmpty());

        List<UserTaskInstanceEntity> userTaskInstancesRejected = userTaskInstanceRepository.findByStatus(FlowNodeStatus.REJECTED);
        Assertions.assertFalse(userTaskInstancesRejected.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        String resourceRoleIdentifier = "admin";
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList(resourceRoleIdentifier));
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(roleId);
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserId(userId);
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(id);
        UserTaskInstanceEntity updatedEntity = userTaskInstanceRepository.update(entity);
        Assertions.assertNotNull(updatedEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "12345";
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        Assertions.assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
        Assertions.assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        Assertions.assertEquals(1, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "task1";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "task1";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
        Assertions.assertFalse(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        flowInstanceIds.add(2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }
}