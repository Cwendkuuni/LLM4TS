package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        List<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        List<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        List<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        List<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        List<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findByRoleId(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        List<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findByCandidateUserId(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        int rowsAffected = new UserTaskInstanceRepositoryImpl().update(entity);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int rowsAffected = new UserTaskInstanceRepositoryImpl().updateUserTaskIdById(1L, "userTaskId");
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        int rowsAffected = new UserTaskInstanceRepositoryImpl().updateStatusById(1L, FlowNodeStatus.APPROVED);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = new UserTaskInstanceRepositoryImpl().deleteByFlowInstanceId(1L);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> expected = Optional.of(new UserTaskInstanceEntity());
        Optional<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "activityId", 1L);
        assertTrue(expected.isPresent());
        assertEquals(expected.get(), actual.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> expected = Optional.of(new UserTaskInstanceEntity());
        Optional<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name", 1L);
        assertTrue(expected.isPresent());
        assertEquals(expected.get(), actual.get());
    }

    @Test
    public void testFindConfirmById() {
        Boolean expected = true;
        Boolean actual = new UserTaskInstanceRepositoryImpl().findConfirmById(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        List<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        List<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findAll();
        assertEquals(expected, actual);
    }

    @Test
    public void testFindAllEmpty() {
        List<UserTaskInstanceEntity> actual = new UserTaskInstanceRepositoryImpl().findAll();
        assertTrue(isEmpty(actual));
    }

    @Test
    public void testFindAllByPage() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Page<UserTaskInstanceEntity> page = new UserTaskInstanceRepositoryImpl().findAll(new PageRequest(0, 10));
        List<UserTaskInstanceEntity> actual = page.getContent();
        assertEquals(expected, actual);
    }

    @Test
    public void testFindAllByPageEmpty() {
        Page<UserTaskInstanceEntity> page = new UserTaskInstanceRepositoryImpl().findAll(new PageRequest(0, 10));
        assertTrue(isEmpty(page.getContent()));
    }
}