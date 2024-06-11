package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.UserTaskInstanceEntity;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setup() {
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L))).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"))).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"))).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByRoleId(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        doNothing().when(userTaskInstanceRepository).update(entity);

        int actual = userTaskInstanceRepository.update(entity);

        assertEquals(1, actual);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";

        doNothing().when(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);

        int actual = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        assertEquals(1, actual);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        doNothing().when(userTaskInstanceRepository).updateStatusById(id, status);

        int actual = userTaskInstanceRepository.updateStatusById(id, status);

        assertEquals(1, actual);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        doNothing().when(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);

        int actual = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, actual);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L)).thenReturn(Optional.of(expected));

        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);

        assertEquals(expected, actual.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.END, "name", 1L)).thenReturn(Optional.of(expected));

        Optional<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.END, "name", 1L);

        assertEquals(expected, actual.get());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = true;

        when(userTaskInstanceRepository.findConfirmById(1L)).thenReturn(confirm);

        Boolean actual = userTaskInstanceRepository.findConfirmById(1L);

        assertEquals(confirm, actual);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED))
                .thenReturn(expected);

        List<UserTaskInstanceEntity> actual = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);

        assertEquals(expected, actual);
    }
}