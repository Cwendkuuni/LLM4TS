package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.springframework.util.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(
                new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, null),
                new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.WAITING_FOR_APPROVAL, null),
                new UserTaskInstanceEntity(3L, "task3", FlowNodeStatus.REJECTED, null));

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAITING_FOR_APPROVAL);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(userTaskInstanceEntities.get(1)));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, 1L);
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.APPROVED, 2L);
        UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity(3L, "task3", FlowNodeStatus.APPROVED, 3L);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2, userTaskInstanceEntity3);

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));

        assertThat(result, hasSize(2));
        assertThat(result, contains(userTaskInstanceEntity1, userTaskInstanceEntity2));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, "role1");
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.APPROVED, "role2");
        UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity(3L, "task3", FlowNodeStatus.APPROVED, "role1");

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2, userTaskInstanceEntity3);

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));

        assertThat(result, hasSize(2));
        assertThat(result, contains(userTaskInstanceEntity1, userTaskInstanceEntity3));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, "role1");
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.APPROVED, "role2");
        UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity(3L, "task3", FlowNodeStatus.APPROVED, "role1");

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2, userTaskInstanceEntity3);

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));

        assertThat(result, hasSize(2));
        assertThat(result, contains(userTaskInstanceEntity1, userTaskInstanceEntity3));
    }

    @Test
    public void testFindByRoleId() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, null);
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.APPROVED, 1L);
        UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity(3L, "task3", FlowNodeStatus.APPROVED, 2L);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2, userTaskInstanceEntity3);

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(userTaskInstanceEntity2));
    }

    @Test
    public void testFindByCandidateUserId() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, 1L);
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.APPROVED, 2L);
        UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity(3L, "task3", FlowNodeStatus.APPROVED, 3L);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2, userTaskInstanceEntity3);

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalTo(userTaskInstanceEntity1));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.WAITING_FOR_APPROVAL, null);

        UserTaskInstanceEntity updatedUserTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, 1L);

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        int result = userTaskInstanceRepository.update(updatedUserTaskInstanceEntity);

        verify(userTaskInstanceRepository).save(updatedUserTaskInstanceEntity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.WAITING_FOR_APPROVAL, null);

        String userTaskId = "userTaskId";

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, userTaskId);

        verify(userTaskInstanceRepository).save(argThat(instance -> instance.getUserTaskId().equals(userTaskId)));
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.WAITING_FOR_APPROVAL, null);

        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        int result = userTaskInstanceRepository.updateStatusById(1L, status);

        verify(userTaskInstanceRepository).save(argThat(instance -> instance.getStatus().equals(status)));
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, null);
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.WAITING_FOR_APPROVAL, null);
        UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity(3L, "task3", FlowNodeStatus.REJECTED, null);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2, userTaskInstanceEntity3);

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);

        verifyNoMoreInteractions(userTaskInstanceRepository);
        assertEquals(2, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, null);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activityId", 1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activityId", 1L);

        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, null);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TASK, "name", 1L)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.TASK, "name", 1L);

        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.WAITING_FOR_APPROVAL, null);

        Boolean confirm = true;

        when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(confirm);

        Boolean result = userTaskInstanceRepository.findConfirmById(1L);

        assertEquals(confirm, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity(1L, "task1", FlowNodeStatus.APPROVED, null);
        UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity(2L, "task2", FlowNodeStatus.WAITING_FOR_APPROVAL, null);
        UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity(3L, "task3", FlowNodeStatus.REJECTED, null);

        List<UserTaskInstanceEntity> userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2, userTaskInstanceEntity3);

        when(userTaskInstanceRepository.findAll()).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);

        assertThat(result, hasSize(1));
        assertThat(result, contains(userTaskInstanceEntity1));
    }
}