package com.oceanbase.odc.service.flow.repository;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    // Initialize test data here, if needed

    @Before
    public void setup() {
        // Initialize test data here, if needed
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());

        // Test with other statuses
        userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.REJECTED);
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());

        userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.IN_PROGRESS);
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Test with valid user ids
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());

        // Test with valid role ids
        userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Arrays.asList(1L));
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());

        // Test with both user ids and role ids
        userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        // Test with valid role identifiers
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());

        userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role2"));
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(1L);
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserId(1L);
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        int rowsAffected = userTaskInstanceRepository.update(entity);
        Assert.assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "taskId1";
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        Assert.assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.REJECTED;
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
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "activityId1", 1L);
        Assert.assertTrue(userTaskInstanceEntityOptional.isPresent());
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
        Assert.assertNotNull(userTaskInstanceEntity);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name1", 1L);
        Assert.assertTrue(userTaskInstanceEntityOptional.isPresent());
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
        Assert.assertNotNull(userTaskInstanceEntity);
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
        Assert.assertNotNull(confirm);
        Assert.assertEquals(true, confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        Assert.assertNotNull(userTaskInstances);
        Assert.assertEquals(2, userTaskInstances.size());
    }

    @Test
    public void testFindAll() {
        Page<UserTaskInstanceEntity> page = userTaskInstanceRepository.findAll(PageRequest.of(0, 10));
        Assert.assertNotNull(page);
        Assert.assertNotNull(page.getContent());
        Assert.assertEquals(10, page.getSize());
    }
}