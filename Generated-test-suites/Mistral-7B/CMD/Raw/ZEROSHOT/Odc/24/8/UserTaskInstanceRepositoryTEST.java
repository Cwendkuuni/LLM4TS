package com.oceanbase.odc.metadb.flow.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

@RunWith(SpringRunner.class)
public class UserTaskInstanceRepositoryTest {

    @org.junit.Inject
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 2L));
        assertNotNull(userTaskInstances);
        assertEquals(2, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2"));
        assertNotNull(userTaskInstances);
        assertEquals(2, userTaskInstances.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("role1"));
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(userTaskInstances);
        assertEquals(1, userTaskInstances.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setUserTaskId("userTaskId1");
        userTaskInstance.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstance.setApproved(true);
        userTaskInstance.setOperatorId(1L);
        userTaskInstance.setComment("comment1");
        userTaskInstance.setExpireIntervalSeconds(10L);
        userTaskInstance.setExternalFlowInstanceId("externalFlowInstanceId1");

        int updatedRows = userTaskInstanceRepository.update(userTaskInstance);
        assertEquals(1, updatedRows);

        UserTaskInstanceEntity updatedUserTaskInstance = userTaskInstanceRepository.findById(1L).get();
        assertEquals(userTaskInstance.getUserTaskId(), updatedUserTaskInstance.getUserTaskId());
        assertEquals(userTaskInstance.getStatus(), updatedUserTaskInstance.getStatus());
        assertEquals(userTaskInstance.isApproved(), updatedUserTaskInstance.isApproved());
        assertEquals(userTaskInstance.getOperatorId(), updatedUserTaskInstance.getOperatorId());
        assertEquals(userTaskInstance.getComment(), updatedUserTaskInstance.getComment());
        assertEquals(userTaskInstance.getExpireIntervalSeconds(), updatedUserTaskInstance.getExpireIntervalSeconds());
        assertEquals(userTaskInstance.getExternalFlowInstanceId(), updatedUserTaskInstance.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId2");
        assertEquals(1, updatedRows);

        UserTaskInstanceEntity userTaskInstance = userTaskInstanceRepository.findById(1L).get();
        assertEquals("userTaskId2", userTaskInstance.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        int updatedRows = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.REJECTED);
        assertEquals(1, updatedRows);

        UserTaskInstanceEntity userTaskInstance = userTaskInstanceRepository.findById(1L).get();
        assertEquals(FlowNodeStatus.REJECTED, userTaskInstance.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, deletedRows);

        assertNull(userTaskInstanceRepository.findByFlowInstanceId(1L));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.APPROVAL, "activityId1", 1L);
        assertFalse(userTaskInstance.isPresent());

        userTaskInstanceRepository.save(new UserTaskInstanceEntity());

        userTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId1", 1L);
        assertTrue(userTaskInstance.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> userTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.APPROVAL, "name1", 1L);
        assertFalse(userTaskInstance.isPresent());

        userTaskInstanceRepository.save(new UserTaskInstanceEntity());

        userTaskInstance = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name1", 1L);
        assertTrue(userTaskInstance.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
        assertFalse(confirm);

        userTaskInstanceRepository.save(new UserTaskInstanceEntity());

        confirm = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstances = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertEquals(1, userTaskInstances.size());
    }

}