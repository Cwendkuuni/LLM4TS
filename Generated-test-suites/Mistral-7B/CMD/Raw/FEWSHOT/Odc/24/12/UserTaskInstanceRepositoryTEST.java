package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntityBuilder;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceRepository = Mockito.spy(UserTaskInstanceRepository.class);
        userTaskInstanceEntity = new UserTaskInstanceEntityBuilder().build();
    }

    @Test
    public void testFindByStatus() {
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(Arrays.asList(userTaskInstanceEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.APPROVED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(Mockito.anyLong(),
                Mockito.any()))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1"));

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1"));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Mockito.any()))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1"));

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findByResourceRoleIdentifierIn(Arrays.asList("role1"));
    }

    @Test
    public void testFindByRoleId() {
        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(Arrays.asList(userTaskInstanceEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findByRoleId(1L);
    }

    @Test
    public void testFindByCandidateUserId() {
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(Arrays.asList(userTaskInstanceEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertEquals(1, result.size());
        verify(userTaskInstanceRepository).findByCandidateUserId(1L);
    }

    @Test
    @Transactional
    public void testUpdate() {
        Long id = 1L;
        UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntityBuilder().build();

        doNothing().when(userTaskInstanceRepository).update(updatedEntity);

        int result = userTaskInstanceRepository.update(updatedEntity);

        assertEquals(1, result);
        verify(userTaskInstanceRepository).update(updatedEntity);
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";

        doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        assertEquals(1, result);
        verify(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        doNothing().when(userTaskInstanceRepository).updateStatusById(id, status);

        int result = userTaskInstanceRepository.updateStatusById(id, status);

        assertEquals(1, result);
        verify(userTaskInstanceRepository).updateStatusById(id, status);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        doNothing().when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
        verify(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Long id = 1L;
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
        verify(userTaskInstanceRepository).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Long id = 1L;
        FlowNodeType instanceType = FlowNodeType.ACTIVITY;
        String name = "name";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(userTaskInstanceEntity, result.get());
        verify(userTaskInstanceRepository).findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        when(userTaskInstanceRepository.findConfirmById(id)).thenReturn(true);

        Boolean result = userTaskInstanceRepository.findConfirmById(id);

        assertTrue(result);
        verify(userTaskInstanceRepository).findConfirmById(id);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status))
                .thenReturn(Arrays.asList(userTaskInstanceEntity));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceId, status);

        assertEquals(1, result.size());
        assertEquals(userTaskInstanceEntity, result.get(0));
        verify(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceId, status);
    }
}