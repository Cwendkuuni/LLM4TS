package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = new UserTaskInstanceRepositoryImpl().findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> result = new UserTaskInstanceRepositoryImpl().findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> result = new UserTaskInstanceRepositoryImpl().findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");
        List<UserTaskInstanceEntity> result = new UserTaskInstanceRepositoryImpl().findByResourceRoleIdentifierIn(resourceRoleIdentifier);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> result = new UserTaskInstanceRepositoryImpl().findByRoleId(roleId);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> result = new UserTaskInstanceRepositoryImpl().findByCandidateUserId(userId);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        Long id = 1L;
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceRepositoryImpl().update(entity).orElseThrow();
        assertEquals(id, updatedEntity.getId());
        // Add assertions for other fields as needed
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "task1";
        int result = new UserTaskInstanceRepositoryImpl().updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        int result = new UserTaskInstanceRepositoryImpl().updateStatusById(id, status);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = new UserTaskInstanceRepositoryImpl().deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        Long flowInstanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String activityId = "activity1";
        Optional<UserTaskInstanceEntity> result = new UserTaskInstanceRepositoryImpl().findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        Long flowInstanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.USER_TASK;
        String name = "userTask1";
        Optional<UserTaskInstanceEntity> result = new UserTaskInstanceRepositoryImpl().findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = true;
        Boolean result = new UserTaskInstanceRepositoryImpl().findConfirmById(id);
        assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> result = new UserTaskInstanceRepositoryImpl().findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
}