import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowInstance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity testUserTaskInstance;
    private Long testId;

    @Before
    public void setup() {
        testUserTaskInstance = new UserTaskInstanceEntity();
        testUserTaskInstance.setId(1L);
        testUserTaskInstance.setFlowInstanceId(1L);
        testUserTaskInstance.setStatus(FlowNodeStatus.APPROVED);

        testId = testUserTaskInstance.getId();
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(testUserTaskInstance);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        Assert.assertEquals(userTaskInstanceEntities, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setId(2L);
        userTaskInstance1.setFlowInstanceId(1L);
        userTaskInstance1.setStatus(FlowNodeStatus.WAITING_FOR_APPROVAL);

        UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setId(3L);
        userTaskInstance2.setFlowInstanceId(1L);
        userTaskInstance2.setStatus(FlowNodeStatus.APPROVED);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstance1, testUserTaskInstance, userTaskInstance2);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Long userId = 1L;
        Collection<Long> roleIds = Collections.singletonList(1L);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        List<UserTaskInstanceEntity> expectedResult = Arrays.asList(userTaskInstance1);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setId(2L);
        userTaskInstance1.setFlowInstanceId(1L);
        userTaskInstance1.setStatus(FlowNodeStatus.WAITING_FOR_APPROVAL);

        UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setId(3L);
        userTaskInstance2.setFlowInstanceId(1L);
        userTaskInstance2.setStatus(FlowNodeStatus.APPROVED);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstance1, testUserTaskInstance, userTaskInstance2);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Collection<String> resourceRoleIdentifiers = Arrays.asList("roleIdentifier1");

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        List<UserTaskInstanceEntity> expectedResult = Collections.singletonList(userTaskInstance1);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByRoleId() {
        UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setId(2L);
        userTaskInstance1.setFlowInstanceId(1L);
        userTaskInstance1.setStatus(FlowNodeStatus.WAITING_FOR_APPROVAL);

        UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setId(3L);
        userTaskInstance2.setFlowInstanceId(1L);
        userTaskInstance2.setStatus(FlowNodeStatus.APPROVED);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstance1, testUserTaskInstance, userTaskInstance2);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Long roleId = 2L;

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);

        List<UserTaskInstanceEntity> expectedResult = Arrays.asList(userTaskInstance1);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setId(2L);
        userTaskInstance1.setFlowInstanceId(1L);
        userTaskInstance1.setStatus(FlowNodeStatus.WAITING_FOR_APPROVAL);

        UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setId(3L);
        userTaskInstance2.setFlowInstanceId(1L);
        userTaskInstance2.setStatus(FlowNodeStatus.APPROVED);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstance1, testUserTaskInstance, userTaskInstance2);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        Long userId = 1L;

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);

        List<UserTaskInstanceEntity> expectedResult = Arrays.asList(userTaskInstance1);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(testId);

        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        String userTaskId = "userTaskId";
        Long operatorId = 1L;
        String comment = "comment";
        int expireIntervalSeconds = 1;
        Long externalFlowInstanceId = 1L;

        int rowsAffected = userTaskInstanceRepository.update(entity);
        Assert.assertEquals(1, rowsAffected);

        UserTaskInstanceEntity updatedEntity = Mockito.spy(entity);
        Mockito.when(updatedEntity.getUserTaskId()).thenReturn(userTaskId);
        Mockito.when(updatedEntity.getStatus()).thenReturn(status);
        Mockito.when(updatedEntity.getApproved()).thenReturn(true);
        Mockito.when(updatedEntity.getOperatorId()).thenReturn(operatorId);
        Mockito.when(updatedEntity.getComment()).thenReturn(comment);
        Mockito.when(updatedEntity.getExpireIntervalSeconds()).thenReturn(expireIntervalSeconds);
        Mockito.when(updatedEntity.getExternalFlowInstanceId()).thenReturn(externalFlowInstanceId);

        UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(updatedEntity);

        Assert.assertEquals(userTaskId, savedEntity.getUserTaskId());
        Assert.assertEquals(status, savedEntity.getStatus());
        Assert.assertEquals(operatorId, savedEntity.getOperatorId());
        Assert.assertEquals(comment, savedEntity.getComment());
        Assert.assertEquals(expireIntervalSeconds, savedEntity.getExpireIntervalSeconds());
        Assert.assertEquals(externalFlowInstanceId, savedEntity.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        String userTaskId = "userTaskId";
        Long id = 1L;

        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        Assert.assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
        Assert.assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
        Assert.assertEquals(1, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setFlowInstanceId(1L);
        userTaskInstance.setStatus(FlowNodeStatus.APPROVED);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "activityId", 1L))
                .thenReturn(Optional.of(userTaskInstance));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "activityId", 1L);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(userTaskInstance, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setFlowInstanceId(1L);
        userTaskInstance.setStatus(FlowNodeStatus.APPROVED);

        Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name", 1L))
                .thenReturn(Optional.of(userTaskInstance));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name", 1L);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(userTaskInstance, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;

        Mockito.when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(confirm);

        Boolean result = userTaskInstanceRepository.findConfirmById(id);

        Assert.assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(testUserTaskInstance);
        Mockito.when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(1L, FlowNodeStatus.APPROVED);

        List<UserTaskInstanceEntity> expectedResult = Arrays.asList(testUserTaskInstance);

        Assert.assertEquals(expectedResult, result);
    }
}