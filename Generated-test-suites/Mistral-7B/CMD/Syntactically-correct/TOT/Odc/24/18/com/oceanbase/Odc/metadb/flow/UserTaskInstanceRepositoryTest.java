import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceDto;

@ExtendWith(MockitoExtension.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // given
        List<UserTaskInstanceEntity> expected = List.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        // then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // given
        List<UserTaskInstanceEntity> expected = List.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, List.of(1L))).thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, List.of(1L));

        // then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testUpdate() {
        // given
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        UserTaskInstanceDto dto = new UserTaskInstanceDto();

        // when
        int actual = userTaskInstanceRepository.update(entity);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).update(entity);

        // then
        Assertions.assertEquals(1, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // given
        long instanceId = 1L;

        // when
        int actual = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).deleteByFlowInstanceId(instanceId);

        // then
        Assertions.assertEquals(1, actual);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // given
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "activityId", 1L))
                .thenReturn(Optional.of(expected));

        // when
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.USER_TASK, "activityId", 1L);

        // then
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // given
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name", 1L))
                .thenReturn(Optional.of(expected));

        // when
        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.USER_TASK, "name", 1L);

        // then
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    public void testFindConfirmById() {
        // given
        boolean expected = true;
        Mockito.when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(expected);

        // when
        boolean actual = userTaskInstanceRepository.findConfirmById(1L);

        // then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // given
        List<UserTaskInstanceEntity> expected = List.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(List.of(1L), FlowNodeStatus.APPROVED))
                .thenReturn(expected);

        // when
        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                List.of(1L), FlowNodeStatus.APPROVED);

        // then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAll() {
        // given
        Page<UserTaskInstanceEntity> expected = Mockito.mock(Page.class);
        Pageable pageable = Mockito.mock(Pageable.class);

        Mockito.when(userTaskInstanceRepository.findAll(pageable)).thenReturn(expected);

        // when
        Page<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findAll(pageable);

        // then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findAll(pageable);
        Assertions.assertEquals(expected, actual);
    }
}