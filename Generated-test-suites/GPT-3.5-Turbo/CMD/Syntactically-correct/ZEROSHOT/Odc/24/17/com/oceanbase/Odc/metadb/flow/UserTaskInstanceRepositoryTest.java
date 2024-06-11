import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.query.Param;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userList = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertNotNull(userList);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userList = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));
        assertNotNull(userList);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userList = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("role1"));
        assertNotNull(userList);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userList = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("role1"));
        assertNotNull(userList);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userList = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(userList);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userList = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(userList);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTask1");
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TYPE1, "activity1", 1L);
        assertTrue(entity.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TYPE1, "name1", 1L);
        assertTrue(entity.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertNotNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userList = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED);
        assertNotNull(userList);
    }
}