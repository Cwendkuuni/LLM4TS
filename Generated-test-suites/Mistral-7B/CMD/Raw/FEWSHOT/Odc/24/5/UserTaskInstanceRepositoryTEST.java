package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
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
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.APPROVED);
        entities.add(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.REJECTED);
        entities.add(entity2);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);
        entity1.setRoleIds(List.of(1L, 2L));

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setUserId(2L);
        entity2.setRoleIds(List.of(2L));

        UserTaskInstanceEntity entity3 = new UserTaskInstanceEntity();
        entity3.setUserId(3L);
        entity3.setRoleIds(List.of(1L));

        List<UserTaskInstanceEntity> entities = List.of(entity1, entity2, entity3);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByCandidateUserIdOrRoleIds(1L, List.of(1L))).thenReturn(entities);

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(1L, List.of(1L));
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));

        result = repository.findByCandidateUserIdOrRoleIds(2L, List.of(2L));
        assertEquals(1, result.size());
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());

        result = repository.findByCandidateUserIdOrRoleIds(3L, List.of(1L, 2L));
        assertNull(result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);
        entity1.setResourceRoleIdentifier("role1");

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setUserId(2L);
        entity2.setResourceRoleIdentifier("role2");

        List<UserTaskInstanceEntity> entities = List.of(entity1, entity2);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, List.of("role1"))).thenReturn(entities);

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, List.of("role1"));
        assertEquals(1, result.size());
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());

        result = repository.findByCandidateUserIdOrResourceRoleIdentifier(2L, List.of("role1"));
        assertNull(result);

        result = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, List.of("role2"));
        assertNull(result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setResourceRoleIdentifier("role1");

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setResourceRoleIdentifier("role2");

        List<UserTaskInstanceEntity> entities = List.of(entity1, entity2);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByResourceRoleIdentifierIn(List.of("role1"))).thenReturn(entities);

        List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(List.of("role1"));
        assertEquals(1, result.size());
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());

        result = repository.findByResourceRoleIdentifierIn(List.of("role2"));
        assertEquals(1, result.size());
        assertEquals(FlowNodeStatus.REJECTED, result.get(0).getStatus());
    }

    @Test
    public void testFindByRoleId() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setRoleId(1L);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setRoleId(2L);

        List<UserTaskInstanceEntity> entities = List.of(entity1, entity2);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByRoleId(1L)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = repository.findByRoleId(1L);
        assertEquals(1, result.size());
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setUserId(2L);

        List<UserTaskInstanceEntity> entities = List.of(entity1, entity2);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findByCandidateUserId(1L)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(1L);
        assertEquals(1, result.size());
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("userTaskId");
        entity.setStatus(FlowNodeStatus.APPROVED);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        doNothing().when(repository).update(entity);

        repository.update(entity);
        verify(repository, times(1)).update(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        doNothing().when(repository).updateUserTaskIdById(1L, "userTaskId");

        repository.updateUserTaskIdById(1L, "userTaskId");
        verify(repository, times(1)).updateUserTaskIdById(1L, "userTaskId");
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.APPROVED);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        doNothing().when(repository).updateStatusById(1L, FlowNodeStatus.REJECTED);

        repository.updateStatusById(1L, FlowNodeStatus.REJECTED);
        verify(repository, times(1)).updateStatusById(1L, FlowNodeStatus.REJECTED);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        doNothing().when(repository).deleteByFlowInstanceId(1L);

        repository.deleteByFlowInstanceId(1L);
        verify(repository, times(1)).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);
        entity.setUserTaskId("userTaskId");

        when(repository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "activityId", 1L)).thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "activityId", 1L);
        assertTrue(result.isPresent());
        assertEquals("userTaskId", result.get().getUserTaskId());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);
        entity.setUserTaskId("userTaskId");

        when(repository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name", 1L)).thenReturn(Optional.of(entity));

        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "name", 1L);
        assertTrue(result.isPresent());
        assertEquals("userTaskId", result.get().getUserTaskId());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setConfirm(true);

        when(repository.findConfirmById(1L)).thenReturn(true);

        boolean result = repository.findConfirmById(1L);
        assertTrue(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setId(1L);
        entity1.setFlowInstanceId(1L);
        entity1.setStatus(FlowNodeStatus.APPROVED);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(2L);
        entity2.setFlowInstanceId(1L);
        entity2.setStatus(FlowNodeStatus.REJECTED);

        List<UserTaskInstanceEntity> entities = List.of(entity1, entity2);

        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        when(repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(List.of(1L), FlowNodeStatus.APPROVED)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(List.of(1L), FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());
    }
}