package com.oceanbase.odc.service.flow.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepositoryImpl;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository repository;
    private UserTaskInstanceEntity userTaskInstance1;
    private UserTaskInstanceEntity userTaskInstance2;

    @Before
    public void setup() {
        repository = new UserTaskInstanceRepositoryImpl();
        UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setId(1L);
        userTaskInstance1.setFlowInstanceId(1L);
        userTaskInstance1.setStatus(FlowNodeStatus.APPROVED);
        UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setId(2L);
        userTaskInstance2.setFlowInstanceId(1L);
        userTaskInstance2.setStatus(FlowNodeStatus.PENDING);
        when(repository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(Arrays.asList(userTaskInstance1));
        when(repository.findByStatus(FlowNodeStatus.PENDING)).thenReturn(Arrays.asList(userTaskInstance2));
        when(repository.findByFlowInstanceId(1L)).thenReturn(Arrays.asList(userTaskInstance1, userTaskInstance2));
        when(repository.findByCandidateUserId(1L)).thenReturn(Collections.emptyList());
        when(repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L))).thenReturn(Arrays.asList(userTaskInstance1));
        when(repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("testRole"))).thenReturn(Arrays.asList(userTaskInstance1));
        when(repository.findByResourceRoleIdentifierIn(Arrays.asList("testRole"))).thenReturn(Collections.emptyList());
        when(repository.findByRoleId(1L)).thenReturn(Collections.emptyList());
        when(repository.update(Mockito.any())).thenAnswer(invocation -> 1);
        when(repository.updateUserTaskIdById(1L, "testUserTaskId")).thenAnswer(invocation -> 1);
        when(repository.updateStatusById(1L, FlowNodeStatus.APPROVED)).thenAnswer(invocation -> 1);
        when(repository.deleteByFlowInstanceId(1L)).thenAnswer(invocation -> 1);
        when(repository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "testActivityId", 1L)).thenReturn(Optional.empty());
        when(repository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "testName", 1L)).thenReturn(Optional.empty());
        when(repository.findConfirmById(1L)).thenReturn(false);
        when(repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED))
                .thenReturn(Arrays.asList(userTaskInstance1));
    }

    @After
    public void teardown() {
        reset(repository);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertEquals(userTaskInstance1, result.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<UserTaskInstanceEntity> result = repository.findByFlowInstanceId(1L);
        assertEquals(2, result.size());
        assertEquals(userTaskInstance1, result.get(0));
        assertEquals(userTaskInstance2, result.get(1));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L));
        assertEquals(1, result.size());
        assertEquals(userTaskInstance1, result.get(0));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("testRole"));
        assertEquals(1, result.size());
        assertEquals(userTaskInstance1, result.get(0));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(Arrays.asList("testRole"));
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = repository.findByRoleId(1L);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(1L);
        assertEquals(0, result.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        repository.update(entity);
        verify(repository, times(1)).update(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "testUserTaskId";
        repository.updateUserTaskIdById(id, userTaskId);
        verify(repository, times(1)).updateUserTaskIdById(id, userTaskId);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        repository.updateStatusById(id, status);
        verify(repository, times(1)).updateStatusById(id, status);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        repository.deleteByFlowInstanceId(instanceId);
        verify(repository, times(1)).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(FlowNodeType.USER_TASK, "testActivityId", 1L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "testName", 1L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = repository.findConfirmById(1L);
        assertFalse(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.APPROVED);
        assertEquals(1, result.size());
        assertEquals(userTaskInstance1, result.get(0));
    }

}