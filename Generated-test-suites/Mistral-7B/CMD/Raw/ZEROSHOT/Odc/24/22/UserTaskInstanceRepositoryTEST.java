import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        expectedUserTaskInstanceEntities.add(userTaskInstanceEntity);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "status", FlowNodeStatus.APPROVED);

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        expectedUserTaskInstanceEntities.add(userTaskInstanceEntity);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "status", FlowNodeStatus.APPROVED);

        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        expectedUserTaskInstanceEntities.add(userTaskInstanceEntity);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "status", FlowNodeStatus.APPROVED);

        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier1");

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        expectedUserTaskInstanceEntities.add(userTaskInstanceEntity);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "status", FlowNodeStatus.APPROVED);

        Long roleId = 1L;

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(roleId);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        expectedUserTaskInstanceEntities.add(userTaskInstanceEntity);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "status", FlowNodeStatus.APPROVED);

        Long userId = 1L;

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(userId);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();

        Long id = 1L;
        String userTaskId = "userTaskId1";
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        String operatorId = "operatorId1";
        String comment = "comment1";
        Integer expireIntervalSeconds = 1;
        Long externalFlowInstanceId = 1L;

        userTaskInstanceRepository.update(userTaskInstanceEntity);

        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "id", id);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "userTaskId", userTaskId);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "status", status);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "approved", true);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "operatorId", operatorId);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "comment", comment);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "expireIntervalSeconds", expireIntervalSeconds);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "externalFlowInstanceId", externalFlowInstanceId);

        int actualResult = userTaskInstanceRepository.update(userTaskInstanceEntity);

        assertEquals(1, actualResult);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId1";

        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(id);
        userTaskInstanceEntity.setUserTaskId(userTaskId);

        UserTaskInstanceEntity actualUserTaskInstanceEntity = userTaskInstanceRepository.findById(id).get();

        assertEquals(userTaskInstanceEntity, actualUserTaskInstanceEntity);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        userTaskInstanceRepository.updateStatusById(id, status);

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(id);
        userTaskInstanceEntity.setStatus(status);

        UserTaskInstanceEntity actualUserTaskInstanceEntity = userTaskInstanceRepository.findById(id).get();

        assertEquals(userTaskInstanceEntity, actualUserTaskInstanceEntity);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int actualResult = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, actualResult);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "activityId1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> expectedUserTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());

        Optional<UserTaskInstanceEntity> actualUserTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        assertEquals(expectedUserTaskInstanceEntityOptional, actualUserTaskInstanceEntityOptional);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "name1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> expectedUserTaskInstanceEntityOptional = Optional.of(new UserTaskInstanceEntity());

        Optional<UserTaskInstanceEntity> actualUserTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        assertEquals(expectedUserTaskInstanceEntityOptional, actualUserTaskInstanceEntityOptional);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        Boolean expectedBoolean = true;

        Boolean actualBoolean = userTaskInstanceRepository.findConfirmById(id);

        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        expectedUserTaskInstanceEntities.add(userTaskInstanceEntity);
        ReflectionTestUtils.setFieldValue(userTaskInstanceEntity, "status", status);

        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status);

        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }
}