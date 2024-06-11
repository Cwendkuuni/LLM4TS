import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                List.of(1L, 2L));
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                List.of("role1", "role2"));
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(1L);
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserId(1L);
        Assertions.assertFalse(userTaskInstances.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testUpdate(boolean success) {
        long id = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(id);

        if (success) {
            int updatedRows = userTaskInstanceRepository.update(entity);
            Assertions.assertEquals(1, updatedRows);
        } else {
            int updatedRows = userTaskInstanceRepository.update(entity);
            Assertions.assertEquals(0, updatedRows);
        }
    }

    @Test
    public void testUpdateUserTaskIdById() {
        long id = 1L;
        String userTaskId = "testUserTaskId";
        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        Assertions.assertEquals(1, updatedRows);
    }

    @Test
    public void testUpdateStatusById() {
        long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        int updatedRows = userTaskInstanceRepository.updateStatusById(id, status);
        Assertions.assertEquals(1, updatedRows);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        long instanceId = 1L;
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        Assertions.assertEquals(1, deletedRows);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "name";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository
                .findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        Assertions.assertTrue(userTaskInstanceEntityOptional.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        long id = 1L;
        boolean confirm = userTaskInstanceRepository.findConfirmById(id);
        Assertions.assertTrue(confirm); // Assuming all records have confirm set to true
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus(boolean success) {
        List<Long> flowInstanceIds = List.of(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        if (success) {
            List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository
                    .findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
            Assertions.assertFalse(userTaskInstances.isEmpty());
        } else {
            List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository
                    .findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
            Assertions.assertTrue(userTaskInstances.isEmpty());
        }
    }
}