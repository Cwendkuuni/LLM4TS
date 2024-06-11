package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserTaskInstanceRepositoryTest {

    @Mock
    UserTaskInstanceEntity entity;

    @InjectMocks
    UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = new ArrayList<>();
        expected.add(entity);

        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 123L;
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 123L;
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        assertNotNull(result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");

        List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);

        assertNotNull(result);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> result = repository.findByRoleId(roleId);

        assertNotNull(result);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 123L;

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(userId);

        assertNotNull(result);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity param = new UserTaskInstanceEntity();
        param.setId(1L);
        param.setUserTaskId("Task1");
        param.setStatus(FlowNodeStatus.PENDING);
        param.setApproved(false);
        param.setOperatorId(123L);
        param.setComment("Test comment");
        param.setExpireIntervalSeconds(60L);
        param.setExternalFlowInstanceId("Flow1");

        int result = repository.update(param);

        assertTrue(result >= 0);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "Task1";

        int result = repository.updateUserTaskIdById(id, userTaskId);

        assertTrue(result >= 0);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;

        int result = repository.updateStatusById(id, FlowNodeStatus.APPROVED);

        assertTrue(result >= 0);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = repository.deleteByFlowInstanceId(instanceId);

        assertTrue(result >= 0);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String activityId = "Activity1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        assertTrue(result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.TASK;
        String name = "Task1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);

        assertTrue(result.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        Boolean result = repository.findConfirmById(id);

        assertNotNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.PENDING;

        List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        assertNotNull(result);
    }

}