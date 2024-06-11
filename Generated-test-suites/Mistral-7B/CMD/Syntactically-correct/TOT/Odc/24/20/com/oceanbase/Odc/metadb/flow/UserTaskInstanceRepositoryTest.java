import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@ExtendWith(MockitoExtension.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = Mockito.mockList(UserTaskInstanceEntity.class);
        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expectedUserTaskInstances);

        // When & Then
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = Mockito.mockList(UserTaskInstanceEntity.class);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.any()))
                .thenReturn(expectedUserTaskInstances);

        // When & Then
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
        Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        // Given
        List<UserTaskInstanceEntity> expectedUserTaskInstances = Mockito.mockList(UserTaskInstanceEntity.class);
        Mockito.when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Mockito.anyList()))
                .thenReturn(expectedUserTaskInstances);

        // When & Then
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(null);
        Assertions.assertEquals(expectedUserTaskInstances, actualUserTaskInstances);
    }

    @Test
    public void testUpdate() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = Mockito.mock(UserTaskInstanceEntity.class);
        Long id = 1L;
        Mockito.when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        // When
        userTaskInstanceRepository.update(userTaskInstanceEntity);

        // Then
        Mockito.verify(userTaskInstanceEntity).setUserTaskId("updatedUserTaskId");
        Mockito.verify(userTaskInstanceEntity).setStatus(FlowNodeStatus.APPROVED);
        Mockito.verify(userTaskInstanceEntity).setApproved(true);
        Mockito.verify(userTaskInstanceEntity).setOperatorId("updatedOperatorId");
        Mockito.verify(userTaskInstanceEntity).setComment("updatedComment");
        Mockito.verify(userTaskInstanceEntity).setExpireIntervalSeconds(120L);
        Mockito.verify(userTaskInstanceEntity).setExternalFlowInstanceId("updatedExternalFlowInstanceId");
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = Mockito.mock(UserTaskInstanceEntity.class);
        Long id = 1L;
        Mockito.when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        // When
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.TASK, "activityId", 1L);

        // Then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndActivityId(
                FlowNodeType.TASK, "activityId", 1L);
        Assertions.assertEquals(userTaskInstanceEntity, actualUserTaskInstance.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Given
        UserTaskInstanceEntity userTaskInstanceEntity = Mockito.mock(UserTaskInstanceEntity.class);
        Long id = 1L;
        Mockito.when(userTaskInstanceRepository.findById(id)).thenReturn(Optional.ofNullable(userTaskInstanceEntity));

        // When
        Optional<UserTaskInstanceEntity> actualUserTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.TASK, "name", 1L);

        // Then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndName(
                FlowNodeType.TASK, "name", 1L);
        Assertions.assertEquals(userTaskInstanceEntity, actualUserTaskInstance.get());
    }

    @Test
    public void testFindConfirmById() {
        // Given
        Boolean confirm = true;
        Long id = 1L;
        Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        // When
        Boolean actualConfirm = userTaskInstanceRepository.findConfirmById(id);

        // Then
        Assertions.assertEquals(confirm, actualConfirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Mockito.mockList(UserTaskInstanceEntity.class);
        Pageable pageable = Mockito.mock(Pageable.class);
        Specification<UserTaskInstanceEntity> specification = Mockito.mock(Specification.class);

        Mockito.when(userTaskInstanceRepository.findAll(specification, pageable)).thenReturn(Page.empty());
        Mockito.when(userTaskInstanceRepository.findByFlowInstanceIdAndStatus(1L, FlowNodeStatus.APPROVED)).thenReturn(userTaskInstanceEntities);

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                1L, FlowNodeStatus.APPROVED);

        // Then
        Assertions.assertEquals(userTaskInstanceEntities, actualUserTaskInstances);
    }
}