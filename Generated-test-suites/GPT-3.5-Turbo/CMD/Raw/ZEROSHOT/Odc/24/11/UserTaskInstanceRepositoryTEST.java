import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(userTaskInstances);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(2L, 3L);
        List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(userTaskInstances);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // Add similar test methods for other query methods

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("taskId");
        entity.setStatus(FlowNodeStatus.COMPLETED);
        entity.setApproved(true);
        entity.setOperatorId(2L);
        entity.setComment("Test comment");
        entity.setExpireIntervalSeconds(30);
        entity.setExternalFlowInstanceId("extInstanceId");

        int updatedRows = userTaskInstanceRepository.update(entity);

        assertEquals(1, updatedRows);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, deletedRows);
    }

    // Add similar test methods for other update and delete methods

}