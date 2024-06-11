package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity1 = Mockito.spy(new UserTaskInstanceEntity());
        entity1.setStatus(FlowNodeStatus.APPROVED);
        UserTaskInstanceEntity entity2 = Mockito.spy(new UserTaskInstanceEntity());
        entity2.setStatus(FlowNodeStatus.IN_PROGRESS);
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.APPROVED);

        verify(repository, times(1)).findByStatus(eq(FlowNodeStatus.APPROVED));
        assertEquals(1, result.size());
        assertEquals(FlowNodeStatus.APPROVED, result.get(0).getStatus());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setUserId(2L);
        UserTaskInstanceEntity entity3 = new UserTaskInstanceEntity();
        entity3.setRoleId(1L);
        when(repository.findAll()).thenReturn(List.of(entity1, entity2, entity3));

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(1L, List.of(1L));

        verify(repository, times(1)).findByCandidateUserIdOrRoleIds(eq(1L), eq(List.of(1L)));
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setResourceRoleIdentifier("role1");
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, List.of("role1"));

        verify(repository, times(1)).findByCandidateUserIdOrResourceRoleIdentifier(eq(1L), eq(List.of("role1")));
        assertEquals(1, result.size());
        assertTrue(result.contains(entity2));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setResourceRoleIdentifier("role1");
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setResourceRoleIdentifier("role2");
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(List.of("role1"));

        verify(repository, times(1)).findByResourceRoleIdentifierIn(eq(List.of("role1")));
        assertEquals(1, result.size());
        assertTrue(result.contains(entity1));
    }

    @Test
    public void testFindByRoleId() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setRoleId(1L);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        List<UserTaskInstanceEntity> result = repository.findByRoleId(1L);

        verify(repository, times(1)).findByRoleId(eq(1L));
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getRoleId());
    }

    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setUserId(1L);
        when(repository.findAll()).thenReturn(List.of(entity1));

        List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(1L);

        verify(repository, times(1)).findByCandidateUserId(eq(1L));
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        doNothing().when(repository).update(eq(entity));

        repository.update(entity);

        verify(repository, times(1)).update(eq(entity));
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        doNothing().when(repository).updateUserTaskIdById(eq(entity.getId()), eq("userTaskId"));

        repository.updateUserTaskIdById(entity.getId(), "userTaskId");

        verify(repository, times(1)).updateUserTaskIdById(eq(entity.getId()), eq("userTaskId"));
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        doNothing().when(repository).updateStatusById(eq(entity.getId()), eq(FlowNodeStatus.APPROVED));

        repository.updateStatusById(entity.getId(), FlowNodeStatus.APPROVED);

        verify(repository, times(1)).updateStatusById(eq(entity.getId()), eq(FlowNodeStatus.APPROVED));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        doNothing().when(repository).deleteByFlowInstanceId(eq(entity.getFlowInstanceId()));

        repository.deleteByFlowInstanceId(entity.getFlowInstanceId());

        verify(repository, times(1)).deleteByFlowInstanceId(eq(entity.getFlowInstanceId()));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        when(repository.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activityId", 1L)).thenReturn(Optional.ofNullable(entity));

        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activityId", 1L);

        verify(repository, times(1)).findByInstanceTypeAndActivityId(eq(FlowNodeType.TASK), eq("activityId"), eq(1L));
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        when(repository.findByInstanceTypeAndName(FlowNodeType.TASK, "name", 1L)).thenReturn(Optional.ofNullable(entity));

        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(FlowNodeType.TASK, "name", 1L);

        verify(repository, times(1)).findByInstanceTypeAndName(eq(FlowNodeType.TASK), eq("name"), eq(1L));
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        when(repository.findConfirmById(1L)).thenReturn(true);

        boolean result = repository.findConfirmById(1L);

        verify(repository, times(1)).findConfirmById(eq(1L));
        assertTrue(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setFlowInstanceId(1L);
        entity1.setStatus(FlowNodeStatus.APPROVED);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setFlowInstanceId(1L);
        entity2.setStatus(FlowNodeStatus.IN_PROGRESS);
        when(repository.findAll()).thenReturn(new ArrayList<>(List.of(entity1, entity2)));

        List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(List.of(1L), FlowNodeStatus.APPROVED);

        verify(repository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(eq(List.of(1L)), eq(FlowNodeStatus.APPROVED));
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }
}