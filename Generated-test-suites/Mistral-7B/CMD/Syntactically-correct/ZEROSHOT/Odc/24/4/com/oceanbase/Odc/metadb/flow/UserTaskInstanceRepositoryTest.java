import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        MockitoAnnotations.when(userTaskInstanceRepository).findByStatus(FlowNodeStatus.APPROVED);
        Assertions.assertEquals(userTaskInstances, userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        MockitoAnnotations.when(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        Assertions.assertEquals(userTaskInstances, userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        MockitoAnnotations.when(userTaskInstanceRepository).findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        Assertions.assertEquals(userTaskInstances, userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2")));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        MockitoAnnotations.when(userTaskInstanceRepository).findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        Assertions.assertEquals(userTaskInstances, userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2")));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        MockitoAnnotations.when(userTaskInstanceRepository).findByRoleId(1L);
        Assertions.assertEquals(userTaskInstances, userTaskInstanceRepository.findByRoleId(1L));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        MockitoAnnotations.when(userTaskInstanceRepository).findByCandidateUserId(1L);
        Assertions.assertEquals(userTaskInstances, userTaskInstanceRepository.findByCandidateUserId(1L));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        int updatedRows = userTaskInstanceRepository.update(userTaskInstanceEntity);
        MockitoAnnotations.verify(userTaskInstanceRepository, MockitoAnnotations.times(1))
                .update(MockitoAnnotations.argThat(entity -> entity.getId().equals(userTaskInstanceEntity.getId())));
        Assertions.assertEquals(1, updatedRows);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "taskId";
        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        MockitoAnnotations.verify(userTaskInstanceRepository, MockitoAnnotations.times(1))
                .updateUserTaskIdById(id, userTaskId);
        Assertions.assertEquals(1, updatedRows);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        int updatedRows = userTaskInstanceRepository.updateStatusById(id, status);
        MockitoAnnotations.verify(userTaskInstanceRepository, MockitoAnnotations.times(1))
                .updateStatusById(id, status);
        Assertions.assertEquals(1, updatedRows);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        MockitoAnnotations.verify(userTaskInstanceRepository, MockitoAnnotations.times(1))
                .deleteByFlowInstanceId(instanceId);
        Assertions.assertEquals(1, deletedRows);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = Optional.of(userTaskInstanceEntity);
        MockitoAnnotations.when(userTaskInstanceRepository).findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);
        Assertions.assertEquals(optionalUserTaskInstanceEntity, userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L));
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        Optional<UserTaskInstanceEntity> optionalUserTaskInstanceEntity = Optional.of(userTaskInstanceEntity);
        MockitoAnnotations.when(userTaskInstanceRepository).findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);
        Assertions.assertEquals(optionalUserTaskInstanceEntity, userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L));
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = true;
        MockitoAnnotations.when(userTaskInstanceRepository).findConfirmById(1L);
        Assertions.assertEquals(confirm, userTaskInstanceRepository.findConfirmById(1L));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        MockitoAnnotations.when(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        Assertions.assertEquals(userTaskInstances, userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED));
    }
}