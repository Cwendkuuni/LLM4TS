import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.UserTaskInstanceRepository;

@SpringBootTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = List.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedUserTaskInstances);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        // Then
        Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = List.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, List.of(1L))).thenReturn(expectedUserTaskInstances);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, List.of(1L));

        // Then
        Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = List.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(List.of("roleIdentifier"))).thenReturn(expectedUserTaskInstances);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(List.of("roleIdentifier"));

        // Then
        Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByRoleId() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = List.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(expectedUserTaskInstances);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByRoleId(1L);

        // Then
        Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserId() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = List.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(expectedUserTaskInstances);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserId(1L);

        // Then
        Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstance));

        // When
        Executable updateMethod = () -> userTaskInstanceRepository.update(userTaskInstance);

        // Then
        Assertions.assertDoesNotThrow(updateMethod::execute);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        int expectedNumberOfDeletedEntities = 1;

        // When
        int actualNumberOfDeletedEntities = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);

        // Then
        Assertions.assertEquals(expectedNumberOfDeletedEntities, actualNumberOfDeletedEntities);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L))
                .thenReturn(Optional.of(expectedUserTaskInstance));

        // When
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.START, "activityId", 1L);

        // Then
        Assertions.assertEquals(expectedUserTaskInstance, actualUserTaskInstance.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        UserTaskInstanceEntity expectedUserTaskInstance = new UserTaskInstanceEntity();
        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L))
                .thenReturn(Optional.of(expectedUserTaskInstance));

        // When
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.START, "name", 1L);

        // Then
        Assertions.assertEquals(expectedUserTaskInstance, actualUserTaskInstance.get());
    }

    @Test
    public void testFindConfirmById() {
        // Given
        boolean expectedConfirm = true;
        Mockito.when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(expectedConfirm);

        // When
        boolean actualConfirm = userTaskInstanceRepository.findConfirmById(1L);

        // Then
        Assertions.assertEquals(expectedConfirm, actualConfirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = List.of(new UserTaskInstanceEntity());
        Mockito.when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(List.of(1L), FlowNodeStatus.APPROVED))
                .thenReturn(expectedUserTaskInstances);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                List.of(1L), FlowNodeStatus.APPROVED);

        // Then
        Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }
}