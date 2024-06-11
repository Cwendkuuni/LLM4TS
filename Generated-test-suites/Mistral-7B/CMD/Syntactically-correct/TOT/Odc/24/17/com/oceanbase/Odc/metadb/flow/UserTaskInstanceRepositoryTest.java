package com.oceanbase.odc.metadb.flow.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserTaskInstanceEntity userTaskInstance1;
    private UserTaskInstanceEntity userTaskInstance2;
    private UserTaskInstanceEntity userTaskInstance3;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setId(1L);
        userTaskInstance1.setFlowInstanceId(1L);
        userTaskInstance1.setStatus(FlowNodeStatus.WAITING);
        userTaskInstance1.setUserTaskId("task1");

        userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setId(2L);
        userTaskInstance2.setFlowInstanceId(1L);
        userTaskInstance2.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstance2.setUserTaskId("task2");

        userTaskInstance3 = new UserTaskInstanceEntity();
        userTaskInstance3.setId(3L);
        userTaskInstance3.setFlowInstanceId(2L);
        userTaskInstance3.setStatus(FlowNodeStatus.REJECTED);
        userTaskInstance3.setUserTaskId("task3");

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAITING)).thenReturn(Arrays.asList(userTaskInstance1));
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(Arrays.asList(userTaskInstance2));
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.REJECTED)).thenReturn(Collections.singletonList(userTaskInstance3));
        when(userTaskInstanceRepository.findById(1L)).thenReturn(Optional.of(userTaskInstance1));
        when(userTaskInstanceRepository.findById(2L)).thenReturn(Optional.of(userTaskInstance2));
        when(userTaskInstanceRepository.findById(3L)).thenReturn(Optional.of(userTaskInstance3));
        when(userTaskInstanceRepository.save(ArgumentMatchers.any(UserTaskInstanceEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntitiesWaiting = userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAITING);
        assertEquals(1, userTaskInstanceEntitiesWaiting.size());
        UserTaskInstanceEntity userTaskInstanceEntityWaiting = userTaskInstanceEntitiesWaiting.get(0);
        assertEquals(FlowNodeStatus.WAITING, userTaskInstanceEntityWaiting.getStatus());

        List<UserTaskInstanceEntity> userTaskInstanceEntitiesApproved = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, userTaskInstanceEntitiesApproved.size());
        UserTaskInstanceEntity userTaskInstanceEntityApproved = userTaskInstanceEntitiesApproved.get(0);
        assertEquals(FlowNodeStatus.APPROVED, userTaskInstanceEntityApproved.getStatus());

        List<UserTaskInstanceEntity> userTaskInstanceEntitiesRejected = userTaskInstanceRepository.findByStatus(FlowNodeStatus.REJECTED);
        assertEquals(1, userTaskInstanceEntitiesRejected.size());
        UserTaskInstanceEntity userTaskInstanceEntityRejected = userTaskInstanceEntitiesRejected.get(0);
        assertEquals(FlowNodeStatus.REJECTED, userTaskInstanceEntityRejected.getStatus());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertEquals(2, userTaskInstanceEntities.size());
        for (UserTaskInstanceEntity userTaskInstanceEntity : userTaskInstanceEntities) {
            if (userTaskInstanceEntity.getCandidate().getUserId().equals(1L)) {
                assertEquals(1L, userTaskInstanceEntity.getId());
                assertEquals(FlowNodeStatus.WAITING, userTaskInstanceEntity.getStatus());
                assertEquals("task1", userTaskInstanceEntity.getUserTaskId());
            } else {
                assertEquals(2L, userTaskInstanceEntity.getId());
                assertEquals(FlowNodeStatus.APPROVED, userTaskInstanceEntity.getStatus());
                assertEquals("task2", userTaskInstanceEntity.getUserTaskId());
            }
        }
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        assertEquals(2, userTaskInstanceEntities.size());
        for (UserTaskInstanceEntity userTaskInstanceEntity : userTaskInstanceEntities) {
            if (userTaskInstanceEntity.getCandidate().getUserId().equals(1L)) {
                assertEquals(1L, userTaskInstanceEntity.getId());
                assertEquals(FlowNodeStatus.WAITING, userTaskInstanceEntity.getStatus());
                assertEquals("task1", userTaskInstanceEntity.getUserTaskId());
            } else {
                assertEquals(3L, userTaskInstanceEntity.getId());
                assertEquals(FlowNodeStatus.REJECTED, userTaskInstanceEntity.getStatus());
                assertEquals("task3", userTaskInstanceEntity.getUserTaskId());
                assertEquals("role2", userTaskInstanceEntity.getCandidate().getResourceRoleIdentifier());
            }
        }
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        assertEquals(3, userTaskInstanceEntities.size());
        for (UserTaskInstanceEntity userTaskInstanceEntity : userTaskInstanceEntities) {
            if (userTaskInstanceEntity.getCandidate().getResourceRoleIdentifier().equals("role1")) {
                assertEquals(1L, userTaskInstanceEntity.getId());
                assertEquals(FlowNodeStatus.WAITING, userTaskInstanceEntity.getStatus());
                assertEquals("task1", userTaskInstanceEntity.getUserTaskId());
            } else if (userTaskInstanceEntity.getCandidate().getResourceRoleIdentifier().equals("role2")) {
                assertEquals(3L, userTaskInstanceEntity.getId());
                assertEquals(FlowNodeStatus.REJECTED, userTaskInstanceEntity.getStatus());
                assertEquals("task3", userTaskInstanceEntity.getUserTaskId());
            }
        }
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(1, userTaskInstanceEntities.size());
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntities.get(0);
        assertEquals(1L, userTaskInstanceEntity.getId());
        assertEquals(FlowNodeStatus.WAITING, userTaskInstanceEntity.getStatus());
        assertEquals("task1", userTaskInstanceEntity.getUserTaskId());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(1, userTaskInstanceEntities.size());
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntities.get(0);
        assertEquals(1L, userTaskInstanceEntity.getId());
        assertEquals(FlowNodeStatus.WAITING, userTaskInstanceEntity.getStatus());
        assertEquals("task1", userTaskInstanceEntity.getUserTaskId());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity updatedUserTaskInstance = new UserTaskInstanceEntity();
        updatedUserTaskInstance.setId(1L);
        updatedUserTaskInstance.setFlowInstanceId(1L);
        updatedUserTaskInstance.setStatus(FlowNodeStatus.APPROVED);
        updatedUserTaskInstance.setUserTaskId("task4");
        updatedUserTaskInstance = userTaskInstanceRepository.save(updatedUserTaskInstance);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);
        entity.setUserTaskId("task1");
        entity.setStatus(FlowNodeStatus.WAITING);

        int result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).save(entity);

        UserTaskInstanceEntity updatedEntity = userTaskInstanceRepository.findById(1L).get();
        assertEquals("task4", updatedEntity.getUserTaskId());
        assertEquals(FlowNodeStatus.APPROVED, updatedEntity.getStatus());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "task4");
        assertEquals(1, result);
        verify(userTaskInstanceRepository).executeUpdate("update flow_instance_node_approval set user_task_id=? where id=?", "task4", 1L);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        assertEquals(1, result);
        verify(userTaskInstanceRepository).executeUpdate("update flow_instance_node_approval set status=? where id=?", "APPROVED", 1L);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(3, result);
        verify(userTaskInstanceRepository).executeUpdate("delete from UserTaskInstanceEntity as ut where ut.flowInstanceId=?", 1L);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activity1", 1L);
        assertTrue(userTaskInstanceEntityOptional.isPresent());
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
        assertEquals(1L, userTaskInstanceEntity.getId());
        assertEquals(FlowNodeStatus.APPROVED, userTaskInstanceEntity.getStatus());
        assertEquals("task2", userTaskInstanceEntity.getUserTaskId());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.GATEWAY, "gateway1", 1L);
        assertTrue(userTaskInstanceEntityOptional.isPresent());
        UserTaskInstanceEntity userTaskInstanceEntity = userTaskInstanceEntityOptional.get();
        assertEquals(2L, userTaskInstanceEntity.getId());
        assertEquals(FlowNodeStatus.APPROVED, userTaskInstanceEntity.getStatus());
        assertEquals("task2", userTaskInstanceEntity.getUserTaskId());
    }

    @Test
    public void testFindConfirmById() {
        assertTrue(userTaskInstanceRepository.findConfirmById(1L));
        verify(userTaskInstanceRepository, times(1)).executeQuery("select wait_for_confirm from flow_instance_node_approval where id =? limit 1", ArgumentMatchers.eq(1L));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertEquals(2, userTaskInstanceEntities.size());
        for (UserTaskInstanceEntity userTaskInstanceEntity : userTaskInstanceEntities) {
            assertEquals(2L, userTaskInstanceEntity.getId());
            assertEquals(FlowNodeStatus.APPROVED, userTaskInstanceEntity.getStatus());
            assertEquals("task2", userTaskInstanceEntity.getUserTaskId());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceIdAndStatusWithEmptyList() {
        userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.emptyList(), FlowNodeStatus.APPROVED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceIdAndStatusWithNullStatus() {
        userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceIdAndStatusWithNullFlowInstanceId() {
        userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(null, FlowNodeStatus.APPROVED);
    }
}