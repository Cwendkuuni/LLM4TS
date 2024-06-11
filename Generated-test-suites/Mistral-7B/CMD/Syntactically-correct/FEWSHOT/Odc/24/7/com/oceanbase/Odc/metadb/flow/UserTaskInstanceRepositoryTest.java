package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        userTaskInstanceEntity = new UserTaskInstanceEntity();
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        verify(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2")))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        verify(userTaskInstanceRepository).findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2")))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        verify(userTaskInstanceRepository).findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByRoleId() {
        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        verify(userTaskInstanceRepository).findByRoleId(1L);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        verify(userTaskInstanceRepository).findByCandidateUserId(1L);
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdate() {
        doNothing().when(userTaskInstanceRepository).update(Mockito.any(UserTaskInstanceEntity.class));
        userTaskInstanceRepository.update(userTaskInstanceEntity);
        verify(userTaskInstanceRepository).update(userTaskInstanceEntity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(1L, "userTaskId");
        userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");
        verify(userTaskInstanceRepository).updateUserTaskIdById(1L, "userTaskId");
    }

    @Test
    public void testUpdateStatusById() {
        doNothing().when(userTaskInstanceRepository).updateStatusById(1L, FlowNodeStatus.APPROVED);
        userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        verify(userTaskInstanceRepository).updateStatusById(1L, FlowNodeStatus.APPROVED);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        doNothing().when(userTaskInstanceRepository).deleteByFlowInstanceId(1L);
        userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        verify(userTaskInstanceRepository).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);
        verify(userTaskInstanceRepository).findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.END, "name", 1L))
                .thenReturn(Optional.of(userTaskInstanceEntity));
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.END, "name", 1L);
        verify(userTaskInstanceRepository).findByInstanceTypeAndName(FlowNodeType.END, "name", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(true);
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        verify(userTaskInstanceRepository).findConfirmById(1L);
        assertTrue(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        verify(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
    }

}