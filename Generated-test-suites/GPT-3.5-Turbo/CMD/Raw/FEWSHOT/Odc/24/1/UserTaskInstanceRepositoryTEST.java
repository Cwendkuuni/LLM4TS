import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;

public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("role"));
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("role"));
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("task1");
        entity.setStatus(FlowNodeStatus.COMPLETED);
        entity.setApproved(true);
        entity.setOperatorId(1L);
        entity.setComment("comment");
        entity.setExpireIntervalSeconds(60);
        entity.setExternalFlowInstanceId("external");
        
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(0, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "task1");
        assertEquals(0, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
        assertEquals(0, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.NODE, "activity", 1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.NODE, "name", 1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.COMPLETED);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}