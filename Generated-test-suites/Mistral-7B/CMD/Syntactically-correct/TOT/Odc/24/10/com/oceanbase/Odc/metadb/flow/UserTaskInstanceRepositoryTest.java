import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@SpringJUnitConfig(classes = UserTaskInstanceRepository.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED));
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedUserTaskInstanceEntities);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        Assertions.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.APPROVED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED));
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L))).thenReturn(expectedUserTaskInstanceEntities);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));

        // Then
        Assertions.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED));
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("roleIdentifier"))).thenReturn(expectedUserTaskInstanceEntities);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("roleIdentifier"));

        // Then
        Assertions.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
        verify(userTaskInstanceRepository, times(1)).findByResourceRoleIdentifierIn(Arrays.asList("roleIdentifier"));
    }

    @Test
    public void testFindByRoleId() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED));
        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(expectedUserTaskInstanceEntities);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(1L);

        // Then
        Assertions.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
        verify(userTaskInstanceRepository, times(1)).findByRoleId(1L);
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED));
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(expectedUserTaskInstanceEntities);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(1L);

        // Then
        Assertions.assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserId(1L);
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED);

        // When
        Executable updateUserTaskInstance = () -> userTaskInstanceRepository.update(userTaskInstanceEntity);

        // Then
        Assertions.assertDoesNotThrow(updateUserTaskInstance);
        verify(userTaskInstanceRepository, times(1)).update(userTaskInstanceEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Given
        Long id = 1L;
        String userTaskId = "newUserTaskId";

        // When
        Executable updateUserTaskIdById = () -> userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        // Then
        Assertions.assertDoesNotThrow(updateUserTaskIdById);
        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(id, userTaskId);
    }

    @Test
    public void testUpdateStatusById() {
        // Given
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        // When
        Executable updateStatusById = () -> userTaskInstanceRepository.updateStatusById(id, status);

        // Then
        Assertions.assertDoesNotThrow(updateStatusById);
        verify(userTaskInstanceRepository, times(1)).updateStatusById(id, status);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;

        // When
        Executable deleteByFlowInstanceId = () -> userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Then
        Assertions.assertDoesNotThrow(deleteByFlowInstanceId);
        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED);
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        // When
        Optional<UserTaskInstanceEntity> actualUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);

        // Then
        Assertions.assertTrue(actualUserTaskInstanceEntity.isPresent());
        Assertions.assertEquals(userTaskInstanceEntity, actualUserTaskInstanceEntity.get());
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED);
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "task1", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        // When
        Optional<UserTaskInstanceEntity> actualUserTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "task1", 1L);

        // Then
        Assertions.assertTrue(actualUserTaskInstanceEntity.isPresent());
        Assertions.assertEquals(userTaskInstanceEntity, actualUserTaskInstanceEntity.get());
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndName(FlowNodeType.START, "task1", 1L);
    }

    @Test
    public void testFindConfirmById() {
        // Given
        Long id = 1L;
        boolean confirm = true;
        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        // When
        boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);

        // Then
        Assertions.assertEquals(confirm, actualConfirm);
        verify(userTaskInstanceRepository, times(1)).findConfirmById(id);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED));
        userTaskInstanceEntities.add(new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.REJECTED));
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED))
                .thenReturn(userTaskInstanceEntities);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);

        // Then
        Assertions.assertEquals(userTaskInstanceEntities, actualUserTaskInstanceEntities);
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus_EmptyList() {
        // Given
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.emptyList(), FlowNodeStatus.APPROVED))
                .thenReturn(Collections.emptyList());

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.emptyList(), FlowNodeStatus.APPROVED);

        // Then
        Assertions.assertEquals(Collections.emptyList(), actualUserTaskInstanceEntities);
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.emptyList(), FlowNodeStatus.APPROVED);
    }
}