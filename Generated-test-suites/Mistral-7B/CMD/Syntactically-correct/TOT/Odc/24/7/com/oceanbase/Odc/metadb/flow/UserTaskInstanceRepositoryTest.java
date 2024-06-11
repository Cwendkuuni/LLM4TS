import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Given
        Specification<UserTaskInstanceEntity> spec = Mockito.mock(Specification.class);
        Mockito.when(userTaskInstanceRepository.findAll(spec)).thenReturn(Mockito.mock(Page.class));
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        // When
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);

        // Then
        Assertions.assertNotNull(result);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByStatus(FlowNodeStatus.PENDING);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Given
        Long userId = 1L;
        Collection<Long> roleIds = List.of(1L, 2L);

        // When
        Executable execution = () -> userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        // Then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        // Given
        String resourceRoleIdentifier = "testResourceRoleIdentifier";
        Collection<String> resourceRoleIdentifiers = List.of(resourceRoleIdentifier);

        // When
        Executable execution = () -> userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        // Then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
    }

    @Test
    public void testUpdate() {
        // Given
        Long id = 1L;
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();

        // When
        Executable execution = () -> userTaskInstanceRepository.update(entity);

        // Then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).update(entity);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Given
        FlowNodeType instanceType = FlowNodeType.SEQUENTIAL_TASK;
        String activityId = "testActivityId";
        Long flowInstanceId = 1L;

        // When
        Executable execution = () -> userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        // Then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;

        // When
        Executable execution = () -> userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        // Then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testFindConfirmById() {
        // Given
        Long id = 1L;

        // When
        Executable execution = () -> userTaskInstanceRepository.findConfirmById(id);

        // Then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findConfirmById(id);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Given
        Collection<Long> flowInstanceIds = List.of(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        // When
        Executable execution = () -> userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        // Then
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
    }
}