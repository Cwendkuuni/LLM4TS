import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@SpringJUnitConfig(classes = UserTaskInstanceRepositoryTest.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity testEntity;

    @BeforeEach
    public void setup() {
        testEntity = new UserTaskInstanceEntity();
        testEntity.setId(1L);
        testEntity.setUserTaskId("userTaskId");
        testEntity.setStatus(FlowNodeStatus.PENDING);
        testEntity.setApproved(false);
        testEntity.setOperatorId("operatorId");
        testEntity.setComment("comment");
        testEntity.setExpireIntervalSeconds(60);
        testEntity.setExternalFlowInstanceId("externalFlowInstanceId");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.PENDING);
        Assertions.assertEquals(userTaskInstanceEntities, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        Assertions.assertEquals(userTaskInstanceEntities, result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("roleIdentifier1", "roleIdentifier2");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        Assertions.assertEquals(userTaskInstanceEntities, result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Collection<String> resourceRoleIdentifiers = Arrays.asList("roleIdentifier1");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        Assertions.assertEquals(userTaskInstanceEntities, result);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Long roleId = 1L;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        Assertions.assertEquals(userTaskInstanceEntities, result);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Long userId = 1L;
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
        Assertions.assertEquals(userTaskInstanceEntities, result);
    }

    @Test
    public void testUpdate() {
        int rowsAffected = 1;
        Mockito.doReturn(rowsAffected).when(userTaskInstanceRepository).update(Mockito.any());

        UserTaskInstanceEntity entity = Mockito.spy(testEntity);
        userTaskInstanceRepository.update(entity);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).update(Mockito.any());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int rowsAffected = 1;
        Mockito.doReturn(rowsAffected).when(userTaskInstanceRepository).updateUserTaskIdById(Mockito.anyLong(), Mockito.anyString());

        Long id = 1L;
        String userTaskId = "newUserTaskId";
        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).updateUserTaskIdById(Mockito.eq(id), Mockito.eq(userTaskId));
    }

    @Test
    public void testUpdateStatusById() {
        int rowsAffected = 1;
        Mockito.doReturn(rowsAffected).when(userTaskInstanceRepository).updateStatusById(Mockito.anyLong(), Mockito.any(FlowNodeStatus.class));

        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        userTaskInstanceRepository.updateStatusById(id, status);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).updateStatusById(Mockito.eq(id), Mockito.eq(status));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = 1;
        Mockito.doReturn(rowsAffected).when(userTaskInstanceRepository).deleteByFlowInstanceId(Mockito.anyLong());

        Long instanceId = 1L;
        userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        Mockito.verify(userTaskInstanceRepository, Mockito.times(1)).deleteByFlowInstanceId(Mockito.eq(instanceId));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity result = new UserTaskInstanceEntity();
        result.setId(1L);
        result.setUserTaskId("userTaskId");
        result.setStatus(FlowNodeStatus.PENDING);
        result.setApproved(false);
        result.setOperatorId("operatorId");
        result.setComment("comment");
        result.setExpireIntervalSeconds(60);
        result.setExternalFlowInstanceId("externalFlowInstanceId");

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(Mockito.eq(FlowNodeType.APPROVAL), Mockito.anyString(), Mockito.eq(1L)))
                .thenReturn(Optional.of(result));

        Optional<UserTaskInstanceEntity> resultOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);
        Assertions.assertTrue(resultOptional.isPresent());
        Assertions.assertEquals(result, resultOptional.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity result = new UserTaskInstanceEntity();
        result.setId(1L);
        result.setUserTaskId("userTaskId");
        result.setStatus(FlowNodeStatus.PENDING);
        result.setApproved(false);
        result.setOperatorId("operatorId");
        result.setComment("comment");
        result.setExpireIntervalSeconds(60);
        result.setExternalFlowInstanceId("externalFlowInstanceId");

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(Mockito.eq(FlowNodeType.APPROVAL), Mockito.eq("name"), Mockito.eq(1L)))
                .thenReturn(Optional.of(result));

        Optional<UserTaskInstanceEntity> resultOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);
        Assertions.assertTrue(resultOptional.isPresent());
        Assertions.assertEquals(result, resultOptional.get());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = true;
        Mockito.when(userTaskInstanceRepository.findConfirmById(Mockito.anyLong())).thenReturn(confirm);

        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        Assertions.assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(testEntity);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.PENDING);
        Assertions.assertEquals(userTaskInstanceEntities, result);
    }
}