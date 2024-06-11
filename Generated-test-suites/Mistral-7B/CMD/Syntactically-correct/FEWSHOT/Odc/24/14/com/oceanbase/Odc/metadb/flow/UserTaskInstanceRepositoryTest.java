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
import com.oceanbase.odc.service.flow.repository.UserTaskInstanceRepository;

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

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);

        assertEquals(expected, result);
        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.APPROVED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L))).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));

        assertEquals(expected, result);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrRoleIds(1L, Arrays.asList(1L, 2L));
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"))).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));

        assertEquals(expected, result);
        verify(userTaskInstanceRepository, times(1)).findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByRoleId(1L)).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);

        assertEquals(expected, result);
        verify(userTaskInstanceRepository, times(1)).findByRoleId(1L);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertEquals(expected, result);
        verify(userTaskInstanceRepository, times(1)).findByCandidateUserId(1L);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.update(entity)).thenReturn(1);

        int result = userTaskInstanceRepository.update(entity);

        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).update(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        when(userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId")).thenReturn(1);

        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");

        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).updateUserTaskIdById(1L, "userTaskId");
    }

    @Test
    public void testUpdateStatusById() {
        when(userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED)).thenReturn(1);

        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);

        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).updateStatusById(1L, FlowNodeStatus.APPROVED);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(userTaskInstanceRepository.deleteByFlowInstanceId(1L)).thenReturn(1);

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, result);
        verify(userTaskInstanceRepository, times(1)).deleteByFlowInstanceId(1L);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L)).thenReturn(Optional.of(expected));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);

        assertEquals(expected, result.get());
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndActivityId(FlowNodeType.START, "activityId", 1L);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity expected = new UserTaskInstanceEntity();
        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L)).thenReturn(Optional.of(expected));

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name", 1L);

        assertEquals(expected, result.get());
        verify(userTaskInstanceRepository, times(1)).findByInstanceTypeAndName(FlowNodeType.START, "name", 1L);
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);

        assertFalse(result);
        verify(userTaskInstanceRepository, times(1)).findConfirmById(1L);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED)).thenReturn(expected);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);

        assertEquals(expected, result);
        verify(userTaskInstanceRepository, times(1)).findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.APPROVED);
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        when(userTaskInstanceRepository.findAll()).thenReturn(new PageImpl<>(expected, Pageable.unpaged(), expected.size()));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findAll();

        assertEquals(expected, result);
        verify(userTaskInstanceRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllWithPageable() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Pageable pageable = Mockito.mock(Pageable.class);
        when(userTaskInstanceRepository.findAll(pageable)).thenReturn(new PageImpl<>(expected, pageable, expected.size()));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findAll(pageable);

        assertEquals(expected, result);
        verify(userTaskInstanceRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testFindAllWithSort() {
        List<UserTaskInstanceEntity> expected = Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity(), new UserTaskInstanceEntity());
        Sort sort = Sort.by(Sort.Order.desc("id"));
        when(userTaskInstanceRepository.findAll(sort)).thenReturn(new PageImpl<>(expected, Collections.emptyPageable(), expected.size()));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findAll(sort);

        assertEquals(expected, result);
        verify(userTaskInstanceRepository, times(1)).findAll(sort);
    }

}