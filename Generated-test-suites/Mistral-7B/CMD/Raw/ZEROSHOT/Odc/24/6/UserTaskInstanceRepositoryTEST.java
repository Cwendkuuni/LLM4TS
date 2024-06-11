import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        Assertions.assertNotNull(userTaskInstances);
        Assertions.assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
        Assertions.assertNotNull(userTaskInstances);
        Assertions.assertEquals(1, userTaskInstances.size());

        List<UserTaskInstanceEntity> userTaskInstances2 = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Arrays.asList(1L));
        Assertions.assertNotNull(userTaskInstances2);
        Assertions.assertEquals(1, userTaskInstances2.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, null);
        Assertions.assertNotNull(userTaskInstances);
        Assertions.assertEquals(1, userTaskInstances.size());

        List<UserTaskInstanceEntity> userTaskInstances2 = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, Arrays.asList("role1"));
        Assertions.assertNotNull(userTaskInstances2);
        Assertions.assertEquals(1, userTaskInstances2.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));
        Assertions.assertNotNull(userTaskInstances);
        Assertions.assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(1L);
        Assertions.assertNotNull(userTaskInstances);
        Assertions.assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserId(1L);
        Assertions.assertNotNull(userTaskInstances);
        Assertions.assertEquals(1, userTaskInstances.size());
    }

    @Rollback(false)
    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setUserTaskId("testUserTaskId");
        userTaskInstance.setStatus(FlowNodeStatus.CREATED);

        int rowsAffected = userTaskInstanceRepository.update(userTaskInstance);
        Assertions.assertEquals(1, rowsAffected);

        UserTaskInstanceEntity updatedUserTaskInstance = userTaskInstanceRepository.findById(userTaskInstance.getId()).get();
        Assertions.assertEquals("testUserTaskId", updatedUserTaskInstance.getUserTaskId());
        Assertions.assertEquals(FlowNodeStatus.APPROVED, updatedUserTaskInstance.getStatus());
    }

    @Rollback(false)
    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testUserTaskId";

        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        Assertions.assertEquals(1, rowsAffected);

        UserTaskInstanceEntity userTaskInstance = userTaskInstanceRepository.findById(id).get();
        Assertions.assertEquals(userTaskId, userTaskInstance.getUserTaskId());
    }

    @Rollback(false)
    @Test
    @Transactional
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
        Assertions.assertEquals(1, rowsAffected);

        UserTaskInstanceEntity userTaskInstance = userTaskInstanceRepository.findById(id).get();
        Assertions.assertEquals(status, userTaskInstance.getStatus());
    }

    @Rollback(false)
    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        Assertions.assertEquals(1, rowsAffected);

        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "testActivityId", instanceId);
        Assertions.assertFalse(userTaskInstanceOptional.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "testActivityId", 1L);
        Assertions.assertFalse(userTaskInstanceOptional.isPresent());

        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setFlowInstanceId(1L);
        userTaskInstance.setId(1L);
        userTaskInstance.setUserTaskId("testUserTaskId");
        userTaskInstance.setStatus(FlowNodeStatus.CREATED);
        userTaskInstanceRepository.save(userTaskInstance);

        Optional<UserTaskInstanceEntity> userTaskInstanceOptional2 = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "testActivityId", 1L);
        Assertions.assertTrue(userTaskInstanceOptional2.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setFlowInstanceId(1L);
        userTaskInstance.setId(1L);
        userTaskInstance.setUserTaskId("testUserTaskId");
        userTaskInstance.setStatus(FlowNodeStatus.CREATED);
        userTaskInstance.setName("testName");
        userTaskInstanceRepository.save(userTaskInstance);

        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "testName", 1L);
        Assertions.assertTrue(userTaskInstanceOptional.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
        Assertions.assertFalse(confirm);

        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setConfirm(true);
        userTaskInstanceRepository.save(userTaskInstance);

        Boolean confirm2 = userTaskInstanceRepository.findConfirmById(id);
        Assertions.assertTrue(confirm2);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        Assertions.assertNotNull(userTaskInstances);
        Assertions.assertEquals(2, userTaskInstances.size());

        List<UserTaskInstanceEntity> userTaskInstances2 = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L, 3L), FlowNodeStatus.APPROVED);
        Assertions.assertNotNull(userTaskInstances2);
        Assertions.assertEquals(2, userTaskInstances2.size());
    }
}