package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> list = repository.findByStatus(FlowNodeStatus.APPROVED);
        assertNotNull(list);
        assertEquals(0, list.size());

        list = repository.findByStatus(FlowNodeStatus.REJECTED);
        assertNotNull(list);
        assertEquals(1, list.size());

        list = repository.findByStatus(FlowNodeStatus.WAITING);
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> list = repository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>());
        assertNotNull(list);
        assertEquals(0, list.size());

        list = repository.findByCandidateUserIdOrRoleIds(1L, List.of(1L, 2L));
        assertNotNull(list);
        assertEquals(2, list.size());

        list = repository.findByCandidateUserIdOrRoleIds(null, List.of(1L, 2L));
        assertNull(list);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> list = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>());
        assertNotNull(list);
        assertEquals(0, list.size());

        list = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, List.of("admin", "user"));
        assertNotNull(list);
        assertEquals(2, list.size());

        list = repository.findByCandidateUserIdOrResourceRoleIdentifier(null, List.of("admin", "user"));
        assertNull(list);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> list = repository.findByResourceRoleIdentifierIn(List.of("admin"));
        assertNotNull(list);
        assertEquals(0, list.size());

        list = repository.findByResourceRoleIdentifierIn(List.of("admin", "user"));
        assertNotNull(list);
        assertEquals(2, list.size());

        list = repository.findByResourceRoleIdentifierIn(null);
        assertNull(list);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> list = repository.findByRoleId(1L);
        assertNotNull(list);
        assertEquals(0, list.size());

        list = repository.findByRoleId(2L);
        assertNotNull(list);
        assertEquals(1, list.size());

        list = repository.findByRoleId(null);
        assertNull(list);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> list = repository.findByCandidateUserId(1L);
        assertNotNull(list);
        assertEquals(0, list.size());

        list = repository.findByCandidateUserId(2L);
        assertNotNull(list);
        assertEquals(1, list.size());

        list = repository.findByCandidateUserId(null);
        assertNull(list);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        UserTaskInstanceEntity param = new UserTaskInstanceEntity();
        param.setUserTaskId("testUserTaskId");
        param.setStatus(FlowNodeStatus.APPROVED);
        param.setApproved(true);
        param.setOperatorId(1L);
        param.setComment("testComment");
        param.setExpireIntervalSeconds(3600);
        param.setExternalFlowInstanceId("testExternalFlowInstanceId");

        int rowsAffected = repository.update(param);
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity dbEntity = repository.findById(entity.getId()).get();
        assertEquals("testUserTaskId", dbEntity.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, dbEntity.getStatus());
        assertTrue(dbEntity.isApproved());
        assertEquals(1L, dbEntity.getOperatorId().longValue());
        assertEquals("testComment", dbEntity.getComment());
        assertEquals(3600, dbEntity.getExpireIntervalSeconds().intValue());
        assertEquals("testExternalFlowInstanceId", dbEntity.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testUserTaskId";

        int rowsAffected = repository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity dbEntity = repository.findById(id).get();
        assertEquals(userTaskId, dbEntity.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int rowsAffected = repository.updateStatusById(id, status);
        assertEquals(1, rowsAffected);

        UserTaskInstanceEntity dbEntity = repository.findById(id).get();
        assertEquals(status, dbEntity.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int rowsAffected = repository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, rowsAffected);

        Optional<UserTaskInstanceEntity> dbEntity = repository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "task1", instanceId);
        assertFalse(dbEntity.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> dbEntity = repository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "task1", 1L);
        assertFalse(dbEntity.isPresent());

        dbEntity = repository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "task1", 2L);
        assertTrue(dbEntity.isPresent());
        UserTaskInstanceEntity entity = dbEntity.get();
        assertEquals(FlowNodeType.USER_TASK, entity.getNodeType());
        assertEquals("task1", entity.getNodeName());
        assertEquals(2L, entity.getFlowInstanceId().longValue());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> dbEntity = repository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "task2", 1L);
        assertFalse(dbEntity.isPresent());

        dbEntity = repository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "task2", 2L);
        assertTrue(dbEntity.isPresent());
        UserTaskInstanceEntity entity = dbEntity.get();
        assertEquals(FlowNodeType.USER_TASK, entity.getNodeType());
        assertEquals("task2", entity.getNodeName());
        assertEquals(2L, entity.getFlowInstanceId().longValue());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = repository.findConfirmById(id);
        assertFalse(confirm);

        repository.updateStatusById(id, FlowNodeStatus.APPROVED);
        confirm = repository.findConfirmById(id);
        assertTrue(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> list = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(List.of(1L, 2L),
                FlowNodeStatus.APPROVED);
        assertEquals(1, list.size());
        UserTaskInstanceEntity entity = list.get(0);
        assertEquals(FlowNodeStatus.APPROVED, entity.getStatus());
    }
}