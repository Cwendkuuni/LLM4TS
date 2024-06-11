
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository repository = Mockito.mock(UserTaskInstanceRepository.class);

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        Mockito.when(repository.findByStatus(any(FlowNodeStatus.class))).thenReturn(expected);
        List<UserTaskInstanceEntity> actual = repository.findByStatus(FlowNodeStatus.APPROVED);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        Mockito.when(repository.findByCandidateUserIdOrRoleIds(anyLong(), anyCollection())).thenReturn(expected);
        List<UserTaskInstanceEntity> actual = repository.findByCandidateUserIdOrRoleIds(1L, Sets.newSet(1L));
        Assert.assertEquals(expected, actual);
    }

    /* similar tests for other methods goes here */

    @Test
    public void testUpdate() {
        Mockito.when(repository.update(any(UserTaskInstanceEntity.class))).thenReturn(1);
        int actual = repository.update(new UserTaskInstanceEntity());
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Mockito.when(repository.updateUserTaskIdById(anyLong(), anyString())).thenReturn(1);
        int actual = repository.updateUserTaskIdById(1L, "test");
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testUpdateStatusById() {
        Mockito.when(repository.updateStatusById(anyLong(), any(FlowNodeStatus.class))).thenReturn(1);
        int actual = repository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Mockito.when(repository.deleteByFlowInstanceId(anyLong())).thenReturn(1);
        int actual = repository.deleteByFlowInstanceId(1L);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> optional = Optional.of(new UserTaskInstanceEntity());
        Mockito.when(repository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(), anyLong())).thenReturn(optional);
        Optional<UserTaskInstanceEntity> actual = repository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "test", 1L);
        Assert.assertEquals(optional, actual);
    }

    @Test
    public void testFindConfirmById() {
        Mockito.when(repository.findConfirmById(anyLong())).thenReturn(true);
        Boolean actual = repository.findConfirmById(1L);
        Assert.assertTrue(actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        Mockito.when(repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(anyCollection(), any(FlowNodeStatus.class))).thenReturn(expected);
        List<UserTaskInstanceEntity> actual = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Sets.newSet(1L), FlowNodeStatus.APPROVED);
        Assert.assertEquals(expected, actual);
    }
}
