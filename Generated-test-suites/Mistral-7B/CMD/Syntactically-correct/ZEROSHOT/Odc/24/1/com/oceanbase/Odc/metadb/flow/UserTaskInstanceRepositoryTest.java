import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setUserTaskId("userTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceEntity.setApproved(true);
        userTaskInstanceEntity.setOperatorId("operatorId");
        userTaskInstanceEntity.setComment("comment");
        userTaskInstanceEntity.setExpireIntervalSeconds(1000L);
        userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        Assert.assertEquals(1, userTaskInstanceEntities.size());
        Assert.assertEquals(userTaskInstanceEntity, userTaskInstanceEntities.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
        Assert.assertEquals(1, userTaskInstanceEntities.size());
        Assert.assertEquals(userTaskInstanceEntity, userTaskInstanceEntities.get(0));

        List<UserTaskInstanceEntity> userTaskInstanceEntities2 = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Arrays.asList(1L));
        Assert.assertEquals(1, userTaskInstanceEntities2.size());
        Assert.assertEquals(userTaskInstanceEntity, userTaskInstanceEntities2.get(0));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("resourceRoleIdentifier"));
        Assert.assertEquals(1, userTaskInstanceEntities.size());
        Assert.assertEquals(userTaskInstanceEntity, userTaskInstanceEntities.get(0));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(1L);
        Assert.assertEquals(1, userTaskInstanceEntities.size());
        Assert.assertEquals(userTaskInstanceEntity, userTaskInstanceEntities.get(0));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(1L);
        Assert.assertEquals(1, userTaskInstanceEntities.size());
        Assert.assertEquals(userTaskInstanceEntity, userTaskInstanceEntities.get(0));
    }

    @Test
    public void testUpdate() {
        int result = userTaskInstanceRepository.update(userTaskInstanceEntity);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "updatedUserTaskId");
        Assert.assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.REJECTED);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START_EVENT, "activityId", 1L);
        Assert.assertTrue(userTaskInstanceOptional.isPresent());
        Assert.assertEquals(userTaskInstanceEntity, userTaskInstanceOptional.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> userTaskInstanceOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name", 1L);
        Assert.assertTrue(userTaskInstanceOptional.isPresent());
        Assert.assertEquals(userTaskInstanceEntity, userTaskInstanceOptional.get());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
        Assert.assertFalse(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        Assert.assertEquals(1, userTaskInstanceEntities.size());
        Assert.assertEquals(userTaskInstanceEntity, userTaskInstanceEntities.get(0));
    }
}