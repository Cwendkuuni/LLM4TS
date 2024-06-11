import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Autowired
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = Collections.singletonList(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        Assertions.assertEquals(expected, actual);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.APPROVED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = Collections.singletonList(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singleton(1L)))
                .thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Collections.singleton(1L));

        Assertions.assertEquals(expected, actual);
        verify(userTaskInstanceRepository, times(1))
                .findByCandidateUserIdOrRoleIds(1L, Collections.singleton(1L));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> expected = Collections.singletonList(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singleton("testRole")))
                .thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Collections.singleton("testRole"));

        Assertions.assertEquals(expected, actual);
        verify(userTaskInstanceRepository, times(1))
                .findByResourceRoleIdentifierIn(Collections.singleton("testRole"));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> expected = Collections.singletonList(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(1L);

        Assertions.assertEquals(expected, actual);
        verify(userTaskInstanceRepository, times(1)).findByRoleId(1L);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> expected = Collections.singletonList(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(1L);

        Assertions.assertEquals(expected, actual);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserId(1L);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.save(userTaskInstanceEntity)).thenReturn(updatedEntity);

        int actual = userTaskInstanceRepository.update(userTaskInstanceEntity);

        Assertions.assertEquals(1, actual);
        verify(userTaskInstanceRepository, times(1)).save(userTaskInstanceEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int actual = userTaskInstanceRepository.updateUserTaskIdById(1L, "testUserTaskId");

        Assertions.assertEquals(1, actual);
        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(1L, "testUserTaskId");
    }

    @Test
    public void testUpdateStatusById() {
        int actual = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);

        Assertions.assertEquals(1, actual);
        verify(userTaskInstanceRepository, times(1)).updateStatusById(1L, FlowNodeStatus.APPROVED);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int actual = userTaskInstanceRepository.deleteByFlowInstanceId(1L);

        Assertions.assertEquals(1, actual);
        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity expected = userTaskInstanceEntity;
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SEQUENTIAL, "testActivityId", 1L))
                .thenReturn(Optional.of(expected));

        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(FlowNodeType.SEQUENTIAL, "testActivityId", 1L);

        Assertions.assertEquals(expected, actual.get());
        verify(userTaskInstanceRepository, times(1))
                .findByInstanceTypeAndActivityId(FlowNodeType.SEQUENTIAL, "testActivityId", 1L);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity expected = userTaskInstanceEntity;
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.SEQUENTIAL, "testName", 1L))
                .thenReturn(Optional.of(expected));

        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository
                .findByInstanceTypeAndName(FlowNodeType.SEQUENTIAL, "testName", 1L);

        Assertions.assertEquals(expected, actual.get());
        verify(userTaskInstanceRepository, times(1))
                .findByInstanceTypeAndName(FlowNodeType.SEQUENTIAL, "testName", 1L);
    }

    @Test
    public void testFindConfirmById() {
        boolean expected = true;
        when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(expected);

        boolean actual = userTaskInstanceRepository.findConfirmById(1L);

        Assertions.assertEquals(expected, actual);
        verify(userTaskInstanceRepository, times(1)).findConfirmById(1L);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> expected = Collections.singletonList(userTaskInstanceEntity);
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Collections.singletonList(1L), FlowNodeStatus.APPROVED)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED);

        Assertions.assertEquals(expected, actual);
        verify(userTaskInstanceRepository, times(1))
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED);
    }
}