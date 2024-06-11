
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import com.oceanbase.odc.metadb.flow.*;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    UserTaskInstanceEntity userTaskInstanceEntity;
    
    @Mock
    FlowNodeStatus flowNodeStatus;
    
    @Mock
    FlowNodeType flowNodeType;

    @Test
    public void testFindByStatus() {
        userTaskInstanceRepository.findByStatus(flowNodeStatus);
        verify(userTaskInstanceRepository, times(1)).findByStatus(flowNodeStatus);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(123L, Arrays.asList(456L, 789L));
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(123L, Arrays.asList(456L, 789L));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(123L, Arrays.asList("role1", "role2"));
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrResourceRoleIdentifier(123L, Arrays.asList("role1", "role2"));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        verify(userTaskInstanceRepository, times(1)).findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
    }

    @Test
    public void testFindByRoleId() {
        userTaskInstanceRepository.findByRoleId(123L);
        verify(userTaskInstanceRepository, times(1)).findByRoleId(123L);
    }

    @Test
    public void testFindByCandidateUserId() {
        userTaskInstanceRepository.findByCandidateUserId(123L);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserId(123L);
    }

    @Test
    public void testUpdate() {
        userTaskInstanceRepository.update(userTaskInstanceEntity);
        verify(userTaskInstanceRepository, times(1)).update(userTaskInstanceEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        userTaskInstanceRepository.updateUserTaskIdById(123L, "userTaskId");
        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(123L, "userTaskId");
    }

    @Test
    public void testUpdateStatusById() {
        userTaskInstanceRepository.updateStatusById(123L, flowNodeStatus);
        verify(userTaskInstanceRepository, times(1)).updateStatusById(123L, flowNodeStatus);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        userTaskInstanceRepository.deleteByFlowInstanceId(123L);
        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(123L);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        userTaskInstanceRepository.findByInstanceTypeAndActivityId(flowNodeType, "activityId", 123L);
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(flowNodeType, "activityId", 123L);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        userTaskInstanceRepository.findByInstanceTypeAndName(flowNodeType, "name", 123L);
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndName(flowNodeType, "name", 123L);
    }

    @Test
    public void testFindConfirmById() {
        userTaskInstanceRepository.findConfirmById(123L);
        verify(userTaskInstanceRepository, times(1)).findConfirmById(123L);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(123L, 456L), flowNodeStatus);
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(123L, 456L), flowNodeStatus);
    }
}
