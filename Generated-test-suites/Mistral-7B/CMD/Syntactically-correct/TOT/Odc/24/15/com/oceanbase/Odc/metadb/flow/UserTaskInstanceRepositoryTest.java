import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@SpringJUnitConfig
@SpringBootTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        Assertions.assertNotNull(userTaskInstanceEntities);
        Assertions.assertEquals(1, userTaskInstanceEntities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = List.of(1L, 2L);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        Assertions.assertNotNull(userTaskInstanceEntities);
        Assertions.assertEquals(2, userTaskInstanceEntities.size());
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        Collection<String> resourceRoleIdentifiers = List.of("role1", "role2");
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        Assertions.assertNotNull(userTaskInstanceEntities);
        Assertions.assertEquals(2, userTaskInstanceEntities.size());
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);
        Assertions.assertNotNull(userTaskInstanceEntities);
        Assertions.assertEquals(1, userTaskInstanceEntities.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);
        Assertions.assertNotNull(userTaskInstanceEntities);
        Assertions.assertEquals(1, userTaskInstanceEntities.size());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testUpdate(boolean isUpdate) {
        Long id = 1L;
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(id);

        if (isUpdate) {
            userTaskInstanceEntity.setUserTaskId("testUserTaskId");
            userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
            userTaskInstanceEntity.setApproved(true);
            userTaskInstanceEntity.setOperatorId("testOperatorId");
            userTaskInstanceEntity.setComment("testComment");
            userTaskInstanceEntity.setExpireIntervalSeconds(120);
            userTaskInstanceEntity.setExternalFlowInstanceId("testExternalFlowInstanceId");

            int result = userTaskInstanceRepository.update(userTaskInstanceEntity);
            Assertions.assertEquals(1, result);
        } else {
            int result = userTaskInstanceRepository.update(Mockito.any(UserTaskInstanceEntity.class));
            Assertions.assertEquals(1, result);
        }

        UserTaskInstanceEntity entityFromDb = userTaskInstanceRepository.findById(id).get();
        if (isUpdate) {
            Assertions.assertEquals("testUserTaskId", entityFromDb.getUserTaskId());
            Assertions.assertEquals(FlowNodeStatus.APPROVED, entityFromDb.getStatus());
            Assertions.assertEquals(true, entityFromDb.isApproved());
            Assertions.assertEquals("testOperatorId", entityFromDb.getOperatorId());
            Assertions.assertEquals("testComment", entityFromDb.getComment());
            Assertions.assertEquals(120, entityFromDb.getExpireIntervalSeconds());
            Assertions.assertEquals("testExternalFlowInstanceId", entityFromDb.getExternalFlowInstanceId());
        } else {
            Assertions.assertNull(entityFromDb);
        }
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        Assertions.assertEquals(1, result);

        UserTaskInstanceEntity entityFromDb = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "testActivityId", instanceId);
        Assertions.assertNull(entityFromDb);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setFlowInstanceId(1L);
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("testUserTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId("testOperatorId");
        userTaskInstanceEntity.setComment("testComment");
        userTaskInstanceEntity.setExpireIntervalSeconds(120);
        userTaskInstanceEntity.setExternalFlowInstanceId("testExternalFlowInstanceId");

        Mockito.when(userTaskInstanceRepository.save(Mockito.any(UserTaskInstanceEntity.class)))
                .thenReturn(userTaskInstanceEntity);

        UserTaskInstanceEntity entityFromDb = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "testActivityId", 1L);
        Assertions.assertEquals(userTaskInstanceEntity, entityFromDb);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setFlowInstanceId(1L);
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("testUserTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId("testOperatorId");
        userTaskInstanceEntity.setComment("testComment");
        userTaskInstanceEntity.setExpireIntervalSeconds(120);
        userTaskInstanceEntity.setExternalFlowInstanceId("testExternalFlowInstanceId");
        userTaskInstanceEntity.setName("testName");

        Mockito.when(userTaskInstanceRepository.save(Mockito.any(UserTaskInstanceEntity.class)))
                .thenReturn(userTaskInstanceEntity);

        UserTaskInstanceEntity entityFromDb = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "testName", 1L);
        Assertions.assertEquals(userTaskInstanceEntity, entityFromDb);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
        Assertions.assertEquals(false, confirm);

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(id);
        userTaskInstanceEntity.setConfirm(true);

        Mockito.when(userTaskInstanceRepository.save(Mockito.any(UserTaskInstanceEntity.class)))
                .thenReturn(userTaskInstanceEntity);

        Boolean confirmFromDb = userTaskInstanceRepository.findConfirmById(id);
        Assertions.assertEquals(true, confirmFromDb);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        List<UserTaskInstanceEntity> userTaskInstanceEntities = List.of(
                new UserTaskInstanceEntity(1L, flowInstanceId, FlowNodeStatus.APPROVED),
                new UserTaskInstanceEntity(2L, flowInstanceId, FlowNodeStatus.REJECTED),
                new UserTaskInstanceEntity(3L, flowInstanceId, FlowNodeStatus.APPROVED)
        );

        Mockito.when(userTaskInstanceRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(Page.of(userTaskInstanceEntities, Pageable.ofSize(10)));

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, FlowNodeStatus.APPROVED);
        Assertions.assertEquals(2, entities.size());
    }
}